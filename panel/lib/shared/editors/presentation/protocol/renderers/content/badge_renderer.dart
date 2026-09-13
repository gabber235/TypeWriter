part of "../../content_renderer.dart";

/// Renders a protocol badge as a compact, theme aware status marker.
///
/// The badge owns only its visual mapping. Its label is resolved by the
/// presentation scope, while the protocol tone selects a semantic color and
/// unknown tones deliberately fall back to the theme primary color.
extension BadgeElementRendering on BadgeElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final color = tone._badgeTone(context);

    return Chip(
      visualDensity: VisualDensity.compact,
      label: Text(scope.expressionText(label), style: TextStyle(color: color)),
      avatar: Icon(Icons.circle, size: 10, color: color),
      backgroundColor: color.withValues(alpha: 0.2),
      side: BorderSide(color: color),
    );
  }
}

extension on String {
  Color _badgeTone(BuildContext context) => switch (this) {
    "danger" => Theme.of(context).colorScheme.error,
    "success" => context.colors.success,
    "warning" => context.colors.warning,
    _ => Theme.of(context).colorScheme.primary,
  };
}
