import "package:collection/collection.dart";
import "package:flutter/material.dart" hide FilledButton;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/enum.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/editors/string.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/add_action.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class MapEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is MapField;

  @override
  Widget build(String path, FieldInfo info) =>
      MapEditor(path: path, field: info as MapField);
}

class MapEditor extends HookConsumerWidget {
  const MapEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();
  final String path;
  final MapField field;

  void _addNew(WidgetRef ref, Map<String, dynamic> value) {
    final key = field.key is EnumField
        ? (field.key as EnumField)
            .values
            .firstWhereOrNull((e) => !value.containsKey(e))
        : field.key.defaultValue;
    if (key == null) return;
    final val = field.value.defaultValue;
    ref.read(inspectingEntryDefinitionProvider)?.updateField(
      ref.passing,
      path,
      {
        ...value.map(MapEntry.new),
        key: val,
      },
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // ignore: provider_parameters
    final rawValue = ref.watch(fieldValueProvider(path, {}));

    // Since the map will be of the form {dynamic: dynamic}, we
    // need to recreate the map.
    final value = <String, dynamic>{
      ...rawValue.map((key, value) => MapEntry(key.toString(), value)),
    };

    // Create global keys for the different fields.
    // This is to keep the state of the fields when they are when a field is added or removed.
    final globalKeys = useMemoized(
      () => List.generate(
        value.length,
        (index) => GlobalKey(debugLabel: "item-$index"),
      ),
      [value.length],
    );

    return FieldHeader(
      field: field,
      path: path,
      canExpand: true,
      actions: [
        AddHeaderAction(
          path: path,
          onAdd: () => _addNew(ref, value),
        ),
      ],
      child: Column(
        children: value.entries
            .mapIndexed(
              (index, entry) => _MapEntry(
                key: globalKeys[index],
                index: index,
                map: value,
                entry: MapEntry(entry.key, entry.value),
                path: path,
                field: field,
              ),
            )
            .toList(),
      ),
    );
  }
}

class _MapEntry extends HookConsumerWidget {
  const _MapEntry({
    required this.index,
    required this.map,
    required this.entry,
    required this.path,
    required this.field,
    super.key,
  }) : super();
  final int index;
  final Map<String, dynamic> map;
  final MapEntry<String, dynamic> entry;
  final String path;
  final MapField field;

  void _changeKeyField(PassingRef ref, String key) {
    ref.read(inspectingEntryDefinitionProvider)?.updateField(
      ref,
      path,
      {
        ...map.map(MapEntry.new)..removeWhere((key, value) => key == entry.key),
        key: entry.value,
      },
    );
  }

  bool _alreadyContainsKey(String key) =>
      map.containsKey(key) && key != entry.key;

  Future<void> _changeKey(
    BuildContext context,
    PassingRef ref,
    String key,
  ) async {
    if (_alreadyContainsKey(key)) {
      final confirm = await showConfirmationDialogue(
        context: context,
        title: "Override key?",
        content:
            "The key '$key' already exists.\nThis will delete all the data from the existing key.",
        confirmIcon: TWIcons.warning,
        onConfirm: () => _changeKeyField(ref, key),
      );
      if (!confirm) return;
    }
    _changeKeyField(ref, key);
  }

  void _delete(PassingRef ref, Map<String, dynamic> value, String key) {
    final newValue = {
      ...value.map(MapEntry.new)..removeWhere((key, value) => key == entry.key),
    };

    ref.read(inspectingEntryDefinitionProvider)?.updateField(
          ref,
          path,
          newValue,
        );
  }

  Widget _keyEditor(BuildContext context, WidgetRef ref, String name) {
    if (field.key is PrimitiveField &&
        (field.key as PrimitiveField).type == PrimitiveFieldType.string) {
      return Flexible(
        child: _StringKey(
          path: path,
          field: field.key as PrimitiveField,
          value: entry.key,
          onChanged: (value) => _changeKey(context, ref.passing, value),
        ),
      );
    }
    if (field.key is EnumField) {
      return Flexible(
        child: _EnumKey(
          path: path,
          field: field.key as EnumField,
          value: entry.key,
          onChanged: (value) => _changeKey(context, ref.passing, value),
        ),
      );
    }

    return Text(name);
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
              const Iconify(TWIcons.barsStaggered, size: 12),
              const SizedBox(width: 8),
              _keyEditor(context, ref, name),
              IconButton(
                icon: const Iconify(TWIcons.trash, size: 12),
                color: Theme.of(context).colorScheme.error,
                onPressed: () => _delete(ref.passing, map, entry.key),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Padding(
            padding: const EdgeInsets.only(left: 24),
            child: FieldEditor(
              key: ValueKey("map-$index"),
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
  const _StringKey({
    required this.path,
    required this.field,
    required this.value,
    required this.onChanged,
  }) : super();
  final String path;
  final PrimitiveField field;
  final String value;
  final Function(String) onChanged;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return StringEditor(
      path: path,
      field: field,
      forcedValue: value,
      icon: TWIcons.key,
      hint: "Enter a key",
      onChanged: onChanged,
    );
  }
}

class _EnumKey extends HookConsumerWidget {
  const _EnumKey({
    required this.path,
    required this.field,
    required this.value,
    required this.onChanged,
  });
  final String path;
  final EnumField field;
  final String value;
  final Function(String) onChanged;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return EnumEditor(
      path: path,
      field: field,
      forcedValue: value,
      icon: TWIcons.key,
      onChanged: onChanged,
    );
  }
}
