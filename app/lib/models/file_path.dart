import "dart:convert";
import "dart:io";

import "package:file_picker/file_picker.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/page.dart";

final directoryPathProvider = StateProvider<String>((ref) => "");

Future<void> pickAndLoadBook(WidgetRef ref) async {
  final result = await FilePicker.platform.getDirectoryPath(
    dialogTitle: "Select typewriter folder",
    lockParentWindow: true,
  );

  if (result == null) return;

  ref.read(directoryPathProvider.notifier).state = result;

  final adapters = await _loadAdapters(result);
  final pages = await _loadPages(result);

  await ref.read(bookProvider.notifier).loadBook(
        Book(
          name: "Typewriter",
          path: result,
          adapters: adapters,
          pages: pages,
        ),
      );

  await ref.read(appRouter).push(const BookRoute());
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
