import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:rive/rive.dart';
import 'package:typewriter/models/file_path.dart';
import 'package:typewriter/widgets/filled_button.dart';

class HomePage extends HookConsumerWidget {
  const HomePage({Key? key}) : super(key: key);

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
          const Text("Your journey starts here",
              style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold)),
          const SizedBox(height: 24),
          FilledButton.icon(
            onPressed: () async {
              if (openedSelector.value) return;
              openedSelector.value = true;
              await pickDirectory(ref);
              openedSelector.value = false;
            },
            label: const Text("Open Typewriter Directory"),
            icon: const Icon(FontAwesomeIcons.folderOpen),
          ),
          const Spacer(),
        ],
      ),
    );
  }
}
