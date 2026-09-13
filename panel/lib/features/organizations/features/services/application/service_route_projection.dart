part of "services.dart";

@riverpod
class CanonicalServices extends _$CanonicalServices {
  @override
  Stream<List<Service>> build() async* {
    final organization = ref.watch(organizationIdProvider);
    if (organization == null) {
      yield [];
      return;
    }
    final provider = canonicalOrganizationServicesProvider(organization);
    ref.listen(provider, (_, next) => state = next);
    yield await ref.read(provider.future);
  }

  CanonicalOrganizationServices get _repository {
    final organization = ref.read(organizationIdProvider);
    if (organization == null) throw ApiException.noOrganization();
    return ref.read(
      canonicalOrganizationServicesProvider(organization).notifier,
    );
  }

  Future<void> bindService(String token) async =>
      _repository.bindService(token);
  Future<TypedMutationResult> updateService(Service service) async =>
      _repository.updateService(service);
  Future<void> deleteService(skir.RecordId id) async =>
      _repository.deleteService(id);
}

@riverpod
AsyncValue<List<Service>> projectedServices(Ref ref) {
  final canonical = ref.watch(canonicalServicesProvider);
  if (canonical.mapUnready<List<Service>>() case final value?) return value;

  final organization = ref.watch(organizationIdProvider);
  if (organization == null) return AsyncData(canonical.requireValue);

  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues),
  );
  return AsyncData([
    for (final service in canonical.requireValue)
      service.projected(
        local[EditorResourceKey(
          scope: EditorResourceScope(organizationId: organization),
          identity: service.serviceId,
        )],
      ),
  ]);
}

@riverpod
AsyncValue<Service?> projectedService(Ref ref, skir.RecordId serviceId) {
  final canonical = ref.watch(canonicalServiceProvider(serviceId));
  if (canonical.mapUnready<Service?>() case final value?) return value;

  final organization = ref.watch(organizationIdProvider);
  if (organization == null) return canonical;

  final key = EditorResourceKey(
    scope: EditorResourceScope(organizationId: organization),
    identity: serviceId,
  );
  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues[key]),
  );
  return AsyncData(canonical.requireValue?.projected(local));
}

@riverpod
Stream<OrganizationTopology> organizationTopologyStream(Ref ref) async* {
  final organization = ref.watch(organizationIdProvider);
  if (organization == null) {
    yield OrganizationTopology.empty;
    return;
  }
  yield await ref.watch(
    organizationTopologyControllerProvider(organization).future,
  );
}
