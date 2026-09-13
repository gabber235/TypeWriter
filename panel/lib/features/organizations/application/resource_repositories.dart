import "dart:async";

import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";

part "resource_repositories.g.dart";

/// Provides one resource repository owner for the active local work scope.
///
/// Rebuilding the scope creates fresh transport and catalog dependencies; disposal
/// cascades to all repositories cached by that owner.
@Riverpod(keepAlive: true)
ResourceRepositories resourceRepositories(Ref ref) {
  ref.watch(localWorkScopeProvider);
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

/// Organization scoped factory and lifetime owner for resource repositories.
///
/// The provider creates one instance for the active local work scope and disposes
/// every cached child when that scope ends. Children retain transport and catalog
/// dependencies but no resource snapshots or network watches, so callers must not
/// keep them beyond this owner lifecycle.
final class ResourceRepositories {
  ResourceRepositories(this.transport, this.userId, this.catalog);
  final SkirMutationClient transport;
  final Future<String?> userId;
  final RealmEditorCatalogSource catalog;
  final _services = <skir.RecordId, ServiceResourceRepository>{};
  final _authoring =
      <(skir.RecordId, skir.RecordId), AuthoringResourceRepository>{};
  bool _disposed = false;

  /// Fails fast when a child repository is requested after scope disposal.
  void checkActive() {
    if (_disposed) throw StateError("The resource session ended");
  }

  /// Returns the cached services repository for one organization.
  ///
  /// Repeated calls share the repository and therefore its mutation boundary.
  ServiceResourceRepository services(skir.RecordId organization) {
    checkActive();
    return _services.putIfAbsent(
      organization,
      () => ServiceResourceRepository(this, organization),
    );
  }

  /// Returns the cached authoring repository for one organization and realm.
  ///
  /// The realm is part of the cache key because authoring operations are scoped
  /// more narrowly than organization level service operations.
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

  /// Ends the resource scope and disposes every repository created by this owner.
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
