import "package:flutter/material.dart";
import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";

import "support/members_test_support.dart";

void main() {
  group("OrganizationMembers.updateMemberRoles role merging", () {
    late FakeNatsClient mockNats;

    setUp(() {
      mockNats = FakeNatsClient();
    });

    tearDown(() {
      mockNats.dispose();
    });

    test(
      "refreshes current membership instead of applying a historical receipt",
      () async {
        final role = createRole(
          id: "member",
          name: "Member",
          color: Colors.grey,
          assignable: true,
        );

        final member = OrganizationMember(
          userId: recordId("user:m1"),
          name: "Original Name",
          email: "test@test.com",
          avatarUrl: "",
          roles: [role],
          joinedAt: testTimestamp,
        );

        var reads = 0;
        final container = ProviderContainer.test(
          overrides: [
            userIdProvider.overrideWith((ref) async => testUserId),
            organizationIdProvider.overrideWith((ref) => testOrganizationId),
            natsProvider.overrideWithValue(mockNats),
            organizationMembersProvider.overrideWith(
              () => _RefreshingMembers(
                () => [
                  if (reads++ == 0)
                    member
                  else
                    member.copyWith(name: "Current Name"),
                ],
              ),
            ),
            organizationRolesProvider.overrideWith(
              () => MockRolesNotifier([role]),
            ),
          ],
        );

        await readMembers(container);

        mockNats.registerHandler(
          memberUpdateSubject,
          (data) =>
              skir.UpdateOrganizationMemberRolesResponse.serializer.toBytes(
                skir.UpdateOrganizationMemberRolesResponse.wrapSuccess([
                  skir.OrganizationMember(
                    userId: recordId("user:m1"),
                    name: "Historical Name",
                    email: "test@test.com",
                    avatarUrl: "",
                    roles: [],
                    joinedAt: testTimestamp,
                  ),
                ]),
              ),
        );

        await container
            .read(organizationMembersProvider.notifier)
            .updateMemberRoles([recordId("user:m1")], [role]);

        final current = await readMembers(container);
        expect(current.single.name, "Current Name");
        expect(reads, 2);
      },
    );
  });
}

class _RefreshingMembers extends OrganizationMembers {
  _RefreshingMembers(this.load);
  final List<OrganizationMember> Function() load;

  @override
  Stream<List<OrganizationMember>> build() async* {
    yield load();
  }
}
