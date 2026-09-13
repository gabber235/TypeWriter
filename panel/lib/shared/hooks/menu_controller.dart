import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";

/// Creates a stable [MenuController] for the current widget instance.
///
/// The controller is memoized without dependencies, so it remains the same
/// across rebuilds and is replaced when this hook leaves the widget tree.
MenuController useMenuController() {
  return useMemoized<MenuController>(MenuController.new);
}
