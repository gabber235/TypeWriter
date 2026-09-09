import "dart:async";
import "dart:typed_data";

import "package:riverpod/riverpod.dart";
import "package:skir_client/skir_client.dart";
import "package:typewriter_panel/typewriter_panel.dart";

enum MutationResponseDisposition { confirmed, rejected, uncertain }

extension RefSkirMutation on Ref {
  PreparedCommit<TResponse> prepareSkir<TResponse>(
    String subject,
    Uint8List requestBytes,
    Serializer<TResponse> serializer, {
    required String label,
    required MutationResponseDisposition Function(TResponse) classify,
    Future<void> Function(TResponse)? onResponse,
    String? submissionId,
    Set<Object> resources = const {},
    SubmissionReplay replay = SubmissionReplay.unsupported,
  }) {
    final retention = keepAlive();
    return SkirMutationClient(
          () => read(natsProvider),
          () => read(panelTelemetryProvider.future),
        )
        .prepare(
          subject,
          requestBytes,
          serializer,
          label: label,
          classify: classify,
          onResponse: onResponse,
          submissionId: submissionId,
          resources: resources,
          replay: replay,
        )
        .copyWith(dispose: retention.close);
  }

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
  }) => read(localWorkProvider).execute(
    prepareSkir(
      subject,
      requestBytes,
      serializer,
      label: label,
      classify: classify,
      onResponse: onResponse,
      submissionId: submissionId,
      resources: resources,
      replay: replay,
    ),
  );
}

/// Resolves the current connection for each attempt while preserving captured request bytes.
/// Dependencies belong to the originating scope and must reject access after disposal.
final class SkirMutationClient {
  const SkirMutationClient(this._client, this._telemetry);
  final NatsClient Function() _client;
  final Future<PanelTelemetry> Function() _telemetry;

  Future<T> request<T>(
    FutureOr<String> subject,
    Uint8List bytes,
    Serializer<T> serializer,
  ) async {
    final destination = await subject;
    final telemetry = await _telemetry();
    final client = _client();
    final response = await telemetry.traceNats(
      subject: destination,
      payloadSize: bytes.length,
      operationName: "request",
      operation: (headers) => client.request(
        destination,
        bytes,
        headers: headers,
        timeout: const Duration(seconds: 10),
      ),
    );
    return serializer.fromBytes(response.payload);
  }

  PreparedCommit<TResponse> prepare<TResponse>(
    FutureOr<String> subject,
    Uint8List requestBytes,
    Serializer<TResponse> serializer, {
    required String label,
    required MutationResponseDisposition Function(TResponse) classify,
    Future<void> Function(TResponse)? onResponse,
    String? submissionId,
    Set<Object> resources = const {},
    SubmissionReplay replay = SubmissionReplay.unsupported,
  }) {
    final bytes = Uint8List.fromList(requestBytes).asUnmodifiableView();
    return PreparedCommit<TResponse>(
      id: submissionId ?? uuid.v4(),
      label: label,
      replay: replay,
      resources: resources,
      integrate: (result) async {
        switch (result) {
          case SubmissionConfirmed(:final value):
            await onResponse?.call(value);
          case SubmissionRejected(response: final TResponse response):
            await onResponse?.call(response);
          case SubmissionRejected() ||
              SubmissionNotSubmitted() ||
              SubmissionUncertain():
        }
      },
      send: () async {
        final PanelTelemetry telemetry;
        final NatsClient client;
        final String destination;
        try {
          destination = await subject;
          telemetry = await _telemetry();
          client = _client();
        } on Object catch (error) {
          return SubmissionResult.notSubmitted(
            message: "The request could not be prepared",
            cause: error,
          );
        }
        final response = await telemetry.traceNats(
          subject: destination,
          payloadSize: bytes.length,
          operationName: "request",
          operation: (headers) => client.request(
            destination,
            bytes,
            headers: headers,
            timeout: const Duration(seconds: 10),
          ),
        );
        final value = serializer.fromBytes(response.payload);
        final disposition = classify(value);
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
  }
}
