import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/components/app/cord_property.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/info_action.dart";

class FloatRangeFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is CustomField && info.editor == "floatRange";

  @override
  Widget build(String path, FieldInfo info) =>
      FloatRangeEditor(path: path, field: info as CustomField);
}

class FloatRangeEditor extends HookConsumerWidget {
  const FloatRangeEditor({
    required this.path,
    required this.field,
    super.key,
  });
  final String path;

  final CustomField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return FieldHeader(
      path: path,
      field: field,
      trailing: const [
        InfoHeaderAction(
          tooltip: "Max is included in the range.",
          icon: TWIcons.inclusive,
          color: Color(0xFF0ccf92),
          url: "",
        ),
      ],
      child: Row(
        children: [
          CordPropertyEditor(
            path: "$path.start",
            label: "Min",
            color: Colors.red,
          ),
          const Iconify(TWIcons.range),
          CordPropertyEditor(
            path: "$path.end",
            label: "Max",
            color: Colors.green,
          ),
        ],
      ),
    );
  }
}
