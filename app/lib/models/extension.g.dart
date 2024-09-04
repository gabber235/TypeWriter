// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'extension.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$ExtensionImpl _$$ExtensionImplFromJson(Map<String, dynamic> json) =>
    _$ExtensionImpl(
      extension:
          ExtensionInfo.fromJson(json['extension'] as Map<String, dynamic>),
      entries: (json['entries'] as List<dynamic>)
          .map((e) => EntryBlueprint.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$ExtensionImplToJson(_$ExtensionImpl instance) =>
    <String, dynamic>{
      'extension': instance.extension,
      'entries': instance.entries,
    };

_$ExtensionInfoImpl _$$ExtensionInfoImplFromJson(Map<String, dynamic> json) =>
    _$ExtensionInfoImpl(
      name: json['name'] as String,
      shortDescription: json['shortDescription'] as String,
      description: json['description'] as String,
      version: json['version'] as String,
    );

Map<String, dynamic> _$$ExtensionInfoImplToJson(_$ExtensionInfoImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'shortDescription': instance.shortDescription,
      'description': instance.description,
      'version': instance.version,
    };

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$extensionsHash() => r'c5ab97411f68e5201eaf3154a8c578929e519906';

/// A generated provider to fetch and cache a list of [Extension]s.
///
/// Copied from [extensions].
@ProviderFor(extensions)
final extensionsProvider = AutoDisposeProvider<List<Extension>>.internal(
  extensions,
  name: r'extensionsProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$extensionsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef ExtensionsRef = AutoDisposeProviderRef<List<Extension>>;
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
