import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/widgets/components/app/empty_screen.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/entry_search.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";

part "static_entries_list.g.dart";

@riverpod
List<String> _staticEntryIds(_StaticEntryIdsRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return [];

  return page.entries
      .where((entry) {
        final tags = ref.watch(entryBlueprintTagsProvider(entry.type));
        return tags.contains("static");
      })
      .map((entry) => entry.id)
      .toList();
}

class StaticEntriesList extends HookConsumerWidget {
  const StaticEntriesList({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entryIds = ref.watch(_staticEntryIdsProvider);

    if (entryIds.isEmpty) {
      return EmptyScreen(
        title: "There are no static entries on this page.",
        buttonText: "Add Entry",
        onButtonPressed: () => ref.read(searchProvider.notifier).asBuilder()
          ..fetchNewEntry()
          ..tag("static", canRemove: false)
          ..open(),
      );
    }

    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Wrap(
        spacing: 16,
        runSpacing: 16,
        children: entryIds.map((id) => EntryNode(entryId: id)).toList(),
      ),
    );
  }
}
