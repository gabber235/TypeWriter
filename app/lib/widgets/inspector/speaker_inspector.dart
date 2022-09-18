import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';
import 'package:typewriter/pages/inspection_menu.dart';

class SpeakerInspector extends HookConsumerWidget {
  final Speaker speaker;

  const SpeakerInspector({
    Key? key,
    required this.speaker,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        EntryInformation(
          entry: speaker,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertEntry(speaker.copyWith(name: name)),
        ),
        const Divider(),
        _DisplayNameField(speaker: speaker),
        const Divider(),
        _SoundField(speaker: speaker),
        const Divider(),
        Operations(entry: speaker),
      ],
    );
  }
}

class _DisplayNameField extends HookConsumerWidget {
  final Speaker speaker;

  const _DisplayNameField({Key? key, required this.speaker}) : super(key: key);

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertEntry(speaker.copyWith(
          displayName: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SizedBox(height: 8),
        const SectionTitle(title: "Display Name"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: speaker.displayName,
          onChanged: (value) {
            _onChanged(value, ref);
          },
          hintText: "Enter a display name",
          icon: FontAwesomeIcons.tag,
        ),
        const SizedBox(height: 8),
      ],
    );
  }
}

class _SoundField extends HookConsumerWidget {
  final Speaker speaker;

  const _SoundField({
    Key? key,
    required this.speaker,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Sound"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: speaker.sound,
          onChanged: (value) {
            ref.read(pageProvider.notifier).insertEntry(speaker.copyWith(
                  sound: value,
                ));
          },
          hintText: "Enter a minecraft sound",
          icon: FontAwesomeIcons.volumeHigh,
        ),
      ],
    );
  }
}
