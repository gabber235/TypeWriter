import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/hooks/delayed_execution.dart";

/// For shortcuts to work there must be a focused widget.
/// This widget is a hack to ensure that there is always a focused widget.
class AlwaysFocused extends HookConsumerWidget {
  const AlwaysFocused({
    required this.child,
    super.key,
  }) : super();
  final Widget child;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    useDelayedExecution(() {
      void onFocusChange() {
        final currentFocus = FocusScope.of(context).focusedChild;

        // For shortcuts to work, a widget must be focused.
        // So if nothing is focused, request focus for a placeholder focus.
        if (currentFocus == null) {
          focus.requestFocus();
        }
      }

      FocusScope.of(context).addListener(onFocusChange);
      return () => FocusScope.of(context).removeListener(onFocusChange);
    });

    return Focus(
      focusNode: focus,
      canRequestFocus: true,
      child: child,
    );
  }
}
