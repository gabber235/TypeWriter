import "package:collapsible/collapsible.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/deprecated/pages/inspection_menu.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";

class ObjectEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldType type) => type is ObjectField;

  @override
  Widget build(String path, FieldType type) => ObjectEditor(path: path, object: type as ObjectField);
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
    final expanded = useState(defaultExpanded);
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Material(
          borderRadius: BorderRadius.circular(4),
          child: InkWell(
            borderRadius: BorderRadius.circular(4),
            onTap: () => expanded.value = !expanded.value,
            child: Row(
              children: [
                Icon(expanded.value ? Icons.expand_less : Icons.expand_more),
                SectionTitle(
                  title: ref.watch(pathDisplayNameProvider(path, "Fields")),
                ),
              ],
            ),
          ),
        ),
        Collapsible(
          collapsed: !expanded.value,
          axis: CollapsibleAxis.vertical,
          maintainAnimation: true,
          child: Padding(
            padding: const EdgeInsets.only(left: 8),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                for (final field in object.fields.entries)
                  if (!ignoreFields.contains(field.key)) ...[
                    const SizedBox(height: 12),
                    if (!field.value.hasCustomLayout) ...[
                      SectionTitle(title: field.key.formatted),
                      const SizedBox(height: 8),
                    ],
                    FieldEditor(
                      key: ValueKey(
                        path.isNotEmpty ? "$path.${field.key}" : field.key,
                      ),
                      path: path.isNotEmpty ? "$path.${field.key}" : field.key,
                      type: field.value,
                    ),
                  ],
              ],
            ),
          ),
        ),
      ],
    );
  }
}
