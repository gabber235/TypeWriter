import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/deprecated/models/page.dart";
import "package:typewriter/deprecated/pages/graph.dart";
import "package:typewriter/deprecated/pages/inspection_menu.dart";

class SpeakerInspector extends HookConsumerWidget {

  const SpeakerInspector({
    super.key,
    required this.speaker,
  });
  final Speaker speaker;

  @override
  Widget build(BuildContext context, WidgetRef ref) => Column(
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

class _DisplayNameField extends HookConsumerWidget {

  const _DisplayNameField({required this.speaker});
  final Speaker speaker;

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertEntry(speaker.copyWith(
          displayName: value,
        ),);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) => Column(
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

class _SoundField extends HookConsumerWidget {

  const _SoundField({
    required this.speaker,
  });
  final Speaker speaker;

  @override
  Widget build(BuildContext context, WidgetRef ref) => Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Sound"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: speaker.sound,
          onChanged: (value) {
            ref.read(pageProvider.notifier).insertEntry(speaker.copyWith(
                  sound: value,
                ),);
          },
          hintText: "Enter a minecraft sound",
          icon: FontAwesomeIcons.volumeHigh,
        ),
      ],
    );
}
