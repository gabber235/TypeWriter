part of "services.dart";

extension OrganizationTopologyConfiguration on OrganizationTopology {
  OrganizationTopology applyHostObservation(TopologyHost incoming) {
    final previous = hosts.firstWhereOrNull(
      (host) => host.hostId == incoming.hostId,
    );
    if (previous == null) return copyWith(hosts: [...hosts, incoming]);
    if (incoming.state.updatedAt.isBefore(previous.state.updatedAt)) {
      return this;
    }
    return copyWith(
      hosts: _upsertById(
        hosts,
        previous.copyWith(
          state: incoming.state,
          topologyRevision: previous.topologyRevision.copyWith(
            applied: incoming.topologyRevision.applied,
          ),
        ),
        (host) => host.hostId,
      ),
    );
  }

  OrganizationTopology applyRealmObservation(TopologyRealm incoming) {
    final previous = realmInstances.firstWhereOrNull(
      (realm) => realm.realmId == incoming.realmId,
    );
    if (previous == null ||
        incoming.state.updatedAt.isBefore(previous.state.updatedAt)) {
      return this;
    }
    return copyWith(
      realmInstances: _upsertById(
        realmInstances,
        previous.copyWith(state: incoming.state),
        (realm) => realm.realmId,
      ),
    );
  }

  OrganizationTopology applyEngineObservation(TopologyEngine incoming) {
    final previous = engineInstances.firstWhereOrNull(
      (engine) => engine.engineId == incoming.engineId,
    );
    if (previous == null ||
        incoming.state.updatedAt.isBefore(previous.state.updatedAt)) {
      return this;
    }
    return copyWith(
      engineInstances: _upsertById(
        engineInstances,
        previous.copyWith(state: incoming.state),
        (engine) => engine.engineId,
      ),
    );
  }

  /// Applies the transaction as one projection, preserving newer observations.
  /// Responses and watch events may arrive in either order or repeat.
  OrganizationTopology applyConfiguration(skir.HostConfigurationChange change) {
    var host = TopologyHost.fromSkir(change.host);
    final previousHost = hosts.firstWhereOrNull(
      (item) => item.hostId == host.hostId,
    );
    if (previousHost != null) {
      if (previousHost.revision > host.revision) return this;
      if (previousHost.state.updatedAt.isAfter(host.state.updatedAt)) {
        host = host.copyWith(
          state: previousHost.state,
          topologyRevision: host.topologyRevision.copyWith(
            applied: previousHost.topologyRevision.applied,
          ),
        );
      }
    }
    final removed = change.removedResources.toSet();
    var realms = realmInstances
        .where(
          (item) =>
              !removed.contains(item.realmId) &&
              (item.ownerHost.id != host.hostId ||
                  item.realmId == change.realm?.realmId),
        )
        .toList();
    var engines = engineInstances
        .where(
          (item) =>
              !removed.contains(item.engineId) &&
              (item.ownerHost.id != host.hostId ||
                  item.engineId == change.engine?.engineId),
        )
        .toList();

    if (change.realm case final value?) {
      var realm = TopologyRealm.fromSkir(value);
      final previous = realms.firstWhereOrNull(
        (item) => item.realmId == realm.realmId,
      );
      if (previous != null &&
          previous.state.updatedAt.isAfter(realm.state.updatedAt)) {
        realm = realm.copyWith(state: previous.state);
      }
      realms = _upsertById(realms, realm, (item) => item.realmId);
    }
    if (change.engine case final value?) {
      var engine = TopologyEngine.fromSkir(value);
      final previous = engines.firstWhereOrNull(
        (item) => item.engineId == engine.engineId,
      );
      if (previous != null &&
          previous.state.updatedAt.isAfter(engine.state.updatedAt)) {
        engine = engine.copyWith(state: previous.state);
      }
      engines = _upsertById(engines, engine, (item) => item.engineId);
    }
    return copyWith(
      hosts: _upsertById(hosts, host, (item) => item.hostId),
      realmInstances: realms,
      engineInstances: engines,
    );
  }
}
