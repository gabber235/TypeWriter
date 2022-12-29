import "package:flutter/material.dart";

class AnimatedOvalClip extends ImplicitlyAnimatedWidget {
  const AnimatedOvalClip({
    required super.duration,
    required this.child,
    required this.size,
    super.key,
    super.curve,
    super.onEnd,
  });
  final Widget child;
  final double size;

  @override
  ImplicitlyAnimatedWidgetState<AnimatedOvalClip> createState() => _AnimatedOvalClipState();
}

class _AnimatedOvalClipState extends ImplicitlyAnimatedWidgetState<AnimatedOvalClip> {
  Tween<double>? _sizeTween;

  @override
  void forEachTween(TweenVisitor<dynamic> visitor) {
    _sizeTween =
        visitor(_sizeTween, widget.size, (dynamic value) => Tween<double>(begin: value as double)) as Tween<double>?;
  }

  @override
  Widget build(BuildContext context) {
    return ClipOval(
      clipper: _SizeClipper(size: _sizeTween!.evaluate(animation)),
      child: widget.child,
    );
  }
}

/// Set the size of the oval clip to a percentage of the size of the child.
/// This can be used to animate the size of the oval clip.
class _SizeClipper extends CustomClipper<Rect> {
  _SizeClipper({
    required this.size,
  });

  final double size;

  @override
  Rect getClip(Size size) {
    return Rect.fromLTWH(0, 0, size.width * this.size, size.height * this.size);
  }

  @override
  bool shouldReclip(_SizeClipper oldClipper) {
    return oldClipper.size != size;
  }
}
