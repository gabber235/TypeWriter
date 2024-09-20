import "package:flutter/material.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector/editors/algebraic.dart";
import "package:typewriter/widgets/inspector/editors/boolean.dart";
import "package:typewriter/widgets/inspector/editors/closed_range.dart";
import "package:typewriter/widgets/inspector/editors/color.dart";
import "package:typewriter/widgets/inspector/editors/cron.dart";
import "package:typewriter/widgets/inspector/editors/duration.dart";
import "package:typewriter/widgets/inspector/editors/entry_selector.dart";
import "package:typewriter/widgets/inspector/editors/enum.dart";
import "package:typewriter/widgets/inspector/editors/item.dart";
import "package:typewriter/widgets/inspector/editors/list.dart";
import "package:typewriter/widgets/inspector/editors/location.dart";
import "package:typewriter/widgets/inspector/editors/map.dart";
import "package:typewriter/widgets/inspector/editors/material.dart";
import "package:typewriter/widgets/inspector/editors/number.dart";
import "package:typewriter/widgets/inspector/editors/object.dart";
import "package:typewriter/widgets/inspector/editors/optional.dart";
import "package:typewriter/widgets/inspector/editors/page_selector.dart";
import "package:typewriter/widgets/inspector/editors/potion_effect.dart";
import "package:typewriter/widgets/inspector/editors/skin.dart";
import "package:typewriter/widgets/inspector/editors/sound_id.dart";
import "package:typewriter/widgets/inspector/editors/sound_source.dart";
import "package:typewriter/widgets/inspector/editors/string.dart";
import "package:typewriter/widgets/inspector/editors/vector.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "editors.g.dart";

@riverpod
dynamic fieldValue(FieldValueRef ref, String path, [dynamic defaultValue]) {
  final entry = ref.watch(inspectingEntryProvider);
  return entry?.get(path, defaultValue) ?? defaultValue;
}

@riverpod
List<EditorFilter> editorFilters(EditorFiltersRef ref) => [
      // Modifier Editors
      EntrySelectorEditorFilter(),
      PageSelectorEditorFilter(),

      // Custom Editors
      AlgebraicEditorFilter(),
      MaterialEditorFilter(),
      OptionalEditorFilter(),
      PositionEditorFilter(),
      VectorEditorFilter(),
      DurationEditorFilter(),
      CronEditorFilter(),
      ClosedRangeFilter(),
      PotionEffectEditorFilter(),
      ItemEditorFilter(),
      SerializedItemEditorFilter(),
      SoundIdEditorFilter(),
      SoundSourceEditorFilter(),
      SkinEditorFilter(),
      ColorEditorFilter(),

      // Default filters
      StringEditorFilter(),
      NumberEditorFilter(),
      BooleanEditorFilter(),
      EnumEditorFilter(),
      ListEditorFilter(),
      MapEditorFilter(),
      ObjectEditorFilter(),
    ];

abstract class EditorFilter {
  bool canEdit(DataBlueprint dataBlueprint);

  Widget build(String path, DataBlueprint dataBlueprint);
}

@riverpod
String pathDisplayName(PathDisplayNameRef ref, String path) {
  final parts = path.split(".");
  final name = parts.removeLast();

  if (name == "") return "";
  if (int.tryParse(name) != null) {
    final index = int.parse(name) + 1;
    final parent = parts.removeLast();
    if (parent == "") return "#$index";
    return "${parent.formatted} #$index";
  }

  return name.formatted;
}
