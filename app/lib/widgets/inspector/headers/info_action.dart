import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:url_launcher/url_launcher_string.dart";

class InfoHeaderAction extends HookConsumerWidget {
  const InfoHeaderAction({
    required this.tooltip,
    required this.icon,
    required this.color,
    required this.url,
    super.key,
  }) : super();

  final String tooltip;
  final IconData icon;
  final Color color;
  final String url;

  Future<void> _launchUrl(String url) async {
    if (await canLaunchUrlString(url)) {
      await launchUrlString(url);
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return IconButton(
      tooltip: tooltip,
      icon: FaIcon(
        icon,
        size: 14,
        color: color,
      ),
      hoverColor: color.withOpacity(0.2),
      padding: const EdgeInsets.all(0),
      visualDensity: VisualDensity.compact,
      onPressed: () => _launchUrl(url),
    );
  }
}
