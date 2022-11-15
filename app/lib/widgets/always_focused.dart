import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

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
    final currentFocus = FocusScope.of(context).focusedChild;

    useEffect(
      () {
        // For shortcuts to work, a widget must be focused.
        // So if nothing is focused, request focus for a placeholder focus.
        if (currentFocus == null) {
          focus.requestFocus();
        }
        return null;
      },
      [currentFocus],
    );

    return Focus(
      focusNode: focus,
      canRequestFocus: true,
      child: child,
    );
  }
}
