import "package:flutter/material.dart";
import "package:flutter_colorpicker/flutter_colorpicker.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class ColorEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "color";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => ColorEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class ColorEditor extends HookConsumerWidget {
  const ColorEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });

  final String path;
  final CustomBlueprint customBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final startColor =
        ref.watch(fieldValueProvider(path, customBlueprint.defaultValue));
    final pickerColor = startColor is int ? Color(startColor) : Colors.black;
    return FieldHeader(
      path: path,
      dataBlueprint: customBlueprint,
      canExpand: true,
      child: Padding(
        padding: const EdgeInsets.only(top: 12),
        child: LayoutBuilder(
          builder: (context, constraints) {
            return ColorPicker(
              pickerColor: pickerColor,
              colorPickerWidth: constraints.maxWidth,
              portraitOnly: true,
              labelTypes: const [],
              pickerAreaBorderRadius: BorderRadius.circular(4),
              onColorChanged: (color) {
                ref
                    .read(inspectingEntryDefinitionProvider)
                    ?.updateField(ref.passing, path, color.value);
              },
            );
          },
        ),
      ),
    );
  }
}
