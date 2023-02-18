import "package:clipboard/clipboard.dart";
import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

class CopyableText extends HookConsumerWidget {
  const CopyableText({required this.text, super.key}) : super();

  final String text;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).inputDecorationTheme.fillColor,
        borderRadius: BorderRadius.circular(8),
      ),
      padding: const EdgeInsets.only(left: 20, top: 5, bottom: 5, right: 10),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          SelectableText(text, style: const TextStyle(fontSize: 16)),
          IconButton(
            onPressed: () => FlutterClipboard.copy(text),
            icon: const Icon(FontAwesomeIcons.solidClipboard, size: 18),
          ),
        ],
      ),
    );
  }
}
