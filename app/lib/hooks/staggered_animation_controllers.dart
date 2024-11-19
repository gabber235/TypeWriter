import "dart:async";

import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";

/// Run multiple animation staggered from each other.
List<AnimationController> useStaggeredAnimationControllers({
  required int count,
  required Duration duration,
  required Duration interval,
  required TickerProvider vsync,
  bool autoPlay = true,
}) {
  return use(
    _StaggeredAnimationControllersHook(
      count,
      duration,
      interval,
      vsync,
      autoPlay: autoPlay,
    ),
  );
}

class _StaggeredAnimationControllersHook
    extends Hook<List<AnimationController>> {
  const _StaggeredAnimationControllersHook(
    this.count,
    this.duration,
    this.interval,
    this.vsync, {
    required this.autoPlay,
  });

  final int count;
  final Duration duration;
  final Duration interval;
  final bool autoPlay;
  final TickerProvider vsync;

  @override
  _StaggeredAnimationControllersHookState createState() =>
      _StaggeredAnimationControllersHookState();
}

class _StaggeredAnimationControllersHookState extends HookState<
    List<AnimationController>, _StaggeredAnimationControllersHook> {
  List<AnimationController> controllers = [];
  Timer? timer;
  int index = 0;

  void _animate() {
    if (!hook.autoPlay) {
      return;
    }
    if (timer != null) {
      return;
    }
    if (index >= hook.count) {
      return;
    }
    timer = Timer.periodic(hook.interval, (timer) {
      controllers[index].forward();
      index = index + 1;
      if (index >= hook.count) {
        timer.cancel();
        this.timer = null;
      }
    });
  }

  void _updateControllers() {
    while (controllers.length > hook.count) {
      controllers.removeLast();
    }

    while (controllers.length < hook.count) {
      controllers.add(
        AnimationController(
          duration: hook.duration,
          vsync: hook.vsync,
        ),
      );

      if (index >= controllers.length) {
        index = controllers.length - 1;
      }
    }
  }

  @override
  void initHook() {
    _updateControllers();
    _animate();
    super.initHook();
  }

  @override
  List<AnimationController> build(BuildContext context) {
    _updateControllers();
    _animate();
    return controllers;
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }
}
