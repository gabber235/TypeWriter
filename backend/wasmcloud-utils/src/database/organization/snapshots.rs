use otel_wasi::ResultWithSlug;
use serde::Deserialize;

use super::{
    projections::{JoinRequestProjection, OrganizationMemberProjection},
    JoinCodeRecord, OrganizationRecord,
};
use crate::{
    database::{read_query, RecordId},
    skir::base::organization::v1::{
        join_codes::{OrganizationJoinCodesSnapshot, WatchOrganizationJoinCodesResponse},
        join_request::{
            OrganizationJoinRequestsSnapshot, UserJoinRequestsSnapshot,
            WatchOrganizationJoinRequestsResponse,
        },
        member::{OrganizationMembersSnapshot, WatchOrganizationMembersResponse},
        organization::UserOrganizationsSnapshot,
        user::{WatchUserJoinRequestsResponse, WatchUserOrganizationsResponse},
    },
};

#[derive(Deserialize)]
struct SnapshotRow<T> {
    sequence: i64,
    values: Vec<T>,
}

/// Reads current state for watches and publication after committed receipt recovery.
/// Stored receipt results are historical and must not be used as watch snapshots.
pub async fn members(
    organization: RecordId,
) -> Result<WatchOrganizationMembersResponse, otel_wasi::Error> {
    let snapshot = read_query!(
        r#"
        RETURN {
            sequence: (SELECT VALUE members_sequence FROM ONLY $organization),
            values: (SELECT in.id AS user_id, in.name AS name, in.email AS email,
                in.avatar_url AS avatar_url, roles.* AS roles, joined_at
                FROM member_of WHERE out = $organization FETCH roles)
        }
    "#
    )
    .bind("organization", organization)
    .execute()
    .await
    .error_with_slug("member-snapshot-query-failed")?
    .parse::<SnapshotRow<OrganizationMemberProjection>>()
    .error_with_slug("member-snapshot-parse-failed")?;
    Ok(WatchOrganizationMembersResponse::Snapshot(Box::new(
        OrganizationMembersSnapshot {
            sequence: snapshot.sequence,
            values: snapshot.values.into_iter().map(Into::into).collect(),
            ..Default::default()
        },
    )))
}

pub async fn organizations(
    user: RecordId,
) -> Result<WatchUserOrganizationsResponse, otel_wasi::Error> {
    let snapshot = read_query!(
        "RETURN {
            sequence: (SELECT VALUE organizations_sequence FROM ONLY $user),
            values: (SELECT VALUE ->member_of->organization.* FROM ONLY $user)
        }"
    )
    .bind("user", user)
    .execute()
    .await
    .error_with_slug("organization-snapshot-query-failed")?
    .parse::<SnapshotRow<OrganizationRecord>>()
    .error_with_slug("organization-snapshot-parse-failed")?;
    Ok(WatchUserOrganizationsResponse::Snapshot(Box::new(
        UserOrganizationsSnapshot {
            sequence: snapshot.sequence,
            values: snapshot.values.into_iter().map(Into::into).collect(),
            ..Default::default()
        },
    )))
}

pub async fn join_requests(
    organization: RecordId,
) -> Result<WatchOrganizationJoinRequestsResponse, otel_wasi::Error> {
    let snapshot = read_query!(
        r#"
        RETURN {
            sequence: (SELECT VALUE join_requests_sequence FROM ONLY $organization),
            values: (SELECT id, in.* AS user, out.* AS organization, requested_at, expires_at
                FROM request_to_join WHERE out = $organization AND expires_at > time::now())
        }
    "#
    )
    .bind("organization", organization)
    .execute()
    .await
    .error_with_slug("organization-join-request-snapshot-query-failed")?
    .parse::<SnapshotRow<JoinRequestProjection>>()
    .error_with_slug("organization-join-request-snapshot-parse-failed")?;
    Ok(WatchOrganizationJoinRequestsResponse::Snapshot(Box::new(
        OrganizationJoinRequestsSnapshot {
            sequence: snapshot.sequence,
            values: snapshot.values.into_iter().map(Into::into).collect(),
            ..Default::default()
        },
    )))
}

pub async fn user_join_requests(
    user: RecordId,
) -> Result<WatchUserJoinRequestsResponse, otel_wasi::Error> {
    let snapshot = read_query!(
        r#"
        RETURN {
            sequence: (SELECT VALUE join_requests_sequence FROM ONLY $user),
            values: (SELECT id, in.* AS user, out.* AS organization, requested_at, expires_at
                FROM request_to_join WHERE in = $user AND expires_at > time::now())
        }
    "#
    )
    .bind("user", user)
    .execute()
    .await
    .error_with_slug("user-join-request-snapshot-query-failed")?
    .parse::<SnapshotRow<JoinRequestProjection>>()
    .error_with_slug("user-join-request-snapshot-parse-failed")?;
    Ok(WatchUserJoinRequestsResponse::Snapshot(Box::new(
        UserJoinRequestsSnapshot {
            sequence: snapshot.sequence,
            values: snapshot.values.into_iter().map(Into::into).collect(),
            ..Default::default()
        },
    )))
}

pub async fn join_codes(
    organization: RecordId,
) -> Result<WatchOrganizationJoinCodesResponse, otel_wasi::Error> {
    let snapshot = read_query!(
        r#"
        RETURN {
            sequence: (SELECT VALUE join_codes_sequence FROM ONLY $organization),
            values: (SELECT id, created_at, expires_at, single_use, auto_accept_roles
                FROM organization_join_code WHERE organization = $organization
                    AND (expires_at IS NONE OR expires_at IS NULL OR expires_at > time::now())
                ORDER BY created_at DESC)
        }
    "#
    )
    .bind("organization", organization)
    .execute()
    .await
    .error_with_slug("join-code-snapshot-query-failed")?
    .parse::<SnapshotRow<JoinCodeRecord>>()
    .error_with_slug("join-code-snapshot-parse-failed")?;
    Ok(WatchOrganizationJoinCodesResponse::Snapshot(Box::new(
        OrganizationJoinCodesSnapshot {
            sequence: snapshot.sequence,
            values: snapshot.values.into_iter().map(Into::into).collect(),
            ..Default::default()
        },
    )))
}
