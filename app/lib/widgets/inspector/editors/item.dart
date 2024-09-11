import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/error_box.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class ItemEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "item";
  @override
  Widget build(String path, DataBlueprint dataBlueprint) =>
      ItemEditor(path: path, customBlueprint: dataBlueprint as CustomBlueprint);
}

class ItemEditor extends HookConsumerWidget {
  const ItemEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });

  final String path;
  final CustomBlueprint customBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // final value = ref.watch(fieldValueProvider(path));
    final objectBlueprint = customBlueprint.shape is ObjectBlueprint
        ? customBlueprint.shape as ObjectBlueprint?
        : null;
    if (objectBlueprint == null) {
      return ErrorBox(
        message: "Could not find subfields for item field: $path",
      );
    }
    return FieldHeader(
      path: path,
      dataBlueprint: customBlueprint,
      canExpand: true,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const SizedBox(height: 8),
          _FieldSelector(path: path, objectBlueprint: objectBlueprint),
          _ItemEditors(path: path, objectBlueprint: objectBlueprint),
          // Text("Item: $value"),
          // Text("Info: ${info.DataBlueprint}"),
        ],
      ),
    );
  }
}

class _FieldSelector extends HookConsumerWidget {
  const _FieldSelector({
    required this.path,
    required this.objectBlueprint,
  });

  final String path;
  final ObjectBlueprint objectBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final hovering = useState(false);
    final selected = objectBlueprint.fields.keys
        .where(
          (id) =>
              ref.watch(fieldValueProvider("$path.$id.enabled", false)) == true,
        )
        .toList();

    final showAll = hovering.value || selected.isEmpty;

    return MouseRegion(
      onEnter: (_) => hovering.value = true,
      onExit: (_) => hovering.value = false,
      child: SizedBox(
        width: double.infinity,
        child: AnimatedSize(
          duration: 1200.ms,
          curve: Curves.elasticOut,
          alignment: Alignment.topCenter,
          clipBehavior: Clip.none,
          child: Wrap(
            spacing: 6,
            runSpacing: 6,
            children: [
              for (final MapEntry(key: id, value: dataBlueprint)
                  in objectBlueprint.fields.entries)
                if (showAll || selected.contains(id)) _chip(id, dataBlueprint),
            ].animate(interval: 40.ms).fadeIn(duration: 250.ms).scaleXY(
                  begin: 0.6,
                  end: 1.0,
                  duration: 800.ms,
                  curve: Curves.elasticOut,
                ),
          ),
        ),
      ),
    );
  }

  Widget _chip(String id, DataBlueprint dataBlueprint) {
    if (dataBlueprint is! CustomBlueprint) return const SizedBox();
    final shapeBlueprint = dataBlueprint.shape;
    if (shapeBlueprint is! ObjectBlueprint) return const SizedBox();
    if (!shapeBlueprint.fields.containsKey("value")) return const SizedBox();
    final valueBlueprint = shapeBlueprint.fields["value"];
    if (valueBlueprint == null) return const SizedBox();
    return _ItemChip(
      key: ValueKey(id),
      path: "$path.$id",
      id: id,
      dataBlueprint: valueBlueprint,
    );
  }
}

class _ItemChip extends HookConsumerWidget {
  const _ItemChip({
    required this.path,
    required this.id,
    required this.dataBlueprint,
    super.key,
  });

  final String path;
  final String id;
  final DataBlueprint dataBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final enabled = ref.watch(fieldValueProvider("$path.enabled", false));

    final foregroundColor = enabled
        ? Theme.of(context).colorScheme.primary
        : Theme.of(context).colorScheme.onSurface.withValues(alpha:0.7);

    return InputChip(
      label: Text(id.titleCase(), style: TextStyle(color: foregroundColor)),
      avatar: Iconify(
        dataBlueprint.get("icon"),
        size: 14,
        color: foregroundColor,
      ),
      backgroundColor: Theme.of(context).colorScheme.onSurface.withValues(alpha:0.1),
      selectedColor: Theme.of(context).colorScheme.primary.withValues(alpha:0.1),
      side: BorderSide.none,
      showCheckmark: false,
      selected: enabled,
      onSelected: (value) {
        ref
            .read(inspectingEntryDefinitionProvider)
            ?.updateField(ref.passing, "$path.enabled", value);
      },
    );
  }
}

class _ItemEditors extends HookConsumerWidget {
  const _ItemEditors({
    required this.path,
    required this.objectBlueprint,
  });

  final String path;
  final ObjectBlueprint objectBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return AnimatedSize(
      duration: 200.ms,
      curve: Curves.easeOutCubic,
      alignment: Alignment.topCenter,
      clipBehavior: Clip.none,
      child: Column(
        children: [
          for (final MapEntry(key: id, value: dataBlueprint)
              in objectBlueprint.fields.entries)
            _editor(id, dataBlueprint),
        ],
      ),
    );
  }

  Widget _editor(String id, DataBlueprint dataBlueprint) {
    if (dataBlueprint is! CustomBlueprint) return const SizedBox();
    final shapeBlueprint = dataBlueprint.shape;
    if (shapeBlueprint is! ObjectBlueprint) return const SizedBox();
    if (!shapeBlueprint.fields.containsKey("value")) return const SizedBox();
    final valueBlueprint = shapeBlueprint.fields["value"];
    if (valueBlueprint == null) return const SizedBox();
    return _ItemEditor(
      key: ValueKey(id),
      path: "$path.$id",
      dataBlueprint: valueBlueprint,
    );
  }
}

class _ItemEditor extends HookConsumerWidget {
  const _ItemEditor({
    required this.path,
    required this.dataBlueprint,
    super.key,
  });

  final String path;
  final DataBlueprint dataBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final enabled = ref.watch(fieldValueProvider("$path.enabled", false));

    if (!enabled) {
      return const SizedBox();
    }

    return Column(
      children: [
        const SizedBox(height: 8),
        FieldHeader(
          dataBlueprint: dataBlueprint,
          path: path,
          child: FieldEditor(path: "$path.value", dataBlueprint: dataBlueprint),
        ),
      ],
    ).animate().fadeIn(duration: 250.ms).moveY(
          begin: 20,
          end: 0,
          duration: 500.ms,
          curve: Curves.easeOutCubic,
        );
  }
}
