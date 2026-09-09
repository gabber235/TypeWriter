part of "authoring_session.dart";

/// Durable authoring requests. Live projections subscribe only while they are observed.
final class AuthoringResourceRepository {
  AuthoringResourceRepository(this.session, this.organization, this.realm);
  final ResourceRepositories session;
  final skir.RecordId organization;
  final skir.RecordId realm;
  final _changes = StreamController<wire.AuthoringChanged>.broadcast(
    sync: true,
  );
  final _invalidations = StreamController<void>.broadcast(sync: true);
  Stream<wire.AuthoringChanged> get changes => _changes.stream;
  Stream<void> get invalidations => _invalidations.stream;
  RealmServiceAddress get address =>
      RealmServiceAddress(organizationId: organization, realmId: realm);
  late final combiner =
      MutationCombiner<
        wire.AuthoringOperation,
        wire.ApplyAuthoringBatchResponse
      >(prepare: prepare);

  Future<wire.AuthoringSnapshot> fetch(
    wire.AuthoringSnapshotScope scope,
  ) async {
    session.checkActive();
    final request = wire.GetAuthoringSnapshotRequest(scopes: [scope]);
    final response = await session.transport.request(
      address.request("library.authoring.snapshot.get"),
      wire.GetAuthoringSnapshotRequest.serializer.toBytes(request),
      wire.GetAuthoringSnapshotResponse.serializer,
    );
    session.checkActive();
    return switch (response) {
      wire.GetAuthoringSnapshotResponse_successWrapper(:final value) => value,
      wire.GetAuthoringSnapshotResponse_invalidWrapper(:final value) =>
        throw value.toApiException(),
      _ => throw ApiException.internalServerError(),
    };
  }

  PreparedCommit<wire.ApplyAuthoringBatchResponse> prepare(
    List<wire.AuthoringOperation> operations,
  ) {
    session.checkActive();
    final request = wire.ApplyAuthoringBatchRequest(
      batchId: uuid.v4(),
      operations: operations,
    );
    return session.transport.prepare(
      address.request("library.authoring.batch.apply"),
      wire.ApplyAuthoringBatchRequest.serializer.toBytes(request),
      wire.ApplyAuthoringBatchResponse.serializer,
      submissionId: request.batchId,
      replay: SubmissionReplay.identicalRequest,
      label: _authoringLabel(operations),
      resources: {
        for (final operation in operations)
          for (final id in _operationResources(operation))
            (organization, realm, id),
      },
      classify: (response) => switch (response) {
        wire.ApplyAuthoringBatchResponse_appliedWrapper() =>
          MutationResponseDisposition.confirmed,
        wire.ApplyAuthoringBatchResponse_internalErrorWrapper() ||
        wire.ApplyAuthoringBatchResponse_unknown() =>
          MutationResponseDisposition.uncertain,
        _ => MutationResponseDisposition.rejected,
      },
      onResponse: (response) async {
        session.checkActive();
        switch (response) {
          case wire.ApplyAuthoringBatchResponse_appliedWrapper(:final value):
            _changes.add(value);
          case wire.ApplyAuthoringBatchResponse_conflictWrapper():
            _invalidations.add(null);
          default:
            break;
        }
      },
    );
  }

  void dispose() {
    unawaited(_changes.close());
    unawaited(_invalidations.close());
  }
}
