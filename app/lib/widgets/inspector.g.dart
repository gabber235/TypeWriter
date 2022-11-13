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

String $selectedEntryHash() => r'5e6499ecca7f1a0c6b0ae209ff4522267b47a482';

/// See also [selectedEntry].
final selectedEntryProvider = AutoDisposeProvider<Entry?>(
  selectedEntry,
  name: r'selectedEntryProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : $selectedEntryHash,
);
typedef SelectedEntryRef = AutoDisposeProviderRef<Entry?>;
String $entryDefinitionHash() => r'89f83097e410280addba5d831fce39857bd41e86';

/// See also [entryDefinition].
final entryDefinitionProvider = AutoDisposeProvider<EntryDefinition?>(
  entryDefinition,
  name: r'entryDefinitionProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : $entryDefinitionHash,
);
typedef EntryDefinitionRef = AutoDisposeProviderRef<EntryDefinition?>;
