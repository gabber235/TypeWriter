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
    final snapshot = replaced.valueOrNull!.bindings[bindingId];
    if (snapshot == null)
      return invalidLocalMutation("Updated binding is absent");
    return LocalMutationApplied(bindingId: bindingId, value: snapshot.value);
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
