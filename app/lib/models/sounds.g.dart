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

String _$minecraftSoundsHash() => r'057ac7e0ea33c9ed26f63e57a67091721811fd50';

/// See also [minecraftSounds].
final minecraftSoundsProvider = FutureProvider<Map<String, List<SoundData>>>(
  minecraftSounds,
  name: r'minecraftSoundsProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$minecraftSoundsHash,
);
typedef MinecraftSoundsRef = FutureProviderRef<Map<String, List<SoundData>>>;
String _$minecraftSoundHash() => r'3a6806c1a0d534e49af8e067f895dfa0912868ee';

/// See also [minecraftSound].
class MinecraftSoundProvider
    extends AutoDisposeFutureProvider<MapEntry<String, List<SoundData>>?> {
  MinecraftSoundProvider(
    this.id,
  ) : super(
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

typedef MinecraftSoundRef
    = AutoDisposeFutureProviderRef<MapEntry<String, List<SoundData>>?>;

/// See also [minecraftSound].
final minecraftSoundProvider = MinecraftSoundFamily();

class MinecraftSoundFamily
    extends Family<AsyncValue<MapEntry<String, List<SoundData>>?>> {
  MinecraftSoundFamily();

  MinecraftSoundProvider call(
    String id,
  ) {
    return MinecraftSoundProvider(
      id,
    );
  }

  @override
  AutoDisposeFutureProvider<MapEntry<String, List<SoundData>>?>
      getProviderOverride(
    covariant MinecraftSoundProvider provider,
  ) {
    return call(
      provider.id,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'minecraftSoundProvider';
}
