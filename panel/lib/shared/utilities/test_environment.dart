/// Platform neutral access to whether Flutter's test binding is active.
///
/// The IO implementation reads the process environment. Web builds use the
/// stub because `dart:io` is unavailable there.
library;

export "package:typewriter_panel/shared/utilities/test_environment_stub.dart"
    if (dart.library.io) "package:typewriter_panel/shared/utilities/test_environment_io.dart";
