import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/formatted_text_field.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class StringEditorFilter extends EditorFilter {
  @override
  Widget build(String path, FieldInfo info) =>
      StringEditor(path: path, field: info as PrimitiveField);

  @override
  bool canEdit(FieldInfo info) =>
      info is PrimitiveField && info.type == PrimitiveFieldType.string;
}

class StringEditor extends HookConsumerWidget {
  const StringEditor({
    required this.path,
    required this.field,
    this.forcedValue,
    this.icon = TWIcons.pencil,
    this.hint = "",
    this.onChanged,
    super.key,
  }) : super();

  final String path;
  final PrimitiveField field;
  final String? forcedValue;

  final String icon;
  final String hint;
  final Function(String)? onChanged;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    useFocusedBasedCurrentEditingField(focus, ref.passing, path);
    final value = ref.watch(fieldValueProvider(path, ""));

    final singleLine = !field.hasModifier("multiline");

    return WritersIndicator(
      provider: fieldWritersProvider(path, exact: true),
      shift: (_) => const Offset(15, 0),
      child: FormattedTextField(
        focus: focus,
        icon: icon,
        hintText: hint.isNotEmpty ? hint : "Enter a ${field.type.name}",
        text: forcedValue ?? value,
        singleLine: singleLine,
        keyboardType: singleLine ? TextInputType.text : TextInputType.multiline,
        inputFormatters: [
          if (field.hasModifier("snake_case")) snakeCaseFormatter(),
        ],
        onChanged: onChanged ??
            (value) => ref
                .read(inspectingEntryDefinitionProvider)
                ?.updateField(ref.passing, path, value),
      ),
    );
  }
}
