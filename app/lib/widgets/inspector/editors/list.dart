import "package:flutter/material.dart" hide FilledButton;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/add_action.dart";
import "package:typewriter/widgets/inspector/headers/delete_action.dart";
import "package:typewriter/widgets/inspector/headers/duplicate_list_item_action.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "list.g.dart";

class ListEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) => dataBlueprint is ListBlueprint;

  @override
  Widget build(String path, DataBlueprint dataBlueprint) =>
      ListEditor(path: path, listBlueprint: dataBlueprint as ListBlueprint);
}

@riverpod
int _listValueLength(_ListValueLengthRef ref, String path) {
  return (ref.watch(fieldValueProvider(path)) as List<dynamic>? ?? []).length;
}

class ListEditor extends HookConsumerWidget {
  const ListEditor({
    required this.path,
    required this.listBlueprint,
    super.key,
  }) : super();
  final String path;
  final ListBlueprint listBlueprint;

  List<dynamic> _get(PassingRef ref) {
    return ref.read(fieldValueProvider(path)) as List<dynamic>? ?? [];
  }

  void _addNew(PassingRef ref) {
    ref.read(inspectingEntryDefinitionProvider)?.updateField(
      ref,
      path,
      [..._get(ref), listBlueprint.type.defaultValue()],
    );
  }

  void _reorderList(List<dynamic> value, int oldIndex, int newIndex) {
    final item = value.removeAt(oldIndex);
    if (newIndex > oldIndex) {
      value.insert(newIndex - 1, item);
    } else {
      value.insert(newIndex, item);
    }
  }

  void _reorder(
    PassingRef ref,
    int oldIndex,
    int newIndex,
  ) {
    final newValue = [..._get(ref)];
    _reorderList(newValue, oldIndex, newIndex);

    ref.read(inspectingEntryDefinitionProvider)?.updateField(
          ref,
          path,
          newValue,
        );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final length = ref.watch(_listValueLengthProvider(path));
    final globalKeys = useMemoized(
      () => List.generate(
        length,
        (index) => GlobalKey(debugLabel: "item-$index"),
      ),
      [length],
    );

    return FieldHeader(
      path: path,
      dataBlueprint: listBlueprint,
      canExpand: true,
      actions: [
        AddHeaderAction(
          path: path,
          onAdd: () => _addNew(ref.passing),
        ),
      ],
      child: ReorderableList(
        itemCount: length,
        onReorder: (oldIndex, newIndex) {
          _reorder(ref.passing, oldIndex, newIndex);
          _reorderList(globalKeys, oldIndex, newIndex);
        },
        shrinkWrap: true,
        itemBuilder: (context, index) => _ListItem(
          key: globalKeys[index],
          index: index,
          path: path,
          listBlueprint: listBlueprint,
        ),
      ),
    );
  }
}

class _ListItem extends HookConsumerWidget {
  const _ListItem({
    required this.index,
    required this.path,
    required this.listBlueprint,
    super.key,
  }) : super();

  final int index;
  final String path;
  final ListBlueprint listBlueprint;

  void _remove(PassingRef ref, int index) {
    final value = ref.read(fieldValueProvider(path)) as List<dynamic>? ?? [];
    ref.read(inspectingEntryDefinitionProvider)?.updateField(
          ref,
          path,
          [...value]..removeAt(index),
        );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final dataBlueprint = listBlueprint.type;
    final childPath = "$path.$index";
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: FieldHeader(
        dataBlueprint: dataBlueprint,
        path: childPath,
        canExpand: true,
        leading: [
          MouseRegion(
            cursor: SystemMouseCursors.grab,
            child: ReorderableDragStartListener(
              index: index,
              child: const Iconify(
                TWIcons.barsStaggered,
                size: 12,
                color: Colors.grey,
              ),
            ),
          ),
        ],
        actions: [
          DuplicateListItemAction(
            parentPath: path,
            path: childPath,
            dataBlueprint: dataBlueprint,
          ),
          RemoveHeaderAction(
            path: "$path.$index",
            onRemove: () => _remove(ref.passing, index),
          ),
        ],
        child: FieldEditor(
          path: "$path.$index",
          dataBlueprint: dataBlueprint,
        ),
      ),
    );
  }
}
