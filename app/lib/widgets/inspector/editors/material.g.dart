// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'material.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$materialPropertiesHash() =>
    r'2537760a0cb4dc55f7fb40e4e045184959705bc2';

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

typedef MaterialPropertiesRef = AutoDisposeProviderRef<List<MaterialProperty>>;

/// See also [materialProperties].
@ProviderFor(materialProperties)
const materialPropertiesProvider = MaterialPropertiesFamily();

/// See also [materialProperties].
class MaterialPropertiesFamily extends Family<List<MaterialProperty>> {
  /// See also [materialProperties].
  const MaterialPropertiesFamily();

  /// See also [materialProperties].
  MaterialPropertiesProvider call(
    String meta,
  ) {
    return MaterialPropertiesProvider(
      meta,
    );
  }

  @override
  MaterialPropertiesProvider getProviderOverride(
    covariant MaterialPropertiesProvider provider,
  ) {
    return call(
      provider.meta,
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
  String? get name => r'materialPropertiesProvider';
}

/// See also [materialProperties].
class MaterialPropertiesProvider
    extends AutoDisposeProvider<List<MaterialProperty>> {
  /// See also [materialProperties].
  MaterialPropertiesProvider(
    this.meta,
  ) : super.internal(
          (ref) => materialProperties(
            ref,
            meta,
          ),
          from: materialPropertiesProvider,
          name: r'materialPropertiesProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$materialPropertiesHash,
          dependencies: MaterialPropertiesFamily._dependencies,
          allTransitiveDependencies:
              MaterialPropertiesFamily._allTransitiveDependencies,
        );

  final String meta;

  @override
  bool operator ==(Object other) {
    return other is MaterialPropertiesProvider && other.meta == meta;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, meta.hashCode);

    return _SystemHash.finish(hash);
  }
}

String _$fuzzyMaterialsHash() => r'bfaa303c86894a2592b383e46588f82a7a8b7e6d';

/// See also [_fuzzyMaterials].
@ProviderFor(_fuzzyMaterials)
final _fuzzyMaterialsProvider =
    AutoDisposeProvider<Fuzzy<MapEntry<String, MinecraftMaterial>>>.internal(
  _fuzzyMaterials,
  name: r'_fuzzyMaterialsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$fuzzyMaterialsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _FuzzyMaterialsRef
    = AutoDisposeProviderRef<Fuzzy<MapEntry<String, MinecraftMaterial>>>;
// ignore_for_file: unnecessary_raw_strings, subtype_of_sealed_class, invalid_use_of_internal_member, do_not_use_environment, prefer_const_constructors, public_member_api_docs, avoid_private_typedef_functions
