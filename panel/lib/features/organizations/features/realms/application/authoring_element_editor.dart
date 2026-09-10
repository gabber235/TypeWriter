import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

EditorTarget authoringElementTarget({
  required AuthoringResourceRepository repository,
  required AuthoringSessionState state,
  required SelectableIdentifier identity,
  required String pageId,
  required String label,
  required EditorDocument document,
}) {
  final page = recordId("page:$pageId");
  final element = state.documents[page]?.elements
      .where((value) => value.id.id == identity.id)
      .firstOrNull;
  if (element == null) throw StateError("The editable element is not loaded");
  return ResourceEditorTarget(
    targetId: identity,
    label: label,
    resource: ElementEditorResource(repository, element.id, page),
    snapshot: elementEditorSnapshot(element, document),
  );
}

EditorSnapshot elementEditorSnapshot(
  wire.PageElement element,
  EditorDocument value,
) => DocumentEditorSnapshot(
  value.copyWith(
    rootType: RecordType(
      fields: {
        "value": TypeField(name: "value", type: value.rootType),
        "placement": TypeField(
          name: "placement",
          type: elementPlacementType(element.placement),
        ),
      },
    ),
    confirmedValue: RecordValue({
      "value": value.confirmedValue,
      "placement": elementPlacementValue(element.placement),
    }),
    mergePolicies: {elementPlacementPath: EditorMergePolicy.record},
  ),
);

final class ElementEditorResource extends AuthoringEditorResource {
  const ElementEditorResource(super.repository, super.id, this.pageId);
  final skir.RecordId pageId;
  @override
  wire.AuthoringSnapshotScope get scope =>
      wire.AuthoringSnapshotScope.createPage(pageId: pageId);
  @override
  Future<EditorSnapshot?> project(wire.AuthoringSnapshot snapshot) async {
    wire.PageElement? element;
    for (final slice in snapshot.slices) {
      if (slice case wire.AuthoringSnapshotSlice_pageWrapper(:final value)) {
        element = value.document?.elements
            .where((value) => value.id == id)
            .firstOrNull;
      }
    }
    if (element == null) {
      throw StateError(
        "The element was removed or moved from its original page",
      );
    }
    final root = ResolvedTypeRef(
      id: DeclaredTypeId(element.elementType),
      revision: element.schemaRevision,
    );
    final fetched = await repository.session.catalog.fetch(
      RealmEditorCatalogRoute(
        organizationId: repository.organization,
        realmId: repository.realm,
      ),
      RealmEditorCatalogRequest(types: {root}),
    );
    repository.session.checkActive();

    final catalog = switch (fetched) {
      RealmEditorCatalogFetched(:final snapshot) => bootstrapTypeCatalog(
        snapshot.catalog.definitions,
      ),
      RealmEditorCatalogFetchUnavailable(:final diagnostics) =>
        throw ElementDefinitionException(diagnostics),
      RealmEditorCatalogGenerationMismatch() => throw StateError(
        "The editor catalog changed while refreshing",
      ),
    };
    final decoded = SkirEditorCodec(
      TypeRegistry(catalog),
    ).decodeValue(element.value);
    final value = decoded.valueOrNull;
    if (value == null) throw ElementDefinitionException(decoded.diagnostics);
    return elementEditorSnapshot(
      element,
      EditorDocument(
        rootType: NamedType(root),
        typeCatalog: catalog,
        confirmedValue: value,
        revision: snapshot.sequence,
      ),
    );
  }

  @override
  wire.AuthoringOperation operation(
    EditorSnapshot snapshot,
    EditorCommit commit,
  ) => elementCommitOperation(id.id, commit, snapshot.document.typeCatalog);
}
