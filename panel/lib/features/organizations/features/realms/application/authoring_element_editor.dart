import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

EditorTarget authoringElementTarget({
  required AuthoringSession session,
  required SelectableIdentifier identity,
  required String pageId,
  required String label,
  required EditorDocument document,
}) {
  final codec = SkirEditorCodec(TypeRegistry(document.typeCatalog));
  wire.PageElement? element(AuthoringSessionState snapshot) => snapshot
      .documents
      .values
      .expand((document) => document.elements)
      .where((element) => element.id.id == identity.id)
      .firstOrNull;
  final initial = element(session.snapshot);
  if (initial == null) throw StateError("The editable element is not loaded");
  EditorDocument? project(AuthoringSessionState snapshot) {
    final current = element(snapshot);
    if (current == null) return null;
    final decoded = codec.decodeValue(current.value);
    return document.copyWith(
      rootType: RecordType(
        fields: {
          "value": TypeField(name: "value", type: document.rootType),
          "placement": TypeField(
            name: "placement",
            type: elementPlacementType(current.placement),
          ),
        },
      ),
      confirmedValue: RecordValue({
        "value": decoded.valueOrNull ?? document.confirmedValue,
        "placement": elementPlacementValue(current.placement),
      }),
      revision: snapshot.sequence ?? document.revision,
      mergePolicies: {elementPlacementPath: EditorMergePolicy.record},
      diagnostics: decoded.diagnostics,
      readOnly: decoded.valueOrNull == null,
    );
  }

  return ResourceEditorTarget(
    targetId: identity,
    label: label,
    document: project(session.snapshot)!,
    updates: session
        .watchSnapshots(() => session.acquirePage(recordId("page:$pageId")))
        .map(project),
    commit: (commit) async {
      final operation = elementCommitOperation(
        identity.id,
        commit,
        document.typeCatalog,
      );
      Future<TypedMutationResult> accept(
        wire.ApplyAuthoringBatchResponse response,
      ) => acceptElementCommit(response, commit, project(session.snapshot));
      try {
        return await accept(await session.apply([operation]));
      } on SubmissionException<wire.ApplyAuthoringBatchResponse> catch (error) {
        return error.toMutation(accept);
      }
    },
  );
}
