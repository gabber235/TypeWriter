part of "../../data_renderer.dart";

/// Publishes a binding under the presentation's local scope binding id.
///
/// The projected source is read through the current expression environment and
/// the alias maps nested writes back to the original reference. This creates a
/// lexical view for the child without transferring ownership of the value.
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
