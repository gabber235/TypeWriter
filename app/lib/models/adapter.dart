import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:theme_json_converter/theme_json_converter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/widgets/inspector/editors/object.dart";

part "adapter.freezed.dart";
part "adapter.g.dart";

/// A generated provider to fetch and cache a list of [Adapter]s.
@riverpod
List<Adapter> adapters(AdaptersRef ref) => ref.watch(bookProvider).adapters;

/// A generated provider to fetch and cache a list of all the [EntryBlueprint]s.
@riverpod
List<EntryBlueprint> entryBlueprints(EntryBlueprintsRef ref) =>
    ref.watch(adaptersProvider).expand((e) => e.entries).toList();

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
@riverpod
EntryBlueprint? entryBlueprint(EntryBlueprintRef ref, String name) =>
    ref.watch(entryBlueprintsProvider).firstWhereOrNull((e) => e.name == name);

/// A data model that represents an adapter.
@freezed
class Adapter with _$Adapter {
  const factory Adapter({
    required String name,
    required String description,
    required String version,
    required List<EntryBlueprint> entries,
  }) = _Adapter;

  factory Adapter.fromJson(Map<String, dynamic> json) => _$AdapterFromJson(json);
}

/// A data model that represents an entry blueprint.
@freezed
class EntryBlueprint with _$EntryBlueprint {
  const factory EntryBlueprint({
    required String name,
    required String description,
    required ObjectField fields,
    @ColorConverter() @Default(Colors.grey) Color color,
  }) = _EntryBlueprint;

  factory EntryBlueprint.fromJson(Map<String, dynamic> json) => _$EntryBlueprintFromJson(json);
}

/// A data model for the fields of an adapter entry.
@Freezed(unionKey: "kind")
class FieldType with _$FieldType {
  /// A default constructor that should never be used.
  const factory FieldType() = _FieldType;

  /// Primitive field type, such as a string or a number.
  const factory FieldType.primitive({
    required PrimitiveFieldType type,
  }) = PrimitiveField;

  /// Enum field type, such as a list of options.
  @FreezedUnionValue("enum")
  const factory FieldType.enumField({
    required List<String> values,
  }) = EnumField;

  /// List field type, such as a list of strings.
  const factory FieldType.list({
    required FieldType type,
  }) = ListField;

  /// Map field type, such as a map of strings to strings.
  /// Only strings and enums are supported as keys.
  const factory FieldType.map({
    required FieldType key,
    required FieldType value,
  }) = MapField;

  /// Object field type, such as a nested object.
  const factory FieldType.object({
    required Map<String, FieldType> fields,
  }) = ObjectField;

  factory FieldType.fromJson(Map<String, dynamic> json) => _$FieldTypeFromJson(json);
}

/// Since freezed does not support methods on data models, we have to create a separate extension class.
extension FieldTypeExtension on FieldType {
  /// Get the default value for this field type.
  dynamic get defaultValue => when(
        () => null,
        primitive: (type) => type.defaultValue,
        enumField: (values) => values.first,
        list: (type) => [],
        map: (key, value) => {},
        object: (fields) => fields.map((key, value) => MapEntry(key, value.defaultValue)),
      );

  /// If the [ObjectEditor] needs to show a default layout or if a field declares a custom layout.
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
    if (this is PrimitiveField && (this as PrimitiveField).type == PrimitiveFieldType.boolean) {
      return true;
    }
    return false;
  }
}

/// A data model that represents a primitive field type.
enum PrimitiveFieldType {
  boolean(false),
  double(0.0),
  integer(0),
  string(""),
  ;

  /// A constructor that is used to create an instance of the [PrimitiveFieldType] class.
  const PrimitiveFieldType(this.defaultValue);

  /// The default value for this field type.
  final dynamic defaultValue;
}
