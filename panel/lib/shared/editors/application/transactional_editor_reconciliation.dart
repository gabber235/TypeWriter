part of "transactional_editor_source.dart";

extension _EditorReconciliation on TransactionalEditorSource {
  void _refreshDocument(EditorDocument document) {
    if (_disposed) return;
    if (_document.hasSameContent(document)) return;
    acceptRemote(revision: document.revision, value: document.confirmedValue);
    final refreshed = _document.copyWith(
      rootType: document.rootType,
      typeCatalog: document.typeCatalog,

      mergePolicies: document.mergePolicies,

      diagnostics: document.diagnostics,
      readOnly: document.readOnly,
    );

    if (_document.hasSameContent(refreshed)) return;

    _document = refreshed;
    _notify();
  }

  void _acceptRemote({required int revision, required DataValue value}) {
    if (_disposed || _deleted || revision < _document.revision) return;
    if (revision == _document.revision) {
      if (value == _document.confirmedValue) return;
      _document = _document.copyWith(
        diagnostics: [
          ..._document.diagnostics,
          _diagnostic("Different values share the same revision"),
        ],
      );
      _notify();
      return;
    }
    final result = _reconciler.reconcile(
      base: _document.confirmedValue,
      local: _draft,
      remote: value,
      remoteRevision: revision,
      dirtyPaths: _states.dirtyPaths,
      mergePolicies: _document.mergePolicies,
    );
    _document = _document.copyWith(
      confirmedValue: result.base,
      revision: result.revision,
      diagnostics: [..._document.diagnostics, ...result.diagnostics],
    );

    _draft = result.draft;
    _states.applyReconciliation(
      dirtyPaths: result.dirtyPaths,
      confirmedPaths: result.confirmedPaths,
      conflicts: result.conflicts,
      confirmedPhase: EditorSavePhase.saved,
    );
    _notify();
  }

  void _acceptSuccess(
    int revision,
    DataValue value,
    DataValue sent,
    Set<DataPath> committed,
    int submittedRevision,
  ) {
    var nextDraft = value;
    final confirmed = <DataPath>{};
    for (final path in _states.dirtyPaths) {
      final local = path.read(_draft).valueOrNull;
      final editedAfterSubmission = _pendingMutations.any(
        (pending) =>
            pending.revision > submittedRevision &&
            _pathsOverlap(path, pending.mutation.path),
      );
      if (committed.contains(path) &&
          !editedAfterSubmission &&
          local == path.read(sent).valueOrNull) {
        confirmed.add(path);
        continue;
      }
      if (local != null) {
        nextDraft = path.replace(nextDraft, local).valueOrNull ?? nextDraft;
      }
    }
    _states.confirm(confirmed, EditorSavePhase.saved);

    _document = _document.copyWith(confirmedValue: value, revision: revision);

    _draft = nextDraft;
    _notify();
  }
}
