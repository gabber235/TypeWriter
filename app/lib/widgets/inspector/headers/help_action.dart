import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/widgets/inspector/header.dart";

class HelpHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, DataBlueprint dataBlueprint) =>
      (dataBlueprint.getModifier("help")?.data as String?) != null;

  @override
  HeaderActionLocation location(String path, DataBlueprint dataBlueprint) =>
      HeaderActionLocation.trailing;

  @override
  Widget build(String path, DataBlueprint dataBlueprint) =>
      HelpHeaderAction(dataBlueprint: dataBlueprint);
}

class HelpHeaderAction extends HookConsumerWidget {
  const HelpHeaderAction({
    required this.dataBlueprint,
    super.key,
  });

  final DataBlueprint dataBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final help = dataBlueprint.getModifier("help");
    final helpText = help?.data as String?;
    if (helpText == null) {
      return const SizedBox();
    }
    final formattedHelp =
        helpText.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    return Tooltip(
      message: formattedHelp,
      child: Icon(
        Icons.help_outline,
        size: 16,
        color: Theme.of(context).textTheme.bodySmall?.color?.withOpacity(0.6),
      ),
    );
  }
}
