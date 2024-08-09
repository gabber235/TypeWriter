import "dart:async";

import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart" hide FilledButton;
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:rive/rive.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/utils/fonts.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/components/general/copyable_text.dart";
import "package:typewriter/widgets/components/general/filled_button.dart";
import "package:typewriter/widgets/components/general/iconify.dart";

@RoutePage()
class HomePage extends HookConsumerWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return const Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Spacer(),
          Expanded(
            flex: 2,
            child: RiveAnimation.asset(
              "assets/game_character.riv",
              stateMachines: ["State Machine"],
            ),
          ),
          Text(
            "Your journey starts here",
            textAlign: TextAlign.center,
            style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),
          ),
          Text(
            "Run the following command on your server to start editing",
            textAlign: TextAlign.center,
            style: TextStyle(
              fontSize: 20,
              color: Colors.grey,
              fontVariations: [thinWeight],
            ),
          ),
          SizedBox(height: 24),
          CopyableText(text: "/typewriter connect"),
          SizedBox(height: 24),
          _ConnectButtons(),
          SizedBox(height: 24),
          Spacer(),
        ],
      ),
    );
  }
}

class _ConnectButtons extends HookConsumerWidget {
  const _ConnectButtons();

  void connectTo(
    WidgetRef ref,
    String hostname,
    int port, [
    String token = "",
  ]) {
    ref.read(appRouter).replaceAll(
      [ConnectRoute(hostname: hostname, port: port, token: token)],
    );
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
              icon: const Iconify(TWIcons.externalLink),
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
    final hostname =
        uri.queryParameters["host"] ?? uri.queryParameters["hostname"];
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
          icon: const Iconify(TWIcons.home),
          label: const Text("Connect Localhost"),
          onPressed: () => connectTo(ref, "localhost", 9092),
        ),
        const SizedBox(width: 24),
        FilledButton.icon(
          icon: const Iconify(TWIcons.connect),
          label: const Text("Connect Custom"),
          onPressed: () => customConnectToPopup(context, ref),
        ),
      ],
    );
  }
}
