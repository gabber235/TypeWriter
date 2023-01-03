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
String _$triggerPathsHash() => r'fe9fc27eaf50cce29fb0c25957206160dd9f66d0';

/// See also [triggerPaths].
class TriggerPathsProvider extends AutoDisposeProvider<List<String>> {
  TriggerPathsProvider(
    this.type,
    this.isReceiver,
  ) : super(
          (ref) => triggerPaths(
            ref,
            type,
            isReceiver,
          ),
          from: triggerPathsProvider,
          name: r'triggerPathsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$triggerPathsHash,
        );

  final String type;
  final bool isReceiver;

  @override
  bool operator ==(Object other) {
    return other is TriggerPathsProvider &&
        other.type == type &&
        other.isReceiver == isReceiver;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, type.hashCode);
    hash = _SystemHash.combine(hash, isReceiver.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef TriggerPathsRef = AutoDisposeProviderRef<List<String>>;

/// See also [triggerPaths].
final triggerPathsProvider = TriggerPathsFamily();

class TriggerPathsFamily extends Family<List<String>> {
  TriggerPathsFamily();

  TriggerPathsProvider call(
    String type,
    bool isReceiver,
  ) {
    return TriggerPathsProvider(
      type,
      isReceiver,
    );
  }

  @override
  AutoDisposeProvider<List<String>> getProviderOverride(
    covariant TriggerPathsProvider provider,
  ) {
    return call(
      provider.type,
      provider.isReceiver,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'triggerPathsProvider';
}

String _$graphHash() => r'a99712df689e8750a5cd4c1bf512e0284e2063bd';

/// See also [graph].
final graphProvider = AutoDisposeProvider<Graph>(
  graph,
  name: r'graphProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$graphHash,
);
typedef GraphRef = AutoDisposeProviderRef<Graph>;
