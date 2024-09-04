import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
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
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is PrimitiveBlueprint &&
      (dataBlueprint.type == PrimitiveType.integer ||
          dataBlueprint.type == PrimitiveType.double);

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => NumberEditor(
        path: path,
        primitiveBlueprint: dataBlueprint as PrimitiveBlueprint,
      );
}

class NumberEditor extends HookConsumerWidget {
  const NumberEditor({
    required this.path,
    required this.primitiveBlueprint,
    super.key,
  }) : super();
  final String path;
  final PrimitiveBlueprint primitiveBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    useFocusedBasedCurrentEditingField(focus, ref.passing, path);

    final value = ref.watch(fieldValueProvider(path, 0));

    final isNegativeAllowed =
        primitiveBlueprint.get("negative") as bool? ?? true;

    return WritersIndicator(
      provider: fieldWritersProvider(path),
      shift: (_) => const Offset(15, 0),
      child: FormattedTextField(
        focus: focus,
        icon: TWIcons.hashtag,
        hintText: "Enter a ${primitiveBlueprint.type.name}",
        text: "$value",
        keyboardType: TextInputType.number,
        inputFormatters: [
          if (!isNegativeAllowed) ...[
            if (primitiveBlueprint.type == PrimitiveType.integer)
              FilteringTextInputFormatter.digitsOnly,
            if (primitiveBlueprint.type == PrimitiveType.double)
              FilteringTextInputFormatter.allow(RegExp(r"^\d+\.?\d*")),
          ] else ...[
            if (primitiveBlueprint.type == PrimitiveType.integer)
              FilteringTextInputFormatter.allow(RegExp(r"^-?\d*")),
            if (primitiveBlueprint.type == PrimitiveType.double)
              FilteringTextInputFormatter.allow(RegExp(r"^-?\d*\.?\d*")),
          ],
        ],
        onChanged: (value) {
          final number = primitiveBlueprint.type == PrimitiveType.integer
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
