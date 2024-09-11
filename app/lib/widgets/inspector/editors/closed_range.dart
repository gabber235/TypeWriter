import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/info_action.dart";

class ClosedRangeFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "closedRange";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => ClosedRangeEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class ClosedRangeEditor extends HookConsumerWidget {
  const ClosedRangeEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });
  final String path;

  final CustomBlueprint customBlueprint;

  DataBlueprint get startBlueprint {
    final object = customBlueprint.shape as ObjectBlueprint;
    return object.fields["start"]!;
  }

  DataBlueprint get endBlueprint {
    final object = customBlueprint.shape as ObjectBlueprint;
    return object.fields["end"]!;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return FieldHeader(
      path: path,
      dataBlueprint: customBlueprint,
      trailing: const [
        InfoHeaderAction(
          tooltip: "From start to end inclusive",
          icon: TWIcons.inclusive,
          color: Color(0xFF0ccf92),
          url: "",
        ),
      ],
      child: LayoutBuilder(
        builder: (context, constraints) {
          final width = (constraints.maxWidth - 24) / 2;
          return Row(
            children: [
              SizedBox(
                width: width,
                child: FieldEditor(
                  path: "$path.start",
                  dataBlueprint: startBlueprint,
                ),
              ),
              const Iconify(TWIcons.range),
              SizedBox(
                width: width,
                child: FieldEditor(
                  path: "$path.end",
                  dataBlueprint: endBlueprint,
                ),
              ),
            ],
          );
        },
      ),
    );
  }
}
