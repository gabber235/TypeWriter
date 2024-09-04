import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class BooleanEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is PrimitiveBlueprint &&
      dataBlueprint.type == PrimitiveType.boolean;

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => BooleanEditor(
        path: path,
        primitiveBlueprint: dataBlueprint as PrimitiveBlueprint,
      );
}

class BooleanEditor extends HookConsumerWidget {
  const BooleanEditor({
    required this.path,
    required this.primitiveBlueprint,
    super.key,
  }) : super();
  final String path;
  final PrimitiveBlueprint primitiveBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, false));

    return FieldHeader(
      path: path,
      dataBlueprint: primitiveBlueprint,
      trailing: [
        Row(
          children: [
            Checkbox(
              value: value,
              onChanged: (value) {
                ref
                    .read(inspectingEntryDefinitionProvider)
                    ?.updateField(ref.passing, path, value ?? false);
              },
            ),
            if (value)
              const Text("True", style: TextStyle(color: Colors.greenAccent))
            else
              const Text("False", style: TextStyle(color: Colors.grey)),
          ],
        ),
      ],
      child: const SizedBox(),
    );
  }
}
