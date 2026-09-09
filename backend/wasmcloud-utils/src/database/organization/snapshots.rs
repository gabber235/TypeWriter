use otel_wasi::ResultWithSlug;

use super::{
    JoinCodeRecord, OrganizationRecord,
    projections::{JoinRequestProjection, OrganizationMemberProjection},
};
use crate::{
    database::{RecordId, read_query},
    skir::base::organization::v1::{
        join_codes::WatchOrganizationJoinCodesResponse,
        join_request::WatchOrganizationJoinRequestsResponse,
        member::WatchOrganizationMembersResponse,
        user::{WatchUserJoinRequestsResponse, WatchUserOrganizationsResponse},
    },
};

/// Reads current state for watches and publication after committed receipt recovery.
/// Stored receipt results are historical and must not be used as watch snapshots.
pub async fn members(
    organization: RecordId,
) -> Result<WatchOrganizationMembersResponse, otel_wasi::Error> {
    let rows = read_query!(
        r#"
        SELECT in.id AS user_id, in.name AS name, in.email AS email,
            in.avatar_url AS avatar_url, roles.* AS roles, joined_at
        FROM member_of WHERE out = $organization FETCH roles
    "#
    )
    .bind("organization", organization)
    .execute()
    .await
    .error_with_slug("member-snapshot-query-failed")?
    .take::<Vec<OrganizationMemberProjection>>()
    .error_with_slug("member-snapshot-parse-failed")?;
    Ok(WatchOrganizationMembersResponse::List(
        rows.into_iter().map(Into::into).collect(),
    ))
}

pub async fn organizations(
    user: RecordId,
) -> Result<WatchUserOrganizationsResponse, otel_wasi::Error> {
    let rows = read_query!("SELECT VALUE ->member_of->organization.* FROM ONLY $user")
        .bind("user", user)
        .execute()
        .await
        .error_with_slug("organization-snapshot-query-failed")?
        .take::<Vec<OrganizationRecord>>()
        .error_with_slug("organization-snapshot-parse-failed")?;
    Ok(WatchUserOrganizationsResponse::List(
        rows.into_iter().map(Into::into).collect(),
    ))
}

pub async fn join_requests(
    organization: RecordId,
) -> Result<WatchOrganizationJoinRequestsResponse, otel_wasi::Error> {
    let rows = read_query!(
        r#"
        SELECT id, in.* AS user, out.* AS organization, requested_at, expires_at
        FROM request_to_join WHERE out = $organization AND expires_at > time::now()
    "#
    )
    .bind("organization", organization)
    .execute()
    .await
    .error_with_slug("organization-join-request-snapshot-query-failed")?
    .take::<Vec<JoinRequestProjection>>()
    .error_with_slug("organization-join-request-snapshot-parse-failed")?;
    Ok(WatchOrganizationJoinRequestsResponse::List(
        rows.into_iter().map(Into::into).collect(),
    ))
}

pub async fn user_join_requests(
    user: RecordId,
) -> Result<WatchUserJoinRequestsResponse, otel_wasi::Error> {
    let rows = read_query!(
        r#"
        SELECT id, in.* AS user, out.* AS organization, requested_at, expires_at
        FROM request_to_join WHERE in = $user AND expires_at > time::now()
    "#
    )
    .bind("user", user)
    .execute()
    .await
    .error_with_slug("user-join-request-snapshot-query-failed")?
    .take::<Vec<JoinRequestProjection>>()
    .error_with_slug("user-join-request-snapshot-parse-failed")?;
    Ok(WatchUserJoinRequestsResponse::List(
        rows.into_iter().map(Into::into).collect(),
    ))
}

pub async fn join_codes(
    organization: RecordId,
) -> Result<WatchOrganizationJoinCodesResponse, otel_wasi::Error> {
    let rows = read_query!(
        r#"
        SELECT id, created_at, expires_at, single_use, auto_accept_roles
        FROM organization_join_code WHERE organization = $organization
            AND (expires_at IS NONE OR expires_at IS NULL OR expires_at > time::now())
        ORDER BY created_at DESC
    "#
    )
    .bind("organization", organization)
    .execute()
    .await
    .error_with_slug("join-code-snapshot-query-failed")?
    .take::<Vec<JoinCodeRecord>>()
    .error_with_slug("join-code-snapshot-parse-failed")?;
    Ok(WatchOrganizationJoinCodesResponse::List(
        rows.into_iter().map(Into::into).collect(),
    ))
}
