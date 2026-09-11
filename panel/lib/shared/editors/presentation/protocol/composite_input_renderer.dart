import "package:flutter/material.dart";
import "package:iconify_flutter_plus/icons/heroicons_solid.dart";
import "package:iconify_flutter_plus/icons/ion.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "renderers/input/collection_support.dart";
part "renderers/input/list_input_renderer.dart";
part "renderers/input/map_entry_support.dart";
part "renderers/input/record_input_renderer.dart";
part "renderers/input/map_input_renderer.dart";

extension ListInputElementResolvedRendering on ListInputElement {
  Widget renderInput(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      labeled: false,
      shapeMismatch: (binding) =>
          binding.type is ListType &&
              (binding.value is MixedEditorValue ||
                  binding.value.valueOrNull is ListValue)
          ? null
          : "List control does not match its binding",
      builder: (context, field) {
        if (field.mixed) {
          return LabeledControl(
            control: control,
            scope: scope,
            child: MixedCollectionControl(
              onReplace: field.editable
                  ? () => field.update(const ListValue([]))
                  : null,
            ),
          );
        }
        return render(
          binding: field.binding.resolvedOrNull!,
          scope: scope,
          editable: field.editable,
        );
      },
    );
  }
}

extension MapInputElementResolvedRendering on MapInputElement {
  Widget renderInput(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      labeled: false,
      shapeMismatch: (binding) =>
          binding.type is MapType &&
              (binding.value is MixedEditorValue ||
                  binding.value.valueOrNull is MapValue)
          ? null
          : "Map control does not match its binding",
      builder: (context, field) {
        if (field.mixed) {
          return LabeledControl(
            control: control,
            scope: scope,
            child: MixedCollectionControl(
              onReplace: field.editable
                  ? () => field.update(const MapValue([]))
                  : null,
            ),
          );
        }
        return render(binding: field.binding.resolvedOrNull!, scope: scope);
      },
    );
  }
}

extension RecordInputElementResolvedRendering on RecordInputElement {
  Widget renderInput(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      control: control,
      scope: scope,
      labeled: false,
      shapeMismatch: (binding) =>
          binding.type is RecordType &&
              (binding.value is MixedEditorValue ||
                  binding.value.valueOrNull is RecordValue)
          ? null
          : "Record control does not match its binding",
      builder: (context, field) {
        return render(binding: field.binding, scope: scope);
      },
    );
  }
}
