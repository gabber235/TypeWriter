part of "../../simple_input_renderer.dart";

extension ToggleInputElementRendering on ToggleInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      labeled: false,
      shapeMismatch: (binding) =>
          binding.type is BooleanType &&
              (binding.value is MixedEditorValue ||
                  binding.value.valueOrNull is BooleanValue)
          ? null
          : "Toggle control requires a boolean binding",
      builder: (context, field) {
        if (!field.mixed) return const SizedBox.shrink();
        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            CheckboxListTile(
              value: null,
              tristate: true,
              contentPadding: EdgeInsets.zero,
              title: control.label == null
                  ? null
                  : Text(scope.expressionText(control.label!)),
              subtitle: control.description == null
                  ? null
                  : Text(scope.expressionText(control.description!)),
              onChanged: field.editable
                  ? (next) {
                      if (next != null) field.update(BooleanValue(next));
                    }
                  : null,
            ),
            const MixedValueMessage(),
          ],
        );
      },
    );
  }
}
