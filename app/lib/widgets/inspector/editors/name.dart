import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/utils/extensions.dart';
import 'package:typewriter/widgets/inspector.dart';
import 'package:typewriter/widgets/inspector/section_title.dart';
import 'package:typewriter/widgets/inspector/single_line_text_field.dart';

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
        SingleLineTextField(
          text: def?.entry.name,
          onChanged: (value) {
            def?.updateField(ref, "name", value);
          },
          inputFormatters: [
            snakeCaseFormatter(),
            FilteringTextInputFormatter.allow(RegExp(r'[a-z0-9_.]')),
          ],
          hintText: "Enter a name",
          icon: FontAwesomeIcons.signature,
        ),
      ],
    );
  }
}
