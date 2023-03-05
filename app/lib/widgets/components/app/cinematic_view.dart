import "package:flutter/cupertino.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/widgets/components/app/empty_screen.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";

part "cinematic_view.g.dart";

@riverpod
List<String> _cinematicEntryIds(_CinematicEntryIdsRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return [];

  return page.entries
      .where((entry) {
        final tags = ref.watch(entryTagsProvider(entry.type));
        return tags.contains("cinematic");
      })
      .map((entry) => entry.id)
      .toList();
}

class CinematicView extends HookConsumerWidget {
  const CinematicView({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entryIds = ref.watch(_cinematicEntryIdsProvider);

    if (entryIds.isEmpty) {
      return EmptyScreen(
        title: "There are no cinematic entries on this page.",
        buttonText: "Add Entry",
        onButtonPressed: () => ref.read(searchProvider.notifier).asBuilder()
          ..fetchNewEntry()
          ..tag("cinematic", canRemove: false)
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
