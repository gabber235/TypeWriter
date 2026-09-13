import "dart:async";

import "package:flutter/foundation.dart";

/// Tracks a route access state while filtering transient states from guards.
///
/// [isPending] defines readiness. [stableStateOf] projects a state into the
/// stable facts that affect authorization. A readiness waiter completes at the
/// first nonpending state, while [reevaluation] emits only when consecutive
/// stable projections differ. Disposal completes pending waiters so guards do
/// not remain blocked when the router provider is torn down.
final class RouteAccessStateController<State, StableState extends Object> {
  RouteAccessStateController({
    required State initialState,
    required bool Function(State state) isPending,
    required StableState? Function(State state) stableStateOf,
  }) : _current = initialState,
       _isPending = isPending,
       _stableStateOf = stableStateOf,
       _lastStable = isPending(initialState)
           ? null
           : stableStateOf(initialState),
       _ready = isPending(initialState) ? Completer<void>() : null;

  final bool Function(State state) _isPending;
  final StableState? Function(State state) _stableStateOf;
  final _RouteReevaluationSignal _reevaluation = _RouteReevaluationSignal();

  State _current;
  StableState? _lastStable;
  Completer<void>? _ready;
  bool _disposed = false;

  State get current => _current;
  Listenable get reevaluation => _reevaluation;

  /// Completes when the current access state first becomes nonpending.
  Future<void> waitUntilReady() => _ready?.future ?? Future.value();

  /// Replaces the current observation and emits semantic changes when stable.
  void transitionTo(State next) {
    assert(!_disposed, "Cannot transition disposed route access state");
    if (next == _current) return;
    _current = next;

    if (_isPending(next)) {
      _ready ??= Completer<void>();
      return;
    }

    _completeReady();
    final stable = _stableStateOf(next);
    if (stable == null) return;

    final previous = _lastStable;
    _lastStable = stable;
    if (previous == null || previous == stable) return;
    _reevaluation.emit();
  }

  void _completeReady() {
    _ready?.complete();
    _ready = null;
  }

  void dispose() {
    if (_disposed) return;
    _disposed = true;
    _completeReady();
    _reevaluation.dispose();
  }
}

final class _RouteReevaluationSignal extends ChangeNotifier {
  void emit() => notifyListeners();
}
