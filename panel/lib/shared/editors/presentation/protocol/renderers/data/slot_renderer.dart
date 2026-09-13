part of "../../data_renderer.dart";

/// Inserts content supplied by a composite renderer through the render scope.
///
/// Slots are resolved by identifier at render time. Missing content is an
/// invalid presentation, because silently omitting it would hide a broken
/// collaboration between the template and its renderer.
extension PresentationSlotElementRendering on PresentationSlotElement {
  Widget render(BuildContext context, PresentationRenderScope scope) {
    final content = scope.presentationSlots[slotId];
    if (content != null) return content;
    return presentationDiagnostic(context, [
      TypeDiagnostic(
        code: TypeDiagnosticCode.invalidValue,
        message: "Presentation slot $slotId has no content",
      ),
    ]);
  }
}
