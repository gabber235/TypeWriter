/// Shared graph capability for rendering, navigating, and editing node graphs.
///
/// Callers provide an immutable [GraphData] snapshot and receive committed move
/// or resize payloads through callbacks. [Graph] owns only transient interaction
/// state: drag previews, resize previews, keyboard movement baselines, and the
/// viewport animation. Selection remains owned by the shared selection system,
/// while focus identifies the keyboard target and may narrow the selected set.
library;

export "application/application.dart";
export "domain/domain.dart";
export "presentation/presentation.dart";
