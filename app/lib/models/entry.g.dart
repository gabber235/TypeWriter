// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entry.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$entryDefinitionHash() => r'5cd9fa81725650c8aedfcf39f5cb1f767433e4f1';

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

typedef EntryDefinitionRef = AutoDisposeProviderRef<EntryDefinition?>;

/// See also [entryDefinition].
@ProviderFor(entryDefinition)
const entryDefinitionProvider = EntryDefinitionFamily();

/// See also [entryDefinition].
class EntryDefinitionFamily extends Family<EntryDefinition?> {
  /// See also [entryDefinition].
  const EntryDefinitionFamily();

  /// See also [entryDefinition].
  EntryDefinitionProvider call(
    String pageId,
    String entryId,
  ) {
    return EntryDefinitionProvider(
      pageId,
      entryId,
    );
  }

  @override
  EntryDefinitionProvider getProviderOverride(
    covariant EntryDefinitionProvider provider,
  ) {
    return call(
      provider.pageId,
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
  String? get name => r'entryDefinitionProvider';
}

/// See also [entryDefinition].
class EntryDefinitionProvider extends AutoDisposeProvider<EntryDefinition?> {
  /// See also [entryDefinition].
  EntryDefinitionProvider(
    this.pageId,
    this.entryId,
  ) : super.internal(
          (ref) => entryDefinition(
            ref,
            pageId,
            entryId,
          ),
          from: entryDefinitionProvider,
          name: r'entryDefinitionProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryDefinitionHash,
          dependencies: EntryDefinitionFamily._dependencies,
          allTransitiveDependencies:
              EntryDefinitionFamily._allTransitiveDependencies,
        );

  final String pageId;
  final String entryId;

  @override
  bool operator ==(Object other) {
    return other is EntryDefinitionProvider &&
        other.pageId == pageId &&
        other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}
// ignore_for_file: unnecessary_raw_strings, subtype_of_sealed_class, invalid_use_of_internal_member, do_not_use_environment, prefer_const_constructors, public_member_api_docs, avoid_private_typedef_functions
