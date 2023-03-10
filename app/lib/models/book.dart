import "package:collection/collection.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/page.dart";

part "book.freezed.dart";

final bookProvider = StateNotifierProvider<BookNotifier, Book>(
  (ref) => BookNotifier(const Book(name: "", adapters: [], pages: []), ref: ref),
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

  /// Creates a new page.
  Future<void> createPage(String name, [PageType type = PageType.static]) async {
    final page = Page(name: name, type: type);
    await ref.read(communicatorProvider).createPage(page);
    state = state.copyWith(
      pages: [...state.pages, page],
    );
  }

  /// Inserts a page. If the page already exists, it will be replaced.
  void insertPage(Page page) {
    state = state.copyWith(
      pages: state.pages.map((p) => p.name == page.name ? page : p).toList(),
    );
  }

  /// Rename a page.
  /// If the page does not exist, it will be added.
  Future<void> renamePage(String old, String newName) async {
    final page = state.pages.firstWhereOrNull((p) => p.name == old);
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
    final page = state.pages.firstWhereOrNull((p) => p.name == old);
    if (page == null) return;
    state = state.copyWith(
      pages: [
        ...state.pages.where((p) => p.name != old),
        page.copyWith(name: newName),
      ],
    );
  }

  /// Only for internal use.
  void syncDeletePage(String name) {
    state = state.copyWith(
      pages: state.pages.where((p) => p.name != name).toList(),
    );
  }
}
