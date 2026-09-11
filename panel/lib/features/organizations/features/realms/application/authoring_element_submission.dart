import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

wire.AuthoringOperation elementCommitOperation(
  String id,
  EditorCommit commit,
  TypeCatalog catalog,
) {
  final codec = SkirEditorCodec(TypeRegistry(catalog));
  final mutations = <wire.ExpectedElementValueMutation>[];
  for (final path in commit.changedPaths.where(
    (path) => path.isAtOrBelow(elementValuePath),
  )) {
    final before = path.read(commit.baseValue).valueOrNull!;
    final after = path.read(commit.rootValue).valueOrNull!;
    mutations.add(
      wire.ExpectedElementValueMutation(
        expected: codec.encodeValue(before).valueOrNull!,
        mutation: wire.ElementValueMutation.createSetValue(
          path: codec
              .encodePath(DataPath(path.segments.skip(1).toList()))
              .valueOrNull!,
          value: codec.encodeValue(after).valueOrNull!,
        ),
      ),
    );
  }
  return wire.AuthoringOperation.createPatchElement(
    id: recordId("element:$id"),
    page: null,
    name: null,
    placement:
        commit.changedPaths.any(
          (path) => path.isAtOrBelow(elementPlacementPath),
        )
        ? wire.ElementPlacementChange(
            expected: encodeElementPlacement(
              elementPlacementPath.read(commit.baseValue).valueOrNull!,
            ),
            value: encodeElementPlacement(
              elementPlacementPath.read(commit.rootValue).valueOrNull!,
            ),
          )
        : null,
    valueMutations: mutations,
  );
}

Future<TypedMutationResult> acceptElementCommit(
  wire.ApplyAuthoringBatchResponse response,
  EditorCommit commit,
  EditorDocument? actual,
) async => switch (response) {
  wire.ApplyAuthoringBatchResponse_appliedWrapper() =>
    actual == null
        ? unavailableMutation(
            "The element no longer exists",
            targetDeleted: true,
          )
        : TypedMutationResult.success(
            revision: actual.revision,
            value: actual.confirmedValue,
          ),
  wire.ApplyAuthoringBatchResponse_conflictWrapper() =>
    actual == null
        ? unavailableMutation(
            "The element no longer exists",
            targetDeleted: true,
          )
        : TypedMutationResult.conflict(
            expectedRevision: commit.expectedRevision,
            actualRevision: actual.revision,
            actualValue: actual.confirmedValue,
          ),
  _ => response.toMutationFailure(
    unavailableMessage: "The element could not be saved",
  ),
};
