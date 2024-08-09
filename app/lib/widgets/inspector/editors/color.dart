import "package:flutter/material.dart";
import "package:flutter_colorpicker/flutter_colorpicker.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class ColorEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is CustomField && info.editor == "color";

  @override
  Widget build(String path, FieldInfo info) =>
      ColorEditor(path: path, field: info as CustomField);
}

class ColorEditor extends HookConsumerWidget {
  const ColorEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final CustomField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final startColor = ref.watch(fieldValueProvider(path, field.defaultValue));
    final pickerColor = startColor is int ? Color(startColor) : Colors.black;
    return FieldHeader(
      path: path,
      field: field,
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
