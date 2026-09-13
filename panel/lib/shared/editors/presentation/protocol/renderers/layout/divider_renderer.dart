part of "../../layout_renderer.dart";

/// Renders the protocol divider with the panel's fixed visual spacing.
///
/// A divider has no child scope or mutable state. Its element exists to place a
/// structural separator in a presentation sequence.
extension DividerElementRendering on DividerElement {
  Widget render() => const Divider(height: 24);
}
