/// Safe conversions for values crossing dynamic data boundaries.
extension ObjectExtension on Object? {
  /// Returns this value when it has type [T], otherwise returns `null`.
  T? cast<T>() => this is T ? this as T : null;
}
