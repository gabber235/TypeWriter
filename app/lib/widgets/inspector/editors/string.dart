import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/widgets/inspector.dart';
import 'package:typewriter/widgets/inspector/editors.dart';
import 'package:typewriter/widgets/inspector/single_line_text_field.dart';

class StringEditorFilter extends EditorFilter {
  @override
  bool canFilter(FieldType type) =>
      type is PrimitiveField && type.type == PrimitiveFieldType.string;

  @override
  Widget build(String path, FieldType type) =>
      StringEditor(path: path, field: type as PrimitiveField);
}

class StringEditor extends HookConsumerWidget {
  final String path;
  final PrimitiveField field;

  final String? forcedValue;
  final IconData icon;
  final String hint;
  final Function(String)? onChanged;

  const StringEditor({
    super.key,
    required this.path,
    required this.field,
    this.forcedValue,
    this.icon = FontAwesomeIcons.pencil,
    this.hint = "",
    this.onChanged,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, ""));
    return SingleLineTextField(
      icon: icon,
      hintText: hint.isNotEmpty ? hint : "Enter a ${field.type.name}",
      text: forcedValue ?? value,
      onChanged: onChanged ??
          (value) =>
              ref.read(entryDefinitionProvider)?.updateField(ref, path, value),
    );
  }
}
