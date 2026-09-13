import "package:typewriter_panel/typewriter_panel.dart";

/// Immutable presentation input for a resource and its current snapshot.
abstract interface class EditorTarget {
  Object get targetId;
  String get label;
  EditableResource get resource;
  EditorSnapshot get snapshot;
  EditorDocument get document;
  EditorCommitPolicy get commitPolicy;
  List<TypeDiagnostic> validateDraft(DataValue value);
  EditorValue value(DataPath path);
  EditorMutationResult validate(DataPath path, DataValue value);
}
