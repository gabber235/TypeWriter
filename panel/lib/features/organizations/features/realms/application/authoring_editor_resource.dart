part of "authoring_session.dart";

/// Shares authoring transaction semantics without knowing any resource presentation.
abstract class AuthoringEditorResource implements EditableResource {
  const AuthoringEditorResource(this.repository, this.id);
  final AuthoringResourceRepository repository;
  final skir.RecordId id;
  wire.AuthoringSnapshotScope get scope;
  @override
  EditorResourceKey get key => EditorResourceKey(
    scope: (repository.organization, repository.realm),
    identity: id,
  );
  @override
  Set<Object> get reservations => {
    (repository.organization, repository.realm, id),
  };
  FutureOr<EditorSnapshot?> project(wire.AuthoringSnapshot snapshot);
  wire.AuthoringOperation operation(
    EditorSnapshot snapshot,
    EditorCommit commit,
  );

  @override
  Future<EditorSnapshot?> refresh() async =>
      project(await repository.fetch(scope));

  @override
  MutationIntent prepare(
    EditorSnapshot snapshot,
    EditorCommit commit,
    void Function(TypedMutationResult) accept,
  ) =>
      CombinedMutation<
        wire.AuthoringOperation,
        wire.ApplyAuthoringBatchResponse
      >(
        combiner: repository.combiner,
        resources: reservations,
        prepare: () =>
            MutationContribution<
              wire.AuthoringOperation,
              wire.ApplyAuthoringBatchResponse
            >(
              operation: operation(snapshot, commit),
              integrate: (result) async {
                switch (result) {
                  case SubmissionConfirmed(:final value) ||
                      SubmissionRejected(
                        response: final wire.ApplyAuthoringBatchResponse value,
                      ):
                    final actual =
                        value
                            is wire.ApplyAuthoringBatchResponse_conflictWrapper
                        ? (await refresh())?.document
                        : snapshot.document;
                    accept(await acceptElementCommit(value, commit, actual));
                  default:
                    break;
                }
              },
            ),
      );
}
