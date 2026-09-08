import "package:typewriter_panel/typewriter_panel.dart";

abstract interface class EditorTarget {
  Object get targetId;

  String get label;

  EditorDocument get document;

  Stream<EditorDocument?> get updates;

  EditorCommitPolicy get commitPolicy;

  List<TypeDiagnostic> validateDraft(DataValue value);

  EditorValue value(DataPath path);

  EditorMutationResult validate(DataPath path, DataValue value);

  Future<TypedMutationResult> commit(EditorCommit commit);
}
