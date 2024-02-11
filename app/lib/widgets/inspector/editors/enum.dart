import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/dropdown.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class EnumEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is EnumField && info.values.isNotEmpty;

  @override
  Widget build(String path, FieldInfo info) => EnumEditor(
        path: path,
        field: info as EnumField,
      );
}

class EnumEditor extends HookConsumerWidget {
  const EnumEditor({
    required this.path,
    required this.field,
    this.forcedValue,
    this.icon = TWIcons.list,
    this.onChanged,
    super.key,
  }) : super();
  final String path;
  final EnumField field;

  final String? forcedValue;
  final String icon;
  final Function(String)? onChanged;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, field.values.first));
    return Dropdown<String>(
      icon: icon,
      value: forcedValue ?? value,
      values: field.values,
      builder: (context, value) => Padding(
        padding: const EdgeInsets.only(right: 8),
        child: Text(value),
      ),
      onChanged: onChanged ??
          (value) => ref
              .read(inspectingEntryDefinitionProvider)
              ?.updateField(ref.passing, path, value),
    );
  }
}
