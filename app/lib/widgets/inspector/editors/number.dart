import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/formatted_text_field.dart";

class NumberEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is PrimitiveField && (info.type == PrimitiveFieldType.integer || info.type == PrimitiveFieldType.double);

  @override
  Widget build(String path, FieldInfo info) => IntegerEditor(path: path, field: info as PrimitiveField);
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
    final focus = useFocusNode();
    useFocusedBasedCurrentEditingField(focus, ref, path);
    final value = ref.watch(fieldValueProvider(path, ""));
    return FormattedTextField(
      focus: focus,
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
        ref.read(entryDefinitionProvider)?.updateField(ref.passing, path, number ?? 0);
      },
    );
  }
}
