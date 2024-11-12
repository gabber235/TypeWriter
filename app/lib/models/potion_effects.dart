import "dart:convert";

import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:http/http.dart" as http;
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/main.dart";

part "potion_effects.g.dart";

const _minecraftRegistriesUrl = "$mcmetaUrl/summary/registries/data.min.json";

@riverpod
Future<List<String>> potionEffects(Ref ref) async {
  final response =
      await http.get(Uri.parse(_minecraftRegistriesUrl)).timeout(5.seconds);
  if (response.statusCode != 200) {
    throw Exception("Failed to load sounds");
  }
  return _mapData(jsonDecode(response.body));
}

List<String> _mapData(Map<String, dynamic> json) {
  final effects = json["mob_effect"];
  if (effects is! List) {
    throw const FormatException("Invalid data format");
  }
  return effects.map((effect) => effect as String).toList();
}

enum PotionEffectCategory {
  beneficial("Beneficial", Colors.green),
  neutral("Neutral", Colors.blue),
  harmful("Harmful", Colors.red),
  ;

  const PotionEffectCategory(this.name, this.color);

  final String name;
  final Color color;

  static PotionEffectCategory fromPotionEffect(String effect) {
    switch (effect) {
      case "absorption":
      case "conduit_power":
      case "dolphins_grace":
      case "fire_resistance":
      case "haste":
      case "health_boost":
      case "hero_of_the_village":
      case "instant_health":
      case "invisibility":
      case "jump_boost":
      case "luck":
      case "night_vision":
      case "regeneration":
      case "resistance":
      case "saturation":
      case "speed":
      case "strength":
      case "water_breathing":
        return beneficial;

      case "bad_omen":
      case "blindness":
      case "darkness":
      case "hunger":
      case "instant_damage":
      case "mining_fatigue":
      case "nausea":
      case "poison":
      case "slowness":
      case "unluck":
      case "weakness":
      case "wither":
        return harmful;

      case "glowing":
      case "levitation":
      case "slow_falling":
      default:
        return neutral;
    }
  }
}
