import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/color_filter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class OptionalEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is CustomField && info.editor == "optional";

  @override
  Widget build(String path, FieldInfo info) =>
      OptionalEditor(path: path, field: info as CustomField);
}

class OptionalEditor extends HookConsumerWidget {
  const OptionalEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final CustomField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final enabled = ref.watch(fieldValueProvider("$path.enabled", false));

    final subField = field.fieldInfo;
    if (subField == null) {
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
        .where((filter) => filter.shouldShow(subPath, subField))
        .toList();

    return FieldHeader(
      field: field,
      path: path,
      leading: _buildActions(
        HeaderActionLocation.leading,
        subFieldActions,
        subPath,
        subField,
        enabled: enabled,
      ),
      trailing: _buildActions(
        HeaderActionLocation.trailing,
        subFieldActions,
        subPath,
        subField,
        enabled: enabled,
      ),
      actions: _buildActions(
        HeaderActionLocation.actions,
        subFieldActions,
        subPath,
        subField,
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
                    type: subField,
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
    FieldInfo field, {
    bool enabled = true,
  }) {
    final children = actions
        .where((action) => action.location(path, field) == location)
        .map((action) => action.build(path, field))
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
