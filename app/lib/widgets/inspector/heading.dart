import "package:auto_size_text/auto_size_text.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/components/general/admonition.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/inspector.dart";
import "package:url_launcher/url_launcher_string.dart";

part "heading.g.dart";

@riverpod
String _entryId(_EntryIdRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.entry.id ?? "";
}

@riverpod
String _entryName(_EntryNameRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.entry.formattedName ?? "";
}

@riverpod
String _entryType(_EntryTypeRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.blueprint.name ?? "";
}

@riverpod
String _entryUrl(_EntryUrlRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.blueprint.wikiUrl ?? "";
}

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
    final id = ref.watch(_entryIdProvider);
    final name = ref.watch(_entryNameProvider);
    final type = ref.watch(_entryTypeProvider);
    final url = ref.watch(_entryUrlProvider);
    final color = ref.watch(_entryColorProvider);
    final isDeprecated = ref.watch(isEntryDeprecatedProvider(id));

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Title(
          color: color,
          title: name,
          isDeprecated: isDeprecated,
        ),
        Row(
          children: [
            _Type(type: type, url: url, color: color),
            const SizedBox(width: 8),
            _Identifier(id: id),
          ],
        ),
        if (isDeprecated) ...[
          const SizedBox(height: 8),
          _DeperecationWarning(url: url),
        ],
      ],
    );
  }
}

class Title extends StatelessWidget {
  const Title({
    required this.title,
    required this.color,
    this.isDeprecated = false,
    super.key,
  });
  final String title;
  final Color color;
  final bool isDeprecated;

  @override
  Widget build(BuildContext context) {
    return AutoSizeText(
      title,
      style: TextStyle(
        color: color,
        fontSize: 40,
        fontWeight: FontWeight.bold,
        decoration: isDeprecated ? TextDecoration.lineThrough : null,
        decorationThickness: 2.8,
        decorationStyle: TextDecorationStyle.wavy,
        decorationColor: color,
      ),
      maxLines: 1,
    );
  }
}

class _Identifier extends StatelessWidget {
  const _Identifier({
    required this.id,
  });
  final String id;

  @override
  Widget build(BuildContext context) {
    return SelectableText(
      id,
      style:
          Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey),
    );
  }
}

class _Type extends HookWidget {
  const _Type({
    required this.type,
    required this.url,
    required this.color,
  });
  final String type;
  final String url;
  final Color color;

  Future<void> _launceUrl() async {
    if (url.isEmpty) return;
    if (!await canLaunchUrlString(url)) return;
    await launchUrlString(url);
  }

  @override
  Widget build(BuildContext context) {
    final hovering = useState(false);
    return MouseRegion(
      cursor: SystemMouseCursors.click,
      onEnter: (_) => hovering.value = true,
      onExit: (_) => hovering.value = false,
      child: GestureDetector(
        onTap: _launceUrl,
        child: Text(
          type.formatted,
          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                color: color.withOpacity(0.9),
                decoration: hovering.value ? TextDecoration.underline : null,
              ),
        ),
      ),
    );
  }
}

class _DeperecationWarning extends StatelessWidget {
  const _DeperecationWarning({
    required this.url,
  });

  final String url;

  Future<void> _launceUrl() async {
    if (url.isEmpty) return;
    if (!await canLaunchUrlString(url)) return;
    await launchUrlString(url);
  }

  @override
  Widget build(BuildContext context) {
    return Admonition.danger(
      onTap: _launceUrl,
      child: const Text.rich(
        TextSpan(
          text: "This entry has been marked as deprecated. Take a look at the ",
          children: [
            TextSpan(
              text: "documentation",
              style: TextStyle(
                decoration: TextDecoration.underline,
                decorationColor: Colors.redAccent,
              ),
            ),
            TextSpan(text: " for more information."),
          ],
        ),
      ),
    );
  }
}
