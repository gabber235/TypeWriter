// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'manifest_view.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$manifestEntriesHash() => r'9c186fdee30d9960d12573d66cb2f060d3b3927e';

/// See also [manifestEntries].
@ProviderFor(manifestEntries)
final manifestEntriesProvider = AutoDisposeProvider<List<Entry>>.internal(
  manifestEntries,
  name: r'manifestEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$manifestEntriesHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef ManifestEntriesRef = AutoDisposeProviderRef<List<Entry>>;
String _$manifestEntryIdsHash() => r'a54b8e8a32fe603b535a9a30af0e4fd39d6a24ee';

/// See also [manifestEntryIds].
@ProviderFor(manifestEntryIds)
final manifestEntryIdsProvider = AutoDisposeProvider<List<String>>.internal(
  manifestEntryIds,
  name: r'manifestEntryIdsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$manifestEntryIdsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef ManifestEntryIdsRef = AutoDisposeProviderRef<List<String>>;
String _$entryReferencesHash() => r'3586d5429538084a11cd4329d4547dd2346ba02c';

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

/// See also [entryReferences].
@ProviderFor(entryReferences)
const entryReferencesProvider = EntryReferencesFamily();

/// See also [entryReferences].
class EntryReferencesFamily extends Family<Set<String>?> {
  /// See also [entryReferences].
  const EntryReferencesFamily();

  /// See also [entryReferences].
  EntryReferencesProvider call(
    String entryId,
  ) {
    return EntryReferencesProvider(
      entryId,
    );
  }

  @override
  EntryReferencesProvider getProviderOverride(
    covariant EntryReferencesProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'entryReferencesProvider';
}

/// See also [entryReferences].
class EntryReferencesProvider extends AutoDisposeProvider<Set<String>?> {
  /// See also [entryReferences].
  EntryReferencesProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryReferences(
            ref as EntryReferencesRef,
            entryId,
          ),
          from: entryReferencesProvider,
          name: r'entryReferencesProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryReferencesHash,
          dependencies: EntryReferencesFamily._dependencies,
          allTransitiveDependencies:
              EntryReferencesFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryReferencesProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    Set<String>? Function(EntryReferencesRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryReferencesProvider._internal(
        (ref) => create(ref as EntryReferencesRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<Set<String>?> createElement() {
    return _EntryReferencesProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryReferencesProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryReferencesRef on AutoDisposeProviderRef<Set<String>?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryReferencesProviderElement
    extends AutoDisposeProviderElement<Set<String>?> with EntryReferencesRef {
  _EntryReferencesProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryReferencesProvider).entryId;
}

String _$manifestGraphHash() => r'8feed62d6e7aacf18404b8159c32080eb9fdd7b3';

/// See also [manifestGraph].
@ProviderFor(manifestGraph)
final manifestGraphProvider = AutoDisposeProvider<Graph>.internal(
  manifestGraph,
  name: r'manifestGraphProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$manifestGraphHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef ManifestGraphRef = AutoDisposeProviderRef<Graph>;
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
