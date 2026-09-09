mod http;
mod messaging;

/// Allocates a fresh request identity; replay tests reuse their original request.
pub fn operation_id() -> String {
    static NEXT: std::sync::atomic::AtomicU64 = std::sync::atomic::AtomicU64::new(0);
    format!(
        "test.{}",
        NEXT.fetch_add(1, std::sync::atomic::Ordering::Relaxed)
    )
}
