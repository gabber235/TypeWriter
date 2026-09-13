import "package:typewriter_panel/typewriter_panel.dart";

/// Durable access to one resource in its original scope. No live state is retained.
abstract interface class EditableResource {
  EditorResourceKey get key;
  Set<Object> get reservations;

  /// Returns null only when the resource is confirmed deleted.
  Future<EditorSnapshot?> refresh();

  /// Encodes against the exact snapshot used for reconciliation and validation.
  MutationIntent prepare(
    EditorSnapshot snapshot,
    EditorCommit commit,
    void Function(TypedMutationResult) accept,
  );
}

/// Immutable document and validation context from one resource revision.
abstract class EditorSnapshot {
  const EditorSnapshot();
  EditorDocument get document;
  List<TypeDiagnostic> validateDraft(DataValue value) => const [];
  EditorMutationResult validate(DataPath path, DataValue value) =>
      document.rootType.validateEditorMutation(
        path,
        value,
        registry: TypeRegistry(document.typeCatalog),
      );
}

final class DocumentEditorSnapshot extends EditorSnapshot {
  const DocumentEditorSnapshot(this.document);

  @override
  final EditorDocument document;
}
