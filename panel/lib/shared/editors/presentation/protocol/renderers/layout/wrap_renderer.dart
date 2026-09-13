part of "../../layout_renderer.dart";

/// Arranges children across runs while preventing any child from exceeding
/// the available width.
///
/// The constraint wrapper matters for protocol content whose natural width is
/// larger than its surface. It lets [Wrap] choose runs without changing child
/// rendering or scope semantics.
extension WrapElementRendering on WrapElement {
  Widget render(PresentationRenderScope scope) => LayoutBuilder(
    builder: (context, constraints) => Wrap(
      spacing: spacing,
      runSpacing: runSpacing,
      alignment: mainAxisAlignment.wrapAlignment,
      crossAxisAlignment: crossAxisAlignment.wrapCrossAlignment,
      children: [
        for (final child in children.renderChildren(scope))
          ConstrainedBox(
            constraints: BoxConstraints(maxWidth: constraints.maxWidth),
            child: child,
          ),
      ],
    ),
  );
}
