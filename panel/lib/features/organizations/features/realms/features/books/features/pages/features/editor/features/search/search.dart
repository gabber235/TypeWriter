/// Search presentation for navigating editor books, pages, entries, tags, and element definitions.
///
/// The feature owns only the visual adapters. Shared search code supplies the result model,
/// selection state, keyboard actions, and asynchronous preview lifecycle. Result items render
/// those inputs without loading data or changing selection themselves.
library;

export "presentation/presentation.dart";
