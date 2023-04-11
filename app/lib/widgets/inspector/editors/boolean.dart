import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/help_info.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class BooleanEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is PrimitiveField && info.type == PrimitiveFieldType.boolean;

  @override
  Widget build(String path, FieldInfo info) => BooleanEditor(path: path, field: info as PrimitiveField);
}

class BooleanEditor extends HookConsumerWidget {
  const BooleanEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();
  final String path;
  final PrimitiveField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, false));

    final help = field.getModifier("help");
    final helpText = help?.data as String?;

    return Row(
      children: [
        SelectableText(ref.watch(pathDisplayNameProvider(path))),
        Checkbox(
          value: value,
          onChanged: (value) {
            ref.read(inspectingEntryDefinitionProvider)?.updateField(ref.passing, path, value ?? false);
          },
        ),
        if (value)
          const Text("True", style: TextStyle(color: Colors.greenAccent))
        else
          const Text("False", style: TextStyle(color: Colors.grey)),
        if (helpText != null) ...[
          const SizedBox(width: 4),
          HelpInfo(helpText: helpText),
        ],
      ],
    );
  }
}
