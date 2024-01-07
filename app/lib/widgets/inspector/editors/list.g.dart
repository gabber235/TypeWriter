// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'list.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$listValueLengthHash() => r'df97a1cf8a545d4290a4ebda9f64f91deaf9e8e9';

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

/// See also [_listValueLength].
@ProviderFor(_listValueLength)
const _listValueLengthProvider = _ListValueLengthFamily();

/// See also [_listValueLength].
class _ListValueLengthFamily extends Family<int> {
  /// See also [_listValueLength].
  const _ListValueLengthFamily();

  /// See also [_listValueLength].
  _ListValueLengthProvider call(
    String path,
  ) {
    return _ListValueLengthProvider(
      path,
    );
  }

  @override
  _ListValueLengthProvider getProviderOverride(
    covariant _ListValueLengthProvider provider,
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
  String? get name => r'_listValueLengthProvider';
}

/// See also [_listValueLength].
class _ListValueLengthProvider extends AutoDisposeProvider<int> {
  /// See also [_listValueLength].
  _ListValueLengthProvider(
    String path,
  ) : this._internal(
          (ref) => _listValueLength(
            ref as _ListValueLengthRef,
            path,
          ),
          from: _listValueLengthProvider,
          name: r'_listValueLengthProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$listValueLengthHash,
          dependencies: _ListValueLengthFamily._dependencies,
          allTransitiveDependencies:
              _ListValueLengthFamily._allTransitiveDependencies,
          path: path,
        );

  _ListValueLengthProvider._internal(
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
    int Function(_ListValueLengthRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _ListValueLengthProvider._internal(
        (ref) => create(ref as _ListValueLengthRef),
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
  AutoDisposeProviderElement<int> createElement() {
    return _ListValueLengthProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _ListValueLengthProvider && other.path == path;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, path.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _ListValueLengthRef on AutoDisposeProviderRef<int> {
  /// The parameter `path` of this provider.
  String get path;
}

class _ListValueLengthProviderElement extends AutoDisposeProviderElement<int>
    with _ListValueLengthRef {
  _ListValueLengthProviderElement(super.provider);

  @override
  String get path => (origin as _ListValueLengthProvider).path;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
