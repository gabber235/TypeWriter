import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";

class AnimationAdapter extends Adapter {
  AnimationAdapter({
    required this.animation,
    super.animated,
    super.direction,
  });

  final Animation<double> animation;

  @override
  void attach(AnimationController controller) {
    config(controller, animation.value);

    animation.addListener(_updateValue);
  }

  void _updateValue() {
    updateValue(animation.value);
  }

  @override
  void detach() {
    super.detach();
    animation.removeListener(_updateValue);
  }
}
