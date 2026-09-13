part of "../../content_renderer.dart";

/// Renders numeric protocol progress with an accessible textual value.
///
/// Integer, float, and decimal values are accepted. The maximum must be
/// positive, while the visual fraction is clamped to the indicator range so
/// out of range progress remains displayable without weakening validation.
extension ProgressElementRendering on ProgressElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final resolvedValue = scope.evaluate(value).valueOrNull._progressNumber;
    final resolvedMaximum = scope.evaluate(maximum).valueOrNull._progressNumber;
    if (resolvedValue == null ||
        resolvedMaximum == null ||
        resolvedMaximum <= 0) {
      return presentationDiagnostic(context, [
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message:
              "Progress values must be numeric and maximum must be positive",
        ),
      ]);
    }
    final resolvedLabel = label == null ? null : scope.expressionText(label!);
    return Semantics(
      label: resolvedLabel,
      value: "$resolvedValue of $resolvedMaximum",
      child: LinearProgressIndicator(
        value: (resolvedValue / resolvedMaximum).clamp(0, 1),
      ),
    );
  }
}

extension on DataValue? {
  double? get _progressNumber => switch (this) {
    IntegerValue(:final value) => value.toDouble(),
    FloatValue(:final value) => value,
    DecimalValue(:final value) => double.tryParse(value),
    _ => null,
  };
}
