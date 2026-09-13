part of "../../simple_input_renderer.dart";

/// Converts renderer configuration and value shape failures into the same
/// diagnostic surface used by the protocol renderer.
Widget _inputDiagnostic(String message) {
  return Builder(
    builder: (context) => presentationDiagnostic(context, [
      TypeDiagnostic(code: TypeDiagnosticCode.invalidValue, message: message),
    ]),
  );
}

/// Provides the shared interaction lifecycle for text encoded values such as
/// bytes. Parsing failures do not update the bound value, so incomplete input
/// remains recoverable in the field.
Widget _renderParsedTextValue({
  required BoundControlField field,
  required String? text,
  required DataValue? Function(String) parse,
}) {
  return EditorTextField(
    key: ValueKey(field.binding.reference),
    text: text,
    decoration: field.mixed ? const InputDecoration().forMixedValue : null,
    enabled: field.editable,
    onInputFocus: field.interaction.begin,
    onDone: (_) => field.interaction.commit(),
    onCancel: field.interaction.cancel,
    onChanged: (next) {
      final parsed = parse(next);
      if (parsed != null &&
          parsed.validateAgainst(field.binding.type).isEmpty) {
        field.update(parsed);
      }
    },
  );
}
