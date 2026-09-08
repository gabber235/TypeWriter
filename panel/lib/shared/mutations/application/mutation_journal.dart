import "package:flutter/foundation.dart";
import "package:riverpod/riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "mutation_journal.g.dart";

@Riverpod(keepAlive: true)
MutationJournal mutationJournal(Ref ref) {
  ref.watch(userIdProvider.select((value) => value.value));
  final journal = MutationJournal();
  ref.onDispose(journal.dispose);
  return journal;
}

/// Reports submissions owned by their callers. It never constructs requests.
final class MutationJournal extends ChangeNotifier {
  final coordinator = MutationCoordinator();
  final Map<Object, MutationSubmission<Object?>> _submissions = {};
  bool _disposed = false;

  List<MutationSubmission<Object?>> get submissions =>
      List.unmodifiable(_submissions.values);

  void track<T>(MutationSubmission<T> submission) {
    if (_disposed) throw StateError("Mutation journal is disposed");
    if (_submissions.containsKey(submission.id)) return;
    for (final previous in _submissions.values.toList()) {
      if (!previous.sending &&
          previous.result is SubmissionRejected &&
          submission.resources.isNotEmpty &&
          previous.label == submission.label &&
          setEquals(previous.resources, submission.resources)) {
        dismiss(previous.id);
      }
    }
    _submissions[submission.id] = submission;
    submission.addListener(_changed);
    _changed();
  }

  void dismiss(Object id) {
    final submission = _submissions[id];
    if (submission == null ||
        submission.sending ||
        submission.result is SubmissionUncertain) {
      return;
    }
    _submissions.remove(id)?.removeListener(_changed);
    notifyListeners();
  }

  void _changed() {
    if (_disposed) return;
    notifyListeners();
  }

  @override
  void dispose() {
    _disposed = true;
    coordinator.dispose();
    for (final submission in _submissions.values) {
      submission.removeListener(_changed);
    }
    _submissions.clear();
    super.dispose();
  }
}
