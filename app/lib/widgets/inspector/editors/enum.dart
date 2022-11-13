import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/deprecated/widgets/dropdown.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/widgets/inspector/editors.dart';

import '../../inspector.dart';

class EnumEditorFilter extends EditorFilter {
  @override
  bool canFilter(FieldType type) => type is EnumField && type.values.isNotEmpty;

  @override
  Widget build(String path, FieldType type) => EnumEditor(
        path: path,
        field: type as EnumField,
      );
}

class EnumEditor extends HookConsumerWidget {
  final String path;
  final EnumField field;

  const EnumEditor({
    super.key,
    required this.path,
    required this.field,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, field.values.first));
    return Dropdown<String>(
      value: value,
      values: field.values,
      onChanged: (value) =>
          ref.read(entryDefinitionProvider)?.updateField(ref, path, value),
    );
  }
}
