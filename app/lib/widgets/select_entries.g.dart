// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'select_entries.dart';

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

String _$isSelectingEntriesHash() =>
    r'7dbeb22f747ea2196261c6c276fb6c0985d291a8';

/// See also [isSelectingEntries].
final isSelectingEntriesProvider = AutoDisposeProvider<bool>(
  isSelectingEntries,
  name: r'isSelectingEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$isSelectingEntriesHash,
);
typedef IsSelectingEntriesRef = AutoDisposeProviderRef<bool>;
String _$hasEntryInSelectionHash() =>
    r'37d3f99d551b6c262d5482dab88037f89f214ce1';

/// See also [hasEntryInSelection].
class HasEntryInSelectionProvider extends AutoDisposeProvider<bool> {
  HasEntryInSelectionProvider(
    this.id,
  ) : super(
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

typedef HasEntryInSelectionRef = AutoDisposeProviderRef<bool>;

/// See also [hasEntryInSelection].
final hasEntryInSelectionProvider = HasEntryInSelectionFamily();

class HasEntryInSelectionFamily extends Family<bool> {
  HasEntryInSelectionFamily();

  HasEntryInSelectionProvider call(
    String id,
  ) {
    return HasEntryInSelectionProvider(
      id,
    );
  }

  @override
  AutoDisposeProvider<bool> getProviderOverride(
    covariant HasEntryInSelectionProvider provider,
  ) {
    return call(
      provider.id,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'hasEntryInSelectionProvider';
}

String _$selectingTagHash() => r'e22363af393fbc3ddb3ad07ab830c863da440dfa';

/// See also [selectingTag].
final selectingTagProvider = AutoDisposeProvider<String>(
  selectingTag,
  name: r'selectingTagProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$selectingTagHash,
);
typedef SelectingTagRef = AutoDisposeProviderRef<String>;
