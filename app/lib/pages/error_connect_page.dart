import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart" hide FilledButton;
import "package:google_fonts/google_fonts.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:rive/rive.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/widgets/components/general/copyable_text.dart";

@RoutePage()
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
          const CopyableText(text: "/typewriter connect"),
          const SizedBox(height: 24),
          const Spacer(),
        ],
      ),
    );
  }
}
