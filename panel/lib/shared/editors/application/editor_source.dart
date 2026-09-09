import "package:typewriter_panel/typewriter_panel.dart";

abstract interface class EditorSource implements EditOwner {
  EditorDocument? get document;

  EditorCommitPolicy get commitPolicy;

  bool get hasWork;

  List<TypeDiagnostic> get draftDiagnostics;

  void discardDraft();

  @override
  EditorValue value(DataPath path);

  @override
  EditorMutationResult update(
    DataPath path,
    DataValue value, {
    EditorStructuralMutation? structuralMutation,
  });

  void refreshDocument(EditorDocument document);

  @override
  EditorInteractionSession beginInteraction(DataPath path);

  EditorSaveState saveState(DataPath path);

  Future<TypedMutationResult> flush({Set<DataPath>? paths});

  void acceptRemote({required int revision, required DataValue value});

  void acceptRemoteDeletion();

  void useRemote(DataPath path);

  Future<TypedMutationResult> keepLocal(DataPath path);

  @override
  void dispose();
}
