/// Trace propagation for panel requests crossing HTTP and NATS boundaries.
///
/// The exported interface keeps instrumentation optional and prevents feature
/// code from depending directly on the OpenTelemetry implementation.
library;

export "telemetry.dart";
