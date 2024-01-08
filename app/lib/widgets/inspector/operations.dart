import "package:flutter/material.dart" hide FilledButton;
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/entries_graph.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/components/general/filled_button.dart";
import "package:typewriter/widgets/components/general/outline_button.dart";
import "package:typewriter/widgets/inspector/inspector.dart";
import "package:typewriter/widgets/inspector/section_title.dart";

class Operations extends HookConsumerWidget {
  const Operations({
    this.actions = const [],
    super.key,
  }) : super();

  final List<ContextMenuTile> actions;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final type = ref.watch(inspectingEntryProvider.select((e) => e?.type));
    if (type == null) return const SizedBox();
    final canTrigger = ref.watch(isTriggerEntryProvider(type));
    final canBeTriggered = ref.watch(isTriggerableEntryProvider(type));
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const SectionTitle(title: "Operations"),
        const SizedBox(height: 8),
        for (final action in actions) ...[
          if (action is ContextMenuDivider) const Divider(),
          if (action is ContextMenuButton)
            OutlineButton.icon(
              icon: FaIcon(action.icon),
              label: Text(action.title),
              onPressed: action.onTap,
              color: action.color,
            ),
          const SizedBox(height: 8),
        ],
        if (canTrigger) ...[
          const _ExtendWithEntry(),
          const SizedBox(height: 8),
        ],
        if (canTrigger && canBeTriggered) ...[
          const _ExtendWithDuplicate(),
          const SizedBox(height: 8),
        ],
        const _DeleteEntry(),
        const SizedBox(height: 8),
      ],
    );
  }
}

class _ExtendWithDuplicate extends HookConsumerWidget {
  const _ExtendWithDuplicate();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return OutlineButton.icon(
      onPressed: () {
        final page = ref.read(currentPageProvider);
        if (page == null) return;
        final entryId = ref.read(inspectingEntryIdProvider);
        if (entryId.isNullOrEmpty) return;
        page.extendsWithDuplicate(ref.passing, entryId!);
      },
      icon: const FaIcon(FontAwesomeIcons.solidCopy),
      label: const Text("Extend with Duplicate"),
      color: Colors.blue,
    );
  }
}

class _ExtendWithEntry extends HookConsumerWidget {
  const _ExtendWithEntry();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return OutlineButton.icon(
      onPressed: () {
        final page = ref.read(currentPageProvider);
        if (page == null) return;
        final entryId = ref.read(inspectingEntryIdProvider);
        if (entryId.isNullOrEmpty) return;
        page.extendsWith(ref.passing, entryId!);
      },
      icon: const FaIcon(FontAwesomeIcons.plus),
      label: const Text("Extend with ..."),
      color: Colors.blue,
    );
  }
}

class _DeleteEntry extends HookConsumerWidget {
  const _DeleteEntry();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return FilledButton.icon(
      onPressed: () {
        final page = ref.read(currentPageProvider);
        if (page == null) return;
        final entryId = ref.read(inspectingEntryIdProvider);
        if (entryId.isNullOrEmpty) return;
        page.deleteEntryWithConfirmation(context, ref.passing, entryId!);
      },
      icon: const FaIcon(FontAwesomeIcons.trash),
      label: const Text("Delete Entry"),
      color: Theme.of(context).colorScheme.error,
    );
  }
}
