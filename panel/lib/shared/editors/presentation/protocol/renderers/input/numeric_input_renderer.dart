part of "../../simple_input_renderer.dart";

/// Renders integer, float, and decimal values through one validated text
/// boundary. The type parser and validator preserve the binding's numeric
/// representation before [BoundControlField.update] forwards a value to its
/// owner.
extension NumericInputElementRendering on NumericInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      builder: (context, field) {
        final prefix = renderControlPrefix(context, control, scope);
        return ValidatedTextField<DataValue>(
          key: ValueKey(field.binding.reference),
          value: field.value,
          name: "number",
          icon: HeroiconsSolid.hashtag,
          decoration: prefix == null
              ? null
              : InputDecoration(prefixIcon: prefix),
          deserialize: (value) => value.expressionDisplayText,
          serialize: (text) => field.binding.type.parseNumberInput(text),
          validator: (value) {
            final diagnostics = value.validateAgainst(field.binding.type);
            return diagnostics.isEmpty ? null : diagnostics.first.message;
          },
          keyboardType: const TextInputType.numberWithOptions(
            signed: true,
            decimal: true,
          ),
          mixed: field.mixed,
          readOnly: field.locked,
          onInputFocus: field.interaction.begin,
          onInputBlur: field.interaction.commit,
          onCancel: field.interaction.cancel,
          onChanged: field.update,
        );
      },
    );
  }
}
