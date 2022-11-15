import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/widgets/entry_node.dart";

part "entries_graph.g.dart";

@riverpod
List<Entry> graphableEntries(GraphableEntriesRef ref) {
  return ref.watch(currentPageProvider)?.graphableEntries ?? [];
}

class EntriesGraph extends HookConsumerWidget {
  const EntriesGraph({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entries = ref.watch(graphableEntriesProvider);
    return ListView(
      children: [
        for (final entry in entries) ...[
          EntryNode(entry: entry, key: ValueKey(entry.id)),
          const SizedBox(height: 8),
        ],
      ],
    );
  }
}
