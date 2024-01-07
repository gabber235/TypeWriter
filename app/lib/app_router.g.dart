// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'app_router.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$currentRouteDataHash() => r'3f2df7b23b6d8e38ccafd0d9de54c82fcc924dc6';

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

/// Provides the current route data for the given [name].
///
/// Copied from [currentRouteData].
@ProviderFor(currentRouteData)
const currentRouteDataProvider = CurrentRouteDataFamily();

/// Provides the current route data for the given [name].
///
/// Copied from [currentRouteData].
class CurrentRouteDataFamily extends Family<RouteData?> {
  /// Provides the current route data for the given [name].
  ///
  /// Copied from [currentRouteData].
  const CurrentRouteDataFamily();

  /// Provides the current route data for the given [name].
  ///
  /// Copied from [currentRouteData].
  CurrentRouteDataProvider call(
    String path,
  ) {
    return CurrentRouteDataProvider(
      path,
    );
  }

  @override
  CurrentRouteDataProvider getProviderOverride(
    covariant CurrentRouteDataProvider provider,
  ) {
    return call(
      provider.path,
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
  String? get name => r'currentRouteDataProvider';
}

/// Provides the current route data for the given [name].
///
/// Copied from [currentRouteData].
class CurrentRouteDataProvider extends Provider<RouteData?> {
  /// Provides the current route data for the given [name].
  ///
  /// Copied from [currentRouteData].
  CurrentRouteDataProvider(
    String path,
  ) : this._internal(
          (ref) => currentRouteData(
            ref as CurrentRouteDataRef,
            path,
          ),
          from: currentRouteDataProvider,
          name: r'currentRouteDataProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$currentRouteDataHash,
          dependencies: CurrentRouteDataFamily._dependencies,
          allTransitiveDependencies:
              CurrentRouteDataFamily._allTransitiveDependencies,
          path: path,
        );

  CurrentRouteDataProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.path,
  }) : super.internal();

  final String path;

  @override
  Override overrideWith(
    RouteData? Function(CurrentRouteDataRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: CurrentRouteDataProvider._internal(
        (ref) => create(ref as CurrentRouteDataRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        path: path,
      ),
    );
  }

  @override
  ProviderElement<RouteData?> createElement() {
    return _CurrentRouteDataProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is CurrentRouteDataProvider && other.path == path;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, path.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin CurrentRouteDataRef on ProviderRef<RouteData?> {
  /// The parameter `path` of this provider.
  String get path;
}

class _CurrentRouteDataProviderElement extends ProviderElement<RouteData?>
    with CurrentRouteDataRef {
  _CurrentRouteDataProviderElement(super.provider);

  @override
  String get path => (origin as CurrentRouteDataProvider).path;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
