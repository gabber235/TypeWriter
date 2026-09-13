/// Tree models and diffs used to preserve hierarchical search identity.
///
/// The domain owns structural comparison. Search controllers and widgets use
/// the resulting model to update rows without taking ownership of source data.
library;

export "tree_diff.dart";
export "tree_model.dart";
