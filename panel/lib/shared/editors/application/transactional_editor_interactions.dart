part of "transactional_editor_source.dart";

extension _EditorInteractions on TransactionalEditorSource {
  void _cancelDebounce() {
    _debounceTask?.cancel();
    _debounceTask = null;
  }

  void _cancelScheduledTasks() {
    _cancelDebounce();
    _retryTask?.cancel();
    _retryTask = null;
  }

  Future<TypedMutationResult> _commitInteraction(_Interaction interaction) {
    if (!_release(interaction)) {
      return Future.value(_unavailable("Interaction is closed"));
    }
    if (commitPolicy == EditorCommitPolicy.applyResource) {
      return Future.value(_settledResult());
    }
    return flush(paths: {..._states.autoFlushCandidates, interaction.path});
  }

  void _discardDraft() {
    if (_disposed || _activeCommit != null || _unresolved != null) return;
    _cancelScheduledTasks();
    _closeGates();
    _draft = _document.confirmedValue;

    _rejectedBatch = null;

    _pendingMutations.clear();
    for (final path in _states.dirtyPaths) {
      _states.reset(path);
    }
    _notify();
  }

  void _cancelInteraction(_Interaction interaction) {
    if (!_release(interaction)) return;
    if (_disposed || _deleted) return;
    final origin = interaction.origin;
    if (origin != null) {
      _draft = interaction.path.replace(_draft, origin).valueOrNull ?? _draft;
    }
    _pendingMutations.removeWhere(
      (pending) =>
          pending.revision > interaction.startingRevision &&
          _pathsOverlap(interaction.path, pending.mutation.path),
    );

    _states.reset(interaction.path);
    _notify();
  }

  bool _release(_Interaction interaction) {
    if (!interaction.active) return false;
    interaction.close();
    _states.clearGate(interaction.path, interaction);
    return true;
  }

  void _closeGates() {
    for (final gate in _states.takeGates()) {
      if (gate is _Interaction) gate.close();
    }
  }
}

final class _Interaction implements EditorInteractionSession {
  _Interaction({
    required this.source,
    required this.path,
    required this.origin,
    required this.startingRevision,
  });

  final TransactionalEditorSource source;

  @override
  final DataPath path;
  final DataValue? origin;
  final int startingRevision;

  @override
  bool active = true;

  @override
  Future<void> commit() async {
    await source._commitInteraction(this);
  }

  @override
  void cancel() => source._cancelInteraction(this);

  void close() => active = false;
}

TypeDiagnostic _diagnostic(String message, [DataPath path = DataPath.root]) {
  return TypeDiagnostic(
    code: TypeDiagnosticCode.mutationConflict,
    message: message,
    path: path,
  );
}

TypeDiagnostic _deletedDiagnostic() => _diagnostic("Deleted elsewhere");

TypedMutationResult _unavailable(String message) =>
    TypedMutationResult.unavailable([_diagnostic(message)]);

bool _targetWasDeleted(List<TypeDiagnostic> diagnostics) {
  return diagnostics.any(
    (diagnostic) => diagnostic.details.any(
      (detail) => detail.key == "editor.target" && detail.value == "deleted",
    ),
  );
}

final class _PendingStructuralMutation {
  const _PendingStructuralMutation(this.revision, this.mutation);

  final int revision;
  final EditorStructuralMutation mutation;
}

bool _pathsOverlap(DataPath first, DataPath second) {
  final shared = first.segments.length < second.segments.length
      ? first.segments.length
      : second.segments.length;
  for (var index = 0; index < shared; index++) {
    if (first.segments[index] != second.segments[index]) return false;
  }
  return true;
}

const _retryDelays = [
  Duration(milliseconds: 50),
  Duration(milliseconds: 100),
  Duration(milliseconds: 200),
];

final class _UnresolvedCommit {
  const _UnresolvedCommit(this.commit, this.result);
  final EditorCommit commit;
  final MutationUncertain result;
}
