import "package:typewriter_panel/typewriter_panel.dart";

/// Retains resource editors across presentation refreshes and disposes unused owners.
final class EditorOwnerRegistry {
  EditorOwnerRegistry({LocalWorkCommands? workspace})
    : workspace = workspace ?? LocalWorkSession(),
      _ownsWorkspace = workspace == null;

  final LocalWorkCommands workspace;
  final bool _ownsWorkspace;
  EditorDestination Function(Object identity)? destinationFor;
  final Set<EditorResourceKey> _retained = {};
  Set<EditorResourceKey> _previous = {};

  Map<EditOwner, String> get labels => {
    for (final key in _retained)
      if (workspace.resources[key] case final resource?)
        resource.source: resource.label,
  };

  void begin() {
    _previous = Set.of(_retained);
    _retained.clear();
  }

  EditorSource editor(EditorTarget target) {
    final key = target.resource.key;
    final first = _retained.add(key);
    final source = workspace.editor(target);
    if (first && !_previous.contains(key)) workspace.retain(key);

    final destination = destinationFor;
    if (destination != null) {
      workspace.resources[key]!.destination = destination(target.targetId);
    }
    return source;
  }

  void deleted(Object id) {
    final identity = _resourceIdentity(id);
    for (final key in _retained) {
      if (key.identity == identity) {
        workspace.resources[key]?.source.acceptRemoteDeletion();
      }
    }
  }

  void unavailable(String message) {
    for (final key in _retained) {
      final source = workspace.resources[key]?.source;
      if (source == null) continue;
      source.refreshDocument(
        source.document.copyWith(
          readOnly: true,
          diagnostics: [
            TypeDiagnostic(
              code: TypeDiagnosticCode.invalidValue,
              message: message,
            ),
          ],
        ),
      );
    }
  }

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
        selected[index].targetId: results[index],
    };
  }

  void end() {
    for (final key in _previous.difference(_retained)) {
      workspace.release(key);
    }
  }

  void dispose() {
    if (_ownsWorkspace) {
      (workspace as LocalWorkSession).dispose();
      return;
    }
    for (final key in _retained) {
      workspace.release(key);
    }
    _retained.clear();
  }
}

/// Binds immutable presentation metadata to an explicitly scoped resource.
final class ResourceEditorTarget implements EditorTarget {
  const ResourceEditorTarget({
    required this.targetId,
    required this.label,
    required this.resource,
    required this.snapshot,
    this.commitPolicy = EditorCommitPolicy.autosaveChanges,
  });

  @override
  final Object targetId;

  @override
  final String label;

  @override
  final EditableResource resource;

  @override
  final EditorSnapshot snapshot;

  @override
  final EditorCommitPolicy commitPolicy;

  @override
  EditorDocument get document => snapshot.document;

  @override
  List<TypeDiagnostic> validateDraft(DataValue value) =>
      snapshot.validateDraft(value);

  @override
  EditorValue value(DataPath path) =>
      document.confirmedValue.readEditorValue(path);

  @override
  EditorMutationResult validate(DataPath path, DataValue value) =>
      snapshot.validate(path, value);
}

Object _resourceIdentity(Object id) =>
    id is SelectableIdentifier ? id.resourceId : id;
