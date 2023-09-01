// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'segment.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$segmentWritersHash() => r'011eacd554b34d97ed234e8d2a30d527854c1a20';

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

typedef SegmentWritersRef = AutoDisposeProviderRef<List<Writer>>;

/// See also [segmentWriters].
@ProviderFor(segmentWriters)
const segmentWritersProvider = SegmentWritersFamily();

/// See also [segmentWriters].
class SegmentWritersFamily extends Family<List<Writer>> {
  /// See also [segmentWriters].
  const SegmentWritersFamily();

  /// See also [segmentWriters].
  SegmentWritersProvider call(
    String entryId,
    String segmentId,
  ) {
    return SegmentWritersProvider(
      entryId,
      segmentId,
    );
  }

  @override
  SegmentWritersProvider getProviderOverride(
    covariant SegmentWritersProvider provider,
  ) {
    return call(
      provider.entryId,
      provider.segmentId,
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
  String? get name => r'segmentWritersProvider';
}

/// See also [segmentWriters].
class SegmentWritersProvider extends AutoDisposeProvider<List<Writer>> {
  /// See also [segmentWriters].
  SegmentWritersProvider(
    this.entryId,
    this.segmentId,
  ) : super.internal(
          (ref) => segmentWriters(
            ref,
            entryId,
            segmentId,
          ),
          from: segmentWritersProvider,
          name: r'segmentWritersProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$segmentWritersHash,
          dependencies: SegmentWritersFamily._dependencies,
          allTransitiveDependencies:
              SegmentWritersFamily._allTransitiveDependencies,
        );

  final String entryId;
  final String segmentId;

  @override
  bool operator ==(Object other) {
    return other is SegmentWritersProvider &&
        other.entryId == entryId &&
        other.segmentId == segmentId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);
    hash = _SystemHash.combine(hash, segmentId.hashCode);

    return _SystemHash.finish(hash);
  }
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member
