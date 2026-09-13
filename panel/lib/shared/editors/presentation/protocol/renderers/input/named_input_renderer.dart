part of "../../simple_input_renderer.dart";

/// Renders the concrete representation behind a nominal binding without
/// exposing that representation as the control's public type contract.
///
/// The projected payload binding is local to this presentation. Updates still
/// target the original binding, so the enclosing scope retains ownership of
/// persistence and editability.
extension NamedInputElementRendering on NamedInputElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    return BoundControlShell(
      nominal: true,
      control: control,
      scope: scope,
      shapeMismatch: (binding) => binding.type is NamedType
          ? null
          : "Named control requires a nominal binding",
      builder: (context, field) {
        final namedType = field.binding.type as NamedType;
        final resolved = scope.registry.resolve(namedType);
        if (resolved case TypeFailure(:final diagnostics)) {
          return presentationDiagnostic(context, diagnostics);
        }
        final nominal = resolved.valueOrNull!;
        if (!nominal.isConcrete) {
          return _inputDiagnostic(
            "Abstract values require a polymorphic control",
          );
        }

        const payloadId = BindingId(2147483646);
        const payloadReference = BindingReference(bindingId: payloadId);
        final source = scope.expressions.bindings.project(
          control.binding,
          registry: scope.registry,
        );
        if (source case TypeFailure(:final diagnostics)) {
          return presentationDiagnostic(context, diagnostics);
        }
        final childScope = scope.withAlias(
          payloadId,
          control.binding,
          source.valueOrNull!.withRootType(nominal.representation),
        );
        return InspectedBinding(
          reference: payloadReference,
          type: nominal.representation,
          value: field.binding.value,
          revision: field.binding.revision,
          writable: field.binding.writable,
        ).renderDefaultPresentation(
          childScope,
          nodeId: "named.${namedType.reference.id}",
        );
      },
    );
  }
}
