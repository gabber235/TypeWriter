import "package:file_picker/file_picker.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/main.dart";
import 'package:typewriter/models/book.dart';

Future<void> pickAndLoadBook(WidgetRef ref) async {
  final path = await FilePicker.platform.getDirectoryPath(
    dialogTitle: "Select typewriter folder",
    lockParentWindow: true,
  );

  if (path == null) return;

  await ref.read(bookProvider.notifier).loadBook(path);

  await ref.read(appRouter).push(const BookRoute());
}
