import "package:collection/collection.dart";
import "package:typewriter_panel/typewriter_panel.dart";

export "query_cursor.dart";
export "query_lexer.dart";
export "query_models.dart";
export "query_selector.dart";
export "query_spans.dart";
export "query_suggestions.dart";

/// Parses search input against the selector definitions currently available.
///
/// Parsing returns both the normalized free text sent to sources and the
/// selector expression used for structured filtering. Cursor context and
/// issues are derived from the same parse, so query bar suggestions cannot
/// drift from search semantics.
class QueryEngine {
  QueryEngine(List<QuerySelectorDefinition> selectors)
    : selectors = List.unmodifiable(selectors),
      lexer = QueryLexer(selectors);

  final List<QuerySelectorDefinition> selectors;
  final QueryLexer lexer;

  /// Parses [input], optionally resolving the cursor at [cursorOffset].
  ///
  /// The offset is clamped to the input bounds. A null offset omits cursor
  /// context, which is useful for callers that only need search semantics.
  QueryParseResult parse(String input, {int? cursorOffset}) {
    final clamped = cursorOffset?.clamp(0, input.length);

    if (input.trim().isEmpty) {
      if (cursorOffset == null) {
        return QueryParseResult.empty();
      }
      return QueryParseResult(
        query: "",
        queryBefore: "",
        queryAfter: "",
        raw: input,
        expression: null,
        tokens: const [],
        selectors: const [],
        issues: const [],
        cursorContext: UnknownCursorContext(
          cursorOffset: clamped!,
          activeRange: QueryRange(clamped, clamped),
          partial: "",
          side: QuerySide.before,
        ),
      );
    }

    final result = lexer.tokenize(input);
    final tokens = result.expression?.flatten() ?? <QueryLexerToken>[];
    final issues = tokens.expand((token) => token.issues).toList();
    final selectors = tokens.whereType<QueryLexerSelectorToken>().toList();

    final cursorContext = clamped != null
        ? resolveQueryCursorContext(tokens, input, clamped)
        : null;

    final selectorsById = selectors.groupListsBy((s) => s.selectorId);

    for (final selector in this.selectors) {
      final sel = selectorsById[selector.id];
      if (sel == null) {
        continue;
      }
      final i = selector.validate(sel);
      issues.addAll(i);
    }

    return QueryParseResult(
      query: result.query,
      queryBefore: result.queryBefore,
      queryAfter: result.queryAfter,
      raw: result.raw,
      expression: result.expression,
      tokens: List.unmodifiable(tokens),
      selectors: List.unmodifiable(selectors),
      issues: List.unmodifiable(issues),
      cursorContext: cursorContext,
    );
  }
}

/// Small facade for parsing queries without exposing the lexer lifecycle.
class Query {
  Query(List<QuerySelectorDefinition> selectors)
    : _engine = QueryEngine(selectors);
  final QueryEngine _engine;

  /// Parses [query] using the selector definitions supplied at construction.
  QueryParseResult parse(String query, {int? cursorOffset}) {
    return _engine.parse(query, cursorOffset: cursorOffset);
  }
}
