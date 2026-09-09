import "package:flutter/material.dart";
import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";
import "package:widgetbook_workspace/stories/features/organizations/features/services/presentation/topology_scenarios.dart";

void main() {
  test("complete scenario covers topology roles and runtime states", () {
    final scenario = completeTopologyScenario();
    final topology = scenario.topology;

    expect(topology.hosts.map((host) => host.entrypoint).toSet(), {
      "PAPER",
      "STANDALONE",
    });
    expect(topology.hosts.map((host) => host.state.status).toSet(), {
      TopologyHostStatus.active,
      TopologyHostStatus.reconciling,
      TopologyHostStatus.drifted,
      TopologyHostStatus.failed,
      TopologyHostStatus.offline,
    });
    expect(
      {
        ...topology.realmInstances.map((realm) => realm.state.status),
        ...topology.engineInstances.map((engine) => engine.state.status),
      },
      {
        TopologyRuntimeStatus.absent,
        TopologyRuntimeStatus.staging,
        TopologyRuntimeStatus.active,
        TopologyRuntimeStatus.quiescing,
        TopologyRuntimeStatus.failed,
        TopologyRuntimeStatus.rolledBack,
        TopologyRuntimeStatus.drifted,
      },
    );
    expect(
      scenario.services.any((service) => service.isConnectedAt(DateTime.now())),
      isTrue,
    );
    expect(
      scenario.services.any(
        (service) => !service.isConnectedAt(DateTime.now()),
      ),
      isTrue,
    );
    expect(
      scenario.services.where((service) => service.isCustom),
      hasLength(4),
    );
  });

  testWidgets("services story selects a host through the shared inspector", (
    tester,
  ) async {
    await tester.binding.setSurfaceSize(const Size(1280, 800));
    addTearDown(() => tester.binding.setSurfaceSize(null));
    final scenario = completeTopologyScenario();
    final topology = scenario.topology;
    final services = scenario.services;
    await tester.pumpWidget(
      FakeApp(
        overrides: [
          organizationServicesProvider.overrideWith2(
            (_) => _StoryServices(services),
          ),
          organizationTopologyControllerProvider.overrideWith2(
            (_) => _StoryTopology(topology),
          ),
          organizationIdProvider.overrideWithValue(
            recordId("organization:story"),
          ),
          ...appearanceProviderOverrides(),
        ],
        child: SizedBox(
          width: 1180,
          height: 720,
          child: InspectorScaffold(
            child: ServicesGraph(services: services, topology: topology),
          ),
        ),
      ),
    );
    await tester.pumpAndSettle();

    await tester.tap(find.text("PAPER HOST").first);
    await tester.pumpAndSettle();

    expect(find.text("Identity and connection"), findsNothing);
    expect(find.text("CONNECTION"), findsOneWidget);
    expect(find.text("Capabilities and runtime health"), findsNothing);
    expect(find.text("CAPABILITIES"), findsOneWidget);
    expect(find.text("RUNTIME HEALTH"), findsOneWidget);
    expect(find.text("Configuration"), findsOneWidget);
    expect(find.text("Host a Realm"), findsOneWidget);
    expect(find.text("Run an execution engine"), findsOneWidget);
    expect(find.text("Apply"), findsNothing);
    expect(find.byType(EditorCommitControls), findsOneWidget);
    expect(
      find.ancestor(
        of: find.byType(EditorCommitControls),
        matching: find.byWidgetPredicate(
          (widget) =>
              widget is PresentationNodeRenderer &&
              widget.node.id == "serviceHost.configuration",
        ),
      ),
      findsOneWidget,
    );
    expect(
      tester
          .widget<EditorCommitControls>(find.byType(EditorCommitControls))
          .label,
      isNull,
    );

    expect(find.byTooltip("Zoom to fit"), findsNothing);
    expect(find.text("Enabled"), findsNothing);
    expect(find.text("Disabled"), findsNothing);
    expect(find.text("Assigned Realm"), findsNothing);
    final realmCheckbox = find.byType(Checkbox).first;
    expect(tester.widget<Checkbox>(realmCheckbox).value, isTrue);
    await tester.ensureVisible(realmCheckbox);
    await tester.tap(realmCheckbox);
    await tester.pumpAndSettle();
    expect(find.text("Assigned Realm"), findsOneWidget);
    expect(find.text("Hosted here"), findsNothing);
    expect(find.text("Existing Realm"), findsNothing);
    await tester.ensureVisible(realmCheckbox);
    await tester.tap(realmCheckbox);
    await tester.pumpAndSettle();
    expect(find.text("Assigned Realm"), findsOneWidget);
    final engineCheckbox = find.byType(Checkbox).last;
    final wasEnabled = tester.widget<Checkbox>(engineCheckbox).value!;
    await tester.ensureVisible(engineCheckbox);
    await tester.tap(engineCheckbox);
    await tester.pumpAndSettle();
    expect(tester.widget<Checkbox>(engineCheckbox).value, !wasEnabled);
    expect(
      find.text("Engine target"),
      wasEnabled ? findsNothing : findsOneWidget,
    );
    await tester.ensureVisible(engineCheckbox);
    await tester.tap(engineCheckbox);
    await tester.pumpAndSettle();
    expect(tester.widget<Checkbox>(engineCheckbox).value, wasEnabled);
    expect(
      find.text("Engine target"),
      wasEnabled ? findsOneWidget : findsNothing,
    );
  });
}

class _StoryServices extends OrganizationServices {
  _StoryServices(this.services);

  final List<Service> services;

  @override
  Stream<List<Service>> build(skir.RecordId organizationId) =>
      Stream.value(services);
}

class _StoryTopology extends OrganizationTopologyController {
  _StoryTopology(this.topology);

  final OrganizationTopology topology;

  @override
  Stream<OrganizationTopology> build(skir.RecordId organizationId) =>
      Stream.value(topology);
}
