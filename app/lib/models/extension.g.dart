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
      'extension': instance.extension.toJson(),
      'entries': instance.entries.map((e) => e.toJson()).toList(),
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

String _$extensionsHash() => r'10f9edbc062d3761b5fdbeb067a30725925444ba';

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

@Deprecated('Will be removed in 3.0. Use Ref instead')
// ignore: unused_element
typedef ExtensionsRef = AutoDisposeProviderRef<List<Extension>>;
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member, deprecated_member_use_from_same_package
