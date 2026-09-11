import "dart:async";

// ignore: implementation_imports
import "package:riverpod/src/framework.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

class AuthoringSessionMock extends AuthoringSession {
  AuthoringSessionMock({
    this.initial = const AuthoringSessionState(sequence: 1),
    this.onApply,
  });
  final AuthoringSessionState initial;
  final FutureOr<void> Function(List<wire.AuthoringOperation> operations)?
  onApply;

  @override
  AuthoringSessionState build(
    skir.RecordId organizationId,
    skir.RecordId realmId,
  ) => initial;

  @override
  Future<wire.ApplyAuthoringBatchResponse> apply(
    Iterable<wire.AuthoringOperation> operations, {
    String? batchId,
  }) async {
    await onApply?.call(List.unmodifiable(operations));
    state = state.copyWith(sequence: (state.sequence ?? 0) + 1);
    return wire.ApplyAuthoringBatchResponse.createApplied(
      sequence: state.sequence!,
      batchId: batchId ?? "fixture",
      changes: const [],
      indirectlyAffectedResources: const [],
    );
  }
}

List<Override> authoringSessionMockOverrides({
  AuthoringSessionState? initial,
  FutureOr<void> Function(List<wire.AuthoringOperation> operations)? onApply,
}) => [
  authoringSessionProvider.overrideWith2(
    (_) => AuthoringSessionMock(
      initial: initial ?? const AuthoringSessionState(sequence: 1),
      onApply: onApply,
    ),
  ),
];
