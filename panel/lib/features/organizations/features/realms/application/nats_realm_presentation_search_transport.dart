import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/editor/v1/search.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

/// Transports one realm presentation search stream over NATS.
///
/// The editor search source owns query evaluation and result semantics. This
/// adapter encodes the request, decodes each realm update, and cancels the
/// server subscription when the consumer stops listening, including on stream
/// failure. Encoding failures become an unavailable update so callers receive
/// a typed search outcome rather than a partially sent request.
final class NatsRealmPresentationSearchTransport {
  const NatsRealmPresentationSearchTransport({
    required this.ref,
    required this.organizationId,
    required this.realmId,
    required this.registry,
  });

  final Ref ref;
  final skir.RecordId organizationId;
  final skir.RecordId realmId;
  final TypeRegistry registry;

  RealmServiceAddress get _address =>
      RealmServiceAddress(organizationId: organizationId, realmId: realmId);

  String get _requestSubject => _address.request("editor.presentation.search");

  String get _updateSubject => _address.event("editor.presentation.search");

  String get _cancelSubject =>
      _address.request("editor.presentation.search.cancel");

  /// Starts a correlated search subscription and returns its decoded updates.
  ///
  /// The request subscription identifier is also sent to cancellation, which
  /// makes cancellation scoped to this stream rather than the realm generally.
  Stream<RealmPresentationSearchUpdate> watch(
    RealmPresentationSearchRequest request,
  ) async* {
    final codec = SkirRealmPresentationSearchCodec(SkirEditorCodec(registry));
    final encoded = codec.encodeRequest(request);
    if (encoded case TypeFailure(:final diagnostics)) {
      yield RealmPresentationSearchUpdate.unavailable(
        subscriptionId: request.subscriptionId,
        diagnostics: diagnostics,
      );
      return;
    }
    try {
      yield* ref.watchRequest(
        subject: _requestSubject,
        listenSubject: _updateSubject,
        requestBytes: wire.RealmPresentationSearchRequest.serializer.toBytes(
          encoded.valueOrNull!,
        ),
        serializer: wire.RealmPresentationSearchUpdate.serializer,
        transformer: (_, update) => codec.decodeUpdate(update),
      );
    } finally {
      await ref.requestSkir(
        _cancelSubject,
        wire.CancelRealmPresentationSearchRequest.serializer.toBytes(
          wire.CancelRealmPresentationSearchRequest(
            subscriptionId: request.subscriptionId,
          ),
        ),
        wire.CancelRealmPresentationSearchResult.serializer,
      );
    }
  }
}
