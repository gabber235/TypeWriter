//! Synthetic messaging dependency used by the composed component fixture.
//!
//! It replies only to `dependency.echo`, returning the request body unchanged. The narrow subject
//! check proves that request delivery reaches the configured dependency rather than a general
//! catch all handler.

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
    /// Echoes dependency requests through the broker reply route.
    async fn handle_message(message: types::BrokerMessage) -> Result<(), String> {
        // Ignore unrelated subjects so the dependency remains safe to compose with other routes.
        if message.subject == "dependency.echo" {
            messaging::reply(message.clone(), message.body)
                .await
                .map_err(|error| error.to_string())?;
        }
        Ok(())
    }
}
