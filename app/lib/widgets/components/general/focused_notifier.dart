import "package:flutter/cupertino.dart";

class FocusedNotifier extends InheritedNotifier<FocusNode> {
  const FocusedNotifier({
    required super.child,
    required FocusNode focusNode,
    super.key,
  }) : super(notifier: focusNode);

  static FocusedNotifier? of(BuildContext context) {
    return context.dependOnInheritedWidgetOfExactType<FocusedNotifier>();
  }

  static bool isFocused(BuildContext context) {
    return of(context)?.notifier?.hasFocus ?? false;
  }
}
