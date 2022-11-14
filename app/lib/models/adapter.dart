import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';
import 'package:theme_json_converter/theme_json_converter.dart';
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
    required ObjectField fields,
    @ColorConverter() @Default(Colors.grey) Color color,
  }) = _AdapterEntry;

  factory AdapterEntry.fromJson(Map<String, dynamic> json) =>
      _$AdapterEntryFromJson(json);
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

  const factory FieldType.map({
    required FieldType key,
    required FieldType value,
  }) = MapField;

  const factory FieldType.object({
    required Map<String, FieldType> fields,
  }) = ObjectField;

  factory FieldType.fromJson(Map<String, dynamic> json) =>
      _$FieldTypeFromJson(json);
}

extension FieldTypeExtension on FieldType {
  dynamic get defaultValue {
    return when(
      () => null,
      primitive: (type) => type.defaultValue,
      enumField: (values) => values.first,
      list: (type) => [],
      map: (key, value) => {},
      object: (fields) =>
          fields.map((key, value) => MapEntry(key, value.defaultValue)),
    );
  }

  bool get hasCustomLayout {
    if (this is ObjectField) {
      return true;
    }
    if (this is ListField) {
      return true;
    }
    if (this is MapField) {
      return true;
    }
    if (this is PrimitiveField &&
        (this as PrimitiveField).type == PrimitiveFieldType.boolean) {
      return true;
    }
    return false;
  }
}

enum PrimitiveFieldType {
  boolean(false),
  double(0.0),
  integer(0),
  string(""),
  ;

  final dynamic defaultValue;

  const PrimitiveFieldType(this.defaultValue);
}
