part of "../../data_renderer.dart";

/// Renders a typed binding with an explicit presentation or its default editor.
///
/// The renderer never owns the binding. Both paths keep writes and interaction
/// routing in [PresentationRenderScope], so a custom presentation cannot bypass
/// the enclosing editor transaction.
extension TypedFieldElementRendering on TypedFieldElement {
  Widget render(PresentationRenderScope scope) => presentation == null
      ? ProtocolBoundValueEditor(
          control: BoundControl(binding: binding),
          scope: scope,
        )
      : PresentationNodeRenderer(node: presentation!, scope: scope);
}
