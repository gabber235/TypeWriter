part of "authoring_session.dart";

/// Shares authoring transaction semantics without knowing any resource presentation.
abstract class AuthoringEditorResource implements EditableResource {
  const AuthoringEditorResource(this.repository, this.id);
  final AuthoringResourceRepository repository;
  final skir.RecordId id;
  wire.AuthoringSnapshotScope get scope;
  @override
  EditorResourceKey get key => EditorResourceKey(
    scope: EditorResourceScope(
      organizationId: repository.organization,
      realmId: repository.realm,
    ),
    identity: id,
  );
  @override
  Set<Object> get reservations => {
    (repository.organization, repository.realm, id),
  };
  FutureOr<EditorSnapshot?> project(wire.AuthoringSnapshot snapshot);

  /// Projects the complete resource returned by an applied authoring batch.
  ///
  /// The response contains resource changes rather than snapshot slices, so
  /// each resource type maps its matching change into its editor snapshot.
  /// The submitted snapshot supplies immutable decoding metadata when needed.
  /// Returning null means the response did not contain this resource and the
  /// caller must perform one authoritative refresh.
  FutureOr<EditorSnapshot?> projectApplied(
    wire.AuthoringChanged change,
    EditorSnapshot submitted,
  );
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
                    final actual = switch (value) {
                      wire.ApplyAuthoringBatchResponse_appliedWrapper(
                        :final value,
                      ) =>
                        await projectApplied(value, snapshot) ??
                            await refresh(),
                      wire.ApplyAuthoringBatchResponse_conflictWrapper() =>
                        await refresh(),
                      _ => null,
                    };
                    accept(
                      await acceptElementCommit(
                        value,
                        commit,
                        actual?.document,
                      ),
                    );
                  default:
                    break;
                }
              },
            ),
      );
}
