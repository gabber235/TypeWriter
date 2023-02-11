import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "page.freezed.dart";
part "page.g.dart";

@riverpod
List<Page> pages(PagesRef ref) {
  return ref.watch(bookProvider).pages;
}

@riverpod
Page? page(PageRef ref, String name) {
  return ref.watch(pagesProvider).firstWhereOrNull((page) => page.name == name);
}

@riverpod
String? entriesPage(EntriesPageRef ref, String entryId) {
  return ref.watch(pagesProvider).firstWhereOrNull((page) => page.entries.any((entry) => entry.id == entryId))?.name;
}

@riverpod
Entry? entry(EntryRef ref, String pageId, String entryId) {
  return ref.watch(pageProvider(pageId))?.entries.firstWhereOrNull((entry) => entry.id == entryId);
}

@riverpod
Entry? globalEntry(GlobalEntryRef ref, String entryId) {
  final page = ref.watch(entriesPageProvider(entryId));
  if (page == null) {
    return null;
  }
  return ref.watch(entryProvider(page, entryId));
}

@riverpod
MapEntry<String, Entry>? globalEntryWithPage(GlobalEntryWithPageRef ref, String entryId) {
  final page = ref.watch(entriesPageProvider(entryId));
  if (page == null) {
    return null;
  }
  final entry = ref.watch(entryProvider(page, entryId));
  if (entry == null) {
    return null;
  }
  return MapEntry(page, entry);
}

@freezed
class Page with _$Page {
  const factory Page({
    required String name,
    @Default([]) List<Entry> entries,
  }) = _Page;

  factory Page.fromJson(Map<String, dynamic> json) => _$PageFromJson(json);
}

extension PageExtension on Page {
  void updatePage(PassingRef ref, Page Function(Page) update) {
    // If multiple updates are done at the same time, `this` might be outdated. So we need to get the latest version.
    final currentPage = ref.read(pageProvider(name));
    if (currentPage == null) {
      return;
    }
    final newPage = update(currentPage);
    ref.read(bookProvider.notifier).insertPage(newPage);
  }

  Future<void> createEntry(PassingRef ref, Entry entry) async {
    updatePage(
      ref,
      (page) => _insertEntry(page, entry),
    );
    await ref.read(communicatorProvider).createEntry(name, entry);
  }

  Future<void> updateEntireEntry(PassingRef ref, Entry entry) async {
    updatePage(
      ref,
      (page) => _insertEntry(page, entry),
    );
    ref.read(communicatorProvider).updateEntireEntry(name, entry);
  }

  void updateEntryValue(PassingRef ref, Entry entry, String path, dynamic value) {
    updatePage(
      ref,
      (page) => _insertEntry(page, entry.copyWith(path, value)),
    );
    ref.read(communicatorProvider).updateEntry(name, entry.id, path, value);
  }

  /// This should only be used to sync the entry from the server.
  void syncInsertEntry(PassingRef ref, Entry entry) {
    updatePage(ref, (page) => _insertEntry(page, entry));
  }

  Page _insertEntry(Page page, Entry entry) {
    // If the entry already exists, replace it.
    final entryExists = page.entries.any((e) => e.id == entry.id);
    if (entryExists) {
      return page.copyWith(
        entries: page.entries.map((e) => e.id == entry.id ? entry : e).toList(),
      );
    }

    // Otherwise, just add it to the end.
    return page.copyWith(
      entries: [...page.entries, entry],
    );
  }

  void deleteEntry(PassingRef ref, Entry entry) {
    ref.read(communicatorProvider).deleteEntry(name, entry.id);
    updatePage(
      ref,
      (page) => page.copyWith(
        entries: [
          ...page.entries.where((e) => e.id != entry.id).map((e) => _removedReferencesFromEntry(ref, e, entry.id))
        ],
      ),
    );
    // Also delete all references to this entry from other pages.
    ref.read(bookProvider).pages.where((page) => page.name != name).forEach((page) {
      page.removeReferencesTo(ref, entry.id);
    });

    // If the entry is selected, deselect it.
    if (ref.read(inspectingEntryIdProvider) == entry.id) {
      ref.read(inspectingEntryIdProvider.notifier).clearSelection();
    }
  }

  void removeReferencesTo(PassingRef ref, String entryId) {
    updatePage(
      ref,
      (page) => page.copyWith(
        entries: [...page.entries.map((e) => _removedReferencesFromEntry(ref, e, entryId))],
      ),
    );
  }

  /// When an entry is delete all references in other entries need to be removed.
  Entry _removedReferencesFromEntry(PassingRef ref, Entry entry, String targeId) {
    final referenceEntryPaths = ref.read(modifierPathsProvider(entry.type, "entry"));

    final referenceEntryIds = referenceEntryPaths.expand((path) => entry.getAll(path)).whereType<String>().toList();
    if (!referenceEntryIds.contains(targeId)) {
      return entry;
    }

    final newEntry = referenceEntryPaths.fold(
      entry,
      (previousEntry, path) => previousEntry.copyMapped(path, (value) => value == targeId ? null : value),
    );

    ref.read(communicatorProvider).updateEntireEntry(name, newEntry);

    return newEntry;
  }

  /// This should only be used to sync the entry from the server.
  void syncDeleteEntry(PassingRef ref, String entryId) {
    ref.read(bookProvider.notifier).insertPage(
          copyWith(
            entries: [...entries.where((e) => e.id != entryId)],
          ),
        );
  }
}

/// These are specialized shortcuts for common operations.
extension PageX on Page {
  Future<Entry> createEntryFromBlueprint(PassingRef ref, EntryBlueprint blueprint) async {
    final entry = Entry.fromBlueprint(id: getRandomString(), blueprint: blueprint);
    await createEntry(ref, entry);
    return entry;
  }

  Future<void> _wireEntryToOtherEntry(PassingRef ref, Entry originalEntry, Entry newEntry) async {
    final currentTriggers = originalEntry.get("triggers");
    if (currentTriggers == null || currentTriggers is! List) return;
    final newTriggers = currentTriggers + [newEntry.id];
    final modifiedOriginalEntry = originalEntry.copyWith("triggers", newTriggers);
    await updateEntireEntry(ref, modifiedOriginalEntry);
  }

  Future<void> extendsWithDuplicate(PassingRef ref, String entryId) async {
    final entry = ref.read(entryProvider(name, entryId));
    if (entry == null) return;
    final triggerPaths = ref.read(modifierPathsProvider(entry.type, "trigger"));
    if (!triggerPaths.contains("triggers.*")) {
      debugPrint("Cannot duplicate entry with no triggers.*");
      return;
    }

    final newEntry = triggerPaths
        .fold(
          entry.copyWith("id", getRandomString()),
          (previousEntry, path) => previousEntry.copyMapped(path, (_) => null), // Remove all triggers
        )
        .copyWith("name", entry.name.incrementedName);
    await createEntry(ref, newEntry);

    await _wireEntryToOtherEntry(ref, entry, newEntry);
  }

  void extendsWith(PassingRef ref, String entryId) {
    final entry = ref.read(entryProvider(name, entryId));
    if (entry == null) return;
    final triggerPaths = ref.read(modifierPathsProvider(entry.type, "trigger"));
    if (!triggerPaths.contains("triggers.*")) {
      debugPrint("Cannot extend entry with no triggers.*");
      return;
    }

    ref.read(searchProvider.notifier).asBuilder()
      ..tag("triggerable", canRemove: false)
      ..fetchNewEntry(
        onAdd: (blueprint) async {
          final newEntry = await createEntryFromBlueprint(ref, blueprint);
          await _wireEntryToOtherEntry(ref, entry, newEntry);
          await ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref, newEntry.id);
        },
      )
      ..fetchEntry(
        onSelect: (selectedEntry) async {
          await _wireEntryToOtherEntry(ref, entry, selectedEntry);
        },
      )
      ..open();
  }

  void deleteEntryWithConfirmation(BuildContext context, PassingRef ref, String entryId) {
    showConfirmationDialogue(
      context: context,
      title: "Delete Entry",
      content: "Are you sure you want to delete this entry?",
      confirmText: "Delete",
      onConfirm: () {
        final entry = ref.read(entryProvider(name, entryId));
        if (entry == null) return;
        deleteEntry(ref, entry);
      },
    );
  }
}
