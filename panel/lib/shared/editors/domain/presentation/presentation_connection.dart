part of "presentation_element.dart";

/// Position of an anchor relative to the bounds of its owning presentation.
enum PresentationAnchorAlignment {
  topStart,
  topCenter,
  topEnd,
  centerStart,
  center,
  centerEnd,
  bottomStart,
  bottomCenter,
  bottomEnd,
}

/// Binding scope used when evaluating a connection marker expression.
enum ConnectionExpressionScope { layer, source, target }

/// Axis used to route an orthogonal connection bundle.
enum ConnectionAxis { horizontal, vertical }

@freezed
/// Expression driven offset used by anchors and curved connection control
/// points.
abstract class PresentationOffset with _$PresentationOffset {
  const factory PresentationOffset({
    required TypedExpression x,
    required TypedExpression y,
  }) = _PresentationOffset;
}

@freezed
/// Named point that a connection can resolve inside a rendered presentation.
///
/// The renderer discovers local anchors in the current layer. An anchor marked
/// [exportToParent] can additionally be selected through an exported group,
/// allowing a child presentation to participate in a connection owned by its
/// parent.
abstract class PresentationAnchorPoint with _$PresentationAnchorPoint {
  @Assert("id != \"\"", "Anchor ID must not be empty.")
  const factory PresentationAnchorPoint({
    required String id,
    @Default([]) List<String> groupIds,
    @Default(PresentationAnchorAlignment.center)
    PresentationAnchorAlignment alignment,
    PresentationOffset? offset,
    TypedExpression? visibleIf,
    @Default(false) bool exportToParent,
  }) = _PresentationAnchorPoint;
}

@freezed
/// Selects either a local anchor or an exported anchor group.
sealed class PresentationAnchorSelector with _$PresentationAnchorSelector {
  @Assert("id != \"\"", "Anchor ID must not be empty.")
  const factory PresentationAnchorSelector.local(String id) = LocalAnchor;

  @Assert("groupId != \"\"", "Anchor group ID must not be empty.")
  const factory PresentationAnchorSelector.exportedGroup(String groupId) =
      ExportedAnchorGroup;
}

@freezed
/// Visual stroke settings shared by connector paths.
abstract class ConnectorStroke with _$ConnectorStroke {
  const factory ConnectorStroke({
    required TypedExpression color,
    required TypedExpression width,
  }) = _ConnectorStroke;
}

@freezed
/// Marker rendered at a connector endpoint.
sealed class ConnectorEndpointMarker with _$ConnectorEndpointMarker {
  const factory ConnectorEndpointMarker.arrow({required TypedExpression size}) =
      ArrowConnectorMarker;

  const factory ConnectorEndpointMarker.circle({
    required TypedExpression diameter,
  }) = CircleConnectorMarker;
}

@freezed
/// Stroke and endpoint appearance for a connection overlay.
abstract class ConnectorStyle with _$ConnectorStyle {
  const factory ConnectorStyle({
    required ConnectorStroke stroke,
    required TypedExpression cornerRadius,
    ConnectorEndpointMarker? startMarker,
    ConnectorEndpointMarker? endMarker,
  }) = _ConnectorStyle;
}

@freezed
/// Presentation node placed on a resolved connection path.
///
/// Marker expressions are evaluated in [scope], which determines whether they
/// see layer, source, or target bindings. Markers are rendered as an overlay
/// and do not participate in pointer input or semantics.
abstract class ConnectionMarker with _$ConnectionMarker {
  const factory ConnectionMarker({
    required PresentationNode node,
    required TypedExpression position,
    required TypedExpression alignToPath,
    @Default(ConnectionExpressionScope.layer) ConnectionExpressionScope scope,
  }) = _ConnectionMarker;
}

@freezed
/// One bend position for an orthogonal connection.
abstract class OrthogonalConnectionPath with _$OrthogonalConnectionPath {
  const factory OrthogonalConnectionPath({
    required TypedExpression bendPosition,
  }) = _OrthogonalConnectionPath;
}

@freezed
/// Control point offsets that define a curved connection.
abstract class CurvedConnectionPath with _$CurvedConnectionPath {
  const factory CurvedConnectionPath({
    required PresentationOffset sourceControlOffset,
    required PresentationOffset targetControlOffset,
  }) = _CurvedConnectionPath;
}

@freezed
/// Routing strategy for one source to one target connection.
///
/// A path is resolved after anchors are collected. Invalid expressions or
/// unavailable anchors become presentation diagnostics in the renderer.
sealed class ConnectionPath with _$ConnectionPath {
  const factory ConnectionPath.straight() = StraightConnectionPath;
  const factory ConnectionPath.orthogonal(OrthogonalConnectionPath path) =
      OrthogonalPath;
  const factory ConnectionPath.curved(CurvedConnectionPath path) = CurvedPath;
}

@freezed
/// Routing settings for the trunk and branches of a bundled connection.
abstract class OrthogonalConnectionBundlePath
    with _$OrthogonalConnectionBundlePath {
  const factory OrthogonalConnectionBundlePath({
    required ConnectionAxis axis,
    required TypedExpression bendPosition,
  }) = _OrthogonalConnectionBundlePath;
}

@freezed
/// Routing strategy for one source connected to multiple targets.
sealed class ConnectionBundlePath with _$ConnectionBundlePath {
  const factory ConnectionBundlePath.orthogonal(
    OrthogonalConnectionBundlePath path,
  ) = OrthogonalBundlePath;
  const factory ConnectionBundlePath.fan() = FanBundlePath;
}

@freezed
/// Declarative connection drawn between anchors in the layout overlay.
///
/// A regular connection resolves one target. A bundle shares a trunk across
/// several targets. Connections are purely presentation metadata: the layout
/// renderer resolves their expressions and geometry each paint pass, then
/// reports unresolved anchors and invalid values as diagnostics.
sealed class PresentationConnection with _$PresentationConnection {
  const factory PresentationConnection.connection({
    required PresentationAnchorSelector source,
    required PresentationAnchorSelector target,
    required ConnectionPath path,
    required ConnectorStyle style,
    @Default([]) List<ConnectionMarker> markers,
    TypedExpression? visibleIf,
  }) = AnchoredConnection;

  const factory PresentationConnection.bundle({
    required PresentationAnchorSelector source,
    required PresentationAnchorSelector targets,
    required ConnectionBundlePath path,
    required ConnectorStyle trunkStyle,
    required ConnectorStyle branchStyle,
    @Default([]) List<ConnectionMarker> trunkMarkers,
    @Default([]) List<ConnectionMarker> branchMarkers,
    TypedExpression? visibleIf,
  }) = AnchoredConnectionBundle;
}
