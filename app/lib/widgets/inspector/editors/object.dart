import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/deprecated/pages/inspection_menu.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/utils/extensions.dart';
import 'package:typewriter/widgets/inspector/editors.dart';
import 'package:typewriter/widgets/inspector/editors/field.dart';

class ObjectEditorFilter extends EditorFilter {
  @override
  bool canFilter(FieldType type) => type is ObjectField;

  @override
  Widget build(String path, FieldType type) =>
      ObjectEditor(path: path, object: type as ObjectField);
}

class ObjectEditor extends HookConsumerWidget {
  final String path;
  final ObjectField object;
  final List<String> ignoreFields;

  const ObjectEditor({
    super.key,
    required this.path,
    required this.object,
    this.ignoreFields = const [],
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        for (final field in object.fields.entries)
          if (!ignoreFields.contains(field.key)) ...[
            const Divider(),
            SectionTitle(title: field.key.formatted),
            const SizedBox(height: 8),
            FieldEditor(
                key: ValueKey(
                    path.isNotEmpty ? "$path.${field.key}" : field.key),
                path: path.isNotEmpty ? "$path.${field.key}" : field.key,
                type: field.value),
          ],
      ],
    );
  }
}
