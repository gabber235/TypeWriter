import "package:dotted_border/dotted_border.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/general/admonition.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/editors/generic.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class VariableEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "var";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => VariableEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class VariableEditor extends HookConsumerWidget {
  const VariableEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });

  final String path;
  final CustomBlueprint customBlueprint;

  void _removeVariable(PassingRef ref) {
    ref
        .read(inspectingEntryDefinitionProvider)
        ?.updateField(ref, path, customBlueprint.defaultValue());
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path));
    final data = variableData(value);
    if (data == null) {
      return FieldEditor(path: path, dataBlueprint: customBlueprint.shape);
    }

    final variableEntryId = data.ref;
    if (variableEntryId == null) {
      return _buildError(ref.passing);
    }

    final variableBlueprintId =
        ref.watch(entryBlueprintIdProvider(variableEntryId));
    if (variableBlueprintId == null) {
      return _buildError(ref.passing);
    }
    final variableBlueprint =
        ref.watch(entryBlueprintProvider(variableBlueprintId));
    if (variableBlueprint == null) {
      return _buildError(ref.passing);
    }

    final variableDataBlueprint = variableBlueprint.variableDataBlueprint;

    return Generic(
      dataBlueprint: customBlueprint.shape,
      child: Padding(
        padding: const EdgeInsets.all(4.0),
        child: DottedBorder(
          color: Colors.green,
          borderType: BorderType.RRect,
          strokeWidth: 2,
          dashPattern: const [5, 5],
          radius: const Radius.circular(4),
          child: Material(
            color: Colors.green.withValues(alpha: 0.1),
            borderRadius: BorderRadius.circular(4),
            child: Padding(
              padding: const EdgeInsets.all(4),
              child: Column(
                children: [
                  ContextMenuRegion(
                    builder: (context) => [
                      ContextMenuTile.button(
                        title: "Navigate to entry",
                        icon: TWIcons.pencil,
                        onTap: () {
                          ref
                              .read(inspectingEntryIdProvider.notifier)
                              .navigateAndSelectEntry(
                                ref.passing,
                                variableEntryId,
                              );
                        },
                      ),
                    ],
                    child: Row(
                      children: [
                        Expanded(
                          child: InkWell(
                            onTap: () {
                              ref
                                  .read(inspectingEntryIdProvider.notifier)
                                  .navigateAndSelectEntry(
                                    ref.passing,
                                    variableEntryId,
                                  );
                            },
                            child: FakeEntryNode(entryId: variableEntryId),
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 8),
                  if (variableDataBlueprint != null)
                    Header(
                      expanded: ValueNotifier(true),
                      canExpand: false,
                      depth: Header.maybeOf(context)?.depth ?? 0,
                      path: "$path.data",
                      child: FieldEditor(
                        path: "$path.data",
                        dataBlueprint: variableDataBlueprint,
                      ),
                    ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildError(PassingRef ref) {
    return Admonition.danger(
      onTap: () => _removeVariable(ref),
      child: Text.rich(
        TextSpan(
          text: "Could not find Variable Reference, ",
          children: [
            TextSpan(
              text: "click to reset to default",
              style: TextStyle(
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

VariableData? variableData(dynamic value) {
  if (value is! Map<String, dynamic>) {
    return null;
  }
  if (!value.containsKey("_kind")) {
    return null;
  }
  return VariableData.fromJson(value);
}

class VariableData {
  const VariableData(this._kind, this.ref, this.data);

  factory VariableData.fromJson(Map<String, dynamic> json) => VariableData(
        json["_kind"] as String,
        json["ref"] as String?,
        json["data"] as Map<String, dynamic>,
      );

  final String _kind;
  final String? ref;
  final Map<String, dynamic> data;

  Map<String, dynamic> toJson() => {
        "_kind": _kind,
        "ref": ref,
        "data": data,
      };
}
