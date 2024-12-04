import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/components/app/cord_property.dart";
import "package:typewriter/widgets/inspector/editors.dart";

class CoordinateEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "coordinate";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => CoordinateEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class CoordinateEditor extends HookConsumerWidget {
  const CoordinateEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });
  final String path;

  final CustomBlueprint customBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final withRotation = customBlueprint.hasModifier("with_rotation");

    return Column(
      children: [
        Row(
          children: [
            CordPropertyEditor(
              path: path.join("x"),
              label: "X",
              color: Colors.red,
            ),
            const SizedBox(width: 8),
            CordPropertyEditor(
              path: path.join("y"),
              label: "Y",
              color: Colors.green,
            ),
            const SizedBox(width: 8),
            CordPropertyEditor(
              path: path.join("z"),
              label: "Z",
              color: Colors.blue,
            ),
          ],
        ),
        if (withRotation) ...[
          const SizedBox(height: 8),
          Row(
            children: [
              CordPropertyEditor(
                path: path.join("yaw"),
                label: "Yaw",
                color: Colors.deepPurpleAccent,
              ),
              const SizedBox(width: 8),
              CordPropertyEditor(
                path: path.join("pitch"),
                label: "Pitch",
                color: Colors.amberAccent,
              ),
            ],
          ),
        ],
      ],
    );
  }
}
