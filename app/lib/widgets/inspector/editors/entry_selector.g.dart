// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entry_selector.dart';

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

String _$entriesFromTagHash() => r'33a9868fdd798563abb49d7ff3c7a5acfe123a30';

/// See also [entriesFromTag].
class EntriesFromTagProvider extends AutoDisposeProvider<Map<String, Entry>> {
  EntriesFromTagProvider(
    this.tag,
  ) : super(
          (ref) => entriesFromTag(
            ref,
            tag,
          ),
          from: entriesFromTagProvider,
          name: r'entriesFromTagProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entriesFromTagHash,
        );

  final String tag;

  @override
  bool operator ==(Object other) {
    return other is EntriesFromTagProvider && other.tag == tag;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, tag.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef EntriesFromTagRef = AutoDisposeProviderRef<Map<String, Entry>>;

/// See also [entriesFromTag].
final entriesFromTagProvider = EntriesFromTagFamily();

class EntriesFromTagFamily extends Family<Map<String, Entry>> {
  EntriesFromTagFamily();

  EntriesFromTagProvider call(
    String tag,
  ) {
    return EntriesFromTagProvider(
      tag,
    );
  }

  @override
  AutoDisposeProvider<Map<String, Entry>> getProviderOverride(
    covariant EntriesFromTagProvider provider,
  ) {
    return call(
      provider.tag,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'entriesFromTagProvider';
}
