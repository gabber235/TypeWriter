import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/widgets/components/general/error_box.dart";
import "package:typewriter/widgets/inspector/editors.dart";

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
    final filters = ref.watch(editorFiltersProvider);

    final editor = filters
        .firstWhereOrNull((filter) => filter.canEdit(type))
        ?.build(path, type);

    return editor ?? _NoEditorFound(path: path);
  }
}

class _NoEditorFound extends StatelessWidget {
  const _NoEditorFound({
    required this.path,
  });

  final String path;

  @override
  Widget build(BuildContext context) {
    return ErrorBox(message: "Could not find a editor for $path");
  }
}
