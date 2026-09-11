part of "../../simple_input_renderer.dart";

extension BytesInputElementRendering on BytesInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      shapeMismatch: (binding) =>
          binding.value is MixedEditorValue ||
              binding.value.valueOrNull is BytesValue
          ? null
          : "Bytes control requires base64 content",
      builder: (context, field) => _renderParsedTextValue(
        field: field,
        text: switch (field.value) {
          BytesValue(:final value) => base64Encode(value),
          _ => null,
        },
        parse: (text) {
          try {
            return BytesValue(Uint8List.fromList(base64Decode(text)));
          } on FormatException {
            return null;
          }
        },
      ),
    );
  }
}
