import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";

/// Creates a [MaterialStatesController] that will be disposed automatically.
///
/// See also:
/// - [MaterialStatesController]
MaterialStatesController useStatesController({
  List<Object?>? keys,
  Set<MaterialState>? value,
}) => use(
    _StatesControllerHook(
      keys: keys,
      value: value,
    ),
  );

class _StatesControllerHook extends Hook<MaterialStatesController> {
  const _StatesControllerHook({
    super.keys,
    this.value,
  }) : super();

  final Set<MaterialState>? value;

  @override
  HookState<MaterialStatesController, Hook<MaterialStatesController>>
      createState() => _StatesControllerHookState();
}

class _StatesControllerHookState
    extends HookState<MaterialStatesController, _StatesControllerHook> {
  late final controller = MaterialStatesController(hook.value);

  @override
  MaterialStatesController build(BuildContext context) => controller;

  @override
  void dispose() => controller.dispose();

  @override
  String get debugLabel => "useStatesController";
}
