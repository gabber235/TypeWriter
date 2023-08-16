// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entry_node.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$writersHash() => r'991a539369dd5e8d952a9bd955779c6a4e65c69f';

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

typedef _WritersRef = AutoDisposeProviderRef<List<Writer>>;

/// See also [_writers].
@ProviderFor(_writers)
const _writersProvider = _WritersFamily();

/// See also [_writers].
class _WritersFamily extends Family<List<Writer>> {
  /// See also [_writers].
  const _WritersFamily();

  /// See also [_writers].
  _WritersProvider call(
    String id,
  ) {
    return _WritersProvider(
      id,
    );
  }

  @override
  _WritersProvider getProviderOverride(
    covariant _WritersProvider provider,
  ) {
    return call(
      provider.id,
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
  String? get name => r'_writersProvider';
}

/// See also [_writers].
class _WritersProvider extends AutoDisposeProvider<List<Writer>> {
  /// See also [_writers].
  _WritersProvider(
    this.id,
  ) : super.internal(
          (ref) => _writers(
            ref,
            id,
          ),
          from: _writersProvider,
          name: r'_writersProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$writersHash,
          dependencies: _WritersFamily._dependencies,
          allTransitiveDependencies: _WritersFamily._allTransitiveDependencies,
        );

  final String id;

  @override
  bool operator ==(Object other) {
    return other is _WritersProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member
