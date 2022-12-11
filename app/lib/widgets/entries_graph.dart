import "package:flutter/material.dart";
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:graphview/GraphView.dart';
import "package:hooks_riverpod/hooks_riverpod.dart";
import 'package:rive/rive.dart';
import "package:riverpod_annotation/riverpod_annotation.dart";
import 'package:typewriter/models/adapter.dart';
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import 'package:typewriter/widgets/entry_node.dart';
import 'package:typewriter/widgets/filled_button.dart';
import 'package:typewriter/widgets/search_bar.dart';

part "entries_graph.g.dart";

@riverpod
List<Entry> graphableEntries(GraphableEntriesRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return [];

  return page.entries.where((entry) {
    final paths = ref.watch(modifierPathsProvider(entry.type, "trigger"));
    return paths.isNotEmpty;
  }).toList();
}

@riverpod
List<String> triggerPaths(TriggerPathsRef ref, String type, bool isReceiver) {
  final modifiers = ref.watch(fieldModifiersProvider(type, "trigger"));
  return modifiers.entries.where((entry) => entry.value.data == isReceiver).map((entry) => entry.key).toList();
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
    final triggers = ref.watch(triggerPathsProvider(entry.type, false)).expand(entry.getAll);

    final color = ref.watch(entryBlueprintProvider(entry.type))?.color ?? Colors.grey;

    for (final trigger in triggers) {
      final receivers =
          entries.where((e) => ref.watch(triggerPathsProvider(e.type, true)).expand(e.getAll).contains(trigger));

      for (final receiver in receivers) {
        graph.addEdge(Node.Id(entry.id), Node.Id(receiver.id), paint: Paint()..color = color);
      }
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

    if (entries.isEmpty) return const _EmptyScreen();

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
          final entry = entries.firstWhere((entry) => entry.id == id);
          return EntryNode(
            entry: entry,
            key: ValueKey(entry.id),
          );
        },
      ),
    );
  }
}

class _EmptyScreen extends HookConsumerWidget {
  const _EmptyScreen({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      children: [
        const Spacer(),
        const Expanded(
          flex: 2,
          child: RiveAnimation.asset(
            "assets/cute_robot.riv",
            stateMachines: ["Motion"],
          ),
        ),
        const Text(
          "There are no graphable entries in this page.",
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 24),
        FilledButton.icon(
          label: const Text("Add Entry"),
          onPressed: () => ref.read(searchingProvider.notifier).startSearch(),
          icon: const Icon(FontAwesomeIcons.plus),
        ),
        const Spacer(),
      ],
    );
  }
}
