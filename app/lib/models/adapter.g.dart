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
      adapter: json['adapter'] as String,
      fields: ObjectField.fromJson(json['fields'] as Map<String, dynamic>),
      tags:
          (json['tags'] as List<dynamic>?)?.map((e) => e as String).toList() ??
              const <String>[],
      color: json['color'] == null
          ? Colors.grey
          : const ColorConverter().fromJson(json['color'] as String),
      icon: json['icon'] == null
          ? Icons.help
          : const IconConverter().fromJson(json['icon'] as String),
    );

Map<String, dynamic> _$$_EntryBlueprintToJson(_$_EntryBlueprint instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
      'adapter': instance.adapter,
      'fields': instance.fields,
      'tags': instance.tags,
      'color': const ColorConverter().toJson(instance.color),
      'icon': const IconConverter().toJson(instance.icon),
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

_$CustomField _$$CustomFieldFromJson(Map<String, dynamic> json) =>
    _$CustomField(
      editor: json['editor'] as String,
      defaultValue: json['default'],
      fieldInfo: json['fieldInfo'] == null
          ? null
          : FieldInfo.fromJson(json['fieldInfo'] as Map<String, dynamic>),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$CustomFieldToJson(_$CustomField instance) =>
    <String, dynamic>{
      'editor': instance.editor,
      'default': instance.defaultValue,
      'fieldInfo': instance.fieldInfo,
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

String _$adaptersHash() => r'b55e4dd5d1802c4e21359d057a765443bb896bd3';

/// A generated provider to fetch and cache a list of [Adapter]s.
///
/// Copied from [adapters].
@ProviderFor(adapters)
final adaptersProvider = AutoDisposeProvider<List<Adapter>>.internal(
  adapters,
  name: r'adaptersProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$adaptersHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef AdaptersRef = AutoDisposeProviderRef<List<Adapter>>;
String _$entryBlueprintsHash() => r'0e9019f7b17aa6dbeaed59a83c58fa1f1c0a52f6';

/// A generated provider to fetch and cache a list of all the [EntryBlueprint]s.
///
/// Copied from [entryBlueprints].
@ProviderFor(entryBlueprints)
final entryBlueprintsProvider =
    AutoDisposeProvider<List<EntryBlueprint>>.internal(
  entryBlueprints,
  name: r'entryBlueprintsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$entryBlueprintsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef EntryBlueprintsRef = AutoDisposeProviderRef<List<EntryBlueprint>>;
String _$entryBlueprintHash() => r'd49a1e5e458e22493e802271db7ba55fffb887bc';

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

typedef EntryBlueprintRef = AutoDisposeProviderRef<EntryBlueprint?>;

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
///
/// Copied from [entryBlueprint].
@ProviderFor(entryBlueprint)
const entryBlueprintProvider = EntryBlueprintFamily();

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
///
/// Copied from [entryBlueprint].
class EntryBlueprintFamily extends Family<EntryBlueprint?> {
  /// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
  ///
  /// Copied from [entryBlueprint].
  const EntryBlueprintFamily();

  /// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
  ///
  /// Copied from [entryBlueprint].
  EntryBlueprintProvider call(
    String name,
  ) {
    return EntryBlueprintProvider(
      name,
    );
  }

  @override
  EntryBlueprintProvider getProviderOverride(
    covariant EntryBlueprintProvider provider,
  ) {
    return call(
      provider.name,
    );
  }

  static const Iterable<ProviderOrFamily>? _dependencies = null;

  @override
  Iterable<ProviderOrFamily>? get dependencies => _dependencies;

  static const Iterable<ProviderOrFamily>? _allTransitiveDependencies = null;

  @override
  Iterable<ProviderOrFamily>? get allTransitiveDependencies =>
      _allTransitiveDependencies;

  @override
  String? get name => r'entryBlueprintProvider';
}

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
///
/// Copied from [entryBlueprint].
class EntryBlueprintProvider extends AutoDisposeProvider<EntryBlueprint?> {
  /// A generated provider to fetch and cache a specific [EntryBlueprint] by its [name].
  ///
  /// Copied from [entryBlueprint].
  EntryBlueprintProvider(
    this.name,
  ) : super.internal(
          (ref) => entryBlueprint(
            ref,
            name,
          ),
          from: entryBlueprintProvider,
          name: r'entryBlueprintProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryBlueprintHash,
          dependencies: EntryBlueprintFamily._dependencies,
          allTransitiveDependencies:
              EntryBlueprintFamily._allTransitiveDependencies,
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

String _$entryTagsHash() => r'41b7964e296b646f18ac537db2579bf0dce7ab2a';
typedef EntryTagsRef = AutoDisposeProviderRef<List<String>>;

/// See also [entryTags].
@ProviderFor(entryTags)
const entryTagsProvider = EntryTagsFamily();

/// See also [entryTags].
class EntryTagsFamily extends Family<List<String>> {
  /// See also [entryTags].
  const EntryTagsFamily();

  /// See also [entryTags].
  EntryTagsProvider call(
    String name,
  ) {
    return EntryTagsProvider(
      name,
    );
  }

  @override
  EntryTagsProvider getProviderOverride(
    covariant EntryTagsProvider provider,
  ) {
    return call(
      provider.name,
    );
  }

  static const Iterable<ProviderOrFamily>? _dependencies = null;

  @override
  Iterable<ProviderOrFamily>? get dependencies => _dependencies;

  static const Iterable<ProviderOrFamily>? _allTransitiveDependencies = null;

  @override
  Iterable<ProviderOrFamily>? get allTransitiveDependencies =>
      _allTransitiveDependencies;

  @override
  String? get name => r'entryTagsProvider';
}

/// See also [entryTags].
class EntryTagsProvider extends AutoDisposeProvider<List<String>> {
  /// See also [entryTags].
  EntryTagsProvider(
    this.name,
  ) : super.internal(
          (ref) => entryTags(
            ref,
            name,
          ),
          from: entryTagsProvider,
          name: r'entryTagsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryTagsHash,
          dependencies: EntryTagsFamily._dependencies,
          allTransitiveDependencies: EntryTagsFamily._allTransitiveDependencies,
        );

  final String name;

  @override
  bool operator ==(Object other) {
    return other is EntryTagsProvider && other.name == name;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, name.hashCode);

    return _SystemHash.finish(hash);
  }
}

String _$fieldModifiersHash() => r'ad6700316538a1e9a2dfba24f4f124f68cf845c6';
typedef FieldModifiersRef = AutoDisposeProviderRef<Map<String, Modifier>>;

/// Gets all the modifiers with a given name.
///
/// Copied from [fieldModifiers].
@ProviderFor(fieldModifiers)
const fieldModifiersProvider = FieldModifiersFamily();

/// Gets all the modifiers with a given name.
///
/// Copied from [fieldModifiers].
class FieldModifiersFamily extends Family<Map<String, Modifier>> {
  /// Gets all the modifiers with a given name.
  ///
  /// Copied from [fieldModifiers].
  const FieldModifiersFamily();

  /// Gets all the modifiers with a given name.
  ///
  /// Copied from [fieldModifiers].
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
  FieldModifiersProvider getProviderOverride(
    covariant FieldModifiersProvider provider,
  ) {
    return call(
      provider.blueprint,
      provider.name,
    );
  }

  static const Iterable<ProviderOrFamily>? _dependencies = null;

  @override
  Iterable<ProviderOrFamily>? get dependencies => _dependencies;

  static const Iterable<ProviderOrFamily>? _allTransitiveDependencies = null;

  @override
  Iterable<ProviderOrFamily>? get allTransitiveDependencies =>
      _allTransitiveDependencies;

  @override
  String? get name => r'fieldModifiersProvider';
}

/// Gets all the modifiers with a given name.
///
/// Copied from [fieldModifiers].
class FieldModifiersProvider
    extends AutoDisposeProvider<Map<String, Modifier>> {
  /// Gets all the modifiers with a given name.
  ///
  /// Copied from [fieldModifiers].
  FieldModifiersProvider(
    this.blueprint,
    this.name,
  ) : super.internal(
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
                  : _$fieldModifiersHash,
          dependencies: FieldModifiersFamily._dependencies,
          allTransitiveDependencies:
              FieldModifiersFamily._allTransitiveDependencies,
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

String _$modifierPathsHash() => r'da7347b63deeccd651514c7b338113d7d56424a5';
typedef ModifierPathsRef = AutoDisposeProviderRef<List<String>>;

/// Gets all the paths from fields with a given modifier.
///
/// Copied from [modifierPaths].
@ProviderFor(modifierPaths)
const modifierPathsProvider = ModifierPathsFamily();

/// Gets all the paths from fields with a given modifier.
///
/// Copied from [modifierPaths].
class ModifierPathsFamily extends Family<List<String>> {
  /// Gets all the paths from fields with a given modifier.
  ///
  /// Copied from [modifierPaths].
  const ModifierPathsFamily();

  /// Gets all the paths from fields with a given modifier.
  ///
  /// Copied from [modifierPaths].
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
  ModifierPathsProvider getProviderOverride(
    covariant ModifierPathsProvider provider,
  ) {
    return call(
      provider.blueprint,
      provider.name,
    );
  }

  static const Iterable<ProviderOrFamily>? _dependencies = null;

  @override
  Iterable<ProviderOrFamily>? get dependencies => _dependencies;

  static const Iterable<ProviderOrFamily>? _allTransitiveDependencies = null;

  @override
  Iterable<ProviderOrFamily>? get allTransitiveDependencies =>
      _allTransitiveDependencies;

  @override
  String? get name => r'modifierPathsProvider';
}

/// Gets all the paths from fields with a given modifier.
///
/// Copied from [modifierPaths].
class ModifierPathsProvider extends AutoDisposeProvider<List<String>> {
  /// Gets all the paths from fields with a given modifier.
  ///
  /// Copied from [modifierPaths].
  ModifierPathsProvider(
    this.blueprint,
    this.name,
  ) : super.internal(
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
                  : _$modifierPathsHash,
          dependencies: ModifierPathsFamily._dependencies,
          allTransitiveDependencies:
              ModifierPathsFamily._allTransitiveDependencies,
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
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member
