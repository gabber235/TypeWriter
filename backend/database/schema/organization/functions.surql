DEFINE FUNCTION OVERWRITE fn::organization::roles::valid_for(
    $roles: array<record<organization_role>>,
    $organization: record<organization>
) -> bool {
    RETURN array::len($roles) > 0 AND $roles.all(|$role| $role.organization = $organization);
};

DEFINE FUNCTION OVERWRITE fn::organization::roles::assignable_for(
    $roles: array<record<organization_role>>,
    $organization: record<organization>
) -> bool {
    RETURN $roles.all(|$role| $role.organization = $organization AND $role.assignable = true);
};

DEFINE FUNCTION OVERWRITE fn::organization::roles::has_named_role(
    $roles: array<record<organization_role>>,
    $name: string
) -> bool {
    RETURN $roles.any(|$role| $role.name = $name);
};

DEFINE FUNCTION OVERWRITE fn::organization::member(
    $account: option<record<user>>,
    $organization: option<record<organization>>
) -> bool {
    IF $account == NONE OR $organization == NONE {
        RETURN false
    };

    RETURN (SELECT VALUE id FROM member_of WHERE in = $account AND out = $organization LIMIT 1) != [];
};

DEFINE FUNCTION OVERWRITE fn::organization::founder_member(
    $account: option<record<user>>,
    $organization: option<record<organization>>
) -> bool {
    IF $account == NONE OR $organization == NONE {
        RETURN false
    };

    LET $role_sets = (SELECT VALUE roles FROM member_of WHERE out = $organization AND in = $account);
    LET $roles = array::flatten($role_sets);

    RETURN fn::organization::roles::has_named_role($roles, "founder");
};

DEFINE FUNCTION OVERWRITE fn::account::organization::unit(
    $account: record<user>,
    $organization: record<organization>
) -> option<record> {
    RETURN NONE;
};

DEFINE FUNCTION OVERWRITE fn::account::organization::teams(
    $account: record<user>,
    $organization: record<organization>
) -> array<any> {
    RETURN [];
};

DEFINE FUNCTION OVERWRITE fn::organization::members::update_roles(
    $org: record<organization>,
    $users: array<record<user>>,
    $roles: array<record<organization_role>>,
    $receipt: record<mutation_receipt>,
    $request_bytes: array<int>
) {
    LET $previous = fn::mutation::recall($receipt, $request_bytes);

    IF $previous != NONE {
        RETURN $previous
    };

    IF array::is_empty($users) OR array::len(array::distinct($users)) != array::len($users) {
        RETURN {
            outcome: 'invalid-selection-error'
        }
    };

    LET $members = SELECT *
        FROM member_of
        WHERE in IN $users AND out = $org;
    LET $missing_users = array::complement($users, $members.in);

    IF array::len($missing_users) > 0 {
        RETURN {
            outcome: 'user-not-found-error',
            user_ids: $missing_users
        }
    };

    LET $requested = SELECT *
        FROM $roles
        WHERE organization = $org;
    LET $missing = array::complement(array::distinct($roles), $requested.id);

    IF array::len($missing) > 0 {
        RETURN {
            outcome: 'roles-not-found-error',
            role_ids: $missing
        }
    };

    LET $defaults = SELECT VALUE id
        FROM organization_role
        WHERE organization = $org AND default_role;

    LET $changes = $members.map(|$member| {
        LET $protected = SELECT VALUE id
            FROM $member.roles
            WHERE !assignable;
        LET $effective = array::distinct(array::union($roles, $protected));

        RETURN {
            id: $member.id,
            user_id: $member.in,
            roles: IF array::is_empty($effective) { $defaults } ELSE { $effective },
            forbidden: (SELECT VALUE id FROM $requested WHERE !assignable AND id NOT IN $protected)
        };
    });

    LET $forbidden = $changes.filter(|$change| array::len($change.forbidden) > 0);

    IF array::len($forbidden) > 0 {
        RETURN {
            outcome: 'roles-not-assignable-error',
            user_ids: $forbidden.user_id,
            role_ids: array::distinct(array::flatten($forbidden.forbidden))
        }
    };

    LET $empty = $changes.filter(|$change| array::is_empty($change.roles));

    IF array::len($empty) > 0 {
        RETURN {
            outcome: 'roles-required-error',
            user_ids: $empty.user_id
        }
    };

    LET $remaining_founders = SELECT VALUE id
        FROM member_of
        WHERE out = $org
            AND in NOT IN $users
            AND fn::organization::roles::has_named_role(roles, 'founder');

    IF array::is_empty($remaining_founders)
        AND !array::any(
            $changes,
            |$change| fn::organization::roles::has_named_role($change.roles, 'founder')
        )
    {
        RETURN {
            outcome: 'founder-role-required-error'
        }
    };

    FOR $change IN $changes {
        UPDATE ONLY $change.id SET roles = $change.roles;
    };

    LET $result = {
        outcome: 'updated',
        members: (
            SELECT
                in.id AS user_id,
                in.name AS name,
                in.email AS email,
                in.avatar_url AS avatar_url,
                roles.* AS roles,
                joined_at
            FROM member_of
            WHERE in IN $users AND out = $org
            FETCH roles
        )
    };

    RETURN fn::mutation::commit($receipt, $request_bytes, $result);
} PERMISSIONS NONE;

DEFINE FUNCTION OVERWRITE fn::organization::members::approve_requests(
    $org: record<organization>,
    $requests: array<record<request_to_join>>,
    $roles: array<record<organization_role>>,
    $receipt: record<mutation_receipt>,
    $request_bytes: array<int>
) {
    LET $previous = fn::mutation::recall($receipt, $request_bytes);

    IF $previous != NONE {
        RETURN $previous
    };

    IF array::is_empty($requests) OR array::len(array::distinct($requests)) != array::len($requests) {
        RETURN {
            outcome: 'invalid-selection-error'
        }
    };

    LET $pending = SELECT
            id,
            in.* AS user,
            out.* AS organization,
            requested_at,
            expires_at
        FROM $requests
        WHERE out = $org AND expires_at > time::now();
    LET $missing_requests = array::complement($requests, $pending.id);

    IF array::len($missing_requests) > 0 {
        RETURN {
            outcome: 'request-not-found-error',
            request_ids: $missing_requests
        }
    };

    IF array::len(array::distinct($pending.user.id)) != array::len($pending) {
        RETURN {
            outcome: 'invalid-selection-error'
        }
    };

    IF array::is_empty($roles) {
        RETURN {
            outcome: 'roles-required-error'
        }
    };

    LET $requested = SELECT *
        FROM $roles
        WHERE organization = $org;
    LET $missing = array::complement(array::distinct($roles), $requested.id);

    IF array::len($missing) > 0 {
        RETURN {
            outcome: 'roles-not-found-error',
            role_ids: $missing
        }
    };

    LET $unassignable = SELECT VALUE id
        FROM $requested
        WHERE !assignable;

    IF array::len($unassignable) > 0 {
        RETURN {
            outcome: 'roles-not-assignable-error',
            role_ids: $unassignable
        }
    };

    LET $existing = SELECT VALUE in
        FROM member_of
        WHERE in IN $pending.user.id AND out = $org;

    IF array::len($existing) > 0 {
        RETURN {
            outcome: 'user-already-member-error',
            user_ids: $existing
        }
    };

    FOR $pending_request IN $pending {
        LET $user = $pending_request.user.id;
        RELATE $user->member_of->$org SET roles = $roles;
        DELETE $pending_request.id;
    };

    LET $result = {
        outcome: 'approved',
        approvals: $pending.map(|$pending_request| {
            RETURN {
                request: $pending_request,
                member: (
                    SELECT
                        in.id AS user_id,
                        in.name AS name,
                        in.email AS email,
                        in.avatar_url AS avatar_url,
                        roles.* AS roles,
                        joined_at
                    FROM member_of
                    WHERE in = $pending_request.user.id AND out = $org
                    FETCH roles
                )[0]
            };
        })
    };

    RETURN fn::mutation::commit($receipt, $request_bytes, $result);
} PERMISSIONS NONE;
