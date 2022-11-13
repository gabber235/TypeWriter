import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/utils/extensions.dart';
import 'package:typewriter/widgets/inspector.dart';

final _entryColorProvider = Provider.autoDispose<Color>((ref) {
  final def = ref.watch(entryDefinitionProvider);
  return def?.adapterEntry.color ?? Colors.grey;
}, dependencies: [entryDefinitionProvider]);

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
          title: def?.entry.name.formatted ?? '',
        ),
        Identifier(id: def?.entry.id ?? ''),
      ],
    );
  }
}

class Title extends StatelessWidget {
  final String title;
  final Color color;

  const Title({
    Key? key,
    required this.title,
    required this.color,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AutoSizeText(
      title,
      style: TextStyle(color: color, fontSize: 40, fontWeight: FontWeight.w900),
      maxLines: 1,
    );
  }
}

class Identifier extends StatelessWidget {
  final String id;

  const Identifier({Key? key, required this.id}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SelectableText(id, style: Theme.of(context).textTheme.caption);
  }
}
