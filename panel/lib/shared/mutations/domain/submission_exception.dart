import "package:typewriter_panel/typewriter_panel.dart";

final class SubmissionException<T> implements Exception {
  const SubmissionException(this.submission);

  final MutationSubmission<T> submission;

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
