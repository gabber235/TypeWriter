import 'package:collapsible/collapsible.dart';
import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/deprecated/pages/inspection_menu.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/widgets/filled_button.dart';
import 'package:typewriter/widgets/inspector.dart';
import 'package:typewriter/widgets/inspector/editors.dart';
import 'package:typewriter/widgets/inspector/editors/enum.dart';
import 'package:typewriter/widgets/inspector/editors/field.dart';
import 'package:typewriter/widgets/inspector/editors/string.dart';

class MapEditorFilter extends EditorFilter {
  @override
  bool canFilter(FieldType type) => type is MapField;

  @override
  Widget build(String path, FieldType type) =>
      MapEditor(path: path, field: type as MapField);
}

class MapEditor extends HookConsumerWidget {
  final String path;
  final MapField field;

  const MapEditor({
    super.key,
    required this.path,
    required this.field,
  }) : super();

  void _addNew(WidgetRef ref, Map<String, dynamic> value) {
    final key = field.key is EnumField
        ? (field.key as EnumField)
            .values
            .firstWhereOrNull((e) => !value.containsKey(e))
        : field.key.defaultValue;
    if (key == null) return;
    final val = field.value.defaultValue;
    ref.read(entryDefinitionProvider)?.updateField(
      ref,
      path,
      {
        ...value.map((key, value) => MapEntry(key, value)),
        key: val,
      },
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final rawValue = ref.watch(fieldValueProvider(path, {}));

    // Since the map will be of the form {dynamic: dynamic}, we
    // need to recreate the map.
    Map<String, dynamic> value = {
      ...rawValue.map((key, value) => MapEntry(key, value)),
    };

    final expanded = useState(false);
    final globalKeys = useMemoized(
      () => List.generate(
          value.length, (index) => GlobalKey(debugLabel: 'item-$index')),
      [value.length],
    );

    return Column(
      children: [
        Row(
          children: [
            Material(
              borderRadius: BorderRadius.circular(4),
              child: InkWell(
                borderRadius: BorderRadius.circular(4),
                onTap: () => expanded.value = !expanded.value,
                child: Row(
                  children: [
                    Icon(
                        expanded.value ? Icons.expand_less : Icons.expand_more),
                    SectionTitle(
                        title: ref.watch(pathDisplayNameProvider(path))),
                    const SizedBox(width: 8),
                    Text("(${value.length})",
                        style: Theme.of(context).textTheme.caption),
                    const SizedBox(width: 8),
                  ],
                ),
              ),
            ),
            const Spacer(),
            IconButton(
              icon: const Icon(FontAwesomeIcons.plus, size: 16),
              onPressed: () {
                _addNew(ref, value);
              },
            ),
          ],
        ),
        Collapsible(
          collapsed: !expanded.value,
          axis: CollapsibleAxis.vertical,
          child: Padding(
            padding: const EdgeInsets.only(left: 8),
            child: Column(
              children: value.entries
                  .mapIndexed((index, entry) => _MapEntry(
                        key: globalKeys[index],
                        index: index,
                        map: value,
                        entry: MapEntry(entry.key, entry.value),
                        path: path,
                        field: field,
                      ))
                  .toList(),
            ),
          ),
        ),
      ],
    );
  }
}

class _MapEntry extends HookConsumerWidget {
  final int index;
  final Map<String, dynamic> map;
  final MapEntry<String, dynamic> entry;
  final String path;
  final MapField field;

  const _MapEntry({
    super.key,
    required this.index,
    required this.map,
    required this.entry,
    required this.path,
    required this.field,
  }) : super();

  _changeKeyField(WidgetRef ref, String key) {
    ref.read(entryDefinitionProvider)?.updateField(
      ref,
      path,
      {
        ...map.map((key, value) => MapEntry(key, value))
          ..removeWhere((key, value) => key == entry.key),
        key: entry.value,
      },
    );
  }

  _alreadyContainsKey(String key) {
    return map.containsKey(key) && key != entry.key;
  }

  Future<bool?> _showOverrideConfirmation(BuildContext context, String key) {
    return showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text("Override key?"),
        content: Text(
            "The key '$key' already exists.\nThis will delete all the data from the existing key."),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(false),
            child: const Text("Cancel"),
          ),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pop(true),
            icon: const Icon(FontAwesomeIcons.triangleExclamation),
            label: const Text("Override"),
            color: Theme.of(context).errorColor,
          ),
        ],
      ),
    );
  }

  _changeKey(BuildContext context, WidgetRef ref, String key) async {
    if (_alreadyContainsKey(key)) {
      final override = await _showOverrideConfirmation(context, key);
      if (override != true) return;
    }
    _changeKeyField(ref, key);
  }

  void _delete(WidgetRef ref, Map<String, dynamic> value, String key) {
    final newValue = {
      ...value.map((key, value) => MapEntry(key, value))
        ..removeWhere((key, value) => key == entry.key),
    };

    ref.read(entryDefinitionProvider)?.updateField(
          ref,
          path,
          newValue,
        );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final name = ref.watch(pathDisplayNameProvider("$path.${entry.key}"));
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Column(
        children: [
          Row(
            children: [
              const Icon(FontAwesomeIcons.barsStaggered, size: 12),
              const SizedBox(width: 8),
              if (field.key is PrimitiveField &&
                  (field.key as PrimitiveField).type ==
                      PrimitiveFieldType.string)
                Flexible(
                  child: _StringKey(
                    path: "$path.${entry.key}",
                    field: field.key as PrimitiveField,
                    value: entry.key,
                    onChanged: (value) => _changeKey(context, ref, value),
                  ),
                )
              else if (field.key is EnumField)
                Flexible(
                  child: _EnumKey(
                    path: "$path.${entry.key}",
                    field: field.key as EnumField,
                    value: entry.key,
                    onChanged: (value) => _changeKey(context, ref, value),
                  ),
                )
              else
                Text(name),
              IconButton(
                icon: const Icon(FontAwesomeIcons.trash, size: 12),
                color: Theme.of(context).errorColor,
                onPressed: () => _delete(ref, map, entry.key),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Padding(
            padding: const EdgeInsets.only(left: 24),
            child: FieldEditor(
              key: ValueKey("map-${index}"),
              path: "$path.${entry.key}",
              type: field.value,
            ),
          ),
        ],
      ),
    );
  }
}

class _StringKey extends HookConsumerWidget {
  final String path;
  final PrimitiveField field;
  final String value;
  final Function(String) onChanged;

  const _StringKey({
    super.key,
    required this.path,
    required this.field,
    required this.value,
    required this.onChanged,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return StringEditor(
      path: path,
      field: field,
      forcedValue: value,
      icon: FontAwesomeIcons.key,
      hint: "Enter a key",
      onChanged: onChanged,
    );
  }
}

class _EnumKey extends HookConsumerWidget {
  final String path;
  final EnumField field;
  final String value;
  final Function(String) onChanged;

  const _EnumKey({
    Key? key,
    required this.path,
    required this.field,
    required this.value,
    required this.onChanged,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return EnumEditor(
      path: path,
      field: field,
      forcedValue: value,
      icon: FontAwesomeIcons.key,
      onChanged: onChanged,
    );
  }
}
