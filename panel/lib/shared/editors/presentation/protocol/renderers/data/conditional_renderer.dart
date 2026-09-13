part of "../../data_renderer.dart";

/// Chooses one branch after evaluating the condition in the current scope.
///
/// A failed evaluation, or a value other than [BooleanValue], becomes a
/// presentation diagnostic. A missing false branch intentionally renders no
/// content rather than inventing a fallback node.
extension ConditionalElementRendering on ConditionalElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final result = scope.evaluate(condition);
    if (result case TypeFailure(:final diagnostics)) {
      return presentationDiagnostic(context, diagnostics);
    }
    final value = result.valueOrNull;
    if (value is! BooleanValue) {
      return presentationDiagnostic(context, [
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Conditional expression must evaluate to boolean",
        ),
      ]);
    }
    final selected = value.value ? whenTrue : whenFalse;
    return selected == null
        ? const SizedBox.shrink()
        : PresentationNodeRenderer(node: selected, scope: scope);
  }
}
