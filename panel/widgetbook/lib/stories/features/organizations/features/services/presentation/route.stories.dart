import "package:flutter/material.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";
import "package:widgetbook_annotation/widgetbook_annotation.dart" as widgetbook;
import "package:widgetbook_workspace/stories/features/organizations/features/services/presentation/topology_scenarios.dart";

@widgetbook.UseCase(name: "Default", type: ServicesPage)
Widget servicesPageUseCase(BuildContext context) {
  return servicesPageStory();
}

Widget servicesPageStory() {
  final scenario = completeTopologyScenario();
  return FakeApp(
    overrides: [
      organizationTopologyControllerProvider.overrideWith2(
        (_) => _StoryTopology(scenario.topology),
      ),
      organizationServicesProvider.overrideWith2(
        (_) => _StoryServices(scenario.services),
      ),
      ...organizationProviderOverrides(),
      ...organizationsProviderOverrides(state: DisplayState.fewItems),
      ...authProviderOverrides(),
      ...appearanceProviderOverrides(),
    ],
    child: OrganizationScaffold(child: ServicesPage()),
  );
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
