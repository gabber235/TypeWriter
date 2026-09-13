import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "presentation_node.freezed.dart";

/// Recursive unit of the declarative editor surface.
///
/// Catalog producers compose nodes into a tree. Renderers evaluate the node's
/// expressions against the current binding environment, apply [properties],
/// render [header], then render [element]. The node owns no editor values or
/// persistence state. Its [id] identifies the stable presentation location used
/// by rendering and interaction scopes.
@freezed
abstract class PresentationNode with _$PresentationNode {
  @Assert("id != \"\"", "Presentation node ID must not be empty.")
  const factory PresentationNode({
    required String id,
    required PresentationElement element,
    @Default(PresentationProperties()) PresentationProperties properties,
    PresentationHeader? header,
  }) = _PresentationNode;
}

/// Rendering constraints that apply to a [PresentationNode] and its subtree.
///
/// [enabledIf] controls whether the subtree is enabled after evaluation. A
/// read only node remains visible but cannot be used to edit its bound values.
/// The editor remains the authority for mutability, authorization, and commit
/// policy; these properties only describe the presentation request.
@freezed
abstract class PresentationProperties with _$PresentationProperties {
  const factory PresentationProperties({
    TypedExpression? enabledIf,
    @Default(false) bool readOnly,
  }) = _PresentationProperties;
}
