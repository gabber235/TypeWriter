import "dart:math";

import "package:flutter/material.dart";
import "package:flutter/services.dart";

extension BuildContextExtension on BuildContext {
  bool get isDark => Theme.of(this).brightness == Brightness.dark;
}

extension StringExtension on String {
  String get capitalize {
    if (isEmpty) return this;
    return "${this[0].toUpperCase()}${substring(1)}";
  }

  String get formatted {
    if (isEmpty) return this;
    return split(".")
        .map((e) => e.capitalize)
        .join(" | ")
        .split("_")
        .map((e) => e.capitalize)
        .join(" ");
  }

  String get singular {
    if (isEmpty) return this;
    if (!endsWith("s")) return this;
    return substring(0, length - 1);
  }

  String get plural {
    if (isEmpty) return this;
    if (endsWith("s")) return this;
    return "${this}s";
  }

  String get incrementedName {
    if (isEmpty) return "1";
    final number = asInt;
    if (number != null) return "${number + 1}";

    if (contains("_")) {
      final parts = split("_");
      final last = parts.removeLast();
      final number = last.asInt;
      if (number != null) return "${parts.join("_")}_${number + 1}";
    }
    return "${this}_2";
  }

  int? get asInt => int.tryParse(this);

  String replacePrefix(Pattern prefix, String replacement) {
    if (startsWith(prefix)) {
      return replacement + substring(prefix.toString().length);
    }
    return this;
  }

  String replaceSuffix(String suffix, String replacement) {
    if (endsWith(suffix)) {
      return substring(0, length - suffix.length) + replacement;
    }
    return this;
  }

  /// Returns a new string with all indexes replaced with wild cards
  /// Example: "some.1.test2.5" => "some.*.test2.*"
  String wild() {
    final pattern = RegExp(r"(\.\d+\.?)");
    final newPath = replaceAllMapped(pattern, (match) {
      if (match.group(1)?.endsWith(".") ?? false) return ".*.";
      return ".*";
    });
    return newPath;
  }

  /// If the string is empty, returns null
  /// Otherwise returns the string
  String? get nullIfEmpty => isEmpty ? null : this;
}

extension StringExt on String? {
  bool get isNullOrEmpty => this?.isEmpty ?? true;
  bool get hasValue => !isNullOrEmpty;
}

extension IntExt on int {
  String get ordinal {
    if (this == 1) return "1st";
    if (this == 2) return "2nd";
    if (this == 3) return "3rd";
    return "${this}th";
  }

  String pluralize(String singular, [String? plural]) =>
      this == 1 ? singular : plural ?? singular.plural;
}

extension ObjectExtension on Object? {
  T? cast<T>() => this is T ? this as T : null;
}

extension ListExtensions<T> on List<T> {
  List<int> get indices => List.generate(length, (index) => index);

  List<T> difference(List<T> other) {
    return where((element) => !other.contains(element)).toList()
      ..addAll(other.where((element) => !contains(element)).toSet());
  }

  bool containsAny(Iterable<T> elements) {
    for (final element in elements) {
      if (contains(element)) return true;
    }
    return false;
  }
}

TextInputFormatter snakeCaseFormatter() => TextInputFormatter.withFunction(
      (oldValue, newValue) => newValue.copyWith(
        text: newValue.text
            .toLowerCase()
            .replaceAll(" ", "_")
            .replaceAll("-", "_"),
      ),
    );

extension RandomColor on String {
  Color get randomColor {
    final random = Random(hashCode);

    // Let the hue range from 0 to 360 degrees, with a fixed saturation and value.
    final hue = random.nextInt(360);
    final saturation = 0.5 + random.nextDouble() * 0.25;
    final value = 0.8 + random.nextDouble() * 0.2;

    final hsv = HSVColor.fromAHSV(1.0, hue.toDouble(), saturation, value);
    return hsv.toColor();
  }
}

extension IteratorExt<E> on Iterator<E> {
  E? get nextOrNull => moveNext() ? current : null;
}

extension EntriesIterable<K, V> on Iterable<MapEntry<K, V>> {
  Map<K, V> toMap() {
    return Map<K, V>.fromEntries(this);
  }
}

extension ListExt<E> on List<E> {
  List<E> joinWith(E Function() separator) {
    final result = <E>[];
    for (var i = 0; i < length; i++) {
      result.add(this[i]);
      if (i < length - 1) result.add(separator());
    }
    return result;
  }
}

const _chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
final Random _random = Random();

String getRandomString([int length = 15]) => String.fromCharCodes(
      Iterable.generate(
        length,
        (_) => _chars.codeUnitAt(_random.nextInt(_chars.length)),
      ),
    );
