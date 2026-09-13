part of "../../simple_input_renderer.dart";

/// Renders a nominal unsigned 32 bit color value through the shared color
/// picker. The nominal binding is resolved here because the picker edits the
/// encoded integer representation while the presentation contract names the
/// domain value as a color.
extension ColorInputElementRendering on ColorInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      nominal: true,
      control: control,
      scope: scope,
      shapeMismatch: (binding) => binding.type is NamedType
          ? null
          : "Color control requires a nominal binding",
      builder: (context, field) {
        final resolved = scope.registry.resolve(
          field.binding.type as NamedType,
        );
        if (resolved case TypeFailure(:final diagnostics)) {
          return presentationDiagnostic(context, diagnostics);
        }
        const expected = IntegerType(width: IntegerWidth.unsigned32);
        if (!resolved.valueOrNull!.isConcrete ||
            resolved.valueOrNull!.representation != expected) {
          return _inputDiagnostic(
            "Color control requires a concrete unsigned 32 bit value",
          );
        }
        if (field.mixed) {
          return ColorPickerField.mixed(
            includeAlpha: includeAlpha,
            enabled: field.enabled,
            readOnly: field.readOnly,
            onInteractionStart: field.interaction.begin,
            onInteractionCommit: field.interaction.commit,
            onInteractionCancel: field.interaction.cancel,
            onChanged: (next) => field.update(next.asValue),
          );
        }
        final value = field.value;
        if (value is! IntegerValue) {
          return _inputDiagnostic(
            "Color control requires a concrete unsigned 32 bit value",
          );
        }
        return ColorPickerField(
          color: Color(value.value.toInt()),
          includeAlpha: includeAlpha,
          enabled: field.enabled,
          readOnly: field.readOnly,
          onInteractionStart: field.interaction.begin,
          onInteractionCommit: field.interaction.commit,
          onInteractionCancel: field.interaction.cancel,
          onChanged: (next) => field.update(next.asValue),
        );
      },
    );
  }
}
