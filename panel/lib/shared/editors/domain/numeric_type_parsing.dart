import "package:typewriter_panel/typewriter_panel.dart";

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
