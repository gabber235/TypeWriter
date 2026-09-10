part of "transactional_editor_source.dart";

typedef EditorBatchSender =
    Future<Map<TransactionalEditorSource, TypedMutationResult>> Function(
      Map<TransactionalEditorSource, EditorCommit> commits,
    );

/// Reserves all participating drafts before capturing one atomic submission.
final class EditorBatch {
  EditorBatch._(
    this._commits,
    this._send, {
    Map<TransactionalEditorSource, Set<DataPath>>? paths,
  }) : _paths =
           paths ??
           {
             for (final entry in _commits.entries)
               entry.key: entry.value.changedPaths.toSet(),
           };
  final EditorBatchSender? _send;
  final Map<TransactionalEditorSource, EditorCommit> _commits;
  final Map<TransactionalEditorSource, Set<DataPath>> _paths;
  Map<TransactionalEditorSource, TypedMutationResult> _results = {};
  Future<Map<TransactionalEditorSource, TypedMutationResult>>? _recovery;

  static Future<Map<TransactionalEditorSource, TypedMutationResult>> submit({
    required Map<TransactionalEditorSource, Map<DataPath, DataValue>> changes,
    EditorBatchSender? send,
  }) async {
    while (changes.keys.any((source) => source._activeCommit != null)) {
      await Future.wait([
        for (final source in changes.keys) ?source._activeCommit,
      ]);
    }
    final resourceBatch = changes.keys.any((source) => source.resource != null);
    if (resourceBatch &&
        changes.keys.any((source) => source.resource == null)) {
      throw StateError(
        "A resource transaction cannot include local editor callbacks",
      );
    }
    if (!resourceBatch && send == null) {
      throw StateError("A local editor batch requires a sender");
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
    if (invalid.isNotEmpty) {
      return {
        for (final source in changes.keys)
          source: TypedMutationResult.invalid(invalid),
      };
    }
    final completions = {
      for (final source in changes.keys)
        source: Completer<TypedMutationResult>(),
    };
    for (final source in changes.keys) {
      source._activeCommit = completions[source]!.future;
      source._cancelScheduledTasks();
    }

    final commits = <TransactionalEditorSource, EditorCommit>{};
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
    }
    if (resourceBatch) {
      final saved = await _ResourceSave.run({
        for (final entry in changes.entries)
          entry.key: entry.value.keys.toSet(),
      });
      final batch =
          EditorBatch._(
              saved.commits,
              send,
              paths: {
                for (final entry in changes.entries)
                  entry.key: entry.value.keys.toSet(),
              },
            )
            .._results = saved.results
            .._settle();
      for (final source in changes.keys) {
        final result = saved.results[source] ?? source._settledResult();
        if (!saved.commits.containsKey(source) && result is! MutationSuccess) {
          source._rejectedBatch = batch;
          source._failPaths(changes[source]!.keys.toSet(), [
            _diagnostic("The batch could not be prepared"),
          ]);
        }
        source._activeCommit = null;
        completions[source]!.complete(result);
        source._notify();
      }
      return {
        for (final source in changes.keys)
          source: saved.results[source] ?? source._settledResult(),
      };
    }
    for (final entry in changes.entries) {
      final source = entry.key;
      final paths = source._states.flushCandidates(entry.value.keys.toSet());
      if (paths.isEmpty) continue;
      commits[source] = source.captureCommit(paths);

      source._states.markSaving(paths);
      source._notify();
    }

    final batch = EditorBatch._(Map.unmodifiable(commits), send);
    try {
      batch._results = commits.isEmpty ? {} : await send!(batch._commits);
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
      completions[source]!.complete(
        batch._results[source] ?? source._settledResult(),
      );
      source
        .._notify()
        .._scheduleAutoFlush();
    }
    return {
      for (final source in changes.keys)
        source: batch._results[source] ?? source._settledResult(),
    };
  }

  void _settle() {
    for (final entry in _commits.entries) {
      final source = entry.key;
      if (source._disposed || source._deleted) continue;
      final commit = entry.value;
      final result = _results[source] ?? _unavailable("Missing batch result");

      source._states.clearSaving();
      source
        .._unresolved = null
        .._rejectedBatch =
            result is MutationSuccess || result is MutationUncertain
            ? null
            : this;
      if (result is MutationUncertain) {
        source._unresolved = _UnresolvedCommit(
          commit,
          result.copyWith(
            replay: result.replay == null
                ? null
                : () async => (await _retry())[source]!,
          ),
        );
      } else {
        source.acceptCommit(commit, result);
      }
      source
        .._notify()
        .._scheduleAutoFlush();
    }
  }
}
