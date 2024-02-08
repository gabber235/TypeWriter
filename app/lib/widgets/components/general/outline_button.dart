import "dart:math";
import "dart:ui";

import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

class OutlineButton extends HookConsumerWidget {
  const OutlineButton({
    required this.child,
    required this.onPressed,
    this.color,
    this.controller,
    super.key,
  });

  factory OutlineButton.icon({
    required Widget icon,
    required Widget label,
    required VoidCallback? onPressed,
    Color? color,
    MaterialStatesController? controller,
    Key? key,
  }) = _OutlineButtonIcon;

  final Widget child;
  final VoidCallback? onPressed;

  final Color? color;

  final MaterialStatesController? controller;

  @override
  Widget build(BuildContext context, WidgetRef ref) => OutlinedButton(
        onPressed: onPressed,
        style: OutlinedButton.styleFrom(
          foregroundColor: color ?? Theme.of(context).colorScheme.primary,
          side: BorderSide(
            color: color?.withOpacity(0.6) ??
                Theme.of(context).colorScheme.primary.withOpacity(0.6),
            width: 2,
          ),
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

class _OutlineButtonIcon extends OutlineButton {
  _OutlineButtonIcon({
    required Widget icon,
    required Widget label,
    required super.onPressed,
    super.controller,
    super.color,
    super.key,
  }) : super(
          child: _OutlineButtonWithIconChild(label: label, icon: icon),
        );
}

class _OutlineButtonWithIconChild extends StatelessWidget {
  const _OutlineButtonWithIconChild({
    required this.label,
    required this.icon,
  });

  final Widget label;
  final Widget icon;

  @override
  Widget build(BuildContext context) {
    // ignore: deprecated_member_use
    final scale = MediaQuery.textScalerOf(context).textScaleFactor;
    final gap = scale <= 1 ? 8.0 : lerpDouble(8, 4, min(scale - 1, 1))!;
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
