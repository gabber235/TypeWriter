import "dart:math";

import "package:flutter/foundation.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";

extension BuildContextExtension on BuildContext {
  bool get isDark => Theme.of(this).brightness == Brightness.dark;
}

extension StringExtension on String {
  String get capitalize {
    if (isEmpty) {
      return this;
    }
    return "${this[0].toUpperCase()}${substring(1)}";
  }

  String get formatted {
    if (isEmpty) {
      return this;
    }
    return split(".").map((e) => e.capitalize).join(" | ").split("_").map((e) => e.capitalize).join(" ");
  }
}

extension ObjectExtension on Object? {
  T? cast<T>() => this is T ? this as T : null;
}

TextInputFormatter snakeCaseFormatter() => TextInputFormatter.withFunction(
      (oldValue, newValue) => newValue.copyWith(text: newValue.text.toLowerCase().replaceAll(" ", "_")),
    );

bool get isApple => defaultTargetPlatform == TargetPlatform.iOS || defaultTargetPlatform == TargetPlatform.macOS;

/// A [SingleActivator] that automatically maps the [control] key to the [meta] key for apple platforms.
class SmartSingleActivator extends SingleActivator {
  SmartSingleActivator(
    super.trigger, {
    bool control = false,
    super.alt,
    super.shift,
    super.includeRepeats,
  }) : super(control: control && !isApple, meta: control && isApple);
}

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

extension StringExt on String? {
  bool get isNullOrEmpty => this?.isEmpty ?? true;
  bool get hasValue => !isNullOrEmpty;
}
