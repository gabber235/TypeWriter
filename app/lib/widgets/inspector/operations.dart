import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
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

  Future<bool> _showDeleteDialog(BuildContext context) async =>
      await showDialog<bool>(
        context: context,
        builder: (context) => AlertDialog(
          title: const Text("Delete Entry"),
          content: const Text("Are you sure you want to delete this entry?"),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(false),
              child: const Text("Cancel"),
            ),
            FilledButton.icon(
              onPressed: () => Navigator.of(context).pop(true),
              icon: const FaIcon(FontAwesomeIcons.trash),
              label: const Text("Delete"),
              color: Theme.of(context).errorColor,
            ),
          ],
        ),
      ) ??
      false;

  void _deleteEntry(WidgetRef ref, Entry entry) {
    ref.read(selectedEntryIdProvider.notifier).state = "";
    ref.read(currentPageProvider)?.deleteEntry(ref, entry);
  }

  Future<void> _confirmDeleteAndDelete(
    BuildContext context,
    WidgetRef ref,
  ) async {
    final sure = await _showDeleteDialog(context);

    if (sure) {
      final entry = ref.read(selectedEntryProvider);
      if (entry != null) _deleteEntry(ref, entry);
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) => FilledButton.icon(
        onPressed: () => _confirmDeleteAndDelete(context, ref),
        icon: const FaIcon(FontAwesomeIcons.trash),
        label: const Text("Delete Entry"),
        color: Theme.of(context).errorColor,
      );
}
