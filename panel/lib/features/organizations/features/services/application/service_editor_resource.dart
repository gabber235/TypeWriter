part of "services.dart";

EditorSnapshot serviceEditorSnapshot(Service service) => DocumentEditorSnapshot(
  EditorDocument(
    rootType: _serviceIdentityType,
    typeCatalog: _serviceInspectorCatalog,
    confirmedValue: service.identityValue,
    revision: service.revision,
  ),
);

final class ServiceEditorResource implements EditableResource {
  const ServiceEditorResource(this.repository, this.serviceId);
  final ServiceResourceRepository repository;
  final skir.RecordId serviceId;
  @override
  EditorResourceKey get key => EditorResourceKey(
    scope: (repository.organization, null),
    identity: serviceId,
  );
  @override
  Set<Object> get reservations => {(repository.organization, serviceId)};
  @override
  Future<EditorSnapshot?> refresh() async {
    final values = await repository.services();
    final service = values.firstWhereOrNull(
      (value) => value.serviceId == serviceId,
    );
    return service == null ? null : serviceEditorSnapshot(service);
  }

  @override
  MutationIntent prepare(
    EditorSnapshot snapshot,
    EditorCommit commit,
    void Function(TypedMutationResult) accept,
  ) {
    final name = DataPath.root
        .field("name")
        .read(commit.rootValue)
        .valueOrNull
        ?.asStringOrNull;
    if (name == null || name.trim().isEmpty) {
      throw StateError("Name must not be empty");
    }
    return IndependentMutation(
      PendingCommit(
        resources: reservations,
        prepare: () => repository
            .rename(serviceId, commit.expectedRevision, name)
            .copyWith(
              integrate: (result) async {
                switch (result) {
                  case SubmissionConfirmed(:final value) ||
                      SubmissionRejected(
                        response: final skir.UpdateOrganizationServiceResponse
                        value,
                      ):
                    switch (value) {
                      case skir.UpdateOrganizationServiceResponse_successWrapper(
                        :final value,
                      ):
                        final actual = Service.fromSkir(value);
                        repository.acceptService(actual);
                        accept(
                          MutationSuccess(
                            revision: actual.revision,
                            value: actual.identityValue,
                          ),
                        );
                      case skir.UpdateOrganizationServiceResponse_conflictErrorWrapper(
                        :final value,
                      ):
                        final actual = Service.fromSkir(value.actual);
                        repository.acceptService(actual);
                        accept(
                          MutationConflict(
                            expectedRevision: commit.expectedRevision,
                            actualRevision: actual.revision,
                            actualValue: actual.identityValue,
                          ),
                        );
                      case skir.UpdateOrganizationServiceResponse_serviceNotFoundErrorWrapper():
                        accept(
                          unavailableMutation(
                            "The service was deleted",
                            targetDeleted: true,
                          ),
                        );
                      case skir.UpdateOrganizationServiceResponse_validationErrorWrapper() ||
                          skir.UpdateOrganizationServiceResponse_invalidRecordIdErrorWrapper():
                        accept(
                          invalidMutation(
                            "The service contains invalid values",
                          ),
                        );
                      default:
                        accept(
                          unavailableMutation("The service could not be saved"),
                        );
                    }
                  default:
                    break;
                }
              },
            ),
      ),
    );
  }
}
