import "package:typewriter_panel/typewriter_panel.dart";

/// Converts numeric input text using the editor's declared numeric type.
///
/// Integer input uses [BigInt], float input uses [double], and decimal input
/// remains text backed so its exact written digits are preserved. Invalid text
/// and nonnumeric types throw [FormatException]; the numeric input renderer
/// uses that boundary to reject serialization before a draft mutation.
extension NumericTypeParsing on TypeExpression {
  DataValue parseNumberInput(String text) => switch (this) {
    IntegerType() => switch (BigInt.tryParse(text)) {
      final value? => IntegerValue(value),
      null => throw const FormatException("Invalid integer"),
    },
    FloatType() => switch (double.tryParse(text)) {
      final value? => FloatValue(value),
      null => throw const FormatException("Invalid number"),
    },
    DecimalType()
        when RegExp(r"^-?(0|[1-9][0-9]*)(\.[0-9]+)?$").hasMatch(text) =>
      DecimalValue(text),
    DecimalType() => throw const FormatException("Invalid decimal"),
    _ => throw const FormatException("Input is not numeric"),
  };
}
