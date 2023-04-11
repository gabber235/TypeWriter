// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'cinematic_view.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$cinematicEntryIdsHash() => r'a2cd61ec3d1a8d66e273283ed93bfcf3ed2b3241';

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
String _$segmentPathsHash() => r'4456836fe10ea7d1100810e16b819ad4e2f0339c';

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

typedef _SegmentPathsRef = AutoDisposeProviderRef<Map<String, Modifier>>;

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
    this.entryId,
  ) : super.internal(
          (ref) => _segmentPaths(
            ref,
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
        );

  final String entryId;

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

String _$segmentsHash() => r'440d101120d3a1b897dec0821320d4a06dd3ea64';
typedef _SegmentsRef = AutoDisposeProviderRef<List<Segment>>;

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
    this.entryId,
    this.path,
  ) : super.internal(
          (ref) => _segments(
            ref,
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
        );

  final String entryId;
  final String path;

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

String _$allSegmentsHash() => r'00ddff554f166898f9bb911168a5c18e369887ef';
typedef _AllSegmentsRef = AutoDisposeProviderRef<List<Segment>>;

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
    this.entryId,
  ) : super.internal(
          (ref) => _allSegments(
            ref,
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
        );

  final String entryId;

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

String _$endingFrameHash() => r'b1ee54645a8ea133a3e35488b84aeb79b9701544';
typedef _EndingFrameRef = AutoDisposeProviderRef<int>;

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
    this.entryId,
  ) : super.internal(
          (ref) => _endingFrame(
            ref,
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
        );

  final String entryId;

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
String _$longestEntryNameHash() => r'578f68acdba591e2dd1f520439a646d955714ad1';

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
String _$timeFractionFramesHash() =>
    r'8015741ea21a16f837d627eefe543fbc6236db0f';
typedef _TimeFractionFramesRef = AutoDisposeProviderRef<List<int>>;

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
    this.fractionModifier = 1.0,
  }) : super.internal(
          (ref) => _timeFractionFrames(
            ref,
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
        );

  final double fractionModifier;

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

String _$timePointOffsetHash() => r'7498c13cafb7517b374493ac38ab8e5e3a3441d0';
typedef _TimePointOffsetRef = AutoDisposeProviderRef<double>;

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
    this.frame,
    this.widgetWidth,
  ) : super.internal(
          (ref) => _timePointOffset(
            ref,
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
        );

  final int frame;
  final double widgetWidth;

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
String _$frameStartOffsetHash() => r'13381822c602dddec85110a9b1c3855a47c07172';
typedef _FrameStartOffsetRef = AutoDisposeProviderRef<double>;

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
    this.frame,
  ) : super.internal(
          (ref) => _frameStartOffset(
            ref,
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
        );

  final int frame;

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

String _$frameEndOffsetHash() => r'23292b810d25146945a92761725780a6c01c5616';
typedef _FrameEndOffsetRef = AutoDisposeProviderRef<double>;

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
    this.frame,
  ) : super.internal(
          (ref) => _frameEndOffset(
            ref,
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
        );

  final int frame;

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

String _$showThumbsHash() => r'54d29c90b44317760879cbb63656a53fd73cced4';
typedef _ShowThumbsRef = AutoDisposeProviderRef<bool>;

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
    this.startFrame,
    this.endFrame,
  ) : super.internal(
          (ref) => _showThumbs(
            ref,
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
        );

  final int startFrame;
  final int endFrame;

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
String _$entryContextActionsHash() =>
    r'28cdf01b3a0ee4838ebb303ef475c14c090b88c1';
typedef _EntryContextActionsRef = AutoDisposeProviderRef<List<ContextMenuTile>>;

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
    this.entryId,
  ) : super.internal(
          (ref) => _entryContextActions(
            ref,
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
        );

  final String entryId;

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
// ignore_for_file: unnecessary_raw_strings, subtype_of_sealed_class, invalid_use_of_internal_member, do_not_use_environment, prefer_const_constructors, public_member_api_docs, avoid_private_typedef_functions
