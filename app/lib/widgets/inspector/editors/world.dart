import "package:flutter/material.dart";
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

class WorldEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "world";
  @override
  Widget build(String path, DataBlueprint dataBlueprint) => WorldEditor(
        path: path,
      );
}

class WorldEditor extends HookConsumerWidget {
  const WorldEditor({
    required this.path,
    super.key,
  });

  final String path;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    final value = ref.watch(fieldValueProvider(path, ""));

    useFocusedBasedCurrentEditingField(focus, ref.passing, path);

    return WritersIndicator(
      provider: fieldWritersProvider(path),
      shift: (_) => const Offset(15, 0),
      child: FormattedTextField(
        focus: focus,
        text: value,
        icon: TWIcons.earth,
        hintText: "World",
        onChanged: (value) {
          ref
              .read(inspectingEntryDefinitionProvider)
              ?.updateField(ref.passing, path, value);
        },
      ),
    );
  }
}
