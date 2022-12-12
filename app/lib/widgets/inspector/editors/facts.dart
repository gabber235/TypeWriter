import "package:auto_size_text/auto_size_text.dart";
import "package:dropdown_search/dropdown_search.dart";
import "package:flutter/cupertino.dart";
import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:ktx/ktx.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import 'package:typewriter/utils/extensions.dart';
import 'package:typewriter/widgets/inspector.dart';
import "package:typewriter/widgets/inspector/editors.dart";

part "facts.g.dart";

@riverpod
Map<String, Entry> facts(FactsRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return {};

  return page.entries
      .where((entry) => ref.watch(entryTagsProvider(entry.type)).contains("fact"))
      .associateBy((entry) => entry.id);
}

class FactsEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is PrimitiveField && info.hasModifier("fact");

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
    final value = ref.watch(fieldValueProvider(path, ""));
    final facts = ref.watch(factsProvider);

    return DropdownSearch<String>(
      items: facts.keys.toList(),
      dropdownButtonProps: const DropdownButtonProps(
        icon: Icon(FontAwesomeIcons.caretDown, size: 18),
      ),
      selectedItem: value,
      dropdownDecoratorProps: const DropDownDecoratorProps(
        dropdownSearchDecoration: InputDecoration(
          contentPadding: EdgeInsets.symmetric(horizontal: 8),
          hintText: "Select a fact",
          prefixIcon: Icon(FontAwesomeIcons.penNib, size: 18),
        ),
      ),
      popupProps: PopupProps.menu(
        showSearchBox: true,
        title: Padding(
          padding: const EdgeInsets.only(left: 16.0, top: 8),
          child: Text(
            "Select a fact",
            style: Theme.of(context).textTheme.titleMedium,
          ),
        ),
        itemBuilder: (context, id, isSelected) => ListTile(
          title: AutoSizeText(facts[id]?.formattedName ?? "", maxLines: 1),
          subtitle: Text(facts[id]?.type?.formatted ?? ""),
          selected: isSelected,
        ),
      ),
      dropdownBuilder: (context, id) => AutoSizeText(
        facts[id]?.formattedName ?? "",
        maxLines: 1,
      ),
      filterFn: (id, string) {
        final fact = facts[id];
        if (fact == null) return false;

        return fact.formattedName.toLowerCase().contains(string.toLowerCase()) ||
            fact.name.toLowerCase().contains(string.toLowerCase());
      },
      onChanged: (fact) {
        if (fact == null) return;
        ref.read(entryDefinitionProvider)?.updateField(ref, path, fact);
      },
      onSaved: (fact) {
        if (fact == null) return;
        ref.read(entryDefinitionProvider)?.updateField(ref, path, fact);
      },
    );
  }
}
