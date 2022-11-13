import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/page_editor.dart';
import 'package:typewriter/widgets/entry_node.dart';

final entriesProvider = Provider.autoDispose<List<Entry>>((ref) {
  return ref.watch(pageProvider).graphableEntries;
}, dependencies: [pageProvider]);

class EntriesGraph extends HookConsumerWidget {
  const EntriesGraph({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entries = ref.watch(entriesProvider);
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
