import "package:typewriter_panel/typewriter_panel.dart";

final class SubmissionException<T> implements Exception {
  const SubmissionException(this.submission);

  final MutationSubmission<T> submission;

  @override
  String toString() => "The operation result could not be confirmed";
}
