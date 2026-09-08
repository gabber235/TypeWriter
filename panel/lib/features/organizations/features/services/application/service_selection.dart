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
  AsyncValue<Selectable> create(Ref ref) {
    final services = ref.watch(servicesProvider).value ?? const <Service>[];
    final connections = ref.watch(serviceConnectionsProvider(services));
    return ref.watch(serviceProvider(serviceId)).whenData((value) {
      if (value == null) throw SelectableNotFoundException(this);
      return ServiceSelectable(
        ref: ref,
        id: this,
        service: value,
        connected: connections[serviceId] ?? false,
      );
    });
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
  ServiceSelectable({
    required this.ref,
    required this.id,
    required this.service,
    required this.connected,
  });

  @override
  final ServiceIdentifier id;
  final Service service;
  final bool connected;
  final Ref ref;

  RecordValue get _data => service.observationValue(connected);

  @override
  String get name => service.displayName;

  EditorTarget get editTarget {
    final organization = ref.read(organizationIdProvider);
    if (organization == null) throw ApiException.noOrganization();
    final repository = ref.read(
      organizationServicesProvider(organization).notifier,
    );
    return ResourceEditorTarget(
      targetId: id,
      label: "$name: identity",
      updates: repository
          .watchValues()
          .where((value) => value.hasValue || value.hasError)
          .map((value) {
            final current = value.requireValue
                .where((item) => item.serviceId == service.serviceId)
                .firstOrNull;
            return current == null
                ? null
                : EditorDocument(
                    rootType: _serviceIdentityType,
                    typeCatalog: _serviceInspectorCatalog,
                    confirmedValue: current.identityValue,
                    revision: current.revision,
                  );
          }),
      document: EditorDocument(
        rootType: _serviceIdentityType,
        typeCatalog: _serviceInspectorCatalog,
        confirmedValue: RecordValue({"name": StringValue(service.name)}),
        revision: service.revision,
      ),
      commit: (commit) async {
        final value = commit.rootValue;
        if (value is! RecordValue)
          return invalidMutation("Service identity is invalid");
        final name = value.fields["name"]?.stringOrNull;
        if (name == null || name.trim().isEmpty)
          return invalidMutation("Name must not be empty");
        final result = await repository.updateService(
          service.copyWith(revision: commit.expectedRevision, name: name),
        );
        return switch (result) {
          MutationSuccess(:final revision, :final value) =>
            TypedMutationResult.success(
              revision: revision,
              value: _identityValue(value),
            ),
          MutationConflict(
            :final expectedRevision,
            :final actualRevision,
            :final actualValue,
          ) =>
            TypedMutationResult.conflict(
              expectedRevision: expectedRevision,
              actualRevision: actualRevision,
              actualValue: _identityValue(actualValue),
            ),
          _ => result,
        };
      },
    );
  }

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
    UnbindSelectionCapability(
      onUnbind: () =>
          ref.read(servicesProvider.notifier).deleteService(service.serviceId),
    ),
  ];

  @override
  Widget? buildInspectorHeader() => ServiceHeader(
    id: service.serviceId.id,
    name: service.displayName,
    color: service.color,
  );

  @override
  int get hashCode => Object.hash(id, service, connected);

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is ServiceSelectable &&
          other.id == id &&
          other.service == service &&
          other.connected == connected;
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

RecordValue _identityValue(DataValue value) =>
    RecordValue({"name": (value as RecordValue).fields["name"]!});
