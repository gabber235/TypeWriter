import "package:typewriter_panel/typewriter_panel.dart";

/// Retains resource editors across presentation refreshes and disposes unused owners.
final class EditorOwnerRegistry {
  EditorOwnerRegistry({EditorWorkspace? workspace, this.scope})
    : workspace = workspace ?? EditorWorkspace(),
      _ownsWorkspace = workspace == null;

  final EditorWorkspace workspace;
  final bool _ownsWorkspace;
  Object? scope;
  EditorDestination Function(Object identity)? destinationFor;
  final Set<EditorResourceKey> _retained = {};
  Set<EditorResourceKey> _previous = {};

  Map<EditOwner, String> get labels => {
    for (final key in _retained)
      if (workspace.resources[key] case final resource?)
        resource.source: resource.target.label,
  };

  void begin() {
    _previous = Set.of(_retained);
    _retained.clear();
  }

  EditorSource editor(EditorTarget target) {
    final key = EditorResourceKey(
      scope: scope,
      identity: _resourceIdentity(target.targetId),
    );
    final first = _retained.add(key);
    final source = workspace.editor(key, target);
    if (first && !_previous.contains(key)) workspace.retain(key);
    final destination = destinationFor;
    if (destination != null) {
      workspace.resources[key]!.destination = destination(target.targetId);
    }
    return source;
  }

  void deleted(Object id) => workspace
      .resources[EditorResourceKey(
        scope: scope,
        identity: _resourceIdentity(id),
      )]
      ?.source
      .acceptRemoteDeletion();

  Future<Map<Object, TypedMutationResult>> flush({
    bool failedOnly = false,
  }) async {
    final selected = [
      for (final key in _retained)
        if (workspace.resources[key] case final resource?)
          if (!failedOnly || resource.source.saveState(DataPath.root).canRetry)
            resource,
    ];
    final results = await Future.wait(
      selected.map((entry) => entry.source.flush()),
    );
    return {
      for (var index = 0; index < selected.length; index++)
        selected[index].target.targetId: results[index],
    };
  }

  void end() {
    for (final key in _previous.difference(_retained)) {
      workspace.release(key);
    }
  }

  void dispose() {
    if (_ownsWorkspace) {
      workspace.dispose();
      return;
    }
    for (final key in _retained) {
      workspace.release(key);
    }
    _retained.clear();
  }
}

/// Adapts one backend mutation boundary into a retained resource editor.
final class ResourceEditorTarget implements EditorTarget {
  const ResourceEditorTarget({
    required this.targetId,
    required this.label,
    required this.document,
    required EditorCommitter commit,
    this.updates = const Stream.empty(),
    this.commitPolicy = EditorCommitPolicy.autosaveChanges,
    List<TypeDiagnostic> Function(DataValue)? validateDraft,
  }) : _commit = commit,
       _validateDraft = validateDraft;
  @override
  final EditorCommitPolicy commitPolicy;
  final List<TypeDiagnostic> Function(DataValue)? _validateDraft;
  @override
  List<TypeDiagnostic> validateDraft(DataValue value) =>
      _validateDraft?.call(value) ?? const [];
  @override
  final Object targetId;
  @override
  final String label;
  @override
  final EditorDocument document;
  @override
  final Stream<EditorDocument?> updates;
  final EditorCommitter _commit;
  @override
  Future<TypedMutationResult> commit(EditorCommit commit) => _commit(commit);
  @override
  EditorValue value(DataPath path) =>
      document.confirmedValue.readEditorValue(path);
  @override
  EditorMutationResult validate(DataPath path, DataValue value) =>
      document.rootType.validateEditorMutation(
        path,
        value,
        registry: TypeRegistry(document.typeCatalog),
      );
}

Object _resourceIdentity(Object id) =>
    id is SelectableIdentifier ? id.resourceId : id;
