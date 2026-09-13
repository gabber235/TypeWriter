import "package:freezed_annotation/freezed_annotation.dart";

part "text_input_format.freezed.dart";

/// Declarative normalization or filtering applied while a text control changes.
///
/// The renderer converts these values to Flutter input formatters in listed
/// order. Formatters affect candidate text before the bound editor receives an
/// update; they do not replace binding validation. Regular expression variants
/// are checked by presentation validation so malformed catalog data becomes a
/// diagnostic instead of a renderer exception.
@freezed
sealed class TextInputFormat with _$TextInputFormat {
  const factory TextInputFormat.lowercase() = LowercaseTextInputFormat;

  const factory TextInputFormat.uppercase() = UppercaseTextInputFormat;

  const factory TextInputFormat.replace({
    required String pattern,
    required String replacement,
  }) = ReplaceTextInputFormat;

  const factory TextInputFormat.allow(String pattern) = AllowTextInputFormat;

  const factory TextInputFormat.deny(String pattern) = DenyTextInputFormat;
}
