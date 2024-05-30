// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'adapter.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$AdapterImpl _$$AdapterImplFromJson(Map<String, dynamic> json) =>
    _$AdapterImpl(
      name: json['name'] as String,
      description: json['description'] as String,
      version: json['version'] as String,
      entries: (json['entries'] as List<dynamic>)
          .map((e) => EntryBlueprint.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$AdapterImplToJson(_$AdapterImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
      'version': instance.version,
      'entries': instance.entries,
    };

_$EntryBlueprintImpl _$$EntryBlueprintImplFromJson(Map<String, dynamic> json) =>
    _$EntryBlueprintImpl(
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
      icon: json['icon'] as String? ?? TWIcons.help,
    );

Map<String, dynamic> _$$EntryBlueprintImplToJson(
        _$EntryBlueprintImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
      'adapter': instance.adapter,
      'fields': instance.fields,
      'tags': instance.tags,
      'color': const ColorConverter().toJson(instance.color),
      'icon': instance.icon,
    };

_$FieldTypeImpl _$$FieldTypeImplFromJson(Map<String, dynamic> json) =>
    _$FieldTypeImpl(
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$FieldTypeImplToJson(_$FieldTypeImpl instance) =>
    <String, dynamic>{
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$PrimitiveFieldImpl _$$PrimitiveFieldImplFromJson(Map<String, dynamic> json) =>
    _$PrimitiveFieldImpl(
      type: $enumDecode(_$PrimitiveFieldTypeEnumMap, json['type']),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$PrimitiveFieldImplToJson(
        _$PrimitiveFieldImpl instance) =>
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

_$EnumFieldImpl _$$EnumFieldImplFromJson(Map<String, dynamic> json) =>
    _$EnumFieldImpl(
      values:
          (json['values'] as List<dynamic>).map((e) => e as String).toList(),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$EnumFieldImplToJson(_$EnumFieldImpl instance) =>
    <String, dynamic>{
      'values': instance.values,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$ListFieldImpl _$$ListFieldImplFromJson(Map<String, dynamic> json) =>
    _$ListFieldImpl(
      type: FieldInfo.fromJson(json['type'] as Map<String, dynamic>),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$ListFieldImplToJson(_$ListFieldImpl instance) =>
    <String, dynamic>{
      'type': instance.type,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$MapFieldImpl _$$MapFieldImplFromJson(Map<String, dynamic> json) =>
    _$MapFieldImpl(
      key: FieldInfo.fromJson(json['key'] as Map<String, dynamic>),
      value: FieldInfo.fromJson(json['value'] as Map<String, dynamic>),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$MapFieldImplToJson(_$MapFieldImpl instance) =>
    <String, dynamic>{
      'key': instance.key,
      'value': instance.value,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$ObjectFieldImpl _$$ObjectFieldImplFromJson(Map<String, dynamic> json) =>
    _$ObjectFieldImpl(
      fields: (json['fields'] as Map<String, dynamic>).map(
        (k, e) => MapEntry(k, FieldInfo.fromJson(e as Map<String, dynamic>)),
      ),
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Modifier.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$ObjectFieldImplToJson(_$ObjectFieldImpl instance) =>
    <String, dynamic>{
      'fields': instance.fields,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$CustomFieldImpl _$$CustomFieldImplFromJson(Map<String, dynamic> json) =>
    _$CustomFieldImpl(
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

Map<String, dynamic> _$$CustomFieldImplToJson(_$CustomFieldImpl instance) =>
    <String, dynamic>{
      'editor': instance.editor,
      'default': instance.defaultValue,
      'fieldInfo': instance.fieldInfo,
      'modifiers': instance.modifiers,
      'kind': instance.$type,
    };

_$ModifierImpl _$$ModifierImplFromJson(Map<String, dynamic> json) =>
    _$ModifierImpl(
      name: json['name'] as String,
      data: json['data'],
    );

Map<String, dynamic> _$$ModifierImplToJson(_$ModifierImpl instance) =>
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
String _$entryBlueprintHash() => r'331a2ea9f825002876e28ac1a1805ed8e63e0934';

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

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [blueprintName].
///
/// Copied from [entryBlueprint].
@ProviderFor(entryBlueprint)
const entryBlueprintProvider = EntryBlueprintFamily();

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [blueprintName].
///
/// Copied from [entryBlueprint].
class EntryBlueprintFamily extends Family<EntryBlueprint?> {
  /// A generated provider to fetch and cache a specific [EntryBlueprint] by its [blueprintName].
  ///
  /// Copied from [entryBlueprint].
  const EntryBlueprintFamily();

  /// A generated provider to fetch and cache a specific [EntryBlueprint] by its [blueprintName].
  ///
  /// Copied from [entryBlueprint].
  EntryBlueprintProvider call(
    String blueprintName,
  ) {
    return EntryBlueprintProvider(
      blueprintName,
    );
  }

  @override
  EntryBlueprintProvider getProviderOverride(
    covariant EntryBlueprintProvider provider,
  ) {
    return call(
      provider.blueprintName,
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

/// A generated provider to fetch and cache a specific [EntryBlueprint] by its [blueprintName].
///
/// Copied from [entryBlueprint].
class EntryBlueprintProvider extends AutoDisposeProvider<EntryBlueprint?> {
  /// A generated provider to fetch and cache a specific [EntryBlueprint] by its [blueprintName].
  ///
  /// Copied from [entryBlueprint].
  EntryBlueprintProvider(
    String blueprintName,
  ) : this._internal(
          (ref) => entryBlueprint(
            ref as EntryBlueprintRef,
            blueprintName,
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
          blueprintName: blueprintName,
        );

  EntryBlueprintProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.blueprintName,
  }) : super.internal();

  final String blueprintName;

  @override
  Override overrideWith(
    EntryBlueprint? Function(EntryBlueprintRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryBlueprintProvider._internal(
        (ref) => create(ref as EntryBlueprintRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        blueprintName: blueprintName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<EntryBlueprint?> createElement() {
    return _EntryBlueprintProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryBlueprintProvider &&
        other.blueprintName == blueprintName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, blueprintName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryBlueprintRef on AutoDisposeProviderRef<EntryBlueprint?> {
  /// The parameter `blueprintName` of this provider.
  String get blueprintName;
}

class _EntryBlueprintProviderElement
    extends AutoDisposeProviderElement<EntryBlueprint?> with EntryBlueprintRef {
  _EntryBlueprintProviderElement(super.provider);

  @override
  String get blueprintName => (origin as EntryBlueprintProvider).blueprintName;
}

String _$entryBlueprintTagsHash() =>
    r'999df9eb33de7198efa9eb463cca6dd400d6456a';

/// See also [entryBlueprintTags].
@ProviderFor(entryBlueprintTags)
const entryBlueprintTagsProvider = EntryBlueprintTagsFamily();

/// See also [entryBlueprintTags].
class EntryBlueprintTagsFamily extends Family<List<String>> {
  /// See also [entryBlueprintTags].
  const EntryBlueprintTagsFamily();

  /// See also [entryBlueprintTags].
  EntryBlueprintTagsProvider call(
    String blueprintName,
  ) {
    return EntryBlueprintTagsProvider(
      blueprintName,
    );
  }

  @override
  EntryBlueprintTagsProvider getProviderOverride(
    covariant EntryBlueprintTagsProvider provider,
  ) {
    return call(
      provider.blueprintName,
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
  String? get name => r'entryBlueprintTagsProvider';
}

/// See also [entryBlueprintTags].
class EntryBlueprintTagsProvider extends AutoDisposeProvider<List<String>> {
  /// See also [entryBlueprintTags].
  EntryBlueprintTagsProvider(
    String blueprintName,
  ) : this._internal(
          (ref) => entryBlueprintTags(
            ref as EntryBlueprintTagsRef,
            blueprintName,
          ),
          from: entryBlueprintTagsProvider,
          name: r'entryBlueprintTagsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryBlueprintTagsHash,
          dependencies: EntryBlueprintTagsFamily._dependencies,
          allTransitiveDependencies:
              EntryBlueprintTagsFamily._allTransitiveDependencies,
          blueprintName: blueprintName,
        );

  EntryBlueprintTagsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.blueprintName,
  }) : super.internal();

  final String blueprintName;

  @override
  Override overrideWith(
    List<String> Function(EntryBlueprintTagsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryBlueprintTagsProvider._internal(
        (ref) => create(ref as EntryBlueprintTagsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        blueprintName: blueprintName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<String>> createElement() {
    return _EntryBlueprintTagsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryBlueprintTagsProvider &&
        other.blueprintName == blueprintName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, blueprintName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryBlueprintTagsRef on AutoDisposeProviderRef<List<String>> {
  /// The parameter `blueprintName` of this provider.
  String get blueprintName;
}

class _EntryBlueprintTagsProviderElement
    extends AutoDisposeProviderElement<List<String>>
    with EntryBlueprintTagsRef {
  _EntryBlueprintTagsProviderElement(super.provider);

  @override
  String get blueprintName =>
      (origin as EntryBlueprintTagsProvider).blueprintName;
}

String _$entryTagsHash() => r'b71bc52479db49fa3f55d80106a998243f377f3d';

/// See also [entryTags].
@ProviderFor(entryTags)
const entryTagsProvider = EntryTagsFamily();

/// See also [entryTags].
class EntryTagsFamily extends Family<List<String>> {
  /// See also [entryTags].
  const EntryTagsFamily();

  /// See also [entryTags].
  EntryTagsProvider call(
    String entryId,
  ) {
    return EntryTagsProvider(
      entryId,
    );
  }

  @override
  EntryTagsProvider getProviderOverride(
    covariant EntryTagsProvider provider,
  ) {
    return call(
      provider.entryId,
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
    String entryId,
  ) : this._internal(
          (ref) => entryTags(
            ref as EntryTagsRef,
            entryId,
          ),
          from: entryTagsProvider,
          name: r'entryTagsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryTagsHash,
          dependencies: EntryTagsFamily._dependencies,
          allTransitiveDependencies: EntryTagsFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryTagsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    List<String> Function(EntryTagsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryTagsProvider._internal(
        (ref) => create(ref as EntryTagsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<String>> createElement() {
    return _EntryTagsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryTagsProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryTagsRef on AutoDisposeProviderRef<List<String>> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryTagsProviderElement extends AutoDisposeProviderElement<List<String>>
    with EntryTagsRef {
  _EntryTagsProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryTagsProvider).entryId;
}

String _$entryBlueprintPageTypeHash() =>
    r'ab9695f4ad5cf34bf53658d8f765b51d8f463868';

/// See also [entryBlueprintPageType].
@ProviderFor(entryBlueprintPageType)
const entryBlueprintPageTypeProvider = EntryBlueprintPageTypeFamily();

/// See also [entryBlueprintPageType].
class EntryBlueprintPageTypeFamily extends Family<PageType> {
  /// See also [entryBlueprintPageType].
  const EntryBlueprintPageTypeFamily();

  /// See also [entryBlueprintPageType].
  EntryBlueprintPageTypeProvider call(
    String blueprintName,
  ) {
    return EntryBlueprintPageTypeProvider(
      blueprintName,
    );
  }

  @override
  EntryBlueprintPageTypeProvider getProviderOverride(
    covariant EntryBlueprintPageTypeProvider provider,
  ) {
    return call(
      provider.blueprintName,
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
  String? get name => r'entryBlueprintPageTypeProvider';
}

/// See also [entryBlueprintPageType].
class EntryBlueprintPageTypeProvider extends AutoDisposeProvider<PageType> {
  /// See also [entryBlueprintPageType].
  EntryBlueprintPageTypeProvider(
    String blueprintName,
  ) : this._internal(
          (ref) => entryBlueprintPageType(
            ref as EntryBlueprintPageTypeRef,
            blueprintName,
          ),
          from: entryBlueprintPageTypeProvider,
          name: r'entryBlueprintPageTypeProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryBlueprintPageTypeHash,
          dependencies: EntryBlueprintPageTypeFamily._dependencies,
          allTransitiveDependencies:
              EntryBlueprintPageTypeFamily._allTransitiveDependencies,
          blueprintName: blueprintName,
        );

  EntryBlueprintPageTypeProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.blueprintName,
  }) : super.internal();

  final String blueprintName;

  @override
  Override overrideWith(
    PageType Function(EntryBlueprintPageTypeRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryBlueprintPageTypeProvider._internal(
        (ref) => create(ref as EntryBlueprintPageTypeRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        blueprintName: blueprintName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<PageType> createElement() {
    return _EntryBlueprintPageTypeProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryBlueprintPageTypeProvider &&
        other.blueprintName == blueprintName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, blueprintName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryBlueprintPageTypeRef on AutoDisposeProviderRef<PageType> {
  /// The parameter `blueprintName` of this provider.
  String get blueprintName;
}

class _EntryBlueprintPageTypeProviderElement
    extends AutoDisposeProviderElement<PageType>
    with EntryBlueprintPageTypeRef {
  _EntryBlueprintPageTypeProviderElement(super.provider);

  @override
  String get blueprintName =>
      (origin as EntryBlueprintPageTypeProvider).blueprintName;
}

String _$fieldModifiersHash() => r'2e926b371b7db972ec7c3ed7db468baaa6839aba';

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
    String blueprintName,
    String modifierName,
  ) {
    return FieldModifiersProvider(
      blueprintName,
      modifierName,
    );
  }

  @override
  FieldModifiersProvider getProviderOverride(
    covariant FieldModifiersProvider provider,
  ) {
    return call(
      provider.blueprintName,
      provider.modifierName,
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
    String blueprintName,
    String modifierName,
  ) : this._internal(
          (ref) => fieldModifiers(
            ref as FieldModifiersRef,
            blueprintName,
            modifierName,
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
          blueprintName: blueprintName,
          modifierName: modifierName,
        );

  FieldModifiersProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.blueprintName,
    required this.modifierName,
  }) : super.internal();

  final String blueprintName;
  final String modifierName;

  @override
  Override overrideWith(
    Map<String, Modifier> Function(FieldModifiersRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: FieldModifiersProvider._internal(
        (ref) => create(ref as FieldModifiersRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        blueprintName: blueprintName,
        modifierName: modifierName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<Map<String, Modifier>> createElement() {
    return _FieldModifiersProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is FieldModifiersProvider &&
        other.blueprintName == blueprintName &&
        other.modifierName == modifierName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, blueprintName.hashCode);
    hash = _SystemHash.combine(hash, modifierName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin FieldModifiersRef on AutoDisposeProviderRef<Map<String, Modifier>> {
  /// The parameter `blueprintName` of this provider.
  String get blueprintName;

  /// The parameter `modifierName` of this provider.
  String get modifierName;
}

class _FieldModifiersProviderElement
    extends AutoDisposeProviderElement<Map<String, Modifier>>
    with FieldModifiersRef {
  _FieldModifiersProviderElement(super.provider);

  @override
  String get blueprintName => (origin as FieldModifiersProvider).blueprintName;
  @override
  String get modifierName => (origin as FieldModifiersProvider).modifierName;
}

String _$modifierPathsHash() => r'284078b7f30b5e344e771c4a093ded5131de6632';

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
    String blueprintName,
    String modifierName, [
    String? data,
  ]) {
    return ModifierPathsProvider(
      blueprintName,
      modifierName,
      data,
    );
  }

  @override
  ModifierPathsProvider getProviderOverride(
    covariant ModifierPathsProvider provider,
  ) {
    return call(
      provider.blueprintName,
      provider.modifierName,
      provider.data,
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
    String blueprintName,
    String modifierName, [
    String? data,
  ]) : this._internal(
          (ref) => modifierPaths(
            ref as ModifierPathsRef,
            blueprintName,
            modifierName,
            data,
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
          blueprintName: blueprintName,
          modifierName: modifierName,
          data: data,
        );

  ModifierPathsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.blueprintName,
    required this.modifierName,
    required this.data,
  }) : super.internal();

  final String blueprintName;
  final String modifierName;
  final String? data;

  @override
  Override overrideWith(
    List<String> Function(ModifierPathsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: ModifierPathsProvider._internal(
        (ref) => create(ref as ModifierPathsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        blueprintName: blueprintName,
        modifierName: modifierName,
        data: data,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<String>> createElement() {
    return _ModifierPathsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is ModifierPathsProvider &&
        other.blueprintName == blueprintName &&
        other.modifierName == modifierName &&
        other.data == data;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, blueprintName.hashCode);
    hash = _SystemHash.combine(hash, modifierName.hashCode);
    hash = _SystemHash.combine(hash, data.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin ModifierPathsRef on AutoDisposeProviderRef<List<String>> {
  /// The parameter `blueprintName` of this provider.
  String get blueprintName;

  /// The parameter `modifierName` of this provider.
  String get modifierName;

  /// The parameter `data` of this provider.
  String? get data;
}

class _ModifierPathsProviderElement
    extends AutoDisposeProviderElement<List<String>> with ModifierPathsRef {
  _ModifierPathsProviderElement(super.provider);

  @override
  String get blueprintName => (origin as ModifierPathsProvider).blueprintName;
  @override
  String get modifierName => (origin as ModifierPathsProvider).modifierName;
  @override
  String? get data => (origin as ModifierPathsProvider).data;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
