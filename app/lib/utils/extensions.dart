import 'dart:io' show Platform;

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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
    return split(".")
        .map((e) => e.capitalize)
        .join(" | ")
        .split("_")
        .map((e) => e.capitalize)
        .join(" ");
  }
}

extension ObjectExtension on Object? {
  T? cast<T>() {
    return this is T ? this as T : null;
  }
}

TextInputFormatter snakeCaseFormatter() {
  return TextInputFormatter.withFunction((oldValue, newValue) => newValue
      .copyWith(text: newValue.text.toLowerCase().replaceAll(" ", "_")));
}

bool get isApple => Platform.isIOS || Platform.isMacOS;

class SmartSingleActivator extends SingleActivator {
  SmartSingleActivator(
    super.trigger, {
    bool control = false,
    super.alt,
    super.shift,
    super.includeRepeats,
  }) : super(control: control && !isApple, meta: control && isApple);
}
