use std::collections::HashMap;

use otel_wasi::ResultWithSlug;
use serde::Deserialize;
use wasmcloud_utils::{
    database::{RecordId, read_query, transaction_query},
    decode_skir, extract_params,
    skir::base::service::v1::registration::{
        BindServiceRequest, BindServiceResponse, BindServiceResponse_InvalidOperationIdError,
        BindServiceResponse_OperationIdentityReusedError, BindServiceResponse_Success,
        ServiceBoundNotification,
    },
    skir_domain_result, skir_variant,
    wasmcloud::messaging::types::BrokerMessage,
};

use wasmcloud_utils::database::{organization::OrganizationRecord, service::ServiceRecord};

#[derive(Debug, Deserialize)]
struct BindResult {
    service: ServiceRecord,
}

#[tracing::instrument(skip(msg, params))]
pub async fn handle_bind(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<BindServiceResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string()
    );
    let request = decode_skir!(BindServiceRequest, &msg.body)?;
    if request.operation_id.is_empty() {
        return Ok(wasmcloud_utils::skir_variant!(
            BindServiceResponse::InvalidOperationIdError
        ));
    }
    let receipt = wasmcloud_utils::database::mutation::receipt_id(
        actor_id,
        org_id,
        "BindService",
        &request.operation_id,
    );
    let organization_id = RecordId::new("organization", org_id);

    let response = transaction_query!(
        BindResult,
        r#"
        BEGIN TRANSACTION;
        RETURN {
            LET $previous = fn::mutation::recall($receipt, $request_bytes);
            IF $previous != NONE { RETURN $previous.value };
            LET $result = {
LET $services = SELECT * FROM service
            WHERE registration.token = $registration_token
                AND registration.expires_at > time::now();

        IF array::is_empty($services) {
            THROW 'invalid-registration-token-error'
        };

        LET $organizations = SELECT id, name FROM $organization_id;
        IF array::is_empty($organizations) {
            THROW 'organization-not-found-error'
        };

        LET $organization = array::first($organizations);
        LET $updated = UPDATE ONLY $services[0].id SET
            organization = $organization.id,
            registration = NONE
        RETURN AFTER;

        RETURN {
            service: $updated,
        };
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
    .bind("registration_token", request.registration_token)
    .bind("organization_id", organization_id)
    .bind("receipt", receipt)
    .bind("request_bytes", msg.body.clone())
    .execute()
    .await
    .error_with_slug("service-bind-query-failed")?;
    otel_wasi::main_attribute!(
        "db.query.attempts" = response.attempts().get() as i64,
        "db.query.retries" = response.attempts().get().saturating_sub(1) as i64,
    );
    let result = response
        .decode()
        .error_with_slug("service-bind-result-parse-failed")?;
    let result = skir_domain_result!(BindServiceResponse, result,
        "operation-identity-reused-error" => {});

    let service_id = result.service.id.key.to_string();
    let service_name = result.service.name.clone();
    let role = result.service.role.clone().try_into()?;

    wasmcloud_utils::skir_subjects::organization_services(org_id)
        .publish(crate::watch::snapshot(org_id).await?)
        .await?;

    let organization = read_query!("SELECT VALUE organization.* FROM ONLY $service")
        .bind("service", result.service.id)
        .execute()
        .await
        .error_with_slug("service-binding-snapshot-query-failed")?
        .parse::<Option<OrganizationRecord>>()
        .error_with_slug("service-binding-snapshot-parse-failed")?;
    if let Some(organization) = organization {
        wasmcloud_utils::skir_subjects::service_bound(&service_id)
            .publish(ServiceBoundNotification {
                organization_id: organization.id.key.to_string(),
                organization_name: Some(organization.name),
                _unrecognized: None,
            })
            .await?;
    }

    otel_wasi::main_attribute!(
        "service.id" = service_id.clone(),
        "service.outcome" = "bound"
    );
    Ok(skir_variant!(BindServiceResponse::Success {
        service_id,
        service_name: Some(service_name),
        service_role: role,
    }))
}
