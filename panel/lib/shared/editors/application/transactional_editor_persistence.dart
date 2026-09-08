part of "transactional_editor_source.dart";

extension _EditorPersistence on TransactionalEditorSource {
  void _scheduleAutoFlush() {
    if (_disposed ||
        _deleted ||
        _unresolved != null ||
        commitPolicy == EditorCommitPolicy.applyResource)
      return;
    _cancelDebounce();
    final task = _scheduler.schedule(debounce);
    _debounceTask = task;
    unawaited(_autoFlushAfter(task));
  }

  Future<void> _autoFlushAfter(EditorScheduledTask task) async {
    final completion = await task.completed;
    if (!identical(_debounceTask, task)) return;
    _debounceTask = null;
    if (completion == EditorTaskCompletion.cancelled || _disposed || _deleted) {
      return;
    }
    final candidates = _states.autoFlushCandidates;
    if (candidates.isEmpty) return;
    await flush(paths: candidates);
  }

  Future<TypedMutationResult> _runCommit(Set<DataPath> selected) async {
    final commit = _persist(selected);
    _activeCommit = commit;
    try {
      return await commit;
    } finally {
      _activeCommit = null;
      _notify();
      if (!_disposed && !_deleted && _states.autoFlushCandidates.isNotEmpty) {
        _scheduleAutoFlush();
      }
    }
  }

  Future<TypedMutationResult> _persist(Set<DataPath> paths) async {
    var attempt = 0;
    var activePaths = paths;
    try {
      while (true) {
        if (_disposed) return _unavailable("Editor is disposed");
        final generation = _generation;
        final rootValue = _commitValue(activePaths);
        final pendingMutations = _mutationsFor(activePaths);
        _states.markSaving(activePaths);
        _notify();
        final captured = EditorCommit(
          expectedRevision: _document.revision,
          localRevision: _localRevision,
          rootValue: rootValue,
          baseValue: _document.confirmedValue,
          changedPaths: Set.unmodifiable(activePaths),
          mutations: pendingMutations
              .map((pending) => pending.mutation)
              .toList(),
        );
        final result = await _send(captured);
        if (_disposed || _deleted || generation != _generation) {
          return _unavailable("The commit result is stale");
        }
        switch (result) {
          case MutationUncertain():
            _unresolved = _UnresolvedCommit(captured, result, pendingMutations);
            return result;
          case MutationSuccess(:final revision, :final value):
            if (revision < _document.revision) {
              final retryPaths = _states.flushCandidates(activePaths);
              if (retryPaths.isEmpty) return result;
              if (!await _waitForRetry(retryPaths, attempt++)) return result;
              activePaths = retryPaths;
              continue;
            }
            if (revision == _document.revision &&
                value != _document.confirmedValue) {
              acceptRemote(revision: revision, value: value);
              _failPaths(activePaths, [
                _diagnostic("Different values share the same revision"),
              ]);
              return result;
            }
            _acceptSuccess(
              revision,
              value,
              rootValue,
              activePaths,
              captured.localRevision,
            );
            _pendingMutations.removeWhere(pendingMutations.contains);
            return result;
          case MutationConflict(:final actualRevision, :final actualValue):
            acceptRemote(revision: actualRevision, value: actualValue);
            if (commitPolicy == EditorCommitPolicy.applyResource) return result;
            final retryPaths = _states.flushCandidates(activePaths);
            if (retryPaths.isEmpty) return result;
            if (!await _waitForRetry(retryPaths, attempt++)) return result;
            activePaths = retryPaths;
          case MutationInvalid(:final diagnostics) ||
              MutationUnavailable(:final diagnostics):
            if (_targetWasDeleted(diagnostics)) {
              acceptRemoteDeletion();
              return result;
            }
            _failPaths(activePaths, diagnostics);
            return result;
          case MutationPermissionDenied(:final message):
            _failPaths(activePaths, [_diagnostic(message)]);
            return result;
        }
      }
    } finally {
      if (!_disposed) {
        _states.clearSaving();
        _notify();
      }
    }
  }

  Future<TypedMutationResult> _send(EditorCommit commit) async {
    try {
      return await _commit(commit);
    } on Object catch (error, stackTrace) {
      return TypedMutationResult.uncertain(
        message: "The save result could not be confirmed",
        cause: error,
        stackTrace: stackTrace,
      );
    }
  }

  Future<TypedMutationResult> _replayCommit(
    _UnresolvedCommit unresolved,
  ) async {
    final replay = unresolved.result.replay;
    if (replay == null) return unresolved.result;
    final operation = Future<TypedMutationResult>.sync(replay);
    _activeCommit = operation;
    _notify();
    try {
      final result = await operation;
      if (_disposed || _deleted) return result;
      switch (result) {
        case MutationUncertain():
          _unresolved = _UnresolvedCommit(
            unresolved.commit,
            result,
            unresolved.mutations,
          );
        case MutationSuccess(:final revision, :final value):
          _unresolved = null;
          final commit = unresolved.commit;
          _acceptSuccess(
            revision < _document.revision ? _document.revision : revision,
            revision < _document.revision ? _document.confirmedValue : value,
            commit.rootValue,
            commit.changedPaths,
            commit.localRevision,
          );
          _pendingMutations.removeWhere(unresolved.mutations.contains);
        case MutationConflict(:final actualRevision, :final actualValue):
          _unresolved = null;
          acceptRemote(revision: actualRevision, value: actualValue);
        case MutationInvalid(:final diagnostics) ||
            MutationUnavailable(:final diagnostics):
          _unresolved = null;
          _failPaths(unresolved.commit.changedPaths, diagnostics);
        case MutationPermissionDenied(:final message):
          _unresolved = null;
          _failPaths(unresolved.commit.changedPaths, [_diagnostic(message)]);
      }
      return result;
    } on Object catch (error, stackTrace) {
      final result = MutationUncertain(
        message: "The save result could not be confirmed",
        cause: error,
        stackTrace: stackTrace,
        replay: replay,
        submissionId: unresolved.result.submissionId,
      );
      _unresolved = _UnresolvedCommit(
        unresolved.commit,
        result,
        unresolved.mutations,
      );
      return result;
    } finally {
      _activeCommit = null;
      _notify();
      _scheduleAutoFlush();
    }
  }

  Future<bool> _waitForRetry(Set<DataPath> paths, int attempt) async {
    if (attempt >= _retryDelays.length) {
      _states.markContended(paths);
      _notify();
      return false;
    }
    final base = _retryDelays[attempt];
    final jitter = _jitter.next(
      Duration(microseconds: base.inMicroseconds ~/ 2),
    );
    final task = _scheduler.schedule(base + jitter);
    _retryTask?.cancel();
    _retryTask = task;
    final completion = await task.completed;
    if (identical(_retryTask, task)) _retryTask = null;
    return completion == EditorTaskCompletion.executed &&
        !_disposed &&
        !_deleted;
  }

  DataValue _commitValue(Set<DataPath> paths) {
    if (commitPolicy == EditorCommitPolicy.applyResource) return _draft;
    var value = _document.confirmedValue;
    for (final path in paths) {
      final local = path.read(_draft).valueOrNull;
      if (local != null) {
        value = path.replace(value, local).valueOrNull ?? value;
      }
    }
    return value;
  }

  void _failPaths(Set<DataPath> paths, List<TypeDiagnostic> diagnostics) {
    _states.fail(paths, diagnostics);
    _notify();
  }

  List<_PendingStructuralMutation> _mutationsFor(Set<DataPath> paths) => [
    for (final pending in _pendingMutations)
      if (paths.any((path) => _pathsOverlap(path, pending.mutation.path)))
        pending,
  ];
}
