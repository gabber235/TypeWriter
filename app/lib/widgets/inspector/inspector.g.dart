// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'inspector.dart';

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

String _$inspectingEntryHash() => r'84ccfbfca0da9b171e8f509e02c225094ff4636c';

/// See also [inspectingEntry].
final inspectingEntryProvider = AutoDisposeProvider<Entry?>(
  inspectingEntry,
  name: r'inspectingEntryProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$inspectingEntryHash,
);
typedef InspectingEntryRef = AutoDisposeProviderRef<Entry?>;
String _$inspectingEntryDefinitionHash() =>
    r'a3c771a509fe14061f2fe969813f1851afb458c6';

/// See also [inspectingEntryDefinition].
final inspectingEntryDefinitionProvider = AutoDisposeProvider<EntryDefinition?>(
  inspectingEntryDefinition,
  name: r'inspectingEntryDefinitionProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$inspectingEntryDefinitionHash,
);
typedef InspectingEntryDefinitionRef = AutoDisposeProviderRef<EntryDefinition?>;
