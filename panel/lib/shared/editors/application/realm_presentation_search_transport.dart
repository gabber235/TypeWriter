import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "realm_presentation_search_transport.freezed.dart";

/// Immutable request sent from a presentation search source to Realm.
///
/// [subscriptionId] scopes updates to this search attempt. Consumers of the
/// transport must echo it on every update, and the source discards updates for
/// older attempts. [generation] identifies the catalog and presentation
/// context that produced the payload, so the backend can reject stale work.
@freezed
abstract class RealmPresentationSearchRequest
    with _$RealmPresentationSearchRequest {
  const factory RealmPresentationSearchRequest({
    required String subscriptionId,
    required CatalogGeneration generation,
    required CapabilityId capabilityId,
    required DataValue payload,
    required TypeExpression resultType,
    required SearchQueryContext query,
  }) = _RealmPresentationSearchRequest;
}

/// Update returned by Realm for one presentation search subscription.
///
/// Snapshot updates carry renderable values and nonfatal guidance or
/// diagnostics. Unavailable updates describe a capability that cannot serve
/// the request. Both variants retain [subscriptionId] so concurrent searches
/// cannot cross their result streams.
@freezed
sealed class RealmPresentationSearchUpdate
    with _$RealmPresentationSearchUpdate {
  const factory RealmPresentationSearchUpdate.snapshot({
    required String subscriptionId,
    required SearchSourceStatus status,
    required List<DataValue> values,
    @Default([]) List<String> guidance,
    @Default([]) List<TypeDiagnostic> diagnostics,
  }) = RealmPresentationSearchSnapshotUpdate;

  const factory RealmPresentationSearchUpdate.unavailable({
    required String subscriptionId,
    required List<TypeDiagnostic> diagnostics,
  }) = RealmPresentationSearchUnavailableUpdate;
}

/// Streams Realm results for one captured presentation search request.
///
/// Implementations own transport concerns only. They must preserve update
/// identity through [RealmPresentationSearchUpdate.subscriptionId] and expose
/// unavailable capabilities as typed updates rather than throwing them into
/// the presentation source.
typedef RealmPresentationSearchTransport =
    Stream<RealmPresentationSearchUpdate> Function(
      RealmPresentationSearchRequest request,
    );
