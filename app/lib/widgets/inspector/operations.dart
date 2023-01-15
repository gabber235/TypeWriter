import "package:flutter/material.dart" hide FilledButton;
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/passing_reference.dart";
import 'package:typewriter/utils/popups.dart';
import "package:typewriter/widgets/filled_button.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/section_title.dart";

class Operations extends HookConsumerWidget {
  const Operations({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: const [
        SectionTitle(title: "Operations"),
        SizedBox(height: 8),
        _DeleteEntry(),
      ],
    );
  }
}

class _DeleteEntry extends HookConsumerWidget {
  const _DeleteEntry();

  void _deleteEntry(WidgetRef ref, Entry entry) {
    ref.read(selectedEntryIdProvider.notifier).state = "";
    ref.read(currentPageProvider)?.deleteEntry(ref.passing, entry);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) => FilledButton.icon(
        onPressed: () => showConfirmationDialogue(
          context: context,
          title: "Delete Entry",
          content: "Are you sure you want to delete this entry?",
          onConfirm: () {
            final entry = ref.read(selectedEntryProvider);
            if (entry != null) _deleteEntry(ref, entry);
          },
        ),
        icon: const FaIcon(FontAwesomeIcons.trash),
        label: const Text("Delete Entry"),
        color: Theme.of(context).colorScheme.error,
      );
}
