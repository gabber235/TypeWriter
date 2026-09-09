part of "transactional_editor_source.dart";

extension _EditorPersistence on TransactionalEditorSource {
  void _scheduleAutoFlush() {
    if (_disposed ||
        _deleted ||
        _unresolved != null ||
        commitPolicy == EditorCommitPolicy.applyResource) {
      return;
    }
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
    if (resource != null) return _persistResource(paths);
    var attempt = 0;
    var activePaths = paths;
    try {
      while (true) {
        if (_disposed) return _unavailable("Editor is disposed");
        if (_disposed || _deleted) {
          return _unavailable("The resource is no longer available");
        }
        if (document.readOnly) {
          final diagnostics = [
            _diagnostic("The resource is not ready to save"),
          ];
          _failPaths(activePaths, diagnostics);
          return TypedMutationResult.unavailable(diagnostics);
        }
        if (commitPolicy == EditorCommitPolicy.applyResource &&
            _states.hasConflicts) {
          return _unavailable("Conflicting fields require a choice");
        }
        final diagnostics = _saveDiagnostics();
        if (diagnostics.isNotEmpty) {
          _failPaths(activePaths, diagnostics);
          return TypedMutationResult.invalid(diagnostics);
        }
        activePaths = _states.flushCandidates(
          commitPolicy == EditorCommitPolicy.applyResource ? null : activePaths,
        );
        if (activePaths.isEmpty) return _settledResult();
        final generation = _generation;
        final captured = captureCommit(activePaths);
        _states.markSaving(activePaths);
        _notify();
        final result = await _send(captured);
        if (_disposed || _deleted || generation != _generation) {
          return _unavailable("The commit result is stale");
        }
        if (result is MutationUncertain) {
          _unresolved = _UnresolvedCommit(captured, result);
          return result;
        }
        acceptCommit(captured, result);
        if (result is! MutationConflict ||
            commitPolicy == EditorCommitPolicy.applyResource) {
          return result;
        }
        final retryPaths = _states.flushCandidates(activePaths);
        if (retryPaths.isEmpty) return result;
        if (!await _waitForRetry(retryPaths, attempt++)) return result;
        activePaths = retryPaths;
      }
    } finally {
      if (!_disposed) {
        _states.clearSaving();
        _notify();
      }
    }
  }

  List<TypeDiagnostic> _saveDiagnostics() {
    final diagnostics = [...draftDiagnostics];
    final validation = document.rootType.validateEditorMutation(
      DataPath.root,
      _draft,
      registry: TypeRegistry(document.typeCatalog),
    );
    if (validation is InvalidEditorMutation) {
      diagnostics.addAll(validation.diagnostics);
    }
    return diagnostics;
  }

  Future<TypedMutationResult> _send(EditorCommit commit) async {
    try {
      return await _commit!(commit);
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
      if (result is MutationUncertain) {
        _unresolved = _UnresolvedCommit(unresolved.commit, result);
      } else {
        _unresolved = null;
        acceptCommit(unresolved.commit, result);
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
      _unresolved = _UnresolvedCommit(unresolved.commit, result);
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
