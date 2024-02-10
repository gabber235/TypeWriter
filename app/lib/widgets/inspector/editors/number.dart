import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/formatted_text_field.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class NumberEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is PrimitiveField &&
      (info.type == PrimitiveFieldType.integer ||
          info.type == PrimitiveFieldType.double);

  @override
  Widget build(String path, FieldInfo info) =>
      NumberEditor(path: path, field: info as PrimitiveField);
}

class NumberEditor extends HookConsumerWidget {
  const NumberEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();
  final String path;
  final PrimitiveField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    useFocusedBasedCurrentEditingField(focus, ref.passing, path);
    final value = ref.watch(fieldValueProvider(path, ""));
    return WritersIndicator(
      provider: fieldWritersProvider(path),
      shift: (_) => const Offset(15, 0),
      child: FormattedTextField(
        focus: focus,
        icon: TWIcons.hashtag,
        hintText: "Enter a ${field.type.name}",
        text: "$value",
        keyboardType: TextInputType.number,
        inputFormatters: [
          if (field.type == PrimitiveFieldType.integer)
            FilteringTextInputFormatter.digitsOnly,
          if (field.type == PrimitiveFieldType.double)
            FilteringTextInputFormatter.allow(RegExp(r"^\d+\.?\d*")),
        ],
        onChanged: (value) {
          final number = field.type == PrimitiveFieldType.integer
              ? int.tryParse(value)
              : double.tryParse(value);
          ref
              .read(inspectingEntryDefinitionProvider)
              ?.updateField(ref.passing, path, number ?? 0);
        },
      ),
    );
  }
}
