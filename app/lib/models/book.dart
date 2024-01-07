import "package:collection/collection.dart";
import "package:collection_ext/all.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";

part "book.freezed.dart";

final bookProvider = StateNotifierProvider<BookNotifier, Book>(
  (ref) =>
      BookNotifier(const Book(name: "", adapters: [], pages: []), ref: ref),
  name: "bookProvider",
);

@freezed
class Book with _$Book {
  const factory Book({
    required String name,
    required List<Adapter> adapters,
    required List<Page> pages,
  }) = _Book;
}

class BookNotifier extends StateNotifier<Book> {
  BookNotifier(super.state, {required this.ref});
  final Ref<Book> ref;

  set book(Book book) => state = book;

  @override
  bool updateShouldNotify(Book old, Book current) => old != current;

  /// Creates a new page.
  Future<void> createPage(
    String name, [
    PageType type = PageType.static,
  ]) async {
    final page = Page(pageName: name, type: type);
    await ref.read(communicatorProvider).createPage(page);
    state = state.copyWith(
      pages: [...state.pages, page],
    );
  }

  /// Inserts a page. If the page already exists, it will be replaced.
  void insertPage(Page page) {
    state = state.copyWith(
      pages: state.pages
          .map((p) => p.pageName == page.pageName ? page : p)
          .toList(),
    );
  }

  /// Rename a page.
  /// If the page does not exist, it will be added.
  Future<void> renamePage(String old, String newName) async {
    final page = state.pages.firstWhereOrNull((p) => p.pageName == old);
    if (page == null) return;
    await ref.read(communicatorProvider).renamePage(old, newName);
    syncRenamePage(old, newName);
  }

  /// Deletes a page.
  Future<void> deletePage(String name) async {
    await ref.read(communicatorProvider).deletePage(name);
    syncDeletePage(name);
  }

  /// Reloads the book from the server.
  Future<void> reload() async {
    return ref.read(communicatorProvider).fetchBook();
  }

  /// Only for internal use.
  void syncRenamePage(String old, String newName) {
    final page = state.pages.firstWhereOrNull((p) => p.pageName == old);
    if (page == null) return;
    state = state.copyWith(
      pages: [
        ...state.pages
            .where((p) => p.pageName != old)
            .map((p) => _fixPage(p, old, newName)),
        _fixPage(page, old, newName).copyWith(pageName: newName),
      ],
    );
  }

  /// Only for internal use.
  void syncDeletePage(String name) {
    state = state.copyWith(
      pages: state.pages
          .where((p) => p.pageName != name)
          .map((p) => _fixPage(p, name, null))
          .toList(),
    );
  }

  /// Fix page references. This is called for all other pages when a page is renamed or deleted.
  Page _fixPage(Page page, String targetId, String? newId) {
    final newEntries = page.entries
        .map(
          (entry) => _removedReferencesFromEntry(page, entry, targetId, newId),
        )
        .toList();
    return page.copyWith(entries: newEntries);
  }

  /// When a page is renamed or deleted, all references to it must be updated.
  Entry _removedReferencesFromEntry(
    Page page,
    Entry entry,
    String targetId,
    String? newId,
  ) {
    final referenceEntryPaths = state.adapters
            .flatMap((adapter) => adapter.entries)
            .firstWhereOrNull((blueprint) => blueprint.name == entry.type)
            ?.fieldsWithModifier("page")
            .keys
            .toList() ??
        [];

    final referenceEntryIds = referenceEntryPaths
        .expand((path) => entry.getAll(path))
        .whereType<String>()
        .toList();
    if (!referenceEntryIds.contains(targetId)) {
      return entry;
    }

    final newEntry = referenceEntryPaths.fold(
      entry,
      (previousEntry, path) => previousEntry.copyMapped(
        path,
        (value) => value == targetId ? newId : value,
      ),
    );

    ref.read(communicatorProvider).updateEntireEntry(page.pageName, newEntry);

    return newEntry;
  }
}
