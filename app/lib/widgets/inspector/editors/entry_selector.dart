import "package:flutter/material.dart" hide Page;
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/smart_single_activator.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/entry_search.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class EntrySelectorEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info.hasModifier("entry");

  @override
  Widget build(String path, FieldInfo info) =>
      EntrySelectorEditor(path: path, field: info);
}

class EntrySelectorEditor extends HookConsumerWidget {
  const EntrySelectorEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();

  final String path;
  final FieldInfo field;

  bool _update(PassingRef ref, Entry? entry) {
    if (entry == null) return false;
    ref
        .read(inspectingEntryDefinitionProvider)
        ?.updateField(ref, path, entry.id);
    return true;
  }

  void _select(PassingRef ref, String tag) {
    final selectedEntryId = ref.read(inspectingEntryIdProvider);
    ref.read(searchProvider.notifier).asBuilder()
      ..tag(tag, canRemove: false)
      ..excludeEntry(selectedEntryId ?? "", canRemove: false)
      ..fetchEntry(onSelect: (entry) => _update(ref, entry))
      ..fetchNewEntry(onAdded: (entry) => _update(ref, entry))
      ..open();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tag = field.get<String>("entry") ?? "";
    final id = ref.watch(fieldValueProvider(path, "")) as String;

    final hasEntry = ref.watch(entryExistsProvider(id));

    return DragTarget<EntryDrag>(
      onWillAcceptWithDetails: (details) {
        if (details.data.entryId == id) return false;

        final entry = ref.read(globalEntryProvider(details.data.entryId));
        if (entry == null) return false;
        final blueprint = ref.read(entryBlueprintProvider(entry.type));
        if (blueprint == null) return false;

        return blueprint.tags.contains(tag);
      },
      onAcceptWithDetails: (details) {
        ref
            .read(inspectingEntryDefinitionProvider)
            ?.updateField(ref.passing, path, details.data.entryId);
      },
      builder: (context, candidateData, rejectedData) {
        if (rejectedData.isNotEmpty) {
          return _rejectWidget(context);
        }
        final isAccepting = candidateData.isNotEmpty;

        final needsPadding = !hasEntry && !isAccepting;

        return Material(
          color: Theme.of(context).inputDecorationTheme.fillColor,
          borderRadius: BorderRadius.circular(8),
          child: ContextMenuRegion(
            builder: (context) {
              return [
                if (hasEntry) ...[
                  ContextMenuTile.button(
                    title: "Navigate to entry",
                    icon: TWIcons.pencil,
                    onTap: () {
                      ref
                          .read(inspectingEntryIdProvider.notifier)
                          .navigateAndSelectEntry(ref.passing, id);
                    },
                  ),
                  ContextMenuTile.button(
                    title: "Remove reference",
                    icon: TWIcons.squareMinus,
                    color: Colors.redAccent,
                    onTap: () {
                      ref
                          .read(inspectingEntryDefinitionProvider)
                          ?.updateField(ref.passing, path, "");
                    },
                  ),
                ],
                if (!hasEntry) ...[
                  ContextMenuTile.button(
                    title: "Select entry",
                    icon: TWIcons.magnifyingGlass,
                    onTap: () {
                      _select(ref.passing, tag);
                    },
                  ),
                ],
              ];
            },
            child: InkWell(
              onTap: () {
                if (hasOverrideDown && hasEntry) {
                  ref
                      .read(inspectingEntryIdProvider.notifier)
                      .navigateAndSelectEntry(ref.passing, id);
                  return;
                }
                _select(ref.passing, tag);
              },
              borderRadius: BorderRadius.circular(8),
              child: Padding(
                padding: EdgeInsets.only(
                  left: needsPadding ? 12 : 4,
                  right: 16,
                  top: needsPadding ? 12 : 4,
                  bottom: needsPadding ? 12 : 4,
                ),
                child: Row(
                  children: [
                    if (!hasEntry && !isAccepting) ...[
                      Iconify(
                        TWIcons.database,
                        size: 16,
                        color: Theme.of(context)
                            .inputDecorationTheme
                            .hintStyle
                            ?.color,
                      ),
                      const SizedBox(width: 12),
                    ],
                    if (isAccepting)
                      Expanded(
                        child: Opacity(
                          opacity: 0.5,
                          child: FakeEntryNode(
                            entryId: candidateData.first!.entryId,
                          ),
                        ),
                      )
                    else if (hasEntry)
                      Expanded(child: FakeEntryNode(entryId: id))
                    else
                      Expanded(
                        child: Text(
                          "Select a $tag",
                          style:
                              Theme.of(context).inputDecorationTheme.hintStyle,
                        ),
                      ),
                    const SizedBox(width: 12),
                    Iconify(
                      TWIcons.caretDown,
                      size: 16,
                      color: Theme.of(context)
                          .inputDecorationTheme
                          .hintStyle
                          ?.color,
                    ),
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  Widget _rejectWidget(BuildContext context) {
    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: Padding(
        padding: const EdgeInsets.all(15),
        child: Row(
          children: [
            Iconify(
              TWIcons.x,
              size: 16,
              color: Theme.of(context).colorScheme.error,
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Text(
                "Entry is not allowed here",
                style: TextStyle(color: Theme.of(context).colorScheme.error),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
