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
  });
  final AuthoringSessionState initial;
  @override
  AuthoringSessionState build(
    skir.RecordId organizationId,
    skir.RecordId realmId,
  ) => initial;
  @override
  Stream<AuthoringSessionState> watchSnapshots(
    AuthoringScopeLease Function() acquire,
  ) => const Stream.empty();
  @override
  Future<wire.ApplyAuthoringBatchResponse> apply(
    Iterable<wire.AuthoringOperation> operations, {
    String? batchId,
  }) async {
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
}) => [
  authoringSessionProvider.overrideWith(
    () => AuthoringSessionMock(
      initial: initial ?? const AuthoringSessionState(sequence: 1),
    ),
  ),
];
