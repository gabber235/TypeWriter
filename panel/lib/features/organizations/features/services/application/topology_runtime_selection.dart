part of "services.dart";

/// Exposes Realm deployment state through the shared inspector presentation.
///
/// Realm runtime data is observational. Opening the Realm is available only
/// while the owner host service is connected, because all Realm operations use
/// that service connection.
class _RealmInstanceSelectable
    extends InspectableSelectable<RealmInstanceIdentifier> {
  const _RealmInstanceSelectable({
    required this.onOpen,
    required this.id,
    required this.realm,
    required this.connected,
    required this.host,
    required this.service,
  });

  final VoidCallback? onOpen;
  @override
  final RealmInstanceIdentifier id;
  final TopologyRealm realm;
  final bool connected;
  final TopologyHost? host;
  final Service? service;

  @override
  String get name => realm.ownerHost.name.formatted;

  @override
  PresentationModel buildPresentation(EditorOwnerRegistry owners) =>
      PresentationModel(
        catalog: _realmInstanceInspectorCatalog,
        inputs: {
          const BindingId(0): PresentationInput.value(
            type: NamedType(_realmInstanceInspectorTypeRef),
            value: EditorValue.ready(
              _runtimeValue(
                ownerHost: realm.ownerHost.name.formatted,
                target: realm.targetEngine,
                state: realm.state,
              ),
            ),
          ),
        },
        root: _realmInstanceInspectorPresentation.root,
      );

  @override
  List<SelectionCapability> get capabilities => [
    if (onOpen case final open?)
      OpenSelectionCapability(onOpen: open, allowMultiSelect: false),
  ];

  @override
  Widget? buildInspectorHeader() => ServiceHeader(
    id: realm.realmId.id,
    name: name,
    color: realmServiceRoleColor,
  );
}

OrganizationRoute realmNavigationRoute(
  skir.RecordId organizationId,
  skir.RecordId realmId,
) => OrganizationRoute(
  organizationId: organizationId.id,
  children: [RealmRoute(realmId: realmId.id)],
);

/// Exposes execution engine deployment state through the shared inspector.
///
/// Engine instances are controlled through their owner host configuration, so
/// their own inspector remains read only and reports assignment and lifecycle
/// state without creating a second mutation path.
class _EngineInstanceSelectable
    extends InspectableSelectable<EngineInstanceIdentifier> {
  _EngineInstanceSelectable({
    required this.id,
    required this.engine,
    required this.host,
    required this.service,
  });

  @override
  final EngineInstanceIdentifier id;
  final TopologyEngine engine;
  final TopologyHost? host;
  final Service? service;

  @override
  String get name => "${engine.target.engineId} engine";

  @override
  PresentationModel buildPresentation(EditorOwnerRegistry owners) =>
      PresentationModel(
        catalog: _engineInstanceInspectorCatalog,
        inputs: {
          const BindingId(0): PresentationInput.value(
            type: NamedType(_engineInstanceInspectorTypeRef),
            value: EditorValue.ready(
              _runtimeValue(
                ownerHost: engine.ownerHost.name.formatted,
                target: engine.target,
                state: engine.state,
                assignedRealm: engine.realm.ownerHost.name.formatted,
              ),
            ),
          ),
        },
        root: _engineInstanceInspectorPresentation.root,
      );

  @override
  List<SelectionCapability> get capabilities => [];

  @override
  Widget? buildInspectorHeader() => ServiceHeader(
    id: engine.engineId.id,
    name: name,
    color: engineServiceRoleColor,
  );
}

RecordValue _runtimeValue({
  required String ownerHost,
  required TopologyEngineTarget target,
  required TopologyRuntimeState state,
  String? assignedRealm,
}) => RecordValue({
  _RuntimeInspectorFields.ownerHost: ownerHost.asValue,
  if (assignedRealm != null)
    _RuntimeInspectorFields.assignedRealm: assignedRealm.asValue,
  _RuntimeInspectorFields.target: _targetLabel(target).asValue,
  _RuntimeInspectorFields.runtimeStatus: childRuntimeStatusLabel(
    state.status,
  ).asValue,
  _RuntimeInspectorFields.artifactVersion:
      (state.activeArtifactVersion ?? "None").asValue,
  _RuntimeInspectorFields.runtimeMessage: (state.message ?? "None").asValue,
  _RuntimeInspectorFields.updatedAt: TimestampValue(state.updatedAt),
});
