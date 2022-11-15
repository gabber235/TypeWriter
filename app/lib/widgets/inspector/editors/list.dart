import "package:collapsible/collapsible.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/widgets/filled_button.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/listable_header.dart";

class ListEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldType type) => type is ListField;

  @override
  Widget build(String path, FieldType type) => ListEditor(path: path, field: type as ListField);
}

class ListEditor extends HookConsumerWidget {
  const ListEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();
  final String path;
  final ListField field;

  void _addNew(WidgetRef ref, List<dynamic> value) {
    ref.read(entryDefinitionProvider)?.updateField(
      ref,
      path,
      [...value, field.type.defaultValue],
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
    WidgetRef ref,
    List<dynamic> value,
    int oldIndex,
    int newIndex,
  ) {
    final newValue = [...value];
    _reorderList(newValue, oldIndex, newIndex);

    ref.read(entryDefinitionProvider)?.updateField(
          ref,
          path,
          newValue,
        );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, []));
    final expanded = useState(false);
    final globalKeys = useMemoized(
      () => List.generate(
        value.length,
        (index) => GlobalKey(debugLabel: "item-$index"),
      ),
      [value.length],
    );
    return Column(
      children: [
        ListableHeader(
          path: path,
          length: value.length,
          expanded: expanded,
          onAdd: () => _addNew(ref, value),
        ),
        Collapsible(
          collapsed: !expanded.value,
          axis: CollapsibleAxis.vertical,
          maintainAnimation: true,
          child: Padding(
            padding: const EdgeInsets.only(left: 8),
            child: ReorderableList(
              itemCount: value.length,
              onReorder: (oldIndex, newIndex) {
                _reorder(ref, value, oldIndex, newIndex);
                _reorderList(globalKeys, oldIndex, newIndex);
              },
              shrinkWrap: true,
              itemBuilder: (context, index) => _ListItem(
                key: globalKeys[index],
                index: index,
                value: value,
                path: path,
                field: field,
              ),
            ),
          ),
        ),
      ],
    );
  }
}

class _ListItem extends HookConsumerWidget {
  const _ListItem({
    required this.index,
    required this.value,
    required this.path,
    required this.field,
    super.key,
  }) : super();

  final int index;
  final dynamic value;
  final String path;
  final ListField field;

  void _remove(WidgetRef ref, List<dynamic> value, int index) {
    ref.read(entryDefinitionProvider)?.updateField(
          ref,
          path,
          [...value]..removeAt(index),
        );
  }

  Future<void> _checkRemove(
    BuildContext context,
    WidgetRef ref,
    List<dynamic> value,
    int index,
  ) async {
    final bool remove = await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text("Remove item?"),
        content: const Text("Are you sure you want to remove this item?"),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(false),
            child: const Text("Cancel"),
          ),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pop(true),
            icon: const Icon(FontAwesomeIcons.trash),
            label: const Text("Remove"),
            color: Theme.of(context).errorColor,
          ),
        ],
      ),
    );

    if (remove) {
      _remove(ref, value, index);
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final name = ref.watch(pathDisplayNameProvider("$path.$index"));
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Column(
        children: [
          Row(
            children: [
              MouseRegion(
                cursor: SystemMouseCursors.grab,
                child: ReorderableDragStartListener(
                  index: index,
                  child: const Icon(FontAwesomeIcons.barsStaggered, size: 12),
                ),
              ),
              const SizedBox(width: 8),
              Text(name, style: Theme.of(context).textTheme.caption),
              const Spacer(),
              IconButton(
                icon: const Icon(FontAwesomeIcons.trash, size: 12),
                color: Theme.of(context).errorColor,
                onPressed: () => _checkRemove(context, ref, value, index),
              ),
            ],
          ),
          FieldEditor(
            path: "$path.$index",
            type: field.type,
          ),
        ],
      ),
    );
  }
}
