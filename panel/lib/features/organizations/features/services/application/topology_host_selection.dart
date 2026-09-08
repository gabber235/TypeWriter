part of "services.dart";

class _ServiceHostSelectable
    extends InspectableSelectable<ServiceHostIdentifier> {
  _ServiceHostSelectable({
    required this.ref,
    required this.id,
    required this.host,
    required this.service,
    required this.topology,
    required this.connected,
  });

  final Ref ref;
  @override
  final ServiceHostIdentifier id;
  final TopologyHost host;
  final Service? service;
  final OrganizationTopology topology;
  final bool connected;
  late final ScopedOrganizationTopology _repository = ref.read(
    scopedOrganizationTopologyProvider(
      ref.read(organizationIdProvider)!,
    ).notifier,
  );
  OrganizationTopology get _configurationTopology =>
      _repository.snapshot ?? topology;

  TopologyRealm? get _realm => topology.realmOwnedBy(host.hostId);
  TopologyEngine? get _engine => topology.engineOwnedBy(host.hostId);

  Map<String, List<String>> get _realmTargets {
    final catalog = _engineTargetCatalog(
      _configurationTopology.hosts.expand(
        (candidate) => candidate.supportedEngines,
      ),
    );
    final target = _realm?.targetEngine;
    if (target != null) {
      final constraints = catalog.putIfAbsent(target.engineId, () => []);
      if (!constraints.contains(target.versionConstraint))
        constraints.add(target.versionConstraint);
    }
    return catalog;
  }

  Map<String, List<String>> get _engineTargets {
    final catalog = _engineTargetCatalog(host.supportedEngines);
    final target = _engine?.target;
    if (target != null) {
      final constraints = catalog.putIfAbsent(target.engineId, () => []);
      if (!constraints.contains(target.versionConstraint))
        constraints.add(target.versionConstraint);
    }
    return catalog;
  }

  @override
  String get name => service?.displayName ?? host.hostId.id;

  @override
  PresentationModel buildPresentation(EditorOwnerRegistry owners) {
    final view = _hostObservation(host, service);
    final definition = _hostInspectorPresentation(
      realmTargets: _realmTargets,
      engineTargets: _engineTargets,
      realms: topology.realmInstances
          .where((realm) => realm.ownerHost.id != host.hostId)
          .toList(),
      canHostRealm: host.canHostRealm,
      color: service?.color ?? standaloneServiceColor,
    );
    final target = ResourceEditorTarget(
      targetId: id,
      label: "$name: configuration",
      commitPolicy: EditorCommitPolicy.applyResource,
      updates: _repository
          .watchValues()
          .where((value) => value.hasValue || value.hasError)
          .map((value) {
            final topology = value.requireValue;
            final current = topology.hosts
                .where((item) => item.hostId == host.hostId)
                .firstOrNull;
            return current == null
                ? null
                : EditorDocument(
                    rootType: _hostConfigurationType,
                    typeCatalog: _hostInspectorCatalog,
                    confirmedValue: _configurationValue(
                      topology.realmOwnedBy(host.hostId),
                      topology.engineOwnedBy(host.hostId),
                    ),
                    revision: current.revision,
                  );
          }),
      validateDraft: _configurationIssues,
      document: EditorDocument(
        rootType: _hostConfigurationType,
        typeCatalog: _hostInspectorCatalog,
        confirmedValue: _configurationValue(_realm, _engine),
        revision: host.revision,
      ),
      commit: _commitConfiguration,
    );
    return PresentationModel(
      catalog: _hostInspectorCatalog,
      inputs: {
        const BindingId(0): PresentationInput.value(
          type: NamedType(_hostInspectorTypeRef),
          value: EditorValue.ready(view),
        ),
        const BindingId(1): PresentationInput.edit(owners.editor(target)),
        if (service case final service?)
          const BindingId(2): PresentationInput.edit(
            owners.editor(
              ServiceSelectable(
                ref: ref,
                id: ServiceIdentifier(service.serviceId),
                service: service,
                connected: connected,
              ).editTarget,
            ),
          ),
        if (service == null)
          const BindingId(2): PresentationInput.value(
            type: _serviceIdentityType,
            value: EditorValue.ready(
              RecordValue({"name": StringValue("Unavailable")}),
            ),
          ),
      },
      presentations: [definition],
      root: PresentationNode(
        id: "host",
        element: PresentationInvocationElement(
          presentationId: definition.id,
          arguments: {
            for (final input in definition.inputs)
              input.id: BindingReference(bindingId: input.id),
          },
        ),
      ),
    );
  }

  @override
  List<SelectionCapability> get capabilities => [
    if (service case final backingService?)
      UnbindSelectionCapability(
        onUnbind: () => ref
            .read(servicesProvider.notifier)
            .deleteService(backingService.serviceId),
      ),
  ];

  @override
  Widget? buildInspectorHeader() => ServiceHeader(
    id: host.hostId.id,
    name: name,
    color: service?.color ?? standaloneServiceColor,
  );

  Future<TypedMutationResult> _commitConfiguration(EditorCommit commit) async {
    final execution = _decodeExecution(commit.rootValue);
    if (execution == null) {
      return invalidMutation("The host configuration is invalid");
    }
    try {
      final configured = await _repository.configureHost(
        host: host.copyWith(revision: commit.expectedRevision),
        execution: execution,
      );
      return TypedMutationResult.success(
        revision: configured.host.revision,
        value: _configurationValue(configured.realm, configured.engine),
      );
    } on _HostConfigurationConflict catch (conflict) {
      return TypedMutationResult.conflict(
        expectedRevision: commit.expectedRevision,
        actualRevision: conflict.actual.host.revision,
        actualValue: _configurationValue(
          conflict.actual.realm,
          conflict.actual.engine,
        ),
      );
    } on SubmissionException<skir.ConfigureServiceHostResponse> catch (error) {
      return error.toMutation(
        (_) async => throw StateError("Host replay is unsupported"),
      );
    } on ApiException catch (error) {
      return unavailableMutation(error.message);
    }
  }

  RecordValue _hostObservation(
    TopologyHost currentHost,
    Service? currentService,
  ) {
    return RecordValue({
      _HostInspectorFields.service: RecordValue({
        _HostInspectorFields.version: StringValue(
          currentService?.role.version ?? "Unavailable",
        ),
        _HostInspectorFields.state: StringValue(
          connected ? "Connected" : "Offline",
        ),
        _HostInspectorFields.lastSeen: _optionalTimestamp(
          currentService?.lastSeen,
        ),
      }),
      _HostInspectorFields.host: RecordValue({
        _HostInspectorFields.entrypoint: StringValue(
          currentHost.entrypoint.formatted,
        ),
        _HostInspectorFields.canHostRealm: BooleanValue(
          currentHost.canHostRealm,
        ),
        _HostInspectorFields.supportedEngines: ListValue(
          currentHost.supportedEngines
              .map((supported) => StringValue(supported.engineId))
              .toList(),
        ),
        _HostInspectorFields.state: StringValue(
          hostRuntimeStatusLabel(currentHost.state.status),
        ),
        _HostInspectorFields.message: StringValue(
          currentHost.state.message ?? "None",
        ),
        _HostInspectorFields.updatedAt: TimestampValue(
          currentHost.state.updatedAt,
        ),
      }),
    });
  }

  TopologyEngineTarget? _decodeTarget(
    String? value,
    Map<String, List<String>> targets,
  ) {
    if (value == null) return null;
    final separator = value.lastIndexOf("@");
    if (separator <= 0) return null;
    final id = value.substring(0, separator);
    final constraint = value.substring(separator + 1);
    if (!(targets[id]?.contains(constraint) ?? false)) return null;
    return TopologyEngineTarget(engineId: id, versionConstraint: constraint);
  }
}
