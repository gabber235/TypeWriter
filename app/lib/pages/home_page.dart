import "package:flutter/foundation.dart";
import "package:flutter/material.dart";
import "package:google_fonts/google_fonts.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:rive/rive.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/main.dart";
import "package:typewriter/widgets/copyable_text.dart";

class HomePage extends HookConsumerWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // If we are in debug mode we cant use the server command as we debug in the application rather than the web version
    // Hence we assume that we want to connect to localhost if we are in debug mode.
    // Normally we should never use an if statement before a hook. Only this will get compiled out in release mode.
    if (kDebugMode) {
      useDelayedExecution(() => ref.read(appRouter).replaceAll([ConnectRoute(hostname: "127.0.0.1", port: 9092)]));
    }

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
