import "dart:convert";
import "dart:math";

import "package:freezed_annotation/freezed_annotation.dart";
import "package:http/http.dart" as http;
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/main.dart";
import "package:typewriter/utils/extensions.dart";

part "sounds.freezed.dart";
part "sounds.g.dart";

typedef MinecraftSound = MapEntry<String, List<SoundData>>;

@freezed
class SoundData with _$SoundData {
  const factory SoundData({
    @Default("") String name,
    @Default(1) int weight,
    @Default(1) double volume,
  }) = _SoundData;

  factory SoundData.fromJson(Map<String, dynamic> json) =>
      _$SoundDataFromJson(json);
}

SoundData _parseSoundData(dynamic data) {
  if (data is String) {
    return SoundData(name: data);
  }
  if (data is Map<String, dynamic>) {
    return SoundData.fromJson(data);
  }
  throw const FormatException("Invalid data format");
}

Map<String, List<SoundData>> _mapData(Map<String, dynamic> json) {
  return json.map((key, value) {
    if (value is! Map) {
      throw const FormatException("Invalid data format");
    }
    final sounds = value["sounds"];
    if (sounds is! List) {
      throw const FormatException("Invalid data format");
    }
    return MapEntry(key, sounds.map(_parseSoundData).toList());
  });
}

const _minecraftSoundsUrl = "$mcmetaUrl/summary/sounds/data.min.json";

@Riverpod(keepAlive: true)
Future<Map<String, List<SoundData>>> minecraftSounds(
  MinecraftSoundsRef ref,
) async {
  final response = await http.get(Uri.parse(_minecraftSoundsUrl));
  if (response.statusCode != 200) {
    throw Exception("Failed to load sounds");
  }
  return _mapData(jsonDecode(response.body));
}

@riverpod
Future<MinecraftSound?> minecraftSound(MinecraftSoundRef ref, String id) async {
  final strippedId = id.replacePrefix("minecraft:", "");
  final sounds = await ref.watch(minecraftSoundsProvider.future);
  if (!sounds.containsKey(strippedId)) return null;
  final sound = sounds[strippedId];
  if (sound == null) return null;
  return MapEntry(strippedId, sound);
}

extension SoundDataX on SoundData {
  String get url => "$mcmetaUrl/assets/assets/minecraft/sounds/$name.ogg";
}

extension MinecraftSoundX on MinecraftSound {
  String get category => key.split(".").first;
  String get name => key.split(".").sublist(1).join(".");
}

final Random _random = Random();

extension ListSoundDataX on List<SoundData> {
  String pickRandomSoundUrl() {
    final totalWeight =
        fold(0, (previousValue, element) => previousValue + element.weight);
    final random = (totalWeight * _random.nextDouble()).toInt();
    var currentWeight = 0;
    for (final sound in this) {
      currentWeight += sound.weight;
      if (currentWeight >= random) {
        return sound.url;
      }
    }
    return first.url;
  }
}
