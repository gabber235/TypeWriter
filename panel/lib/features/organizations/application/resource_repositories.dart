import "dart:async";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";

part "resource_repositories.g.dart";

@Riverpod(keepAlive: true)
ResourceRepositories resourceRepositories(Ref ref) {
  ref.watch(localWorkProvider);
  final repositories = ResourceRepositories(
    SkirMutationClient(
      () => ref.read(natsProvider),
      () => ref.read(panelTelemetryProvider.future),
    ),
    ref.read(userIdProvider.future),
    ref.watch(realmEditorCatalogSourceProvider),
  );
  ref.onDispose(repositories.dispose);
  return repositories;
}

/// Organization lifetime dependencies. Repositories retain no resource snapshots or network watches.
final class ResourceRepositories {
  ResourceRepositories(this.transport, this.userId, this.catalog);
  final SkirMutationClient transport;
  final Future<String?> userId;
  final RealmEditorCatalogSource catalog;
  final _services = <skir.RecordId, ServiceResourceRepository>{};
  final _authoring =
      <(skir.RecordId, skir.RecordId), AuthoringResourceRepository>{};
  bool _disposed = false;

  void checkActive() {
    if (_disposed) throw StateError("The resource session ended");
  }

  ServiceResourceRepository services(skir.RecordId organization) {
    checkActive();
    return _services.putIfAbsent(
      organization,
      () => ServiceResourceRepository(this, organization),
    );
  }

  AuthoringResourceRepository authoring(
    skir.RecordId organization,
    skir.RecordId realm,
  ) {
    checkActive();
    return _authoring.putIfAbsent((
      organization,
      realm,
    ), () => AuthoringResourceRepository(this, organization, realm));
  }

  void dispose() {
    _disposed = true;
    for (final repository in _services.values) {
      repository.dispose();
    }
    for (final repository in _authoring.values) {
      repository.dispose();
    }
    _services.clear();
    _authoring.clear();
  }
}
