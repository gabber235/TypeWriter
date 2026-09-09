import "package:typewriter_panel/typewriter_panel.dart";

/// Successful feedback remains visible briefly; unresolved work never expires.
const savedFeedbackDuration = Duration(seconds: 5);

enum MutationActivityPhase {
  idle,
  pending,
  saving,
  savingWithAttention,
  needsAttention,
  needsInput,
  drafts,
  saved;

  bool get isSaving => this == saving || this == savingWithAttention;

  static MutationActivityPhase resolve(
    Iterable<MutationSubmission<Object?>> submissions,
    Iterable<EditorResource> drafts,
  ) {
    final phases = drafts
        .map((entry) => entry.source.saveState(DataPath.root).phase)
        .toSet();
    final saving =
        submissions.any((entry) => entry.sending) ||
        phases.contains(EditorSavePhase.saving);
    final attention =
        submissions.any(
          (entry) =>
              !entry.sending &&
              entry.result != null &&
              (entry.result is! SubmissionConfirmed ||
                  entry.integrationError != null),
        ) ||
        phases.any(
          {
            EditorSavePhase.failed,
            EditorSavePhase.conflict,
            EditorSavePhase.uncertain,
            EditorSavePhase.repeatedContention,
            EditorSavePhase.deletedElsewhere,
          }.contains,
        );
    if (attention) return saving ? savingWithAttention : needsAttention;
    if (saving) return MutationActivityPhase.saving;
    if (drafts.any((entry) => entry.source.draftDiagnostics.isNotEmpty)) {
      return needsInput;
    }
    if (drafts.isNotEmpty) return MutationActivityPhase.drafts;
    if (submissions.any((entry) => entry.result == null)) return pending;
    return submissions.isEmpty ? idle : saved;
  }
}
