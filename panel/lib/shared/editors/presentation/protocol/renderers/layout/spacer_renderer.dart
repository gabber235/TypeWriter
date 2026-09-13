part of "../../layout_renderer.dart";

/// Reserves optional width and height in the current render scope.
///
/// Expression values that are absent or invalid resolve to null, allowing
/// Flutter's [SizedBox] to leave that axis unconstrained.
extension SpacerElementRendering on SpacerElement {
  Widget render(PresentationRenderScope scope) => SizedBox(
    width: width.resolveLayoutSize(scope),
    height: height.resolveLayoutSize(scope),
  );
}
