import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:rive/rive.dart';
import 'package:typewriter/models/page.dart';

import 'graph.dart';

class OpenPage extends HookConsumerWidget {
  const OpenPage({
    Key? key,
  }) : super(key: key);

  Future<FilePickerResult?> pickFile() async {
    FilePickerResult? result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: ['json'],
      dialogTitle: "Select a page file to continue the journey",
      withData: true,
    );
    return result;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final openedSelector = useState(false);
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Spacer(),
          const Expanded(
            flex: 2,
            child: RiveAnimation.asset("assets/game_character.riv"),
          ),
          Text("Your journey starts here",
              style: GoogleFonts.jetBrainsMono(
                  textStyle: const TextStyle(
                      fontSize: 40, fontWeight: FontWeight.bold))),
          const SizedBox(height: 24),
          TextButton(
            onPressed: () {
              ref.read(pageProvider.notifier).addEntry(EntryType.npcEvent);
            },
            child: Row(
              mainAxisSize: MainAxisSize.min,
              children: const [
                Text("Create A New Page"),
                SizedBox(width: 8),
                Icon(FontAwesomeIcons.plus),
              ],
            ),
          ),
          const SizedBox(height: 12),
          ElevatedButton(
            onPressed: () async {
              if (openedSelector.value) return;
              openedSelector.value = true;
              final result = await pickFile();
              openedSelector.value = false;
              if (result == null) return;
              final bytes = result.files.first.bytes;
              if (bytes == null) return;
              final text = String.fromCharCodes(bytes);
              final model = pageModelFromJson(text);
              ref.read(pageProvider.notifier).setModel(model);
              ref.read(fileNameProvider.notifier).state =
                  result.files.first.name;
            },
            child: Row(
              mainAxisSize: MainAxisSize.min,
              children: const [
                Text("Open An Existing Page"),
                SizedBox(width: 8),
                Icon(FontAwesomeIcons.folderOpen),
              ],
            ),
          ),
          const Spacer(),
        ],
      ),
    );
  }
}
