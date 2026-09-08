import "package:typewriter_panel/typewriter_panel.dart";

Map<DataPath, DataValue> editorValueChanges(
  DataValue before,
  DataValue after, [
  DataPath path = DataPath.root,
]) {
  if (before == after) return const {};
  if (before is RecordValue &&
      after is RecordValue &&
      before.fields.keys.toSet().containsAll(after.fields.keys) &&
      before.fields.length == after.fields.length) {
    return {
      for (final entry in after.fields.entries)
        ...editorValueChanges(
          before.fields[entry.key]!,
          entry.value,
          path.field(entry.key),
        ),
    };
  }
  return {path: after};
}

extension EditorChanges on EditorSource {
  Future<TypedMutationResult> applyChanges(
    Map<DataPath, DataValue> changes,
  ) async {
    for (final entry in changes.entries) {
      final validation = validate(entry.key, entry.value);
      if (validation case InvalidEditorMutation(:final diagnostics)) {
        return TypedMutationResult.invalid(diagnostics);
      }
    }
    for (final entry in changes.entries) {
      update(entry.key, entry.value);
    }
    return flush(paths: changes.keys.toSet());
  }
}
