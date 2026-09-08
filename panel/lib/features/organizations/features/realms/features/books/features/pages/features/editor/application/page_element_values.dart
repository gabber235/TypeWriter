part of "page_elements.dart";

mixin _PageElementValues on _$PageElements, _PageElementMutationContext {
  Future<void> updateCueFieldValue(
    String cueId,
    DataPath path,
    DataValue value,
  ) => _updateFieldValue(cueId, path, value);

  Future<void> updateEntryFieldValue(
    String entryId,
    DataPath path,
    DataValue value,
  ) => _updateFieldValue(entryId, path, value);

  Future<void> _updateFieldValue(
    String elementId,
    DataPath path,
    DataValue value,
  ) async {
    final owners = EditorOwnerRegistry(
      workspace: ref.read(editorWorkspaceProvider),
      scope: (organizationId, realmId),
    );
    try {
      final result = await owners.editor(_target(elementId)).applyChanges({
        elementValuePath.followedBy(path): value,
      });
      switch (result) {
        case MutationSuccess():
          return;
        case MutationUncertain():
          return;
        case MutationConflict():
          throw ApiException.conflict("The field changed elsewhere");
        case MutationInvalid(:final diagnostics) ||
            MutationUnavailable(:final diagnostics):
          throw ApiException.badRequest(
            diagnostics.map((value) => value.message).join("; "),
          );
        case MutationPermissionDenied(:final message):
          throw ApiException.badRequest(message);
      }
    } finally {
      owners.dispose();
    }
  }

  Future<TypedMutationResult> commitElementValue(
    String elementId,
    EditorCommit commit,
  ) => _target(elementId).commit(commit);

  EditorTarget _target(String elementId) {
    state.ensureReady();
    final current = state.requireValue.singleWhere(
      (element) => element.id == elementId,
    );
    final definition = switch (current) {
      PageElementEntry(entry: DefinitionPageEntry(:final definition)) =>
        definition.elementDefinition,
      PageElementCue(:final cue) => cue.elementDefinition,
      _ => throw ApiException.badRequest("The element has no editable value"),
    };
    final codec = _codec();
    final canonical = codec.codec
        .decodeValue(_wireElement(elementId).value)
        .valueOrNull;
    if (canonical == null)
      throw ApiException.badRequest("The element value cannot be decoded");
    return authoringElementTarget(
      session: _commands,
      identity: EntryIdentifier(elementId),
      pageId: _pageId.id,
      label: _elementName(current),
      document: EditorDocument(
        rootType: NamedType(definition.rootType),
        typeCatalog: codec.registry.catalog,
        confirmedValue: canonical,
        revision: ref.read(_sessionProvider).sequence ?? 0,
      ),
    );
  }
}
