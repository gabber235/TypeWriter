part of "../../content_renderer.dart";

/// Renders a remote protocol image behind an HTTPS trust boundary.
///
/// The source is rejected before Flutter creates a network image provider.
/// Loading remains visible through the shared shimmer treatment, while a
/// failed request becomes a presentation diagnostic rather than an exception.
extension ImageElementRendering on ImageElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final resolvedSource = scope.expressionText(source);
    final uri = Uri.tryParse(resolvedSource);
    if (uri == null || uri.scheme != "https") {
      return presentationDiagnostic(context, [
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Image source must use HTTPS",
        ),
      ]);
    }
    final label = semanticLabel == null
        ? null
        : scope.expressionText(semanticLabel!);
    return ClipRRect(
      borderRadius: context.shapes.mediumBorderRadius,
      child: Image.network(
        resolvedSource,
        semanticLabel: label,
        frameBuilder: (_, child, frame, _) {
          if (frame != null) return child;

          return ShimmerBox.rectangle(
            borderRadius: context.shapes.mediumBorderRadius,
          );
        },
        errorBuilder: (context, error, stackTrace) =>
            presentationDiagnostic(context, [
              const TypeDiagnostic(
                code: TypeDiagnosticCode.invalidValue,
                message: "Image could not be loaded",
              ),
            ]),
      ),
    );
  }
}
