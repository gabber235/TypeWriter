// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'app_router.dart';

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

String _$currentRouteDataHash() => r'26e8ab76dc65802a22863a447292ee0d7c4e3b0d';

/// Provides the current route data for the given [name].
///
/// Copied from [currentRouteData].
class CurrentRouteDataProvider extends Provider<RouteData?> {
  CurrentRouteDataProvider(
    this.name,
  ) : super(
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

typedef CurrentRouteDataRef = ProviderRef<RouteData?>;

/// Provides the current route data for the given [name].
///
/// Copied from [currentRouteData].
final currentRouteDataProvider = CurrentRouteDataFamily();

class CurrentRouteDataFamily extends Family<RouteData?> {
  CurrentRouteDataFamily();

  CurrentRouteDataProvider call(
    String name,
  ) {
    return CurrentRouteDataProvider(
      name,
    );
  }

  @override
  Provider<RouteData?> getProviderOverride(
    covariant CurrentRouteDataProvider provider,
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
  String? get name => r'currentRouteDataProvider';
}
