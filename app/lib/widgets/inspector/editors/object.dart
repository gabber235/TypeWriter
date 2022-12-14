import "package:collapsible/collapsible.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/extensions.dart";
import 'package:typewriter/widgets/inspector.dart';
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/section_title.dart";
import 'package:typewriter/widgets/writers.dart';

class ObjectEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is ObjectField;

  @override
  Widget build(String path, FieldInfo info) => ObjectEditor(path: path, object: info as ObjectField);
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
    final selectedEntryId = ref.watch(selectedEntryIdProvider);
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
            child: WritersIndicator(
              filter: (writer) {
                // Only show when a writer is selecting a subfield while this is collapsed
                if (expanded.value) return false;
                if (writer.entryId.isNullOrEmpty) return false;
                if (writer.entryId != selectedEntryId) return false;
                if (writer.field.isNullOrEmpty) return false;
                return writer.field!.startsWith(path);
              },
              offset: const Offset(15, 15),
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
