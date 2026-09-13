import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

/// Creates the editor target for a loaded element on [pageId].
///
/// The page document in [state] is the lookup boundary. The target captures
/// the element's typed value together with its page specific placement, so the
/// shared editor can own local changes while [ElementEditorResource] refreshes
/// and saves the canonical resource. Throws when the element is not retained
/// by the session, because an unloaded element has no safe authoritative base.
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

/// Embeds an element value and its placement in one editor document.
///
/// Placement is a sibling field rather than part of the element's typed value.
/// The merge policy therefore treats placement as one record while value edits
/// remain path based. This shape lets the editor preserve placement changes and
/// typed content changes in the same authoring commit.
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

/// Projects and patches one element within its owning page document.
///
/// Element values are decoded with the catalog revision carried by the
/// authoritative snapshot or applied change. A catalog that cannot resolve the
/// element type is unavailable, not an invitation to decode with a stale
/// definition. The page identifier stays fixed for this retained resource and
/// is included in placement and value operation routing.
final class ElementEditorResource extends AuthoringEditorResource {
  const ElementEditorResource(super.repository, super.id, this.pageId);
  final skir.RecordId pageId;
  @override
  wire.AuthoringSnapshotScope get scope =>
      wire.AuthoringSnapshotScope.createPage(pageId: pageId);

  /// Finds this element in the requested page snapshot and decodes its value.
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
      return null;
    }
    return _projectElement(element, snapshot.sequence);
  }

  /// Projects this element directly from an applied resource change.
  ///
  /// The submitted type catalog is retained as decoding context for the change.
  /// If the change is unrelated, malformed, or references an unavailable type,
  /// null asks the caller to perform the authoritative refresh fallback.
  @override
  EditorSnapshot? projectApplied(
    wire.AuthoringChanged change,
    EditorSnapshot submitted,
  ) {
    for (final resource in change.changes) {
      switch (resource) {
        case wire.AuthoringResourceChange_upsertElementWrapper(:final value):
          if (value.id == id && value.page == pageId) {
            final root = ResolvedTypeRef(
              id: DeclaredTypeId(value.elementType),
              revision: value.schemaRevision,
            );
            final registry = TypeRegistry(submitted.document.typeCatalog);
            if (registry.definition(root) == null) return null;
            final decoded = SkirEditorCodec(registry).decodeValue(value.value);
            final elementValue = decoded.valueOrNull;
            if (elementValue == null) return null;
            return elementEditorSnapshot(
              value,
              EditorDocument(
                rootType: NamedType(root),
                typeCatalog: submitted.document.typeCatalog,
                confirmedValue: elementValue,
                revision: change.sequence,
              ),
            );
          }
        case wire.AuthoringResourceChange_removeElementWrapper(:final value):
          if (value == id) return null;
        case wire.AuthoringResourceChange_unknown() ||
            wire.AuthoringResourceChange_upsertBookWrapper() ||
            wire.AuthoringResourceChange_removeBookWrapper() ||
            wire.AuthoringResourceChange_upsertTagWrapper() ||
            wire.AuthoringResourceChange_removeTagWrapper() ||
            wire.AuthoringResourceChange_upsertPageWrapper() ||
            wire.AuthoringResourceChange_removePageWrapper():
      }
    }
    return null;
  }

  Future<EditorSnapshot?> _projectElement(
    wire.PageElement element,
    int sequence,
  ) async {
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
    final decoded = SkirEditorCodec(TypeRegistry(catalog))
        .decodeValue(element.value);
    final value = decoded.valueOrNull;
    if (value == null) throw ElementDefinitionException(decoded.diagnostics);
    return elementEditorSnapshot(
      element,
      EditorDocument(
        rootType: NamedType(root),
        typeCatalog: catalog,
        confirmedValue: value,
        revision: sequence,
      ),
    );
  }

  /// Builds guarded value and placement mutations for this element.
  @override
  wire.AuthoringOperation operation(
    EditorSnapshot snapshot,
    EditorCommit commit,
  ) => elementCommitOperation(id.id, commit, snapshot.document.typeCatalog);
}
