//! HTTP boundary helpers for forwarding the active W3C trace context.
//!
//! The returned header values are consumed by outbound WASI HTTP requests. An absent
//! propagation context is represented by an empty header list rather than an error.

/// Returns the active trace context as `traceparent` and optional `tracestate` headers.
///
/// The context is read from the current task. This helper does not create a span or
/// mutate the context, and returns no headers when the caller is outside a propagated
/// trace.
pub fn propagation_headers() -> Vec<(String, Vec<u8>)> {
    let Some(context) = otel_wasi::current_propagation_context() else {
        return Vec::new();
    };

    let mut headers = vec![("traceparent".to_string(), context.traceparent.into_bytes())];
    if let Some(tracestate) = context.tracestate {
        headers.push(("tracestate".to_string(), tracestate.into_bytes()));
    }
    headers
}
