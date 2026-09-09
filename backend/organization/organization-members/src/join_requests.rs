use otel_wasi::ResultWithSlug;
use serde::{Deserialize, Serialize};
use std::collections::HashMap;
use wasmcloud_utils::database::organization::projections::{
    JoinRequestProjection, OrganizationMemberProjection,
};
use wasmcloud_utils::{
    database::{RecordId, TransactionOutcome, transaction_query},
    decode_skir, extract_params,
    skir::base::organization::v1::{join_request::*, member::OrganizationMember},
    skir_transaction_outcome,
    skir_utils::{IntoSkirRecordIds, IntoSurrealRecordIds},
    skir_variant,
    wasmcloud::messaging::types::BrokerMessage,
};

#[derive(Debug, Serialize, Deserialize)]
struct ApprovalRecord {
    request: JoinRequestProjection,
    member: OrganizationMemberProjection,
}

#[derive(Debug, Deserialize)]
#[serde(tag = "outcome", rename_all = "kebab-case")]
enum ApprovalOutcome {
    Approved { approvals: Vec<ApprovalRecord> },
    RequestNotFoundError { request_ids: Vec<RecordId> },
    InvalidSelectionError,
    RolesRequiredError,
    RolesNotFoundError { role_ids: Vec<RecordId> },
    RolesNotAssignableError { role_ids: Vec<RecordId> },
    UserAlreadyMemberError { user_ids: Vec<RecordId> },
}

impl ApprovalOutcome {
    fn as_str(&self) -> &'static str {
        match self {
            Self::Approved { .. } => "approved",
            Self::RequestNotFoundError { .. } => "request-not-found-error",
            Self::InvalidSelectionError => "invalid-selection-error",
            Self::RolesRequiredError => "roles-required-error",
            Self::RolesNotFoundError { .. } => "roles-not-found-error",
            Self::RolesNotAssignableError { .. } => "roles-not-assignable-error",
            Self::UserAlreadyMemberError { .. } => "user-already-member-error",
        }
    }
}

#[tracing::instrument(skip(msg, params))]
pub async fn handle_watch(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<WatchOrganizationJoinRequestsResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string()
    );
    let _ = decode_skir!(WatchOrganizationJoinRequestsRequest, &msg.body)?;

    wasmcloud_utils::database::organization::snapshots::join_requests(RecordId::new(
        "organization",
        org_id,
    ))
    .await
}

#[tracing::instrument(skip(msg, params))]
pub async fn handle_approve(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<ApproveOrganizationJoinRequestsResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    let req = decode_skir!(ApproveOrganizationJoinRequestsRequest, &msg.body)?;
    wasmcloud_utils::validate_record_ids!(
        ApproveOrganizationJoinRequestsResponse,
        req.request_ids,
        "request_to_join"
    );
    wasmcloud_utils::validate_record_ids!(
        ApproveOrganizationJoinRequestsResponse,
        req.role_ids,
        "organization_role"
    );
    let request_ids = req.request_ids.clone();
    let role_ids = req.role_ids.clone();
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string(),
        "request.result_count" = request_ids.len() as i64,
        "role.result_count" = role_ids.len() as i64
    );
    let request_record_ids = request_ids.as_slice().into_surreal_record_ids();
    let organization_id = RecordId::new("organization", org_id);
    let receipt = wasmcloud_utils::database::mutation::receipt_id(
        actor_id,
        org_id,
        "members.join_requests.approve",
        &req.operation_id,
    );
    if req.operation_id.is_empty() {
        return Ok(skir_variant!(
            ApproveOrganizationJoinRequestsResponse::InvalidSelectionError
        ));
    }

    let db_role_ids = role_ids.as_slice().into_surreal_record_ids();
    let result = transaction_query!(
        ApprovalOutcome,
        r#"
        BEGIN TRANSACTION;

        RETURN fn::organization::members::approve_requests($org, $requests, $roles, $receipt, $request_bytes);

        COMMIT TRANSACTION;
        "#,
    )
    .bind("requests", request_record_ids)
    .bind("org", organization_id)
    .bind("roles", db_role_ids)
    .bind("receipt", receipt)
    .bind("request_bytes", msg.body.clone())
    .execute()
    .await
    .error_with_slug("join-request-approve-query-failed")?
    .decode()
    .error_with_slug("join-request-approve-result-parse-failed")?;

    let result = wasmcloud_utils::skir_domain_result!(ApproveOrganizationJoinRequestsResponse, result,
        "operation-identity-reused-error" => {}
    );
    otel_wasi::main_attribute!("join_request.outcome" = result.as_str());
    let approvals = skir_transaction_outcome!(
        ApproveOrganizationJoinRequestsResponse,
        result,
        success ApprovalOutcome::Approved { approvals } => approvals,
        errors {
            ApprovalOutcome::RequestNotFoundError { request_ids } => {
                request_ids: request_ids.into_skir_record_ids()
            },
            ApprovalOutcome::RolesRequiredError => {},
            ApprovalOutcome::InvalidSelectionError => {},
            ApprovalOutcome::RolesNotFoundError { role_ids } => {
                role_ids: role_ids.into_skir_record_ids()
            },
            ApprovalOutcome::RolesNotAssignableError { role_ids } => {
                role_ids: role_ids.into_skir_record_ids()
            },
            ApprovalOutcome::UserAlreadyMemberError { user_ids } => {
                user_ids: user_ids.into_skir_record_ids()
            },
        }
    );

    wasmcloud_utils::skir_subjects::organization_join_requests(org_id)
        .publish(
            wasmcloud_utils::database::organization::snapshots::join_requests(RecordId::new(
                "organization",
                org_id,
            ))
            .await?,
        )
        .await?;
    wasmcloud_utils::skir_subjects::organization_members(org_id)
        .publish(
            wasmcloud_utils::database::organization::snapshots::members(RecordId::new(
                "organization",
                org_id,
            ))
            .await?,
        )
        .await?;
    let mut results = Vec::with_capacity(approvals.len());
    for approved in approvals {
        let request_id: wasmcloud_utils::skir::base::kernel::v1::record_id::RecordId =
            approved.request.id.clone().into();
        let member: OrganizationMember = approved.member.into();

        let user_id = approved.request.user.id.key.to_string();

        wasmcloud_utils::skir_subjects::user_join_requests(&user_id)
            .publish(
                wasmcloud_utils::database::organization::snapshots::user_join_requests(
                    approved.request.user.id.clone(),
                )
                .await?,
            )
            .await?;
        wasmcloud_utils::skir_subjects::user_organizations(&user_id)
            .publish(
                wasmcloud_utils::database::organization::snapshots::organizations(
                    approved.request.user.id,
                )
                .await?,
            )
            .await?;

        results.push(ApprovedOrganizationJoinRequest {
            request_id,
            member,
            ..Default::default()
        });
    }
    otel_wasi::main_attribute!("join_request.outcome" = "approved");
    Ok(ApproveOrganizationJoinRequestsResponse::Success(results))
}

#[tracing::instrument(skip(msg, params))]
pub async fn handle_decline(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<DeclineOrganizationJoinRequestResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    let req = decode_skir!(DeclineOrganizationJoinRequestRequest, &msg.body)?;
    if req.operation_id.is_empty() {
        return Ok(wasmcloud_utils::skir_variant!(
            DeclineOrganizationJoinRequestResponse::InvalidOperationIdError
        ));
    }
    let receipt = wasmcloud_utils::database::mutation::receipt_id(
        actor_id,
        org_id,
        "DeclineOrganizationJoinRequest",
        &req.operation_id,
    );
    wasmcloud_utils::validate_record_ids!(
        DeclineOrganizationJoinRequestResponse,
        req.request_id,
        "request_to_join"
    );
    let request_id = req.request_id.clone();
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string(),
        "request.id" = request_id.key.to_string()
    );
    let request_record_id = RecordId::from(&request_id);
    let organization_id = RecordId::new("organization", org_id);

    let row = transaction_query!(
        Option<JoinRequestProjection>,
        r#"
        BEGIN TRANSACTION;
        RETURN {
            LET $previous = fn::mutation::recall($receipt, $request_bytes);
            IF $previous != NONE { RETURN $previous.value };
            LET $result = {
LET $r = SELECT
            id,
            in.* AS user,
            out.* AS organization,
            requested_at,
            expires_at
        FROM $request
        WHERE out = $org
            AND expires_at > time::now();

        DELETE $r.id;

        RETURN $r[0];
            };
            IF $result != NONE {
                LET $stored = fn::mutation::commit($receipt, $request_bytes, { value: $result });
                RETURN $stored.value;
            };
            RETURN $result;
        };
        COMMIT TRANSACTION;
        "#,
    )
    .bind("request", request_record_id)
    .bind("org", organization_id)
    .bind("receipt", receipt)
    .bind("request_bytes", msg.body.clone())
    .execute()
    .await
    .error_with_slug("join-request-decline-query-failed")?
    .decode()
    .error_with_slug("join-request-decline-result-parse-failed")?;

    if let TransactionOutcome::Rejected(error) = &row {
        otel_wasi::main_attribute!("join_request.outcome" = error.message().to_owned());
    }

    let row = wasmcloud_utils::skir_domain_result!(DeclineOrganizationJoinRequestResponse, row,
        "operation-identity-reused-error" => {},
        "request-not-found-error" => { request_id: request_id.clone() }
    );

    let Some(row) = row else {
        otel_wasi::main_attribute!("join_request.outcome" = "request_not_found");
        return Ok(skir_variant!(
            DeclineOrganizationJoinRequestResponse::RequestNotFoundError { request_id }
        ));
    };

    wasmcloud_utils::skir_subjects::organization_join_requests(org_id)
        .publish(
            wasmcloud_utils::database::organization::snapshots::join_requests(RecordId::new(
                "organization",
                org_id,
            ))
            .await?,
        )
        .await?;
    wasmcloud_utils::skir_subjects::user_join_requests(row.user.id.key.to_string())
        .publish(
            wasmcloud_utils::database::organization::snapshots::user_join_requests(row.user.id)
                .await?,
        )
        .await?;

    otel_wasi::main_attribute!("join_request.outcome" = "declined");
    Ok(skir_variant!(
        DeclineOrganizationJoinRequestResponse::Success
    ))
}
