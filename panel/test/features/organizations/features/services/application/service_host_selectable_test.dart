import "dart:async";
import "dart:typed_data";

import "package:flutter/material.dart";

import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";

import "../../../../../support/provider_test_utils.dart";
import "../../../../../support/test_utils.dart";

part "topology_selection_removal_test_cases.dart";
part "host_apply_test_cases.dart";
part "host_target_selection_test_cases.dart";
part "resource_connection_test_cases.dart";
part "service_host_selectable_test_support.dart";

const _updateSubject = "cloud.to.user.user1.organization.org1.services.update";
const _configureSubject =
    "cloud.to.user.user1.organization.org1.topology.configure";
final _organizationId = recordId("organization:org1");

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  _testTopologySelectionRemoval();
  _testHostApply();
  _testHostTargetSelection();
  _testResourceConnections();

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
        ((model.inputs[const BindingId(2)]! as PresentationEditInput).owner
              as EditorSource)
          ..update(DataPath.root.field("name"), const StringValue("renamed"));
    final result = await owner.flush() as MutationSuccess;
    expect(request!.name, "renamed");
    expect(result.revision, 2);
    expect((result.value as RecordValue).fields.keys, ["name"]);

    expect(harness.nats.requests.map((entry) => entry.subject), [
      "cloud.to.user.user1.organization.org1.services.watch",
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
        ((model.inputs[const BindingId(1)]! as PresentationEditInput).owner
              as EditorSource)
          ..update(
            DataPath.root.field("realm"),
            PolymorphicValue(
              concreteType: const ResolvedTypeRef(
                id: QualifiedTypeId(
                  namespace: "panel.host",
                  name: "RealmHosted",
                ),
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
      "cloud.to.user.user1.organization.org1.topology.watch",
      _configureSubject,
    ]);
  });
}
