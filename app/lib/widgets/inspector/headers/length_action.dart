import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";

class LengthHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, FieldInfo field) {
    return field is ListField || field is MapField;
  }

  @override
  HeaderActionLocation location(String path, FieldInfo field) =>
      HeaderActionLocation.trailing;

  @override
  Widget build(String path, FieldInfo field) => LengthHeaderAction(path, field);
}

class LengthHeaderAction extends HookConsumerWidget {
  const LengthHeaderAction(
    this.path,
    this.field, {
    super.key,
  });

  final String path;
  final FieldInfo field;

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
