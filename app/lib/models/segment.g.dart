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
    String entryId,
    String segmentId,
  ) : this._internal(
          (ref) => segmentWriters(
            ref as SegmentWritersRef,
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
          entryId: entryId,
          segmentId: segmentId,
        );

  SegmentWritersProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
    required this.segmentId,
  }) : super.internal();

  final String entryId;
  final String segmentId;

  @override
  Override overrideWith(
    List<Writer> Function(SegmentWritersRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: SegmentWritersProvider._internal(
        (ref) => create(ref as SegmentWritersRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
        segmentId: segmentId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<Writer>> createElement() {
    return _SegmentWritersProviderElement(this);
  }

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

mixin SegmentWritersRef on AutoDisposeProviderRef<List<Writer>> {
  /// The parameter `entryId` of this provider.
  String get entryId;

  /// The parameter `segmentId` of this provider.
  String get segmentId;
}

class _SegmentWritersProviderElement
    extends AutoDisposeProviderElement<List<Writer>> with SegmentWritersRef {
  _SegmentWritersProviderElement(super.provider);

  @override
  String get entryId => (origin as SegmentWritersProvider).entryId;
  @override
  String get segmentId => (origin as SegmentWritersProvider).segmentId;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
