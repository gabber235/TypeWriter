part of "presentation_element.dart";

@freezed
/// Chooses the corner radius applied by a container element.
///
/// Named variants resolve through the panel design system. [custom] evaluates
/// a numeric expression at render time and rejects missing, negative, or
/// nonfinite values as a presentation diagnostic.
sealed class PresentationRadius with _$PresentationRadius {
  /// Removes corner rounding.
  const factory PresentationRadius.none() = NoPresentationRadius;

  /// Uses the design system's small radius.
  const factory PresentationRadius.small() = SmallPresentationRadius;

  /// Uses the design system's medium radius.
  const factory PresentationRadius.medium() = MediumPresentationRadius;

  /// Uses the design system's large radius.
  const factory PresentationRadius.large() = LargePresentationRadius;

  /// Evaluates a radius in logical pixels from the current render scope.
  const factory PresentationRadius.custom(TypedExpression value) =
      CustomPresentationRadius;
}
