// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'app_router.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$currentRouteDataHash() => r'26e8ab76dc65802a22863a447292ee0d7c4e3b0d';

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

typedef CurrentRouteDataRef = ProviderRef<RouteData?>;

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
    String name,
  ) {
    return CurrentRouteDataProvider(
      name,
    );
  }

  @override
  CurrentRouteDataProvider getProviderOverride(
    covariant CurrentRouteDataProvider provider,
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
    this.name,
  ) : super.internal(
          (ref) => currentRouteData(
            ref,
            name,
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
        );

  final String name;

  @override
  bool operator ==(Object other) {
    return other is CurrentRouteDataProvider && other.name == name;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, name.hashCode);

    return _SystemHash.finish(hash);
  }
}
// ignore_for_file: unnecessary_raw_strings, subtype_of_sealed_class, invalid_use_of_internal_member, do_not_use_environment, prefer_const_constructors, public_member_api_docs, avoid_private_typedef_functions
