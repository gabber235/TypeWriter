import "dart:io";

/// Whether the Flutter test runner marked this process as a test process.
bool get isFlutterTest => Platform.environment["FLUTTER_TEST"] == "true";
