part of "services.dart";

/// Owns organization service and host configuration transport for the resource
/// session.
///
/// It is the mutation boundary, not the application state store. The backend
/// remains authoritative. Snapshot requests provide refresh data, while
/// [acceptService] and [acceptConfiguration] publish committed results to the
/// providers that own the corresponding projections. Service identity and host
/// runtime topology remain separate resources even when a host refers to the
/// same service.
///
/// The repository lives as long as its [ResourceRepositories] session. Closing
/// that session closes its streams and makes further operations invalid.
final class ServiceResourceRepository {
  ServiceResourceRepository(this.session, this.organization);
  final ResourceRepositories session;
  final skir.RecordId organization;
  final _configurations =
      StreamController<skir.HostConfigurationChange>.broadcast(sync: true);
  final _identities = StreamController<Service>.broadcast(sync: true);

  /// Committed host configuration changes for the organization.
  ///
  /// Consumers apply these changes to the topology projection. The change
  /// carries the resulting host configuration and any affected runtime
  /// resources, so a mutation result can update the projection without
  /// waiting for another snapshot.
  Stream<skir.HostConfigurationChange> get configurations =>
      _configurations.stream;

  /// Committed service identity changes for the organization.
  ///
  /// The canonical service provider consumes this stream and reconciles each
  /// value by service identity and revision.
  Stream<Service> get identities => _identities.stream;

  /// Builds the authenticated subject for an organization operation.
  Future<String> subject(String operation) async {
    session.checkActive();
    final user = await session.userId;
    session.checkActive();
    if (user == null) throw ApiException.notAuthenticated();
    return "cloud.to.user.$user.organization.${organization.id}.$operation";
  }

  /// Fetches the current topology snapshot for refresh or initial state.
  ///
  /// Topology watches are owned by the provider layer. This request is scoped
  /// to the repository session and does not create a watch lifetime.
  Future<OrganizationTopology> topology() async {
    final response = await session.transport.request(
      subject("topology.watch"),
      skir.WatchOrganizationTopologyRequest.serializer.toBytes(
        skir.WatchOrganizationTopologyRequest(),
      ),
      skir.WatchOrganizationTopologyResponse.serializer,
    );
    session.checkActive();
    if (response is! skir.WatchOrganizationTopologyResponse_listWrapper) {
      throw StateError("The topology request did not return a snapshot");
    }
    return _reduceTopology(null, response);
  }

  /// Fetches the current service identity snapshot for refresh or initial
  /// state.
  Future<List<Service>> services() async {
    final response = await session.transport.request(
      subject("services.watch"),
      skir.WatchOrganizationServicesRequest.serializer.toBytes(
        skir.WatchOrganizationServicesRequest(),
      ),
      skir.WatchOrganizationServicesResponse.serializer,
    );
    session.checkActive();
    return switch (response) {
      skir.WatchOrganizationServicesResponse_listWrapper(:final value) =>
        value.map(Service.fromSkir).toList(),
      _ => throw StateError("The services request did not return a snapshot"),
    };
  }

  /// Publishes a backend configuration result to topology consumers.
  void acceptConfiguration(skir.HostConfigurationChange change) {
    session.checkActive();
    _configurations.add(change);
  }

  /// Publishes a backend service identity result to canonical service
  /// consumers.
  void acceptService(Service service) {
    session.checkActive();
    _identities.add(service);
  }

  /// Prepares a host configuration mutation against [revision].
  ///
  /// The expected revision is an optimistic concurrency check. The prepared
  /// commit reserves the organization and host for mutation tracking. Callers
  /// reconcile accepted configuration through the topology provider flow.
  PreparedCommit<skir.ConfigureServiceHostResponse> configure(
    skir.RecordId host,
    int revision,
    skir.HostExecutionConfiguration execution,
  ) {
    session.checkActive();
    final request = skir.ConfigureServiceHostRequest(
      operationId: uuid.v4(),
      hostId: host,
      expectedRevision: revision,
      execution: execution,
    );
    return session.transport.prepare(
      subject("topology.configure"),
      skir.ConfigureServiceHostRequest.serializer.toBytes(request),
      skir.ConfigureServiceHostResponse.serializer,
      submissionId: request.operationId,
      replay: SubmissionReplay.identicalRequest,
      resources: {(organization, host)},
      label: "Apply Host configuration: ${host.id}",
      classify: (response) => switch (response) {
        skir.ConfigureServiceHostResponse_successWrapper() =>
          MutationResponseDisposition.confirmed,
        skir.ConfigureServiceHostResponse_unknown() ||
        skir.ConfigureServiceHostResponse_internalErrorWrapper() =>
          MutationResponseDisposition.uncertain,
        _ => MutationResponseDisposition.rejected,
      },
    );
  }

  /// Prepares a service identity rename against [revision].
  ///
  /// The service revision protects identity edits from overwriting a newer
  /// canonical value. Runtime topology is not changed by this operation.
  PreparedCommit<skir.UpdateOrganizationServiceResponse> rename(
    skir.RecordId service,
    int revision,
    String name,
  ) {
    session.checkActive();
    final request = skir.UpdateOrganizationServiceRequest(
      operationId: uuid.v4(),
      serviceId: service,
      expectedRevision: revision,
      name: name,
    );
    return session.transport.prepare(
      subject("services.update"),
      skir.UpdateOrganizationServiceRequest.serializer.toBytes(request),
      skir.UpdateOrganizationServiceResponse.serializer,
      submissionId: request.operationId,
      replay: SubmissionReplay.identicalRequest,
      resources: {(organization, service)},
      label: "Update service: ${service.id}",
      classify: (response) => switch (response) {
        skir.UpdateOrganizationServiceResponse_successWrapper() =>
          MutationResponseDisposition.confirmed,
        skir.UpdateOrganizationServiceResponse_unknown() ||
        skir.UpdateOrganizationServiceResponse_internalErrorWrapper() =>
          MutationResponseDisposition.uncertain,
        _ => MutationResponseDisposition.rejected,
      },
    );
  }

  /// Closes the repository's result streams.
  void dispose() {
    unawaited(_configurations.close());
    unawaited(_identities.close());
  }
}
