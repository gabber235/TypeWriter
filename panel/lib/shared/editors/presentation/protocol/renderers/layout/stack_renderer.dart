part of "../../layout_renderer.dart";

/// Paints children in the same bounds, preserving their render order.
///
/// The stack is a composition boundary only. Child nodes retain the shared
/// scope and remain responsible for hit testing and interaction.
extension StackElementRendering on StackElement {
  Widget render(PresentationRenderScope scope) =>
      Stack(children: children.renderChildren(scope));
}
