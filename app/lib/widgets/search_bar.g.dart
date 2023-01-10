// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'search_bar.dart';

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

String _$_fuzzyEntriesHash() => r'34ffff98f1b2c262ecb01421d24a7034f77cb3ff';

/// See also [_fuzzyEntries].
final _fuzzyEntriesProvider = AutoDisposeProvider<Fuzzy<Entry>>(
  _fuzzyEntries,
  name: r'_fuzzyEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$_fuzzyEntriesHash,
);
typedef _FuzzyEntriesRef = AutoDisposeProviderRef<Fuzzy<Entry>>;
String _$_fuzzyBlueprintsHash() => r'4f3deb485417dd7aa708887da6bafa06a4e42798';

/// See also [_fuzzyBlueprints].
final _fuzzyBlueprintsProvider = AutoDisposeProvider<Fuzzy<EntryBlueprint>>(
  _fuzzyBlueprints,
  name: r'_fuzzyBlueprintsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$_fuzzyBlueprintsHash,
);
typedef _FuzzyBlueprintsRef = AutoDisposeProviderRef<Fuzzy<EntryBlueprint>>;
String _$_actionsHash() => r'1196b42fa9a321108ae320068529110d64cffb0b';

/// See also [_actions].
final _actionsProvider = AutoDisposeProvider<List<_Action>>(
  _actions,
  name: r'_actionsProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$_actionsHash,
);
typedef _ActionsRef = AutoDisposeProviderRef<List<_Action>>;
