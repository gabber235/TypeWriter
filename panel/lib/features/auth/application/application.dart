/// Authentication state and normalized identity claims for the panel.
///
/// The application owner is [Auth]. UI and transport code consume the derived
/// providers exported here instead of managing OIDC sessions independently.
library;

export "auth.dart";
