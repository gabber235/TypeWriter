import 'dart:convert';

import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import 'package:typewriter/pages/connect_page.dart';
import 'package:typewriter/utils/socket_extensions.dart';

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

  Future<void> fetchBook() async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final String rawPages = await socket.emitWithAckAsync("fetch", "pages");
    final String rawAdapters = await socket.emitWithAckAsync("fetch", "adapters");

    final jsonPages = jsonDecode(rawPages) as List;
    final jsonAdapters = jsonDecode(rawAdapters) as List;

    final pages = jsonPages.map((e) => Page.fromJson(e)).toList();
    final adapters = jsonAdapters.map((e) => Adapter.fromJson(e)).toList();

    state = Book(name: "Typewriter", adapters: adapters, pages: pages);
  }

  /// Inserts a page. If the page already exists, it will be replaced.
  void insertPage(Page page) {
    state = state.copyWith(
      pages: [...state.pages.where((p) => p.name != page.name), page],
    );
  }

  /// Removes a page.
  void removePage(Page page) {
    state = state.copyWith(pages: state.pages.where((p) => p != page).toList());
  }

  // Saves the book to disk.

  Future<void> save() async {
    // TODO: Implement
  }

  /// Reloads the book from the server.
  Future<void> reload() async {
    return fetchBook();
  }
}
