import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/single_line_text_field.dart";

class NumberEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldType type) =>
      type is PrimitiveField && (type.type == PrimitiveFieldType.integer || type.type == PrimitiveFieldType.double);

  @override
  Widget build(String path, FieldType type) => IntegerEditor(path: path, field: type as PrimitiveField);
}

class IntegerEditor extends HookConsumerWidget {
  const IntegerEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();
  final String path;
  final PrimitiveField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, ""));
    return SingleLineTextField(
      icon: FontAwesomeIcons.hashtag,
      hintText: "Enter a ${field.type.name}",
      text: "$value",
      keyboardType: TextInputType.number,
      inputFormatters: [
        if (field.type == PrimitiveFieldType.integer) FilteringTextInputFormatter.digitsOnly,
        if (field.type == PrimitiveFieldType.double) FilteringTextInputFormatter.allow(RegExp(r"^\d+\.?\d*")),
      ],
      onChanged: (value) {
        final number = field.type == PrimitiveFieldType.integer ? int.tryParse(value) : double.tryParse(value);
        ref.read(entryDefinitionProvider)?.updateField(ref, path, number ?? 0);
      },
    );
  }
}
