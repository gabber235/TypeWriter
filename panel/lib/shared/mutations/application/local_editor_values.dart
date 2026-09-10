import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "local_editor_values.freezed.dart";

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
