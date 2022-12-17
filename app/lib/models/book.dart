import "dart:convert";
import "dart:io";

import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";

part "book.freezed.dart";

final bookProvider = StateNotifierProvider<BookNotifier, Book>(
  (ref) => BookNotifier(const Book(name: "", path: "", adapters: [], pages: [])),
);

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
  BookNotifier(super.state);

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
    // Save pages/*.json
    for (final page in state.pages) {
      final file = File("${state.path}/pages/${page.name}.json");
      await file.writeAsString(jsonEncode(page.toJson()));
    }
  }

  /// Loads in the parsed book.
  Future<void> loadBook(String path) async {
    final adapters = await _loadAdapters(path);
    final pages = await _loadPages(path);

    state = Book(
      name: "Typewriter",
      path: path,
      adapters: adapters,
      pages: pages,
    );
  }

  /// Reloads the book from disk.
  Future<void> reload() async {
    await loadBook(state.path);
  }
}

Future<List<Adapter>> _loadAdapters(String path) async {
  // Read file "path/adapters.json"
  final file = File("$path/adapters.json");
  if (!await file.exists()) {
    return [];
  }

  final content = await file.readAsString();
  final json = jsonDecode(content);

  return (json as List).map((e) => Adapter.fromJson(e as Map<String, dynamic>)).toList();
}

Future<List<Page>> _loadPages(String path) async {
  // Read all files in "path/pages"
  final directory = Directory("$path/pages");
  if (!await directory.exists()) {
    return [];
  }

  final pages = await directory
      .list()
      .where((e) => e is File)
      .where((e) => e.path.endsWith(".json"))
      .map((e) => _loadPage(e as File))
      .toList();
  return Future.wait(pages);
}

Future<Page> _loadPage(File file) async {
  final name = file.path.split("/").last.split(".").first;

  final content = await file.readAsString();
  final json = {
    ...jsonDecode(content) as Map<String, dynamic>,
    "name": name,
  };

  return Page.fromJson(json);
}
