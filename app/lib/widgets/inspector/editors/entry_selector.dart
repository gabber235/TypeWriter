import "package:dropdown_search/dropdown_search.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:ktx/ktx.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/empty_screen.dart";
import "package:typewriter/widgets/entry_node.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/search_bar.dart";

part "entry_selector.g.dart";

@riverpod
Map<String, Entry> entriesFromTag(EntriesFromTagRef ref, String tag) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return {};

  return page.entries
      .where((entry) => ref.watch(entryTagsProvider(entry.type)).contains(tag))
      .associateBy((entry) => entry.id);
}

class EntrySelectorEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is PrimitiveField && info.type == PrimitiveFieldType.string && info.hasModifier("entry");

  @override
  Widget build(String path, FieldInfo info) => FactEditor(path: path, field: info as PrimitiveField);
}

class FactEditor extends HookConsumerWidget {
  const FactEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();

  final String path;
  final PrimitiveField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedEntryId = ref.watch(selectedEntryIdProvider);
    final tag = field.getModifier("entry")!.data;
    final value = ref.watch(fieldValueProvider(path, ""));
    final entries = ref.watch(entriesFromTagProvider(tag));

    final globalKey = useMemoized(GlobalKey<DropdownSearchState<Entry>>.new);

    return DropdownSearch<Entry>(
      key: globalKey,
      itemAsString: (entry) => entry.id,
      items: entries.values.where((entry) => entry.id != selectedEntryId).toList(),
      selectedItem: entries[value],
      filterFn: (entry, string) {
        return entry.formattedName.toLowerCase().contains(string.toLowerCase()) ||
            entry.name.toLowerCase().contains(string.toLowerCase());
      },
      onChanged: (entry) {
        if (entry == null) return;
        ref.read(entryDefinitionProvider)?.updateField(ref.passing, path, entry.id);
      },
      onSaved: (entry) {
        if (entry == null) return;
        ref.read(entryDefinitionProvider)?.updateField(ref.passing, path, entry.id);
      },
      dropdownBuilder: (context, entry) {
        if (entry == null) return Text("Select a $tag", style: Theme.of(context).inputDecorationTheme.hintStyle);

        return Padding(
          padding: const EdgeInsets.symmetric(vertical: 12.0),
          child: FakeEntryNode(entry: entry),
        );
      },
      onBeforePopupOpening: (entry) async {
        ref.read(currentEditingFieldProvider.notifier).path = path;
        return true;
      },
      popupProps: PopupProps.menu(
        itemBuilder: (context, entry, _) => Padding(
          padding: const EdgeInsets.symmetric(vertical: 6.0, horizontal: 12.0),
          child: FakeEntryNode(entry: entry),
        ),
        emptyBuilder: (context, entry) => buildEmpty(ref, globalKey, tag),
        onDismissed: () {
          ref.read(currentEditingFieldProvider.notifier).clearIfSame(path);
        },
        showSearchBox: true,
        title: Padding(
          padding: const EdgeInsets.only(left: 16.0, top: 8),
          child: Text(
            "Select a $tag",
            style: Theme.of(context).textTheme.titleLarge,
          ),
        ),
        searchFieldProps: const TextFieldProps(
          decoration: InputDecoration(
            hintText: "Search...",
            prefixIcon: Icon(Icons.search),
          ),
        ),
        menuProps: MenuProps(
          borderRadius: BorderRadius.circular(8),
        ),
      ),
      dropdownButtonProps: const DropdownButtonProps(
        icon: Icon(FontAwesomeIcons.caretDown, size: 18),
      ),
      dropdownDecoratorProps: DropDownDecoratorProps(
        dropdownSearchDecoration: InputDecoration(
          contentPadding: const EdgeInsets.symmetric(horizontal: 8),
          hintText: "Select a $tag",
          prefixIcon: const Icon(FontAwesomeIcons.database, size: 18),
        ),
      ),
    );
  }

  Widget buildEmpty(WidgetRef ref, GlobalKey<DropdownSearchState<Entry>> globalKey, String tag) {
    return SizedBox.expand(
      child: Column(
        children: [
          Flexible(
            child: EmptyScreen(
              small: true,
              title: "No $tag found",
              onButtonPressed: () {
                globalKey.currentState?.closeDropDownSearch();
                ref.read(searchingProvider.notifier).startSearch("$tag:");
              },
              buttonText: "Create a $tag",
            ),
          ),
          const SizedBox(height: 30),
        ],
      ),
    );
  }
}
