part of "service_host_selectable_test.dart";

PolymorphicValue _mode(
  String name, [
  Map<String, DataValue> fields = const {},
]) => PolymorphicValue(
  concreteType: ResolvedTypeRef(
    id: QualifiedTypeId(namespace: "panel.host", name: name),
    revision: 1,
  ),
  value: RecordValue(fields),
);

void _testHostApply() {
  test(
    "Host Realm and engine assignments remain a draft until complete Apply",
    () async {
      final harness = await _Harness.create();
      addTearDown(harness.dispose);
      final owners = EditorOwnerRegistry();
      addTearDown(owners.dispose);
      final model = harness.selectable.buildPresentation(owners);
      final owner =
          (model.inputs[const BindingId(1)] as PresentationEditInput).owner
              as EditorSource;
      final path = DataPath.root.field("realm");
      final interaction = owner.beginInteraction(path);
      owner.update(path, _mode("RealmHosted", {"target": StringValue("")}));
      await interaction.commit();
      expect(harness.nats.requests, isEmpty);
      expect(await owner.flush(), isA<MutationInvalid>());
      expect(harness.nats.requests, isEmpty);

      owner.update(
        path,
        _mode("RealmHosted", {"target": StringValue("paper@*")}),
      );
      expect(
        owner.update(
          DataPath.root.field("engine"),
          _mode("EngineEnabled", {
            "target": StringValue("paper@*"),
            "realm": StringValue("realm_instance:elsewhere"),
          }),
        ),
        isA<AppliedEditorMutation>(),
      );
      expect(owner.draftDiagnostics, isEmpty);
      expect(harness.nats.requests, isEmpty);
      skir.ConfigureServiceHostRequest? submitted;
      harness.respond(_configureSubject, (bytes) {
        submitted = skir.ConfigureServiceHostRequest.serializer.fromBytes(
          bytes,
        );
        return skir.ConfigureServiceHostResponse.serializer.toBytes(
          skir.ConfigureServiceHostResponse.createSuccess(
            host: _hostWithRevision(harness.host, 2),
            realm: harness.realm,
            engine: skir.EngineInstance(
              engineId: recordId("engine_instance:paper"),
              ownerHost: harness.realm.ownerHost,
              realm: skir.RealmInfo(
                realmId: harness.realm.realmId,
                ownerHost: harness.realm.ownerHost,
              ),
              revision: 1,
              target: skir.EngineTarget(
                engineId: "paper",
                versionConstraint: "*",
              ),
              state: skir.ChildRuntimeState.defaultInstance,
            ),
            removedResources: [],
          ),
        );
      });
      expect(await owner.flush(), isA<MutationSuccess>());
      expect(harness.nats.requests, hasLength(1));
      expect(submitted!.execution.realm, isNotNull);
      expect(
        submitted!.execution.primaryEngine!.realm,
        skir.EngineRealmSelection.hostedRealm,
      );
      expect(owner.hasWork, isFalse);
    },
  );

  test(
    "retained Host draft saves to its original organization after navigation",
    () async {
      var organization = _organizationId;
      final harness = await _Harness.create(organization: () => organization);
      addTearDown(harness.dispose);
      final workspace = EditorWorkspace();
      addTearDown(workspace.dispose);
      final view = EditorOwnerRegistry(
        workspace: workspace,
        scope: (_organizationId, null),
      );
      final owner =
          (harness.selectable.buildPresentation(view).inputs[const BindingId(
                        1,
                      )]!
                      as PresentationEditInput)
                  .owner
              as EditorSource;
      owner.update(
        DataPath.root.field("realm"),
        _mode("RealmHosted", {"target": StringValue("paper@*")}),
      );
      view.dispose();
      harness.servicesSubscription.close();
      harness.topologySubscription.close();
      organization = recordId("organization:org2");
      harness.container.invalidate(organizationIdProvider);
      await harness.container.pump();
      harness.respond(
        _configureSubject,
        (_) => skir.ConfigureServiceHostResponse.serializer.toBytes(
          skir.ConfigureServiceHostResponse.createSuccess(
            host: _hostWithRevision(harness.host, 2),
            realm: harness.realm,
            engine: null,
            removedResources: [],
          ),
        ),
      );
      expect(await owner.flush(), isA<MutationSuccess>());
      expect(
        harness.nats.requests
            .where((request) => request.subject.endsWith(".topology.configure"))
            .map((request) => request.subject),
        [_configureSubject],
      );
    },
  );

  test(
    "Host draft requires an existing Realm when Realm hosting is disabled",
    () async {
      final harness = await _Harness.create();
      addTearDown(harness.dispose);
      final owners = EditorOwnerRegistry();
      addTearDown(owners.dispose);
      final owner =
          (harness.selectable.buildPresentation(owners).inputs[const BindingId(
                        1,
                      )]
                      as PresentationEditInput)
                  .owner
              as EditorSource;
      expect(
        owner.update(
          DataPath.root.field("engine"),
          _mode("EngineEnabled", {
            "target": StringValue("paper@*"),
            "realm": StringValue(""),
          }),
        ),
        isA<AppliedEditorMutation>(),
      );
      expect(
        owner.draftDiagnostics.map((issue) => issue.path),
        contains(DataPath.root.field("engine").field("realm")),
      );
      expect(await owner.flush(), isA<MutationInvalid>());
      expect(harness.nats.requests, isEmpty);
      owner.update(
        DataPath.root.field("realm"),
        _mode("RealmHosted", {"target": StringValue("paper@*")}),
      );
      expect(owner.draftDiagnostics, isEmpty);
      owner.update(DataPath.root.field("realm"), _mode("RealmDisabled"));
      expect(await owner.flush(), isA<MutationInvalid>());
      expect(harness.nats.requests, isEmpty);
      owner.discardDraft();
      expect(owner.hasWork, isFalse);
    },
  );
}
