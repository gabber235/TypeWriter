part of "presentation_element.dart";

enum SearchSelectorMultiplicity { single, multiple }

@freezed
sealed class SearchSelectorValues with _$SearchSelectorValues {
  const factory SearchSelectorValues.freeText() = FreeTextSearchSelectorValues;

  @Assert("values.isNotEmpty", "Selector values must not be empty.")
  factory SearchSelectorValues.enumeration(List<String> values) =
      EnumeratedSearchSelectorValues;
}

/// Declares a named filter exposed by a search provider.
///
/// The selector value is installed under [valueBindingId] while provider
/// expressions run. Enumeration values constrain the query vocabulary, while
/// free text accepts arbitrary input. Multiplicity determines whether one or
/// several selector values may be active.
@freezed
abstract class SearchSelectorDefinition with _$SearchSelectorDefinition {
  @Assert("id != \"\"", "Selector ID must not be empty.")
  @Assert("key != \"\"", "Selector key must not be empty.")
  const factory SearchSelectorDefinition.keyValue({
    required String id,
    required String key,
    required BindingId valueBindingId,
    required SearchSelectorValues values,
    @Default(false) bool caseSensitive,
    @Default(SearchSelectorMultiplicity.single)
    SearchSelectorMultiplicity multiplicity,
    int? colorValue,
  }) = KeyValueSearchSelectorDefinition;
}

/// Maps one provider candidate into a selectable presentation result.
///
/// [key] identifies the candidate, [selectedValue] is written when a caller
/// selects it, and [presentation] renders the candidate in the result list.
/// Expressions are evaluated with provider and result bindings supplied by the
/// search source.
@freezed
abstract class SearchResultMapping with _$SearchResultMapping {
  const factory SearchResultMapping({
    required BindingId bindingId,
    required TypedExpression key,
    required TypedExpression selectedValue,
    required PresentationNode presentation,
    TypedExpression? label,
  }) = _SearchResultMapping;
}

@freezed
abstract class HttpQueryParameter with _$HttpQueryParameter {
  @Assert("name != \"\"", "Query parameter name must not be empty.")
  const factory HttpQueryParameter({
    required String name,
    required TypedExpression value,
    @Default(false) bool omitIfEmpty,
  }) = _HttpQueryParameter;
}

@freezed
abstract class HttpJsonContextBinding with _$HttpJsonContextBinding {
  @Assert("path != \"\"", "Context binding path must not be empty.")
  const factory HttpJsonContextBinding({
    required BindingId bindingId,
    required String path,
    required TypeExpression type,
  }) = _HttpJsonContextBinding;
}

@freezed
abstract class SearchRankingField with _$SearchRankingField {
  @Assert("weight > 0", "Ranking weight must be positive.")
  const factory SearchRankingField({
    required TypedExpression expression,
    required int weight,
  }) = _SearchRankingField;
}

/// Declarative search source and transformation graph.
///
/// Leaf providers obtain candidates from local values, collections, HTTP JSON,
/// or a Realm callback. Wrapper providers compose query gating, rate control,
/// caching, ranking, limits, history, grouping, and merging. The application
/// source factory materializes this value into a disposable search source;
/// provider definitions themselves own no streams, network clients, or cache
/// state.
@freezed
sealed class SearchProvider with _$SearchProvider {
  const factory SearchProvider.collection({
    required PresentationCollectionSourceId sourceId,
    required SearchResultMapping result,
    TypedExpression? where,
    @Default([]) List<SearchSelectorDefinition> selectors,
  }) = CollectionSearchProvider;

  const factory SearchProvider.staticValues({
    required TypedExpression values,
    required SearchResultMapping result,
    @Default([]) List<SearchSelectorDefinition> selectors,
  }) = StaticSearchProvider;

  @Assert("resultPath != \"\"", "Result path must not be empty.")
  const factory SearchProvider.httpJson({
    required TypedExpression uri,
    required List<HttpQueryParameter> parameters,
    required String resultPath,
    required TypeExpression resultType,
    required SearchResultMapping result,
    @Default([]) List<HttpJsonContextBinding> contextBindings,
    @Default([]) List<SearchSelectorDefinition> selectors,
    @Default(Duration(seconds: 5)) Duration timeout,
  }) = HttpJsonSearchProvider;

  const factory SearchProvider.realmCallback({
    required CapabilityId capabilityId,
    required TypedExpression payload,
    required SearchResultMapping result,
    @Default([]) List<SearchSelectorDefinition> selectors,
  }) = RealmCallbackSearchProvider;

  const factory SearchProvider.gate({
    required TypedExpression condition,
    required SearchProvider child,
    TypedExpression? guidance,
  }) = GatedSearchProvider;

  const factory SearchProvider.debounce({
    required Duration duration,
    required SearchProvider child,
  }) = DebouncedSearchProvider;

  @Assert("capacity > 0", "Cache capacity must be positive.")
  const factory SearchProvider.cache({
    required int capacity,
    required SearchProvider child,
    @Default(true) bool retainStaleResults,
  }) = CachedSearchProvider;

  @Assert("fields.isNotEmpty", "Ranking fields must not be empty.")
  factory SearchProvider.rank({
    required List<SearchRankingField> fields,
    required SearchProvider child,
  }) = RankedSearchProvider;

  const factory SearchProvider.limit({
    required TypedExpression maximum,
    required SearchProvider child,
  }) = LimitedSearchProvider;

  const factory SearchProvider.distinct({required SearchProvider child}) =
      DistinctSearchProvider;

  @Assert("key != \"\"", "History key must not be empty.")
  @Assert("capacity > 0", "History capacity must be positive.")
  const factory SearchProvider.history({
    required String key,
    required TypedExpression label,
    required int capacity,
    required SearchProvider child,
  }) = HistoricalSearchProvider;

  @Assert("id != \"\"", "Section ID must not be empty.")
  const factory SearchProvider.section({
    required String id,
    required TypedExpression label,
    required SearchProvider child,
  }) = SectionSearchProvider;

  @Assert("children.isNotEmpty", "Merged providers must not be empty.")
  factory SearchProvider.merge({required List<SearchProvider> children}) =
      MergedSearchProvider;
}
