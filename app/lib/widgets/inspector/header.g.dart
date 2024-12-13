// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'header.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$headerActionsHash() => r'dcfa29cb1ccbe3bfd3a995b00aa4bc4f76d20aed';

/// See also [headerActions].
@ProviderFor(headerActions)
final headerActionsProvider =
    AutoDisposeProvider<Map<String, HeaderActions>>.internal(
  headerActions,
  name: r'headerActionsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$headerActionsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

@Deprecated('Will be removed in 3.0. Use Ref instead')
// ignore: unused_element
typedef HeaderActionsRef = AutoDisposeProviderRef<Map<String, HeaderActions>>;
String _$actionsHash() => r'f20ccbc5db1263e994e56b9dfd275c92a711372a';

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

/// See also [_actions].
@ProviderFor(_actions)
const _actionsProvider = _ActionsFamily();

/// See also [_actions].
class _ActionsFamily extends Family<HeaderActions> {
  /// See also [_actions].
  const _ActionsFamily();

  /// See also [_actions].
  _ActionsProvider call(
    String path,
  ) {
    return _ActionsProvider(
      path,
    );
  }

  @override
  _ActionsProvider getProviderOverride(
    covariant _ActionsProvider provider,
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
  String? get name => r'_actionsProvider';
}

/// See also [_actions].
class _ActionsProvider extends AutoDisposeProvider<HeaderActions> {
  /// See also [_actions].
  _ActionsProvider(
    String path,
  ) : this._internal(
          (ref) => _actions(
            ref as _ActionsRef,
            path,
          ),
          from: _actionsProvider,
          name: r'_actionsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$actionsHash,
          dependencies: _ActionsFamily._dependencies,
          allTransitiveDependencies: _ActionsFamily._allTransitiveDependencies,
          path: path,
        );

  _ActionsProvider._internal(
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
    HeaderActions Function(_ActionsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _ActionsProvider._internal(
        (ref) => create(ref as _ActionsRef),
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
  AutoDisposeProviderElement<HeaderActions> createElement() {
    return _ActionsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _ActionsProvider && other.path == path;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, path.hashCode);

    return _SystemHash.finish(hash);
  }
}

@Deprecated('Will be removed in 3.0. Use Ref instead')
// ignore: unused_element
mixin _ActionsRef on AutoDisposeProviderRef<HeaderActions> {
  /// The parameter `path` of this provider.
  String get path;
}

class _ActionsProviderElement extends AutoDisposeProviderElement<HeaderActions>
    with _ActionsRef {
  _ActionsProviderElement(super.provider);

  @override
  String get path => (origin as _ActionsProvider).path;
}

String _$headerActionFiltersHash() =>
    r'56c608bbf9d2f8a06760e91488519e1e6da6efe2';

/// See also [headerActionFilters].
@ProviderFor(headerActionFilters)
final headerActionFiltersProvider =
    AutoDisposeProvider<List<HeaderActionFilter>>.internal(
  headerActionFilters,
  name: r'headerActionFiltersProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$headerActionFiltersHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

@Deprecated('Will be removed in 3.0. Use Ref instead')
// ignore: unused_element
typedef HeaderActionFiltersRef
    = AutoDisposeProviderRef<List<HeaderActionFilter>>;
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member, deprecated_member_use_from_same_package
