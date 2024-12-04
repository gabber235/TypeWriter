import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/extensions.dart";
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

  @override
  (HeaderActions, Iterable<(String, HeaderContext, DataBlueprint)>)
      headerActions(
    Ref<Object?> ref,
    String path,
    DataBlueprint dataBlueprint,
    HeaderContext context,
  ) {
    final objectBlueprint = dataBlueprint as ObjectBlueprint;
    final actions = super.headerActions(ref, path, dataBlueprint, context);
    final childContext = context.copyWith(parentBlueprint: dataBlueprint);
    final children = objectBlueprint.fields.entries.map(
      (entry) => (path.join(entry.key), childContext, entry.value),
    );

    return (actions.$1, actions.$2.followedBy(children));
  }
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
                  path: path.join(fieldBlueprint.key),
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
        path.isNotEmpty ? path.join(field.key) : field.key,
      ),
      path: path.isNotEmpty ? path.join(field.key) : field.key,
      dataBlueprint: field.value,
    );
  }
}
