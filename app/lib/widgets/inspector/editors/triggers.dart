import "package:flutter/cupertino.dart";
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import 'package:typewriter/utils/extensions.dart';
import "package:typewriter/widgets/auto_complete.dart";
import "package:typewriter/widgets/entries_graph.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/editors.dart";

part "triggers.g.dart";

@riverpod
List<String> triggers(TriggersRef ref) {
  final graphableEntries = ref.watch(graphableEntriesProvider);

  return graphableEntries.expand((e) {
    final paths = ref.watch(modifierPathsProvider(e.type, "trigger"));

    return paths.expand((path) => e.getAll(path)).map((e) => e as String);
  }).toList();
}

class TriggerEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is PrimitiveField && info.hasModifier("trigger");

  @override
  Widget build(String path, FieldInfo info) => TriggerEditor(path: path, field: info as PrimitiveField);
}

class TriggerEditor extends HookConsumerWidget {
  const TriggerEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();

  final String path;
  final PrimitiveField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, ""));

    final modifier = field.getModifier("trigger");
    final isReceiver = modifier?.data == true;

    return AutoCompleteField(
      text: value,
      hintText: "Enter a trigger",
      icon: isReceiver ? FontAwesomeIcons.bolt : FontAwesomeIcons.towerBroadcast,
      inputFormatters: [
        if (field.hasModifier("snake_case")) snakeCaseFormatter(),
      ],
      onQuery: (_) => ref.read(triggersProvider),
      onChanged: (value) => ref.read(entryDefinitionProvider)?.updateField(ref, path, value),
    );
  }
}
