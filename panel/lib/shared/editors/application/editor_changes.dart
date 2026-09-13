import "package:typewriter_panel/typewriter_panel.dart";

/// Computes the smallest leaf changes needed to transform one editor value.
///
/// Record fields are compared recursively when both values retain the same
/// shape. A changed value, removed field, or type change is represented at the
/// current path so the result can be applied as editor mutations.
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

/// Applies a prepared value change set through the editor's normal contract.
///
/// Validation runs for every path before any update is made. If validation
/// succeeds, all local changes are applied and persistence is flushed through
/// the source's configured commit policy. This makes callers receive one
/// result without bypassing draft ownership or save lifecycle handling.
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
