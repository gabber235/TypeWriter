import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";

Widget presentationDiagnostic(
  BuildContext context,
  Iterable<TypeDiagnostic> diagnostics,
) {
  final values = diagnostics.toList();
  final colors = Theme.of(context).colorScheme;
  return Material(
    color: colors.errorContainer,
    borderRadius: context.shapes.mediumBorderRadius,
    child: Padding(
      padding: EdgeInsets.all(context.spacing.space3),
      child: ConstrainedBox(
        constraints: const BoxConstraints(minHeight: 24),
        child: Stack(
          alignment: Alignment.centerLeft,
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 34),
              child: Text(
                values.map((item) => item.message).join("\n"),
                textAlign: TextAlign.left,
                style: Theme.of(context).textTheme.bodyMedium
                    ?.copyWith(color: colors.onErrorContainer),
              ),
            ),
            Positioned(
              left: 0,
              top: 0,
              child: Icon(Icons.error_outline, color: colors.onErrorContainer),
            ),
          ],
        ),
      ),
    ),
  );
}
