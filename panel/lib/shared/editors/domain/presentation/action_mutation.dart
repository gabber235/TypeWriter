import "package:typewriter_panel/typewriter_panel.dart";

/// Applies one validated replacement through the binding environment.
///
/// The path is checked against the declared type before replacement. The
/// returned value is the updated root, because the editor host owns and saves
/// roots rather than child projections.

extension BindingReferenceMutation on BindingReference {
  LocalMutationResult replaceValue(
    TypeExpression type,
    DataValue value,
    ExpressionContext context,
    TypeRegistry? registry,
  ) {
    final diagnostics = value.validateAgainst(type, registry: registry);
    if (diagnostics.isNotEmpty) return LocalMutationInvalid(diagnostics);
    final replaced = context.bindings.replace(this, value);
    if (replaced case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }

    final binding = replaced.valueOrNull!.resolve(
      BindingReference(bindingId: bindingId),
      registry: registry,
    );
    if (binding case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    return LocalMutationApplied(
      bindingId: bindingId,
      value: binding.valueOrNull!.value,
    );
  }
}

/// Creates a local failure for an action that cannot be applied to its target.
LocalMutationInvalid invalidLocalMutation(String message) =>
    LocalMutationInvalid([
      TypeDiagnostic(code: TypeDiagnosticCode.invalidValue, message: message),
    ]);

MutationInvalid invalidMutation(String message) => MutationInvalid([
  TypeDiagnostic(code: TypeDiagnosticCode.invalidValue, message: message),
]);

/// Creates a submission failure and marks a deleted target when known.
MutationUnavailable unavailableMutation(
  String message, {
  bool targetDeleted = false,
}) => MutationUnavailable([
  TypeDiagnostic(
    code: TypeDiagnosticCode.invalidValue,
    message: message,
    details: targetDeleted
        ? const [TypeDiagnosticDetail(key: "editor.target", value: "deleted")]
        : const [],
  ),
]);
