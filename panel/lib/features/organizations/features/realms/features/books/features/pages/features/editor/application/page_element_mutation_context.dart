part of "page_elements.dart";

mixin _PageElementMutationContext on _$PageElements {
  late skir.RecordId _pageId;
  late AuthoringSessionProvider _sessionProvider;

  AuthoringSession get _commands => ref.read(_sessionProvider.notifier);

  Future<void> _submit(Future<wire.ApplyAuthoringBatchResponse> pending) async {
    final response = await pending;
    response.requireApplied(
      conflictMessage: "The page changed while it was being edited",
    );
  }

  ({TypeRegistry registry, SkirEditorCodec codec}) _codec() {
    final snapshot = ref.read(realmEditorCatalogProvider).value?.snapshot;
    if (snapshot == null) {
      throw ApiException.badRequest("The editor catalog is unavailable");
    }
    final registry = TypeRegistry(
      bootstrapTypeCatalog(snapshot.catalog.definitions),
    );
    return (registry: registry, codec: SkirEditorCodec(registry));
  }

  wire.PageDocument get _document {
    final document = ref.read(_sessionProvider).documents[_pageId];
    if (document == null) throw ApiException.notFound("Page");
    return document;
  }

  wire.PageElement _wireElement(String id) =>
      _document.elements.singleWhere((element) => element.id.id == id);
}
