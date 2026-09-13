use std::collections::HashMap;

use otel_wasi::ResultWithSlug;
use wasmcloud_utils::{
    database::{RecordId, transaction_query},
    decode_skir, extract_params,
    skir::base::service::v1::registration::{
        UnbindServiceRequest, UnbindServiceResponse, UnbindServiceResponse_InvalidOperationIdError,
        UnbindServiceResponse_OperationIdentityReusedError,
        UnbindServiceResponse_ServiceNotFoundError, UnbindServiceResponse_Success,
    },
    skir_variant,
    wasmcloud::messaging::types::BrokerMessage,
};

#[tracing::instrument(skip(msg, params))]
pub async fn handle_unbind(
    msg: BrokerMessage,
    params: HashMap<String, String>,
) -> Result<UnbindServiceResponse, otel_wasi::Error> {
    let (actor_id, org_id) = extract_params!(params, user_id, org_id)?;
    let request = decode_skir!(UnbindServiceRequest, &msg.body)?;
    if request.operation_id.is_empty() {
        return Ok(wasmcloud_utils::skir_variant!(
            UnbindServiceResponse::InvalidOperationIdError
        ));
    }
    let receipt = wasmcloud_utils::database::mutation::receipt_id(
        actor_id,
        org_id,
        "UnbindService",
        &request.operation_id,
    );
    otel_wasi::main_attribute!(
        "actor.id" = actor_id.to_string(),
        "organization.id" = org_id.to_string(),
        "service.id" = request.service_id.clone()
    );
    let service_id = RecordId::new("service", request.service_id.as_str());
    let organization_id = RecordId::new("organization", org_id);

    let result = transaction_query!(
        Option<bool>,
        r#"
        BEGIN TRANSACTION;
        RETURN {
            LET $previous = fn::mutation::recall($receipt, $request_bytes);
            IF $previous != NONE { RETURN $previous.value };
            LET $result = {
RETURN {
            LET $services = SELECT * FROM $service_id
                WHERE organization = $organization_id;
            IF array::is_empty($services) {
                RETURN NONE
            };
            UPDATE ONLY $service_id SET
                organization = NONE,
                registration = NONE;
            RETURN true;
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
    .bind("service_id", service_id)
    .bind("organization_id", organization_id)
    .bind("receipt", receipt)
    .bind("request_bytes", msg.body.clone())
    .execute()
    .await
    .error_with_slug("service-unbind-query-failed")?
    .decode()
    .error_with_slug("service-unbind-result-parse-failed")?;
    let result = wasmcloud_utils::skir_domain_result!(UnbindServiceResponse, result,
        "operation-identity-reused-error" => {});

    let Some(true) = result else {
        otel_wasi::main_attribute!("service.outcome" = "not_found");
        return Ok(skir_variant!(UnbindServiceResponse::ServiceNotFoundError));
    };

    wasmcloud_utils::skir_subjects::organization_topology(org_id)
        .publish(crate::watch_topology::snapshot(org_id).await?)
        .await?;
    wasmcloud_utils::skir_subjects::organization_services(org_id)
        .publish(crate::watch::snapshot(org_id).await?)
        .await?;

    otel_wasi::main_attribute!("service.outcome" = "unbound");
    Ok(skir_variant!(UnbindServiceResponse::Success))
}
