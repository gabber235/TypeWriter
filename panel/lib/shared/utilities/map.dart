import "package:typewriter_panel/typewriter_panel.dart";

extension MapX on Map<dynamic, dynamic> {
  /// Merges this map with [other] using "mask" semantics.
  ///
  /// Keys from both maps are retained. Nested maps and lists are merged
  /// recursively. When both values have the same runtime type, [other] wins.
  /// A non null value wins over null. Incompatible values retain the value
  /// from this map. Keys present in only one map are retained unchanged.
  ///
  /// Use this for partial configuration or server patches where omitted values
  /// must preserve existing defaults. List masking is positional, and the
  /// right hand list controls how many positions receive overrides. Passing
  /// null on the right does not delete data.
  ///
  /// Example:
  /// ```dart
  /// final base = {"a": 1, "b": 2};
  /// final patch = {"b": 3, "c": 4};
  /// final merged = base.mask(patch);
  /// // {"a": 1, "b": 3, "c": 4}
  /// ```
  Map<dynamic, dynamic> mask(Map<dynamic, dynamic> other) {
    final result = <dynamic, dynamic>{};
    final keys = [...this.keys, ...other.keys];
    for (final key in keys) {
      if (containsKey(key) && other.containsKey(key)) {
        result[key] = maskObjects(this[key], other[key]);
      } else if (containsKey(key)) {
        result[key] = this[key];
      } else {
        result[key] = other[key];
      }
    }
    return result;
  }
}

/// Applies mask semantics to two dynamic values.
///
/// Nested maps and lists are merged recursively. When types conflict, the
/// base value [a] is retained.
dynamic maskObjects(dynamic a, dynamic b) {
  if (a is List && b is List) {
    return a.mask(b);
  }
  if (a is Map && b is Map) {
    return a.mask(b);
  }
  if (a.runtimeType == b.runtimeType) {
    return b;
  }
  if (a == null && b != null) {
    return b;
  }
  if (a != null && b == null) {
    return a;
  }

  // If the types are not compatible, then the base is the correct type.
  return a;
}

/// Converts map keys to strings when [value] is a map.
///
/// Non map input produces an empty map. Existing string keyed maps are returned
/// unchanged; other maps are copied with each key converted using `toString`.
Map<String, dynamic> stringMap(dynamic value) {
  if (value is Map<String, dynamic>) {
    return value;
  }
  if (value is Map) {
    return value.map((key, value) => MapEntry(key.toString(), value));
  }
  return {};
}
