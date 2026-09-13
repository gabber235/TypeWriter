part of "authoring_session.dart";

/// Owns durable authoring requests for editor resources in one realm.
///
/// The repository is cached by [ResourceRepositories] for the organization and
/// realm. It does not own canonical snapshots or live subscriptions. The
/// [AuthoringSession] owns those projections and consumes [changes] and
/// [invalidations] emitted after mutation integration. [SkirMutationClient]
/// remains the transport owner; this repository supplies authoring subjects,
/// serialization, resource reservations, and response integration.
final class AuthoringResourceRepository {
  AuthoringResourceRepository(this.session, this.organization, this.realm);

  /// Organization repositories that own this repository's transport lifetime.
  final ResourceRepositories session;

  /// Organization containing the realm resources.
  final skir.RecordId organization;

  /// Realm containing the authoring resources.
  final skir.RecordId realm;
  final _changes = StreamController<wire.AuthoringChanged>.broadcast(
    sync: true,
  );
  final _invalidations = StreamController<void>.broadcast(sync: true);

  /// Emits applied authoring events for the owning session's canonical model.
  Stream<wire.AuthoringChanged> get changes => _changes.stream;

  /// Emits when a conflict requires the owning session to refresh.
  Stream<void> get invalidations => _invalidations.stream;

  /// Builds service subjects for this organization's realm.
  RealmServiceAddress get address =>
      RealmServiceAddress(organizationId: organization, realmId: realm);

  /// Combines editor contributions into one authoring batch per preparation.
  late final combiner =
      MutationCombiner<
        wire.AuthoringOperation,
        wire.ApplyAuthoringBatchResponse
      >(prepare: prepare);

  /// Fetches one authoritative snapshot scope for an editor resource.
  ///
  /// The repository must still be active when the request starts and when the
  /// response arrives. A successful response is returned unchanged. Invalid,
  /// internal, and unknown responses become the repository's API exceptions.
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

  /// Prepares an editor batch for the shared local mutation owner.
  ///
  /// Operations must include their expected canonical values. The returned
  /// commit captures immutable request bytes, reserves every affected resource,
  /// supports identical request replay, and emits [changes] after an applied
  /// response. A conflict emits [invalidations]. Other outcomes remain owned
  /// by the shared mutation layer and do not emit canonical changes here.
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

  /// Closes event streams and ends this repository's lifecycle.
  void dispose() {
    unawaited(_changes.close());
    unawaited(_invalidations.close());
  }
}
