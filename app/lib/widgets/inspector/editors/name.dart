import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/formatted_text_field.dart";
import "package:typewriter/widgets/inspector/section_title.dart";

class NameField extends HookConsumerWidget {
  const NameField({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final def = ref.watch(entryDefinitionProvider);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const SectionTitle(title: "Name"),
        const SizedBox(height: 1),
        FormattedTextField(
          text: def?.entry.name,
          onChanged: (value) {
            def?.updateField(ref, "name", value);
          },
          inputFormatters: [
            snakeCaseFormatter(),
            FilteringTextInputFormatter.allow(RegExp("[a-z0-9_.]")),
          ],
          hintText: "Enter a name",
          icon: FontAwesomeIcons.signature,
        ),
      ],
    );
  }
}
