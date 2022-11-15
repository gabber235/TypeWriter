import "dart:async";

typedef FutureOrVoidCallback = FutureOr<void> Function();

class Debouncer {

  Debouncer({required this.milliseconds});
  final int milliseconds;
  Timer? _timer;

  void run(FutureOrVoidCallback action) {
    _timer?.cancel();
    _timer = Timer(Duration(milliseconds: milliseconds), () {
      _timer = null;
      action();
    });
  }
}
