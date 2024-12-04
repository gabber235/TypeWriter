import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/info_action.dart";

class ColoredHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(
    String path,
    HeaderContext context,
    DataBlueprint dataBlueprint,
  ) =>
      dataBlueprint.getModifier("colored") != null;

  @override
  HeaderActionLocation location(
    String path,
    HeaderContext context,
    DataBlueprint dataBlueprint,
  ) =>
      HeaderActionLocation.trailing;

  @override
  Widget build(
    String path,
    HeaderContext context,
    DataBlueprint dataBlueprint,
  ) =>
      ColoredHeaderAction(dataBlueprint: dataBlueprint);
}

class ColoredHeaderAction extends HookConsumerWidget {
  const ColoredHeaderAction({
    required this.dataBlueprint,
    super.key,
  }) : super();

  final DataBlueprint dataBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return const InfoHeaderAction(
      tooltip: "Adventure Mini Format is supported. Click for more info.",
      icon: TWIcons.paintBrush,
      color: Color(0xFFff8e42),
      url: "https://docs.advntr.dev/minimessage/format.html",
    );
  }
}
