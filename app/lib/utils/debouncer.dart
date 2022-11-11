import 'dart:async';

typedef FutureOrVoidCallback = FutureOr<void> Function();

class Debouncer {
  final int milliseconds;
  FutureOrVoidCallback? action;
  int lastTime = 0;

  Debouncer({required this.milliseconds});

  Future<void> run(FutureOrVoidCallback action) async {
    this.action = action;
    lastTime = DateTime.now().millisecondsSinceEpoch;
    await Future.delayed(Duration(milliseconds: milliseconds));
    if (DateTime.now().millisecondsSinceEpoch - lastTime >= milliseconds) {
      await this.action?.call();
    } else {
      return run(action);
    }
  }
}
