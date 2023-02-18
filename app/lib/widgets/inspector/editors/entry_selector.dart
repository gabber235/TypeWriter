import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/smart_single_activator.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/app/select_entries.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class EntrySelectorEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is PrimitiveField && info.type == PrimitiveFieldType.string && info.hasModifier("entry");

  @override
  Widget build(String path, FieldInfo info) => EntrySelectorEditor(path: path, field: info as PrimitiveField);
}

class EntrySelectorEditor extends HookConsumerWidget {
  const EntrySelectorEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();

  final String path;
  final PrimitiveField field;

  void _update(WidgetRef ref, Entry? entry) {
    if (entry == null) return;
    ref.read(inspectingEntryDefinitionProvider)?.updateField(ref.passing, path, entry.id);
  }

  Future<void> _create(WidgetRef ref, EntryBlueprint blueprint) async {
    final page = ref.read(currentPageProvider);
    if (page == null) return;
    final entry = await page.createEntryFromBlueprint(ref.passing, blueprint);
    _update(ref, entry);

    await ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref.passing, entry.id);
  }

  void _select(WidgetRef ref, String tag) {
    ref.read(searchProvider.notifier).asBuilder()
      ..tag(tag, canRemove: false)
      ..fetchEntry(onSelect: (entry) => _update(ref, entry))
      ..fetchNewEntry(onAdd: (blueprint) => _create(ref, blueprint))
      ..open();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tag = field.getModifier("entry")!.data;
    final value = ref.watch(fieldValueProvider(path, ""));

    final entry = ref.watch(globalEntryProvider(value as String));
    final hasEntry = entry != null;

    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: ContextMenuRegion(
        builder: (context) {
          return [
            if (hasEntry) ...[
              ContextMenuTile.button(
                title: "Navigate to entry",
                icon: FontAwesomeIcons.pencil,
                onTap: () {
                  ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref.passing, entry!.id);
                },
              ),
              ContextMenuTile.button(
                title: "Remove reference",
                icon: FontAwesomeIcons.solidSquareMinus,
                color: Colors.redAccent,
                onTap: () {
                  ref.read(inspectingEntryDefinitionProvider)?.updateField(ref.passing, path, null);
                },
              ),
            ],
            if (!hasEntry) ...[
              ContextMenuTile.button(
                title: "Select entry",
                icon: FontAwesomeIcons.magnifyingGlass,
                onTap: () {
                  _select(ref, tag);
                },
              ),
            ],
          ];
        },
        child: InkWell(
          onTap: () {
            if (hasOverrideDown && hasEntry) {
              ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref.passing, entry.id);
              return;
            }
            _select(ref, tag);
          },
          borderRadius: BorderRadius.circular(8),
          child: Padding(
            padding:
                EdgeInsets.only(left: hasEntry ? 4 : 12, right: 16, top: hasEntry ? 4 : 12, bottom: hasEntry ? 4 : 12),
            child: Row(
              children: [
                if (!hasEntry) ...[
                  FaIcon(
                    FontAwesomeIcons.database,
                    size: 16,
                    color: Theme.of(context).inputDecorationTheme.hintStyle?.color,
                  ),
                  const SizedBox(width: 12),
                ],
                if (hasEntry)
                  Expanded(child: FakeEntryNode(entry: entry))
                else
                  Expanded(child: Text("Select a $tag", style: Theme.of(context).inputDecorationTheme.hintStyle)),
                const SizedBox(width: 12),
                FaIcon(
                  FontAwesomeIcons.caretDown,
                  size: 16,
                  color: Theme.of(context).inputDecorationTheme.hintStyle?.color,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

/// The button on a list of entries that allows to select multiple entries at once.
/// See [ListField] for more information.
class EntriesSelectorButton extends HookConsumerWidget {
  const EntriesSelectorButton({required this.path, required this.tag, super.key});

  final String path;
  final String tag;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Tooltip(
      message: "Select multiple entries",
      child: Material(
        borderRadius: BorderRadius.circular(4),
        color: Colors.deepPurple,
        child: InkWell(
          borderRadius: const BorderRadius.all(Radius.circular(4)),
          onTap: () async {
            final currentEntries = ref.watch(fieldValueProvider(path, [])) as List<dynamic>;
            final entryDefinition = ref.watch(inspectingEntryDefinitionProvider);
            if (entryDefinition == null) return;

            ref.read(entrySelectionProvider.notifier).startSelection(
              tag,
              currentEntries.map((e) => e as String).toList(),
              (ref, selectedEntries) {
                ref.read(inspectingEntryDefinitionProvider)?.updateField(
                      ref.passing,
                      path,
                      selectedEntries,
                    );
              },
            );
          },
          child: const Padding(
            padding: EdgeInsets.all(6.0),
            child: FaIcon(FontAwesomeIcons.objectGroup, size: 16),
          ),
        ),
      ),
    );
  }
}
