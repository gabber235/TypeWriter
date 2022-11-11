import 'dart:convert';
import 'dart:io';

import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/models/page.dart';

part 'book.freezed.dart';

final bookProvider = StateNotifierProvider<BookNotifier, Book>((ref) {
  return BookNotifier(const Book(name: '', path: '', adapters: [], pages: []));
});

@freezed
class Book with _$Book {
  const factory Book({
    required String name,
    required String path,
    required List<Adapter> adapters,
    required List<Page> pages,
  }) = _Book;
}

class BookNotifier extends StateNotifier<Book> {
  BookNotifier(Book state) : super(state);

  void loadBook(Book book) async {
    state = book;
  }

  /// Inserts a page. If the page already exists, it will be replaced.
  void insertPage(Page page) {
    state = state.copyWith(
        pages: [...state.pages.where((p) => p.name != page.name), page]);
  }

  /// Removes a page.
  void removePage(Page page) {
    state = state.copyWith(pages: state.pages.where((p) => p != page).toList());
  }

  // Saves the book to disk.
  Future<void> save() async {
    // Save pages/*.json
    for (final page in state.pages) {
      final file = File("${state.path}/pages/${page.name}.json");
      await file.writeAsString(jsonEncode(page.toJson()));
    }
  }
}
