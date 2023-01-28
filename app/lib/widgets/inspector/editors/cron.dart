import "package:flutter/material.dart";
import 'package:flutter/services.dart';
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import 'package:typewriter/utils/cron.dart';
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/validated_text_field.dart";

class CronEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is CustomField && info.editor == "cron";

  @override
  Widget build(String path, FieldInfo info) => CronEditor(path: path, field: info as CustomField);
}

class CronEditor extends HookConsumerWidget {
  const CronEditor({
    required this.path,
    required this.field,
    super.key,
  });
  final String path;
  final CustomField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return ValidatedTextField<String>(
      path: path,
      defaultValue: "0 0 1 1 *",
      icon: FontAwesomeIcons.solidClock,
      inputFormatters: [
        FilteringTextInputFormatter.allow(
          RegExp(r"([0-9\*\/\-\,\?MONTUEWENTHUFRISATSUN JANFEBMARAPRMAYJUNJULAUGSEPOCTNOVDEC LW#]*)"),
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
        return CronExpression.parse(value)?.toHumanReadableString() ?? "Invalid cron expression";
      },
    );
  }
}
