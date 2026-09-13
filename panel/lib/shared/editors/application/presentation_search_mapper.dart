import "package:typewriter_panel/typewriter_panel.dart";

/// Turns one provider value into the typed result consumed by presentation
/// search.
///
/// The candidate is bound under [SearchResultMapping.bindingId], then key,
/// selected value, and optional label expressions are evaluated in that same
/// context. A result is emitted only when its key is nonempty and its label is
/// either absent or a nonempty string. The candidate context is retained in
/// the payload so rendering, ranking, and history can evaluate related
/// expressions without refetching the source value.
final class PresentationSearchMapper {
  const PresentationSearchMapper({
    required this.mapping,
    required this.registry,
    required this.budget,
    required this.providerKey,
  });

  final SearchResultMapping mapping;
  final TypeRegistry registry;
  final ExpressionBudget budget;
  final String providerKey;

  TypeResult<SearchResult> map({
    required DataValue value,
    required TypeExpression type,
    required ExpressionContext expressions,
  }) {
    final candidate = expressions.withBinding(
      mapping.bindingId,
      BindingSnapshot(type: type, value: value, revision: 0, writable: false),
    );
    final key = mapping.key.evaluate(
      candidate,
      registry: registry,
      budget: budget,
    );
    if (key case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }
    final selected = mapping.selectedValue.evaluate(
      candidate,
      registry: registry,
      budget: budget,
    );
    if (selected case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }
    final label = mapping.label?.evaluate(
      candidate,
      registry: registry,
      budget: budget,
    );

    if (label case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }

    final title = switch (label?.valueOrNull) {
      StringValue(:final value) when value.trim().isNotEmpty => value,
      null => "Search result",
      _ => null,
    };

    if (title == null) {
      return TypeResult.failure([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Search result labels must be nonempty strings",
        ),
      ]);
    }

    final id = key.valueOrNull!.expressionDisplayText;
    if (id.isEmpty) {
      return TypeResult.failure([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Search result keys must not be empty",
        ),
      ]);
    }
    return TypeResult.success(
      SearchResult(
        id: id,
        type: presentationSearchResultType,
        payload: PresentationSearchResultPayload(
          selectedValue: selected.valueOrNull!,
          presentation: mapping.presentation,
          expressions: candidate,
          providerKey: providerKey,
        ),
        title: title,
      ),
    );
  }
}
