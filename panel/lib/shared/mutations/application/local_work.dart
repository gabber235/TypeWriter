import "dart:async";
import "package:flutter/foundation.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod/riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "local_work.freezed.dart";
part "local_work.g.dart";

@Riverpod(keepAlive: true)
LocalWork localWork(Ref ref) {
  ref
    ..watch(userIdProvider.select((value) => value.value))
    ..watch(organizationIdProvider);
  final workspace = LocalWork();
  ref.onDispose(workspace.dispose);
  return workspace;
}

@freezed
abstract class EditorResourceKey with _$EditorResourceKey {
  const factory EditorResourceKey({
    required Object? scope,
    required Object identity,
  }) = _EditorResourceKey;
}

/// A live navigation destination. The workspace owns its lifetime.
/// Implementations notify when the current view enters or leaves the destination.
abstract class EditorDestination extends ChangeNotifier {
  bool get isCurrent;
  Future<void> open();
}

final class EditorResource {
  EditorResource(EditorTarget target, this.source)
    : targetId = target.targetId,
      label = target.label;
  final Object targetId;
  String label;
  EditorDestination? _destination;
  EditorDestination? get destination => _destination;
  set destination(EditorDestination? value) {
    if (identical(value, _destination)) return;
    if (listener case final changed?) _destination?.removeListener(changed);
    _destination?.dispose();
    _destination = value;
    if (listener case final changed?) value?.addListener(changed);
  }

  int leases = 0;
  VoidCallback? listener;
  final TransactionalEditorSource source;
}

/// Owns organization drafts, prepared submissions, reservations and save activity.
/// Organization changes discard local work, including tracking for requests already sent.
final class LocalWork extends ChangeNotifier {
  LocalWork();
  final Map<EditorResourceKey, EditorResource> _resources = {};
  final coordinator = MutationCoordinator();
  final Map<Object, MutationSubmission<Object?>> _submissions = {};
  final Map<Object, Timer> _expiry = {};

  List<MutationSubmission<Object?>> get submissions =>
      List.unmodifiable(_submissions.values);

  /// Waits before reading drafts. An uncertain predecessor keeps this queued.
  Future<MutationSubmission<T>> enqueue<T>(PendingCommit<T> pending) async {
    final reservation = await coordinator.reserve(pending.resources);
    try {
      if (_disposed) throw StateError("Mutation session ended");
      return pending.start(this, reservation);
    } on Object {
      reservation.release();
      rethrow;
    }
  }

  /// Owns one prepared commit through delivery, integration and recovery.
  /// Overlapping resources remain reserved while delivery is uncertain.
  MutationSubmission<T> start<T>(
    PreparedCommit<T> commit, {
    MutationReservation? reservation,
  }) => _start(commit, reservation);

  MutationSubmission<T> _start<T>(
    PreparedCommit<T> commit,
    MutationReservation? initialReservation,
  ) {
    if (_disposed) throw StateError("Mutation session ended");
    final existing = _submissions[commit.id];
    if (existing != null) {
      throw StateError(
        "A prepared identity is already owned; retry its submission",
      );
    }
    var reservation = initialReservation;
    final submission = MutationSubmission<T>(
      id: commit.id,
      label: commit.label,
      resources: commit.resources,
      replay: commit.replay,
      integrate: (result) async {
        try {
          await commit.integrate?.call(result);
        } finally {
          reservation?.release();
          reservation = null;
        }
      },
      onDispose: () {
        reservation?.release();
        commit.dispose?.call();
      },
      send: () async {
        reservation ??= await coordinator.reserve(commit.resources);
        return commit.send();
      },
    );
    track(submission);
    unawaited(submission.run());
    return submission;
  }

  Future<T> execute<T>(PreparedCommit<T> commit) async {
    final submission = start(commit);
    return switch (await submission.run()) {
      SubmissionConfirmed(:final value) => value,
      SubmissionRejected(response: final T response) => response,
      _ => throw SubmissionException(submission),
    };
  }

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
    _expiry.remove(id)?.cancel();
    _submissions.remove(id)?.removeListener(_changed);
    submission.dispose();
    notifyListeners();
  }

  void _changed() {
    if (_disposed) return;
    for (final submission in _submissions.values) {
      if (submission.result is SubmissionConfirmed &&
          !submission.sending &&
          submission.integrationError == null) {
        _expiry.putIfAbsent(
          submission.id,
          () => Timer(savedFeedbackDuration, () => dismiss(submission.id)),
        );
      }
    }
    notifyListeners();
  }

  bool _disposed = false;
  Map<EditorResourceKey, EditorResource> get resources =>
      Map.unmodifiable(_resources);

  EditorSource editor(EditorTarget target) {
    if (_disposed) throw StateError("Editor workspace is disposed");
    final key = target.resource.key;
    final existing = _resources[key];
    if (existing != null) {
      if (existing.source.commitPolicy != target.commitPolicy) {
        throw StateError("A resource cannot change its commit policy");
      }
      existing.label = target.label;
      existing.source.refreshTarget(target);
      return existing.source;
    }
    late final EditorResource resource;
    final source = TransactionalEditorSource(
      document: target.document,
      commitPolicy: target.commitPolicy,
      resource: target.resource,
      snapshot: target.snapshot,
      workspace: this,
    );
    resource = EditorResource(target, source);
    _resources[key] = resource;
    resource.listener = () {
      notifyListeners();
      scheduleMicrotask(() {
        if (!_disposed && resource.leases == 0 && !source.hasWork) _remove(key);
      });
    };
    source.addListener(resource.listener!);
    return source;
  }

  void retain(EditorResourceKey key) => _resources[key]!.leases++;

  void release(EditorResourceKey key) {
    final resource = _resources[key];
    if (resource == null) return;
    resource.leases--;
    if (resource.leases > 0 || resource.source.hasWork) return;
    _remove(key);
  }

  void _remove(EditorResourceKey key) {
    final resource = _resources.remove(key);
    if (resource == null) return;
    resource.destination = null;
    resource.source.removeListener(resource.listener!);
    resource.source.dispose();
  }

  @override
  void dispose() {
    _disposed = true;
    for (final resource in _resources.values) {
      resource.destination = null;
      resource.source.removeListener(resource.listener!);
      resource.source.dispose();
    }
    _resources.clear();
    coordinator.dispose();
    for (final timer in _expiry.values) {
      timer.cancel();
    }
    for (final submission in _submissions.values) {
      submission
        ..removeListener(_changed)
        ..dispose();
    }
    _expiry.clear();
    _submissions.clear();
    super.dispose();
  }
}
