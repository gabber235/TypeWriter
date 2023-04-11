import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

class HelpInfo extends HookConsumerWidget {
  const HelpInfo({
    required this.helpText,
    super.key,
  });

  final String helpText;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Tooltip(
      message: helpText,
      child: Icon(
        Icons.help_outline,
        size: 16,
        color: Theme.of(context).textTheme.bodySmall?.color?.withOpacity(0.6),
      ),
    );
  }
}
