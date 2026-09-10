part of "books.dart";

@riverpod
AsyncValue<List<Book>> filteredBooks(Ref ref, String query) {
  final books = ref.watch(projectedBooksProvider);
  if (books.mapUnready<List<Book>>() case final value?) return value;
  if (query.isEmpty) return AsyncData(books.requireValue);
  final tags = ref.watch(projectedTagsProvider);
  if (tags.mapUnready<List<Book>>() case final value?) return value;
  final lowercaseQuery = query.toLowerCase();
  return AsyncData(
    books.requireValue.where((book) {
      if (book.title.toLowerCase().contains(lowercaseQuery)) return true;
      return book.tagIds
          .map(
            (tagId) =>
                tags.requireValue.firstWhereOrNull((tag) => tag.tagId == tagId),
          )
          .nonNulls
          .any((tag) => tag.name.toLowerCase().contains(lowercaseQuery));
    }).toList(),
  );
}

@riverpod
skir.RecordId? bookId(Ref ref) {
  final id = ref.watch(routeParamProvider("bookId"));
  if (id == null) return null;
  return recordId("book:$id");
}

@riverpod
Future<Book?> canonicalBook(Ref ref, skir.RecordId bookId) async {
  final books = await ref.watch(canonicalBooksProvider.future);
  return books.firstWhereOrNull((book) => book.bookId == bookId);
}

@riverpod
AsyncValue<List<Book>> projectedBooks(Ref ref) {
  final canonical = ref.watch(canonicalBooksProvider);
  if (canonical.mapUnready<List<Book>>() case final value?) return value;
  final organizationId = ref.watch(organizationIdProvider);
  final realmId = ref.watch(realmIdProvider);
  if (organizationId == null || realmId == null) {
    return AsyncData(canonical.requireValue);
  }
  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues),
  );
  return AsyncData([
    for (final book in canonical.requireValue)
      book.projected(
        local[EditorResourceKey(
          scope: EditorResourceScope(
            organizationId: organizationId,
            realmId: realmId,
          ),
          identity: book.bookId,
        )],
      ),
  ]);
}

@riverpod
AsyncValue<Book?> projectedBook(Ref ref, skir.RecordId bookId) {
  final canonical = ref.watch(canonicalBookProvider(bookId));
  if (canonical.mapUnready<Book?>() case final value?) return value;
  final organizationId = ref.watch(organizationIdProvider);
  final realmId = ref.watch(realmIdProvider);
  if (organizationId == null || realmId == null) return canonical;
  final key = EditorResourceKey(
    scope: EditorResourceScope(
      organizationId: organizationId,
      realmId: realmId,
    ),
    identity: bookId,
  );
  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues[key]),
  );
  return AsyncData(canonical.requireValue?.projected(local));
}
