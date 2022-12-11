// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'adapter.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_Adapter _$$_AdapterFromJson(Map<String, dynamic> json) => _$_Adapter(
      name: json['name'] as String,
      description: json['description'] as String,
      version: json['version'] as String,
      entries: (json['entries'] as List<dynamic>)
          .map((e) => EntryBlueprint.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$_AdapterToJson(_$_Adapter instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
      'version': instance.version,
      'entries': instance.entries,
    };

_$_EntryBlueprint _$$_EntryBlueprintFromJson(Map<String, dynamic> json) =>
    _$_EntryBlueprint(
      name: json['name'] as String,
      description: json['description'] as String,
      fields: ObjectField.fromJson(json['fields'] as Map<String, dynamic>),
      color: json['color'] == null
          ? Colors.grey
          : const ColorConverter().fromJson(json['color'] as String),
    );

Map<String, dynamic> _$$_EntryBlueprintToJson(_$_EntryBlueprint instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
      'fields': instance.fields,
      'color': const ColorConverter().toJson(instance.color),
    };

_$_FieldType _$$_FieldTypeFromJson(Map<String, dynamic> json) => _$_FieldType(
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$_FieldTypeToJson(_$_FieldType instance) =>
    <String, dynamic>{
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$PrimitiveField _$$PrimitiveFieldFromJson(Map<String, dynamic> json) =>
    _$PrimitiveField(
      type: $enumDecode(_$PrimitiveFieldTypeEnumMap, json['type']),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$PrimitiveFieldToJson(_$PrimitiveField instance) =>
    <String, dynamic>{
      'type': _$PrimitiveFieldTypeEnumMap[instance.type]!,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

const _$PrimitiveFieldTypeEnumMap = {
  PrimitiveFieldType.boolean: 'boolean',
  PrimitiveFieldType.double: 'double',
  PrimitiveFieldType.integer: 'integer',
  PrimitiveFieldType.string: 'string',
};

_$EnumField _$$EnumFieldFromJson(Map<String, dynamic> json) => _$EnumField(
      values:
          (json['values'] as List<dynamic>).map((e) => e as String).toList(),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$EnumFieldToJson(_$EnumField instance) =>
    <String, dynamic>{
      'values': instance.values,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$ListField _$$ListFieldFromJson(Map<String, dynamic> json) => _$ListField(
      type: FieldInfo.fromJson(json['type'] as Map<String, dynamic>),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$ListFieldToJson(_$ListField instance) =>
    <String, dynamic>{
      'type': instance.type,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$MapField _$$MapFieldFromJson(Map<String, dynamic> json) => _$MapField(
      key: FieldInfo.fromJson(json['key'] as Map<String, dynamic>),
      value: FieldInfo.fromJson(json['value'] as Map<String, dynamic>),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$MapFieldToJson(_$MapField instance) =>
    <String, dynamic>{
      'key': instance.key,
      'value': instance.value,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$ObjectField _$$ObjectFieldFromJson(Map<String, dynamic> json) =>
    _$ObjectField(
      fields: (json['fields'] as Map<String, dynamic>).map(
        (k, e) => MapEntry(k, FieldInfo.fromJson(e as Map<String, dynamic>)),
      ),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$ObjectFieldToJson(_$ObjectField instance) =>
    <String, dynamic>{
      'fields': instance.fields,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$_Modifier _$$_ModifierFromJson(Map<String, dynamic> json) => _$_Modifier(
      name: json['name'] as String,
      data: json['data'],
    );

Map<String, dynamic> _$$_ModifierToJson(_$_Modifier instance) =>
    <String, dynamic>{
      'name': instance.name,
      'data': instance.data,
    };

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// ignore_for_file: avoid_private_typedef_functions, non_constant_identifier_names, subtype_of_sealed_class, invalid_use_of_internal_member, unused_element, constant_identifier_names, unnecessary_raw_strings, library_private_types_in_public_api

/// Copied from Dart SDK
class _SystemHash {
  _SystemHash._();

  static int combine(int hash, int value) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + value);
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x0007ffff & hash) << 10));
    return hash ^ (hash >> 6);
  }

  static int finish(int hash) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x03ffffff & hash) << 3));
    // ignore: parameter_assignments
    hash = hash ^ (hash >> 11);
    return 0x1fffffff & (hash + ((0x00003fff & hash) << 15));
  }
}

String $adaptersHash() => r'b55e4dd5d1802c4e21359d057a765443bb896bd3';

/// A generated provider to fetch and cache a list of [Adapter]s.
///
/// Copied from [adapters].
final adaptersProvider = AutoDisposeProvider<List<Adapter>>(
  adapters,
  name: r'adaptersProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : $adaptersHash,
);
typedef AdaptersRef = AutoDisposeProviderRef<List<Adapter>>;
String $entryBlueprintsHash() => r'0e9019f7b17aa6dbeaed59a83c58fa1f1c0a52f6';

/// A generated provider to fetch and cache a list of all the [EntryBlueprint]s.
///
/// Copied from [entryBlueprints].
final entryBlueprintsProvider = AutoDisposeProvider<List<EntryBlueprint>>(
  entryBlueprints,
  name: r'entryBlueprintsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : $entryBlueprintsHash,
);
typedef EntryBlueprintsRef = AutoDisposeProviderRef<List<EntryBlueprint>>;
String $entryBlueprintHash() => r'd49a1e5e458e22493e802271db7ba55fffb887bc';

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
///
/// Copied from [entryBlueprint].
class EntryBlueprintProvider extends AutoDisposeProvider<EntryBlueprint?> {
  EntryBlueprintProvider(
    this.name,
  ) : super(
          (ref) => entryBlueprint(
            ref,
            name,
          ),
          from: entryBlueprintProvider,
          name: r'entryBlueprintProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : $entryBlueprintHash,
        );

  final String name;

  @override
  bool operator ==(Object other) {
    return other is EntryBlueprintProvider && other.name == name;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, name.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef EntryBlueprintRef = AutoDisposeProviderRef<EntryBlueprint?>;

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
///
/// Copied from [entryBlueprint].
final entryBlueprintProvider = EntryBlueprintFamily();

class EntryBlueprintFamily extends Family<EntryBlueprint?> {
  EntryBlueprintFamily();

  EntryBlueprintProvider call(
    String name,
  ) {
    return EntryBlueprintProvider(
      name,
    );
  }

  @override
  AutoDisposeProvider<EntryBlueprint?> getProviderOverride(
    covariant EntryBlueprintProvider provider,
  ) {
    return call(
      provider.name,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'entryBlueprintProvider';
}

String $fieldModifiersHash() => r'ad6700316538a1e9a2dfba24f4f124f68cf845c6';

/// See also [fieldModifiers].
class FieldModifiersProvider
    extends AutoDisposeProvider<Map<String, Modifier>> {
  FieldModifiersProvider(
    this.blueprint,
    this.name,
  ) : super(
          (ref) => fieldModifiers(
            ref,
            blueprint,
            name,
          ),
          from: fieldModifiersProvider,
          name: r'fieldModifiersProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : $fieldModifiersHash,
        );

  final String blueprint;
  final String name;

  @override
  bool operator ==(Object other) {
    return other is FieldModifiersProvider &&
        other.blueprint == blueprint &&
        other.name == name;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, blueprint.hashCode);
    hash = _SystemHash.combine(hash, name.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef FieldModifiersRef = AutoDisposeProviderRef<Map<String, Modifier>>;

/// See also [fieldModifiers].
final fieldModifiersProvider = FieldModifiersFamily();

class FieldModifiersFamily extends Family<Map<String, Modifier>> {
  FieldModifiersFamily();

  FieldModifiersProvider call(
    String blueprint,
    String name,
  ) {
    return FieldModifiersProvider(
      blueprint,
      name,
    );
  }

  @override
  AutoDisposeProvider<Map<String, Modifier>> getProviderOverride(
    covariant FieldModifiersProvider provider,
  ) {
    return call(
      provider.blueprint,
      provider.name,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'fieldModifiersProvider';
}

String $modifierPathsHash() => r'da7347b63deeccd651514c7b338113d7d56424a5';

/// See also [modifierPaths].
class ModifierPathsProvider extends AutoDisposeProvider<List<String>> {
  ModifierPathsProvider(
    this.blueprint,
    this.name,
  ) : super(
          (ref) => modifierPaths(
            ref,
            blueprint,
            name,
          ),
          from: modifierPathsProvider,
          name: r'modifierPathsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : $modifierPathsHash,
        );

  final String blueprint;
  final String name;

  @override
  bool operator ==(Object other) {
    return other is ModifierPathsProvider &&
        other.blueprint == blueprint &&
        other.name == name;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, blueprint.hashCode);
    hash = _SystemHash.combine(hash, name.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef ModifierPathsRef = AutoDisposeProviderRef<List<String>>;

/// See also [modifierPaths].
final modifierPathsProvider = ModifierPathsFamily();

class ModifierPathsFamily extends Family<List<String>> {
  ModifierPathsFamily();

  ModifierPathsProvider call(
    String blueprint,
    String name,
  ) {
    return ModifierPathsProvider(
      blueprint,
      name,
    );
  }

  @override
  AutoDisposeProvider<List<String>> getProviderOverride(
    covariant ModifierPathsProvider provider,
  ) {
    return call(
      provider.blueprint,
      provider.name,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'modifierPathsProvider';
}
