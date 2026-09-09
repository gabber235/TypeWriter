use otel_wasi::ResultWithSlug;
use serde::Deserialize;
use std::collections::HashMap;
use wasmcloud_utils::database::organization::projections::OrganizationMemberProjection;
use wasmcloud_utils::{
    database::{RecordId, TransactionOutcome, transaction_query},
    decode_skir, extract_params,
    skir::base::organization::v1::member::*,
    skir_transaction_outcome,
    skir_utils::{IntoSkirRecordIds, IntoSurrealRecordIds},
    skir_variant,
    wasmcloud::messaging::types::BrokerMessage,
};

#[derive(Debug, Deserialize)]
struct RemovedMemberRecord {
    organization_id: RecordId,
}

#[derive(Debug, Deserialize)]
#[serde(tag = "outcome", rename_all = "kebab-case")]
enum MemberUpdateOutcome {
    Updated {
        members: Vec<OrganizationMemberProjection>,
    },
    UserNotFoundError {
        user_ids: Vec<RecordId>,
    },
    RolesNotFoundError {
        role_ids: Vec<RecordId>,
    },
    RolesNotAssignableError {
        user_ids: Vec<RecordId>,
        role_ids: Vec<RecordId>,
    },
    RolesRequiredError {
        user_ids: Vec<RecordId>,
    },
    InvalidSelectionError,
    FounderRoleRequiredError,
}

impl MemberUpdateOutcome {
    fn as_str(&self) -> &'static str {
        match self {
            Self::Updated { .. } => "updated",
            Self::UserNotFoundError { .. } => "user-not-found-error",
            Self::RolesNotFoundError { .. } => "roles-not-found-error",
            Self::RolesNotAssignableError { .. } => "roles-not-assignable-error",
            Self::RolesRequiredError { .. } => "roles-required-error",
            Self::InvalidSelectionError => "invalid-selection-error",
            Self::FounderRoleRequiredError => "founder-role-required-error",
        }
    }
}

#[tracing::instrument(skip(msg, params))]
pub async fn handle_watch(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<WatchOrganizationMembersResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string()
    );
    let _ = decode_skir!(WatchOrganizationMembersRequest, &msg.body)?;
    wasmcloud_utils::database::organization::snapshots::members(RecordId::new(
        "organization",
        org_id,
    ))
    .await
}

#[tracing::instrument(skip(msg, params))]
pub async fn handle_update(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<UpdateOrganizationMemberRolesResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    let request = decode_skir!(UpdateOrganizationMemberRolesRequest, &msg.body)?;
    wasmcloud_utils::validate_record_ids!(
        UpdateOrganizationMemberRolesResponse,
        request.user_ids,
        "user"
    );
    wasmcloud_utils::validate_record_ids!(
        UpdateOrganizationMemberRolesResponse,
        request.role_ids,
        "organization_role"
    );
    let user_ids = request.user_ids.clone();
    let role_ids = request.role_ids.clone();
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string(),
        "member.result_count" = user_ids.len() as i64,
        "role.result_count" = role_ids.len() as i64
    );
    let user_record_ids = user_ids.as_slice().into_surreal_record_ids();
    let organization_id = RecordId::new("organization", org_id);
    let receipt = wasmcloud_utils::database::mutation::receipt_id(
        actor_id,
        org_id,
        "members.update",
        &request.operation_id,
    );
    if request.operation_id.is_empty() {
        return Ok(skir_variant!(
            UpdateOrganizationMemberRolesResponse::InvalidSelectionError
        ));
    }
    let role_record_ids = role_ids.as_slice().into_surreal_record_ids();

    let result = transaction_query!(
        MemberUpdateOutcome,
        r#"
        BEGIN TRANSACTION;

        RETURN fn::organization::members::update_roles($org, $users, $roles, $receipt, $request_bytes);

        COMMIT TRANSACTION;
        "#,
    )
    .bind("users", user_record_ids)
    .bind("org", organization_id)
    .bind("roles", role_record_ids)
    .bind("receipt", receipt)
    .bind("request_bytes", msg.body.clone())
    .execute()
    .await
    .error_with_slug("member-update-query-failed")?
    .decode()
    .error_with_slug("member-update-result-parse-failed")?;

    let result = wasmcloud_utils::skir_domain_result!(UpdateOrganizationMemberRolesResponse, result,
        "operation-identity-reused-error" => {}
    );
    otel_wasi::main_attribute!("member.outcome" = result.as_str());
    let members = skir_transaction_outcome!(
        UpdateOrganizationMemberRolesResponse,
        result,
        success MemberUpdateOutcome::Updated { members } => members,
        errors {
            MemberUpdateOutcome::UserNotFoundError { user_ids } => {
                user_ids: user_ids.into_skir_record_ids()
            },
            MemberUpdateOutcome::RolesNotFoundError { role_ids } => {
                role_ids: role_ids.into_skir_record_ids()
            },
            MemberUpdateOutcome::RolesNotAssignableError { user_ids, role_ids } => {
                user_ids: user_ids.into_skir_record_ids(),
                role_ids: role_ids.into_skir_record_ids()
            },
            MemberUpdateOutcome::RolesRequiredError { user_ids } => { user_ids: user_ids.into_skir_record_ids() },
            MemberUpdateOutcome::InvalidSelectionError => {},
            MemberUpdateOutcome::FounderRoleRequiredError => {},
        }
    );

    let members: Vec<OrganizationMember> = members.into_iter().map(Into::into).collect();
    wasmcloud_utils::skir_subjects::organization_members(org_id)
        .publish(
            wasmcloud_utils::database::organization::snapshots::members(RecordId::new(
                "organization",
                org_id,
            ))
            .await?,
        )
        .await?;
    otel_wasi::main_attribute!("member.outcome" = "updated");
    Ok(UpdateOrganizationMemberRolesResponse::Success(members))
}

#[tracing::instrument(skip(msg, params))]
pub async fn handle_remove(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<RemoveOrganizationMemberResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    let request = decode_skir!(RemoveOrganizationMemberRequest, &msg.body)?;
    if request.operation_id.is_empty() {
        return Ok(wasmcloud_utils::skir_variant!(
            RemoveOrganizationMemberResponse::InvalidOperationIdError
        ));
    }
    let receipt = wasmcloud_utils::database::mutation::receipt_id(
        actor_id,
        org_id,
        "RemoveOrganizationMember",
        &request.operation_id,
    );
    wasmcloud_utils::validate_record_ids!(
        RemoveOrganizationMemberResponse,
        request.user_id,
        "user"
    );
    let user_id = request.user_id.clone();
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string(),
        "user.id" = user_id.key.to_string()
    );
    let user_record_id = RecordId::from(&user_id);
    let organization_id = RecordId::new("organization", org_id);

    let result = transaction_query!(
        RemovedMemberRecord,
        r#"
        BEGIN TRANSACTION;
        RETURN {
            LET $previous = fn::mutation::recall($receipt, $request_bytes);
            IF $previous != NONE { RETURN $previous.value };
            LET $result = {
LET $founder = $org.founder;
        IF $founder = $user {
            THROW 'founder-cannot-be-removed-error'
        };

        LET $member = SELECT * FROM member_of WHERE in = $user AND out = $org;

        IF array::len($member) = 0 {
            THROW 'user-not-member-error'
        };

        LET $is_founder = fn::organization::roles::has_named_role($member[0].roles, 'founder');
        LET $other_founders = SELECT * FROM member_of WHERE out = $org AND id != $member[0].id AND fn::organization::roles::has_named_role(roles, 'founder');

        IF $is_founder AND array::len($other_founders) = 0 {
            THROW 'founder-cannot-be-removed-error'
        };

        DELETE $member[0].id;

        RETURN { organization_id: $org };
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
    .bind("user", user_record_id)
    .bind("org", organization_id)
    .bind("receipt", receipt)
    .bind("request_bytes", msg.body.clone())
    .execute()
    .await
    .error_with_slug("member-remove-query-failed")?
    .decode()
    .error_with_slug("member-remove-result-parse-failed")?;

    if let TransactionOutcome::Rejected(error) = &result {
        otel_wasi::main_attribute!("member.outcome" = error.message().to_owned());
    }
    let deleted = wasmcloud_utils::skir_domain_result!(RemoveOrganizationMemberResponse, result,
        "operation-identity-reused-error" => {},
        "user-not-member-error" => { user_id: user_id.clone() },
        "founder-cannot-be-removed-error" => { user_id: user_id.clone() }
    );

    wasmcloud_utils::skir_subjects::organization_members(org_id)
        .publish(
            wasmcloud_utils::database::organization::snapshots::members(deleted.organization_id)
                .await?,
        )
        .await?;
    wasmcloud_utils::skir_subjects::user_organizations(user_id.key.to_string())
        .publish(
            wasmcloud_utils::database::organization::snapshots::organizations(RecordId::from(
                &user_id,
            ))
            .await?,
        )
        .await?;

    otel_wasi::main_attribute!("member.outcome" = "removed");
    Ok(skir_variant!(RemoveOrganizationMemberResponse::Success))
}
