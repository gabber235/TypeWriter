import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";

class ObjectEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is ObjectField;

  @override
  Widget build(String path, FieldInfo info) =>
      ObjectEditor(path: path, object: info as ObjectField);
}

class ObjectEditor extends HookConsumerWidget {
  const ObjectEditor({
    required this.path,
    required this.object,
    this.ignoreFields = const [],
    this.defaultExpanded = false,
    super.key,
  }) : super();
  final String path;
  final ObjectField object;
  final List<String> ignoreFields;
  final bool defaultExpanded;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return FieldHeader(
      field: object,
      path: path,
      canExpand: true,
      defaultExpanded: defaultExpanded,
      child: Padding(
        padding: const EdgeInsets.only(bottom: 4),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            for (final field in object.fields.entries)
              if (!ignoreFields.contains(field.key)) ...[
                const SizedBox(height: 12),
                if (!field.value.hasCustomLayout)
                  FieldHeader(
                    field: field.value,
                    path: path.isNotEmpty ? "$path.${field.key}" : field.key,
                    child: buildFieldEditor(field),
                  )
                else
                  buildFieldEditor(field),
              ],
          ],
        ),
      ),
    );
  }

  FieldEditor buildFieldEditor(MapEntry<String, FieldInfo> field) {
    return FieldEditor(
      key: ValueKey(
        path.isNotEmpty ? "$path.${field.key}" : field.key,
      ),
      path: path.isNotEmpty ? "$path.${field.key}" : field.key,
      type: field.value,
    );
  }
}
