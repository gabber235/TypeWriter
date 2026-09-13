part of "../../scalar_input_renderer.dart";

/// Resolves expression backed options into a choice control and routes the
/// selected value through the bound field. A custom value adds the ordinary
/// bound editor below the choices, preserving one source of truth.
extension SelectInputElementRendering on SelectInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      builder: (context, field) {
        final resolvedOptions = [
          for (final option in options)
            if (scope.evaluate(option.value).valueOrNull case final value?)
              (option, value),
        ];
        final current = field.value;
        final hasSelection =
            allowCustomValue ||
            resolvedOptions.any((option) => option.$2 == current);
        final initializer = hasSelection
            ? null
            : field.binding.type
                  .createInitialValue(registry: scope.registry)
                  .valueOrNull;
        final selected = !hasSelection && current == initializer
            ? null
            : current;
        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            AdaptiveChoiceControl<DataValue>(
              selected: selected,
              initialization: field.mixed
                  ? SelectionInitializationPolicy.explicit
                  : SelectionInitializationPolicy.automatic,
              defaultValue: defaultValue == null
                  ? null
                  : scope.evaluate(defaultValue!).valueOrNull,
              choices: {
                for (final option in resolvedOptions)
                  option.$2: scope.expressionText(option.$1.label),
              },
              enabled: field.editable,
              onSelected: (value) {
                if (value != null) field.update(value);
              },
            ),
            if (field.mixed) const MixedValueMessage(),
            if (allowCustomValue) ...[
              SizedBox(height: context.spacing.space2),
              ProtocolBoundValueEditor(
                control: BoundControl(binding: control.binding),
                scope: scope,
              ),
            ],
          ],
        );
      },
    );
  }
}
