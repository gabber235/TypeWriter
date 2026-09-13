import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Adds the current query and selector choices to an expression context.
///
/// These bindings are read only and are rebuilt for every search or provider
/// expression evaluation. A missing single selector is represented by an
/// empty string, while a multiple selector is represented by an empty list;
/// expressions therefore do not need nullable query state.
ExpressionContext presentationSearchContext({
  required ExpressionContext base,
  required BindingId queryBindingId,
  required SearchQueryContext query,
  required List<SearchSelectorDefinition> selectors,
}) {
  var context = base.withBinding(
    queryBindingId,
    BindingSnapshot(
      type: const StringType(),
      value: StringValue(query.normalizedQuery),
      revision: 0,
      writable: false,
    ),
  );
  for (final selector in selectors) {
    final values = query.selectors
        .where((item) => item.selectorId == selector.id)
        .map((item) => item.value)
        .nonNulls
        .toList(growable: false);
    final multiple =
        selector.multiplicity == SearchSelectorMultiplicity.multiple;
    context = context.withBinding(
      selector.valueBindingId,
      BindingSnapshot(
        type: multiple
            ? const ListType(element: StringType())
            : const StringType(),
        value: multiple
            ? ListValue(values.map(StringValue.new).toList())
            : StringValue(values.firstOrNull ?? ""),
        revision: 0,
        writable: false,
      ),
    );
  }
  return context;
}

/// Converts provider selector metadata into the selector model consumed by the
/// search UI, preserving identity, matching behavior, multiplicity, and values.
List<QuerySelectorDefinition> presentationQuerySelectors(
  List<SearchSelectorDefinition> selectors,
) => selectors
    .map(
      (selector) => KeyValueSelectorDefinition(
        id: selector.id,
        key: selector.key,
        caseSensitive: selector.caseSensitive,
        multiplicity: selector.multiplicity == SearchSelectorMultiplicity.single
            ? QueryMultiplicity.single
            : QueryMultiplicity.multiple,
        color: selector.colorValue == null ? null : Color(selector.colorValue!),
        value: switch (selector.values) {
          FreeTextSearchSelectorValues() => const QuerySelectorValue.freeText(),
          EnumeratedSearchSelectorValues(:final values) =>
            QuerySelectorValue.enumValue(values),
        },
      ),
    )
    .toList(growable: false);
