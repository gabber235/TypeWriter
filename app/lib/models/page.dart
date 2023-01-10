import "dart:convert";

import 'package:collection/collection.dart';
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import 'package:typewriter/models/communicator.dart';
import "package:typewriter/utils/extensions.dart";
import 'package:typewriter/utils/passing_reference.dart';

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
Entry? entry(EntryRef ref, String pageId, String entryId) {
  return ref.watch(pageProvider(pageId))?.entries.firstWhereOrNull((entry) => entry.id == entryId);
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
    final newPage = update(this);
    ref.read(bookProvider.notifier).insertPage(newPage);
  }

  void insertEntry(PassingRef ref, Entry entry) {
    updatePage(
      ref,
      (page) => _insertEntry(page, entry),
    );
  }

  /// This should only be used to sync the entry from the server.
  void syncInsertEntry(Ref<dynamic> ref, Entry entry) {
    final newPage = _insertEntry(this, entry);
    ref.read(bookProvider.notifier).insertPage(newPage);
  }

  Page _insertEntry(Page page, Entry entry) {
    // If the entry already exists, replace it at the same index.
    final index = page.entries.indexWhere((e) => e.id == entry.id);
    if (index != -1) {
      return page.copyWith(
        entries: [...page.entries]..[index] = entry,
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
        entries: [...page.entries.where((e) => e.id != entry.id)],
      ),
    );
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

class Entry {
  Entry(this.data);

  Entry.fromBlueprint({required String id, required EntryBlueprint blueprint})
      : data = {
          ...blueprint.fields.defaultValue,
          "id": id,
          "name": "new_${blueprint.name}",
          "type": blueprint.name,
        };

  factory Entry.fromJson(Map<String, dynamic> json) => Entry(json);
  final Map<String, dynamic> data;

  Map<String, dynamic> toJson() => data;

  /// Returns a inner field of this entry.
  /// These fields can be nested using dot notation, like "data.value", "data.1.value", etc.
  /// If the field is not found, a given default value is returned.
  dynamic get(String field, [dynamic defaultValue]) {
    final parts = field.split(".");
    dynamic current = data;
    for (final part in parts) {
      if (current is Map && current.containsKey(part)) {
        current = current[part];
        continue;
      }
      if (current is List && int.tryParse(part) != null && current.length > int.parse(part)) {
        current = current[int.parse(part)];
        continue;
      }
      return defaultValue;
    }
    return current;
  }

  /// Returns all values of a given path.
  /// This may look similar to [get], but it returns all values of a given path.
  /// Hence wildcards are supported, like "data.*.value", "data.*.1.value", etc.
  List<dynamic> getAll(String path) {
    final parts = path.split(".");
    final values = <dynamic>[];
    final current = <dynamic>[data];
    for (final part in parts) {
      if (part == "*") {
        final next = <dynamic>[];
        for (final item in current) {
          if (item is Map) {
            next.addAll(item.values);
          }
          if (item is List) {
            next.addAll(item);
          }
        }
        current
          ..clear()
          ..addAll(next);
        continue;
      }
      final next = <dynamic>[];
      for (final item in current) {
        if (item is Map && item.containsKey(part)) {
          next.add(item[part]);
        }
        if (item is List && int.tryParse(part) != null && item.length > int.parse(part)) {
          next.add(item[int.parse(part)]);
        }
      }
      current
        ..clear()
        ..addAll(next);
    }
    values.addAll(current);
    return values;
  }

  /// Returns a new copy of this entry with the given field updated.
  /// these fields can be nested using dot notation, like "data.value", "data.1.value", etc.
  Entry copyWith(String field, dynamic value) {
    final parts = field.split(".");
    final last = parts.removeLast();
    // Make a deep copy of the data. To avoid modifying the original data.
    final data = jsonDecode(jsonEncode(this.data));

    // Traverse the data to find the field to update.
    dynamic current = data;
    for (final part in parts) {
      // If the current fields is a map, we try to find the next field in it.
      if (current is Map && current.containsKey(part)) {
        current = current[part];
        continue;
      }

      // If the current field is a list, we try to find the next index in it.
      if (current is List && int.tryParse(part) != null && current.length > int.parse(part)) {
        current = current[int.parse(part)];
        continue;
      }

      // If the field could not be found, we don't update anything and return the original entry.
      return this;
    }

    // Update the field.
    if (current is Map) {
      current[last] = value;
    }
    if (current is List && int.tryParse(last) != null) {
      current[int.parse(last)] = value;
    }

    return Entry(data);
  }

  @override
  String toString() => "DynamicEntry{data: $data}";

  String get id => data["id"] as String;

  String get name => data["name"] as String;

  String get formattedName => name.formatted;

  String get type => data["type"] as String;

  @override
  bool operator ==(Object other) => identical(this, other) || other is Entry && data == other.data;

  @override
  int get hashCode => data.hashCode;
}
