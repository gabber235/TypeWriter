/// Shared identity, selection state, operation registry, and selectable UI.
///
/// Selection stores identifiers as canonical state. Resolved selectable objects,
/// operation availability, and focus presentation are derived at their owning
/// boundaries so selection and focus remain distinct.
library;

export "application/operations.dart";
export "application/selection.dart";
export "domain/selectable.dart";
export "operations/delete_operation.dart";
export "operations/open_operation.dart";
export "operations/unbind_operation.dart";
export "presentation/operation_button.dart";
export "presentation/selection_operation_shortcuts.dart";
export "presentation/selection_operations_root.dart";
export "presentation/selector.dart";
