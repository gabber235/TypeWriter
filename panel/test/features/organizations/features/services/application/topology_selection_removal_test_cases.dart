part of "service_host_selectable_test.dart";

void _testTopologySelectionRemoval() {
  for (final kind in ["host", "realm", "engine"]) {
    testWidgets("removed selected $kind recovers the inspector", (
      tester,
    ) async {
      final harness = await tester.runAsync(_Harness.create);
      final container = harness!.container;
      addTearDown(harness.dispose);
      final engineId = recordId("engine_instance:paper");
      final topology =
          (container.read(
                  scopedOrganizationTopologyProvider(_organizationId).notifier,
                )
                as _SeededTopology)
            ..replace(
              OrganizationTopology(
                hosts: [TopologyHost.fromSkir(harness.host)],
                realmInstances: [TopologyRealm.fromSkir(harness.realm)],
                engineInstances: [
                  TopologyEngine.fromSkir(
                    skir.EngineInstance(
                      engineId: engineId,
                      ownerHost: harness.realm.ownerHost,
                      realm: skir.RealmInfo(
                        realmId: harness.realm.realmId,
                        ownerHost: harness.realm.ownerHost,
                      ),
                      revision: 1,
                      target: harness.realm.targetEngine,
                      state: skir.ChildRuntimeState.defaultInstance,
                    ),
                  ),
                ],
              ),
            );
      final identifier = switch (kind) {
        "host" => ServiceHostIdentifier(harness.host.hostId),
        "realm" => RealmInstanceIdentifier(harness.realm.realmId),
        _ => EngineInstanceIdentifier(engineId),
      };
      container.read(selectionProvider.notifier).selectAll([identifier]);
      final session = container.listen(inspectionSessionProvider, (_, _) {});
      addTearDown(session.close);
      expect(
        container.read(inspectedSelectionProvider).requireValue,
        hasLength(1),
      );

      topology.replace(OrganizationTopology.empty);
      final missing = container.read(inspectedSelectionProvider);
      expect(
        missing.asError?.error,
        isA<SelectableNotFoundException>().having(
          (error) => error.id,
          "identifier",
          identifier,
        ),
      );
      expect(
        missing.asError?.stackTrace.toString(),
        contains("topology_selection.dart"),
      );
      expect(container.read(selectionProvider), [identifier]);

      await tester.pump();
      expect(container.read(selectionProvider), isEmpty);
      expect(container.read(inspectedSelectionProvider).requireValue, isEmpty);
      expect(tester.takeException(), isNull);
    });
  }
}
