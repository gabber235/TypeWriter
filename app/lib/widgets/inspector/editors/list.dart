import "package:collapsible/collapsible.dart";
import "package:flutter/material.dart" hide FilledButton;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/entry_selector.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/inspector.dart";
import "package:typewriter/widgets/inspector/listable_header.dart";

part "list.g.dart";

class ListEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is ListField;

  @override
  Widget build(String path, FieldInfo info) => ListEditor(path: path, field: info as ListField);
}

@riverpod
int _listValueLength(_ListValueLengthRef ref, String path) {
  return (ref.watch(fieldValueProvider(path)) as List<dynamic>? ?? []).length ?? 0;
}

class ListEditor extends HookConsumerWidget {
  const ListEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();
  final String path;
  final ListField field;

  List<dynamic> _get(PassingRef ref) {
    return ref.read(fieldValueProvider(path)) as List<dynamic>? ?? [];
  }

  void _addNew(PassingRef ref) {
    ref.read(inspectingEntryDefinitionProvider)?.updateField(
      ref,
      path,
      [..._get(ref), field.type.defaultValue],
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
    final expanded = useState(false);
    final globalKeys = useMemoized(
      () => List.generate(
        length,
        (index) => GlobalKey(debugLabel: "item-$index"),
      ),
      [length],
    );

    final isEntryList = field.hasModifier("entry-list");

    return Column(
      children: [
        ListableHeader(
          path: path,
          length: length,
          expanded: expanded,
          onAdd: () => _addNew(ref.passing),
          actions: [
            if (isEntryList) EntriesSelectorButton(path: path, tag: field.getModifier("entry-list")?.data ?? ""),
          ],
        ),
        Collapsible(
          collapsed: !expanded.value,
          axis: CollapsibleAxis.vertical,
          child: Padding(
            padding: const EdgeInsets.only(left: 8),
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
    required this.path,
    required this.field,
    super.key,
  }) : super();

  final int index;
  final String path;
  final ListField field;

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
              Text(name, style: Theme.of(context).textTheme.bodySmall),
              const Spacer(),
              IconButton(
                icon: const Icon(FontAwesomeIcons.trash, size: 12),
                color: Theme.of(context).colorScheme.error,
                onPressed: () => showConfirmationDialogue(
                  context: context,
                  title: "Remove item?",
                  content: "Are you sure you want to remove this item?",
                  onConfirm: () => _remove(ref.passing, index),
                ),
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
