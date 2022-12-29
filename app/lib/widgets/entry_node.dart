import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector.dart";
import 'package:typewriter/widgets/writers.dart';

class EntryNode extends HookConsumerWidget {
  const EntryNode({
    required this.entry,
    super.key,
  }) : super();
  final Entry entry;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final adapterEntry = ref.watch(entryBlueprintProvider(entry.type));
    if (adapterEntry == null) {
      return Container();
    }
    return _EntryNode(
      id: entry.id,
      backgroundColor: adapterEntry.color,
      foregroundColor: Colors.white,
      name: entry.name.formatted,
      icon: Icon(adapterEntry.icon, size: 18, color: Colors.white),
    );
  }
}

class _EntryNode extends HookConsumerWidget {
  const _EntryNode({
    required this.id,
    this.backgroundColor = Colors.grey,
    this.foregroundColor = Colors.black,
    this.name = "",
    this.icon = const Icon(Icons.book, color: Colors.white),
  });
  final String id;
  final Color backgroundColor;
  final Color foregroundColor;
  final String name;
  final Widget icon;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = ref.watch(selectedEntryIdProvider.select((e) => e == id));

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
        onTap: () => ref.read(selectedEntryIdProvider.notifier).state = id,
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
    );
  }
}
