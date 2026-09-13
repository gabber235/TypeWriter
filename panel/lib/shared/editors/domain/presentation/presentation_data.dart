part of "presentation_element.dart";

/// Defines how a repeated or graph result is rendered as a sequence.
///
/// [item] is evaluated once per result with that result's binding scope.
/// [empty] is used only when the result has no items. [separator] is inserted
/// between items for child based layouts. Hierarchy, grid, and stack layouts
/// impose restrictions on separators that presentation validation reports.
@freezed
abstract class SequencePresentation with _$SequencePresentation {
  const factory SequencePresentation({
    required PresentationNode item,
    PresentationNode? empty,
    PresentationNode? separator,
    @Default(
      PresentationSequenceLayout.children(PresentationChildrenLayout.column()),
    )
    PresentationSequenceLayout layout,
  }) = _SequencePresentation;
}
