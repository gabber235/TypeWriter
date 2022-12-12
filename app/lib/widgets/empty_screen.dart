import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:rive/rive.dart';
import 'package:typewriter/widgets/filled_button.dart';

class EmptyScreen extends HookConsumerWidget {
  const EmptyScreen({
    required this.title,
    this.buttonText,
    this.onButtonPressed,
    super.key,
  }) : super();

  final String title;
  final String? buttonText;
  final VoidCallback? onButtonPressed;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      children: [
        const Spacer(),
        const Expanded(
          flex: 2,
          child: RiveAnimation.asset(
            "assets/cute_robot.riv",
            stateMachines: ["Motion"],
          ),
        ),
        Text(
          title,
          textAlign: TextAlign.center,
          style: const TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 24),
        if (buttonText != null)
          FilledButton.icon(
            label: Text(buttonText ?? ""),
            onPressed: onButtonPressed,
            icon: const Icon(FontAwesomeIcons.plus),
          ),
        const Spacer(),
      ],
    );
  }
}
