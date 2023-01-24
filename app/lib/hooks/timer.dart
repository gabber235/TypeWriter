import "dart:async";

import "package:flutter_hooks/flutter_hooks.dart";

void useTimer(Duration duration, Function runner, {bool repeat = true}) {
  useEffect(
    () {
      Timer timer;
      void callback(Timer timer) {
        runner();
        if (!repeat) {
          timer.cancel();
        }
      }

      timer = Timer.periodic(duration, callback);
      return () => timer.cancel();
    },
    [duration, repeat],
  );
}
