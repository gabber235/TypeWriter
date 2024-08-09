import "package:duration/duration.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/validated_inspector_text_field.dart";

class DurationEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is CustomField && info.editor == "duration";
  @override
  Widget build(String path, FieldInfo info) =>
      DurationEditor(path: path, field: info as CustomField);
}

class DurationEditor extends HookConsumerWidget {
  const DurationEditor({
    required this.path,
    required this.field,
    super.key,
  });
  final String path;
  final CustomField field;
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return WritersIndicator(
      provider: fieldWritersProvider(path),
      shift: (_) => const Offset(15, 0),
      child: ValidatedInspectorTextField<int>(
        path: path,
        defaultValue: 0,
        inputFormatters: [
          FilteringTextInputFormatter.allow(RegExp(r"[\dwdhminsu ]")),
        ],
        icon: TWIcons.stopwatch,
        deserialize: (value) {
          final parsedValue = value.milliseconds;
          return prettyDuration(
            parsedValue,
            abbreviated: true,
            delimiter: " ",
            spacer: "",
            tersity: DurationTersity.millisecond,
          );
        },
        serialize: (value) {
          final parsedValue = parseDuration(value, separator: " ");
          return parsedValue.inMilliseconds;
        },
        formatted: (value) {
          final parsedValue = value.milliseconds;
          final formatted = prettyDuration(
            parsedValue,
            abbreviated: false,
            tersity: DurationTersity.millisecond,
          );
          return "Valid Duration: $formatted";
        },
      ),
    );
  }
}
