import "dart:async";

import "package:flutter/material.dart";
import "package:google_fonts/google_fonts.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:rive/rive.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:socket_io_client/socket_io_client.dart" as IO;
import "package:typewriter/app_router.dart";
import "package:typewriter/main.dart";
import "package:typewriter/widgets/copyable_text.dart";

part "home_page.g.dart";

@riverpod
LocalhostChecker localhostChecker(LocalhostCheckerRef ref) {
  final checker = LocalhostChecker(ref);
  ref.onDispose(checker.dispose);
  checker.initialCheck();
  return checker;
}

class LocalhostChecker {
  LocalhostChecker(this.ref);
  Timer? _timer;
  final LocalhostCheckerRef ref;

  Future<void> initialCheck() async {
    await _handleResult();
  }

  void _startCheck() {
    _timer?.cancel();
    _timer = Timer(const Duration(seconds: 3), _handleResult);
  }

  Future<void> _handleResult() async {
    final localhostOpen = await check();
    if (localhostOpen) {
      await ref.read(appRouter).replace(ConnectRoute(hostname: "localhost", port: 9092));
    } else {
      _startCheck();
    }
  }

  Future<bool> check() async {
    final completer = Completer<bool>();
    try {
      final socket = IO.io(
        "http://localhost:9092",
        IO.OptionBuilder().setTransports(["websocket"]).disableAutoConnect().disableReconnection().build(),
      );
      socket
        ..onConnect((_) {
          if (!completer.isCompleted) completer.complete(true);
          socket.dispose();
        })
        ..onConnectError((data) {
          socket.dispose();
          if (!completer.isCompleted) completer.complete(false);
        })
        ..onConnectTimeout((data) {
          if (!completer.isCompleted) completer.complete(false);
        })
        ..connect();
    } on Error catch (_) {
      if (!completer.isCompleted) completer.complete(false);
    }
    return completer.future;
  }

  void dispose() {
    _timer?.cancel();
  }
}

class HomePage extends HookConsumerWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    ref.watch(localhostCheckerProvider);
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Spacer(),
          const Expanded(
            flex: 2,
            child: RiveAnimation.asset("assets/game_character.riv"),
          ),
          const Text(
            "Your journey starts here",
            style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),
          ),
          Text(
            "Run the following command on your server to start editing",
            style: GoogleFonts.jetBrainsMono(fontSize: 20, fontWeight: FontWeight.w100, color: Colors.grey),
          ),
          const SizedBox(height: 24),
          const CopyableText(text: "/typewriter connect"),
          const Spacer(),
        ],
      ),
    );
  }
}
