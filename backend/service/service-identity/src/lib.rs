//! Issues the durable service identity used to begin service onboarding.
//!
//! The HTTP adapter accepts and returns the Skir contract. The domain workflow owns role
//! validation, generated names, account provisioning, service persistence, and response
//! mapping. The account provider owns the external account system, while the repository
//! owns database validation and the service record transaction. Organization binding and
//! service liveness belong to later workflows and are outside this service.
//!
//! Identity issuance crosses two independent systems. The repository transaction is
//! atomic within the database, but account provisioning and database creation cannot be
//! one atomic operation. If persistence fails after provisioning, the workflow attempts
//! deletion through the provider. That attempt is compensation, not a guarantee that the
//! external account no longer exists.

mod bindings {
    use crate::Component;
    wit_bindgen::generate!({ world: "component", path: "wit", generate_all });
    export!(Component);
}

mod authentik;
mod http;
mod identity;
mod names;
mod repository;

use bindings::exports::wasi::http::handler::Guest;
use bindings::wasi::http::types::{ErrorCode, Request, Response};

struct Component;

impl Guest for Component {
    #[otel_wasi::wasi_instrument(
        service = "service-identity",
        export,
        attributes(
            "http.route" = "/service/identity/issue",
            "http.method" = "POST",
        )
    )]
    async fn handle(request: Request) -> Result<Response, otel_wasi::Error<ErrorCode>> {
        http::handle(request).await
    }
}
