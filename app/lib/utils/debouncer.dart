import "dart:async";

typedef FutureOrVoidValueCallback<T> = FutureOr<void> Function(T);

/// A class that provides debouncing functionality.
///
/// A debouncer is useful in situations where you only want to perform an action
/// after a certain amount of time has passed without the action being triggered
/// again. The debouncer allows you to "group" multiple triggers into one action.
///
/// The <T> is the type of the value that is passed to the callback.
class Debouncer<T> {
  Debouncer({required this.duration, required this.callback});

  /// The duration of time that must pass between the time an action is
  /// triggered and the time the action is actually executed.
  final Duration duration;

  /// The callback that is executed when the debouncer is triggered.
  final FutureOrVoidValueCallback<T> callback;

  Timer? _timer;

  T? _value;

  /// Triggers an action to be executed after [duration] has passed without
  /// the action being triggered again.
  void run(T value) {
    if (_value == value) return;
    _value = value;
    _timer?.cancel();
    _timer = Timer(duration, _trigger);
  }

  void _trigger() {
    _timer = null;
    final value = _value;
    if (value == null) return;
    callback(value);
  }

  /// Cancels the current debouncer.
  void cancel() {
    _timer?.cancel();
    _timer = null;
    _value = null;
  }
}
