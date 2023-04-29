// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'list.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$listValueLengthHash() => r'7137d243eb3e3696ee04b3370dff7fb597570b7f';

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

typedef _ListValueLengthRef = AutoDisposeProviderRef<int>;

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
    this.path,
  ) : super.internal(
          (ref) => _listValueLength(
            ref,
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
        );

  final String path;

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
// ignore_for_file: unnecessary_raw_strings, subtype_of_sealed_class, invalid_use_of_internal_member, do_not_use_environment, prefer_const_constructors, public_member_api_docs, avoid_private_typedef_functions
