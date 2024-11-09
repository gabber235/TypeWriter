import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/info_action.dart";

class PlaceholderHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, DataBlueprint dataBlueprint) =>
      dataBlueprint.getModifier("placeholder") != null;

  @override
  HeaderActionLocation location(String path, DataBlueprint dataBlueprint) =>
      HeaderActionLocation.trailing;

  @override
  Widget build(String path, DataBlueprint dataBlueprint) =>
      const PlaceholderHeaderAction();
}

class PlaceholderHeaderAction extends HookConsumerWidget {
  const PlaceholderHeaderAction({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return const InfoHeaderAction(
      tooltip:
          "Placeholers like %player_name% are supported. Click for more info.",
      icon: TWIcons.subscript,
      color: Color(0xFF00b300),
      url: "https://github.com/PlaceholderAPI/PlaceholderAPI/wiki",
    );
  }
}
