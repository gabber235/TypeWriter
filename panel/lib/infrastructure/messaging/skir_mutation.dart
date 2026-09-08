import "dart:typed_data";

import "package:riverpod/riverpod.dart";
import "package:skir_client/skir_client.dart";
import "package:typewriter_panel/typewriter_panel.dart";

enum MutationResponseDisposition { confirmed, rejected, uncertain }

extension RefSkirMutation on Ref {
  Future<TResponse> mutateSkir<TResponse>(
    String subject,
    Uint8List requestBytes,
    Serializer<TResponse> serializer, {
    required String label,
    required MutationResponseDisposition Function(TResponse) classify,
    Future<void> Function(TResponse)? onResponse,
    String? submissionId,
    Set<Object> resources = const {},
    SubmissionReplay replay = SubmissionReplay.unsupported,
  }) async {
    final client = read(natsProvider);
    final telemetryFuture = read(panelTelemetryProvider.future);
    final bytes = Uint8List.fromList(requestBytes).asUnmodifiableView();
    final journal = read(mutationJournalProvider);
    final telemetry = await telemetryFuture;
    MutationReservation? reservation;
    final submission = MutationSubmission<TResponse>(
      id: submissionId ?? uuid.v4(),
      label: label,
      replay: replay,
      resources: resources,
      send: () async {
        reservation ??= await journal.coordinator.reserve(resources);
        final response = await telemetry.traceNats(
          subject: subject,
          payloadSize: bytes.length,
          operationName: "request",
          operation: (headers) => client.request(
            subject,
            bytes,
            headers: headers,
            timeout: const Duration(seconds: 10),
          ),
        );
        final value = serializer.fromBytes(response.payload);
        await onResponse?.call(value);
        final disposition = classify(value);
        if (disposition != MutationResponseDisposition.uncertain) {
          reservation!.release();
          reservation = null;
        }
        return switch (disposition) {
          MutationResponseDisposition.confirmed => SubmissionResult.confirmed(
            value,
          ),
          MutationResponseDisposition.rejected => SubmissionResult.rejected(
            message: "The operation was rejected",
            response: value,
          ),
          MutationResponseDisposition.uncertain => SubmissionResult.uncertain(
            message: "The server could not confirm the operation",
            cause: StateError("Unconfirmed mutation response"),
            stackTrace: StackTrace.current,
          ),
        };
      },
    );
    journal.track(submission);
    return switch (await submission.run()) {
      SubmissionConfirmed(:final value) => value,
      SubmissionRejected(response: final TResponse response) => response,
      _ => throw SubmissionException(submission),
    };
  }
}
