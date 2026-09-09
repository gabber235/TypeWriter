part of "books.dart";

class BookIdentifier extends SelectableIdentifier {
  const BookIdentifier(this.bookId);

  final skir.RecordId bookId;

  @override
  String get id => bookId.id;

  @override
  Object get resourceId => bookId;

  @override
  AsyncValue<Selectable> create(Ref ref) {
    final organization = ref.watch(organizationIdProvider);
    final realm = ref.watch(realmIdProvider);
    if (organization == null || realm == null) {
      return AsyncError(
        ApiException.badRequest("No realm selected"),
        StackTrace.current,
      );
    }
    final repository = ref
        .watch(resourceRepositoriesProvider)
        .authoring(organization, realm);
    final router = ref.watch(appRouterProvider);
    final asyncBook = ref.watch(bookProvider(bookId));
    if (asyncBook.mapUnready<Selectable>() case final value?) return value;
    final book = asyncBook.requireValue;

    final tags = ref.watch(tagsProvider).value ?? const <Tag>[];
    if (book == null) throw SelectableNotFoundException(this);
    return AsyncData(
      BookSelection(
        resource: BookEditorResource(repository, bookId),
        onOpen: () {
          router.navigate(
            BookRoute(
              organizationId: organization.id,
              realmId: realm.id,
              bookId: bookId.id,
            ),
          );
        },
        id: this,
        book: book,
        tagCollection: tagPresentationCollection(tags),
      ),
    );
  }

  @override
  int get hashCode => bookId.hashCode;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is BookIdentifier && other.bookId == bookId;

  @override
  String toString() => "BookIdentifier(bookId: $bookId)";
}

class BookSelection extends EditableSelectable<BookIdentifier> {
  const BookSelection({
    required this.resource,
    required this.onOpen,
    required this.id,
    required this.book,
    required this.tagCollection,
  });

  @override
  final BookIdentifier id;
  final Book book;
  @override
  final EditableResource resource;
  final VoidCallback? onOpen;

  final PresentationCollectionSource tagCollection;

  @override
  String get name => book.title;

  @override
  List<PresentationDefinition> get presentations => [
    _bookInspectorPresentation,
  ];
  @override
  List<PresentationCollectionSource> get collections => [tagCollection];

  @override
  EditorSnapshot get snapshot => BookEditorSnapshot(book);
  @override
  List<SelectionCapability> get capabilities => [
    if (onOpen case final open?)
      OpenSelectionCapability(onOpen: open, allowMultiSelect: false),
  ];

  @override
  Widget? buildInspectorHeader() => BookHeader(
    id: book.bookId.id,
    name: book.title.formatted,
    color: book.color,
  );
}

BindingReference _bookField(String name) => BindingReference(
  bindingId: const BindingId(0),
  path: DataPath.root.field(name),
);

extension BookInspectorValue on Book {
  RecordValue get inspectorValue => RecordValue({
    "title": StringValue(title),
    "icon": IconValue.from(icon).typedValue,
    "color": color.integerValue,
    "tags": ListValue(tagIds.map((tagId) => StringValue(tagId.id)).toList()),
  });
}
