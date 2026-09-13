import "package:typewriter_panel/typewriter_panel.dart";

/// Shared type constraint for identifiers authored by the panel.
///
/// It is kept aligned with the input formatter's minimum length and lowercase
/// segmented syntax so values accepted by editors and values accepted by the
/// domain model have one contract.
const identifierStringType = StringType(
  minimumLength: identifierMinimumLength,
  patterns: [identifierPattern],
);
