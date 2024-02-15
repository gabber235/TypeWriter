import "package:collection/collection.dart";
import "package:flutter/foundation.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/color_converter.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/inspector/editors/object.dart";
import "package:url_launcher/url_launcher_string.dart";

part "adapter.freezed.dart";
part "adapter.g.dart";

/// A generated provider to fetch and cache a list of [Adapter]s.
@riverpod
List<Adapter> adapters(AdaptersRef ref) => ref.watch(bookProvider).adapters;

/// A generated provider to fetch and cache a list of all the [EntryBlueprint]s.
@riverpod
List<EntryBlueprint> entryBlueprints(EntryBlueprintsRef ref) =>
    ref.watch(adaptersProvider).expand((e) => e.entries).toList();

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [blueprintName].
@riverpod
EntryBlueprint? entryBlueprint(EntryBlueprintRef ref, String blueprintName) =>
    ref
        .watch(entryBlueprintsProvider)
        .firstWhereOrNull((e) => e.name == blueprintName);

@riverpod
List<String> entryBlueprintTags(
  EntryBlueprintTagsRef ref,
  String blueprintName,
) =>
    ref.watch(entryBlueprintProvider(blueprintName))?.tags ?? [];

@riverpod
List<String> entryTags(
  EntryTagsRef ref,
  String entryId,
) {
  final blueprint = ref.watch(entryTypeProvider(entryId));
  if (blueprint == null) return [];
  return ref.watch(entryBlueprintTagsProvider(blueprint));
}

@riverpod
PageType entryBlueprintPageType(
  EntryBlueprintPageTypeRef ref,
  String blueprintName,
) {
  final blueprint = ref.watch(entryBlueprintProvider(blueprintName));
  if (blueprint == null) return PageType.static;
  return PageType.fromBlueprint(blueprint);
}

/// Gets all the modifiers with a given name.
@riverpod
Map<String, Modifier> fieldModifiers(
  FieldModifiersRef ref,
  String blueprintName,
  String modifierName,
) {
  return ref
          .watch(entryBlueprintProvider(blueprintName))
          ?.fieldsWithModifier(modifierName) ??
      {};
}

/// Gets all the paths from fields with a given modifier.
@riverpod
List<String> modifierPaths(
  ModifierPathsRef ref,
  String blueprintName,
  String modifierName, [
  String? data,
]) {
  return ref
      .watch(fieldModifiersProvider(blueprintName, modifierName))
      .entries
      .where((e) => data == null || e.value.data == data)
      .map((e) => e.key)
      .toList();
}

/// A data model that represents an adapter.
@freezed
class Adapter with _$Adapter {
  const factory Adapter({
    required String name,
    required String description,
    required String version,
    required List<EntryBlueprint> entries,
  }) = _Adapter;

  factory Adapter.fromJson(Map<String, dynamic> json) =>
      _$AdapterFromJson(json);
}

/// A data model that represents an entry blueprint.
@freezed
class EntryBlueprint with _$EntryBlueprint {
  const factory EntryBlueprint({
    required String name,
    required String description,
    required String adapter,
    required ObjectField fields,
    @Default(<String>[]) List<String> tags,
    @ColorConverter() @Default(Colors.grey) Color color,
    @Default(TWIcons.help) String icon,
  }) = _EntryBlueprint;

  factory EntryBlueprint.fromJson(Map<String, dynamic> json) =>
      _$EntryBlueprintFromJson(json);
}

/// A data model for the fields of an adapter entry.
@Freezed(unionKey: "kind")
class FieldInfo with _$FieldInfo {
  /// A default constructor that should never be used.
  const factory FieldInfo({
    @Default([]) List<Modifier> modifiers,
  }) = _FieldType;

  /// Primitive field type, such as a string or a number.
  const factory FieldInfo.primitive({
    required PrimitiveFieldType type,
    @Default([]) List<Modifier> modifiers,
  }) = PrimitiveField;

  /// Enum field type, such as a list of options.
  @FreezedUnionValue("enum")
  const factory FieldInfo.enumField({
    required List<String> values,
    @Default([]) List<Modifier> modifiers,
  }) = EnumField;

  /// List field type, such as a list of strings.
  const factory FieldInfo.list({
    required FieldInfo type,
    @Default([]) List<Modifier> modifiers,
  }) = ListField;

  /// Map field type, such as a map of strings to strings.
  /// Only strings and enums are supported as keys.
  const factory FieldInfo.map({
    required FieldInfo key,
    required FieldInfo value,
    @Default([]) List<Modifier> modifiers,
  }) = MapField;

  /// Object field type, such as a nested object.
  const factory FieldInfo.object({
    required Map<String, FieldInfo> fields,
    @Default([]) List<Modifier> modifiers,
  }) = ObjectField;

  /// Custom field type, where a custom editor is used.
  const factory FieldInfo.custom({
    required String editor,
    @JsonKey(name: "default") dynamic defaultValue,
    FieldInfo? fieldInfo,
    @Default([]) List<Modifier> modifiers,
  }) = CustomField;

  factory FieldInfo.fromJson(Map<String, dynamic> json) =>
      _$FieldInfoFromJson(json);
}

@freezed
class Modifier with _$Modifier {
  const factory Modifier({
    required String name,
    dynamic data,
  }) = _Modifier;

  factory Modifier.fromJson(Map<String, dynamic> json) =>
      _$ModifierFromJson(json);
}

const wikiBaseUrl = kDebugMode
    ? "http://localhost:3000/TypeWriter"
    : "https://gabber235.github.io/TypeWriter";

extension EntryBlueprintExt on EntryBlueprint {
  Map<String, Modifier> fieldsWithModifier(String name) =>
      _fieldsWithModifier(name, "", fields);

  /// Parse through the fields of this entry and return a list of all the fields that have the given modifier with [name].
  Map<String, Modifier> _fieldsWithModifier(
    String name,
    String path,
    FieldInfo info,
  ) {
    final fields = {
      if (info.hasModifier(name)) path: info.getModifier(name)!,
    };

    final separator = path.isEmpty ? "" : ".";
    if (info is ObjectField) {
      for (final field in info.fields.entries) {
        fields.addAll(
          _fieldsWithModifier(
            name,
            "$path$separator${field.key}",
            field.value,
          ),
        );
      }
    } else if (info is ListField) {
      fields.addAll(_fieldsWithModifier(name, "$path$separator*", info.type));
    } else if (info is MapField) {
      fields.addAll(_fieldsWithModifier(name, "$path$separator*", info.value));
    }

    return fields;
  }

  FieldInfo? getField(String path) {
    final parts = path.split(".");
    FieldInfo? info = fields;
    for (final part in parts) {
      if (info is ObjectField) {
        info = info.fields[part];
      } else if (info is ListField) {
        info = info.type;
      } else if (info is MapField) {
        info = info.value;
      }
    }

    return info;
  }

  static const _wikiCategories = [
    "action",
    "dialogue",
    "event",
    "fact",
    "speaker",
  ];
  String get wikiUrl {
    final category =
        tags.firstWhereOrNull((tag) => _wikiCategories.contains(tag));

    if (category == null) {
      return "$wikiBaseUrl/adapters/${adapter}Adapter";
    }

    return "$wikiBaseUrl/adapters/${adapter}Adapter/entries/$category/$name";
  }

  Future<void> openWiki() async {
    final url = wikiUrl;
    if (await canLaunchUrlString(url)) {
      await launchUrlString(url);
    }
  }
}

final _customEditorCustomLayout = ["optional", "item"];

/// Since freezed does not support methods on data models, we have to create a separate extension class.
extension FieldTypeExtension on FieldInfo {
  /// Get the default value for this field type.
  dynamic get defaultValue => when(
        (_) => null,
        primitive: (type, _) => _defaultPrimitiveValue(type),
        enumField: (values, _) => values.first,
        list: (type, _) => [],
        map: (key, value, _) => {},
        object: (fields, _) =>
            fields.map((key, value) => MapEntry(key, value.defaultValue)),
        custom: (_, defaultValue, __, ___) => defaultValue,
      );

  dynamic _defaultPrimitiveValue(PrimitiveFieldType type) {
    if (type == PrimitiveFieldType.string) {
      if (hasModifier("generated")) {
        return uuid.v4();
      }
    }

    return type.defaultValue;
  }

  /// If the [ObjectEditor] needs to show a default layout or if a field declares a custom layout.
  bool get hasCustomLayout {
    if (this is CustomField) {
      final editor = (this as CustomField).editor;
      if (_customEditorCustomLayout.contains(editor)) {
        return true;
      }
    }
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

  Modifier? getModifier(String name) {
    return modifiers.firstWhereOrNull((e) => e.name == name);
  }

  bool hasModifier(String name) {
    return getModifier(name) != null;
  }

  T? get<T>(String name) {
    final modifier = getModifier(name);
    return modifier?.data as T?;
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
