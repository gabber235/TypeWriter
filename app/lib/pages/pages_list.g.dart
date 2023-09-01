// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'pages_list.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$pagesDataHash() => r'9907c05e4d5a4641c759f10afee98c130c47cf48';

/// See also [_pagesData].
@ProviderFor(_pagesData)
final _pagesDataProvider = AutoDisposeProvider<List<_PageData>>.internal(
  _pagesData,
  name: r'_pagesDataProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$pagesDataHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _PagesDataRef = AutoDisposeProviderRef<List<_PageData>>;
String _$pageNamesHash() => r'd834681c3a622b64fd2562eb17db3434db172946';

/// See also [_pageNames].
@ProviderFor(_pageNames)
final _pageNamesProvider = AutoDisposeProvider<List<String>>.internal(
  _pageNames,
  name: r'_pageNamesProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$pageNamesHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _PageNamesRef = AutoDisposeProviderRef<List<String>>;
String _$writersHash() => r'49be74d2cb1d297d1af054c85976d3f6c4251713';

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
    String pageId,
  ) {
    return _WritersProvider(
      pageId,
    );
  }

  @override
  _WritersProvider getProviderOverride(
    covariant _WritersProvider provider,
  ) {
    return call(
      provider.pageId,
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
    this.pageId,
  ) : super.internal(
          (ref) => _writers(
            ref,
            pageId,
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

  final String pageId;

  @override
  bool operator ==(Object other) {
    return other is _WritersProvider && other.pageId == pageId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);

    return _SystemHash.finish(hash);
  }
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member
