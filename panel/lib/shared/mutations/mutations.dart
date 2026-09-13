/// Shared mutation state and controls for coordinating local drafts and submissions.
///
/// Application owners keep editable values and save lifecycle state here. The
/// presentation layer consumes the resulting read models and invokes commands
/// through the exported control surfaces.
library;

export "application/local_editor_values.dart";
export "application/local_work.dart";
export "application/local_work_session.dart";
export "application/local_work_state.dart";
export "application/mutation_coordinator.dart";
export "application/mutation_submission.dart";
export "domain/mutation_activity_phase.dart";
export "domain/mutation_intent.dart";
export "domain/pending_commit.dart";
export "domain/prepared_commit.dart";
export "domain/submission_exception.dart";
export "domain/submission_result.dart";
export "presentation/mutation_activity_button.dart";
