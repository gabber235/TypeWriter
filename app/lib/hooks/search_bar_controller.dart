import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:material_floating_search_bar/material_floating_search_bar.dart';

/// Create and manges a [FloatingSearchBarController].
///
/// The [controller] will stay the same during rebuilds.
FloatingSearchBarController useSearchBarController() {
  return use(_SearchBarControllerHook());
}

class _SearchBarControllerHook extends Hook<FloatingSearchBarController> {
  @override
  _SearchBarControllerHookState createState() =>
      _SearchBarControllerHookState();
}

class _SearchBarControllerHookState
    extends HookState<FloatingSearchBarController, _SearchBarControllerHook> {
  late FloatingSearchBarController controller;

  @override
  void initHook() {
    super.initHook();
    controller = FloatingSearchBarController();
  }

  @override
  FloatingSearchBarController build(BuildContext context) => controller;

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }
}
