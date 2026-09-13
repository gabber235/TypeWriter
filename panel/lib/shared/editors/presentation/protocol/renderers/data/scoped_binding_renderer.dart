part of "../../data_renderer.dart";

extension ScopedBindingElementRendering on ScopedBindingElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final bindingResult = scope.inspect(binding);
    if (bindingResult case TypeFailure(:final diagnostics)) {
      return presentationDiagnostic(context, diagnostics);
    }
    final source = scope.expressions.bindings.project(
      binding,
      registry: scope.registry,
    );
    if (source case TypeFailure(:final diagnostics)) {
      return presentationDiagnostic(context, diagnostics);
    }
    final childScope = scope.withAlias(
      scopeBindingId,
      binding,
      source.valueOrNull!,
    );
    final localizedChild = child.localizeFailures(
      childScope.expressions,
      registry: childScope.registry,
      budget: childScope.budget,
    );
    return PresentationNodeRenderer(node: localizedChild, scope: childScope);
  }
}
