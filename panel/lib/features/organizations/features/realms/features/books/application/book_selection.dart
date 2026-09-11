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
    final canonicalBook = ref.watch(canonicalBookProvider(bookId));
    if (canonicalBook.mapUnready<Selectable>() case final value?) return value;
    final book = canonicalBook.requireValue;
    if (book == null) {
      return AsyncError(SelectableNotFoundException(this), StackTrace.current);
    }

    final tagsAsync = ref.watch(projectedTagsProvider);
    if (tagsAsync.mapUnready<Selectable>() case final value?) return value;
    final tags = tagsAsync.requireValue;
    final revision = ref.watch(
      authoringSessionProvider(
        organization,
        realm,
      ).select((value) => value.sequence ?? 0),
    );
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
        revision: revision,
        tagCollection: tags.presentationCollection(),
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
    required this.revision,
    required this.tagCollection,
  });

  @override
  final BookIdentifier id;
  final Book book;
  final int revision;
  @override
  final EditableResource resource;
  final VoidCallback? onOpen;

  final PresentationCollectionSource tagCollection;

  @override
  MultiInspectionDefinition get multiInspection =>
      const BookMultiInspectionDefinition();

  @override
  String get name => book.title;

  @override
  List<PresentationDefinition> get presentations => [
    _bookInspectorPresentation,
  ];
  @override
  List<PresentationCollectionSource> get collections => [tagCollection];

  @override
  EditorSnapshot get snapshot => BookEditorSnapshot(book, revision);
  @override
  List<SelectionCapability> get capabilities => [
    if (onOpen case final open?)
      OpenSelectionCapability(onOpen: open, allowMultiSelect: false),
  ];

  @override
  Widget? buildInspectorHeader(EditOwner owner) => ManagedInspectorHeader(
    id: book.bookId.id,
    owner: owner,
    fallbackName: book.title.formatted,
    fallbackColor: book.color,
    nameField: "title",
  );
}

BindingReference _bookField(String name) => BindingReference(
  bindingId: const BindingId(0),
  path: DataPath.root.field(name),
);

extension BookInspectorValue on Book {
  RecordValue get inspectorValue => RecordValue({
    "title": title.asValue,
    "icon": IconValue.from(icon).typedValue,
    "color": color.asValue,
    "tags": ListValue(tagIds.map((tagId) => tagId.id.asValue).toList()),
  });

  Book? withInspectorValue(DataValue value) {
    if (value is! RecordValue) return null;
    final title = value.fields["title"];
    final icon = value.fields["icon"]?.iconValueOrNull;
    final color = value.fields["color"];
    final tags = value.fields["tags"];
    if (title is! StringValue ||
        title.value.trim().isEmpty ||
        icon == null ||
        color is! IntegerValue ||
        tags is! ListValue) {
      return null;
    }
    final decodedColor = color.asColorOrNull;
    final tagIds = tags.values
        .whereType<StringValue>()
        .map((tag) => recordId("tag:${tag.value}"))
        .toList();
    if (decodedColor == null || tagIds.length != tags.values.length) {
      return null;
    }
    final encodedIcon = switch (icon) {
      IconifyIconValue(:final value) => value,
      SvgIconValue(:final source) => source,
    };
    return copyWith(
      title: title.value,
      icon: encodedIcon,
      color: decodedColor,
      tagIds: tagIds,
    );
  }

  Book projected(LocalEditorValue? local) {
    if (local == null) return this;
    return withInspectorValue(local.projectOnto(inspectorValue)) ?? this;
  }
}
