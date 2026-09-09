use std::collections::HashMap;

use otel_wasi::ResultWithSlug;
use wasmcloud_utils::{
    database::{RecordId, transaction_query},
    decode_skir, extract_param,
    skir::base::organization::v1::organization::*,
    skir_domain_result,
    wasmcloud::messaging::types::BrokerMessage,
};

use wasmcloud_utils::database::organization::OrganizationRecord;

#[tracing::instrument(skip(msg, params))]
pub async fn handle_create(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<CreateOrganizationResponse, otel_wasi::Error> {
    let user_id = extract_param!(params, user_id)?;
    otel_wasi::main_attribute!("user.id" = user_id.to_string());
    let user_key = user_id;
    let user_id = RecordId::new("user", user_id);
    let request = decode_skir!(CreateOrganizationRequest, &msg.body)?;
    if request.operation_id.is_empty() {
        return Ok(wasmcloud_utils::skir_variant!(
            CreateOrganizationResponse::InvalidOperationIdError
        ));
    }
    let receipt = wasmcloud_utils::database::mutation::receipt_id(
        user_key,
        "organizations",
        "CreateOrganization",
        &request.operation_id,
    );

    let name = request.name;
    let logo_url = request.logo_url;

    let organization = transaction_query!(
        OrganizationRecord,
        r#"
        BEGIN TRANSACTION;
        RETURN {
            LET $previous = fn::mutation::recall($receipt, $request_bytes);
            IF $previous != NONE { RETURN $previous.value };
            LET $result = {
LET $organization = CREATE ONLY organization SET
            name = $name,
            logo_url = $logo_url,
            founder = $user_id
            ;

        RETURN $organization;
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
    .bind("name", &name)
    .bind("logo_url", &logo_url)
    .bind("user_id", user_id)
    .bind("receipt", receipt)
    .bind("request_bytes", msg.body.clone())
    .execute()
    .await
    .error_with_slug("organization-create-query-failed")?
    .decode()
    .error_with_slug("organization-create-result-parse-failed")?;
    let organization: Organization = skir_domain_result!(CreateOrganizationResponse, organization,
        "operation-identity-reused-error" => {})
    .into();

    otel_wasi::main_attribute!("organization.id" = organization.organization_id.to_string());
    wasmcloud_utils::skir_subjects::user_organizations(user_key)
        .publish(
            wasmcloud_utils::database::organization::snapshots::organizations(RecordId::new(
                "user", user_key,
            ))
            .await?,
        )
        .await?;

    otel_wasi::main_attribute!("organization.outcome" = "created");
    Ok(CreateOrganizationResponse::Success(organization.into()))
}
