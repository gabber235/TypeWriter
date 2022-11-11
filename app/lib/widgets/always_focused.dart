import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

class AlwaysFocused extends HookConsumerWidget {
  final Widget child;

  const AlwaysFocused({
    super.key,
    required this.child,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    final currentFocus = FocusScope.of(context).focusedChild;

    useEffect(() {
      // For shortcuts to work, a widget must be focused.
      // So if nothing is focused, request focus for a placeholder focus.
      if (currentFocus == null) {
        focus.requestFocus();
      }
      return null;
    }, [currentFocus]);

    return Focus(
      focusNode: focus,
      canRequestFocus: true,
      child: child,
    );
  }
}
