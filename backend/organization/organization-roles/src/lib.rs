//! Serves organization role reads over the user scoped messaging API.
//!
//! The handler accepts subjects shaped as `typewriter.from.user.<user_id>.organization.<org_id>.roles.<action>`.
//! The `watch` module currently implements the read action. Role records are read from the
//! organization role table and encoded as the SKIR response expected by the panel and component tests.

wit_bindgen::generate!({
    with: {
        "wasmcloud:messaging/consumer@0.4.0": wasmcloud_utils::wasmcloud::messaging::consumer,
        "wasmcloud:messaging/handler@0.4.0": wasmcloud_utils::wasmcloud::messaging::handler,
    },
    generate_all,
});

mod watch;

use wasmcloud_utils::{
    dispatch_actions,
    wasmcloud::messaging::{handler::Guest, types},
};

struct Component;
wasmcloud_utils::export!(Component);

impl Guest for Component {
    #[otel_wasi::wasi_instrument(service = "organization-roles", export)]
    /// Dispatches a user scoped role message and preserves the generated messaging contract.
    async fn handle_message(msg: types::BrokerMessage) -> Result<(), otel_wasi::Error> {
        handle_message_async(msg).await
    }
}

/// Routes the action suffix after the common user and organization subject segments.
///
/// Action handlers return typed SKIR values. The dispatch macro owns subject matching and
/// serializing either the handler response or its internal error variant back to the requester.
async fn handle_message_async(msg: types::BrokerMessage) -> Result<(), otel_wasi::Error> {
    dispatch_actions!(msg, "typewriter.from.user.<user_id>.organization.<org_id>.roles.<action>",
        "watch" => async watch::handle_watch,
    )
}
