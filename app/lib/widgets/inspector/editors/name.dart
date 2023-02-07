import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/formatted_text_field.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/inspector.dart";
import "package:typewriter/widgets/inspector/section_title.dart";

class NameField extends HookConsumerWidget {
  const NameField({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedEntryId = ref.watch(inspectingEntryIdProvider);
    final focus = useFocusNode();
    useFocusedBasedCurrentEditingField(focus, ref, "name");
    final def = ref.watch(inspectingEntryDefinitionProvider);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const SectionTitle(title: "Name"),
        const SizedBox(height: 1),
        WritersIndicator(
          filter: (writer) {
            if (writer.entryId.isNullOrEmpty) return false;
            if (writer.entryId != selectedEntryId) return false;
            if (writer.field.isNullOrEmpty) return false;
            return writer.field == "name";
          },
          shift: (_) => const Offset(15, 0),
          child: FormattedTextField(
            focus: focus,
            text: def?.entry.name,
            onChanged: (value) {
              def?.updateField(ref.passing, "name", value);
            },
            inputFormatters: [
              snakeCaseFormatter(),
              FilteringTextInputFormatter.allow(RegExp("[a-z0-9_.]")),
            ],
            hintText: "Enter a name",
            icon: FontAwesomeIcons.signature,
          ),
        ),
      ],
    );
  }
}
