import "package:typewriter_panel/typewriter_panel.dart";

/// Signals that a mutation response could not establish delivery certainty.
///
/// The submission remains available for inspection and possible replay. This
/// exception is for callers that need a feature level mutation result, not a
/// generic transport error.
final class SubmissionException<T> implements Exception {
  const SubmissionException(this.submission);

  final MutationSubmission<T> submission;

  /// Converts the unresolved submission into the editor mutation failure type.
  ///
  /// If identical replay is supported, the returned mutation exposes an
  /// explicit replay callback. A replayed confirmed or rejected response is
  /// passed to [accept]; another unresolved result remains uncertain.
  MutationUncertain toMutation(
    Future<TypedMutationResult> Function(T response) accept,
  ) {
    final result = submission.result;
    final cause = switch (result) {
      SubmissionUncertain(:final cause) => cause,
      _ => this,
    };
    return MutationUncertain(
      message: "The save result could not be confirmed",
      cause: cause,
      submissionId: submission.id,
      stackTrace: switch (result) {
        SubmissionUncertain(:final stackTrace) => stackTrace,
        _ => StackTrace.current,
      },
      replay: submission.canReplay
          ? () async {
              final replayed = await submission.run();
              return switch (replayed) {
                SubmissionConfirmed(:final value) => accept(value),
                SubmissionRejected(response: final T response) => accept(
                  response,
                ),
                _ => toMutation(accept),
              };
            }
          : null,
    );
  }

  @override
  String toString() => "The operation result could not be confirmed";
}
