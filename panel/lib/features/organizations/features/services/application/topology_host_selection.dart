part of "services.dart";

class _ServiceHostSelectable
    extends InspectableSelectable<ServiceHostIdentifier> {
  const _ServiceHostSelectable({
    required this.id,
    required this.host,
    required this.service,
    required this.topology,
    required this.connected,
    required this.configurationTarget,
    required this.onUnbind,
    required this.serviceIdentityTarget,
  });

  @override
  final ServiceHostIdentifier id;
  final TopologyHost host;
  final Service? service;
  final OrganizationTopology topology;
  final bool connected;
  final EditorTarget configurationTarget;
  final Future<void> Function()? onUnbind;
  final EditorTarget? serviceIdentityTarget;

  HostEditorSnapshot get _configuration =>
      configurationTarget.snapshot as HostEditorSnapshot;
  Map<String, List<String>> get _realmTargets => _configuration._realmTargets;
  Map<String, List<String>> get _engineTargets => _configuration._engineTargets;

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
    return PresentationModel(
      catalog: _hostInspectorCatalog,
      inputs: {
        const BindingId(0): PresentationInput.value(
          type: NamedType(_hostInspectorTypeRef),
          value: EditorValue.ready(view),
        ),
        const BindingId(1): PresentationInput.edit(
          owners.editor(configurationTarget),
        ),
        if (serviceIdentityTarget case final target?)
          const BindingId(2): PresentationInput.edit(owners.editor(target)),
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
    if (onUnbind case final unbind?)
      UnbindSelectionCapability(onUnbind: unbind),
  ];

  @override
  Widget? buildInspectorHeader() => ServiceHeader(
    id: host.hostId.id,
    name: name,
    color: service?.color ?? standaloneServiceColor,
  );

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
}
