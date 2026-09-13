part of "../../layout_renderer.dart";

/// Determines where each hierarchy child connects to its branch.
enum _HierarchyAnchorKind { start, center, offset }

/// Authoring values resolved once before the custom hierarchy layout pass.
///
/// Nullable styles represent intentionally absent strokes. [diagnostics]
/// contains expression failures, while geometry failures are produced later
/// when child sizes make anchor bounds and positions observable.
final class _ResolvedHierarchyLayout {
  const _ResolvedHierarchyLayout({
    required this.itemSpacing,
    required this.indentation,
    required this.leadingSpacing,
    required this.flattenSingleItem,
    required this.crossAxisAlignment,
    required this.anchorKind,
    required this.anchorOffsets,
    required this.unaryStyle,
    required this.trunkStyle,
    required this.branchStyles,
    required this.diagnostics,
  });

  final double itemSpacing;
  final double indentation;
  final double leadingSpacing;
  final bool flattenSingleItem;
  final PresentationCrossAxisAlignment crossAxisAlignment;
  final _HierarchyAnchorKind anchorKind;
  final List<double?> anchorOffsets;
  final _ResolvedConnectorStyle? unaryStyle;
  final _ResolvedConnectorStyle? trunkStyle;
  final List<_ResolvedConnectorStyle?> branchStyles;
  final List<TypeDiagnostic> diagnostics;
}

/// Layout output consumed by the hierarchy render object for one pass.
///
/// Child offsets and connector strokes share the same surface coordinates, so
/// they cannot drift between layout and paint. Diagnostics describe only
/// geometry that could not be produced from otherwise resolved layout values.
final class _HierarchyGeometry {
  const _HierarchyGeometry({
    required this.size,
    required this.childOffsets,
    required this.strokes,
    required this.diagnostics,
  });

  final Size size;
  final List<Offset> childOffsets;
  final List<_ResolvedStrokePath> strokes;
  final List<TypeDiagnostic> diagnostics;
}

final class _HierarchyParentData extends ContainerBoxParentData<RenderBox> {}
