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

String $_fuzzyEntriesHash() => r'1b3df7371bbf054615fbb11056cb9d76dd7ae297';

/// See also [_fuzzyEntries].
final _fuzzyEntriesProvider = AutoDisposeProvider<Fuzzy<Entry>>(
  _fuzzyEntries,
  name: r'_fuzzyEntriesProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : $_fuzzyEntriesHash,
);
typedef _FuzzyEntriesRef = AutoDisposeProviderRef<Fuzzy<Entry>>;
String $_fuzzyAddEntriesHash() => r'15d3f5c577c206e2f5c8ccdb9d25b0141437fdb1';

/// See also [_fuzzyAddEntries].
final _fuzzyAddEntriesProvider = AutoDisposeProvider<Fuzzy<AddEntry>>(
  _fuzzyAddEntries,
  name: r'_fuzzyAddEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : $_fuzzyAddEntriesHash,
);
typedef _FuzzyAddEntriesRef = AutoDisposeProviderRef<Fuzzy<AddEntry>>;
String $_actionsHash() => r'7e47833396f9828d9b9b2a96abc3e2ccc47d3ca2';

/// See also [_actions].
final _actionsProvider = AutoDisposeProvider<List<_Action>>(
  _actions,
  name: r'_actionsProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : $_actionsHash,
);
typedef _ActionsRef = AutoDisposeProviderRef<List<_Action>>;
