import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/editors/name.dart";
import "package:typewriter/widgets/inspector/editors/object.dart";
import "package:typewriter/widgets/inspector/heading.dart";
import "package:typewriter/widgets/inspector/operations.dart";
import "package:typewriter/widgets/select_entries.dart";

part "inspector.g.dart";

final selectedEntryIdProvider = StateProvider<String>((ref) => "");

@riverpod
Entry? selectedEntry(SelectedEntryRef ref) {
  final selectedEntryId = ref.watch(selectedEntryIdProvider);
  final page = ref.watch(currentPageProvider);
  return page?.entries.firstWhereOrNull((e) => e.id == selectedEntryId);
}

class Inspector extends HookConsumerWidget {
  const Inspector({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedEntry = ref.watch(selectedEntryProvider);
    final isSelectingEntries = ref.watch(isSelectingEntriesProvider);

    // When we are selecting entries, we want a special inspector that allows
    // us to select entries.
    if (isSelectingEntries) {
      return const EntriesSelectorInspector();
    }

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12),
      child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 400),
        child: selectedEntry != null ? _EntryInspector(key: ValueKey(selectedEntry.id)) : const _EmptyInspector(),
      ),
    );
  }
}

/// The content of the inspector when no entry is selected.
class _EmptyInspector extends HookConsumerWidget {
  const _EmptyInspector();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text("Inspector"),
        const SizedBox(height: 12),
        Text(
          "Select an entry to edit it's properties",
          style: Theme.of(context).textTheme.bodySmall,
        ),
        const SizedBox(height: 40),
      ],
    );
  }
}

@riverpod
EntryDefinition? entryDefinition(EntryDefinitionRef ref) {
  final entry = ref.watch(selectedEntryProvider);
  final adapterEntry = ref.watch(entryBlueprintProvider(entry?.type ?? ""));
  if (entry == null || adapterEntry == null) {
    return null;
  }
  return EntryDefinition(entry: entry, adapterEntry: adapterEntry);
}

class EntryDefinition {
  EntryDefinition({
    required this.entry,
    required this.adapterEntry,
  });
  final Entry entry;
  final EntryBlueprint adapterEntry;

  void updateEntry(PassingRef ref, Entry entry) {
    final page = ref.read(currentPageProvider);
    page?.insertEntry(ref, entry);
  }

  void updateField(PassingRef ref, String field, dynamic value) {
    final entry = this.entry.copyWith(field, value);
    updateEntry(ref, entry);

    // Update the communicator so it can synchronize the changes.
    final page = ref.read(currentPageProvider);
    if (page == null) return;
    ref.read(communicatorProvider).updateEntry(page.name, entry.id, field, value);
  }
}

/// The content of the inspector when an dynamic entry is selected.
class _EntryInspector extends HookConsumerWidget {
  const _EntryInspector({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final object = ref.watch(
      entryDefinitionProvider.select((def) => def?.adapterEntry.fields),
    );

    if (object == null) {
      return const SizedBox();
    }

    return SingleChildScrollView(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Heading(),
          const Divider(),
          const NameField(),
          const Divider(),
          ObjectEditor(
            path: "",
            object: object,
            ignoreFields: const ["id", "name"],
            defaultExpanded: true,
          ),
          const Divider(),
          const Operations(),
          const SizedBox(height: 30),
        ],
      ),
    );
  }
}
