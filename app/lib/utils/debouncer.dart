import "dart:async";

typedef FutureOrVoidCallback = FutureOr<void> Function();

/// A class that provides debouncing functionality.
///
/// A debouncer is useful in situations where you only want to perform an action
/// after a certain amount of time has passed without the action being triggered
/// again. The debouncer allows you to "group" multiple triggers into one action.
class Debouncer {
  Debouncer({required this.duration});

  /// The duration of time that must pass between the time an action is
  /// triggered and the time the action is actually executed.
  final Duration duration;

  Timer? _timer;

  /// Triggers an action to be executed after [duration] has passed without
  /// the action being triggered again.
  void run(FutureOrVoidCallback action) {
    _timer?.cancel();
    _timer = Timer(duration, () {
      _timer = null;
      action();
    });
  }

  /// Cancels the current debouncer.
  void cancel() {
    _timer?.cancel();
    _timer = null;
  }
}
