part of "../../layout_renderer.dart";

/// Projects anchor declarations into render geometry that a connection layer
/// can discover while painting.
///
/// Anchors are observations of the rendered child, not layout owners. The
/// child keeps its normal rendering and size. Anchor expressions are evaluated
/// in this node's scope, then the anchor surface maps each visible alignment
/// and offset into the connection layer's coordinates. This keeps geometry
/// collection separate from connection path resolution.
extension PresentationAnchorElementRendering on PresentationAnchorElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final resolved = <_ResolvedAnchorPoint>[];
    final diagnostics = <TypeDiagnostic>[];
    for (final anchor in anchors) {
      final point = anchor._resolve(scope);
      diagnostics.addAll(point.diagnostics);
      if (point.valueOrNull case final value?) resolved.add(value);
    }
    if (diagnostics.isNotEmpty) {
      return presentationDiagnostic(context, diagnostics);
    }
    return _PresentationAnchorSurface(
      points: resolved,
      scope: scope,
      occurrenceIdentity: scope.expansionIdentity,
      child: PresentationNodeRenderer(node: child, scope: scope),
    );
  }
}

/// Places a connection resolving surface around the rendered child.
///
/// The layer owns connections declared at this level. It can resolve anchors
/// in its child subtree and explicitly exported anchors from one nested layer,
/// but nested layers remain independent owners of their own connections.
extension ConnectionLayerElementRendering on ConnectionLayerElement {
  Widget render(BuildContext context, PresentationRenderScope scope) =>
      _ConnectionLayerSurface(
        connections: connections,
        scope: scope,
        child: PresentationNodeRenderer(node: child, scope: scope),
      );
}

extension on PresentationAnchorPoint {
  /// Resolves visibility and expression driven offset without touching layout.
  ///
  /// Invisible anchors remain in the resolved model as hidden points, which
  /// lets the surface apply one consistent discovery rule and prevents stale
  /// coordinates from being exposed to a connection layer.
  TypeResult<_ResolvedAnchorPoint> _resolve(PresentationRenderScope scope) {
    final visible = visibleIf == null
        ? const TypeResult<DataValue>.success(BooleanValue(true))
        : scope.evaluate(visibleIf!);
    if (visible case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }
    final visibleValue = visible.valueOrNull;
    if (visibleValue is! BooleanValue) {
      return TypeResult.failure([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Anchor visibility must evaluate to boolean",
        ),
      ]);
    } else if (!visibleValue.value) {
      return TypeResult.success(
        _ResolvedAnchorPoint(
          id: id,
          groupIds: groupIds,
          alignment: alignment,
          offset: Offset.zero,
          exportToParent: exportToParent,
          visible: false,
        ),
      );
    }

    final resolvedOffset = offset?._resolve(scope);
    if (resolvedOffset case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }
    return TypeResult.success(
      _ResolvedAnchorPoint(
        id: id,
        groupIds: groupIds,
        alignment: alignment,
        offset: resolvedOffset?.valueOrNull ?? Offset.zero,
        exportToParent: exportToParent,
        visible: true,
      ),
    );
  }
}

extension on PresentationOffset {
  TypeResult<Offset> _resolve(PresentationRenderScope scope) {
    final xValue = scope.evaluate(x);
    final yValue = scope.evaluate(y);
    final diagnostics = [...xValue.diagnostics, ...yValue.diagnostics];
    if (diagnostics.isNotEmpty) return TypeResult.failure(diagnostics);

    final dx = xValue.valueOrNull._connectionNumber;

    final dy = yValue.valueOrNull._connectionNumber;
    if (dx == null || dy == null || !dx.isFinite || !dy.isFinite) {
      return TypeResult.failure([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Anchor offset must contain finite numbers",
        ),
      ]);
    }
    return TypeResult.success(Offset(dx, dy));
  }
}

extension on DataValue? {
  double? get _connectionNumber => switch (this) {
    FloatValue(:final value) => value,
    IntegerValue(:final value) => value.toDouble(),
    _ => null,
  };
}

/// Anchor values after expression evaluation, before render transforms.
///
/// Visibility is retained because hidden anchors must not participate in
/// selection or connection lookup. [position] applies writing direction to
/// both the alignment and the configured logical offset.
final class _ResolvedAnchorPoint {
  const _ResolvedAnchorPoint({
    required this.id,
    required this.groupIds,
    required this.alignment,
    required this.offset,
    required this.exportToParent,
    required this.visible,
  });

  final String id;
  final List<String> groupIds;
  final PresentationAnchorAlignment alignment;
  final Offset offset;
  final bool exportToParent;
  final bool visible;

  Offset position(Size size, TextDirection direction) {
    final start = direction == TextDirection.ltr ? 0.0 : size.width;
    final end = direction == TextDirection.ltr ? size.width : 0.0;
    final aligned = switch (alignment) {
      PresentationAnchorAlignment.topStart => Offset(start, 0),
      PresentationAnchorAlignment.topCenter => Offset(size.width / 2, 0),
      PresentationAnchorAlignment.topEnd => Offset(end, 0),
      PresentationAnchorAlignment.centerStart => Offset(start, size.height / 2),
      PresentationAnchorAlignment.center => size.center(Offset.zero),
      PresentationAnchorAlignment.centerEnd => Offset(end, size.height / 2),
      PresentationAnchorAlignment.bottomStart => Offset(start, size.height),
      PresentationAnchorAlignment.bottomCenter => Offset(
        size.width / 2,
        size.height,
      ),
      PresentationAnchorAlignment.bottomEnd => Offset(end, size.height),
    };
    final logicalOffset = direction == TextDirection.ltr
        ? offset
        : Offset(-offset.dx, offset.dy);
    return aligned + logicalOffset;
  }
}
