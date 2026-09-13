import "package:collection/collection.dart";
import "package:flutter/foundation.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";

part "organization.freezed.dart";
part "organization.g.dart";

@freezed
abstract class OrganizationData with _$OrganizationData {
  @Assert("name != \"\"", "Name must not be empty.")
  const factory OrganizationData({
    required skir.RecordId organizationId,
    required String name,
    required String logoUrl,
  }) = _OrganizationData;

  const OrganizationData._();

  factory OrganizationData.fromSkir(skir.Organization org) {
    return OrganizationData(
      organizationId: org.organizationId,
      name: org.name,
      logoUrl: org.logoUrl,
    );
  }

  skir.Organization toSkir() {
    return skir.Organization(
      organizationId: this.organizationId,
      name: name,
      logoUrl: logoUrl,
    );
  }
}

@riverpod
class Organizations extends _$Organizations {
  final _sequenceState = SequencedCollection<List<OrganizationData>>();

  @override
  Stream<List<OrganizationData>> build() async* {
    final userId = await ref.watch(userIdProvider.future);
    if (userId == null) {
      yield [];
      return;
    }

    yield* ref.watchSequencedRequest(
      subject: "cloud.to.user.$userId.organization.watch",
      eventSubject: "cloud.from.user.$userId.organizations.changed",
      requestBytes: skir.WatchUserOrganizationsRequest.serializer.toBytes(
        skir.WatchUserOrganizationsRequest(),
      ),
      responseSerializer: skir.WatchUserOrganizationsResponse.serializer,
      eventSerializer: skir.UserOrganizationsChanged.serializer,
      snapshot: (response) {
        return switch (response) {
          skir.WatchUserOrganizationsResponse_unknown() =>
            throw ApiException.unknownResponseMessage(),
          skir.WatchUserOrganizationsResponse_internalErrorWrapper() =>
            throw ApiException.internalServerError(),
          skir.WatchUserOrganizationsResponse_snapshotWrapper(:final value) =>
            SequencedSnapshot(
              sequence: value.sequence,
              value: value.values.map(OrganizationData.fromSkir).toList(),
            ),
          skir.WatchUserOrganizationsResponse_changedWrapper() =>
            throw StateError("Snapshot request returned a delta"),
        };
      },
      eventSequence: (event) => event.sequence,
      reduce: _reduceOrganizations,
      sequenceState: _sequenceState,
    );
  }

  /// Creates a new organization and returns its ID
  ///
  /// [name] The name of the organization
  /// [logoUrl] The URL of the organization's logo
  ///
  /// Returns the ID of the created organization
  Future<skir.RecordId> createOrganization({
    required String name,
    required String logoUrl,
  }) async {
    state.ensureReady();

    final userId = await ref.read(userIdProvider.future);
    if (userId == null) {
      throw ApiException.notAuthenticated();
    }

    final request = skir.CreateOrganizationRequest(
      operationId: uuid.v4(),
      name: name,
      logoUrl: logoUrl,
    );

    debugPrint(
      "Creating organization with name: '$name' and logoUrl: '$logoUrl'",
    );

    final response = await ref.mutateSkir(
      "cloud.to.user.$userId.organization.create",
      skir.CreateOrganizationRequest.serializer.toBytes(request),
      skir.CreateOrganizationResponse.serializer,
      submissionId: request.operationId,
      replay: SubmissionReplay.identicalRequest,
      label: "Create organization",
      classify: (response) => switch (response) {
        skir.CreateOrganizationResponse_invalidOperationIdErrorWrapper() ||
        skir.CreateOrganizationResponse_operationIdentityReusedErrorWrapper() =>
          MutationResponseDisposition.rejected,
        skir.CreateOrganizationResponse_successWrapper() =>
          MutationResponseDisposition.confirmed,
        skir.CreateOrganizationResponse_unknown() ||
        skir.CreateOrganizationResponse_internalErrorWrapper() =>
          MutationResponseDisposition.uncertain,
      },
    );

    switch (response) {
      case skir.CreateOrganizationResponse_invalidOperationIdErrorWrapper():
        throw ApiException.badRequest("Operation identity is required");
      case skir.CreateOrganizationResponse_operationIdentityReusedErrorWrapper():
        throw ApiException.conflict(
          "Operation identity was reused with different input",
        );
      case skir.CreateOrganizationResponse_unknown():
        throw ApiException.unknownResponseMessage();
      case skir.CreateOrganizationResponse_internalErrorWrapper():
        throw ApiException.internalServerError();
      case skir.CreateOrganizationResponse_successWrapper(:final value):
        _applyEvent(value.event);
        return value.organization.organizationId;
    }
  }

  void _applyEvent(skir.UserOrganizationsChanged event) {
    switch (_sequenceState.apply(
      sequence: event.sequence,
      reduce: (organizations) => _reduceOrganizations(organizations, event),
    )) {
      case SequencedEventResult.duplicate:
        return;
      case SequencedEventResult.applied:
        state = AsyncData(_sequenceState.value);
      case SequencedEventResult.gap:
        ref.invalidateSelf();
    }
  }
}

List<OrganizationData> _reduceOrganizations(
  List<OrganizationData> organizations,
  skir.UserOrganizationsChanged event,
) {
  return event.changes.fold(organizations, (current, change) {
    return switch (change) {
      skir.UserOrganizationsChange_unknown() =>
        throw ApiException.unknownResponseMessage(),
      skir.UserOrganizationsChange_addWrapper(:final value) =>
        current.upsertByKey(
          (organization) => organization.organizationId,
          OrganizationData.fromSkir(value),
        ),
      skir.UserOrganizationsChange_removeWrapper(:final value) =>
        current
            .where((organization) => organization.organizationId != value)
            .toList(),
    };
  });
}

@riverpod
skir.RecordId? organizationId(Ref ref) {
  final id = ref.watch(routeParamProvider("organizationId"));
  if (id == null) return null;
  return recordId("organization:$id");
}

@riverpod
class Organization extends _$Organization {
  @override
  Future<OrganizationData?> build() async {
    final id = ref.watch(organizationIdProvider);
    if (id == null) {
      return null;
    }
    final organizations = await ref.watch(organizationsProvider.future);
    return organizations.firstWhereOrNull((org) => org.organizationId == id);
  }
}

/// Generates an icon URL for an organization using the provided seed
String generateOrganizationIconUrl(String seed) {
  return "https://api.dicebear.com/9.x/shapes/webp?seed=$seed";
}
