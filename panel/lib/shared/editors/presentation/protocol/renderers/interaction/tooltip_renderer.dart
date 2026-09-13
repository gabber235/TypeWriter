part of "../../interaction_renderer.dart";

/// Adds explanatory hover or long press text without changing child
/// ownership.
///
/// The message is evaluated from the current scope. The child is delegated to
/// [PresentationNodeRenderer], so its bindings, diagnostics, enabled state, and
/// nested actions continue through the normal presentation tree lifecycle.
extension TooltipElementRendering on TooltipElement {
  Widget render(PresentationRenderScope scope) => Tooltip(
    message: scope.expressionText(message),
    child: PresentationNodeRenderer(node: child, scope: scope),
  );
}
