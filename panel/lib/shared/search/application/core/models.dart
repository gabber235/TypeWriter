import "dart:async";

import "package:flutter/foundation.dart";
import "package:flutter/widgets.dart";
import "package:freezed_annotation/freezed_annotation.dart";

part "models.freezed.dart";

/// Immutable contracts shared by search sources, controllers, and widgets.
///
/// Sources publish a [SearchSourceSnapshot] containing a hierarchical
/// [SearchNode] tree. Controllers turn raw input into [SearchQueryContext] and
/// coordinate actions against result IDs still present in that tree.

/// A selector extracted from the normalized query.
@freezed
abstract class SearchParsedSelector with _$SearchParsedSelector {
  @Assert("selectorId != \"\"", "Selector ID must not be empty.")
  @Assert("key != \"\"", "Key must not be empty.")
  const factory SearchParsedSelector({
    required String selectorId,
    required String key,
    String? value,
  }) = _SearchParsedSelector;
}

/// Boolean operators preserved in the parsed selector expression.
enum SearchSelectorOperator { and, or }

/// The selector expression used by sources that need boolean query semantics.
@freezed
sealed class SearchSelectorExpression with _$SearchSelectorExpression {
  const factory SearchSelectorExpression.leaf(SearchParsedSelector selector) =
      SearchSelectorLeafExpression;

  const factory SearchSelectorExpression.binary({
    required SearchSelectorOperator operator,
    required SearchSelectorExpression left,
    required SearchSelectorExpression right,
  }) = SearchSelectorBinaryExpression;

  const factory SearchSelectorExpression.not(
    SearchSelectorExpression expression,
  ) = SearchSelectorNotExpression;
}

/// Parsed query state passed from [SourceController] to a [SearchSource].
@freezed
abstract class SearchQueryContext with _$SearchQueryContext {
  const factory SearchQueryContext({
    required String normalizedQuery,
    required List<SearchParsedSelector> selectors,
    SearchSelectorExpression? selectorExpression,
  }) = _SearchQueryContext;
}

/// Controls whether guidance remains visible with results.
enum SearchGuidanceVisibility { always, emptyOnly }

/// Non error information a source wants the search UI to display.
@freezed
abstract class SearchGuidance with _$SearchGuidance {
  @Assert("id != \"\"", "ID must not be empty.")
  @Assert("title != \"\"", "Title must not be empty.")
  const factory SearchGuidance({
    required String id,
    required String title,
    String? description,
    @Default(SearchGuidanceVisibility.emptyOnly)
    SearchGuidanceVisibility visibility,
    @Default(0) int priority,
  }) = _SearchGuidance;
}

/// Severity presented for a source diagnostic.
enum SearchErrorSeverity { warning, error }

/// A source scoped warning or error rendered above the result tree.
@freezed
abstract class SearchErrorSummary with _$SearchErrorSummary {
  @Assert("id != \"\"", "ID must not be empty.")
  @Assert("message != \"\"", "Message must not be empty.")
  const factory SearchErrorSummary({
    required String id,
    required String message,
    required SearchErrorSeverity severity,
    String? sourceLabel,
  }) = _SearchErrorSummary;
}

/// Coarse lifecycle state represented by a source snapshot.
enum SearchSourceStatus { idle, loading, ready, error }

/// Immutable source projection consumed by search widgets.
///
/// Nodes and actions may remain available while [status] is loading or error,
/// allowing decorators to retain stale results while exposing current feedback.
@freezed
abstract class SearchSourceSnapshot with _$SearchSourceSnapshot {
  const factory SearchSourceSnapshot({
    required SearchSourceStatus status,
    required List<SearchNode> nodes,
    @Default({}) Map<Type, SearchAction> actions,
    @Default(<SearchGuidance>[]) List<SearchGuidance> guidance,
    @Default(<SearchErrorSummary>[]) List<SearchErrorSummary> errorSummaries,
  }) = _SearchSourceSnapshot;

  factory SearchSourceSnapshot.idle({
    List<SearchNode> nodes = const [],
    Map<Type, SearchAction> actions = const {},
    List<SearchGuidance> guidance = const [],
  }) => SearchSourceSnapshot(
    status: SearchSourceStatus.idle,
    nodes: nodes,
    actions: actions,
    guidance: guidance,
  );

  factory SearchSourceSnapshot.loading({
    List<SearchNode> nodes = const [],
    Map<Type, SearchAction> actions = const {},
    List<SearchGuidance> guidance = const [],
    List<SearchErrorSummary> errorSummaries = const [],
  }) => SearchSourceSnapshot(
    status: SearchSourceStatus.loading,
    nodes: nodes,
    actions: actions,
    guidance: guidance,
    errorSummaries: errorSummaries,
  );

  factory SearchSourceSnapshot.ready({
    required List<SearchNode> nodes,
    Map<Type, SearchAction> actions = const {},
    List<SearchGuidance> guidance = const [],
    List<SearchErrorSummary> errorSummaries = const [],
  }) => SearchSourceSnapshot(
    status: SearchSourceStatus.ready,
    nodes: nodes,
    actions: actions,
    guidance: guidance,
    errorSummaries: errorSummaries,
  );

  factory SearchSourceSnapshot.error({
    required List<SearchErrorSummary> errorSummaries,
    List<SearchNode> nodes = const [],
    Map<Type, SearchAction> actions = const {},
    List<SearchGuidance> guidance = const [],
  }) {
    assert(
      errorSummaries.any((s) => s.severity == SearchErrorSeverity.error),
      "Error snapshot requires at least one error severity summary",
    );
    return SearchSourceSnapshot(
      status: SearchSourceStatus.error,
      nodes: nodes,
      actions: actions,
      guidance: guidance,
      errorSummaries: errorSummaries,
    );
  }
}

/// A result tree node. Sections may contain nested sections and results.
///
/// Child order is significant. Sources and tree builders preserve it for
/// display, traversal, ranking tie breaks, and stable row identity.
@freezed
sealed class SearchNode with _$SearchNode {
  @Assert("id != \"\"", "ID must not be empty.")
  @Assert("title != \"\"", "Title must not be empty.")
  const factory SearchNode.section({
    required String id,
    required String title,
    String? subtitle,
    @Default(<SearchNode>[]) List<SearchNode> children,
  }) = SearchSectionNode;

  const factory SearchNode.result({required SearchResult result}) =
      SearchResultNode;
}

/// Traversal helpers that preserve depth first, source order.
extension SearchNodes on List<SearchNode> {
  List<SearchResult> findResults(Set<String> resultIds) {
    final wanted = resultIds.toSet();
    final results = <SearchResult>[];
    final stack = [];

    for (var i = length - 1; i >= 0; i--) {
      stack.add(this[i]);
    }

    while (stack.isNotEmpty && wanted.isNotEmpty) {
      final node = stack.removeLast();

      switch (node) {
        case SearchSectionNode():
          for (var i = node.children.length - 1; i >= 0; i--) {
            stack.add(node.children[i]);
          }
        case SearchResultNode():
          if (wanted.remove(node.result.id)) {
            results.add(node.result);
          }
      }
    }

    return results;
  }

  Iterable<SearchNode> walk() sync* {
    final stack = [];

    for (var i = length - 1; i >= 0; i--) {
      stack.add(this[i]);
    }

    while (stack.isNotEmpty) {
      final node = stack.removeLast();

      yield node;

      switch (node) {
        case SearchSectionNode():
          for (var i = node.children.length - 1; i >= 0; i--) {
            stack.add(node.children[i]);
          }
        case SearchResultNode():
      }
    }
  }
}

/// Rendering identity for a [SearchResult].
@freezed
abstract class SearchResultType with _$SearchResultType {
  @Assert("id != \"\"", "ID must not be empty.")
  @Assert("rowRendererId != \"\"", "Row renderer ID must not be empty.")
  @Assert(
    "previewRendererId == null || previewRendererId != \"\"",
    "Preview renderer ID must be null or nonempty.",
  )
  const factory SearchResultType({
    required String id,
    required String rowRendererId,
    String? previewRendererId,
    String? label,
  }) = _SearchResultType;
}

/// Search data rendered as a row and optionally a preview.
@freezed
abstract class SearchResult with _$SearchResult {
  @Assert("id != \"\"", "ID must not be empty.")
  const factory SearchResult({
    required String id,
    required SearchResultType type,
    required Object payload,
    @Default([]) List<Type> actions,
    String? title,
    String? subtitle,
    @Default(false) bool isStale,
  }) = _SearchResult;
}

/// Execution strategy represented by the action base class selected at runtime.
enum SearchActionBatchMode { none, aggregate, repeated }

/// A user operation exposed by one or more search results.
///
/// Actions are registered in a snapshot by runtime type. [priority] controls
/// presentation order, while optional icon, color, and shortcut customize UI.
abstract class SearchAction {
  const SearchAction();

  String get label;
  int get priority;
  String? get icon => null;
  Color? get color => null;
  ShortcutActivator? get shortcut => null;
}

/// Executes once for exactly one result.
abstract class SingleSearchAction extends SearchAction {
  Future<SearchActionResult> execute(SearchResult result);
}

/// Executes independently for each selected result.
abstract class RepeatedSearchAction extends SearchAction {
  Future<SearchActionResult> execute(SearchResult result);
}

/// Executes once with all selected results.
abstract class BatchSearchAction extends SearchAction {
  Future<SearchActionResult> executeBatch(List<SearchResult> results);
}

/// Outcome and requested UI effect of an action execution.
@freezed
abstract class SearchActionResult with _$SearchActionResult {
  const factory SearchActionResult.completed({
    @Default(SearchActionEffect.close()) SearchActionEffect effect,
  }) = SearchActionResultCompleted;

  @Assert("message != \"\"", "Message must not be empty.")
  const factory SearchActionResult.failed({
    required String message,
    @Default(SearchActionEffect.refresh()) SearchActionEffect effect,
  }) = SearchActionResultFailed;
}

/// Combines repeated action outcomes into one user visible result.
extension SearchActionResults on List<SearchActionResult> {
  SearchActionResult merge() {
    if (isEmpty) {
      return SearchActionResult.completed();
    }

    final effects = map((r) => r.effect).toSet();
    final messages = whereType<SearchActionResultFailed>()
        .map((r) => r.message)
        .toSet();

    final effect = effects.merge();

    if (messages.isEmpty) {
      return SearchActionResult.completed(effect: effect);
    }

    final message = messages.length == 1
        ? messages.first
        : "Search failed: ${messages.join(", ")}";

    return SearchActionResult.failed(message: message, effect: effect);
  }
}

/// Controller instruction emitted after an action completes.
@freezed
abstract class SearchActionEffect with _$SearchActionEffect {
  const factory SearchActionEffect.updateQuery({required String updateQuery}) =
      SearchActionUpdateQuery;

  const factory SearchActionEffect.refresh() = SearchActionRefresh;

  const factory SearchActionEffect.close() = SearchActionClose;
}

/// Resolves multiple controller effects into one deterministic instruction.
///
/// Query updates take precedence over refresh, refresh takes precedence over
/// close, and an empty set closes the search surface.
extension SearchActionEffects on Set<SearchActionEffect> {
  SearchActionEffect merge() {
    if (isEmpty) {
      return SearchActionEffect.close();
    }

    final updates = whereType<SearchActionUpdateQuery>().toList();
    if (updates.isNotEmpty) {
      return updates.first;
    }

    final refreshes = any((e) => e is SearchActionRefresh);
    if (refreshes) {
      return SearchActionEffect.refresh();
    }

    return SearchActionEffect.close();
  }
}

/// Immediate result of attempting to submit an action.
enum SearchActionSubmitResult {
  submitted,
  busy,
  invalidSelection,
  actionNotFound,
}

/// Observable lifecycle of the currently submitted action.
@freezed
sealed class SearchActionState with _$SearchActionState {
  const factory SearchActionState.idle() = SearchActionIdle;

  @Assert("resultIds.length > 0", "Result IDs must not be empty.")
  const factory SearchActionState.running({
    required Type action,
    required Set<String> resultIds,
  }) = SearchActionRunning;

  @Assert("resultIds.length > 0", "Result IDs must not be empty.")
  const factory SearchActionState.completed({
    required Type action,
    required Set<String> resultIds,
  }) = SearchActionCompleted;

  @Assert("resultIds.length > 0", "Result IDs must not be empty.")
  @Assert("message != \"\"", "Message must not be empty.")
  const factory SearchActionState.failed({
    required Type action,
    required Set<String> resultIds,
    required String message,
  }) = SearchActionFailed;
}

/// Identifies a result whose detail should be loaded.
@freezed
abstract class SearchPreviewRequest with _$SearchPreviewRequest {
  @Assert("resultId != \"\"", "Result ID must not be empty.")
  const factory SearchPreviewRequest({
    required String resultId,
    SearchQueryContext? queryContext,
  }) = _SearchPreviewRequest;
}

/// Success or user visible failure from a preview request.
@freezed
abstract class SearchPreviewRequestResult with _$SearchPreviewRequestResult {
  const factory SearchPreviewRequestResult.data({required Object data}) =
      SearchPreviewRequestResultData;

  @Assert("message != \"\"", "Message must not be empty.")
  const factory SearchPreviewRequestResult.error({required String message}) =
      SearchPreviewRequestResultError;
}

/// Selection behavior supported by the actions in the current snapshot.
enum SearchSelectionMode { single, multiple }
