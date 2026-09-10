import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "local_editor_values.freezed.dart";
part "local_editor_values.g.dart";

@freezed
abstract class LocalEditorValue with _$LocalEditorValue {
  const factory LocalEditorValue({
    required DataValue value,
    required Set<DataPath> editedPaths,
  }) = _LocalEditorValue;

  const LocalEditorValue._();

  DataValue projectOnto(DataValue canonical) {
    var projected = canonical;
    for (final path in editedPaths) {
      final local = path.read(value).valueOrNull;
      if (local == null) continue;
      projected = path.replace(projected, local).valueOrNull ?? projected;
    }
    return projected;
  }
}

@riverpod
Map<EditorResourceKey, LocalEditorValue> localEditorValues(Ref ref) {
  final workspace = ref.watch(localWorkProvider);

  void changed() => ref.invalidateSelf();

  workspace.addListener(changed);
  ref.onDispose(() => workspace.removeListener(changed));

  return Map.unmodifiable({
    for (final entry in workspace.resources.entries)
      if (entry.value.source.editedPaths case final editedPaths
          when editedPaths.isNotEmpty)
        if (entry.value.source.value(DataPath.root).valueOrNull
            case final value?)
          entry.key: LocalEditorValue(value: value, editedPaths: editedPaths),
  });
}
