import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/widgets/components/app/cord_property.dart";
import "package:typewriter/widgets/inspector/editors.dart";

class VectorEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "vector";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => VectorEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class VectorEditor extends HookConsumerWidget {
  const VectorEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });
  final String path;
  final CustomBlueprint customBlueprint;

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
