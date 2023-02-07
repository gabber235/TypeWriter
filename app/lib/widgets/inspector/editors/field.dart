import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class FieldEditor extends HookConsumerWidget {
  const FieldEditor({
    required this.path,
    required this.type,
    super.key,
  }) : super();
  final String path;
  final FieldInfo type;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedEntryId = ref.watch(inspectingEntryIdProvider);
    final filters = ref.watch(editorFiltersProvider);

    final editor = filters.firstWhereOrNull((filter) => filter.canEdit(type))?.build(path, type);

    return WritersIndicator(
      filter: (writer) {
        if (writer.entryId.isNullOrEmpty) return false;
        if (writer.entryId != selectedEntryId) return false;
        if (writer.field.isNullOrEmpty) return false;
        return writer.field == path;
      },
      shift: (_) => const Offset(15, 0),
      child: editor ?? _NoEditorFound(path: path),
    );
  }
}

class _NoEditorFound extends StatelessWidget {
  const _NoEditorFound({
    required this.path,
  });

  final String path;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        color: Colors.redAccent,
        borderRadius: BorderRadius.circular(4),
      ),
      child: Text(
        "Could not find a editor for $path",
        style: Theme.of(context).textTheme.bodySmall,
      ),
    );
  }
}
