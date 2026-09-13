import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:flutter_markdown_plus/flutter_markdown_plus.dart";
import "package:intl/intl.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "renderers/content/badge_renderer.dart";
part "renderers/content/chip_renderer.dart";
part "renderers/content/icon_renderer.dart";
part "renderers/content/image_renderer.dart";
part "renderers/content/markdown_renderer.dart";
part "renderers/content/progress_renderer.dart";
part "renderers/content/status_renderer.dart";
part "renderers/content/text_renderer.dart";
part "renderers/content/time_renderer.dart";

/// Evaluates an optional content color and preserves expression diagnostics.
///
/// Content renderers use this boundary so malformed protocol data becomes a
/// presentation diagnostic instead of an exception or an arbitrary Flutter
/// color. A missing expression intentionally means the theme chooses the
/// color.
TypeResult<Color?> resolvePresentationColor(
  TypedExpression? expression,
  PresentationRenderScope scope,
) {
  if (expression == null) return const TypeResult.success(null);
  final result = scope.evaluate(expression);
  if (result case TypeFailure(:final diagnostics)) {
    return TypeResult.failure(diagnostics);
  }
  final value = result.valueOrNull;
  final color = value?.asColorOrNull;
  return color == null
      ? TypeResult.failure([
          const TypeDiagnostic(
            code: TypeDiagnosticCode.invalidValue,
            message: "Content color must evaluate to a Color",
          ),
        ])
      : TypeResult.success(color);
}

/// Evaluates an optional content size and accepts only finite, nonnegative
/// numeric values. Invalid protocol expressions remain typed diagnostics for
/// the caller to render; an absent expression leaves sizing to the widget.
TypeResult<double?> resolvePresentationSize(
  TypedExpression? expression,
  PresentationRenderScope scope,
) {
  if (expression == null) return const TypeResult.success(null);
  final result = scope.evaluate(expression);
  if (result case TypeFailure(:final diagnostics)) {
    return TypeResult.failure(diagnostics);
  }
  final value = switch (result.valueOrNull) {
    FloatValue(:final value) => value,
    IntegerValue(:final value) => value.toDouble(),
    _ => null,
  };
  return value == null || !value.isFinite || value < 0
      ? TypeResult.failure([
          const TypeDiagnostic(
            code: TypeDiagnosticCode.invalidValue,
            message: "Content size must be a finite nonnegative number",
          ),
        ])
      : TypeResult.success(value);
}
