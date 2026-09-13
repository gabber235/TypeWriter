part of "../../interaction_renderer.dart";

/// Renders a text action as the standard filled control.
///
/// The label is evaluated from the current expression environment on each
/// build. Pressing the control only routes the declared action through the
/// render scope; local mutation, Realm execution, and failure handling remain
/// owned by the enclosing presentation owner.
extension ButtonElementRendering on ButtonElement {
  Widget render(PresentationRenderScope scope) => FilledButton(
    onPressed: action.enabledIn(scope) ? () => scope.invoke(action) : null,
    child: Text(scope.expressionText(label)),
  );
}
