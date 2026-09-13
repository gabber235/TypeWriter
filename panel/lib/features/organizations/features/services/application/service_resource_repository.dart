part of "services.dart";

/// Scoped requests and committed results, independent of topology watch lifetimes.
final class ServiceResourceRepository {
  ServiceResourceRepository(this.session, this.organization);
  final ResourceRepositories session;
  final skir.RecordId organization;
  final _configurations =
      StreamController<skir.HostConfigurationChange>.broadcast(sync: true);
  final _identities = StreamController<Service>.broadcast(sync: true);
  Stream<skir.HostConfigurationChange> get configurations =>
      _configurations.stream;
  Stream<Service> get identities => _identities.stream;

  Future<String> subject(String operation) async {
    session.checkActive();
    final user = await session.userId;
    session.checkActive();
    if (user == null) throw ApiException.notAuthenticated();
    return "cloud.to.user.$user.organization.${organization.id}.$operation";
  }

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

  void acceptConfiguration(skir.HostConfigurationChange change) {
    session.checkActive();
    _configurations.add(change);
  }

  void acceptService(Service service) {
    session.checkActive();
    _identities.add(service);
  }

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

  void dispose() {
    unawaited(_configurations.close());
    unawaited(_identities.close());
  }
}
