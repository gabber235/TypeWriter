import 'package:collection/collection.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';
import 'package:typewriter/models/book.dart';

part 'adapter.freezed.dart';

part 'adapter.g.dart';

@riverpod
List<Adapter> adapters(AdaptersRef ref) {
  return ref.watch(bookProvider).adapters;
}

@riverpod
List<AdapterEntry> adapterEntries(AdapterEntriesRef ref) {
  return ref.watch(adaptersProvider).expand((e) => e.entries).toList();
}

@riverpod
AdapterEntry? adapterEntry(AdapterEntryRef ref, String name) {
  return ref
      .watch(adapterEntriesProvider)
      .firstWhereOrNull((e) => e.name == name);
}

@freezed
class Adapter with _$Adapter {
  const factory Adapter({
    required String name,
    required String description,
    required String version,
    required List<AdapterEntry> entries,
  }) = _Adapter;

  factory Adapter.fromJson(Map<String, dynamic> json) =>
      _$AdapterFromJson(json);
}

@freezed
class AdapterEntry with _$AdapterEntry {
  const factory AdapterEntry({
    required String name,
    required String description,
    required EntryType type,
  }) = _AdapterEntry;

  factory AdapterEntry.fromJson(Map<String, dynamic> json) =>
      _$AdapterEntryFromJson(json);
}

enum EntryType {
  @JsonValue("DIALOGUE")
  dialogue,
  @JsonValue("ACTION")
  action,
  @JsonValue("EVENT")
  event,
}

@Freezed(unionKey: 'kind')
class FieldType with _$FieldType {
  const factory FieldType() = _FieldType;

  const factory FieldType.primitive({
    required PrimitiveFieldType type,
  }) = PrimitiveField;

  @FreezedUnionValue("enum")
  const factory FieldType.enumField({
    required List<String> values,
  }) = EnumField;

  const factory FieldType.list({
    required FieldType type,
  }) = ListField;

  const factory FieldType.object({
    required Map<String, FieldType> fields,
  }) = ObjectField;

  factory FieldType.fromJson(Map<String, dynamic> json) =>
      _$FieldTypeFromJson(json);
}

enum PrimitiveFieldType {
  @JsonValue("BOOLEAN")
  boolean,
  @JsonValue("DOUBLE")
  double,
  @JsonValue("INTEGER")
  integer,
  @JsonValue("STRING")
  string,
}
