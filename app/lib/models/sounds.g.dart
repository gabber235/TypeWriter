// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sounds.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_SoundData _$$_SoundDataFromJson(Map<String, dynamic> json) => _$_SoundData(
      name: json['name'] as String? ?? "",
      weight: json['weight'] as int? ?? 1,
      volume: (json['volume'] as num?)?.toDouble() ?? 1,
    );

Map<String, dynamic> _$$_SoundDataToJson(_$_SoundData instance) =>
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

typedef MinecraftSoundRef
    = AutoDisposeFutureProviderRef<MapEntry<String, List<SoundData>>?>;

/// See also [minecraftSound].
@ProviderFor(minecraftSound)
const minecraftSoundProvider = MinecraftSoundFamily();

/// See also [minecraftSound].
class MinecraftSoundFamily
    extends Family<AsyncValue<MapEntry<String, List<SoundData>>?>> {
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
    extends AutoDisposeFutureProvider<MapEntry<String, List<SoundData>>?> {
  /// See also [minecraftSound].
  MinecraftSoundProvider(
    this.id,
  ) : super.internal(
          (ref) => minecraftSound(
            ref,
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
        );

  final String id;

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
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member
