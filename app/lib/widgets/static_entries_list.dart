import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/widgets/empty_screen.dart";
import "package:typewriter/widgets/entry_node.dart";
import "package:typewriter/widgets/search_bar.dart";

part "static_entries_list.g.dart";

@riverpod
List<Entry> staticEntries(StaticEntriesRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return [];

  return page.entries.where((entry) {
    final tags = ref.watch(entryTagsProvider(entry.type));
    return tags.contains("static");
  }).toList();
}

class StaticEntriesList extends HookConsumerWidget {
  const StaticEntriesList({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entries = ref.watch(staticEntriesProvider);

    if (entries.isEmpty) {
      return EmptyScreen(
        title: "There are no static entries on this page.",
        buttonText: "Add Entry",
        onButtonPressed: () => ref.read(searchingProvider.notifier).startSearch("static:"),
      );
    }

    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Wrap(
        spacing: 16,
        runSpacing: 16,
        children: entries.map((entry) => EntryNode(entry: entry)).toList(),
      ),
    );
  }
}
