import "package:flutter/material.dart" hide FilledButton;
import "package:flutter/services.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:typewriter/hooks/timer.dart";
import 'package:typewriter/widgets/components/general/filled_button.dart';

class ConfirmationDialogue extends HookWidget {
  const ConfirmationDialogue({
    required this.title,
    required this.content,
    required this.confirmText,
    required this.confirmIcon,
    required this.confirmColor,
    required this.delayConfirm,
    required this.cancelText,
    required this.cancelIcon,
    required this.onConfirm,
    this.onCancel,
    super.key,
  });

  /// The title of the dialogue.
  final String title;

  /// The content of the dialogue. This can be a small piece of text to explain what the user is confirming.
  final String content;

  /// The text of the confirm button
  final String confirmText;

  /// An icon to display on the confirm button
  final IconData confirmIcon;

  /// The color of the confirm button
  final Color confirmColor;

  /// When [delayConfirm] is larger than 0, the confirm button will be disabled for [delayConfirm] seconds.
  /// This may be useful when the user is about to perform an irreversible action.
  final Duration delayConfirm;

  /// The text of the cancel button
  final String cancelText;

  /// An icon to display on the cancel button
  final IconData cancelIcon;

  /// The action to perform when the user confirms the action.
  final Function onConfirm;

  /// An optional action to perform when the user cancels the action.
  final Function? onCancel;

  @override
  Widget build(BuildContext context) {
    final secondsLeft = useState(delayConfirm.inSeconds);
    final canConfirm = secondsLeft.value <= 0;

    useTimer(
      1.seconds,
      (timer) {
        secondsLeft.value--;
        if (secondsLeft.value <= 0) {
          timer.cancel();
        }
      },
    );

    return AlertDialog(
      title: Text(title),
      content: Text(content),
      actions: [
        TextButton.icon(
          icon: Icon(cancelIcon),
          label: Text(cancelText),
          onPressed: () {
            Navigator.of(context).pop();
            onCancel?.call();
          },
          style: TextButton.styleFrom(
            foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
          ),
        ),
        FilledButton.icon(
          icon: Icon(confirmIcon),
          label: Text(
            canConfirm ? confirmText : "$confirmText (${secondsLeft.value})",
          ),
          color: confirmColor,
          onPressed: canConfirm
              ? () {
                  Navigator.of(context).pop();
                  onConfirm();
                }
              : null,
        ),
      ],
    );
  }
}

void showConfirmationDialogue({
  required BuildContext context,
  required Function onConfirm,
  String title = "Are you sure?",
  String content = "This action cannot be undone.",
  String confirmText = "Confirm",
  IconData confirmIcon = FontAwesomeIcons.trash,
  Color confirmColor = Colors.redAccent,
  Duration delayConfirm = Duration.zero,
  String cancelText = "Cancel",
  IconData cancelIcon = FontAwesomeIcons.xmark,
  Function? onCancel,
}) {
  // If the user has its shift key pressed, we skip the confirmation dialogue.
  // But only if the delay is 0.
  final hasShiftDown = RawKeyboard.instance.keysPressed.contains(LogicalKeyboardKey.shiftLeft);
  if (hasShiftDown && delayConfirm.inSeconds == 0) {
    onConfirm();
    return;
  }

  showDialog(
    context: context,
    builder: (context) => ConfirmationDialogue(
      onConfirm: onConfirm,
      title: title,
      content: content,
      confirmText: confirmText,
      confirmIcon: confirmIcon,
      confirmColor: confirmColor,
      delayConfirm: delayConfirm,
      cancelText: cancelText,
      cancelIcon: cancelIcon,
      onCancel: onCancel,
    ),
  );
}
