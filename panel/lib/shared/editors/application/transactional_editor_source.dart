import "dart:async";

import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "transactional_editor_persistence.dart";
part "transactional_editor_commit.dart";
part "editor_batch.dart";
part "editor_resource_save.dart";
part "editor_batch_recovery.dart";
part "transactional_editor_interactions.dart";
part "transactional_editor_reconciliation.dart";

typedef EditorCommitter =
    Future<TypedMutationResult> Function(EditorCommit commit);
typedef EditorMutationValidator =
    EditorMutationResult Function(DataPath path, DataValue value);
typedef EditorRealmActionExecutor =
    Future<RealmCommandResult> Function(
      RealmAction action,
      ExpressionContext context,
    );

final class TransactionalEditorSource extends ChangeNotifier
    implements EditorSource {
  TransactionalEditorSource({
    required EditorDocument document,
    this._commit,
    this._resource,
    this.workspace,
    this._snapshot,
    this._validate,
    this._validateDraft,
    this.commitPolicy = EditorCommitPolicy.autosaveChanges,
    this._scheduler = const TimerEditorDelayScheduler(),
    EditorJitterSource? jitter,
    this.debounce = const Duration(milliseconds: 250),
    this.onDeleted,
  }) : _document = document,
       _draft = document.confirmedValue,
       _jitter = jitter ?? RandomEditorJitterSource();

  EditorDocument _document;
  DataValue _draft;
  final EditorCommitter? _commit;
  EditableResource? _resource;
  EditableResource? get resource => _resource;
  final LocalWork? workspace;
  EditorSnapshot? _snapshot;
  final EditorMutationValidator? _validate;
  final List<TypeDiagnostic> Function(DataValue)? _validateDraft;

  @override
  final EditorCommitPolicy commitPolicy;

  @override
  bool get hasWork =>
      _states.dirtyPaths.isNotEmpty ||
      _activeCommit != null ||
      _unresolved != null;

  @override
  List<TypeDiagnostic> get draftDiagnostics =>
      _snapshot?.validateDraft(_draft) ??
      _validateDraft?.call(_draft) ??
      const [];

  final EditorDelayScheduler _scheduler;
  final EditorJitterSource _jitter;
  final Duration debounce;
  final VoidCallback? onDeleted;
  final EditorReconciler _reconciler = const EditorReconciler();
  final EditorPathStates _states = EditorPathStates();
  EditorScheduledTask? _debounceTask;
  EditorScheduledTask? _retryTask;
  Future<TypedMutationResult>? _activeCommit;
  _UnresolvedCommit? _unresolved;
  EditorBatch? _rejectedBatch;
  bool _deleted = false;
  bool _disposed = false;
  int _localRevision = 0;
  int _generation = 0;
  final List<_PendingStructuralMutation> _pendingMutations = [];

  @override
  TypeExpression get rootType => document.rootType;

  @override
  TypeCatalog get typeCatalog => document.typeCatalog;

  @override
  bool get readOnly =>
      document.readOnly ||
      (commitPolicy == EditorCommitPolicy.applyResource &&
          (_activeCommit != null || _unresolved != null));

  @override
  EditorDocument get document => _document;

  @override
  EditorValue value(DataPath path) {
    if (_deleted) {
      return EditorValue.invalid([_diagnostic("Deleted elsewhere", path)]);
    }
    return _draft.readEditorValue(path);
  }

  @override
  EditorMutationResult update(
    DataPath path,
    DataValue value, {
    EditorStructuralMutation? structuralMutation,
  }) {
    final validation = validate(path, value);
    if (validation is! AppliedEditorMutation) return validation;
    final replaced = path.replace(_draft, validation.value);
    if (replaced case TypeFailure(:final diagnostics)) {
      return EditorMutationResult.invalid(diagnostics);
    }

    _draft = replaced.valueOrNull!;

    _localRevision++;
    _pendingMutations.add(
      _PendingStructuralMutation(
        _localRevision,
        structuralMutation ?? EditorSetValue(path, validation.value),
      ),
    );

    _states.markEdited(path);

    _notify();

    _scheduleAutoFlush();
    return validation;
  }

  @override
  EditorMutationResult validate(DataPath path, DataValue value) {
    if (_disposed) {
      return EditorMutationResult.invalid([_diagnostic("Editor is disposed")]);
    }
    if (_deleted) return EditorMutationResult.invalid([_deletedDiagnostic()]);
    if (readOnly) {
      return EditorMutationResult.invalid([
        _diagnostic("The editor is read only", path),
      ]);
    }
    return _snapshot?.validate(path, value) ??
        _validate?.call(path, value) ??
        _document.rootType.validateEditorMutation(
          path,
          value,
          registry: TypeRegistry(_document.typeCatalog),
        );
  }

  @override
  void refreshDocument(EditorDocument document) => _refreshDocument(document);

  @override
  EditorInteractionSession beginInteraction(DataPath path) {
    for (final gate in _states.takeGates()) {
      if (_pathsOverlap(path, gate.path)) {
        if (gate is _Interaction) gate.close();
      } else {
        _states.setGate(gate.path, gate);
      }
    }
    final interaction = _Interaction(
      source: this,
      path: path,
      origin: path.read(_draft).valueOrNull,
      startingRevision: _localRevision,
    );
    if (_disposed || _deleted) {
      interaction.close();
      return interaction;
    }
    _states.setGate(path, interaction);
    return interaction;
  }

  @override
  EditorSaveState saveState(DataPath path) {
    if (_deleted) {
      return EditorSaveState(
        phase: EditorSavePhase.deletedElsewhere,
        path: path,
      );
    }
    if (_unresolved case final unresolved?) {
      return EditorSaveState(
        phase: _activeCommit == null
            ? EditorSavePhase.uncertain
            : EditorSavePhase.saving,
        path: path,
        replayAvailable: unresolved.result.replay != null,
        submissionId: unresolved.result.submissionId,
        diagnostics: [_diagnostic(unresolved.result.message)],
      );
    }
    return _states.saveState(path);
  }

  @override
  Future<TypedMutationResult> flush({Set<DataPath>? paths}) async {
    if (_disposed) return _unavailable("Editor is disposed");
    if (_deleted) return _unavailable("Deleted elsewhere");

    while (_activeCommit != null) {
      await _activeCommit!;
      if (_disposed) return _unavailable("Editor is disposed");
      if (_deleted) return _unavailable("Deleted elsewhere");
    }
    if (_unresolved case final unresolved?) return _replayCommit(unresolved);
    if (_rejectedBatch case final batch?) {
      if (paths == null ||
          paths.any(
            (path) => batch._paths[this]!.any(
              (changed) => _pathsOverlap(path, changed),
            ),
          )) {
        return (await batch._retryRejected())[this] ?? _settledResult();
      }
    }
    final selected = _states.flushCandidates(
      commitPolicy == EditorCommitPolicy.applyResource ? null : paths,
    );
    return selected.isEmpty ? _settledResult() : _runCommit(selected);
  }

  TypedMutationResult _settledResult() {
    if (_states.hasConflicts) {
      return _unavailable("Conflicting fields require a choice");
    }
    return TypedMutationResult.success(
      revision: _document.revision,
      value: _document.confirmedValue,
    );
  }

  @override
  void acceptRemote({required int revision, required DataValue value}) =>
      _acceptRemote(revision: revision, value: value);

  @override
  void useRemote(DataPath path) {
    if (_disposed || _unresolved != null) return;
    final remote = path.read(_document.confirmedValue).valueOrNull;
    if (remote == null) return;
    _draft = path.replace(_draft, remote).valueOrNull ?? _draft;
    _pendingMutations.removeWhere(
      (pending) => _pathsOverlap(path, pending.mutation.path),
    );

    _states.adoptRemote(path, EditorSavePhase.saved);
    _notify();
  }

  @override
  Future<TypedMutationResult> keepLocal(DataPath path) {
    if (_disposed) return Future.value(_unavailable("Editor is disposed"));
    if (_unresolved case final unresolved?) {
      return Future.value(unresolved.result);
    }
    _states.resolveConflictLocally(path);
    _notify();
    if (commitPolicy == EditorCommitPolicy.applyResource) {
      return Future.value(_settledResult());
    }
    return flush(paths: {path});
  }

  @override
  void acceptRemoteDeletion() {
    if (_disposed || _deleted) return;
    _deleted = true;
    _pendingMutations.clear();
    _unresolved = null;

    _generation++;

    _cancelScheduledTasks();

    _closeGates();

    _notify();
    onDeleted?.call();
  }

  void _notify() {
    if (!_disposed) notifyListeners();
  }

  @override
  void discardDraft() => _discardDraft();

  @override
  void dispose() {
    if (_disposed) return;
    _disposed = true;
    _generation++;
    _cancelScheduledTasks();

    _closeGates();
    super.dispose();
  }
}
