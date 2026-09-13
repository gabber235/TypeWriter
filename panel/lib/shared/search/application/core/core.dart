/// Application contracts for query ownership, source composition, and result actions.
///
/// A [SearchSource] produces snapshots. [SourceController] owns the parsed
/// query sent to that source. [SearchController] adds selection, preview,
/// section, and action state for the search presentation layer.
library;

export "controller/action_controller.dart";
export "controller/source_controller.dart";
export "models.dart";
export "search_controller.dart";
export "search_source.dart";
export "source/sources.dart";
