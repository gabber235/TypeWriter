import "dart:async";

import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart" show WidgetRef;
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/compiled_content.dart"
    as compiled_wire;
import "package:typewriter_panel/typewriter_panel.dart";

part "authoring_session.freezed.dart";
part "authoring_session.g.dart";
part "authoring_session_snapshots.dart";
part "authoring_resource_repository.dart";
part "authoring_editor_resource.dart";
part "authoring_session_state.dart";
part "authoring_session_sync.dart";
part "authoring_operation_resources.dart";
part "authoring_operation_label.dart";

@riverpod
class AuthoringSession extends _$AuthoringSession
    with _AuthoringSessionSnapshots, _AuthoringSessionSync {
  @override
  AuthoringSessionState build(
    skir.RecordId organizationId,
    skir.RecordId realmId,
  ) {
    _client = ref.watch(natsProvider);
    final repository = ref
        .watch(resourceRepositoriesProvider)
        .authoring(organizationId, realmId);
    final results = repository.changes.listen(_accept);
    final invalidations = repository.invalidations.listen(
      (_) => _scheduleRefresh(),
    );
    ref
      ..onDispose(results.cancel)
      ..onDispose(invalidations.cancel);
    _address = RealmServiceAddress(
      organizationId: organizationId,
      realmId: realmId,
    );

    ref.onDispose(_dispose);
    _startOperation = _start();
    return const AuthoringSessionState();
  }

  AuthoringSessionState get snapshot => state;

  /// Fetches an authoritative snapshot of the scopes held by the caller.
  Future<void> refresh() => _refresh();

  AuthoringScopeLease acquireLibrary() =>
      _acquire(const _AuthoringScope.library());

  AuthoringScopeLease acquireBook(skir.RecordId bookId) =>
      _acquire(_AuthoringScope.book(bookId));

  AuthoringScopeLease acquirePage(skir.RecordId pageId) =>
      _acquire(_AuthoringScope.page(pageId));

  PreparedCommit<wire.ApplyAuthoringBatchResponse> prepare(
    Iterable<wire.AuthoringOperation> operations, {
    String? batchId,
  }) {
    final request = wire.ApplyAuthoringBatchRequest(
      batchId: batchId ?? uuid.v4(),
      operations: operations,
    );
    return ref.prepareSkir(
      _address.request("library.authoring.batch.apply"),
      wire.ApplyAuthoringBatchRequest.serializer.toBytes(request),
      wire.ApplyAuthoringBatchResponse.serializer,
      label: _authoringLabel(request.operations),
      classify: (response) => switch (response) {
        wire.ApplyAuthoringBatchResponse_appliedWrapper() =>
          MutationResponseDisposition.confirmed,
        wire.ApplyAuthoringBatchResponse_unknown() ||
        wire.ApplyAuthoringBatchResponse_internalErrorWrapper() =>
          MutationResponseDisposition.uncertain,
        _ => MutationResponseDisposition.rejected,
      },
      onResponse: (response) async {
        switch (response) {
          case wire.ApplyAuthoringBatchResponse_appliedWrapper(:final value):
            _accept(value);
          case wire.ApplyAuthoringBatchResponse_conflictWrapper():
            await _refresh();
          case wire.ApplyAuthoringBatchResponse_invalidWrapper() ||
              wire.ApplyAuthoringBatchResponse_internalErrorWrapper() ||
              wire.ApplyAuthoringBatchResponse_unknown():
        }
      },
      submissionId: request.batchId,
      resources: {
        for (final operation in request.operations)
          for (final resource in _operationResources(operation))
            (organizationId, realmId, resource),
      },
      replay: SubmissionReplay.identicalRequest,
    );
  }

  Future<wire.ApplyAuthoringBatchResponse> apply(
    Iterable<wire.AuthoringOperation> operations, {
    String? batchId,
  }) async {
    final commit = prepare(operations, batchId: batchId);
    try {
      return await ref.read(localWorkControllerProvider).execute(commit);
    } on Object {
      _scheduleRefresh();
      rethrow;
    }
  }

  _AuthoringScopeLease _acquire(_AuthoringScope scope) {
    final added = !_scopeCounts.containsKey(scope);
    _scopeCounts.update(scope, (count) => count + 1, ifAbsent: () => 1);
    final ready = added
        ? _scopeReadiness[scope] = _startOperation.then((_) => _refresh())
        : _scopeReadiness[scope] ?? _startOperation;
    final retention = ref.keepAlive();
    return _AuthoringScopeLease(ready, () {
      _release(scope);
      retention.close();
    });
  }

  void _release(_AuthoringScope scope) {
    final count = _scopeCounts[scope];
    if (count == null) return;
    if (count == 1) {
      _scopeCounts.remove(scope);
      _scopeReadiness.remove(scope);
    } else {
      _scopeCounts[scope] = count - 1;
    }
  }
}

@freezed
abstract class AuthoringSessionAccess with _$AuthoringSessionAccess {
  const factory AuthoringSessionAccess({
    required AuthoringSession notifier,
    required AuthoringSessionState state,
  }) = _AuthoringSessionAccess;
}

extension AuthoringSessionRef on Ref {
  AuthoringSessionAccess readAuthoringSession() {
    final organizationId = read(organizationIdProvider);
    final realmId = read(realmIdProvider);
    if (organizationId == null) throw ApiException.noOrganization();
    if (realmId == null) throw ApiException.badRequest("No realm selected");
    final provider = authoringSessionProvider(organizationId, realmId);
    return AuthoringSessionAccess(
      notifier: read(provider.notifier),
      state: read(provider),
    );
  }
}

extension AuthoringSessionWidgetRef on WidgetRef {
  AuthoringSessionAccess readAuthoringSession() {
    final organizationId = read(organizationIdProvider);
    final realmId = read(realmIdProvider);
    if (organizationId == null) throw ApiException.noOrganization();
    if (realmId == null) throw ApiException.badRequest("No realm selected");
    final provider = authoringSessionProvider(organizationId, realmId);
    return AuthoringSessionAccess(
      notifier: read(provider.notifier),
      state: read(provider),
    );
  }
}

extension AuthoringInvalidFailure on wire.AuthoringInvalid {
  String get message =>
      diagnostics.map((diagnostic) => diagnostic.message).join("; ");

  ApiException toApiException() => ApiException.badRequest(message);
}

extension AuthoringBatchFailure on wire.ApplyAuthoringBatchResponse {
  void requireApplied({required String conflictMessage}) {
    switch (this) {
      case wire.ApplyAuthoringBatchResponse_appliedWrapper():
        return;
      case wire.ApplyAuthoringBatchResponse_conflictWrapper():
        throw ApiException.conflict(conflictMessage);
      case wire.ApplyAuthoringBatchResponse_invalidWrapper() ||
          wire.ApplyAuthoringBatchResponse_internalErrorWrapper() ||
          wire.ApplyAuthoringBatchResponse_unknown():
        throw toApiException();
    }
  }

  ApiException toApiException() => switch (this) {
    wire.ApplyAuthoringBatchResponse_invalidWrapper(:final value) =>
      value.toApiException(),
    wire.ApplyAuthoringBatchResponse_internalErrorWrapper() =>
      ApiException.internalServerError(),
    wire.ApplyAuthoringBatchResponse_unknown() =>
      ApiException.unknownResponseMessage(),
    _ => throw StateError("The authoring response is not a failure"),
  };

  TypedMutationResult toMutationFailure({required String unavailableMessage}) =>
      switch (this) {
        wire.ApplyAuthoringBatchResponse_invalidWrapper(:final value) =>
          invalidMutation(value.message),
        wire.ApplyAuthoringBatchResponse_internalErrorWrapper() ||
        wire.ApplyAuthoringBatchResponse_unknown() => unavailableMutation(
          unavailableMessage,
        ),
        _ => throw StateError(
          "The authoring response is not a mutation failure",
        ),
      };
}

@riverpod
AuthoringScopeLease authoringLibraryScope(
  Ref ref,
  skir.RecordId organizationId,
  skir.RecordId realmId,
) {
  final session = authoringSessionProvider(organizationId, realmId);
  final lease = ref.read(session.notifier).acquireLibrary();
  ref.onDispose(lease.release);
  return lease;
}

@riverpod
AuthoringScopeLease authoringBookScope(
  Ref ref,
  skir.RecordId organizationId,
  skir.RecordId realmId,
  skir.RecordId bookId,
) {
  final session = authoringSessionProvider(organizationId, realmId);
  final lease = ref.read(session.notifier).acquireBook(bookId);
  ref.onDispose(lease.release);
  return lease;
}

@riverpod
AuthoringScopeLease authoringPageScope(
  Ref ref,
  skir.RecordId organizationId,
  skir.RecordId realmId,
  skir.RecordId pageId,
) {
  final session = authoringSessionProvider(organizationId, realmId);
  final lease = ref.read(session.notifier).acquirePage(pageId);
  ref.onDispose(lease.release);
  return lease;
}
