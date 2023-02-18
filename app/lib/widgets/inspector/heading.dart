import "package:auto_size_text/auto_size_text.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "heading.g.dart";

@riverpod
Color _entryColor(_EntryColorRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.blueprint.color ?? Colors.grey;
}

class Heading extends HookConsumerWidget {
  const Heading({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final def = ref.watch(inspectingEntryDefinitionProvider);
    final color = ref.watch(_entryColorProvider);

    if (def == null) {
      return const SizedBox();
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _Title(
          color: color,
          title: def.entry.formattedName,
        ),
        Row(
          children: [
            _Type(type: def.blueprint.name, color: color),
            const SizedBox(width: 8),
            _Identifier(id: def.entry.id ?? ""),
          ],
        ),
      ],
    );
  }
}

class _Title extends StatelessWidget {
  const _Title({
    required this.title,
    required this.color,
  });
  final String title;
  final Color color;

  @override
  Widget build(BuildContext context) => AutoSizeText(
        title,
        style: TextStyle(color: color, fontSize: 40, fontWeight: FontWeight.bold),
        maxLines: 1,
      );
}

class _Identifier extends StatelessWidget {
  const _Identifier({
    required this.id,
    super.key,
  });
  final String id;

  @override
  Widget build(BuildContext context) {
    return SelectableText(id, style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey));
  }
}

class _Type extends StatelessWidget {
  const _Type({
    required this.type,
    required this.color,
    super.key,
  });
  final String type;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return SelectableText(
      type.formatted,
      style: Theme.of(context).textTheme.bodySmall?.copyWith(color: color.withOpacity(0.9)),
    );
  }
}
