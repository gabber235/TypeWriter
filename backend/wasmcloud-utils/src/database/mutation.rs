use super::RecordId;

/// Identifies a committed mutation within its authenticated actor and domain scope.
/// Bind the returned record and original request bytes inside the effect transaction.
/// The receipt must be created with the effects, never in a subsequent query.
pub fn receipt_id(actor: &str, scope: &str, operation: &str, identity: &str) -> RecordId {
    let key = serde_json::to_string(&(actor, scope, operation, identity))
        .expect("serializing a tuple of strings cannot fail");
    RecordId::new("mutation_receipt", key)
}
