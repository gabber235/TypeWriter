import "dart:math";

import "package:uuid/uuid.dart";

/// Shared UUID generator for identifiers created by panel operations.
///
/// It has no application state and is exposed here so operation owners use one
/// consistent UUID implementation.
const uuid = Uuid();

/// Process local pseudorandom source for non security sensitive UI utilities.
///
/// Do not use this source for identifiers, authentication, or other secrets.
final random = Random();
