import 'dart:convert';

import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/models/book.dart';
import 'package:typewriter/utils/extensions.dart';

part 'page.freezed.dart';

part 'page.g.dart';

final pagesProvider = Provider<List<Page>>((ref) {
  return ref.watch(bookProvider).pages;
}, dependencies: [bookProvider]);

@freezed
class Page with _$Page {
  const factory Page({
    required String name,
    @Default([]) List<Entry> entries,
  }) = _Page;

  factory Page.fromJson(Map<String, dynamic> json) => _$PageFromJson(json);
}

extension PageExtension on Page {
  List<Entry> get graphableEntries => [
        ...entries,
      ];

  void updatePage(WidgetRef ref, Page Function(Page) update) {
    final newPage = update(this);
    ref.read(bookProvider.notifier).insertPage(newPage);
  }

  void insertEntry(WidgetRef ref, Entry entry) {
    updatePage(
        ref,
        (page) => page.copyWith(
            entries: [...page.entries.where((e) => e.id != entry.id), entry]));
  }

  void deleteEntry(WidgetRef ref, Entry entry) {
    updatePage(
        ref,
        (page) => page.copyWith(
            entries: [...page.entries.where((e) => e.id != entry.id)]));
  }
}

class Entry {
  final Map<String, dynamic> data;

  Entry(this.data);

  Entry.fromAdapter({required String id, required AdapterEntry entry})
      : data = {
          ...entry.fields.defaultValue,
          "id": id,
          "name": "new_${entry.name}",
          "type": entry.name,
        };

  factory Entry.fromJson(Map<String, dynamic> json) {
    return Entry(json);
  }

  Map<String, dynamic> toJson() {
    return data;
  }

  /// Returns a inner field of this entry.
  /// These fields can be nested using dot notation, like "data.value", "data.1.value", etc.
  /// If the field is not found, null is returned.
  dynamic get(String field, [dynamic defaultValue]) {
    final parts = field.split(".");
    dynamic current = data;
    for (final part in parts) {
      if (current is Map && current.containsKey(part)) {
        current = current[part];
        continue;
      }
      if (current is List &&
          int.tryParse(part) != null &&
          current.length > int.parse(part)) {
        current = current[int.parse(part)];
        continue;
      }
      return defaultValue;
    }
    return current;
  }

  /// Returns a new copy of this entry with the given field updated.
  /// these fields can be nested using dot notation, like "data.value", "data.1.value", etc.
  Entry copyWith(String field, dynamic value) {
    final parts = field.split(".");
    final last = parts.removeLast();
    final data = jsonDecode(jsonEncode(this.data));
    dynamic current = data;
    for (final part in parts) {
      if (current is Map && current.containsKey(part)) {
        current = current[part];
        continue;
      }
      if (current is List &&
          int.tryParse(part) != null &&
          current.length > int.parse(part)) {
        current = current[int.parse(part)];
        continue;
      }

      return this;
    }

    if (current is Map) {
      current[last] = value;
    }
    if (current is List && int.tryParse(last) != null) {
      current[int.parse(last)] = value;
    }

    return Entry(data);
  }

  @override
  String toString() {
    return 'DynamicEntry{data: $data}';
  }

  String get id => data['id'] as String;

  String get name => data['name'] as String;

  String get formattedName => name.formatted;

  String get type => data['type'] as String;
}
