import "dart:typed_data";
import "package:flutter/material.dart";

import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";

import "../../../../../support/test_utils.dart";

part "topology_selection_removal_test_cases.dart";
part "host_apply_test_cases.dart";
part "host_target_selection_test_cases.dart";

const _updateSubject = "cloud.to.user.user1.organization.org1.services.update";
const _configureSubject =
    "cloud.to.user.user1.organization.org1.topology.configure";
final _organizationId = recordId("organization:org1");

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  _testTopologySelectionRemoval();
  _testHostApply();
  _testHostTargetSelection();

  test("host presentation owns separate runtime and resource inputs", () async {
    final harness = await _Harness.create();
    addTearDown(harness.dispose);
    final owners = EditorOwnerRegistry();
    addTearDown(owners.dispose);
    final model = harness.selectable.buildPresentation(owners);
    expect(
      model.inputs.values.whereType<PresentationValueInput>(),
      hasLength(1),
    );
    final edits = model.inputs.values
        .whereType<PresentationEditInput>()
        .map((input) => input.owner as EditorSource)
        .toList();
    expect(edits, hasLength(2));
    expect(edits.map((owner) => owner.document!.revision), [1, 1]);
    expect(identical(edits[0], edits[1]), isFalse);
  });

  test("service save carries only identity and its own revision", () async {
    final harness = await _Harness.create();
    addTearDown(harness.dispose);
    skir.UpdateOrganizationServiceRequest? request;
    harness.respond(_updateSubject, (data) {
      request = skir.UpdateOrganizationServiceRequest.serializer.fromBytes(
        data,
      );
      return skir.UpdateOrganizationServiceResponse.serializer.toBytes(
        skir.UpdateOrganizationServiceResponse.wrapSuccess(
          harness.service.copyWith(revision: 2, name: "renamed").toSkir(),
        ),
      );
    });
    final owners = EditorOwnerRegistry();
    addTearDown(owners.dispose);
    final model = harness.selectable.buildPresentation(owners);
    final owner =
        (model.inputs[const BindingId(2)] as PresentationEditInput).owner
            as EditorSource;
    owner.update(DataPath.root.field("name"), const StringValue("renamed"));
    final result = await owner.flush() as MutationSuccess;
    expect(request!.name, "renamed");
    expect(result.revision, 2);
    expect((result.value as RecordValue).fields.keys, ["name"]);
    expect(harness.nats.requests.map((entry) => entry.subject), [
      _updateSubject,
    ]);
  });

  test("configuration saves only the host transaction", () async {
    final harness = await _Harness.create();
    addTearDown(harness.dispose);
    skir.ConfigureServiceHostRequest? request;
    harness.respond(_configureSubject, (data) {
      request = skir.ConfigureServiceHostRequest.serializer.fromBytes(data);
      return skir.ConfigureServiceHostResponse.serializer.toBytes(
        skir.ConfigureServiceHostResponse.createSuccess(
          removedResources: [],
          host: _hostWithRevision(harness.host, 2),
          realm: harness.realm,
          engine: null,
        ),
      );
    });
    final owners = EditorOwnerRegistry();
    addTearDown(owners.dispose);
    final model = harness.selectable.buildPresentation(owners);
    final owner =
        (model.inputs[const BindingId(1)] as PresentationEditInput).owner
            as EditorSource;
    owner.update(
      DataPath.root.field("realm"),
      PolymorphicValue(
        concreteType: const ResolvedTypeRef(
          id: QualifiedTypeId(namespace: "panel.host", name: "RealmHosted"),
          revision: 1,
        ),
        value: RecordValue({"target": StringValue("paper@*")}),
      ),
    );

    final result = await owner.flush() as MutationSuccess;
    expect(request!.execution.realm, isNotNull);
    expect(result.revision, 2);
    expect(
      (result.value as RecordValue).fields.containsKey("service"),
      isFalse,
    );
    expect(harness.nats.requests.map((entry) => entry.subject), [
      _configureSubject,
    ]);
  });
}

class _Harness {
  _Harness._({
    required this.nats,
    required this.container,
    required this.service,
    required this.host,
    required this.realm,
    required this.selectable,
    required this.servicesSubscription,
    required this.topologySubscription,
  });

  static Future<_Harness> create({
    skir.RecordId Function()? organization,
    List<String> supportedEngineIds = const ["paper"],
    List<skir.RealmInstance> realms = const [],
  }) async {
    final nats = FakeNatsClient();
    final service = Service(
      serviceId: recordId("service:paper"),
      revision: 1,
      name: "Paper",
      role: HostServiceRole(version: "1.0.0"),
      createdAt: DateTime.utc(2026, 8, 21),
      state: ServiceState(
        status: ServiceStateStatus.online,
        lastSeen: DateTime.now(),
      ),
    );
    final host = skir.ServiceHost(
      hostId: recordId("service_host:paper"),
      serviceId: service.serviceId,
      revision: 1,
      entrypoint: "PAPER",
      canHostRealm: true,
      supportedEngines: [
        for (final id in supportedEngineIds) skir.SupportedEngine(engineId: id),
      ],
      topologyRevision: skir.ReconciledRevision(desired: 1, applied: 1),
      state: skir.HostRuntimeState(
        status: skir.HostRuntimeStatus.active,
        message: null,
        updatedAt: DateTime.utc(2026, 8, 21),
      ),
    );
    final realm = skir.RealmInstance(
      realmId: recordId("realm_instance:paper"),
      ownerHost: skir.OwnerHost(id: host.hostId, name: service.name),
      revision: 1,
      targetEngine: skir.EngineTarget(
        engineId: "paper",
        versionConstraint: "^1",
      ),
      state: skir.ChildRuntimeState.defaultInstance,
    );
    final topology = OrganizationTopology(
      hosts: [TopologyHost.fromSkir(host)],
      realmInstances: realms.map(TopologyRealm.fromSkir).toList(),
      engineInstances: [],
    );
    final container = ProviderContainer.test(
      overrides: [
        userIdProvider.overrideWith((ref) async => "user1"),
        organizationIdProvider.overrideWith(
          (ref) => organization?.call() ?? _organizationId,
        ),
        natsProvider.overrideWithValue(nats),
        panelTelemetryProvider.overrideWithValue(
          const AsyncData(NoopPanelTelemetry()),
        ),
        organizationServicesProvider(
          _organizationId,
        ).overrideWith(() => _SeededServices([service])),
        scopedOrganizationTopologyProvider(
          _organizationId,
        ).overrideWith(() => _SeededTopology(topology)),
      ],
    );
    final servicesSubscription = container.listen(
      servicesProvider,
      (previous, next) {},
    );
    final topologySubscription = container.listen(
      organizationTopologyStreamProvider,
      (previous, next) {},
    );
    await container.read(servicesProvider.future);
    await container.read(organizationTopologyStreamProvider.future);
    container
        .read(selectionProvider.notifier)
        .select(ServiceHostIdentifier(host.hostId));
    final selectable =
        container.read(selectedProvider).requireValue.single
            as InspectableSelectable;
    return _Harness._(
      nats: nats,
      container: container,
      service: service,
      host: host,
      realm: realm,
      selectable: selectable,
      servicesSubscription: servicesSubscription,
      topologySubscription: topologySubscription,
    );
  }

  final FakeNatsClient nats;
  final ProviderContainer container;
  final Service service;
  final skir.ServiceHost host;
  final skir.RealmInstance realm;
  final InspectableSelectable selectable;
  final ProviderSubscription<AsyncValue<List<Service>>> servicesSubscription;
  final ProviderSubscription<AsyncValue<OrganizationTopology>>
  topologySubscription;

  void respond(String subject, Uint8List Function(Uint8List) handler) {
    nats.registerHandler(subject, handler);
  }

  void dispose() {
    servicesSubscription.close();
    topologySubscription.close();
    container.dispose();
    nats.dispose();
  }
}

class _SeededServices extends OrganizationServices {
  _SeededServices(this.services);

  final List<Service> services;

  @override
  Stream<List<Service>> build(skir.RecordId organizationId) =>
      Stream.value(services);
}

class _SeededTopology extends ScopedOrganizationTopology {
  _SeededTopology(this.topology);

  final OrganizationTopology topology;

  void replace(OrganizationTopology value) => state = AsyncData(value);

  @override
  Stream<OrganizationTopology> build(skir.RecordId organizationId) =>
      Stream.value(topology);
}

skir.ServiceHost _hostWithRevision(skir.ServiceHost host, int revision) =>
    skir.ServiceHost(
      hostId: host.hostId,
      serviceId: host.serviceId,
      revision: revision,
      entrypoint: host.entrypoint,
      canHostRealm: host.canHostRealm,
      supportedEngines: host.supportedEngines,
      topologyRevision: host.topologyRevision,
      state: host.state,
    );
