part of "services.dart";

@riverpod
class Services extends _$Services {
  @override
  Stream<List<Service>> build() async* {
    final organization = ref.watch(organizationIdProvider);
    if (organization == null) {
      yield [];
      return;
    }
    final provider = organizationServicesProvider(organization);
    ref.listen(provider, (_, next) => state = next);
    yield await ref.read(provider.future);
  }

  OrganizationServices get _repository {
    final organization = ref.read(organizationIdProvider);
    if (organization == null) throw ApiException.noOrganization();
    return ref.read(organizationServicesProvider(organization).notifier);
  }

  Future<void> bindService(String token) async =>
      _repository.bindService(token);
  Future<TypedMutationResult> updateService(Service service) async =>
      _repository.updateService(service);
  Future<void> deleteService(skir.RecordId id) async =>
      _repository.deleteService(id);
}

@riverpod
class OrganizationTopologyStream extends _$OrganizationTopologyStream {
  @override
  Stream<OrganizationTopology> build() async* {
    final organization = ref.watch(organizationIdProvider);
    if (organization == null) {
      yield OrganizationTopology.empty;
      return;
    }
    final provider = scopedOrganizationTopologyProvider(organization);
    ref.listen(provider, (_, next) => state = next);
    yield await ref.read(provider.future);
  }

  Future<TopologyConfigurationResult> configureHost({
    required TopologyHost host,
    required skir.HostExecutionConfiguration execution,
  }) {
    final organization = ref.read(organizationIdProvider);
    if (organization == null) throw ApiException.noOrganization();
    return ref
        .read(scopedOrganizationTopologyProvider(organization).notifier)
        .configureHost(host: host, execution: execution);
  }
}
