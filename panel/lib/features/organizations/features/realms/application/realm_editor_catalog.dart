// The realm editor catalog is the versioned capability boundary between the
// panel and a running realm. It carries the authoritative type catalog plus
// the presentations, conversions, capabilities, discovered elements, and page
// editors needed to construct local editors. Fetches are paired with an
// invalidation watch so consumers can replace stale definitions after a realm
// reload without owning transport details.
import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";

part "realm_editor_catalog.freezed.dart";

/// Identifies one realm catalog endpoint and derives its request subjects.
///
/// Keeping subject derivation here prevents providers and transports from
/// constructing operation names independently. Organization and realm
/// identifiers stay typed until this transport boundary. Fetch and
/// invalidation subjects are separate because fetch is request and response
/// traffic, while invalidation is a long lived watch. The route owns no
/// connection or subscription.
@freezed
abstract class RealmEditorCatalogRoute with _$RealmEditorCatalogRoute {
  const factory RealmEditorCatalogRoute({
    required skir.RecordId organizationId,
    required skir.RecordId realmId,
  }) = _RealmEditorCatalogRoute;

  const RealmEditorCatalogRoute._();

  RealmServiceAddress get address => RealmServiceAddress(
    organizationId: this.organizationId,
    realmId: this.realmId,
  );

  String get fetchSubject => address.request("editor.catalog.fetch");

  String get invalidationRequestSubject =>
      address.request("editor.catalog.invalidate");

  String get invalidationSubject => address.event("editor.catalog.invalidate");
}

/// One internally consistent catalog generation used by editor construction.
///
/// The realm service is authoritative. Consumers may use this snapshot until
/// an invalidation changes [generation]. That generation is sent with later
/// capability calls. Diagnostics describe rejected or undecodable entries and
/// are retained so valid definitions can still be rendered.
@freezed
abstract class RealmEditorCatalogSnapshot with _$RealmEditorCatalogSnapshot {
  const factory RealmEditorCatalogSnapshot({
    required TypeCatalog catalog,
    required CatalogGeneration generation,
    @Default({}) Map<PresentationId, PresentationDefinition> presentations,
    @Default({}) Map<ConversionId, ConversionDefinition> conversions,
    @Default({}) Map<CapabilityId, CapabilityDefinition> capabilities,
    @Default({}) Map<String, RealmEditorSubtypeResult> subtypeResults,
    @Default([]) List<TypeDiagnostic> diagnostics,
    @Default({}) Map<String, RealmElementCatalogEntry> elements,
    @Default(RealmPageCatalog()) RealmPageCatalog pageCatalog,
  }) = _RealmEditorCatalogSnapshot;
}

/// Outcome of fetching the catalog requested by a consumer.
///
/// A generation mismatch is a coordination result, not transport failure. The
/// cache retries against [currentGeneration]. Unavailable diagnostics are
/// terminal for that fetch and preserve any previous snapshot at the cache.
@freezed
sealed class RealmEditorCatalogFetchResult
    with _$RealmEditorCatalogFetchResult {
  const factory RealmEditorCatalogFetchResult.fetched(
    RealmEditorCatalogSnapshot snapshot,
  ) = RealmEditorCatalogFetched;
  const factory RealmEditorCatalogFetchResult.generationMismatch(
    CatalogGeneration currentGeneration,
  ) = RealmEditorCatalogGenerationMismatch;
  const factory RealmEditorCatalogFetchResult.unavailable(
    List<TypeDiagnostic> diagnostics,
  ) = RealmEditorCatalogFetchUnavailable;
}

/// Notification from the realm that the catalog generation changed or watching
/// became impossible. An invalidation carries the generation to request next;
/// unavailability ends the useful watch state and requires a later provider
/// refresh. The cache surfaces the failure without discarding its prior
/// snapshot.
@freezed
sealed class RealmEditorCatalogWatchEvent with _$RealmEditorCatalogWatchEvent {
  const factory RealmEditorCatalogWatchEvent.invalidated(
    CatalogGeneration generation,
  ) = RealmEditorCatalogInvalidated;
  const factory RealmEditorCatalogWatchEvent.unavailable(
    List<TypeDiagnostic> diagnostics,
  ) = RealmEditorCatalogWatchUnavailable;
}

/// Boundary for fetching catalog projections and receiving invalidations.
///
/// Implementations own protocol encoding and decoding. The cache owns merged
/// consumer demand, state publication, generation retry, stale response
/// suppression, and subscription lifetime. Callers should use
/// [RealmEditorCatalogCache] unless they need another source implementation.
abstract interface class RealmEditorCatalogSource {
  Future<RealmEditorCatalogFetchResult> fetch(
    RealmEditorCatalogRoute route,
    RealmEditorCatalogRequest request, {
    CatalogGeneration? expectedGeneration,
  });

  Stream<RealmEditorCatalogWatchEvent> watchInvalidations(
    RealmEditorCatalogRoute route,
  );
}

/// Catalog source used when the realm transport cannot be constructed.
///
/// It returns a diagnostic fetch result and a diagnostic invalidation event so
/// consumers render an unavailable catalog instead of treating missing transport
/// as an empty, valid catalog.
final class UnavailableRealmEditorCatalogSource
    implements RealmEditorCatalogSource {
  const UnavailableRealmEditorCatalogSource();

  @override
  Future<RealmEditorCatalogFetchResult> fetch(
    RealmEditorCatalogRoute route,
    RealmEditorCatalogRequest request, {
    CatalogGeneration? expectedGeneration,
  }) async =>
      RealmEditorCatalogFetchResult.unavailable([_unavailableDiagnostic()]);

  @override
  Stream<RealmEditorCatalogWatchEvent> watchInvalidations(
    RealmEditorCatalogRoute route,
  ) => Stream.value(
    RealmEditorCatalogWatchEvent.unavailable([_unavailableDiagnostic()]),
  );
}

/// Converts source and decoding failures into diagnostics understood by editor
/// consumers without exposing transport exceptions across the catalog boundary.
TypeDiagnostic realmEditorCatalogUnavailableDiagnostic(String message) =>
    TypeDiagnostic(
      code: TypeDiagnosticCode.invalidPresentation,
      message: message,
      pathPresent: false,
    );

TypeDiagnostic _unavailableDiagnostic() =>
    realmEditorCatalogUnavailableDiagnostic(
      "Realm editor catalog transport is unavailable",
    );
