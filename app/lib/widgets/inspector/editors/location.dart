import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/decorated_text_field.dart";
import "package:typewriter/widgets/components/general/formatted_text_field.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class LocationEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) => info is CustomField && info.editor == "location";

  @override
  Widget build(String path, FieldInfo info) => LocationEditor(path: path, field: info as CustomField);
}

class LocationEditor extends HookConsumerWidget {
  const LocationEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final CustomField field;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final withRotation = field.hasModifier("with_rotation");

    return Column(
      children: [
        _LocationWorldEditor(path: "$path.world"),
        const SizedBox(height: 8),
        Row(
          children: [
            _LocationPropertyEditor(path: "$path.x", label: "X", color: Colors.red),
            const SizedBox(width: 8),
            _LocationPropertyEditor(path: "$path.y", label: "Y", color: Colors.green),
            const SizedBox(width: 8),
            _LocationPropertyEditor(path: "$path.z", label: "Z", color: Colors.blue),
          ],
        ),
        if (withRotation) ...[
          const SizedBox(height: 8),
          Row(
            children: [
              _LocationPropertyEditor(path: "$path.yaw", label: "Yaw", color: Colors.deepPurpleAccent),
              const SizedBox(width: 8),
              _LocationPropertyEditor(path: "$path.pitch", label: "Pitch", color: Colors.amberAccent),
            ],
          )
        ],
      ],
    );
  }
}

class _LocationWorldEditor extends HookConsumerWidget {
  const _LocationWorldEditor({
    required this.path,
    super.key,
  });

  final String path;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    final value = ref.watch(fieldValueProvider(path, ""));

    return FormattedTextField(
      focus: focus,
      text: value,
      icon: FontAwesomeIcons.earthAmericas,
      hintText: "World",
      onChanged: (value) {
        ref.read(inspectingEntryDefinitionProvider)?.updateField(ref.passing, path, value);
      },
    );
  }
}

class _LocationPropertyEditor extends HookConsumerWidget {
  const _LocationPropertyEditor({
    required this.path,
    required this.label,
    required this.color,
    super.key,
  });
  final String path;
  final String label;
  final Color color;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focusNode = useFocusNode();
    final value = ref.watch(fieldValueProvider(path, 0.0));

    return Flexible(
      child: DecoratedTextField(
        focus: focusNode,
        text: value.toString(),
        keyboardType: TextInputType.number,
        inputFormatters: [FilteringTextInputFormatter.allow(RegExp(r"^\-?\d*\.?\d*"))],
        onChanged: (value) {
          final number = double.tryParse(value);
          if (number == null) return;
          ref.read(inspectingEntryDefinitionProvider)?.updateField(ref.passing, path, number);
        },
        decoration: InputDecoration(
          prefixText: "$label: ",
          prefixStyle: TextStyle(color: color),
        ),
      ),
    );
  }
}
