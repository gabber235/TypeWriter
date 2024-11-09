import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/cron.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/validated_inspector_text_field.dart";

class CronEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "cron";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) =>
      CronEditor(path: path, customBlueprint: dataBlueprint as CustomBlueprint);
}

class CronEditor extends HookConsumerWidget {
  const CronEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });
  final String path;
  final CustomBlueprint customBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return WritersIndicator(
      provider: fieldWritersProvider(path),
      shift: (_) => const Offset(15, 0),
      child: ValidatedInspectorTextField<String>(
        path: path,
        defaultValue: "0 0 1 1 *",
        icon: TWIcons.clock,
        inputFormatters: [
          FilteringTextInputFormatter.allow(
            RegExp(
              r"([0-9\*\/\-\,\?MONTUEWENTHUFRISATSUN JANFEBMARAPRMAYJUNJULAUGSEPOCTNOVDEC LW#]*)",
            ),
          ),
        ],
        keepValidVisibleWhileFocused: true,
        deserialize: (value) {
          return value;
        },
        serialize: (value) {
          final cron = CronExpression.parse(value);
          if (cron == null) {
            throw const FormatException("Invalid cron expression");
          }
          return value;
        },
        formatted: (value) {
          final cron = CronExpression.parse(value);
          if (cron == null) {
            return "Invalid cron expression";
          }
          return "Valid Cron: ${cron.toHumanReadableString()}";
        },
      ),
    );
  }
}
