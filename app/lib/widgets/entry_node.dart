import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/context_menu_region.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/select_entries.dart";
import "package:typewriter/widgets/writers.dart";

class EntryNode extends HookConsumerWidget {
  const EntryNode({
    required this.entry,
    super.key,
  }) : super();
  final Entry entry;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = ref.watch(selectedEntryIdProvider.select((e) => e == entry.id));
    final blueprint = ref.watch(entryBlueprintProvider(entry.type));
    if (blueprint == null) {
      return Container();
    }

    final isSelectingEntries = ref.watch(isSelectingEntriesProvider);
    if (isSelectingEntries) {
      return _SelectingEntryNode(entry, blueprint);
    }

    return _EntryNode(
      id: entry.id,
      type: entry.type,
      backgroundColor: blueprint.color,
      foregroundColor: Colors.white,
      name: entry.name.formatted,
      icon: Icon(blueprint.icon, size: 18, color: Colors.white),
      isSelected: isSelected,
      onTap: () => ref.read(selectedEntryIdProvider.notifier).state = entry.id,
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
    this.onTap,
    super.key,
  });
  final String id;
  final String type;
  final Color backgroundColor;
  final Color foregroundColor;
  final String name;
  final Widget icon;
  final bool isSelected;
  final double opacity;

  final VoidCallback? onTap;

  void _extendsWithDuplicate(WidgetRef ref) {
    final pageId = ref.read(currentPageIdProvider);
    if (pageId == null) return;
    final entry = ref.read(entryProvider(pageId, id));
    if (entry == null) return;
    final triggerPaths = ref.read(modifierPathsProvider(entry.type, "trigger"));
    if (!triggerPaths.contains("triggers.*")) {
      debugPrint("Cannot duplicate entry with no triggers.*");
      return;
    }

    final newEntry = triggerPaths.fold(
      entry.copyWith("id", getRandomString()),
      (previousEntry, path) => previousEntry.copyMapped(path, (_) => null), // Remove all triggers
    );
    ref.read(currentPageProvider)?.insertEntry(ref.passing, newEntry);
    ref.read(communicatorProvider).createEntry(pageId, newEntry);

    final currentTriggers = entry.get("triggers");
    if (currentTriggers == null || currentTriggers is! List) return;
    final newTriggers = currentTriggers + [newEntry.id];
    final modifiedEntry = entry.copyWith("triggers", newTriggers);
    ref.read(currentPageProvider)?.insertEntry(ref.passing, modifiedEntry);
    ref.read(communicatorProvider).updateEntry(pageId, modifiedEntry.id, "triggers", newTriggers);
  }

  void _deleteEntry(BuildContext context, WidgetRef ref) {
    showConfirmationDialogue(
      context: context,
      title: "Delete Entry",
      content: "Are you sure you want to delete this entry?",
      confirmText: "Delete",
      onConfirm: () {
        final pageId = ref.read(currentPageIdProvider);
        if (pageId == null) return;
        final entry = ref.read(entryProvider(pageId, id));
        if (entry == null) return;
        ref.read(currentPageProvider)?.deleteEntry(ref.passing, entry);
        if (ref.read(selectedEntryIdProvider) == id) {
          ref.read(selectedEntryIdProvider.notifier).state = "";
        }
      },
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final triggerPaths = ref.read(modifierPathsProvider(type, "trigger"));

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
        builder: (context) {
          return [
            if (triggerPaths.contains("triggers.*"))
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
          child: Material(
            borderRadius: BorderRadius.circular(4),
            color: backgroundColor,
            child: Padding(
              padding: const EdgeInsets.all(4.0),
              child: AnimatedContainer(
                duration: const Duration(milliseconds: 400),
                curve: Curves.easeOutCirc,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(4),
                  border: Border.all(
                    color: isSelected ? Theme.of(context).scaffoldBackgroundColor : backgroundColor,
                    width: 3,
                  ),
                ),
                child: AnimatedOpacity(
                  duration: const Duration(milliseconds: 200),
                  curve: Curves.easeOutCirc,
                  opacity: opacity,
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
    );
  }
}

/// When the user is selecting entries, we will show a different entry node that allows them to select the entry.
class _SelectingEntryNode extends HookConsumerWidget {
  const _SelectingEntryNode(this.entry, this.blueprint, {super.key});
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
        name: entry.name.formatted,
        icon: Icon(blueprint.icon, size: 18, color: Colors.white),
        isSelected: isSelected,
        opacity: canSelect ? 1 : 0.6,
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
