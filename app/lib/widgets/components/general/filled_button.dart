import "dart:math" as math;
import "dart:ui" show lerpDouble;

import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

class FilledButton extends HookConsumerWidget {
  const FilledButton({
    required this.child,
    required this.onPressed,
    this.color,
    this.controller,
    super.key,
  });

  factory FilledButton.icon({
    required Widget icon,
    required Widget label,
    required VoidCallback? onPressed,
    Color? color,
    MaterialStatesController? controller,
    Key? key,
  }) = _FilledButtonIcon;

  final Widget child;
  final VoidCallback? onPressed;

  final Color? color;

  final MaterialStatesController? controller;

  @override
  Widget build(BuildContext context, WidgetRef ref) => TextButton(
        onPressed: onPressed,
        style: TextButton.styleFrom(
          backgroundColor: color ?? Theme.of(context).colorScheme.primary,
          foregroundColor: Colors.white,
          disabledBackgroundColor: color?.withOpacity(0.3) ??
              Theme.of(context).colorScheme.primary.withOpacity(0.3),
          padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 24),
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.all(Radius.circular(8)),
          ),
        ),
        statesController: controller,
        child: child,
      );
}

class _FilledButtonIcon extends FilledButton {
  _FilledButtonIcon({
    required Widget icon,
    required Widget label,
    required super.onPressed,
    super.controller,
    super.color,
    super.key,
  }) : super(
          child: _FilledButtonWithIconChild(label: label, icon: icon),
        );
}

class _FilledButtonWithIconChild extends StatelessWidget {
  const _FilledButtonWithIconChild({
    required this.label,
    required this.icon,
  });

  final Widget label;
  final Widget icon;

  @override
  Widget build(BuildContext context) {
    // ignore: deprecated_member_use
    final scale = MediaQuery.maybeOf(context)?.textScaleFactor ?? 1;
    final gap = scale <= 1 ? 8.0 : lerpDouble(8, 4, math.min(scale - 1, 1))!;
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        IconTheme(
          data: IconThemeData(
            size: 18,
            color: IconTheme.of(context).color,
          ),
          child: icon,
        ),
        SizedBox(width: gap),
        Flexible(child: label),
      ],
    );
  }
}
