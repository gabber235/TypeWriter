import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
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
  bool canEdit(FieldInfo info) => info is CustomField && info.editor == "item";
  @override
  Widget build(String path, FieldInfo info) =>
      ItemEditor(path: path, info: info as CustomField);
}

class ItemEditor extends HookConsumerWidget {
  const ItemEditor({
    required this.path,
    required this.info,
    super.key,
  });

  final String path;
  final CustomField info;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // final value = ref.watch(fieldValueProvider(path));
    final objectField =
        info.fieldInfo is ObjectField ? info.fieldInfo as ObjectField? : null;
    if (objectField == null) {
      return ErrorBox(
        message: "Could not find subfields for item field: $path",
      );
    }
    return FieldHeader(
      path: path,
      field: info,
      canExpand: true,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const SizedBox(height: 8),
          _FieldSelector(path: path, objectField: objectField),
          _ItemEditors(path: path, objectField: objectField),
          // Text("Item: $value"),
          // Text("Info: ${info.fieldInfo}"),
        ],
      ),
    );
  }
}

class _FieldSelector extends HookConsumerWidget {
  const _FieldSelector({
    required this.path,
    required this.objectField,
  });

  final String path;
  final ObjectField objectField;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final hovering = useState(false);
    final selected = objectField.fields.keys
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
              for (final MapEntry(key: id, value: info)
                  in objectField.fields.entries)
                if (showAll || selected.contains(id))
                  _ItemChip(
                    key: ValueKey(id),
                    path: "$path.$id",
                    id: id,
                    info: info,
                  ),
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
}

class _ItemChip extends HookConsumerWidget {
  const _ItemChip({
    required this.path,
    required this.id,
    required this.info,
    super.key,
  });

  final String path;
  final String id;
  final FieldInfo info;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final enabled = ref.watch(fieldValueProvider("$path.enabled", false));

    final foregroundColor = enabled
        ? Theme.of(context).colorScheme.primary
        : Theme.of(context).colorScheme.onSurface.withOpacity(0.7);

    return InputChip(
      label: Text(id.capitalize, style: TextStyle(color: foregroundColor)),
      avatar: Iconify(
        info.get("icon"),
        size: 14,
        color: foregroundColor,
      ),
      backgroundColor: Theme.of(context).colorScheme.onSurface.withOpacity(0.1),
      selectedColor: Theme.of(context).colorScheme.primary.withOpacity(0.1),
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
    required this.objectField,
  });

  final String path;
  final ObjectField objectField;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return AnimatedSize(
      duration: 200.ms,
      curve: Curves.easeOutCubic,
      alignment: Alignment.topCenter,
      clipBehavior: Clip.none,
      child: Column(
        children: [
          for (final MapEntry(key: id, value: info)
              in objectField.fields.entries)
            _ItemEditor(
              key: ValueKey(id),
              path: "$path.$id",
              field: info,
            ),
        ],
      ),
    );
  }
}

class _ItemEditor extends HookConsumerWidget {
  const _ItemEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final FieldInfo field;

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
          field: field,
          path: path,
          child: FieldEditor(path: "$path.value", type: field),
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
