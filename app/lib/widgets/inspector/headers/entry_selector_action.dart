import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/select_entries.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class EntrySelectorHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, FieldInfo field) =>
      field.hasModifier("entry-list");

  @override
  HeaderActionLocation location(String path, FieldInfo field) =>
      HeaderActionLocation.actions;

  @override
  EntrySelectorHeaderAction build(String path, FieldInfo field) =>
      EntrySelectorHeaderAction(path, field);
}

/// The button on a list of entries that allows to select multiple entries at once.
class EntrySelectorHeaderAction extends HookConsumerWidget {
  const EntrySelectorHeaderAction(
    this.path,
    this.field, {
    super.key,
  });

  final String path;
  final FieldInfo field;

  void _startSelection(PassingRef ref, String tag) {
    final currentEntries =
        ref.read(fieldValueProvider(path, [])) as List<dynamic>;
    final inspectingEntryId = ref.read(inspectingEntryIdProvider);
    if (inspectingEntryId == null) return;

    ref.read(entrySelectionProvider.notifier).startSelection(
      tag,
      selectedEntries: currentEntries.map((e) => e as String).toList(),
      excludedEntries: [inspectingEntryId],
      onSelectionChanged: (ref, selectedEntries) {
        ref.read(inspectingEntryDefinitionProvider)?.updateField(
              ref.passing,
              path,
              selectedEntries,
            );
      },
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tag = field.getModifier("entry-list")?.data ?? "";
    return Tooltip(
      message: "Select multiple entries",
      child: Material(
        borderRadius: BorderRadius.circular(4),
        color: Colors.deepPurple,
        child: InkWell(
          borderRadius: const BorderRadius.all(Radius.circular(4)),
          onTap: () => _startSelection(ref.passing, tag),
          child: const Padding(
            padding: EdgeInsets.all(6.0),
            child: FaIcon(
              FontAwesomeIcons.objectGroup,
              size: 16,
              color: Colors.white,
            ),
          ),
        ),
      ),
    );
  }
}
