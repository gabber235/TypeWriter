import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";

class ObjectEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) => dataBlueprint is ObjectBlueprint;

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => ObjectEditor(
        path: path,
        objectBlueprint: dataBlueprint as ObjectBlueprint,
      );
}

class ObjectEditor extends HookConsumerWidget {
  const ObjectEditor({
    required this.path,
    required this.objectBlueprint,
    this.ignoreFields = const [],
    this.defaultExpanded = false,
    super.key,
  }) : super();
  final String path;
  final ObjectBlueprint objectBlueprint;
  final List<String> ignoreFields;
  final bool defaultExpanded;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return FieldHeader(
      path: path,
      dataBlueprint: objectBlueprint,
      canExpand: true,
      defaultExpanded: defaultExpanded,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        spacing: 12,
        children: [
          for (final fieldBlueprint in objectBlueprint.fields.entries)
            if (!ignoreFields.contains(fieldBlueprint.key)) ...[
              if (!fieldBlueprint.value.hasCustomLayout)
                FieldHeader(
                  dataBlueprint: fieldBlueprint.value,
                  path: path.isNotEmpty
                      ? "$path.${fieldBlueprint.key}"
                      : fieldBlueprint.key,
                  child: buildFieldEditor(fieldBlueprint),
                )
              else
                buildFieldEditor(fieldBlueprint),
            ],
        ],
      ),
    );
  }

  FieldEditor buildFieldEditor(MapEntry<String, DataBlueprint> field) {
    return FieldEditor(
      key: ValueKey(
        path.isNotEmpty ? "$path.${field.key}" : field.key,
      ),
      path: path.isNotEmpty ? "$path.${field.key}" : field.key,
      dataBlueprint: field.value,
    );
  }
}
