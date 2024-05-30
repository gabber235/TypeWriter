// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'cinematic_view.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$inspectingSegmentHash() => r'd7155fa87adad4c7e894d76ec98a91c9096f7632';

/// See also [inspectingSegment].
@ProviderFor(inspectingSegment)
final inspectingSegmentProvider = AutoDisposeProvider<Segment?>.internal(
  inspectingSegment,
  name: r'inspectingSegmentProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$inspectingSegmentHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef InspectingSegmentRef = AutoDisposeProviderRef<Segment?>;
String _$allSegmentsHash() => r'00ddff554f166898f9bb911168a5c18e369887ef';

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

/// See also [_allSegments].
@ProviderFor(_allSegments)
const _allSegmentsProvider = _AllSegmentsFamily();

/// See also [_allSegments].
class _AllSegmentsFamily extends Family<List<Segment>> {
  /// See also [_allSegments].
  const _AllSegmentsFamily();

  /// See also [_allSegments].
  _AllSegmentsProvider call(
    String entryId,
  ) {
    return _AllSegmentsProvider(
      entryId,
    );
  }

  @override
  _AllSegmentsProvider getProviderOverride(
    covariant _AllSegmentsProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'_allSegmentsProvider';
}

/// See also [_allSegments].
class _AllSegmentsProvider extends AutoDisposeProvider<List<Segment>> {
  /// See also [_allSegments].
  _AllSegmentsProvider(
    String entryId,
  ) : this._internal(
          (ref) => _allSegments(
            ref as _AllSegmentsRef,
            entryId,
          ),
          from: _allSegmentsProvider,
          name: r'_allSegmentsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$allSegmentsHash,
          dependencies: _AllSegmentsFamily._dependencies,
          allTransitiveDependencies:
              _AllSegmentsFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  _AllSegmentsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    List<Segment> Function(_AllSegmentsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _AllSegmentsProvider._internal(
        (ref) => create(ref as _AllSegmentsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<Segment>> createElement() {
    return _AllSegmentsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _AllSegmentsProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _AllSegmentsRef on AutoDisposeProviderRef<List<Segment>> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _AllSegmentsProviderElement
    extends AutoDisposeProviderElement<List<Segment>> with _AllSegmentsRef {
  _AllSegmentsProviderElement(super.provider);

  @override
  String get entryId => (origin as _AllSegmentsProvider).entryId;
}

String _$cinematicEntryIdsHash() => r'd605052793e8cd783fa8fbb6ed8ea754ccc60023';

/// See also [_cinematicEntryIds].
@ProviderFor(_cinematicEntryIds)
final _cinematicEntryIdsProvider = AutoDisposeProvider<List<String>>.internal(
  _cinematicEntryIds,
  name: r'_cinematicEntryIdsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$cinematicEntryIdsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _CinematicEntryIdsRef = AutoDisposeProviderRef<List<String>>;
String _$endingFrameHash() => r'b1ee54645a8ea133a3e35488b84aeb79b9701544';

/// See also [_endingFrame].
@ProviderFor(_endingFrame)
const _endingFrameProvider = _EndingFrameFamily();

/// See also [_endingFrame].
class _EndingFrameFamily extends Family<int> {
  /// See also [_endingFrame].
  const _EndingFrameFamily();

  /// See also [_endingFrame].
  _EndingFrameProvider call(
    String entryId,
  ) {
    return _EndingFrameProvider(
      entryId,
    );
  }

  @override
  _EndingFrameProvider getProviderOverride(
    covariant _EndingFrameProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'_endingFrameProvider';
}

/// See also [_endingFrame].
class _EndingFrameProvider extends AutoDisposeProvider<int> {
  /// See also [_endingFrame].
  _EndingFrameProvider(
    String entryId,
  ) : this._internal(
          (ref) => _endingFrame(
            ref as _EndingFrameRef,
            entryId,
          ),
          from: _endingFrameProvider,
          name: r'_endingFrameProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$endingFrameHash,
          dependencies: _EndingFrameFamily._dependencies,
          allTransitiveDependencies:
              _EndingFrameFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  _EndingFrameProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    int Function(_EndingFrameRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _EndingFrameProvider._internal(
        (ref) => create(ref as _EndingFrameRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<int> createElement() {
    return _EndingFrameProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _EndingFrameProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _EndingFrameRef on AutoDisposeProviderRef<int> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EndingFrameProviderElement extends AutoDisposeProviderElement<int>
    with _EndingFrameRef {
  _EndingFrameProviderElement(super.provider);

  @override
  String get entryId => (origin as _EndingFrameProvider).entryId;
}

String _$entryContextActionsHash() =>
    r'328ae81dcb26da060ea5514c1336cb20903dd0da';

/// See also [_entryContextActions].
@ProviderFor(_entryContextActions)
const _entryContextActionsProvider = _EntryContextActionsFamily();

/// See also [_entryContextActions].
class _EntryContextActionsFamily extends Family<List<ContextMenuTile>> {
  /// See also [_entryContextActions].
  const _EntryContextActionsFamily();

  /// See also [_entryContextActions].
  _EntryContextActionsProvider call(
    String entryId,
  ) {
    return _EntryContextActionsProvider(
      entryId,
    );
  }

  @override
  _EntryContextActionsProvider getProviderOverride(
    covariant _EntryContextActionsProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'_entryContextActionsProvider';
}

/// See also [_entryContextActions].
class _EntryContextActionsProvider
    extends AutoDisposeProvider<List<ContextMenuTile>> {
  /// See also [_entryContextActions].
  _EntryContextActionsProvider(
    String entryId,
  ) : this._internal(
          (ref) => _entryContextActions(
            ref as _EntryContextActionsRef,
            entryId,
          ),
          from: _entryContextActionsProvider,
          name: r'_entryContextActionsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryContextActionsHash,
          dependencies: _EntryContextActionsFamily._dependencies,
          allTransitiveDependencies:
              _EntryContextActionsFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  _EntryContextActionsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    List<ContextMenuTile> Function(_EntryContextActionsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _EntryContextActionsProvider._internal(
        (ref) => create(ref as _EntryContextActionsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<ContextMenuTile>> createElement() {
    return _EntryContextActionsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _EntryContextActionsProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _EntryContextActionsRef on AutoDisposeProviderRef<List<ContextMenuTile>> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryContextActionsProviderElement
    extends AutoDisposeProviderElement<List<ContextMenuTile>>
    with _EntryContextActionsRef {
  _EntryContextActionsProviderElement(super.provider);

  @override
  String get entryId => (origin as _EntryContextActionsProvider).entryId;
}

String _$frameEndOffsetHash() => r'23292b810d25146945a92761725780a6c01c5616';

/// See also [_frameEndOffset].
@ProviderFor(_frameEndOffset)
const _frameEndOffsetProvider = _FrameEndOffsetFamily();

/// See also [_frameEndOffset].
class _FrameEndOffsetFamily extends Family<double> {
  /// See also [_frameEndOffset].
  const _FrameEndOffsetFamily();

  /// See also [_frameEndOffset].
  _FrameEndOffsetProvider call(
    int frame,
  ) {
    return _FrameEndOffsetProvider(
      frame,
    );
  }

  @override
  _FrameEndOffsetProvider getProviderOverride(
    covariant _FrameEndOffsetProvider provider,
  ) {
    return call(
      provider.frame,
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
  String? get name => r'_frameEndOffsetProvider';
}

/// See also [_frameEndOffset].
class _FrameEndOffsetProvider extends AutoDisposeProvider<double> {
  /// See also [_frameEndOffset].
  _FrameEndOffsetProvider(
    int frame,
  ) : this._internal(
          (ref) => _frameEndOffset(
            ref as _FrameEndOffsetRef,
            frame,
          ),
          from: _frameEndOffsetProvider,
          name: r'_frameEndOffsetProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$frameEndOffsetHash,
          dependencies: _FrameEndOffsetFamily._dependencies,
          allTransitiveDependencies:
              _FrameEndOffsetFamily._allTransitiveDependencies,
          frame: frame,
        );

  _FrameEndOffsetProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.frame,
  }) : super.internal();

  final int frame;

  @override
  Override overrideWith(
    double Function(_FrameEndOffsetRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _FrameEndOffsetProvider._internal(
        (ref) => create(ref as _FrameEndOffsetRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        frame: frame,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<double> createElement() {
    return _FrameEndOffsetProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _FrameEndOffsetProvider && other.frame == frame;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, frame.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _FrameEndOffsetRef on AutoDisposeProviderRef<double> {
  /// The parameter `frame` of this provider.
  int get frame;
}

class _FrameEndOffsetProviderElement extends AutoDisposeProviderElement<double>
    with _FrameEndOffsetRef {
  _FrameEndOffsetProviderElement(super.provider);

  @override
  int get frame => (origin as _FrameEndOffsetProvider).frame;
}

String _$frameSpacingHash() => r'b6efbe3e7da758078921b18a6b91cdd506f86895';

/// See also [_frameSpacing].
@ProviderFor(_frameSpacing)
final _frameSpacingProvider = AutoDisposeProvider<double>.internal(
  _frameSpacing,
  name: r'_frameSpacingProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$frameSpacingHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _FrameSpacingRef = AutoDisposeProviderRef<double>;
String _$frameStartOffsetHash() => r'13381822c602dddec85110a9b1c3855a47c07172';

/// See also [_frameStartOffset].
@ProviderFor(_frameStartOffset)
const _frameStartOffsetProvider = _FrameStartOffsetFamily();

/// See also [_frameStartOffset].
class _FrameStartOffsetFamily extends Family<double> {
  /// See also [_frameStartOffset].
  const _FrameStartOffsetFamily();

  /// See also [_frameStartOffset].
  _FrameStartOffsetProvider call(
    int frame,
  ) {
    return _FrameStartOffsetProvider(
      frame,
    );
  }

  @override
  _FrameStartOffsetProvider getProviderOverride(
    covariant _FrameStartOffsetProvider provider,
  ) {
    return call(
      provider.frame,
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
  String? get name => r'_frameStartOffsetProvider';
}

/// See also [_frameStartOffset].
class _FrameStartOffsetProvider extends AutoDisposeProvider<double> {
  /// See also [_frameStartOffset].
  _FrameStartOffsetProvider(
    int frame,
  ) : this._internal(
          (ref) => _frameStartOffset(
            ref as _FrameStartOffsetRef,
            frame,
          ),
          from: _frameStartOffsetProvider,
          name: r'_frameStartOffsetProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$frameStartOffsetHash,
          dependencies: _FrameStartOffsetFamily._dependencies,
          allTransitiveDependencies:
              _FrameStartOffsetFamily._allTransitiveDependencies,
          frame: frame,
        );

  _FrameStartOffsetProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.frame,
  }) : super.internal();

  final int frame;

  @override
  Override overrideWith(
    double Function(_FrameStartOffsetRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _FrameStartOffsetProvider._internal(
        (ref) => create(ref as _FrameStartOffsetRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        frame: frame,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<double> createElement() {
    return _FrameStartOffsetProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _FrameStartOffsetProvider && other.frame == frame;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, frame.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _FrameStartOffsetRef on AutoDisposeProviderRef<double> {
  /// The parameter `frame` of this provider.
  int get frame;
}

class _FrameStartOffsetProviderElement
    extends AutoDisposeProviderElement<double> with _FrameStartOffsetRef {
  _FrameStartOffsetProviderElement(super.provider);

  @override
  int get frame => (origin as _FrameStartOffsetProvider).frame;
}

String _$ignoreEntryFieldsHash() => r'602c6ed49c2d349912b9a3d448e72be350e9d4aa';

/// See also [_ignoreEntryFields].
@ProviderFor(_ignoreEntryFields)
final _ignoreEntryFieldsProvider = AutoDisposeProvider<List<String>>.internal(
  _ignoreEntryFields,
  name: r'_ignoreEntryFieldsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$ignoreEntryFieldsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _IgnoreEntryFieldsRef = AutoDisposeProviderRef<List<String>>;
String _$longestEntryNameHash() => r'5279a4a558d828a7316e5f78a1744316d08b1d7f';

/// See also [_longestEntryName].
@ProviderFor(_longestEntryName)
final _longestEntryNameProvider = AutoDisposeProvider<String>.internal(
  _longestEntryName,
  name: r'_longestEntryNameProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$longestEntryNameHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _LongestEntryNameRef = AutoDisposeProviderRef<String>;
String _$segmentFieldsHash() => r'50de8ece747eb6fbdd3c504f30873d7546d54b50';

/// See also [_segmentFields].
@ProviderFor(_segmentFields)
final _segmentFieldsProvider = AutoDisposeProvider<ObjectField?>.internal(
  _segmentFields,
  name: r'_segmentFieldsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$segmentFieldsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _SegmentFieldsRef = AutoDisposeProviderRef<ObjectField?>;
String _$segmentPathsHash() => r'4456836fe10ea7d1100810e16b819ad4e2f0339c';

/// See also [_segmentPaths].
@ProviderFor(_segmentPaths)
const _segmentPathsProvider = _SegmentPathsFamily();

/// See also [_segmentPaths].
class _SegmentPathsFamily extends Family<Map<String, Modifier>> {
  /// See also [_segmentPaths].
  const _SegmentPathsFamily();

  /// See also [_segmentPaths].
  _SegmentPathsProvider call(
    String entryId,
  ) {
    return _SegmentPathsProvider(
      entryId,
    );
  }

  @override
  _SegmentPathsProvider getProviderOverride(
    covariant _SegmentPathsProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'_segmentPathsProvider';
}

/// See also [_segmentPaths].
class _SegmentPathsProvider extends AutoDisposeProvider<Map<String, Modifier>> {
  /// See also [_segmentPaths].
  _SegmentPathsProvider(
    String entryId,
  ) : this._internal(
          (ref) => _segmentPaths(
            ref as _SegmentPathsRef,
            entryId,
          ),
          from: _segmentPathsProvider,
          name: r'_segmentPathsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$segmentPathsHash,
          dependencies: _SegmentPathsFamily._dependencies,
          allTransitiveDependencies:
              _SegmentPathsFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  _SegmentPathsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    Map<String, Modifier> Function(_SegmentPathsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _SegmentPathsProvider._internal(
        (ref) => create(ref as _SegmentPathsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<Map<String, Modifier>> createElement() {
    return _SegmentPathsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _SegmentPathsProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _SegmentPathsRef on AutoDisposeProviderRef<Map<String, Modifier>> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _SegmentPathsProviderElement
    extends AutoDisposeProviderElement<Map<String, Modifier>>
    with _SegmentPathsRef {
  _SegmentPathsProviderElement(super.provider);

  @override
  String get entryId => (origin as _SegmentPathsProvider).entryId;
}

String _$segmentsHash() => r'f49571f7d75d0070fcbb69cef364a7cc5c5af448';

/// See also [_segments].
@ProviderFor(_segments)
const _segmentsProvider = _SegmentsFamily();

/// See also [_segments].
class _SegmentsFamily extends Family<List<Segment>> {
  /// See also [_segments].
  const _SegmentsFamily();

  /// See also [_segments].
  _SegmentsProvider call(
    String entryId,
    String path,
  ) {
    return _SegmentsProvider(
      entryId,
      path,
    );
  }

  @override
  _SegmentsProvider getProviderOverride(
    covariant _SegmentsProvider provider,
  ) {
    return call(
      provider.entryId,
      provider.path,
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
  String? get name => r'_segmentsProvider';
}

/// See also [_segments].
class _SegmentsProvider extends AutoDisposeProvider<List<Segment>> {
  /// See also [_segments].
  _SegmentsProvider(
    String entryId,
    String path,
  ) : this._internal(
          (ref) => _segments(
            ref as _SegmentsRef,
            entryId,
            path,
          ),
          from: _segmentsProvider,
          name: r'_segmentsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$segmentsHash,
          dependencies: _SegmentsFamily._dependencies,
          allTransitiveDependencies: _SegmentsFamily._allTransitiveDependencies,
          entryId: entryId,
          path: path,
        );

  _SegmentsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
    required this.path,
  }) : super.internal();

  final String entryId;
  final String path;

  @override
  Override overrideWith(
    List<Segment> Function(_SegmentsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _SegmentsProvider._internal(
        (ref) => create(ref as _SegmentsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
        path: path,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<Segment>> createElement() {
    return _SegmentsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _SegmentsProvider &&
        other.entryId == entryId &&
        other.path == path;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);
    hash = _SystemHash.combine(hash, path.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _SegmentsRef on AutoDisposeProviderRef<List<Segment>> {
  /// The parameter `entryId` of this provider.
  String get entryId;

  /// The parameter `path` of this provider.
  String get path;
}

class _SegmentsProviderElement extends AutoDisposeProviderElement<List<Segment>>
    with _SegmentsRef {
  _SegmentsProviderElement(super.provider);

  @override
  String get entryId => (origin as _SegmentsProvider).entryId;
  @override
  String get path => (origin as _SegmentsProvider).path;
}

String _$showThumbsHash() => r'54d29c90b44317760879cbb63656a53fd73cced4';

/// See also [_showThumbs].
@ProviderFor(_showThumbs)
const _showThumbsProvider = _ShowThumbsFamily();

/// See also [_showThumbs].
class _ShowThumbsFamily extends Family<bool> {
  /// See also [_showThumbs].
  const _ShowThumbsFamily();

  /// See also [_showThumbs].
  _ShowThumbsProvider call(
    int startFrame,
    int endFrame,
  ) {
    return _ShowThumbsProvider(
      startFrame,
      endFrame,
    );
  }

  @override
  _ShowThumbsProvider getProviderOverride(
    covariant _ShowThumbsProvider provider,
  ) {
    return call(
      provider.startFrame,
      provider.endFrame,
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
  String? get name => r'_showThumbsProvider';
}

/// See also [_showThumbs].
class _ShowThumbsProvider extends AutoDisposeProvider<bool> {
  /// See also [_showThumbs].
  _ShowThumbsProvider(
    int startFrame,
    int endFrame,
  ) : this._internal(
          (ref) => _showThumbs(
            ref as _ShowThumbsRef,
            startFrame,
            endFrame,
          ),
          from: _showThumbsProvider,
          name: r'_showThumbsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$showThumbsHash,
          dependencies: _ShowThumbsFamily._dependencies,
          allTransitiveDependencies:
              _ShowThumbsFamily._allTransitiveDependencies,
          startFrame: startFrame,
          endFrame: endFrame,
        );

  _ShowThumbsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.startFrame,
    required this.endFrame,
  }) : super.internal();

  final int startFrame;
  final int endFrame;

  @override
  Override overrideWith(
    bool Function(_ShowThumbsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _ShowThumbsProvider._internal(
        (ref) => create(ref as _ShowThumbsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        startFrame: startFrame,
        endFrame: endFrame,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<bool> createElement() {
    return _ShowThumbsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _ShowThumbsProvider &&
        other.startFrame == startFrame &&
        other.endFrame == endFrame;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, startFrame.hashCode);
    hash = _SystemHash.combine(hash, endFrame.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _ShowThumbsRef on AutoDisposeProviderRef<bool> {
  /// The parameter `startFrame` of this provider.
  int get startFrame;

  /// The parameter `endFrame` of this provider.
  int get endFrame;
}

class _ShowThumbsProviderElement extends AutoDisposeProviderElement<bool>
    with _ShowThumbsRef {
  _ShowThumbsProviderElement(super.provider);

  @override
  int get startFrame => (origin as _ShowThumbsProvider).startFrame;
  @override
  int get endFrame => (origin as _ShowThumbsProvider).endFrame;
}

String _$sliderEndOffsetHash() => r'8b6fcf6b5d98782be5d72e962e4b3f6cf7356d85';

/// See also [_sliderEndOffset].
@ProviderFor(_sliderEndOffset)
final _sliderEndOffsetProvider = AutoDisposeProvider<double>.internal(
  _sliderEndOffset,
  name: r'_sliderEndOffsetProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$sliderEndOffsetHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _SliderEndOffsetRef = AutoDisposeProviderRef<double>;
String _$sliderStartOffsetHash() => r'cea351bd17cd435ff70e20ce48f4b6e53b370445';

/// See also [_sliderStartOffset].
@ProviderFor(_sliderStartOffset)
final _sliderStartOffsetProvider = AutoDisposeProvider<double>.internal(
  _sliderStartOffset,
  name: r'_sliderStartOffsetProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$sliderStartOffsetHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _SliderStartOffsetRef = AutoDisposeProviderRef<double>;
String _$timeFractionFramesHash() =>
    r'8015741ea21a16f837d627eefe543fbc6236db0f';

/// See also [_timeFractionFrames].
@ProviderFor(_timeFractionFrames)
const _timeFractionFramesProvider = _TimeFractionFramesFamily();

/// See also [_timeFractionFrames].
class _TimeFractionFramesFamily extends Family<List<int>> {
  /// See also [_timeFractionFrames].
  const _TimeFractionFramesFamily();

  /// See also [_timeFractionFrames].
  _TimeFractionFramesProvider call({
    double fractionModifier = 1.0,
  }) {
    return _TimeFractionFramesProvider(
      fractionModifier: fractionModifier,
    );
  }

  @override
  _TimeFractionFramesProvider getProviderOverride(
    covariant _TimeFractionFramesProvider provider,
  ) {
    return call(
      fractionModifier: provider.fractionModifier,
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
  String? get name => r'_timeFractionFramesProvider';
}

/// See also [_timeFractionFrames].
class _TimeFractionFramesProvider extends AutoDisposeProvider<List<int>> {
  /// See also [_timeFractionFrames].
  _TimeFractionFramesProvider({
    double fractionModifier = 1.0,
  }) : this._internal(
          (ref) => _timeFractionFrames(
            ref as _TimeFractionFramesRef,
            fractionModifier: fractionModifier,
          ),
          from: _timeFractionFramesProvider,
          name: r'_timeFractionFramesProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$timeFractionFramesHash,
          dependencies: _TimeFractionFramesFamily._dependencies,
          allTransitiveDependencies:
              _TimeFractionFramesFamily._allTransitiveDependencies,
          fractionModifier: fractionModifier,
        );

  _TimeFractionFramesProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.fractionModifier,
  }) : super.internal();

  final double fractionModifier;

  @override
  Override overrideWith(
    List<int> Function(_TimeFractionFramesRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _TimeFractionFramesProvider._internal(
        (ref) => create(ref as _TimeFractionFramesRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        fractionModifier: fractionModifier,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<int>> createElement() {
    return _TimeFractionFramesProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _TimeFractionFramesProvider &&
        other.fractionModifier == fractionModifier;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, fractionModifier.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _TimeFractionFramesRef on AutoDisposeProviderRef<List<int>> {
  /// The parameter `fractionModifier` of this provider.
  double get fractionModifier;
}

class _TimeFractionFramesProviderElement
    extends AutoDisposeProviderElement<List<int>> with _TimeFractionFramesRef {
  _TimeFractionFramesProviderElement(super.provider);

  @override
  double get fractionModifier =>
      (origin as _TimeFractionFramesProvider).fractionModifier;
}

String _$timeFractionsHash() => r'd560bc95ea61d96501c85a1c74e3eb3e22538772';

/// See also [_timeFractions].
@ProviderFor(_timeFractions)
final _timeFractionsProvider = AutoDisposeProvider<int>.internal(
  _timeFractions,
  name: r'_timeFractionsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$timeFractionsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _TimeFractionsRef = AutoDisposeProviderRef<int>;
String _$timePointOffsetHash() => r'7498c13cafb7517b374493ac38ab8e5e3a3441d0';

/// See also [_timePointOffset].
@ProviderFor(_timePointOffset)
const _timePointOffsetProvider = _TimePointOffsetFamily();

/// See also [_timePointOffset].
class _TimePointOffsetFamily extends Family<double> {
  /// See also [_timePointOffset].
  const _TimePointOffsetFamily();

  /// See also [_timePointOffset].
  _TimePointOffsetProvider call(
    int frame,
    double widgetWidth,
  ) {
    return _TimePointOffsetProvider(
      frame,
      widgetWidth,
    );
  }

  @override
  _TimePointOffsetProvider getProviderOverride(
    covariant _TimePointOffsetProvider provider,
  ) {
    return call(
      provider.frame,
      provider.widgetWidth,
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
  String? get name => r'_timePointOffsetProvider';
}

/// See also [_timePointOffset].
class _TimePointOffsetProvider extends AutoDisposeProvider<double> {
  /// See also [_timePointOffset].
  _TimePointOffsetProvider(
    int frame,
    double widgetWidth,
  ) : this._internal(
          (ref) => _timePointOffset(
            ref as _TimePointOffsetRef,
            frame,
            widgetWidth,
          ),
          from: _timePointOffsetProvider,
          name: r'_timePointOffsetProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$timePointOffsetHash,
          dependencies: _TimePointOffsetFamily._dependencies,
          allTransitiveDependencies:
              _TimePointOffsetFamily._allTransitiveDependencies,
          frame: frame,
          widgetWidth: widgetWidth,
        );

  _TimePointOffsetProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.frame,
    required this.widgetWidth,
  }) : super.internal();

  final int frame;
  final double widgetWidth;

  @override
  Override overrideWith(
    double Function(_TimePointOffsetRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _TimePointOffsetProvider._internal(
        (ref) => create(ref as _TimePointOffsetRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        frame: frame,
        widgetWidth: widgetWidth,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<double> createElement() {
    return _TimePointOffsetProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _TimePointOffsetProvider &&
        other.frame == frame &&
        other.widgetWidth == widgetWidth;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, frame.hashCode);
    hash = _SystemHash.combine(hash, widgetWidth.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _TimePointOffsetRef on AutoDisposeProviderRef<double> {
  /// The parameter `frame` of this provider.
  int get frame;

  /// The parameter `widgetWidth` of this provider.
  double get widgetWidth;
}

class _TimePointOffsetProviderElement extends AutoDisposeProviderElement<double>
    with _TimePointOffsetRef {
  _TimePointOffsetProviderElement(super.provider);

  @override
  int get frame => (origin as _TimePointOffsetProvider).frame;
  @override
  double get widgetWidth => (origin as _TimePointOffsetProvider).widgetWidth;
}

String _$totalSequenceFramesHash() =>
    r'448efbdd8b67500a4a9578b7bf46f83f14456e91';

/// See also [_totalSequenceFrames].
@ProviderFor(_totalSequenceFrames)
final _totalSequenceFramesProvider = AutoDisposeProvider<int>.internal(
  _totalSequenceFrames,
  name: r'_totalSequenceFramesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$totalSequenceFramesHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _TotalSequenceFramesRef = AutoDisposeProviderRef<int>;
String _$trackBackgroundFractionModifierHash() =>
    r'e0aaee2ac472bad0bf9777a0ca67ead08a2351b3';

/// See also [_trackBackgroundFractionModifier].
@ProviderFor(_trackBackgroundFractionModifier)
final _trackBackgroundFractionModifierProvider =
    AutoDisposeProvider<double>.internal(
  _trackBackgroundFractionModifier,
  name: r'_trackBackgroundFractionModifierProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$trackBackgroundFractionModifierHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _TrackBackgroundFractionModifierRef = AutoDisposeProviderRef<double>;
String _$trackBackgroundLinesHash() =>
    r'20ac7bd3700f1f91bd9053fb793ae6382fe9b073';

/// See also [_trackBackgroundLines].
@ProviderFor(_trackBackgroundLines)
final _trackBackgroundLinesProvider =
    AutoDisposeProvider<List<_FrameLine>>.internal(
  _trackBackgroundLines,
  name: r'_trackBackgroundLinesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$trackBackgroundLinesHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _TrackBackgroundLinesRef = AutoDisposeProviderRef<List<_FrameLine>>;
String _$trackOffsetHash() => r'1f1ac2cb0fe819222d8ea488e86c896374f631a5';

/// See also [_trackOffset].
@ProviderFor(_trackOffset)
final _trackOffsetProvider = AutoDisposeProvider<double>.internal(
  _trackOffset,
  name: r'_trackOffsetProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$trackOffsetHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _TrackOffsetRef = AutoDisposeProviderRef<double>;
String _$trackSizeHash() => r'9a61b99bd973ee4a896e962cb8b9fa690e2423a9';

/// See also [_trackSize].
@ProviderFor(_trackSize)
final _trackSizeProvider = AutoDisposeProvider<double>.internal(
  _trackSize,
  name: r'_trackSizeProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$trackSizeHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef _TrackSizeRef = AutoDisposeProviderRef<double>;
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
