part of "../../interaction_renderer.dart";

/// Renders an action control whose icon is supplied by a typed expression.
///
/// Icon evaluation is a presentation boundary. Evaluation diagnostics and a
/// value that is not the nominal [Icon] type become inline diagnostics instead
/// of reaching Flutter's icon widget. The semantic label is evaluated in the
/// same scope, while action ownership remains with [PresentationRenderScope].
extension IconButtonElementRendering on IconButtonElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final result = scope.evaluate(icon);
    if (result case TypeFailure(:final diagnostics)) {
      return presentationDiagnostic(context, diagnostics);
    }
    final value = result.valueOrNull?.iconValueOrNull;
    if (value == null) {
      return presentationDiagnostic(context, [
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Button icon must evaluate to the nominal Icon type",
        ),
      ]);
    }
    return IconButton(
      tooltip: scope.expressionText(semanticLabel),
      onPressed: action.enabledIn(scope) ? () => scope.invoke(action) : null,
      icon: Icones.value(value),
    );
  }
}
