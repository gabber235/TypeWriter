import "package:flutter/foundation.dart";
import "package:flutter/material.dart" hide FilledButton;
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:google_fonts/google_fonts.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:rive/rive.dart";
import "package:typewriter/app_router.dart";
import 'package:typewriter/widgets/components/general/copyable_text.dart';
import 'package:typewriter/widgets/components/general/filled_button.dart';

class HomePage extends HookConsumerWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Spacer(),
          const Expanded(
            flex: 2,
            child: RiveAnimation.asset(
              "assets/game_character.riv",
              stateMachines: ["State Machine"],
            ),
          ),
          const Text(
            "Your journey starts here",
            textAlign: TextAlign.center,
            style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),
          ),
          Text(
            "Run the following command on your server to start editing",
            textAlign: TextAlign.center,
            style: GoogleFonts.jetBrainsMono(fontSize: 20, fontWeight: FontWeight.w100, color: Colors.grey),
          ),
          const SizedBox(height: 24),
          const CopyableText(text: "/typewriter connect"),
          if (kDebugMode || kProfileMode) ...[
            const SizedBox(height: 24),
            const _DebugConnectButton(),
          ],
          const Spacer(),
        ],
      ),
    );
  }
}

/// This is a debug widget that allows you to connect quickly to a server.
/// It is only visible in debug mode.
class _DebugConnectButton extends HookConsumerWidget {
  const _DebugConnectButton({super.key});

  void connectTo(WidgetRef ref, String hostname, int port, [String token = ""]) {
    ref.read(appRouter).replaceAll([ConnectRoute(hostname: hostname, port: port, token: token)]);
  }

  Future<void> customConnectToPopup(BuildContext context, WidgetRef ref) async {
    final controller = TextEditingController();
    final url = await showDialog<String?>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text("Connect to"),
          content: TextField(
            controller: controller,
            decoration: const InputDecoration(
              hintText: "Fill in the url to connect to",
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text("Cancel"),
            ),
            FilledButton.icon(
              icon: const FaIcon(FontAwesomeIcons.link),
              label: const Text("Connect"),
              onPressed: () => Navigator.of(context).pop(controller.text),
            ),
          ],
        );
      },
    );

    if (url == null) return;

    final uri = Uri.parse(url.replaceAll("#/", ""));
    // Get the hostname and port and token from the url query parameters
    // The token is optional and the hostname can be "hostname" or "host"
    final hostname = uri.queryParameters["host"] ?? uri.queryParameters["hostname"];
    final port = int.tryParse(uri.queryParameters["port"] ?? "9092") ?? 9092;
    final token = uri.queryParameters["token"] ?? "";

    debugPrint("Connecting to $hostname:$port with token $token");

    if (hostname == null) {
      return;
    }

    connectTo(ref, hostname, port, token);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        FilledButton.icon(
          color: Colors.green,
          icon: const Icon(FontAwesomeIcons.house),
          label: const Text("Connect Home"),
          onPressed: () => connectTo(ref, "localhost", 9092),
        ),
        const SizedBox(width: 24),
        FilledButton.icon(
          icon: const Icon(FontAwesomeIcons.connectdevelop),
          label: const Text("Connect Custom"),
          onPressed: () => customConnectToPopup(context, ref),
        ),
      ],
    );
  }
}
