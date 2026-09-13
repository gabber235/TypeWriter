part of "../../composite_input_renderer.dart";

/// Renders a record with either its declared field presentation or the
/// registry's default representation. The selected presentation owns field
/// layout, while the inspected binding remains the source of values and
/// updates.
extension RecordInputElementRendering on RecordInputElement {
  Widget render({
    required InspectedBinding binding,
    required PresentationRenderScope scope,
  }) => fieldPresentation == null
      ? binding.renderDefaultPresentation(
          scope,
          nodeId: "record.${control.binding.bindingId.value}",
          root: true,
        )
      : PresentationNodeRenderer(
          node: fieldPresentation!.localizeFailures(
            scope.expressions,
            registry: scope.registry,
            budget: scope.budget,
          ),
          scope: scope,
        );
}
