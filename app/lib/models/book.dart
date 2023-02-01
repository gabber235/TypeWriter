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

  /// Inserts a page. If the page already exists, it will be replaced.
  void insertPage(Page page) {
    state = state.copyWith(
      pages: [...state.pages.where((p) => p.name != page.name), page],
    );
  }

  /// Rename a page.
  /// If the page does not exist, it will be added.
  void renamePage(String old, String newName) {
    final page = state.pages.firstWhere((p) => p.name == old, orElse: () => Page(name: old));
    state = state.copyWith(
      pages: [
        ...state.pages.where((p) => p.name != old),
        page.copyWith(name: newName),
      ],
    );
  }

  /// Deletes a page.
  void deletePage(String name) {
    state = state.copyWith(
      pages: state.pages.where((p) => p.name != name).toList(),
    );
  }

  /// Reloads the book from the server.
  Future<void> reload() async {
    return ref.read(communicatorProvider).fetchBook();
  }
}
