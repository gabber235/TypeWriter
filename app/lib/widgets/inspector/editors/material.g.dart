// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'material.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$materialPropertiesHash() =>
    r'ec0ce652cc3458c9e2ee4d54c4fa1f496a45f1db';

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
    String meta,
  ) : this._internal(
          (ref) => materialProperties(
            ref as MaterialPropertiesRef,
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
          meta: meta,
        );

  MaterialPropertiesProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.meta,
  }) : super.internal();

  final String meta;

  @override
  Override overrideWith(
    List<MaterialProperty> Function(MaterialPropertiesRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: MaterialPropertiesProvider._internal(
        (ref) => create(ref as MaterialPropertiesRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        meta: meta,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<MaterialProperty>> createElement() {
    return _MaterialPropertiesProviderElement(this);
  }

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

mixin MaterialPropertiesRef on AutoDisposeProviderRef<List<MaterialProperty>> {
  /// The parameter `meta` of this provider.
  String get meta;
}

class _MaterialPropertiesProviderElement
    extends AutoDisposeProviderElement<List<MaterialProperty>>
    with MaterialPropertiesRef {
  _MaterialPropertiesProviderElement(super.provider);

  @override
  String get meta => (origin as MaterialPropertiesProvider).meta;
}

String _$fuzzyMaterialsHash() => r'bfaa303c86894a2592b383e46588f82a7a8b7e6d';

/// See also [_fuzzyMaterials].
@ProviderFor(_fuzzyMaterials)
final _fuzzyMaterialsProvider =
    AutoDisposeProvider<Fuzzy<CombinedMaterial>>.internal(
  _fuzzyMaterials,
  name: r'_fuzzyMaterialsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$fuzzyMaterialsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _FuzzyMaterialsRef = AutoDisposeProviderRef<Fuzzy<CombinedMaterial>>;
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
