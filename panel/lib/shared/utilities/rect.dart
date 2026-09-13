import "package:flutter/material.dart";

/// Geometric helpers for Flutter rectangles.
extension RectExtension on Rect {
  /// Returns the rectangle's width multiplied by its height.
  double get area => width * height;
}
