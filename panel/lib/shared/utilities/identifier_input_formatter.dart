const identifierMinimumLength = 3;
const identifierPattern = r"^[a-z0-9]+(_[a-z0-9]+)*$";

/// Validates identifiers accepted by panel authored resources.
extension StringIdentifierValidation on String {
  /// Requires at least three lowercase letters or digits separated by single
  /// underscores. Leading, trailing, and repeated underscores are rejected.
  bool get isValidIdentifier =>
      length >= identifierMinimumLength &&
      RegExp(identifierPattern).hasMatch(this);
}
