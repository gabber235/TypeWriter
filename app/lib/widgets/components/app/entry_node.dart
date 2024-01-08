import "package:dotted_border/dotted_border.dart";
import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/entries_graph.dart";
import "package:typewriter/widgets/components/app/select_entries.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "entry_node.g.dart";

class EntryNode extends HookConsumerWidget {
  const EntryNode({
    required this.entryId,
    this.contextActions = const [],
    super.key,
  }) : super();

  final String entryId;
  final List<ContextMenuTile> contextActions;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected =
        ref.watch(inspectingEntryIdProvider.select((e) => e == entryId));
    final entryType = ref.watch(entryTypeProvider(entryId));
    if (entryType == null) return const InvalidEntry();

    final entryName = ref.watch(entryNameProvider(entryId));
    if (entryName == null) return const InvalidEntry();

    final blueprint = ref.watch(entryBlueprintProvider(entryType));
    if (blueprint == null) return const InvalidEntry();

    // If we are selecting entries then we will show a specialized widget to allow the user to select entries
    final isSelectingEntries = ref.watch(isSelectingEntriesProvider);
    if (isSelectingEntries) {
      return _SelectingEntryNode(entryId, entryType, entryName, blueprint);
    }

    return _EntryNode(
      id: entryId,
      type: entryType,
      backgroundColor: blueprint.color,
      foregroundColor: Colors.white,
      name: entryName.formatted,
      icon: Icon(blueprint.icon, size: 18),
      isSelected: isSelected,
      contextActions: contextActions,
      onTap: () =>
          ref.read(inspectingEntryIdProvider.notifier).selectEntry(entryId),
    );
  }
}

@riverpod
List<Writer> _writers(_WritersRef ref, String id) {
  final selectedEntryId = ref.watch(inspectingEntryIdProvider);

  return ref.watch(writersProvider).where((writer) {
    if (writer.entryId.isNullOrEmpty) return false;
    if (writer.entryId != id) return false;
    // If the writer has no field selected then we will always show them on the entry
    if (writer.field.isNullOrEmpty) return true;
    // Otherwise we will only show them if we are not inspecting this entry
    return selectedEntryId != id;
  }).toList();
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
    this.contextActions = const [],
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
  final List<ContextMenuTile> contextActions;

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
    final canTrigger = ref.watch(isTriggerEntryProvider(id));
    final canBeTriggered = ref.watch(isTriggerableEntryProvider(id));

    return WritersIndicator(
      provider: _writersProvider(id),
      child: LongPressDraggable(
        data: EntryDrag(entryId: id),
        feedback: FakeEntryNode(entryId: id),
        childWhenDragging: ColoredBox(
          color: Theme.of(context).scaffoldBackgroundColor,
          child: _placeholderEntry(context, backgroundColor),
        ),
        child: ContextMenuRegion(
          enabled: enableContextMenu,
          builder: (context) {
            return [
              ...contextActions,
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
          child: DragTarget<EntryDrag>(
            onWillAcceptWithDetails: (details) {
              if (!canTrigger) return false;
              final targetId = details.data.entryId;

              return ref.read(isTriggerableEntryProvider(targetId));
            },
            onAcceptWithDetails: (details) {
              final page = ref.read(currentPageProvider);
              if (page == null) return;
              final currentEntry = ref.read(globalEntryProvider(id));
              if (currentEntry == null) return;
              final targetEntry =
                  ref.read(globalEntryProvider(details.data.entryId));
              if (targetEntry == null) return;

              page.wireEntryToOtherEntry(
                ref.passing,
                currentEntry,
                targetEntry,
              );
            },
            builder: (context, candidateData, rejectedData) {
              if (rejectedData.isNotEmpty) {
                return MouseRegion(
                  cursor: SystemMouseCursors.forbidden,
                  child: Material(
                    color: Colors.grey,
                    borderRadius: BorderRadius.circular(4),
                    child: Padding(
                      padding: const EdgeInsets.all(7.0),
                      child: _innerEntry(foregroundColor),
                    ),
                  ),
                );
              }

              final isAccapting = candidateData.isNotEmpty;

              return MouseRegion(
                cursor: SystemMouseCursors.click,
                child: GestureDetector(
                  onTap: onTap,
                  child: ColoredBox(
                    color: Theme.of(context).scaffoldBackgroundColor,
                    child: AnimatedOpacity(
                      duration: 400.ms,
                      curve: Curves.easeOutCubic,
                      opacity: isAccapting ? 0.5 : opacity,
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
                                color: isSelected
                                    ? Theme.of(context).scaffoldBackgroundColor
                                    : backgroundColor,
                                width: 3,
                              ),
                            ),
                            child: _innerEntry(foregroundColor),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              );
            },
          ),
        ),
      ),
    );
  }

  Widget _placeholderEntry(BuildContext context, Color color) {
    return ColoredBox(
      color: Theme.of(context).scaffoldBackgroundColor,
      child: DottedBorder(
        color: color,
        borderType: BorderType.RRect,
        strokeWidth: 2,
        dashPattern: const [5, 5],
        radius: const Radius.circular(1),
        padding: const EdgeInsets.all(6),
        child: _innerEntry(color),
      ),
    );
  }

  Widget _innerEntry(Color color) {
    return Padding(
      padding: const EdgeInsets.all(8),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconTheme(
            data: IconThemeData(color: color),
            child: icon,
          ),
          const SizedBox(width: 12),
          Text(
            name,
            style: TextStyle(
              fontFamily: "JetBrainsMono",
              fontSize: 13,
              color: color,
            ),
          ),
        ],
      ),
    );
  }
}

/// When the user is selecting entries, we will show a different entry node that allows them to select the entry.
class _SelectingEntryNode extends HookConsumerWidget {
  const _SelectingEntryNode(this.entryId, this.type, this.name, this.blueprint);
  final String entryId;
  final String type;
  final String name;
  final EntryBlueprint blueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = ref.watch(hasEntryInSelectionProvider(entryId));
    final canSelect = ref.watch(canSelectEntryProvider(entryId));

    return MouseRegion(
      cursor: canSelect
          ? isSelected
              ? SystemMouseCursors.disappearing
              : SystemMouseCursors.copy
          : SystemMouseCursors.forbidden,
      child: _EntryNode(
        id: entryId,
        type: type,
        backgroundColor: blueprint.color,
        foregroundColor: Colors.white,
        name: name.formatted,
        icon: Icon(blueprint.icon, size: 18, color: Colors.white),
        isSelected: isSelected,
        opacity: canSelect ? 1 : 0.6,
        enableContextMenu: false,
        onTap: canSelect
            ? () => ref
                .read(entrySelectionProvider.notifier)
                .toggleEntrySelection(entryId)
            : null,
      ),
    );
  }
}

class FakeEntryNode extends HookConsumerWidget {
  const FakeEntryNode({required this.entryId, super.key});

  final String entryId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final type = ref.watch(entryTypeProvider(entryId));
    if (type == null) return const InvalidEntry();

    final name = ref.watch(entryNameProvider(entryId));
    if (name == null) return const InvalidEntry();

    final blueprint = ref.watch(entryBlueprintProvider(type));
    if (blueprint == null) return const InvalidEntry();

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
                  name.formatted,
                  style: const TextStyle(color: Colors.white, fontSize: 13),
                ),
                Text(
                  type.formatted,
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
                const Text(
                  "Unknown entry",
                  style: TextStyle(color: Colors.white),
                ),
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
    final pageName = page?.pageName.formatted ?? "Unknown page";
    final isSelectingEntries = ref.watch(isSelectingEntriesProvider);

    if (blueprint == null) {
      return const InvalidEntry();
    }

    return LongPressDraggable(
      data: EntryDrag(entryId: entry.id),
      feedback: FakeEntryNode(entryId: entry.id),
      childWhenDragging: DottedBorder(
        color: blueprint.color,
        borderType: BorderType.RRect,
        strokeWidth: 2,
        dashPattern: const [5, 5],
        radius: const Radius.circular(1),
        child: _innerEntry(blueprint, pageName, blueprint.color),
      ),
      child: ContextMenuRegion(
        enabled: !isSelectingEntries,
        builder: (context) {
          return [
            ContextMenuTile.button(
              title: "Delete Reference",
              icon: Icons.delete,
              color: Colors.redAccent,
              onTap: () => ref
                  .read(currentPageProvider)
                  ?.removeReferencesTo(ref.passing, entry.id),
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
              onTap: () => ref
                  .read(inspectingEntryIdProvider.notifier)
                  .navigateAndSelectEntry(ref.passing, entry.id),
              child: _innerEntry(blueprint, pageName, Colors.white),
            ),
          ),
        ),
      ),
    );
  }

  Widget _innerEntry(EntryBlueprint blueprint, String pageName, Color color) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      child: Row(
        children: [
          Icon(blueprint.icon, color: color, size: 18),
          const SizedBox(width: 8),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                entry.formattedName,
                style: TextStyle(
                  color: color,
                  fontSize: 13,
                ),
              ),
              Text(
                pageName,
                style: TextStyle(
                  color: color.withOpacity(0.7),
                  fontSize: 11,
                ),
              ),
            ],
          ),
          const SizedBox(width: 16),
          Icon(
            Icons.open_in_new,
            color: color,
            size: 18,
          ),
        ],
      ),
    );
  }
}

class EntryDrag {
  const EntryDrag({
    required this.entryId,
  });

  final String entryId;
}
