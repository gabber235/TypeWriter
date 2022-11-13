import 'dart:async';

typedef FutureOrVoidCallback = FutureOr<void> Function();

class Debouncer {
  final int milliseconds;
  Timer? _timer;

  Debouncer({required this.milliseconds});

  void run(FutureOrVoidCallback action) {
    _timer?.cancel();
    _timer = Timer(Duration(milliseconds: milliseconds), () {
      _timer = null;
      action();
    });
  }
}
