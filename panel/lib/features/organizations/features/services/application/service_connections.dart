part of "services.dart";

/// Shares one deadline projection for a service list across its consumers.
@riverpod
Map<skir.RecordId, bool> serviceConnections(Ref ref) {
  final services = ref.watch(servicesProvider).value ?? const <Service>[];

  final now = clock.now();
  final deadline = services
      .map((service) => service.connectionDeadline)
      .whereType<DateTime>()
      .where((deadline) => deadline.isAfter(now))
      .minOrNull;

  if (deadline != null) {
    final timer = Timer(deadline.difference(now), ref.invalidateSelf);
    ref.onDispose(timer.cancel);
  }

  final lifecycle = AppLifecycleListener(onResume: ref.invalidateSelf);
  ref.onDispose(lifecycle.dispose);

  return {
    for (final service in services)
      service.serviceId: service.isConnectedAt(now),
  };
}

@riverpod
bool hostConnected(Ref ref, skir.RecordId hostId) {
  final topology = ref.watch(organizationTopologyStreamProvider).value;
  final connections = ref.watch(serviceConnectionsProvider);
  final host = topology?.hosts.firstWhereOrNull(
    (host) => host.hostId == hostId,
  );
  return connections[host?.serviceId] ?? false;
}
