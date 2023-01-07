import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:google_fonts/google_fonts.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:rive/rive.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/widgets/copyable_text.dart";
import "package:typewriter/widgets/filled_button.dart";

class ErrorConnectPage extends HookConsumerWidget {
  const ErrorConnectPage({required this.hostname, required this.port, this.token, super.key});

  final String hostname;
  final int port;
  final String? token;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    useDelayedExecution(() {
      // Make sure the socket gets cleaned up
      ref.invalidate(socketProvider);
    });

    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Spacer(),
          const Expanded(
            flex: 6,
            child: MouseRegion(
              cursor: SystemMouseCursors.zoomIn,
              child: RiveAnimation.asset(
                "assets/robot_island.riv",
                stateMachines: ["Motion"],
              ),
            ),
          ),
          const SizedBox(height: 24),
          const Text(
            "Communication error",
            style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold, color: Colors.red),
          ),
          Text(
            "There was an error while communicating to the server.\nPlease check your connection and try again.",
            textAlign: TextAlign.center,
            style: GoogleFonts.jetBrainsMono(fontSize: 20, color: Colors.grey),
          ),
          const SizedBox(height: 24),
          // If the token is null than we can always try to reconnect
          if (token == null)
            FilledButton.icon(
              icon: const Icon(FontAwesomeIcons.repeat),
              label: const Text("Reconnect"),
              onPressed: () {
                ref.read(appRouter).replaceAll([ConnectRoute(hostname: hostname, port: port, token: "")]);
              },
            ),

          // If it is not null and we got an error, we want the user to start a new session so we guide them to run the command again
          if (token != null) const CopyableText(text: "/typewriter connect"),
          const SizedBox(height: 24),
          const Spacer(),
        ],
      ),
    );
  }
}
