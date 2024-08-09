import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/widgets/components/general/iconify.dart";

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
      if (act.control) prefixes.add(const Iconify("ph:control-bold"));
      if (act.meta) prefixes.add(const Iconify("ph:command-bold"));
      if (act.alt) prefixes.add(const Iconify("ph:option-bold"));
      if (act.shift) {
        prefixes
            .add(const Iconify("fluent:keyboard-shift-uppercase-24-filled"));
      }
    }
    return prefixes;
  }

  Widget? _overrideIcon() {
    if (activator is SingleActivator) {
      final act = activator as SingleActivator;
      if (act.trigger == LogicalKeyboardKey.enter) {
        return const Iconify("streamline:return-2-solid");
      }
      if (act.trigger == LogicalKeyboardKey.backspace) {
        return const Iconify("fa6-solid:delete-left");
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
