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
          .map((e) => AdapterEntry.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$_AdapterToJson(_$_Adapter instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
      'version': instance.version,
      'entries': instance.entries,
    };

_$_AdapterEntry _$$_AdapterEntryFromJson(Map<String, dynamic> json) =>
    _$_AdapterEntry(
      name: json['name'] as String,
      description: json['description'] as String,
      fields: ObjectField.fromJson(json['fields'] as Map<String, dynamic>),
      color: json['color'] == null
          ? Colors.grey
          : const ColorConverter().fromJson(json['color'] as String),
    );

Map<String, dynamic> _$$_AdapterEntryToJson(_$_AdapterEntry instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
      'fields': instance.fields,
      'color': const ColorConverter().toJson(instance.color),
    };

_$_FieldType _$$_FieldTypeFromJson(Map<String, dynamic> json) => _$_FieldType(
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$_FieldTypeToJson(_$_FieldType instance) =>
    <String, dynamic>{
      'kind': instance.$type,
    };

_$PrimitiveField _$$PrimitiveFieldFromJson(Map<String, dynamic> json) =>
    _$PrimitiveField(
      type: $enumDecode(_$PrimitiveFieldTypeEnumMap, json['type']),
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$PrimitiveFieldToJson(_$PrimitiveField instance) =>
    <String, dynamic>{
      'type': _$PrimitiveFieldTypeEnumMap[instance.type]!,
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
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$EnumFieldToJson(_$EnumField instance) =>
    <String, dynamic>{
      'values': instance.values,
      'kind': instance.$type,
    };

_$ListField _$$ListFieldFromJson(Map<String, dynamic> json) => _$ListField(
      type: FieldType.fromJson(json['type'] as Map<String, dynamic>),
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$ListFieldToJson(_$ListField instance) =>
    <String, dynamic>{
      'type': instance.type,
      'kind': instance.$type,
    };

_$ObjectField _$$ObjectFieldFromJson(Map<String, dynamic> json) =>
    _$ObjectField(
      fields: (json['fields'] as Map<String, dynamic>).map(
        (k, e) => MapEntry(k, FieldType.fromJson(e as Map<String, dynamic>)),
      ),
      $type: json['kind'] as String?,
    );

Map<String, dynamic> _$$ObjectFieldToJson(_$ObjectField instance) =>
    <String, dynamic>{
      'fields': instance.fields,
      'kind': instance.$type,
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

String $adaptersHash() => r'39c289ee0d4a8e60e6fccc8ebb2dac29e6ad2919';

/// See also [adapters].
final adaptersProvider = AutoDisposeProvider<List<Adapter>>(
  adapters,
  name: r'adaptersProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : $adaptersHash,
);
typedef AdaptersRef = AutoDisposeProviderRef<List<Adapter>>;
String $adapterEntriesHash() => r'e4a4c54d59b3bcf60472c24166173d0e20a70260';

/// See also [adapterEntries].
final adapterEntriesProvider = AutoDisposeProvider<List<AdapterEntry>>(
  adapterEntries,
  name: r'adapterEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : $adapterEntriesHash,
);
typedef AdapterEntriesRef = AutoDisposeProviderRef<List<AdapterEntry>>;
String $adapterEntryHash() => r'f00f08d020dc320557235c673f24a81626659f17';

/// See also [adapterEntry].
class AdapterEntryProvider extends AutoDisposeProvider<AdapterEntry?> {
  AdapterEntryProvider(
    this.name,
  ) : super(
          (ref) => adapterEntry(
            ref,
            name,
          ),
          from: adapterEntryProvider,
          name: r'adapterEntryProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : $adapterEntryHash,
        );

  final String name;

  @override
  bool operator ==(Object other) {
    return other is AdapterEntryProvider && other.name == name;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, name.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef AdapterEntryRef = AutoDisposeProviderRef<AdapterEntry?>;

/// See also [adapterEntry].
final adapterEntryProvider = AdapterEntryFamily();

class AdapterEntryFamily extends Family<AdapterEntry?> {
  AdapterEntryFamily();

  AdapterEntryProvider call(
    String name,
  ) {
    return AdapterEntryProvider(
      name,
    );
  }

  @override
  AutoDisposeProvider<AdapterEntry?> getProviderOverride(
    covariant AdapterEntryProvider provider,
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
  String? get name => r'adapterEntryProvider';
}
