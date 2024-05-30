import "package:auto_size_text/auto_size_text.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:fuzzy/fuzzy.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/potion_effects.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "potion_effect.g.dart";

String _potionEffectIconUrl(String potionEffect) {
  return "$mcmetaUrl/assets/assets/minecraft/textures/mob_effect/$potionEffect.png";
}

@riverpod
Fuzzy<String> _fuzzyPotionEffects(_FuzzyPotionEffectsRef ref) {
  final provider = ref.watch(potionEffectsProvider);
  final effects = provider.map(
    data: (data) => data.value,
    loading: (_) => const <String>[],
    error: (_) => const <String>[],
  );

  return Fuzzy(
    effects,
    options: FuzzyOptions(
      keys: [
        WeightedKey(
          name: "name",
          getter: (effect) => effect,
          weight: 1,
        ),
      ],
    ),
  );
}

class PotionEffectsFetcher extends SearchFetcher {
  const PotionEffectsFetcher({
    this.onSelect,
    this.disabled = false,
  });

  final FutureOr<bool?> Function(String)? onSelect;

  @override
  final bool disabled;

  @override
  String get title => "Potion Effects";

  @override
  List<SearchElement> fetch(PassingRef ref) {
    final search = ref.read(searchProvider);
    if (search == null) return [];
    final fuzzy = ref.read(_fuzzyPotionEffectsProvider);

    final results = fuzzy.search(search.query);

    return results
        .map(
          (result) => PotionEffectSearchElement(
            potionEffect: result.item,
            onSelect: onSelect,
          ),
        )
        .toList();
  }

  @override
  PotionEffectsFetcher copyWith({
    bool? disabled,
  }) {
    return PotionEffectsFetcher(
      onSelect: onSelect,
      disabled: disabled ?? this.disabled,
    );
  }
}

class PotionEffectSearchElement extends SearchElement {
  PotionEffectSearchElement({
    required this.potionEffect,
    this.onSelect,
  }) {
    category = PotionEffectCategory.fromPotionEffect(potionEffect);
  }

  final String potionEffect;
  final FutureOr<bool?> Function(String)? onSelect;

  late PotionEffectCategory category;

  @override
  String get title => potionEffect.formatted;

  @override
  Color color(BuildContext context) {
    return category.color;
  }

  @override
  String description(BuildContext context) => category.name;

  @override
  Widget icon(BuildContext context) {
    return Image.network(_potionEffectIconUrl(potionEffect), scale: 0.5);
  }

  @override
  Widget suffixIcon(BuildContext context) =>
      const Iconify(TWIcons.externalLink);

  @override
  List<SearchAction> actions(PassingRef ref) {
    return [
      const SearchAction(
        "Select",
        TWIcons.check,
        SingleActivator(LogicalKeyboardKey.enter),
      ),
    ];
  }

  @override
  Future<bool> activate(BuildContext context, PassingRef ref) async {
    return await onSelect?.call(potionEffect) ?? true;
  }
}

extension PotionEffectSearchBuilderX on SearchBuilder {
  void fetchPotionEffect({
    FutureOr<bool?> Function(String)? onSelect,
    bool disabled = false,
  }) =>
      fetch(
        PotionEffectsFetcher(
          onSelect: onSelect,
          disabled: disabled,
        ),
      );
}

class PotionEffectEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is CustomField && info.editor == "potionEffectType";
  @override
  Widget build(String path, FieldInfo info) =>
      PotionEffectEditor(path: path, field: info as CustomField);
}

class PotionEffectEditor extends HookConsumerWidget {
  const PotionEffectEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final CustomField field;

  bool? _update(WidgetRef ref, String value) {
    ref
        .read(inspectingEntryDefinitionProvider)
        ?.updateField(ref.passing, path, value);
    return null;
  }

  void _select(WidgetRef ref) {
    ref.read(searchProvider.notifier).asBuilder()
      ..fetchPotionEffect(
        onSelect: (potionEffect) => _update(ref, potionEffect),
      )
      ..open();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, "speed"));

    // While we are editing, we need to keep the potion effects loaded
    ref.watch(potionEffectsProvider);

    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: InkWell(
        onTap: () => _select(ref),
        borderRadius: BorderRadius.circular(8),
        child: Padding(
          padding: const EdgeInsets.only(right: 16),
          child: Row(
            children: [
              Expanded(child: _PotionEffectItem(potionEffect: value)),
              const SizedBox(width: 12),
              Iconify(
                TWIcons.caretDown,
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

class _PotionEffectItem extends HookConsumerWidget {
  const _PotionEffectItem({
    required this.potionEffect,
  });

  final String potionEffect;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final category = PotionEffectCategory.fromPotionEffect(potionEffect);
    return ListTile(
      leading: Padding(
        padding: const EdgeInsets.symmetric(vertical: 3.0),
        child: AspectRatio(
          aspectRatio: 1,
          child: Image.network(_potionEffectIconUrl(potionEffect), scale: 0.5),
        ),
      ),
      title: AutoSizeText(
        potionEffect.formatted,
        maxLines: 1,
      ),
      subtitle: Text(
        category.name,
        style: Theme.of(context)
            .textTheme
            .bodySmall
            ?.copyWith(color: category.color),
      ),
    );
  }
}
