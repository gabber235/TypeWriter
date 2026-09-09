import "dart:async";
import "dart:typed_data";

import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";

final _refProvider = Provider<Ref>((ref) => ref);

void main() {
  test(
    "paused watches reduce against the state received by their listener",
    () async {
      final payload = skir.GetSentinelCredentialsResponse.serializer.toBytes(
        skir.GetSentinelCredentialsResponse.createSuccess(
          jwt: "jwt",
          seed: "seed",
        ),
      );
      final nats = FakeNatsClient()..registerHandler("watch", (_) => payload);
      final container = ProviderContainer.test(
        overrides: [
          natsProvider.overrideWithValue(nats),
          panelTelemetryProvider.overrideWithValue(
            const AsyncData(NoopPanelTelemetry()),
          ),
        ],
      );
      addTearDown(nats.dispose);
      addTearDown(container.dispose);
      var current = 0;
      final values = <int>[];
      final initial = Completer<void>();
      final completed = Completer<void>();
      final stream = container
          .read(_refProvider)
          .watchRequest(
            subject: "watch",
            listenSubject: "events",
            requestBytes: Uint8List(0),
            serializer: skir.GetSentinelCredentialsResponse.serializer,
            transformer: (_, _) => current + 1,
          );
      final listener = stream.listen((value) {
        current = value;
        values.add(value);
        if (values.length == 1) initial.complete();
        if (values.length == 3) completed.complete();
      });
      addTearDown(listener.cancel);
      await initial.future.timeout(const Duration(seconds: 2));
      listener.pause();
      nats
        ..emitMessageOnSubject("events", payload)
        ..emitMessageOnSubject("events", payload);
      await pumpEventQueue();
      expect(values, [1]);
      listener.resume();
      await completed.future.timeout(const Duration(seconds: 2));
      expect(values, [1, 2, 3]);
      await listener.cancel();
      expect(nats.subscriptionSubjects, isEmpty);
    },
  );
}
