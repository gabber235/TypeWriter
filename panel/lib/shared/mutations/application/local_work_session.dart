import "dart:async";

import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "local_work_snapshot.dart";

/// Commands and live editor access owned by the current local work scope.
abstract interface class LocalWorkCommands {
  Map<EditorResourceKey, EditorResource> get resources;
  List<MutationSubmission<Object?>> get submissions;
  Future<MutationSubmission<T>> enqueue<T>(PendingCommit<T> pending);
  MutationSubmission<T> start<T>(
    PreparedCommit<T> commit, {
    MutationReservation? reservation,
  });
  Future<T> execute<T>(PreparedCommit<T> commit);
  void track<T>(MutationSubmission<T> submission);
  void dismiss(Object id);
  EditorSource editor(EditorTarget target);
  void retain(EditorResourceKey key);
  void release(EditorResourceKey key);
  Future<void> retry(Object id);
  void discard(EditorResourceKey key);
  Future<void> open(EditorResourceKey key);
  EditorSource? source(EditorResourceKey key);
}

final class EditorResource {
  EditorResource(EditorTarget target, this.source)
    : targetId = target.targetId,
      label = target.label;

  final Object targetId;
  String label;
  EditorDestination? _destination;
  EditorDestination? get destination => _destination;
  LocalWorkDestinationState destinationState =
      LocalWorkDestinationState.unavailable;
  int leases = 0;
  VoidCallback? listener;
  VoidCallback? _destinationListener;
  final TransactionalEditorSource source;

  set destination(EditorDestination? value) {
    if (identical(value, _destination)) return;
    if (_destinationListener case final changed?) {
      _destination?.removeListener(changed);
    }
    _destination?.dispose();
    _destination = value;
    if (value == null) {
      destinationState = LocalWorkDestinationState.unavailable;
      _destinationListener = null;
    } else {
      void changed() {
        destinationState = value.isCurrent
            ? LocalWorkDestinationState.current
            : LocalWorkDestinationState.available;
        listener?.call();
      }

      _destinationListener = changed;
      value.addListener(changed);
      destinationState = value.isCurrent
          ? LocalWorkDestinationState.current
          : LocalWorkDestinationState.available;
    }
    listener?.call();
  }
}

/// Owns mutable editors and submissions for one user and organization scope.
final class LocalWorkSession implements LocalWorkCommands {
  LocalWorkSession();

  final Map<EditorResourceKey, EditorResource> _resources = {};
  final MutationCoordinator coordinator = MutationCoordinator();
  final Map<Object, MutationSubmission<Object?>> _submissions = {};
  final Map<Object, Timer> _expiry = {};
  final StreamController<LocalWorkState> _changes =
      StreamController.broadcast();
  LocalWorkState _state = const LocalWorkState();
  bool _disposed = false;

  @override
  Map<EditorResourceKey, EditorResource> get resources =>
      Map.unmodifiable(_resources);
  @override
  List<MutationSubmission<Object?>> get submissions =>
      List.unmodifiable(_submissions.values);
  LocalWorkState get state => _state;
  Stream<LocalWorkState> get changes => _changes.stream;

  @override
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

  @override
  MutationSubmission<T> start<T>(
    PreparedCommit<T> commit, {
    MutationReservation? reservation,
  }) => _start(commit, reservation);

  MutationSubmission<T> _start<T>(
    PreparedCommit<T> commit,
    MutationReservation? initialReservation,
  ) {
    if (_disposed) throw StateError("Mutation session ended");
    if (_submissions.containsKey(commit.id)) {
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

  @override
  Future<T> execute<T>(PreparedCommit<T> commit) async {
    final submission = start(commit);
    return switch (await submission.run()) {
      SubmissionConfirmed(:final value) => value,
      SubmissionRejected(response: final T response) => response,
      _ => throw SubmissionException(submission),
    };
  }

  @override
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
    submission.addListener(_publish);
    _publish();
  }

  @override
  void dismiss(Object id) {
    final submission = _submissions[id];
    if (submission == null ||
        submission.sending ||
        submission.result is SubmissionUncertain) {
      return;
    }
    _expiry.remove(id)?.cancel();
    _submissions.remove(id)?.removeListener(_publish);
    submission.dispose();
    _publish();
  }

  @override
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
      _publish();
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
      _publish();
      scheduleMicrotask(() {
        if (!_disposed && resource.leases == 0 && !source.hasWork) _remove(key);
      });
    };
    source.addListener(resource.listener!);
    return source;
  }

  @override
  void retain(EditorResourceKey key) => _resources[key]!.leases++;

  @override
  void release(EditorResourceKey key) {
    final resource = _resources[key];
    if (resource == null) return;
    resource.leases--;
    if (resource.leases > 0 || resource.source.hasWork) return;
    _remove(key);
  }

  @override
  Future<void> retry(Object id) async {
    final submission = _submissions[id];
    if (submission == null) return;
    final owners = _resources.values.where(
      (resource) => resource.source.saveState(DataPath.root).submissionId == id,
    );
    if (owners.isNotEmpty) {
      await owners.first.source.flush();
      return;
    }
    await submission.run();
  }

  @override
  void discard(EditorResourceKey key) => _resources[key]?.source.discardDraft();

  @override
  Future<void> open(EditorResourceKey key) async =>
      _resources[key]?.destination?.open();

  @override
  EditorSource? source(EditorResourceKey key) => _resources[key]?.source;

  void _remove(EditorResourceKey key) {
    final resource = _resources.remove(key);
    if (resource == null) return;
    resource.source.removeListener(resource.listener!);
    resource
      ..listener = null
      ..destination = null;
    resource.source.dispose();
    _publish();
  }

  void dispose() {
    if (_disposed) return;
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
        ..removeListener(_publish)
        ..dispose();
    }
    _expiry.clear();
    _submissions.clear();
    unawaited(_changes.close());
  }
}
