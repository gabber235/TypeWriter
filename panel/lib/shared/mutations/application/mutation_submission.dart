import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Owns delivery of one prepared request, independently of later user edits.
///
/// The supplied sender must capture an immutable request and destination.
/// Repeated attempts reuse that sender. Only protocols with verified replay
/// support may retry an uncertain result. Concurrent attempts share one Future.
final class MutationSubmission<T> extends ChangeNotifier {
  MutationSubmission({
    required this.id,
    required this.label,
    required Future<SubmissionResult<T>> Function() send,
    this.replay = SubmissionReplay.unsupported,
    Set<Object> resources = const {},
  }) : resources = Set.unmodifiable(resources),
       _send = send;

  final Object id;
  final Set<Object> resources;
  final String label;
  final SubmissionReplay replay;
  final Future<SubmissionResult<T>> Function() _send;
  Future<SubmissionResult<T>>? _active;
  SubmissionResult<T>? _result;
  bool _disposed = false;

  SubmissionResult<T>? get result => _result;
  bool get sending => _active != null;
  bool get unsettled => sending || _result is! SubmissionConfirmed<T>;
  bool get canReplay =>
      !sending &&
      (_result is SubmissionNotSubmitted<T> ||
          (_result is SubmissionUncertain<T> &&
              replay == SubmissionReplay.identicalRequest));

  Future<SubmissionResult<T>> run() {
    if (_disposed) throw StateError("Submission is disposed");
    final active = _active;
    if (active != null) return active;
    final previous = _result;
    if (previous != null && !canReplay) return Future.value(previous);
    final operation = Future<SubmissionResult<T>>.sync(_send).then(
      (result) => result,
      onError: (Object error, StackTrace stackTrace) =>
          SubmissionResult<T>.uncertain(
            message: "The operation result could not be confirmed",
            cause: error,
            stackTrace: stackTrace,
          ),
    );
    _active = operation.then((result) {
      if (!_disposed) {
        _result = result;
        _active = null;
        notifyListeners();
      }
      return result;
    });
    notifyListeners();
    return _active!;
  }

  @override
  void dispose() {
    _disposed = true;
    super.dispose();
  }
}
