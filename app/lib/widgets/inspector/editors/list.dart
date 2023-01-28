import "package:collapsible/collapsible.dart";
import "package:flutter/material.dart" hide FilledButton;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/listable_header.dart";
import "package:typewriter/widgets/select_entries.dart";

class ListEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is ListField;

  @override
  Widget build(String path, FieldInfo info) => ListEditor(path: path, field: info as ListField);
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
      ref.passing,
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
          ref.passing,
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

    final isEntryList = field.hasModifier("entry-list");

    return Column(
      children: [
        ListableHeader(
          path: path,
          length: value.length,
          expanded: expanded,
          onAdd: () => _addNew(ref, value),
          actions: [
            if (isEntryList) _EntriesSelectorButton(path: path, tag: field.getModifier("entry-list")?.data ?? ""),
          ],
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
          ref.passing,
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
                  onConfirm: () => _remove(ref, value, index),
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

class _EntriesSelectorButton extends HookConsumerWidget {
  const _EntriesSelectorButton({required this.path, required this.tag, super.key});

  final String path;
  final String tag;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Tooltip(
      message: "Select multiple entries",
      child: Material(
        borderRadius: BorderRadius.circular(4),
        color: Colors.deepPurple,
        child: InkWell(
          borderRadius: const BorderRadius.all(Radius.circular(4)),
          onTap: () async {
            final currentEntries = ref.watch(fieldValueProvider(path, [])) as List<dynamic>;
            final entryDefinition = ref.watch(entryDefinitionProvider);
            if (entryDefinition == null) return;

            ref.read(entrySelectionProvider.notifier).startSelection(
              tag,
              currentEntries.map((e) => e as String).toList(),
              (ref, selectedEntries) {
                ref.read(entryDefinitionProvider)?.updateField(
                      ref.passing,
                      path,
                      selectedEntries,
                    );
              },
            );
          },
          child: const Padding(
            padding: EdgeInsets.all(6.0),
            child: FaIcon(FontAwesomeIcons.objectGroup, size: 16),
          ),
        ),
      ),
    );
  }
}
