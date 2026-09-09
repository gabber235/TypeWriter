use wasmcloud_utils::skir::base::{
    kernel::v1::record_id::{RecordId, RecordIdKey},
    organization::v1::member::*,
};
use wasmcloud_utils::skir_transaction_outcome;

#[allow(
    clippy::large_enum_variant,
    reason = "The test mirrors transaction outcome shapes used by generated response types"
)]
enum MemberUpdateOutcome {
    Updated { member: OrganizationMember },
    UserNotFoundError,
    RolesNotFoundError { role_ids: Vec<RecordId> },
    FounderRoleRequiredError,
}

#[test]
fn returns_success_value() {
    let member = OrganizationMember::default();
    let response = map_outcome(
        MemberUpdateOutcome::Updated {
            member: member.clone(),
        },
        record_id("user", "member"),
    )
    .expect("success outcome should map");

    assert_eq!(
        response,
        UpdateOrganizationMemberRolesResponse::Success(vec![member])
    );
}

#[test]
fn maps_database_payload_to_error_variant() {
    let missing = record_id("organization_role", "missing");
    let response = map_outcome(
        MemberUpdateOutcome::RolesNotFoundError {
            role_ids: vec![missing.clone()],
        },
        record_id("user", "member"),
    )
    .expect("payloadful error outcome should map");

    let UpdateOrganizationMemberRolesResponse::RolesNotFoundError(error) = response else {
        panic!("expected roles not found response")
    };
    assert_eq!(error.role_ids, [missing]);
}

#[test]
fn maps_request_payload_to_error_variant() {
    let user_id = record_id("user", "missing");
    let response = map_outcome(MemberUpdateOutcome::UserNotFoundError, user_id.clone())
        .expect("request payload error outcome should map");

    let UpdateOrganizationMemberRolesResponse::UserNotFoundError(error) = response else {
        panic!("expected user not found response")
    };
    assert_eq!(error.user_ids, [user_id]);
}

#[test]
fn maps_payloadless_error_variant() {
    let response = map_outcome(
        MemberUpdateOutcome::FounderRoleRequiredError,
        record_id("user", "member"),
    )
    .expect("payloadless error outcome should map");

    assert!(matches!(
        response,
        UpdateOrganizationMemberRolesResponse::FounderRoleRequiredError(_)
    ));
}

fn map_outcome(
    outcome: MemberUpdateOutcome,
    user_id: RecordId,
) -> Result<UpdateOrganizationMemberRolesResponse, otel_wasi::Error> {
    let member = skir_transaction_outcome!(
        UpdateOrganizationMemberRolesResponse,
        outcome,
        success MemberUpdateOutcome::Updated { member } => member,
        errors {
            MemberUpdateOutcome::UserNotFoundError => {
                user_ids: vec![user_id]
            },
            MemberUpdateOutcome::RolesNotFoundError { role_ids } => {
                role_ids
            },
            MemberUpdateOutcome::FounderRoleRequiredError => {},
        }
    );

    Ok(UpdateOrganizationMemberRolesResponse::Success(vec![member]))
}

fn record_id(table: &str, key: &str) -> RecordId {
    RecordId {
        table: table.to_owned(),
        key: RecordIdKey::String(key.to_owned()),
        _unrecognized: None,
    }
}
