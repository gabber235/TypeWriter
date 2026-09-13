import "package:flutter/material.dart";

/// Geometric helpers for Flutter sizes.
extension SizeExtension on Size {
  /// Returns the size's width multiplied by its height.
  double get area => width * height;
}
