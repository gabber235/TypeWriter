// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entries_graph.dart';

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

String _$graphableEntriesHash() => r'4ca3712feee7d5ce0a1fc89332a04a993a2fed2f';

/// See also [graphableEntries].
final graphableEntriesProvider = AutoDisposeProvider<List<Entry>>(
  graphableEntries,
  name: r'graphableEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$graphableEntriesHash,
);
typedef GraphableEntriesRef = AutoDisposeProviderRef<List<Entry>>;
String _$graphHash() => r'7269326e7ddcfc6d7d7d305a4be789fc3b3ec1e0';

/// See also [graph].
final graphProvider = AutoDisposeProvider<Graph>(
  graph,
  name: r'graphProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$graphHash,
);
typedef GraphRef = AutoDisposeProviderRef<Graph>;
