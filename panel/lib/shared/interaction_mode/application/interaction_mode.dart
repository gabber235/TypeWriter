/// Describes one mutually exclusive interaction context in the panel.
///
/// The current instance is the authoritative mode state exposed by the
/// `currentInteractionModeProvider`. Capabilities such as display, shortcuts,
/// and directional focus are opt in through the interaction mode mixins. Mode
/// instances are immutable values, so a transition replaces the whole value
/// rather than mutating the active mode.
abstract class InteractionMode {
  const InteractionMode();

  /// Stable human readable name used by diagnostics and mode presentations.
  String get name;
}
