import "package:flutter/cupertino.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

class ShortcutLabel extends HookConsumerWidget {
  const ShortcutLabel({
    required this.activator,
    super.key,
  }) : super();
  final ShortcutActivator activator;

  List<Widget> _prefixes() {
    final prefixes = <Widget>[];

    if (activator is SingleActivator) {
      final act = activator as SingleActivator;
      if (act.control) prefixes.add(const Icon(CupertinoIcons.control));
      if (act.meta) prefixes.add(const Icon(CupertinoIcons.command));
      if (act.alt) prefixes.add(const Icon(CupertinoIcons.alt));
      if (act.shift) prefixes.add(const Icon(CupertinoIcons.shift));
    }
    return prefixes;
  }

  Widget? _overrideIcon() {
    if (activator is SingleActivator) {
      final act = activator as SingleActivator;
      if (act.trigger == LogicalKeyboardKey.enter) {
        return const Icon(CupertinoIcons.return_icon);
      }
      if (act.trigger == LogicalKeyboardKey.backspace) {
        return const Icon(CupertinoIcons.delete_left);
      }
    }
    return null;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final prefixes = useMemoized(_prefixes, []);

    return IconTheme(
      data: IconTheme.of(context).copyWith(size: 12, color: Colors.grey),
      child: Row(
        children: [
          ...prefixes,
          _overrideIcon() ??
              Text(
                activator.triggers?.map((e) => e.keyLabel).join(" + ") ?? "",
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
