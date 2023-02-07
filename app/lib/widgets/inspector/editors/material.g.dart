// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'material.dart';

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

String _$materialPropertiesHash() =>
    r'2537760a0cb4dc55f7fb40e4e045184959705bc2';

/// See also [materialProperties].
class MaterialPropertiesProvider
    extends AutoDisposeProvider<List<MaterialProperty>> {
  MaterialPropertiesProvider(
    this.meta,
  ) : super(
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

typedef MaterialPropertiesRef = AutoDisposeProviderRef<List<MaterialProperty>>;

/// See also [materialProperties].
final materialPropertiesProvider = MaterialPropertiesFamily();

class MaterialPropertiesFamily extends Family<List<MaterialProperty>> {
  MaterialPropertiesFamily();

  MaterialPropertiesProvider call(
    String meta,
  ) {
    return MaterialPropertiesProvider(
      meta,
    );
  }

  @override
  AutoDisposeProvider<List<MaterialProperty>> getProviderOverride(
    covariant MaterialPropertiesProvider provider,
  ) {
    return call(
      provider.meta,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'materialPropertiesProvider';
}

String _$_fuzzyMaterialsHash() => r'bfaa303c86894a2592b383e46588f82a7a8b7e6d';

/// See also [_fuzzyMaterials].
final _fuzzyMaterialsProvider =
    AutoDisposeProvider<Fuzzy<MapEntry<String, MinecraftMaterial>>>(
  _fuzzyMaterials,
  name: r'_fuzzyMaterialsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$_fuzzyMaterialsHash,
);
typedef _FuzzyMaterialsRef
    = AutoDisposeProviderRef<Fuzzy<MapEntry<String, MinecraftMaterial>>>;
