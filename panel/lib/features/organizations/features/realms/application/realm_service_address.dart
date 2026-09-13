import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;

/// Derives the request and event subjects for one organization realm pair.
///
/// Subject identity includes both identifiers so messages from different
/// realms or organizations cannot share an application route accidentally.
/// This value only maps addresses. It does not open connections, subscribe,
/// or own transport lifetime.
final class RealmServiceAddress {
  const RealmServiceAddress({
    required this.organizationId,
    required this.realmId,
  });

  final skir.RecordId organizationId;
  final skir.RecordId realmId;

  String request(String operation) =>
      "service.to.${realmId.id}.organization.${organizationId.id}.realm.$operation";

  String event(String operation) =>
      "service.from.${realmId.id}.organization.${organizationId.id}.realm.$operation";
}
