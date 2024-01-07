// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sounds.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$SoundDataImpl _$$SoundDataImplFromJson(Map<String, dynamic> json) =>
    _$SoundDataImpl(
      name: json['name'] as String? ?? "",
      weight: json['weight'] as int? ?? 1,
      volume: (json['volume'] as num?)?.toDouble() ?? 1,
    );

Map<String, dynamic> _$$SoundDataImplToJson(_$SoundDataImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'weight': instance.weight,
      'volume': instance.volume,
    };

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$minecraftSoundsHash() => r'057ac7e0ea33c9ed26f63e57a67091721811fd50';

/// See also [minecraftSounds].
@ProviderFor(minecraftSounds)
final minecraftSoundsProvider =
    FutureProvider<Map<String, List<SoundData>>>.internal(
  minecraftSounds,
  name: r'minecraftSoundsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$minecraftSoundsHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef MinecraftSoundsRef = FutureProviderRef<Map<String, List<SoundData>>>;
String _$minecraftSoundHash() => r'3a6806c1a0d534e49af8e067f895dfa0912868ee';

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

/// See also [minecraftSound].
@ProviderFor(minecraftSound)
const minecraftSoundProvider = MinecraftSoundFamily();

/// See also [minecraftSound].
class MinecraftSoundFamily extends Family<AsyncValue<MinecraftSound?>> {
  /// See also [minecraftSound].
  const MinecraftSoundFamily();

  /// See also [minecraftSound].
  MinecraftSoundProvider call(
    String id,
  ) {
    return MinecraftSoundProvider(
      id,
    );
  }

  @override
  MinecraftSoundProvider getProviderOverride(
    covariant MinecraftSoundProvider provider,
  ) {
    return call(
      provider.id,
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
  String? get name => r'minecraftSoundProvider';
}

/// See also [minecraftSound].
class MinecraftSoundProvider
    extends AutoDisposeFutureProvider<MinecraftSound?> {
  /// See also [minecraftSound].
  MinecraftSoundProvider(
    String id,
  ) : this._internal(
          (ref) => minecraftSound(
            ref as MinecraftSoundRef,
            id,
          ),
          from: minecraftSoundProvider,
          name: r'minecraftSoundProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$minecraftSoundHash,
          dependencies: MinecraftSoundFamily._dependencies,
          allTransitiveDependencies:
              MinecraftSoundFamily._allTransitiveDependencies,
          id: id,
        );

  MinecraftSoundProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.id,
  }) : super.internal();

  final String id;

  @override
  Override overrideWith(
    FutureOr<MinecraftSound?> Function(MinecraftSoundRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: MinecraftSoundProvider._internal(
        (ref) => create(ref as MinecraftSoundRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        id: id,
      ),
    );
  }

  @override
  AutoDisposeFutureProviderElement<MinecraftSound?> createElement() {
    return _MinecraftSoundProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is MinecraftSoundProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin MinecraftSoundRef on AutoDisposeFutureProviderRef<MinecraftSound?> {
  /// The parameter `id` of this provider.
  String get id;
}

class _MinecraftSoundProviderElement
    extends AutoDisposeFutureProviderElement<MinecraftSound?>
    with MinecraftSoundRef {
  _MinecraftSoundProviderElement(super.provider);

  @override
  String get id => (origin as MinecraftSoundProvider).id;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
