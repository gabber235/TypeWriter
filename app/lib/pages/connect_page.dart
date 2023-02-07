import "dart:async";

import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart" hide Page;
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:google_fonts/google_fonts.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:rive/rive.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/widgets/components/general/text_scroller.dart";

class ConnectPage extends HookConsumerWidget {
  const ConnectPage({
    @QueryParam("host") this.hostname = "",
    @QueryParam() this.port = 9092,
    @QueryParam() this.token = "",
    super.key,
  });

  final String hostname;
  final int port;
  final String token;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // If the hostname is empty, we want to go back to the home page.
    useDelayedExecution(() {
      if (hostname.isEmpty) {
        ref.read(appRouter).replaceAll([const HomeRoute()]);
        return;
      }
    });

    // We want to wait a second before we connect to the server.
    // This is to give the user a chance to read the text.
    useEffect(
      () {
        final timer = Timer(1.seconds, () {
          ref.read(socketProvider.notifier).init(hostname, port, token.isEmpty ? null : token);
        });
        return timer.cancel;
      },
      [],
    );

    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Spacer(),
          const Expanded(
            flex: 8,
            child: RiveAnimation.asset(
              "assets/tour.riv",
              stateMachines: ["state_machine"],
            ),
          ),
          const SizedBox(height: 24),
          const Text(
            "Waiting for connection",
            style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),
          ),
          ConnectionScroller(
            style: GoogleFonts.jetBrainsMono(fontSize: 20, color: Colors.grey),
          ),
          const SizedBox(height: 24),
          const Spacer(),
        ],
      ),
    );
  }
}

class ConnectionScroller extends HookWidget {
  const ConnectionScroller({this.style, super.key});

  final TextStyle? style;

  @override
  Widget build(BuildContext context) {
    return TextScroller(
      texts: [
        "Establishing interstellar connection",
        "Tuning communication frequency",
        "Initiating communication protocol",
        "Negotiating connection parameters",
        "Analyzing network traffic",
        "Establishing telepathic link",
        "Activating quantum communication",
        "Setting up virtual private connection",
        "Checking for network interference",
        "Hacking into the Matrix",
        "Summoning the interdimensional portal",
        "Opening the gateway to the astral plane",
        "Establishing connection to the other side",
        "Connecting to the cosmic mind",
        "Contacting extraterrestrial intelligence",
        "Dialing up the time-space continuum",
        "Downloading thoughts from the future",
        "Establishing link to parallel universe",
        "Establishing link to the universal consciousness",
        "Tuning into the cosmic frequency",
        "Initiating intergalactic communication",
        "Bending the fabric of reality",
        "Syncing with the cosmic clock",
        "Activating the trans-dimensional relay",
        "Establishing telekinetic connection",
        "Channeling the universal energy",
        "Unlocking the secrets of the universe",
        "Contacting the all-seeing eye",
        "Teleporting through time and space",
        "Tuning into the higher dimensions",
        "Connecting to the great beyond",
        "Downloading knowledge from the Akashic Records",
        "Establishing a psychic link",
        "Activating the cosmic gateway",
        "Syncing with the universe's frequency",
        "Tuning into the cosmic vibration",
        "Connecting to the quantum field",
        "Establishing a connection to the divine",
        "Channeling the universal wisdom",
      ]..shuffle(),
      style: style,
    );
  }
}
