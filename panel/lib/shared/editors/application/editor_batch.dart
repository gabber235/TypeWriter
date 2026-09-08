part of "transactional_editor_source.dart";

typedef EditorBatchSender =
    Future<Map<TransactionalEditorSource, TypedMutationResult>> Function(
      Map<TransactionalEditorSource, EditorCommit> commits,
    );

/// Reserves all participating drafts before capturing one atomic submission.
final class EditorBatch {
  EditorBatch._(this._commits, this._mutations, this._send);
  final EditorBatchSender _send;
  final Map<TransactionalEditorSource, EditorCommit> _commits;
  final Map<TransactionalEditorSource, List<_PendingStructuralMutation>>
  _mutations;
  Map<TransactionalEditorSource, TypedMutationResult> _results = {};
  Future<Map<TransactionalEditorSource, TypedMutationResult>>? _recovery;

  static Future<Map<TransactionalEditorSource, TypedMutationResult>> submit({
    required Map<TransactionalEditorSource, Map<DataPath, DataValue>> changes,
    required EditorBatchSender send,
  }) async {
    while (changes.keys.any((source) => source._activeCommit != null)) {
      await Future.wait([
        for (final source in changes.keys)
          if (source._activeCommit case final active?) active,
      ]);
    }
    final invalid = <TypeDiagnostic>[];
    final accepted = <TransactionalEditorSource, Map<DataPath, DataValue>>{};
    for (final entry in changes.entries) {
      if (entry.value.isEmpty ||
          entry.value.keys.any(
            (path) => entry.value.keys.any(
              (other) => path != other && _pathsOverlap(path, other),
            ),
          )) {
        invalid.add(_diagnostic("Batch paths must be present and disjoint"));
      }
      if (entry.key._unresolved != null || entry.key._states.hasConflicts) {
        return {
          for (final source in changes.keys)
            source: _unavailable(
              "Resolve the previous save before changing this batch",
            ),
        };
      }
      accepted[entry.key] = {};
      for (final change in entry.value.entries) {
        final result = entry.key.validate(change.key, change.value);
        if (result is AppliedEditorMutation) {
          accepted[entry.key]![change.key] = result.value;
        } else {
          invalid.addAll(
            result is InvalidEditorMutation
                ? result.diagnostics
                : [_diagnostic("The edit conflicts")],
          );
        }
      }
    }
    if (invalid.isNotEmpty)
      return {
        for (final source in changes.keys)
          source: TypedMutationResult.invalid(invalid),
      };
    final completions = {
      for (final source in changes.keys)
        source: Completer<TypedMutationResult>(),
    };
    for (final source in changes.keys) {
      source._activeCommit = completions[source]!.future;
      source._cancelScheduledTasks();
    }
    final commits = <TransactionalEditorSource, EditorCommit>{};
    final mutations =
        <TransactionalEditorSource, List<_PendingStructuralMutation>>{};
    for (final entry in changes.entries) {
      final source = entry.key;
      for (final change in accepted[source]!.entries) {
        source._draft = change.key
            .replace(source._draft, change.value)
            .valueOrNull!;
        source._localRevision++;
        source._pendingMutations.add(
          _PendingStructuralMutation(
            source._localRevision,
            EditorSetValue(change.key, change.value),
          ),
        );
        source._states.markEdited(change.key);
      }
      final paths = entry.value.keys.toSet();
      mutations[source] = source._mutationsFor(paths);
      commits[source] = EditorCommit(
        expectedRevision: source.document.revision,
        localRevision: source._localRevision,
        baseValue: source.document.confirmedValue,
        rootValue: source._commitValue(paths),
        changedPaths: paths,
        mutations: mutations[source]!
            .map((pending) => pending.mutation)
            .toList(),
      );
      source._states.markSaving(paths);
      source._notify();
    }
    final batch = EditorBatch._(Map.unmodifiable(commits), mutations, send);
    try {
      batch._results = await send(batch._commits);
    } on Object catch (error, stackTrace) {
      batch._results = {
        for (final source in changes.keys)
          source: TypedMutationResult.uncertain(
            message: "The batch result could not be confirmed",
            cause: error,
            stackTrace: stackTrace,
          ),
      };
    }
    batch._settle();
    for (final source in changes.keys) {
      source._activeCommit = null;
      completions[source]!.complete(batch._results[source]);
      source._notify();
      source._scheduleAutoFlush();
    }
    return batch._results;
  }

  void _settle() {
    for (final entry in _commits.entries) {
      final source = entry.key;
      if (source._disposed || source._deleted) continue;
      final commit = entry.value;
      final result = _results[source] ?? _unavailable("Missing batch result");
      source._states.clearSaving();
      source._unresolved = null;
      source._rejectedBatch =
          result is MutationSuccess || result is MutationUncertain
          ? null
          : this;
      switch (result) {
        case MutationSuccess(:final revision, :final value):
          source._acceptSuccess(
            revision < source.document.revision
                ? source.document.revision
                : revision,
            revision < source.document.revision
                ? source.document.confirmedValue
                : value,
            commit.rootValue,
            commit.changedPaths,
            commit.localRevision,
          );
          source._pendingMutations.removeWhere(_mutations[source]!.contains);
        case MutationUncertain():
          source._unresolved = _UnresolvedCommit(
            commit,
            result.copyWith(
              replay: result.replay == null
                  ? null
                  : () async => (await _retry())[source]!,
            ),
            _mutations[source]!,
          );
        case MutationConflict(:final actualRevision, :final actualValue):
          source.acceptRemote(revision: actualRevision, value: actualValue);
          source._states.fail(
            {
              for (final path in commit.changedPaths)
                if (source.saveState(path).phase != EditorSavePhase.conflict)
                  path,
            },
            [_diagnostic("The batch changed elsewhere")],
          );
        case MutationInvalid(:final diagnostics) ||
            MutationUnavailable(:final diagnostics):
          source._failPaths(commit.changedPaths, diagnostics);
        case MutationPermissionDenied(:final message):
          source._failPaths(commit.changedPaths, [_diagnostic(message)]);
      }
      source._notify();
      source._scheduleAutoFlush();
    }
  }
}
