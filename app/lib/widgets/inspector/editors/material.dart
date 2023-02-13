import "package:auto_size_text/auto_size_text.dart";
import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:fuzzy/fuzzy.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/materials.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "material.g.dart";

typedef CombinedMaterial = MapEntry<String, MinecraftMaterial>;

@riverpod
List<MaterialProperty> materialProperties(MaterialPropertiesRef ref, String meta) {
  return meta
      .split(";")
      .map((property) => MaterialProperty.values.firstWhere((element) => element.name.toLowerCase() == property))
      .toList();
}

@riverpod
Fuzzy<CombinedMaterial> _fuzzyMaterials(_FuzzyMaterialsRef ref) {
  return Fuzzy(
    materials.entries.toList(),
    options: FuzzyOptions(
      threshold: 0.2,
      keys: [
        WeightedKey(
          name: "name",
          getter: (entry) => entry.value.name,
          weight: 0.7,
        ),
        WeightedKey(
          name: "id",
          getter: (entry) => entry.key,
          weight: 0.3,
        ),
        WeightedKey(
          name: "properties",
          getter: (entry) => entry.value.properties.map((p) => p.name).join(" "),
          weight: 0.2,
        ),
      ],
    ),
  );
}

class MaterialsFetcher extends SearchFetcher {
  const MaterialsFetcher({
    this.onSelect,
    this.disabled = false,
  });

  final Function(CombinedMaterial)? onSelect;

  @override
  final bool disabled;

  @override
  String get title => "Materials";

  @override
  List<SearchAction> fetch(PassingRef ref) {
    final search = ref.read(searchProvider);
    if (search == null) return [];
    final fuzzy = ref.read(_fuzzyMaterialsProvider);

    final results = fuzzy.search(search.query);

    return results.map((result) {
      final material = result.item;
      return SearchMaterialAction(material, onSelect: onSelect);
    }).toList();
  }

  @override
  SearchFetcher copyWith({
    bool? disabled,
  }) {
    return MaterialsFetcher(
      onSelect: onSelect,
      disabled: disabled ?? this.disabled,
    );
  }
}

class SearchMaterialAction extends SearchAction {
  const SearchMaterialAction(this.material, {this.onSelect});

  final CombinedMaterial material;
  final Function(CombinedMaterial)? onSelect;

  @override
  Color color(BuildContext context) {
    final properties = material.value.properties;
    final isDark = context.isDark;

    if (properties.contains(MaterialProperty.item)) return isDark ? Colors.black38 : Colors.black12;
    if (properties.contains(MaterialProperty.block)) return isDark ? Colors.black54 : Colors.black26;

    return Colors.grey;
  }

  @override
  Widget icon(BuildContext context) => Image.asset(material.value.icon);

  @override
  Widget suffixIcon(BuildContext context) => const Icon(FontAwesomeIcons.upRightFromSquare);

  @override
  String title(BuildContext context) => material.value.name;

  @override
  String description(BuildContext context) => material.key;

  @override
  void activate(BuildContext context, PassingRef ref) {
    onSelect?.call(material);
  }
}

class MaterialPropertyFilter extends SearchFilter {
  const MaterialPropertyFilter(this.property, {this.canRemove = false});

  final MaterialProperty property;

  @override
  final bool canRemove;

  @override
  String get title => property.name;

  @override
  Color get color => property.color;
  @override
  IconData get icon => property.icon;
}

extension _SearchBuilderX on SearchBuilder {
  void fetchMaterials({
    Function(CombinedMaterial)? onSelect,
    bool disabled = false,
  }) {
    fetch(MaterialsFetcher(onSelect: onSelect, disabled: disabled));
  }

  void properties(List<MaterialProperty> properties) {
    for (final property in properties) {
      filter(MaterialPropertyFilter(property));
    }
  }
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

  void _update(WidgetRef ref, String value) {
    ref.read(inspectingEntryDefinitionProvider)?.updateField(ref.passing, path, value.toUpperCase());
  }

  void _select(WidgetRef ref, List<MaterialProperty> properties) {
    ref.read(searchProvider.notifier).asBuilder()
      ..properties(properties)
      ..fetchMaterials(onSelect: (material) => _update(ref, material.key))
      ..open();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, ""));
    final propertiesModifier = field.getModifier("material_properties");
    final properties = propertiesModifier != null
        ? ref.watch(materialPropertiesProvider(propertiesModifier.data))
        : <MaterialProperty>[];

    final currentValue = value.isEmpty ? "air" : value.toLowerCase();
    final currentMaterial = materials[currentValue];
    final hasMaterial = currentMaterial != null;

    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: InkWell(
        onTap: () => _select(ref, properties),
        borderRadius: BorderRadius.circular(8),
        child: Padding(
          padding: const EdgeInsets.only(right: 16),
          child: Row(
            children: [
              if (hasMaterial)
                Expanded(child: _MaterialItem(id: currentValue, material: currentMaterial))
              else
                Expanded(child: Text("Select a material", style: Theme.of(context).inputDecorationTheme.hintStyle)),
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
    );
  }
}

class _MaterialItem extends StatelessWidget {
  const _MaterialItem({required this.id, required this.material});

  final String id;
  final MinecraftMaterial material;

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
    );
  }
}
