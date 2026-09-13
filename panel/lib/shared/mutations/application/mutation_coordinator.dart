import "dart:async";

/// Reserves an entire resource set at once. Disjoint work can continue.
final class MutationCoordinator {
  final Set<Object> _held = {};
  final List<({Set<Object> resources, Completer<MutationReservation> ready})>
  _waiting = [];
  bool _disposed = false;

  Future<MutationReservation> reserve(Set<Object> resources) {
    if (_disposed) return Future.error(StateError("Mutation session ended"));
    final ready = Completer<MutationReservation>();
    _waiting.add((resources: Set.unmodifiable(resources), ready: ready));
    _drain();
    return ready.future;
  }

  void _drain() {
    final preceding = <Object>{};
    for (final request in List.of(_waiting)) {
      if (request.resources.any(_held.contains) ||
          request.resources.any(preceding.contains)) {
        preceding.addAll(request.resources);
        continue;
      }
      _waiting.remove(request);
      _held.addAll(request.resources);
      request.ready.complete(
        MutationReservation._(() {
          _held.removeAll(request.resources);
          if (!_disposed) _drain();
        }),
      );
    }
  }

  void dispose() {
    _disposed = true;
    for (final request in _waiting) {
      request.ready.completeError(StateError("Mutation session ended"));
    }
    _waiting.clear();
    _held.clear();
  }
}

final class MutationReservation {
  MutationReservation._(this._release);
  final void Function() _release;
  bool _released = false;

  void release() {
    if (_released) return;
    _released = true;
    _release();
  }
}
