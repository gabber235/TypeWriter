import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/dropdown.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class EnumEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is EnumBlueprint && dataBlueprint.values.isNotEmpty;

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => EnumEditor(
        path: path,
        enumBlueprint: dataBlueprint as EnumBlueprint,
      );
}

class EnumEditor extends HookConsumerWidget {
  const EnumEditor({
    required this.path,
    required this.enumBlueprint,
    this.forcedValue,
    this.icon = TWIcons.list,
    this.onChanged,
    super.key,
  }) : super();
  final String path;
  final EnumBlueprint enumBlueprint;

  final String? forcedValue;
  final String icon;
  final Function(String)? onChanged;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value =
        ref.watch(fieldValueProvider(path, enumBlueprint.values.first));
    return Dropdown<String>(
      icon: icon,
      value: forcedValue ?? value,
      values: enumBlueprint.values,
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
