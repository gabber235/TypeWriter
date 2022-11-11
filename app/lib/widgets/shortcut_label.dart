import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

class ShortcutLabel extends HookConsumerWidget {
  final ShortcutActivator activator;

  const ShortcutLabel({
    super.key,
    required this.activator,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final prefixes = [];

    if (activator is SingleActivator) {
      final act = activator as SingleActivator;
      if (act.control) prefixes.add(const Icon(CupertinoIcons.control));
      if (act.meta) prefixes.add(const Icon(CupertinoIcons.command));
      if (act.alt) prefixes.add(const Icon(CupertinoIcons.alt));
      if (act.shift) prefixes.add(const Icon(CupertinoIcons.shift));
    }

    return IconTheme(
      data: IconTheme.of(context).copyWith(size: 12, color: Colors.grey),
      child: Row(
        children: [
          ...prefixes,
          Text(
            activator.triggers?.map((e) => e.keyLabel).join(' + ') ?? '',
            style: const TextStyle(
              color: Colors.grey,
              fontSize: 12,
            ),
          ),
        ],
      ),
    );
  }
}
