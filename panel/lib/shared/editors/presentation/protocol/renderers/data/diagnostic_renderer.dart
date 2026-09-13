part of "../../data_renderer.dart";

/// Turns protocol diagnostics into the common presentation failure surface.
///
/// This node has no binding or recovery behavior. The enclosing renderer owns
/// the decision to continue rendering siblings beside the diagnostic.
extension DiagnosticElementRendering on DiagnosticElement {
  Widget render(BuildContext context) =>
      presentationDiagnostic(context, diagnostics);
}
