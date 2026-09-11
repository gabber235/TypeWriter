import "package:typewriter_panel/typewriter_panel.dart";

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

LocalMutationInvalid invalidLocalMutation(String message) =>
    LocalMutationInvalid([
      TypeDiagnostic(code: TypeDiagnosticCode.invalidValue, message: message),
    ]);

MutationInvalid invalidMutation(String message) => MutationInvalid([
  TypeDiagnostic(code: TypeDiagnosticCode.invalidValue, message: message),
]);

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
