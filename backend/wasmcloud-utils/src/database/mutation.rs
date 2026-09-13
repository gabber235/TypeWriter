//! Stable identities for mutation receipts.
//!
//! A receipt belongs to the authenticated actor, operation scope, operation name, and caller
//! supplied identity. The surrounding transaction owns creating or recalling it and storing the
//! original request bytes with the result.

use super::RecordId;

/// Builds the stable record identity used to recall an idempotent mutation result.
///
/// Callers must bind this identity and the original request bytes to the effect transaction.
/// The receipt is only useful for idempotency when the transaction stores it with the mutation
/// result. Reusing the identity with different request bytes is a rejected operation, not a new
/// mutation.
pub fn receipt_id(actor: &str, scope: &str, operation: &str, identity: &str) -> RecordId {
    let key = serde_json::to_string(&(actor, scope, operation, identity))
        .expect("serializing a tuple of strings cannot fail");
    RecordId::new("mutation_receipt", key)
}
