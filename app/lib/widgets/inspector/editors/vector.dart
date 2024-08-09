import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/widgets/components/app/cord_property.dart";
import "package:typewriter/widgets/inspector/editors.dart";

class VectorEditorFilter extends EditorFilter {
  @override
  Widget build(String path, FieldInfo info) =>
      VectorEditor(path: path, field: info as CustomField);

  @override
  bool canEdit(FieldInfo info) =>
      info is CustomField && info.editor == "vector";
}

class VectorEditor extends HookConsumerWidget {
  const VectorEditor({
    required this.path,
    required this.field,
    super.key,
  });
  final String path;

  final CustomField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Row(
      children: [
        CordPropertyEditor(
          path: "$path.x",
          label: "X",
          color: Colors.red,
        ),
        const SizedBox(width: 8),
        CordPropertyEditor(
          path: "$path.y",
          label: "Y",
          color: Colors.green,
        ),
        const SizedBox(width: 8),
        CordPropertyEditor(
          path: "$path.z",
          label: "Z",
          color: Colors.blue,
        ),
      ],
    );
  }
}
