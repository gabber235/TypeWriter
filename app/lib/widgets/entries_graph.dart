import 'package:collection/collection.dart';
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:graphview/GraphView.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/widgets/empty_screen.dart";
import "package:typewriter/widgets/entry_node.dart";
import "package:typewriter/widgets/search_bar.dart";

part "entries_graph.g.dart";

@riverpod
List<Entry> graphableEntries(GraphableEntriesRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return [];

  return page.entries.where((entry) {
    final tags = ref.watch(entryTagsProvider(entry.type));
    return tags.contains("trigger");
  }).toList();
}

@riverpod
Graph graph(GraphRef ref) {
  final entries = ref.watch(graphableEntriesProvider);
  final graph = Graph();

  for (final entry in entries) {
    final node = Node.Id(entry.id);
    graph.addNode(node);
  }

  for (final entry in entries) {
    final modifiers = ref.watch(fieldModifiersProvider(entry.type, "trigger"));
    final triggeredEntryIds =
        modifiers.entries.map((e) => e.key).expand(entry.getAll).where((id) => id.isNotEmpty).toSet();

    final color = ref.watch(entryBlueprintProvider(entry.type))?.color ?? Colors.grey;

    for (final triggeredEntryId in triggeredEntryIds) {
      graph.addEdge(Node.Id(entry.id), Node.Id(triggeredEntryId), paint: Paint()..color = color);
    }
  }

  return graph;
}

class EntriesGraph extends HookConsumerWidget {
  const EntriesGraph({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entries = ref.watch(graphableEntriesProvider);
    final graph = ref.watch(graphProvider);

    final builder = useMemoized(
      () => SugiyamaConfiguration()
        ..nodeSeparation = (40)
        ..levelSeparation = (40)
        ..orientation = SugiyamaConfiguration.ORIENTATION_LEFT_RIGHT,
    );

    if (entries.isEmpty) {
      return EmptyScreen(
        title: "There are no graphable entries on this page.",
        buttonText: "Add Entry",
        onButtonPressed: () => ref.read(searchingProvider.notifier).startSearch("trigger:"),
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
          final id = node.key!.value as String?;
          final entry = entries.firstWhereOrNull((entry) => entry.id == id);
          if (entry == null) return const SizedBox();
          return EntryNode(
            entry: entry,
            key: ValueKey(entry.id),
          );
        },
      ),
    );
  }
}
