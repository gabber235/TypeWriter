import "package:auto_size_text/auto_size_text.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector.dart";

part "heading.g.dart";

@riverpod
Color _entryColor(_EntryColorRef ref) {
  final def = ref.watch(entryDefinitionProvider);
  return def?.adapterEntry.color ?? Colors.grey;
}

class Heading extends HookConsumerWidget {
  const Heading({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final def = ref.watch(entryDefinitionProvider);
    final color = ref.watch(_entryColorProvider);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Title(
          color: color,
          title: def?.entry.name.formatted ?? "",
        ),
        Identifier(id: def?.entry.id ?? ""),
      ],
    );
  }
}

class Title extends StatelessWidget {
  const Title({
    required this.title,
    required this.color,
    super.key,
  });
  final String title;
  final Color color;

  @override
  Widget build(BuildContext context) => AutoSizeText(
        title,
        style: TextStyle(color: color, fontSize: 40, fontWeight: FontWeight.w900),
        maxLines: 1,
      );
}

class Identifier extends StatelessWidget {
  const Identifier({super.key, required this.id});
  final String id;

  @override
  Widget build(BuildContext context) => SelectableText(id, style: Theme.of(context).textTheme.caption);
}
