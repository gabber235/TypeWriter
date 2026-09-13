import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "presentation_search_models.freezed.dart";

const presentationSearchResultType = SearchResultType(
  id: "presentation",
  rowRendererId: "presentation",
  label: "Result",
);

/// Carries the selected value and render context produced by a presentation
/// search provider.
///
/// The retained [expressions] are the candidate context, including provider
/// bindings and any HTTP response bindings. They let the result render and be
/// ranked after the source request has finished. [providerKey] identifies the
/// provider definition used to serialize and validate history.
@freezed
abstract class PresentationSearchResultPayload
    with _$PresentationSearchResultPayload {
  const factory PresentationSearchResultPayload({
    required DataValue selectedValue,
    required PresentationNode presentation,
    required ExpressionContext expressions,
    required String providerKey,
  }) = _PresentationSearchResultPayload;
}

/// Announces a selected result to the history coordinator.
///
/// [historyNamespace] routes the result to the matching historical provider;
/// selection is separate from result production so search sources stay
/// independent of persistence.
@freezed
abstract class PresentationSearchSelectionEvent
    with _$PresentationSearchSelectionEvent {
  const factory PresentationSearchSelectionEvent({
    required SearchResult result,
    required String historyNamespace,
  }) = _PresentationSearchSelectionEvent;
}
