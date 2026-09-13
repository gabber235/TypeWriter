part of "search_input.dart";

/// Creates the source used by one presentation search surface.
///
/// The source may observe [selections] to implement history or other
/// selection driven behavior. It is created inside the search provider scope,
/// and its lifetime ends when that surface is removed, so it must release its
/// subscriptions and external resources through [SearchSource.dispose].
typedef PresentationSearchSourceBuilder = SearchSource Function(
  Ref ref,
  Stream<PresentationSearchSelectionEvent> selections,
);
