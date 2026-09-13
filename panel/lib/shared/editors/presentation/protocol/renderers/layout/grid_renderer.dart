part of "../../layout_renderer.dart";

/// Projects grid intent into equal width cells.
///
/// Width is derived from the incoming constraint after all horizontal gaps are
/// reserved. This keeps cell sizing owned by the containing surface while each
/// child keeps the same render scope as its grid.
extension GridElementRendering on GridElement {
  Widget render(PresentationRenderScope scope) => LayoutBuilder(
    builder: (context, constraints) {
      final gaps = horizontalSpacing * (columns - 1);
      final width = (constraints.maxWidth - gaps) / columns;
      return Wrap(
        spacing: horizontalSpacing,
        runSpacing: verticalSpacing,
        children: [
          for (final child in children)
            SizedBox(
              width: width,
              child: PresentationNodeRenderer(node: child, scope: scope),
            ),
        ],
      );
    },
  );
}
