import "dart:async";

import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "realm_editor_catalog_cache.freezed.dart";

/// Observable lifecycle of the cached realm editor catalog.
///
/// Loading and unavailable states retain the last snapshot when one exists.
/// Consumers can therefore keep rendering known definitions while showing the
/// current recovery state.
@freezed
sealed class RealmEditorCatalogState with _$RealmEditorCatalogState {
  const RealmEditorCatalogState._();

  const factory RealmEditorCatalogState.loading([
    RealmEditorCatalogSnapshot? previous,
  ]) = RealmEditorCatalogLoading;
  const factory RealmEditorCatalogState.ready(
    RealmEditorCatalogSnapshot value,
  ) = RealmEditorCatalogReady;
  const factory RealmEditorCatalogState.unavailable(
    List<TypeDiagnostic> diagnostics, {
    RealmEditorCatalogSnapshot? previous,
  }) = RealmEditorCatalogUnavailable;

  RealmEditorCatalogSnapshot? get snapshot => switch (this) {
    RealmEditorCatalogLoading(:final previous) => previous,
    RealmEditorCatalogReady(:final value) => value,
    RealmEditorCatalogUnavailable(:final previous) => previous,
  };
}

/// Keeps one catalog request in the cache's merged demand until [close].
///
/// The provider that acquired the lease owns its release. Releasing is
/// idempotent, so disposal paths can safely call it more than once.
final class RealmEditorCatalogLease {
  RealmEditorCatalogLease._(this._close);

  final void Function() _close;
  var _closed = false;

  void close() {
    if (_closed) return;
    _closed = true;
    _close();
  }
}

/// Owns one realm catalog snapshot, its invalidation watch, and consumer leases.
///
/// Each lease contributes requested types, presentations, or subtype queries.
/// The cache merges those requests into fetches, rejects stale responses after
/// invalidation or disposal, retries generation mismatches, and publishes a
/// previous snapshot with diagnostics when recovery fails. It is created by
/// the online realm provider and must be disposed with that provider.
final class RealmEditorCatalogCache {
  RealmEditorCatalogCache({required this.source, required this.route});

  final RealmEditorCatalogSource source;
  final RealmEditorCatalogRoute route;
  final StreamController<RealmEditorCatalogState> _states =
      StreamController.broadcast();

  StreamSubscription<RealmEditorCatalogWatchEvent>? _watchSubscription;
  RealmEditorCatalogState _state = const RealmEditorCatalogLoading();
  var _epoch = 0;
  var _started = false;
  var _disposed = false;
  var _nextLeaseId = 0;
  final Map<int, RealmEditorCatalogRequest> _requests = {};

  RealmEditorCatalogRequest get _requested => _requests.values.fold(
    RealmEditorCatalogRequest(),
    (combined, request) => combined.merge(request),
  );

  Stream<RealmEditorCatalogState> get states => Stream.multi((controller) {
    controller.add(_state);
    final subscription = _states.stream.listen(
      controller.add,
      onError: controller.addError,
      onDone: controller.close,
    );
    controller.onCancel = subscription.cancel;
  }, isBroadcast: true);

  /// Starts the invalidation watch and schedules the initial fetch once.
  void start() {
    if (_started || _disposed) return;
    _started = true;
    _watchSubscription = source
        .watchInvalidations(route)
        .listen(
          _handleWatchEvent,
          onError: _handleWatchError,
          onDone: _handleWatchDone,
        );
    unawaited(_refresh());
  }

  /// Retains [request] in the merged fetch scope until the returned lease closes.
  ///
  /// A new request triggers a refresh only after the cache has started. The
  /// request is not removed until its consumer releases the lease.
  RealmEditorCatalogLease acquire(RealmEditorCatalogRequest request) {
    if (_disposed) return RealmEditorCatalogLease._(() {});
    final previous = _requested;
    final id = _nextLeaseId++;
    _requests[id] = request;
    if (_started && previous != _requested) {
      unawaited(_refresh(expectedGeneration: _state.snapshot?.generation));
    }
    return RealmEditorCatalogLease._(() => _requests.remove(id));
  }

  /// Reconciles current demand against the latest known catalog generation.
  Future<void> refresh() =>
      _refresh(expectedGeneration: _state.snapshot?.generation);

  /// Stops watches and prevents pending fetches from publishing state.
  Future<void> dispose() async {
    if (_disposed) return;
    _disposed = true;
    _epoch++;
    await _watchSubscription?.cancel();
    await _states.close();
  }

  void _handleWatchEvent(RealmEditorCatalogWatchEvent event) {
    switch (event) {
      case RealmEditorCatalogInvalidated(:final generation):
        _emit(const RealmEditorCatalogLoading());
        unawaited(_refresh(expectedGeneration: generation));
      case RealmEditorCatalogWatchUnavailable(:final diagnostics):
        _epoch++;
        _emitUnavailable(diagnostics);
    }
  }

  void _handleWatchError(Object error, StackTrace stackTrace) {
    _epoch++;
    _emitUnavailable([
      realmEditorCatalogUnavailableDiagnostic(
        "Realm editor catalog invalidation watch failed: $error",
      ),
    ]);
  }

  void _handleWatchDone() {
    _epoch++;
    _emitUnavailable([
      realmEditorCatalogUnavailableDiagnostic(
        "Realm editor catalog invalidation watch closed",
      ),
    ]);
  }

  Future<void> _refresh({CatalogGeneration? expectedGeneration}) async {
    final epoch = ++_epoch;
    _emit(RealmEditorCatalogLoading(_state.snapshot));
    final first = await _fetch(expectedGeneration);
    if (!_isCurrent(epoch)) return;
    if (first case RealmEditorCatalogGenerationMismatch(
      :final currentGeneration,
    )) {
      _emit(const RealmEditorCatalogLoading());
      final retry = await _fetch(currentGeneration);
      if (!_isCurrent(epoch)) return;
      _applyFetchResult(retry);
      return;
    }
    _applyFetchResult(first);
  }

  Future<RealmEditorCatalogFetchResult> _fetch(
    CatalogGeneration? generation,
  ) async {
    try {
      return await source.fetch(
        route,
        _requested,
        expectedGeneration: generation,
      );
    } on Object catch (error) {
      return RealmEditorCatalogFetchUnavailable([
        realmEditorCatalogUnavailableDiagnostic(
          "Realm editor catalog fetch failed: $error",
        ),
      ]);
    }
  }

  void _applyFetchResult(RealmEditorCatalogFetchResult result) {
    switch (result) {
      case RealmEditorCatalogFetched(:final snapshot):
        _emit(RealmEditorCatalogReady(snapshot));
      case RealmEditorCatalogFetchUnavailable(:final diagnostics):
        _emitUnavailable(diagnostics);
      case RealmEditorCatalogGenerationMismatch(:final currentGeneration):
        _emitUnavailable([
          TypeDiagnostic(
            code: TypeDiagnosticCode.invalidRevision,
            message:
                "Realm editor catalog generation remained inconsistent at $currentGeneration",
            pathPresent: false,
          ),
        ]);
    }
  }

  void _emitUnavailable(Iterable<TypeDiagnostic> diagnostics) {
    _emit(
      RealmEditorCatalogState.unavailable(
        diagnostics.toList(growable: false),
        previous: _state.snapshot,
      ),
    );
  }

  void _emit(RealmEditorCatalogState state) {
    if (_disposed) return;
    _state = state;
    _states.add(state);
  }

  bool _isCurrent(int epoch) => !_disposed && epoch == _epoch;
}
