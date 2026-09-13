import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Hosts the diagnostic banner and root node for one render scope.
///
/// Diagnostics are shown before the tree so invalid bindings or actions remain
/// visible without preventing the rest of the presentation from rendering.
/// The scope, not this surface, owns evaluation, mutation routing, and
/// interaction lifecycle.
class PresentationSurface extends StatelessWidget {
  const PresentationSurface({
    required this.presentation,
    required this.scope,
    this.diagnostics = const [],
    super.key,
  });

  final PresentationNode presentation;
  final PresentationRenderScope scope;
  final List<TypeDiagnostic> diagnostics;

  @override
  Widget build(BuildContext context) => Column(
    crossAxisAlignment: CrossAxisAlignment.stretch,
    children: [
      if (diagnostics.isNotEmpty) ...[
        presentationDiagnostic(context, diagnostics),
        SizedBox(height: context.spacing.space3),
      ],
      PresentationNodeRenderer(node: presentation, scope: scope),
    ],
  );
}
