part of "services.dart";

const serviceInspectorTypeRef = ResolvedTypeRef(
  id: QualifiedTypeId(namespace: "panel", name: "Service"),
  revision: 2,
);

final _serviceInspectorType = TypeDefinition(
  id: serviceInspectorTypeRef,
  kind: NominalTypeKind.concrete,
  representation: RecordType(
    fields: {
      "version": const TypeField(name: "version", type: StringType()),
      "state": const TypeField(name: "state", type: StringType()),
      "lastSeen": TypeField(
        name: "lastSeen",
        type: NamedType(standardTypeRefs.optionOf(const TimestampType())),
      ),
    },
  ),
);

final _serviceInspectorCatalog = TypeCatalog([_serviceInspectorType]);

class ServiceIdentifier extends SelectableIdentifier {
  ServiceIdentifier(this.serviceId);

  final skir.RecordId serviceId;

  @override
  String get id => serviceId.id;
  @override
  Object get resourceId => serviceId;

  @override
  AsyncValue<Selectable> create(Ref ref) {
    final connections = ref.watch(serviceConnectionsProvider);
    final organization = ref.watch(organizationIdProvider);
    if (organization == null) {
      return AsyncError(ApiException.noOrganization(), StackTrace.current);
    }
    final repository = ref.watch(
      organizationServicesProvider(organization).notifier,
    );
    final serviceAsync = ref.watch(serviceProvider(serviceId));
    if (serviceAsync.mapUnready<Selectable>() case final value?) return value;
    final service = serviceAsync.requireValue;
    if (service == null) throw SelectableNotFoundException(this);

    return AsyncData(
      ServiceSelectable(
        editTarget: serviceIdentityTarget(
          id: this,
          service: service,

          repository: ref
              .watch(resourceRepositoriesProvider)
              .services(organization),
        ),
        onUnbind: () => repository.deleteService(serviceId),
        id: this,
        service: service,
        connected: connections[serviceId] ?? false,
      ),
    );
  }

  @override
  int get hashCode => serviceId.hashCode;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is ServiceIdentifier && other.serviceId == serviceId;

  @override
  String toString() => "ServiceIdentifier(id: $serviceId)";
}

class ServiceSelectable extends InspectableSelectable<ServiceIdentifier> {
  const ServiceSelectable({
    required this.editTarget,
    required this.onUnbind,
    required this.id,
    required this.service,
    required this.connected,
  });

  @override
  final ServiceIdentifier id;
  final Service service;
  final bool connected;
  final EditorTarget editTarget;
  final Future<void> Function() onUnbind;

  RecordValue get _data => service.observationValue(connected);

  @override
  String get name => service.displayName;

  @override
  PresentationModel buildPresentation(
    EditorOwnerRegistry owners,
  ) => PresentationModel(
    catalog: _serviceInspectorCatalog,
    inputs: {
      const BindingId(0): PresentationInput.value(
        type: NamedType(serviceInspectorTypeRef),
        value: EditorValue.ready(_data),
      ),
      const BindingId(1): PresentationInput.edit(owners.editor(editTarget)),
    },
    presentations: [serviceInspectorPresentation(service)],
    root: PresentationNode(
      id: "service",
      element: PresentationInvocationElement(
        presentationId: _serviceInspectorPresentationId,
        arguments: {
          const BindingId(0): const BindingReference(bindingId: BindingId(0)),
          const BindingId(1): const BindingReference(bindingId: BindingId(1)),
        },
      ),
    ),
  );

  @override
  List<SelectionCapability> get capabilities => [
    UnbindSelectionCapability(onUnbind: onUnbind),
  ];

  @override
  Widget? buildInspectorHeader() => ServiceHeader(
    id: service.serviceId.id,
    name: service.displayName,
    color: service.color,
  );
}

extension ServiceInspectorValue on Service {
  RecordValue get identityValue => RecordValue({"name": StringValue(name)});

  RecordValue observationValue(bool connected) => RecordValue({
    "version": StringValue(role.version),
    "state": StringValue(connected ? "Connected" : "Offline"),
    "lastSeen": _optionalTimestamp(lastSeen),
  });
}

final _serviceIdentityType = RecordType(
  fields: {"name": TypeField(name: "name", type: identifierStringType)},
);

/// Builds an identity editor from resolved service state and scoped commands.
ResourceEditorTarget serviceIdentityTarget({
  required ServiceIdentifier id,
  required Service service,
  required ServiceResourceRepository repository,
}) => ResourceEditorTarget(
  targetId: id,
  label: "${service.displayName}: identity",
  resource: ServiceEditorResource(repository, service.serviceId),
  snapshot: serviceEditorSnapshot(service),
);
