import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/section_title.dart";
import "package:typewriter/widgets/writers.dart";

class ListableHeader extends HookConsumerWidget {
  const ListableHeader({
    required this.path,
    required this.length,
    required this.expanded,
    required this.onAdd,
    this.actions = const [],
    super.key,
  });

  final String path;
  final int length;
  final ValueNotifier<bool> expanded;
  final VoidCallback onAdd;

  final List<Widget> actions;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedEntryId = ref.watch(selectedEntryIdProvider);
    return WritersIndicator(
      filter: (writer) {
        // Only show when a writer is selecting a subfield while this is collapsed
        if (expanded.value) return false;
        if (writer.entryId.isNullOrEmpty) return false;
        if (writer.entryId != selectedEntryId) return false;
        if (writer.field.isNullOrEmpty) return false;
        return writer.field!.startsWith(path);
      },
      offset: const Offset(50, 25),
      child: Row(
        children: [
          Tooltip(
            message: expanded.value ? "Collapse" : "Expand",
            child: Material(
              borderRadius: BorderRadius.circular(4),
              child: InkWell(
                borderRadius: BorderRadius.circular(4),
                onTap: () => expanded.value = !expanded.value,
                child: Row(
                  children: [
                    Icon(
                      expanded.value ? Icons.expand_less : Icons.expand_more,
                    ),
                    SectionTitle(
                      title: ref.watch(pathDisplayNameProvider(path)),
                    ),
                    const SizedBox(width: 8),
                    Text(
                      "($length)",
                      style: Theme.of(context).textTheme.bodySmall,
                    ),
                    const SizedBox(width: 8),
                  ],
                ),
              ),
            ),
          ),
          const Spacer(),
          for (final action in actions) ...[
            action,
            const SizedBox(width: 8),
          ],
          IconButton(
            icon: const Icon(FontAwesomeIcons.plus, size: 16),
            tooltip: "Add new ${ref.watch(pathDisplayNameProvider(path)).singular}",
            onPressed: () {
              onAdd();
              // If we add a new item, we probably want to edit it.
              expanded.value = true;
            },
          ),
        ],
      ),
    );
  }
}
