import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/info_action.dart";

class ColoredHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, FieldInfo field) =>
      field.getModifier("colored") != null;

  @override
  HeaderActionLocation location(String path, FieldInfo field) =>
      HeaderActionLocation.trailing;

  @override
  Widget build(String path, FieldInfo field) =>
      ColoredHeaderAction(field: field);
}

class ColoredHeaderAction extends HookConsumerWidget {
  const ColoredHeaderAction({
    required this.field,
    super.key,
  }) : super();

  final FieldInfo field;

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
