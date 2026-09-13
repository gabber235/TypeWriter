/// Query and tree domain APIs used by search sources and consumers.
///
/// Query parsing is independent from source execution. Tree models preserve
/// source order and hierarchy while adapting nodes for the result list.
library;

export "application/core/core.dart";
export "domain/query/query.dart";
export "domain/tree/tree.dart";
