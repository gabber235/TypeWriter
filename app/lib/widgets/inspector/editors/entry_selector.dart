import "package:flutter/material.dart" hide Page;
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/smart_single_activator.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/entry_search.dart";
import "package:typewriter/widgets/components/app/page_search.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/app/select_entries.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/components/general/toasts.dart";
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

  bool _update(PassingRef ref, Entry? entry) {
    if (entry == null) return false;
    ref.read(inspectingEntryDefinitionProvider)?.updateField(ref, path, entry.id);
    return true;
  }

  Future<bool?> _create(PassingRef ref, EntryBlueprint blueprint) async {
    final page = ref.read(currentPageProvider);
    if (page == null) return false;

    if (page.canHave(blueprint)) {
      return _createAndNavigate(ref, page, blueprint);
    }

    // This page can't have the entry, so we need to select/create a new page where we can.

    ref.read(searchProvider.notifier).asBuilder()
      ..pageType(PageType.fromBlueprint(blueprint), canRemove: false)
      ..fetchPage(onSelect: (page) => _createAndNavigate(ref, page, blueprint))
      ..fetchAddPage(onAdded: (page) => _createAndNavigate(ref, page, blueprint))
      ..open();

    return false;
  }

  Future<bool> _createAndNavigate(PassingRef ref, Page page, EntryBlueprint blueprint) async {
    final entry = await page.createEntryFromBlueprint(ref, blueprint);

    final didUpdate = _update(ref, entry);
    if (!didUpdate) {
      Toasts.showError(ref, "Failed to create entry", description: "There was an error when creating the entry");
      return false;
    }

    final notifier = ref.read(inspectingEntryIdProvider.notifier);

    // Close the search bar.
    ref.read(searchProvider.notifier).endSearch();

    await notifier.navigateAndSelectEntry(ref, entry.id);

    return true;
  }

  void _select(PassingRef ref, String tag) {
    final selectedEntryId = ref.read(inspectingEntryIdProvider);
    ref.read(searchProvider.notifier).asBuilder()
      ..tag(tag, canRemove: false)
      ..excludeEntry(selectedEntryId ?? "", canRemove: false)
      ..fetchEntry(onSelect: (entry) => _update(ref, entry))
      ..fetchNewEntry(onAdd: (blueprint) => _create(ref, blueprint))
      ..open();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tag = field.getModifier("entry")!.data;
    final id = ref.watch(fieldValueProvider(path, "")) as String;

    final hasEntry = ref.watch(entryExistsProvider(id));

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
                  ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref.passing, id);
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
                  _select(ref.passing, tag);
                },
              ),
            ],
          ];
        },
        child: InkWell(
          onTap: () {
            if (hasOverrideDown && hasEntry) {
              ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref.passing, id);
              return;
            }
            _select(ref.passing, tag);
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
                  Expanded(child: FakeEntryNode(entryId: id))
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

  void _startSelection(PassingRef ref) {
    final currentEntries = ref.read(fieldValueProvider(path, [])) as List<dynamic>;
    final inspectingEntryId = ref.read(inspectingEntryIdProvider);
    if (inspectingEntryId == null) return;

    ref.read(entrySelectionProvider.notifier).startSelection(
      tag,
      selectedEntries: currentEntries.map((e) => e as String).toList(),
      excludedEntries: [inspectingEntryId],
      onSelectionChanged: (ref, selectedEntries) {
        ref.read(inspectingEntryDefinitionProvider)?.updateField(
              ref.passing,
              path,
              selectedEntries,
            );
      },
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Tooltip(
      message: "Select multiple entries",
      child: Material(
        borderRadius: BorderRadius.circular(4),
        color: Colors.deepPurple,
        child: InkWell(
          borderRadius: const BorderRadius.all(Radius.circular(4)),
          onTap: () => _startSelection(ref.passing),
          child: const Padding(
            padding: EdgeInsets.all(6.0),
            child: FaIcon(FontAwesomeIcons.objectGroup, size: 16, color: Colors.white),
          ),
        ),
      ),
    );
  }
}
