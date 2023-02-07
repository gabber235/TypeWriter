import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/select_entries.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general//context_menu_region.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class EntryNode extends HookConsumerWidget {
  const EntryNode({
    required this.entry,
    super.key,
  }) : super();
  final Entry entry;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = ref.watch(inspectingEntryIdProvider.select((e) => e == entry.id));
    final blueprint = ref.watch(entryBlueprintProvider(entry.type));
    if (blueprint == null) {
      return const InvalidEntry();
    }

    // If we are selecting entries then we will show a specialized widget to allow the user to select entries
    final isSelectingEntries = ref.watch(isSelectingEntriesProvider);
    if (isSelectingEntries) {
      return _SelectingEntryNode(entry, blueprint);
    }

    return _EntryNode(
      id: entry.id,
      type: entry.type,
      backgroundColor: blueprint.color,
      foregroundColor: Colors.white,
      name: entry.formattedName,
      icon: Icon(blueprint.icon, size: 18, color: Colors.white),
      isSelected: isSelected,
      onTap: () => ref.read(inspectingEntryIdProvider.notifier).select(entry),
    );
  }
}

class _EntryNode extends HookConsumerWidget {
  const _EntryNode({
    required this.id,
    required this.type,
    this.backgroundColor = Colors.grey,
    this.foregroundColor = Colors.black,
    this.name = "",
    this.icon = const Icon(Icons.book, color: Colors.white),
    this.isSelected = false,
    this.opacity = 1.0,
    this.enableContextMenu = true,
    this.onTap,
  });
  final String id;
  final String type;
  final Color backgroundColor;
  final Color foregroundColor;
  final String name;
  final Widget icon;
  final bool isSelected;
  final double opacity;
  final bool enableContextMenu;

  final VoidCallback? onTap;

  void _extendsWith(WidgetRef ref) {
    final page = ref.read(currentPageProvider);
    if (page == null) return;
    page.extendsWith(ref.passing, id);
  }

  void _extendsWithDuplicate(WidgetRef ref) {
    final page = ref.read(currentPageProvider);
    if (page == null) return;
    page.extendsWithDuplicate(ref.passing, id);
  }

  void _deleteEntry(BuildContext context, WidgetRef ref) {
    final page = ref.read(currentPageProvider);
    if (page == null) return;
    page.deleteEntryWithConfirmation(context, ref.passing, id);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final canTrigger = ref.read(modifierPathsProvider(type, "trigger").select((value) => value.contains("triggers.*")));
    final canBeTriggered =
        ref.watch(entryBlueprintProvider(type).select((b) => b?.tags.contains("triggerable") ?? false));

    return WritersIndicator(
      filter: (writer) {
        if (writer.entryId.isNullOrEmpty) return false;
        if (writer.entryId != id) return false;
        // If the writer has no field selected then we will always show them on the entry
        if (writer.field.isNullOrEmpty) return true;
        // Otherwise we will only show them if we are not inspecting this entry
        return !isSelected;
      },
      child: ContextMenuRegion(
        enabled: enableContextMenu,
        builder: (context) {
          return [
            if (canTrigger)
              ContextMenuTile.button(
                title: "Extend with ...",
                icon: FontAwesomeIcons.plus,
                onTap: () => _extendsWith(ref),
              ),
            if (canTrigger && canBeTriggered)
              ContextMenuTile.button(
                title: "Extend with Duplicate",
                icon: FontAwesomeIcons.copy,
                onTap: () => _extendsWithDuplicate(ref),
              ),
            ContextMenuTile.button(
              title: "Delete",
              icon: FontAwesomeIcons.trash,
              color: Colors.redAccent,
              onTap: () => _deleteEntry(context, ref),
            ),
          ];
        },
        child: GestureDetector(
          onTap: onTap,
          child: ColoredBox(
            color: Theme.of(context).scaffoldBackgroundColor,
            child: AnimatedOpacity(
              duration: 200.ms,
              curve: Curves.easeOutCirc,
              opacity: opacity,
              child: Material(
                borderRadius: BorderRadius.circular(4),
                color: backgroundColor,
                child: Padding(
                  padding: const EdgeInsets.all(4.0),
                  child: AnimatedContainer(
                    duration: 400.ms,
                    curve: Curves.easeOutCirc,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(4),
                      border: Border.all(
                        color: isSelected ? Theme.of(context).scaffoldBackgroundColor : backgroundColor,
                        width: 3,
                      ),
                    ),
                    child: Padding(
                      padding: const EdgeInsets.all(8),
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          icon,
                          const SizedBox(width: 12),
                          Text(
                            name,
                            style: TextStyle(fontSize: 13, color: foregroundColor),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

/// When the user is selecting entries, we will show a different entry node that allows them to select the entry.
class _SelectingEntryNode extends HookConsumerWidget {
  const _SelectingEntryNode(this.entry, this.blueprint);
  final Entry entry;
  final EntryBlueprint blueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = ref.watch(hasEntryInSelectionProvider(entry.id));
    final selectingTag = ref.watch(selectingTagProvider);
    final canSelect = blueprint.tags.contains(selectingTag);

    return MouseRegion(
      cursor: canSelect
          ? isSelected
              ? SystemMouseCursors.disappearing
              : SystemMouseCursors.copy
          : SystemMouseCursors.forbidden,
      child: _EntryNode(
        id: entry.id,
        type: entry.type,
        backgroundColor: blueprint.color,
        foregroundColor: Colors.white,
        name: entry.formattedName,
        icon: Icon(blueprint.icon, size: 18, color: Colors.white),
        isSelected: isSelected,
        opacity: canSelect ? 1 : 0.6,
        enableContextMenu: false,
        onTap: canSelect ? () => ref.read(entrySelectionProvider.notifier).toggleEntrySelection(entry.id) : null,
      ),
    );
  }
}

class FakeEntryNode extends HookConsumerWidget {
  const FakeEntryNode({required this.entry, super.key});

  final Entry entry;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final blueprint = ref.watch(entryBlueprintProvider(entry.type));

    if (blueprint == null) {
      return Material(
        color: Colors.redAccent,
        borderRadius: BorderRadius.circular(4),
        child: Text("Unknown entry type ${entry.type}"),
      );
    }

    return Material(
      borderRadius: BorderRadius.circular(4),
      color: blueprint.color,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
        child: Row(
          children: [
            Icon(blueprint.icon, color: Colors.white, size: 18),
            const SizedBox(width: 8),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  entry.formattedName,
                  style: const TextStyle(color: Colors.white, fontSize: 13),
                ),
                Text(
                  entry.type.formatted,
                  style: const TextStyle(color: Colors.white70, fontSize: 11),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class InvalidEntry extends StatelessWidget {
  const InvalidEntry({super.key});

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.redAccent,
      borderRadius: BorderRadius.circular(4),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Row(
          children: [
            const Icon(Icons.error, color: Colors.white),
            const SizedBox(width: 8),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text("Unknown entry", style: TextStyle(color: Colors.white)),
                Text(
                  "(this should never happen)",
                  style: Theme.of(context).textTheme.bodySmall?.copyWith(
                        color: Colors.white70,
                        fontStyle: FontStyle.italic,
                      ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class ExternalEntryNode extends HookConsumerWidget {
  const ExternalEntryNode({
    required this.pageId,
    required this.entry,
    super.key,
  });

  final String pageId;
  final Entry entry;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final blueprint = ref.watch(entryBlueprintProvider(entry.type));
    final page = ref.watch(pageProvider(pageId));
    final pageName = page?.name.formatted ?? "Unknown page";
    final isSelectingEntries = ref.watch(isSelectingEntriesProvider);

    if (blueprint == null) {
      return const InvalidEntry();
    }

    return ContextMenuRegion(
      enabled: !isSelectingEntries,
      builder: (context) {
        return [
          ContextMenuTile.button(
            title: "Delete Reference",
            icon: Icons.delete,
            color: Colors.redAccent,
            onTap: () => ref.read(currentPageProvider)?.removeReferencesTo(ref.passing, entry.id),
          ),
        ];
      },
      child: ColoredBox(
        color: Theme.of(context).scaffoldBackgroundColor,
        child: Material(
          animationDuration: 300.ms,
          color: blueprint.color.withOpacity(0.6),
          shape: RoundedRectangleBorder(
            side: BorderSide(color: blueprint.color, width: 3),
            borderRadius: BorderRadius.circular(4),
          ),
          child: InkWell(
            mouseCursor: SystemMouseCursors.click,
            onTap: () => ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref.passing, entry.id),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              child: Row(
                children: [
                  Icon(blueprint.icon, color: Colors.white, size: 18),
                  const SizedBox(width: 8),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        entry.formattedName,
                        style: const TextStyle(color: Colors.white, fontSize: 13),
                      ),
                      Text(
                        pageName,
                        style: const TextStyle(color: Colors.white70, fontSize: 11),
                      ),
                    ],
                  ),
                  const SizedBox(width: 16),
                  const Icon(Icons.open_in_new, color: Colors.white, size: 18),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
