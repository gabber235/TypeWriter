part of "../../simple_input_renderer.dart";

extension EnumInputElementRendering on EnumInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      shapeMismatch: (binding) => binding.type is EnumType
          ? null
          : "Enum control requires an enum binding",
      builder: (context, field) {
        final values = (field.binding.type as EnumType).values;
        final current = field.value;
        final dropdown = Dropdown<DataValue>(
          selected: values.contains(current) ? current : null,
          initialization: field.mixed
              ? SelectionInitializationPolicy.explicit
              : SelectionInitializationPolicy.automatic,
          dropdownMenuEntries: [
            for (final option in values)
              DropdownMenuEntry(
                value: option,
                label: option.expressionDisplayText,
              ),
          ],
          enabled: field.editable,
          onSelected: (next) {
            if (next != null) field.update(next);
          },
        );
        if (!field.mixed) return dropdown;
        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [dropdown, const MixedValueMessage()],
        );
      },
    );
  }
}
