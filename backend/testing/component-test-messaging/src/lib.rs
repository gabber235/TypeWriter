//! Synthetic messaging component used to verify host publish and request routing.
//!
//! A `test.publish` message is forwarded as `component.out`. A `test.request` message performs
//! a request on `dependency.echo`, then publishes the reply body as `component.result`. Other
//! subjects are ignored so one component can be reused by fixtures with narrow subscriptions.

wit_bindgen::generate!({
    with: {
        "wasmcloud:messaging/consumer@0.4.0": wasmcloud_utils::wasmcloud::messaging::consumer,
        "wasmcloud:messaging/handler@0.4.0": wasmcloud_utils::wasmcloud::messaging::handler,
    },
    generate_all,
});

use wasmcloud_utils::wasmcloud::messaging::{self, handler::Guest, types};

struct Component;
wasmcloud_utils::export!(Component);

impl Guest for Component {
    /// Runs the messaging scenario selected by the incoming subject.
    async fn handle_message(message: types::BrokerMessage) -> Result<(), String> {
        match message.subject.as_str() {
            "test.publish" => messaging::publish("component.out".into(), message.body)
                .await
                .map_err(|error| error.to_string()),
            // The request path keeps the original body unchanged and exposes the dependency
            // reply through a separate publish so the fixture can assert both operations.
            "test.request" => {
                let response = messaging::request("dependency.echo".into(), message.body)
                    .await
                    .map_err(|error| error.to_string())?;
                messaging::publish("component.result".into(), response.body)
                    .await
                    .map_err(|error| error.to_string())
            }
            _ => Ok(()),
        }
    }
}
