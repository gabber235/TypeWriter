part of "../../scalar_input_renderer.dart";

extension SliderInputElementRendering on SliderInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      builder: (context, field) {
        final resolvedValue = field.value._sliderNumber;
        final resolvedMinimum = scope
            .evaluate(minimum)
            .valueOrNull
            ._sliderNumber;
        final resolvedMaximum = scope
            .evaluate(maximum)
            .valueOrNull
            ._sliderNumber;
        final resolvedDivisions = divisions._sliderDivisions(scope);
        if (resolvedMinimum == null ||
            resolvedMaximum == null ||
            resolvedMinimum >= resolvedMaximum) {
          return presentationDiagnostic(context, [
            const TypeDiagnostic(
              code: TypeDiagnosticCode.invalidValue,
              message: "Slider values are invalid",
            ),
          ]);
        }
        if (resolvedValue == null && field.mixed) {
          return MixedSlider(
            minimum: resolvedMinimum,
            maximum: resolvedMaximum,
            divisions: resolvedDivisions,
            onChanged: !field.editable
                ? null
                : (next) {
                    final typed = field.binding.type.sliderValue(next);
                    if (typed != null) field.update(typed);
                  },
          );
        }
        if (resolvedValue == null) {
          return presentationDiagnostic(context, [
            const TypeDiagnostic(
              code: TypeDiagnosticCode.invalidValue,
              message: "Slider value is invalid",
            ),
          ]);
        }
        return Slider(
          value: resolvedValue.clamp(resolvedMinimum, resolvedMaximum),
          min: resolvedMinimum,
          max: resolvedMaximum,
          divisions: resolvedDivisions,
          onChanged: !field.editable
              ? null
              : (next) {
                  final typed = field.binding.type.sliderValue(next);
                  if (typed != null) field.update(typed);
                },
        );
      },
    );
  }
}

extension on TypedExpression? {
  int? _sliderDivisions(PresentationRenderScope scope) {
    if (this == null) return null;
    final value = scope.evaluate(this!).valueOrNull;
    if (value case IntegerValue(:final value)) {
      final divisions = value.toInt();
      return divisions > 0 ? divisions : null;
    }
    return null;
  }
}

extension on DataValue? {
  double? get _sliderNumber => switch (this) {
    IntegerValue(:final value) => value.toDouble(),
    FloatValue(:final value) => value,
    DecimalValue(:final value) => double.tryParse(value),
    _ => null,
  };
}

extension on TypeExpression {
  DataValue? sliderValue(double value) => switch (this) {
    IntegerType() => IntegerValue(BigInt.from(value.round())),
    FloatType() => FloatValue(value),
    DecimalType() => DecimalValue(value.toString()),
    _ => null,
  };
}
