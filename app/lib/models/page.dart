import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/components/app/entry_search.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "page.freezed.dart";
part "page.g.dart";

@riverpod
List<Page> pages(PagesRef ref) {
  return ref.watch(bookProvider).pages;
}

@riverpod
Page? page(PageRef ref, String pageName) {
  return ref
      .watch(pagesProvider)
      .firstWhereOrNull((page) => page.pageName == pageName);
}

@riverpod
bool pageExists(PageExistsRef ref, String pageName) {
  return ref.watch(pageProvider(pageName)) != null;
}

@riverpod
PageType pageType(PageTypeRef ref, String pageName) {
  return ref.watch(pageProvider(pageName))?.type ?? PageType.sequence;
}

@riverpod
String pageChapter(PageChapterRef ref, String pageName) {
  return ref.watch(pageProvider(pageName))?.chapter ?? "";
}

@riverpod
int pagePriority(PagePriorityRef ref, String pageName) {
  return ref.watch(pageProvider(pageName))?.priority ?? 0;
}

@riverpod
String? entryPageId(EntryPageIdRef ref, String entryId) {
  return ref
      .watch(pagesProvider)
      .firstWhereOrNull(
        (page) => page.entries.any((entry) => entry.id == entryId),
      )
      ?.pageName;
}

@riverpod
Page? entryPage(EntryPageRef ref, String entryId) {
  return ref.watch(pagesProvider).firstWhereOrNull(
        (page) => page.entries.any((entry) => entry.id == entryId),
      );
}

@riverpod
Entry? entry(EntryRef ref, String pageId, String entryId) {
  return ref
      .watch(pageProvider(pageId))
      ?.entries
      .firstWhereOrNull((entry) => entry.id == entryId);
}

@riverpod
Entry? globalEntry(GlobalEntryRef ref, String entryId) {
  final pageId = ref.watch(entryPageIdProvider(entryId));
  if (pageId == null) {
    return null;
  }
  return ref.watch(entryProvider(pageId, entryId));
}

@riverpod
MapEntry<String, Entry>? globalEntryWithPage(
  GlobalEntryWithPageRef ref,
  String entryId,
) {
  final pageId = ref.watch(entryPageIdProvider(entryId));
  if (pageId == null) {
    return null;
  }
  final entry = ref.watch(entryProvider(pageId, entryId));
  if (entry == null) {
    return null;
  }
  return MapEntry(pageId, entry);
}

@riverpod
bool entryExists(EntryExistsRef ref, String entryId) {
  return ref.watch(entryPageIdProvider(entryId)) != null;
}

enum PageType {
  sequence("trigger", TWIcons.projectDiagram, Colors.blue),
  static("static", TWIcons.pin, Colors.deepPurple),
  cinematic("cinematic", TWIcons.film, Colors.orange),
  manifest("manifest", TWIcons.treeGraph, Colors.green),
  ;

  const PageType(this.tag, this.icon, this.color);

  final String tag;
  final String icon;
  final Color color;

  static PageType fromBlueprint(EntryBlueprint blueprint) {
    final pageType =
        values.firstWhereOrNull((type) => blueprint.tags.contains(type.tag));
    if (pageType == null) {
      throw Exception(
        "No page type found for blueprint ${blueprint.name}, make sure it has one of the following tags: ${values.map((type) => type.tag).join(", ")}",
      );
    }
    return pageType;
  }

  static PageType fromName(String name) {
    return values.firstWhere((type) => name.startsWith(type.tag));
  }
}

@freezed
class Page with _$Page {
  const factory Page({
    @JsonKey(name: "name") required String pageName,
    required PageType type,
    @Default([]) List<Entry> entries,
    @Default("") String chapter,
    @Default(0) int priority,
  }) = _Page;

  factory Page.fromJson(Map<String, dynamic> json) => _$PageFromJson(json);
}

extension PageExtension on Page {
  void updatePage(PassingRef ref, Page Function(Page) update) {
    // If multiple updates are done at the same time, `this` might be outdated. So we need to get the latest version.
    final currentPage = ref.read(pageProvider(pageName));
    if (currentPage == null) {
      return;
    }
    final newPage = update(currentPage);
    ref.read(bookProvider.notifier).insertPage(newPage);
  }

  Future<void> changeChapter(PassingRef ref, String newChapter) async {
    updatePage(
      ref,
      (page) => page.copyWith(chapter: newChapter),
    );
    await ref
        .read(communicatorProvider)
        .changePageValue(pageName, "chapter", newChapter);
  }

  Future<void> changePriority(PassingRef ref, int newPriority) async {
    updatePage(
      ref,
      (page) => page.copyWith(priority: newPriority),
    );
    await ref
        .read(communicatorProvider)
        .changePageValue(pageName, "priority", newPriority);
  }

  Future<void> createEntry(PassingRef ref, Entry entry) async {
    updatePage(
      ref,
      (page) => _insertEntry(page, entry),
    );
    await ref.read(communicatorProvider).createEntry(pageName, entry);
  }

  Future<void> updateEntireEntry(PassingRef ref, Entry entry) async {
    updatePage(
      ref,
      (page) => _insertEntry(page, entry),
    );
    await ref.read(communicatorProvider).updateEntireEntry(pageName, entry);
  }

  Future<void> updateEntryValue(
    PassingRef ref,
    Entry entry,
    String path,
    dynamic value,
  ) async {
    updatePage(
      ref,
      (page) => _insertEntry(page, entry.copyWith(path, value)),
    );
    await ref
        .read(communicatorProvider)
        .updateEntry(pageName, entry.id, path, value);
  }

  void reorderEntry(PassingRef ref, String entryId, int newIndex) {
    syncReorderEntry(ref, entryId, newIndex);
    ref.read(communicatorProvider).reorderEntry(pageName, entryId, newIndex);
  }

  void syncReorderEntry(PassingRef ref, String entryId, int newIndex) {
    updatePage(
      ref,
      (page) {
        final entries = [...page.entries];

        final oldIndex = entries.indexWhere((entry) => entry.id == entryId);
        if (oldIndex == -1) {
          return page;
        }

        final entry = entries.removeAt(oldIndex);

        if (newIndex > oldIndex) {
          entries.insert(newIndex - 1, entry);
        } else {
          entries.insert(newIndex, entry);
        }

        return page.copyWith(entries: entries);
      },
    );
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
    ref.read(communicatorProvider).deleteEntry(pageName, entry.id);
    updatePage(
      ref,
      (page) => page.copyWith(
        entries: [
          ...page.entries
              .where((e) => e.id != entry.id)
              .map((e) => _removedReferencesFromEntry(ref, e, entry.id)),
        ],
      ),
    );
    // Also delete all references to this entry from other pages.
    ref
        .read(bookProvider)
        .pages
        .where((page) => page.pageName != pageName)
        .forEach((page) {
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
        entries: [
          ...page.entries
              .map((e) => _removedReferencesFromEntry(ref, e, entryId)),
        ],
      ),
    );
  }

  /// When an entry is delete all references in other entries need to be removed.
  Entry _removedReferencesFromEntry(
    PassingRef ref,
    Entry entry,
    String targetId,
  ) {
    final referenceEntryPaths =
        ref.read(modifierPathsProvider(entry.type, "entry"));

    final referenceEntryIds = referenceEntryPaths
        .expand((path) => entry.getAll(path))
        .whereType<String>()
        .toList();

    if (!referenceEntryIds.contains(targetId)) {
      return entry;
    }

    final newEntry = referenceEntryPaths.fold(
      entry,
      (previousEntry, path) => previousEntry.copyMapped(
        path,
        (value) => value == targetId ? null : value,
      ),
    );

    ref.read(communicatorProvider).updateEntireEntry(pageName, newEntry);

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
  Future<Entry> createEntryFromBlueprint(
    PassingRef ref,
    EntryBlueprint blueprint,
  ) async {
    final entry =
        Entry.fromBlueprint(id: getRandomString(), blueprint: blueprint);
    await createEntry(ref, entry);
    return entry;
  }

  /// Will connects an entry to another entry on a given path.
  /// If it is already connected, it will remove the connection.
  Future<void> wireEntryToOtherEntry(
    PassingRef ref,
    Entry baseEntry,
    Entry targetEntry,
    String path,
  ) async {
    final parts = path.split(".");
    final lastPart = parts.last;
    if (int.tryParse(lastPart) == null) {
      // We are setting an exact value. Not a list. So we just overwrite it.
      await updateEntryValue(ref, baseEntry, path, targetEntry.id);
      return;
    }

    // If we have a list, we want to toggle the connection.
    final parentPath = parts.sublist(0, parts.length - 1).join(".");
    final currentTriggers = baseEntry.get(parentPath);
    if (currentTriggers == null || currentTriggers is! List) {
      debugPrint(
        "Invalid path for wiring entry ${baseEntry.id} to target entry ${targetEntry.id}. $path is not a list.",
      );
      return;
    }

    final currentTriggersIds = currentTriggers.cast<String>();
    final List<String> newTriggers;

    if (currentTriggers.contains(targetEntry.id)) {
      newTriggers =
          currentTriggersIds.where((id) => id != targetEntry.id).toList();
    } else {
      newTriggers = currentTriggersIds + [targetEntry.id];
    }

    await updateEntryValue(ref, baseEntry, parentPath, newTriggers);
  }

  Future<void> linkWithDuplicate(
    PassingRef ref,
    String entryId,
    String path,
  ) async {
    final entry = ref.read(entryProvider(pageName, entryId));
    if (entry == null) return;

    final modifiers = ref.read(fieldModifiersProvider(entry.type, "entry"));

    final wildPath = path.wild();
    final pathModifier = modifiers[wildPath];
    if (pathModifier == null) {
      debugPrint(
        "No modifier found for path $wildPath in entry ${entry.id}.",
      );
      return;
    }

    // Remove the paths with the same modifier.
    final resetPaths = modifiers.entries
        .where((e) => e.value.data == pathModifier.data)
        .map((e) => e.key)
        .toList();

    final newEntry = resetPaths
        .fold(
          entry.copyWith("id", getRandomString()),
          (previousEntry, path) => previousEntry.copyMapped(
            path,
            (_) => null,
          ), // Remove all triggers
        )
        .copyWith("name", entry.name.incrementedName);
    await createEntry(ref, newEntry);

    await wireEntryToOtherEntry(ref, entry, newEntry, path);
  }

  void linkWith(PassingRef ref, String entryId, String path) {
    final entry = ref.read(entryProvider(pageName, entryId));
    if (entry == null) return;
    final modifiers = ref.read(fieldModifiersProvider(entry.type, "entry"));

    final wildPath = path.wild();
    final pathModifier = modifiers[wildPath];
    if (pathModifier == null) {
      debugPrint(
        "No modifier found for path $wildPath in entry ${entry.id}.",
      );
      return;
    }

    final tag = pathModifier.data;

    // If the path has a only_tags modifier, we can only link to entries any of those tags and the tag of the entry.
    final onlyTagsModifier =
        ref.read(fieldModifiersProvider(entry.type, "only_tags"));

    final onlyTags = onlyTagsModifier[wildPath];
    final List<String> tags;
    if (onlyTags != null) {
      tags = onlyTags.data.split(",");
    } else {
      tags = [tag];
    }

    ref.read(searchProvider.notifier).asBuilder()
      ..anyTag(tags, canRemove: false)
      ..fetchNewEntry(
        onAdd: (blueprint) async {
          final newEntry = await createEntryFromBlueprint(ref, blueprint);
          await wireEntryToOtherEntry(ref, entry, newEntry, path);
          await ref
              .read(inspectingEntryIdProvider.notifier)
              .navigateAndSelectEntry(ref, newEntry.id);
          return null;
        },
      )
      ..fetchEntry(
        onSelect: (selectedEntry) async {
          await wireEntryToOtherEntry(ref, entry, selectedEntry, path);
          return null;
        },
      )
      ..open();
  }

  bool canHave(EntryBlueprint blueprint) => blueprint.tags.contains(type.tag);

  void deleteEntryWithConfirmation(
    BuildContext context,
    PassingRef ref,
    String entryId,
  ) {
    showConfirmationDialogue(
      context: context,
      title: "Delete Entry",
      content: "Are you sure you want to delete this entry?",
      confirmText: "Delete",
      onConfirm: () {
        final entry = ref.read(entryProvider(pageName, entryId));
        if (entry == null) return;
        deleteEntry(ref, entry);
      },
    );
  }
}
