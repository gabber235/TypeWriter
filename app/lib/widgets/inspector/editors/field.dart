import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/widgets/inspector/editors.dart';

class FieldEditor extends HookConsumerWidget {
  final String path;
  final FieldType type;

  const FieldEditor({
    super.key,
    required this.path,
    required this.type,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final filters = ref.watch(editorFiltersProvider);

    return filters
            .firstWhereOrNull((filter) => filter.canFilter(type))
            ?.build(path, type) ??
        Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(
            color: Colors.redAccent,
            borderRadius: BorderRadius.circular(4),
          ),
          child: Text('Could not find a editor for $path',
              style: Theme.of(context).textTheme.caption),
        );
  }
}
