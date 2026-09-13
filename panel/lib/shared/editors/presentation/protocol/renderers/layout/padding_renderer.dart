part of "../../layout_renderer.dart";

/// Adds directional insets without changing ownership of the child tree.
///
/// The protocol stores start and end rather than left and right, so Flutter
/// resolves the values against the ambient writing direction.
extension PaddingElementRendering on PaddingElement {
  Widget render(PresentationRenderScope scope) => Padding(
    padding: EdgeInsetsDirectional.fromSTEB(start, top, end, bottom),
    child: PresentationNodeRenderer(node: child, scope: scope),
  );
}
