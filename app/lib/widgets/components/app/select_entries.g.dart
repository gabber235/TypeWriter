// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'select_entries.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$isSelectingEntriesHash() =>
    r'7dbeb22f747ea2196261c6c276fb6c0985d291a8';

/// See also [isSelectingEntries].
@ProviderFor(isSelectingEntries)
final isSelectingEntriesProvider = AutoDisposeProvider<bool>.internal(
  isSelectingEntries,
  name: r'isSelectingEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$isSelectingEntriesHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef IsSelectingEntriesRef = AutoDisposeProviderRef<bool>;
String _$hasEntryInSelectionHash() =>
    r'37d3f99d551b6c262d5482dab88037f89f214ce1';

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

typedef HasEntryInSelectionRef = AutoDisposeProviderRef<bool>;

/// See also [hasEntryInSelection].
@ProviderFor(hasEntryInSelection)
const hasEntryInSelectionProvider = HasEntryInSelectionFamily();

/// See also [hasEntryInSelection].
class HasEntryInSelectionFamily extends Family<bool> {
  /// See also [hasEntryInSelection].
  const HasEntryInSelectionFamily();

  /// See also [hasEntryInSelection].
  HasEntryInSelectionProvider call(
    String id,
  ) {
    return HasEntryInSelectionProvider(
      id,
    );
  }

  @override
  HasEntryInSelectionProvider getProviderOverride(
    covariant HasEntryInSelectionProvider provider,
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
  String? get name => r'hasEntryInSelectionProvider';
}

/// See also [hasEntryInSelection].
class HasEntryInSelectionProvider extends AutoDisposeProvider<bool> {
  /// See also [hasEntryInSelection].
  HasEntryInSelectionProvider(
    this.id,
  ) : super.internal(
          (ref) => hasEntryInSelection(
            ref,
            id,
          ),
          from: hasEntryInSelectionProvider,
          name: r'hasEntryInSelectionProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$hasEntryInSelectionHash,
          dependencies: HasEntryInSelectionFamily._dependencies,
          allTransitiveDependencies:
              HasEntryInSelectionFamily._allTransitiveDependencies,
        );

  final String id;

  @override
  bool operator ==(Object other) {
    return other is HasEntryInSelectionProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}

String _$selectingTagHash() => r'e22363af393fbc3ddb3ad07ab830c863da440dfa';

/// See also [selectingTag].
@ProviderFor(selectingTag)
final selectingTagProvider = AutoDisposeProvider<String>.internal(
  selectingTag,
  name: r'selectingTagProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$selectingTagHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef SelectingTagRef = AutoDisposeProviderRef<String>;
// ignore_for_file: unnecessary_raw_strings, subtype_of_sealed_class, invalid_use_of_internal_member, do_not_use_environment, prefer_const_constructors, public_member_api_docs, avoid_private_typedef_functions
