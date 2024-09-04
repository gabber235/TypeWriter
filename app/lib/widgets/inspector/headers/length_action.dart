import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";

class LengthHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, DataBlueprint dataBlueprint) {
    return dataBlueprint is ListBlueprint || dataBlueprint is MapBlueprint;
  }

  @override
  HeaderActionLocation location(String path, DataBlueprint dataBlueprint) =>
      HeaderActionLocation.trailing;

  @override
  Widget build(String path, DataBlueprint dataBlueprint) =>
      LengthHeaderAction(path: path, dataBlueprint: dataBlueprint);
}

class LengthHeaderAction extends HookConsumerWidget {
  const LengthHeaderAction({
    required this.path,
    required this.dataBlueprint,
    super.key,
  });

  final String path;
  final DataBlueprint dataBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path));

    final int length;

    if (value is List) {
      length = value.length;
    } else if (value is Map) {
      length = value.length;
    } else {
      length = 0;
    }

    return Text(
      "($length)",
      style: Theme.of(context).textTheme.bodySmall,
    );
  }
}
