import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:rive/rive.dart";

RiveStateMachine useRiveStateMachine(String stateMachineName) {
  return use(_RiveStateMachineHook(stateMachineName));
}

class _RiveStateMachineHook extends Hook<RiveStateMachine> {
  const _RiveStateMachineHook(this.stateMachineName);
  final String stateMachineName;

  @override
  HookState<RiveStateMachine, Hook<RiveStateMachine>> createState() =>
      _RiveStateMachineHookState();
}

class _RiveStateMachineHookState
    extends HookState<RiveStateMachine, _RiveStateMachineHook> {
  late final RiveStateMachine _riveStateMachine;

  @override
  void initHook() {
    _riveStateMachine = RiveStateMachine(hook.stateMachineName)
      ..addListener(() {
        setState(() {});
      });
    super.initHook();
  }

  @override
  RiveStateMachine build(BuildContext context) => _riveStateMachine;

  @override
  void dispose() {
    _riveStateMachine.dispose();
    super.dispose();
  }
}

class RiveStateMachine extends ChangeNotifier {
  RiveStateMachine(this.stateMachineName);
  final String stateMachineName;

  StateMachineController? _controller;

  bool get hasController => _controller != null;

  void init(Artboard artboard) {
    final controller =
        StateMachineController.fromArtboard(artboard, stateMachineName);
    if (controller != null) {
      artboard.addController(controller);
    } else {
      throw Exception("StateMachineController not found: $stateMachineName");
    }
    _controller = controller;
    notifyListeners();
  }

  SMITrigger? getTrigger(String name) {
    final input = _controller?.findInput<bool>(name);
    if (input == null) return null;
    return input as SMITrigger;
  }

  SMIBool? getBool(String name) {
    final output = _controller?.findInput<bool>(name);
    if (output == null) return null;
    return output as SMIBool;
  }

  SMINumber? getNumber(String name) {
    final output = _controller?.findInput<double>(name);
    if (output == null) return null;
    return output as SMINumber;
  }

  bool getBoolean(String name) => getBool(name)?.value ?? false;

  double getNumberValue(String name) => getNumber(name)?.value ?? 0.0;

  void fire(String name) => getTrigger(name)?.fire();

  // ignore: avoid_positional_boolean_parameters
  void setBool(String name, bool value) => getBool(name)?.value = value;

  void setNumber(String name, double value) => getNumber(name)?.value = value;

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }
}
