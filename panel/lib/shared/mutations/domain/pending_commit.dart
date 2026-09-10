import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// A transaction waiting to capture its request from current session drafts.
/// Resource membership is fixed when queued; values are captured at preparation.
final class PendingCommit<T> {
  PendingCommit({required Set<Object> resources, required this.prepare})
    : resources = Set.unmodifiable(resources);

  /// Keeps T bound when heterogeneous transactions are collected together.
  MutationSubmission<T> start(
    LocalWorkSession workspace,
    MutationReservation reservation,
  ) {
    PreparedCommit<T>? captured;
    try {
      captured = prepare();
      if (!setEquals(captured.resources, resources)) {
        throw StateError("Preparation changed the reserved participants");
      }
      return workspace.start<T>(captured, reservation: reservation);
    } on Object {
      captured?.dispose?.call();
      rethrow;
    }
  }

  final Set<Object> resources;
  final PreparedCommit<T> Function() prepare;
}
