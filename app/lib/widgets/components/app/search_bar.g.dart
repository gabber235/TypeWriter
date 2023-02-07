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

String _$searchFetchersHash() => r'2d5cadf196e4124536a572332a4fee5e52f107e1';

/// See also [searchFetchers].
final searchFetchersProvider = AutoDisposeProvider<List<SearchFetcher>>(
  searchFetchers,
  name: r'searchFetchersProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$searchFetchersHash,
);
typedef SearchFetchersRef = AutoDisposeProviderRef<List<SearchFetcher>>;
String _$searchFiltersHash() => r'7f0eaee452766397d6cbd49a4a6389c2df9ee84e';

/// See also [searchFilters].
final searchFiltersProvider = AutoDisposeProvider<List<SearchFilter>>(
  searchFilters,
  name: r'searchFiltersProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$searchFiltersHash,
);
typedef SearchFiltersRef = AutoDisposeProviderRef<List<SearchFilter>>;
String _$searchActionsHash() => r'd319428296d6711b07f1de0f11766dbdd0319fe8';

/// See also [searchActions].
final searchActionsProvider = AutoDisposeProvider<List<SearchAction>>(
  searchActions,
  name: r'searchActionsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$searchActionsHash,
);
typedef SearchActionsRef = AutoDisposeProviderRef<List<SearchAction>>;
String _$searchGlobalKeysHash() => r'44c4cb18cba3c7c62178d9135112196ad57da138';

/// See also [searchGlobalKeys].
final searchGlobalKeysProvider =
    AutoDisposeProvider<List<GlobalKey<State<StatefulWidget>>>>(
  searchGlobalKeys,
  name: r'searchGlobalKeysProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$searchGlobalKeysHash,
);
typedef SearchGlobalKeysRef
    = AutoDisposeProviderRef<List<GlobalKey<State<StatefulWidget>>>>;
String _$searchFocusNodesHash() => r'8c1aad12acb0c2cc62035af2f18f1b8f11f03327';

/// See also [searchFocusNodes].
final searchFocusNodesProvider = AutoDisposeProvider<List<FocusNode>>(
  searchFocusNodes,
  name: r'searchFocusNodesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$searchFocusNodesHash,
);
typedef SearchFocusNodesRef = AutoDisposeProviderRef<List<FocusNode>>;
String _$_fuzzyEntriesHash() => r'427a58e5164f8f798319d56da92dacd7876b591b';

/// See also [_fuzzyEntries].
final _fuzzyEntriesProvider = AutoDisposeProvider<Fuzzy<EntryDefinition>>(
  _fuzzyEntries,
  name: r'_fuzzyEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$_fuzzyEntriesHash,
);
typedef _FuzzyEntriesRef = AutoDisposeProviderRef<Fuzzy<EntryDefinition>>;
String _$_fuzzyBlueprintsHash() => r'172db39620f20b7b20a7d8fc933c4662b0852e0c';

/// See also [_fuzzyBlueprints].
final _fuzzyBlueprintsProvider = AutoDisposeProvider<Fuzzy<EntryBlueprint>>(
  _fuzzyBlueprints,
  name: r'_fuzzyBlueprintsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$_fuzzyBlueprintsHash,
);
typedef _FuzzyBlueprintsRef = AutoDisposeProviderRef<Fuzzy<EntryBlueprint>>;
