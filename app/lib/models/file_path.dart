import 'package:file_picker/file_picker.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/app_router.dart';
import 'package:typewriter/main.dart';

final directoryPathProvider = StateProvider<String>((ref) {
  return "";
});

Future<void> pickDirectory(WidgetRef ref) async {
  final result = await FilePicker.platform.getDirectoryPath(
    dialogTitle: "Select typewriter folder",
    lockParentWindow: true,
  );

  if (result == null) return;

  ref.read(directoryPathProvider.notifier).state = result;

  //TODO: Read adapter info
  //TODO: Read pages

  ref.read(appRouter).push(const BookRoute());
}
