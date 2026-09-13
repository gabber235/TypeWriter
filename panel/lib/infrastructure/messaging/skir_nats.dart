import "dart:async";
import "dart:typed_data";

import "package:riverpod/riverpod.dart";
import "package:skir_client/skir_client.dart";
import "package:typewriter_panel/infrastructure/messaging/nats_client.dart";
import "package:typewriter_panel/infrastructure/messaging/nats_provider.dart";
import "package:typewriter_panel/infrastructure/observability/telemetry.dart";

const _requestTimeout = Duration(seconds: 10);
const _membershipStream = "TYPEWRITER_MEMBERSHIP";

final class SequencedSnapshot<T> {
  const SequencedSnapshot({required this.sequence, required this.value});

  final int sequence;
  final T value;
}

enum SequencedEventResult { duplicate, applied, gap }

final class SequencedCollection<T> {
  SequencedSnapshot<T>? _current;

  T get value => _current!.value;
  SequencedSnapshot<T>? get snapshot => _current;

  set snapshot(SequencedSnapshot<T> value) => _current = value;

  SequencedEventResult apply({
    required int sequence,
    required T Function(T current) reduce,
  }) {
    final current = _current;
    if (current == null || sequence != current.sequence + 1) {
      if (current != null && sequence <= current.sequence) {
        return SequencedEventResult.duplicate;
      }
      return SequencedEventResult.gap;
    }
    _current = SequencedSnapshot(
      sequence: sequence,
      value: reduce(current.value),
    );
    return SequencedEventResult.applied;
  }
}

final class CollectionSequenceGap implements Exception {
  const CollectionSequenceGap({required this.expected, required this.received});

  final int expected;
  final int received;
}

extension RefNatsExtension on Ref {
  Future<TResponse> requestSkir<TResponse>(
    String subject,
    Uint8List requestBytes,
    Serializer<TResponse> serializer,
  ) async {
    final telemetry = await read(panelTelemetryProvider.future);
    final client = read(natsProvider);
    final response = await telemetry.traceNats(
      subject: subject,
      payloadSize: requestBytes.length,
      operationName: "request",
      operation: (headers) => client.request(
        subject,
        requestBytes,
        headers: headers,
        timeout: _requestTimeout,
      ),
    );
    return serializer.fromBytes(response.payload);
  }

  /// Reduces at delivery so each result reaches its listener before the next reduction.
  Stream<TData> watchRequest<TData, TResponse>({
    required String subject,
    required String listenSubject,
    required Uint8List requestBytes,
    required Serializer<TResponse> serializer,
    required TData Function(TData?, TResponse) transformer,
  }) {
    final client = watch(natsProvider);
    final responses = Stream<TResponse>.multi((controller) async {
      NatsSubscription? subscription;
      var active = true;
      Future<void>? unsubscribeOperation;

      Future<void> unsubscribe() => unsubscribeOperation ??= () async {
        active = false;
        await subscription?.unsubscribe();
      }();

      controller.onCancel = unsubscribe;
      onDispose(() => unawaited(unsubscribe()));

      try {
        subscription = await client.subscribe(listenSubject);
        if (!active) {
          await subscription.unsubscribe();
          return;
        }
        final telemetry = await read(panelTelemetryProvider.future);
        final initial = await telemetry.traceNats(
          subject: subject,
          payloadSize: requestBytes.length,
          operationName: "request",
          operation: (headers) => client.request(
            subject,
            requestBytes,
            headers: headers,
            timeout: _requestTimeout,
          ),
        );
        if (!active) return;

        controller.add(serializer.fromBytes(initial.payload));

        await for (final message in subscription.messages) {
          if (!active) return;
          controller.add(serializer.fromBytes(message.payload));
        }
      } on Object catch (error, stackTrace) {
        if (active) controller.addError(error, stackTrace);
      } finally {
        await unsubscribe();
        if (!controller.isClosed) {
          await controller.close();
        }
      }
    });
    return responses.transform(
      StreamTransformer<TResponse, TData>((stream, cancelOnError) {
        TData? previous;
        return stream
            .transform(
              StreamTransformer<TResponse, TData>.fromHandlers(
                handleData: (response, sink) {
                  try {
                    final next = transformer(previous, response);
                    previous = next;
                    sink.add(next);
                  } on Object catch (error, stackTrace) {
                    sink
                      ..addError(error, stackTrace)
                      ..close();
                  }
                },
              ),
            )
            .listen(null, cancelOnError: cancelOnError);
      }),
    );
  }

  Stream<TData> watchSequencedRequest<TData, TResponse, TEvent>({
    required String subject,
    required String eventSubject,
    required Uint8List requestBytes,
    required Serializer<TResponse> responseSerializer,
    required Serializer<TEvent> eventSerializer,
    required SequencedSnapshot<TData> Function(TResponse) snapshot,
    required int Function(TEvent) eventSequence,
    required TData Function(TData, TEvent) reduce,
    required SequencedCollection<TData> sequenceState,
  }) {
    final client = watch(natsProvider);
    return Stream<TData>.multi((controller) async {
      NatsSubscription? subscription;
      var active = true;
      Future<void>? unsubscribeOperation;

      Future<void> unsubscribe() => unsubscribeOperation ??= () async {
        active = false;
        await subscription?.unsubscribe();
      }();

      Future<SequencedSnapshot<TData>> loadSnapshot() async {
        final telemetry = await read(panelTelemetryProvider.future);
        final response = await telemetry.traceNats(
          subject: subject,
          payloadSize: requestBytes.length,
          operationName: "request",
          operation: (headers) => client.request(
            subject,
            requestBytes,
            headers: headers,
            timeout: _requestTimeout,
          ),
        );
        return snapshot(responseSerializer.fromBytes(response.payload));
      }

      controller.onCancel = unsubscribe;
      onDispose(() => unawaited(unsubscribe()));

      try {
        subscription = await client.subscribeOrdered(
          _membershipStream,
          eventSubject,
        );
        if (!active) {
          await subscription.unsubscribe();
          return;
        }

        var current = await loadSnapshot();
        if (!active) return;
        sequenceState.snapshot = current;
        controller.add(current.value);

        await for (final message in subscription.messages) {
          if (!active) return;
          final event = eventSerializer.fromBytes(message.payload);
          final sequence = eventSequence(event);
          var result = sequenceState.apply(
            sequence: sequence,
            reduce: (value) => reduce(value, event),
          );
          if (result == SequencedEventResult.duplicate) continue;

          if (result == SequencedEventResult.gap) {
            current = await loadSnapshot();
            if (!active) return;
            sequenceState.snapshot = current;
            controller.add(current.value);
            result = sequenceState.apply(
              sequence: sequence,
              reduce: (value) => reduce(value, event),
            );
            if (result == SequencedEventResult.duplicate) continue;
            if (result == SequencedEventResult.gap) {
              throw CollectionSequenceGap(
                expected: current.sequence + 1,
                received: sequence,
              );
            }
          }
          controller.add(sequenceState.value);
        }
      } on Object catch (error, stackTrace) {
        if (active) controller.addError(error, stackTrace);
      } finally {
        await unsubscribe();
        if (!controller.isClosed) await controller.close();
      }
    });
  }
}
