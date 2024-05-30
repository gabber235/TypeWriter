import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:graphview/GraphView.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/widgets/components/app/empty_screen.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/entry_search.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";

part "manifest_view.g.dart";

@riverpod
List<Entry> manifestEntries(ManifestEntriesRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return [];

  return page.entries.where((entry) {
    final tags = ref.watch(entryBlueprintTagsProvider(entry.type));
    if (tags.isEmpty) {
      // Entries without a blueprint are always shown. So that the user can delete them.
      return true;
    }
    return tags.contains("manifest");
  }).toList();
}

@riverpod
List<String> manifestEntryIds(ManifestEntryIdsRef ref) {
  final entries = ref.watch(manifestEntriesProvider);
  return entries.map((entry) => entry.id).toList();
}

@riverpod
Set<String>? entryReferences(EntryReferencesRef ref, String entryId) {
  final entry = ref.watch(globalEntryProvider(entryId));
  if (entry == null) return null;

  final modifiers = ref.watch(modifierPathsProvider(entry.type, "entry"));
  return modifiers
      .expand(entry.getAll)
      .map((id) => id as String)
      .where((id) => id.isNotEmpty)
      .toSet();
}

@riverpod
Graph manifestGraph(ManifestGraphRef ref) {
  final entries = ref.watch(manifestEntriesProvider);
  final graph = Graph();

  for (final entry in entries) {
    final node = Node.Id(entry.id);
    graph.addNode(node);
  }

  for (final entry in entries) {
    final referenceEntryIds = ref.watch(entryReferencesProvider(entry.id));
    if (referenceEntryIds == null) continue;

    final color =
        ref.watch(entryBlueprintProvider(entry.type))?.color ?? Colors.grey;

    for (final referenceEntryId in referenceEntryIds) {
      final referenceTags = ref.watch(entryTagsProvider(referenceEntryId));
      // We only want to show references to manifest entries.
      if (!referenceTags.contains("manifest")) continue;

      graph.addEdge(
        Node.Id(entry.id),
        Node.Id(referenceEntryId),
        paint: Paint()..color = color,
      );
    }
  }

  return graph;
}

class ManifestView extends HookConsumerWidget {
  const ManifestView({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entryIds = ref.watch(manifestEntryIdsProvider);
    final graph = ref.watch(manifestGraphProvider);

    final builder = useMemoized(
      () => SugiyamaConfiguration()
        ..nodeSeparation = (40)
        ..levelSeparation = (40)
        ..orientation = SugiyamaConfiguration.ORIENTATION_TOP_BOTTOM,
    );

    if (entryIds.isEmpty) {
      return EmptyScreen(
        title: "There are no manifest entries on this page.",
        buttonText: "Add Entry",
        onButtonPressed: () => ref.read(searchProvider.notifier).asBuilder()
          ..fetchNewEntry()
          ..tag("manifest")
          ..open(),
      );
    }

    return InteractiveViewer(
      constrained: false,
      boundaryMargin: EdgeInsets.symmetric(
        horizontal: MediaQuery.of(context).size.width,
        vertical: MediaQuery.of(context).size.height,
      ),
      minScale: 0.0001,
      maxScale: 2.6,
      child: GraphView(
        graph: graph,
        algorithm: SugiyamaAlgorithm(builder),
        paint: Paint()
          ..color = Colors.green
          ..strokeWidth = 1
          ..style = PaintingStyle.stroke,
        builder: (node) {
          final id = node.key?.value as String?;
          if (id == null) return const NonExistentEntry();

          final entryOnPage = entryIds.contains(id);
          if (!entryOnPage) {
            final globalEntryWithPage =
                ref.watch(globalEntryWithPageProvider(id));
            if (globalEntryWithPage == null) {
              return const NonExistentEntry();
            }

            return ExternalEntryNode(
              pageId: globalEntryWithPage.key,
              entry: globalEntryWithPage.value,
            );
          }

          return EntryNode(
            entryId: id,
            key: ValueKey(id),
          );
        },
      ),
    );
  }
}
