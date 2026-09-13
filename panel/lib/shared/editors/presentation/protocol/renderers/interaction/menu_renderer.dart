part of "../../interaction_renderer.dart";

/// Renders a protocol menu while keeping menu state in Flutter's menu host.
///
/// The trigger is a labeled text button or an unlabeled overflow icon. Each
/// item evaluates its label at render time and routes its action through the
/// same scope policy as standalone buttons. A disabled item stays visible so
/// the presentation exposes unavailable work without granting a dispatch path.
extension MenuElementRendering on MenuElement {
  Widget render(PresentationRenderScope scope) {
    final resolvedLabel = label == null ? null : scope.expressionText(label!);
    return ContextMenuRegion(
      enableGestures: false,
      items: [
        for (final item in items)
          MenuItem(
            label: scope.expressionText(item.label),
            onPressed: item.action.enabledIn(scope)
                ? () => scope.invoke(item.action)
                : null,
          ),
      ],
      builder: (context, controller, child) => resolvedLabel == null
          ? IconButton(
              tooltip: "Open menu",
              onPressed: ContextMenuRegion.onPress(controller),
              icon: const Icones(Fa6Solid.ellipsis_vertical),
            )
          : TextButton(
              onPressed: ContextMenuRegion.onPress(controller),
              child: Text(resolvedLabel),
            ),
    );
  }
}
