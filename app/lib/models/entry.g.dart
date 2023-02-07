// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entry.dart';

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

String _$entryDefinitionHash() => r'5cd9fa81725650c8aedfcf39f5cb1f767433e4f1';

/// See also [entryDefinition].
class EntryDefinitionProvider extends AutoDisposeProvider<EntryDefinition?> {
  EntryDefinitionProvider(
    this.pageId,
    this.entryId,
  ) : super(
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

typedef EntryDefinitionRef = AutoDisposeProviderRef<EntryDefinition?>;

/// See also [entryDefinition].
final entryDefinitionProvider = EntryDefinitionFamily();

class EntryDefinitionFamily extends Family<EntryDefinition?> {
  EntryDefinitionFamily();

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
  AutoDisposeProvider<EntryDefinition?> getProviderOverride(
    covariant EntryDefinitionProvider provider,
  ) {
    return call(
      provider.pageId,
      provider.entryId,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'entryDefinitionProvider';
}
