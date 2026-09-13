/// Realm tag authoring, inspection, inheritance, and graph presentation.
///
/// The feature reads canonical tags from the selected authoring session and
/// overlays local editor drafts for all visible consumers. Mutations return to
/// that session through guarded authoring operations, keeping graph gestures
/// and inspector edits on one consistency path.
library;

export "application/application.dart";
export "presentation/presentation.dart";
