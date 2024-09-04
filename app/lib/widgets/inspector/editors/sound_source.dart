import "package:flutter/cupertino.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/entry_selector.dart";
import "package:typewriter/widgets/inspector/editors/location.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class SoundSourceEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "soundSource";
  @override
  Widget build(String path, DataBlueprint dataBlueprint) => SoundSourceEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class SoundSourceEditor extends HookConsumerWidget {
  const SoundSourceEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });
  final String path;
  final CustomBlueprint customBlueprint;

  DataBlueprint? get locationShape {
    final shape = customBlueprint.shape;
    if (shape is! ObjectBlueprint) return null;
    if (!shape.fields.containsKey("location")) return null;
    return shape.fields["location"];
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final type = ref.watch(fieldValueProvider("$path.type", "self"));

    final sourceType = SoundSourceType.fromString(type);

    return SizedBox(
      width: double.infinity,
      child: Column(
        children: [
          CupertinoSlidingSegmentedControl<SoundSourceType>(
            children: {
              for (final type in SoundSourceType.values)
                type: _SegmentSelector(text: type.name),
            },
            groupValue: sourceType,
            backgroundColor: Theme.of(context).inputDecorationTheme.fillColor ??
                Theme.of(context).canvasColor,
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            onValueChanged: (value) => ref
                .read(inspectingEntryDefinitionProvider)
                ?.updateField(ref.passing, "$path.type", value?.name ?? "self"),
          ),
          const SizedBox(height: 8),
          if (sourceType == SoundSourceType.emitter)
            EntrySelectorEditor(
              path: "$path.entryId",
              dataBlueprint: const PrimitiveBlueprint(
                type: PrimitiveType.string,
                modifiers: [Modifier(name: "entry", data: "sound_source")],
              ),
            ),
          if (sourceType == SoundSourceType.location)
            PositionEditor(
              path: "$path.location",
              customBlueprint: CustomBlueprint(
                editor: "location",
                internalDefaultValue:
                    customBlueprint.defaultValue()["location"] ?? {},
                shape: locationShape!,
              ),
            ),
        ],
      ),
    );
  }
}

class _SegmentSelector extends StatelessWidget {
  const _SegmentSelector({
    required this.text,
  });

  final String text;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.maxFinite,
      child: Center(child: Text(text)),
    );
  }
}

enum SoundSourceType {
  self,
  emitter,
  location,
  ;

  String get name => toString().split(".").last;

  static SoundSourceType fromString(String? value) {
    return SoundSourceType.values.firstWhere(
      (type) => type.name == value,
      orElse: () => SoundSourceType.self,
    );
  }
}
