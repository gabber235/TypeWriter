import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/color_filter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class OptionalEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "optional";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => OptionalEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class OptionalEditor extends HookConsumerWidget {
  const OptionalEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });

  final String path;
  final CustomBlueprint customBlueprint;

  DataBlueprint? get subDataBlueprint {
    final shape = customBlueprint.shape;
    if (shape is! ObjectBlueprint) return null;
    if (!shape.fields.containsKey("value")) return null;
    return shape.fields["value"];
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final enabled = ref.watch(fieldValueProvider("$path.enabled", false));

    final subDataBlueprint = useMemoized(() => this.subDataBlueprint);
    if (subDataBlueprint == null) {
      return Container(
        padding: const EdgeInsets.all(8),
        decoration: BoxDecoration(
          color: Colors.redAccent,
          borderRadius: BorderRadius.circular(4),
        ),
        child: Text(
          "Invalid subfield, try to restart the server",
          style: Theme.of(context).textTheme.bodySmall,
        ),
      );
    }

    final subPath = "$path.value";
    final headerActionFilters = ref.watch(headerActionFiltersProvider);
    final subFieldActions = headerActionFilters
        .where((filter) => filter.shouldShow(subPath, subDataBlueprint))
        .toList();

    return FieldHeader(
      path: path,
      dataBlueprint: customBlueprint,
      leading: _buildActions(
        HeaderActionLocation.leading,
        subFieldActions,
        subPath,
        subDataBlueprint,
        enabled: enabled,
      ),
      trailing: _buildActions(
        HeaderActionLocation.trailing,
        subFieldActions,
        subPath,
        subDataBlueprint,
        enabled: enabled,
      ),
      actions: _buildActions(
        HeaderActionLocation.actions,
        subFieldActions,
        subPath,
        subDataBlueprint,
        enabled: enabled,
      ),
      child: Row(
        children: [
          Checkbox(
            value: enabled,
            onChanged: (value) {
              ref
                  .read(inspectingEntryDefinitionProvider)
                  ?.updateField(ref.passing, "$path.enabled", value ?? false);
            },
          ),
          Expanded(
            child: AnimatedOpacity(
              opacity: enabled ? 1 : 0.6,
              duration: 200.ms,
              curve: Curves.easeOut,
              child: MouseRegion(
                cursor:
                    enabled ? MouseCursor.defer : SystemMouseCursors.forbidden,
                child: AbsorbPointer(
                  absorbing: !enabled,
                  child: FieldEditor(
                    path: "$path.value",
                    dataBlueprint: subDataBlueprint,
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  List<Widget> _buildActions(
    HeaderActionLocation location,
    List<HeaderActionFilter> actions,
    String path,
    DataBlueprint dataBlueprint, {
    bool enabled = true,
  }) {
    final children = actions
        .where((action) => action.location(path, dataBlueprint) == location)
        .map((action) => action.build(path, dataBlueprint))
        .toList();
    if (enabled) {
      return children;
    }

    return [
      for (final child in children)
        MouseRegion(
          cursor: SystemMouseCursors.forbidden,
          child: AbsorbPointer(
            child: AnimatedOpacity(
              opacity: 0.6,
              duration: 200.ms,
              curve: Curves.easeOut,
              child: ColorFiltered(
                colorFilter: greyscale,
                child: child,
              ),
            ),
          ),
        ),
    ];
  }
}
