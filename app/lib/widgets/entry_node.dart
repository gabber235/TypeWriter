import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/extensions.dart";
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
      backgroundColor: blueprint.color,
      foregroundColor: Colors.white,
      name: entry.name.formatted,
      icon: Icon(blueprint.icon, size: 18, color: Colors.white),
      isSelected: isSelected,
      onTap: () => ref.read(selectedEntryIdProvider.notifier).state = entry.id,
    );
  }
}

class _EntryNode extends HookWidget {
  const _EntryNode({
    required this.id,
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
  final Color backgroundColor;
  final Color foregroundColor;
  final String name;
  final Widget icon;
  final bool isSelected;
  final double opacity;

  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return WritersIndicator(
      filter: (writer) {
        if (writer.entryId.isNullOrEmpty) return false;
        if (writer.entryId != id) return false;
        // If the writer has no field selected then we will always show them on the entry
        if (writer.field.isNullOrEmpty) return true;
        // Otherwise we will only show them if we are not inspecting this entry
        return !isSelected;
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
