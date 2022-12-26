import "package:auto_size_text/auto_size_text.dart";
import "package:dropdown_search/dropdown_search.dart";
import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/materials.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/editors.dart";

part "material.g.dart";

@riverpod
List<MaterialProperty> materialProperties(MaterialPropertiesRef ref, String meta) {
  return meta
      .split(";")
      .map((property) => MaterialProperty.values.firstWhere((element) => element.name.toLowerCase() == property))
      .toList();
}

@riverpod
List<MapEntry<String, MinecraftMaterial>> computeMaterialsWithProperties(
    ComputeMaterialsWithPropertiesRef ref, String? meta) {
  final properties = meta != null ? ref.watch(materialPropertiesProvider(meta)) : [];
  return materials.entries
      .where((element) => properties.every((property) => element.value.properties.contains(property)))
      .toList();
}

class MaterialSelectorEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is CustomField && info.editor == "material";
  @override
  Widget build(String path, FieldInfo info) => MaterialSelectorEditor(path: path, field: info as CustomField);
}

class MaterialSelectorEditor extends HookConsumerWidget {
  const MaterialSelectorEditor({
    required this.path,
    required this.field,
    super.key,
  }) : super();

  final String path;
  final CustomField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, ""));

    final propertiesModifier = field.getModifier("material_properties");
    final items = ref.watch(computeMaterialsWithPropertiesProvider(propertiesModifier?.data));

    final current = value.isEmpty ? "air" : value.toLowerCase();

    return DropdownSearch<MapEntry<String, MinecraftMaterial>>(
      itemAsString: (entry) => entry.key,
      items: items,
      selectedItem: MapEntry(current, materials[current]!),
      onChanged: (entry) {
        if (entry == null) return;
        ref.read(entryDefinitionProvider)?.updateField(ref, path, entry.key.toUpperCase());
      },
      onSaved: (entry) {
        if (entry == null) return;
        ref.read(entryDefinitionProvider)?.updateField(ref, path, entry.key.toUpperCase());
      },
      dropdownBuilder: (context, entry) {
        if (entry == null) return Text("Select a material", style: Theme.of(context).inputDecorationTheme.hintStyle);

        return _MaterialItem(id: entry.key, material: entry.value);
      },
      popupProps: PopupProps.menu(
        itemBuilder: (context, entry, isSelected) => _MaterialItem(
          id: entry.key,
          material: entry.value,
          isSelected: isSelected,
        ),
        showSearchBox: true,
        title: Padding(
          padding: const EdgeInsets.only(left: 16.0, top: 8),
          child: Text(
            "Select a material",
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
      dropdownDecoratorProps: const DropDownDecoratorProps(
        dropdownSearchDecoration: InputDecoration(
          contentPadding: EdgeInsets.zero,
          hintText: "Select a material",
        ),
      ),
    );
  }
}

class _MaterialItem extends StatelessWidget {
  const _MaterialItem({
    required this.id,
    required this.material,
    this.isSelected = false,
    super.key,
  });

  final String id;
  final MinecraftMaterial material;
  final bool isSelected;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Padding(
        padding: const EdgeInsets.symmetric(vertical: 3.0),
        child: AspectRatio(
          aspectRatio: 1,
          child: Image.asset(material.icon),
        ),
      ),
      title: AutoSizeText(
        material.name,
        maxLines: 1,
      ),
      subtitle: AutoSizeText(
        "minecraft:$id",
        maxLines: 1,
      ),
      selected: isSelected,
    );
  }
}
