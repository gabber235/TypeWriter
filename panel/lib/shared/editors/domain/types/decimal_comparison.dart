/// Accepted lexical form for decimal bounds and values.
///
/// Decimal values stay as strings in the editor type model so comparisons do
/// not lose precision when values exceed the range of a Dart numeric type.
final RegExp decimalPattern = RegExp(r"^-?(0|[1-9][0-9]*)(\.[0-9]+)?$");

/// Compares two values that match [decimalPattern] without converting them to
/// a floating point representation.
///
/// Callers use the result for bound validation, constraint intersection, and
/// subtype checks. Both inputs must use the accepted decimal syntax.
int compareDecimalStrings(String left, String right) {
  final leftParts = left._decimalParts;
  final rightParts = right._decimalParts;
  final scale = leftParts.$2 > rightParts.$2 ? leftParts.$2 : rightParts.$2;
  final leftCoefficient =
      leftParts.$1 * BigInt.from(10).pow(scale - leftParts.$2);
  final rightCoefficient =
      rightParts.$1 * BigInt.from(10).pow(scale - rightParts.$2);
  return leftCoefficient.compareTo(rightCoefficient);
}

extension DecimalStringProperties on String {
  /// Number of digits after the decimal point, or zero for an integer form.
  int get decimalScale {
    final separator = indexOf(".");
    return separator < 0 ? 0 : length - separator - 1;
  }

  (BigInt, int) get _decimalParts {
    final separator = indexOf(".");
    if (separator < 0) return (BigInt.parse(this), 0);
    return (BigInt.parse(replaceFirst(".", "")), length - separator - 1);
  }
}
