import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";

/// Creates a [WidgetStatesController] that will be disposed automatically.
///
/// See also:
/// - [WidgetStatesController]
WidgetStatesController useStatesController({
  List<Object?>? keys,
  Set<WidgetState>? value,
}) =>
    use(
      _StatesControllerHook(
        keys: keys,
        value: value,
      ),
    );

class _StatesControllerHook extends Hook<WidgetStatesController> {
  const _StatesControllerHook({
    super.keys,
    this.value,
  }) : super();

  final Set<WidgetState>? value;

  @override
  HookState<WidgetStatesController, Hook<WidgetStatesController>>
      createState() => _StatesControllerHookState();
}

class _StatesControllerHookState
    extends HookState<WidgetStatesController, _StatesControllerHook> {
  late final controller = WidgetStatesController(hook.value);

  @override
  WidgetStatesController build(BuildContext context) => controller;

  @override
  void dispose() => controller.dispose();

  @override
  String get debugLabel => "useStatesController";
}
