import "package:collection/collection.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";

part "members.freezed.dart";
part "members.g.dart";

@freezed
abstract class OrganizationMember with _$OrganizationMember {
  const factory OrganizationMember({
    required skir.RecordId userId,
    required List<OrganizationRole> roles,
    required DateTime joinedAt,
    String? name,
    String? email,
    String? avatarUrl,
  }) = _OrganizationMember;

  const OrganizationMember._();

  factory OrganizationMember.fromSkir(skir.OrganizationMember member) =>
      OrganizationMember(
        userId: member.userId,
        name: member.name,
        email: member.email,
        avatarUrl: member.avatarUrl,
        roles: member.roles.map(OrganizationRole.fromSkir).toList(),
        joinedAt: member.joinedAt,
      );

  skir.OrganizationMember toSkir() => skir.OrganizationMember(
    userId: this.userId,
    name: name,
    email: email,
    avatarUrl: avatarUrl,
    roles: roles.map((r) => r.toSkir()),
    joinedAt: joinedAt,
  );
}

@riverpod
class OrganizationMembers extends _$OrganizationMembers {
  @override
  Stream<List<OrganizationMember>> build() async* {
    final userId = await ref.watch(userIdProvider.future);
    if (userId == null) {
      yield [];
      return;
    }
    final organizationId = ref.watch(organizationIdProvider);
    if (organizationId == null) {
      yield [];
      return;
    }

    final request = skir.WatchOrganizationMembersRequest();
    yield* ref.watchRequest(
      subject:
          "cloud.to.user.$userId.organization.${organizationId.id}.members.watch",
      listenSubject:
          "cloud.from.organization.${organizationId.id}.members.watch",
      requestBytes: skir.WatchOrganizationMembersRequest.serializer.toBytes(
        request,
      ),
      serializer: skir.WatchOrganizationMembersResponse.serializer,
      transformer: (previous, response) {
        switch (response) {
          case skir.WatchOrganizationMembersResponse_unknown():
            throw ApiException.unknownResponseMessage();
          case skir.WatchOrganizationMembersResponse_internalErrorWrapper():
            throw ApiException.internalServerError();
          case skir.WatchOrganizationMembersResponse_listWrapper(:final value):
            return value.map(OrganizationMember.fromSkir).toList();
          case skir.WatchOrganizationMembersResponse_addWrapper(:final value):
            return previous.upsertByKey(
              (member) => member.userId,
              OrganizationMember.fromSkir(value),
            );
          case skir.WatchOrganizationMembersResponse_updateWrapper(
            :final value,
          ):
            return previous.upsertByKey(
              (member) => member.userId,
              OrganizationMember.fromSkir(value),
            );
          case skir.WatchOrganizationMembersResponse_removeWrapper(
            :final value,
          ):
            return previous
                    ?.where((member) => member.userId != value)
                    .toList() ??
                [];
        }
      },
    );
  }

  Future<List<OrganizationRole>> ensureCorrectRoles(
    skir.RecordId memberId,
    List<OrganizationRole> newRoles,
  ) async {
    final oldRoles =
        state.requireValue
            .firstWhereOrNull((m) => m.userId == memberId)
            ?.roles ??
        [];

    final roles = {
      ...oldRoles.where((r) => !r.assignable),
      ...newRoles.where((r) => r.assignable),
    };

    if (roles.isEmpty) {
      final availableRoles = await ref.read(organizationRolesProvider.future);
      final defaultRoles = availableRoles
          .where((role) => role.defaultRole)
          .toList();
      assert(defaultRoles.isNotEmpty, "No default roles available.");
      return defaultRoles;
    }

    return roles.toList();
  }

  /// Applies a role choice atomically to the captured member selection.
  Future<void> updateMemberRoles(
    Iterable<skir.RecordId> memberIds,
    List<OrganizationRole> requestedRoles,
  ) async {
    final ids = List<skir.RecordId>.unmodifiable(memberIds);
    final requested = List<OrganizationRole>.unmodifiable(requestedRoles);
    final userId = await ref.read(userIdProvider.future);
    if (userId == null) {
      throw ApiException.notAuthenticated();
    }
    final organizationId = ref.read(organizationIdProvider);
    if (organizationId == null) {
      throw ApiException.noOrganization();
    }

    state.ensureReady();
    try {
      final request = skir.UpdateOrganizationMemberRolesRequest(
        operationId: uuid.v4(),
        userIds: ids,
        roleIds: requested
            .where((role) => role.assignable)
            .map((role) => role.roleId),
      );

      final response = await ref.mutateSkir(
        "cloud.to.user.$userId.organization.${organizationId.id}.members.update",
        skir.UpdateOrganizationMemberRolesRequest.serializer.toBytes(request),
        skir.UpdateOrganizationMemberRolesResponse.serializer,
        onResponse: (_) async {
          if (!ref.mounted ||
              ref.read(organizationIdProvider) != organizationId) {
            return;
          }
          ref.invalidateSelf();
          await future;
        },
        submissionId: request.operationId,
        replay: SubmissionReplay.identicalRequest,
        label: "Update member roles",
        resources: {for (final id in ids) (organizationId, id)},
        classify: (response) => switch (response) {
          skir.UpdateOrganizationMemberRolesResponse_successWrapper() =>
            MutationResponseDisposition.confirmed,
          skir.UpdateOrganizationMemberRolesResponse_unknown() ||
          skir.UpdateOrganizationMemberRolesResponse_internalErrorWrapper() =>
            MutationResponseDisposition.uncertain,
          _ => MutationResponseDisposition.rejected,
        },
      );

      switch (response) {
        case skir.UpdateOrganizationMemberRolesResponse_unknown():
          throw ApiException.unknownResponseMessage();
        case skir.UpdateOrganizationMemberRolesResponse_internalErrorWrapper():
          throw ApiException.internalServerError();
        case skir.UpdateOrganizationMemberRolesResponse_invalidRecordIdErrorWrapper(
          :final value,
        ):
          throw ApiException.invalidRecordId(value);
        case skir.UpdateOrganizationMemberRolesResponse_userNotFoundErrorWrapper():
          throw ApiException.notFound("User");
        case skir.UpdateOrganizationMemberRolesResponse_rolesNotFoundErrorWrapper():
          throw ApiException.notFound("Roles");
        case skir.UpdateOrganizationMemberRolesResponse_rolesNotAssignableErrorWrapper():
          throw ApiException.badRequest("One or more roles cannot be assigned");
        case skir.UpdateOrganizationMemberRolesResponse_operationIdentityReusedErrorWrapper():
          throw ApiException.conflict(
            "Operation identity was reused with different input",
          );
        case skir.UpdateOrganizationMemberRolesResponse_invalidSelectionErrorWrapper():
          throw ApiException.badRequest("Select distinct organization members");
        case skir.UpdateOrganizationMemberRolesResponse_rolesRequiredErrorWrapper():
          throw ApiException.badRequest("At least one role is required");
        case skir.UpdateOrganizationMemberRolesResponse_founderRoleRequiredErrorWrapper():
          throw ApiException.conflict(
            "Organization must retain at least one founder",
          );
        case skir.UpdateOrganizationMemberRolesResponse_successWrapper():
          break;
      }
    } catch (e) {
      ref.invalidateSelf();
      rethrow;
    }
  }

  /// Removes a member from the organization.
  Future<void> removeMember(skir.RecordId memberId) async {
    final userId = await ref.read(userIdProvider.future);
    if (userId == null) {
      throw ApiException.notAuthenticated();
    }
    final organizationId = ref.read(organizationIdProvider);
    if (organizationId == null) {
      throw ApiException.noOrganization();
    }

    state.ensureReady();

    try {
      final request = skir.RemoveOrganizationMemberRequest(
        operationId: uuid.v4(),
        userId: memberId,
      );

      final response = await ref.mutateSkir(
        "cloud.to.user.$userId.organization.${organizationId.id}.members.remove",
        skir.RemoveOrganizationMemberRequest.serializer.toBytes(request),
        skir.RemoveOrganizationMemberResponse.serializer,
        onResponse: (_) async {
          if (!ref.mounted ||
              ref.read(organizationIdProvider) != organizationId) {
            return;
          }
          ref.invalidateSelf();
          await future;
        },
        submissionId: request.operationId,
        replay: SubmissionReplay.identicalRequest,
        label:
            "Remove member: ${state.requireValue.firstWhereOrNull((member) => member.userId == memberId)?.name ?? memberId.id}",
        resources: {(organizationId, memberId)},
        classify: (response) => switch (response) {
          skir.RemoveOrganizationMemberResponse_successWrapper() =>
            MutationResponseDisposition.confirmed,
          skir.RemoveOrganizationMemberResponse_unknown() ||
          skir.RemoveOrganizationMemberResponse_internalErrorWrapper() =>
            MutationResponseDisposition.uncertain,
          _ => MutationResponseDisposition.rejected,
        },
      );

      switch (response) {
        case skir.RemoveOrganizationMemberResponse_invalidOperationIdErrorWrapper():
          throw ApiException.badRequest("Operation identity is required");
        case skir.RemoveOrganizationMemberResponse_operationIdentityReusedErrorWrapper():
          throw ApiException.conflict(
            "Operation identity was reused with different input",
          );
        case skir.RemoveOrganizationMemberResponse_unknown():
          throw ApiException.unknownResponseMessage();
        case skir.RemoveOrganizationMemberResponse_internalErrorWrapper():
          throw ApiException.internalServerError();
        case skir.RemoveOrganizationMemberResponse_invalidRecordIdErrorWrapper(
          :final value,
        ):
          throw ApiException.invalidRecordId(value);
        case skir.RemoveOrganizationMemberResponse_userNotMemberErrorWrapper():
          throw ApiException.notFound("Organization member");
        case skir.RemoveOrganizationMemberResponse_founderCannotBeRemovedErrorWrapper():
          throw ApiException.conflict("Organization founder cannot be removed");
        case skir.RemoveOrganizationMemberResponse_successWrapper():
          break;
      }
    } catch (e) {
      ref.invalidateSelf();
      rethrow;
    }
  }

  @override
  bool updateShouldNotify(
    AsyncValue<List<OrganizationMember>> previous,
    AsyncValue<List<OrganizationMember>> next,
  ) => true;
}

/// Provider for the list of pending join requests to the current organization.
