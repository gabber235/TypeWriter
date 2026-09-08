import "dart:async";
import "package:flutter/foundation.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod/riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "editor_workspace.freezed.dart";
part "editor_workspace.g.dart";

@Riverpod(keepAlive: true)
EditorWorkspace editorWorkspace(Ref ref) {
  ref.watch(userIdProvider.select((value) => value.value));
  final workspace = EditorWorkspace();
  ref.onDispose(workspace.dispose);
  return workspace;
}

@freezed
abstract class EditorResourceKey with _$EditorResourceKey {
  const factory EditorResourceKey({
    required Object? scope,
    required Object identity,
  }) = _EditorResourceKey;
}

/// A live navigation destination. The workspace owns its lifetime.
/// Implementations notify when the current view enters or leaves the destination.
abstract class EditorDestination extends ChangeNotifier {
  bool get isCurrent;
  Future<void> open();
}

final class EditorResource {
  EditorResource(this.target, this.source);
  EditorTarget target;
  EditorDestination? _destination;
  EditorDestination? get destination => _destination;
  set destination(EditorDestination? value) {
    if (identical(value, _destination)) return;
    if (listener case final changed?) _destination?.removeListener(changed);
    _destination?.dispose();
    _destination = value;
    if (listener case final changed?) value?.addListener(changed);
  }

  int leases = 0;
  VoidCallback? listener;
  final TransactionalEditorSource source;
  StreamSubscription<EditorDocument?>? subscription;
}

/// Owns resource drafts for a signed in session, independently of presentations.
final class EditorWorkspace extends ChangeNotifier {
  final Map<EditorResourceKey, EditorResource> _resources = {};
  bool _disposed = false;
  Map<EditorResourceKey, EditorResource> get resources =>
      Map.unmodifiable(_resources);

  EditorSource editor(EditorResourceKey key, EditorTarget target) {
    final existing = _resources[key];
    if (existing != null) {
      existing.target = target;
      existing.source.refreshDocument(target.document);
      return existing.source;
    }
    late final EditorResource resource;
    final source = TransactionalEditorSource(
      document: target.document,
      commitPolicy: target.commitPolicy,
      validateDraft: (value) => resource.target.validateDraft(value),
      validate: (path, value) => resource.target.validate(path, value),
      commit: target.commit,
    );
    resource = EditorResource(target, source);
    _resources[key] = resource;
    resource.listener = () {
      notifyListeners();
      scheduleMicrotask(() {
        if (!_disposed && resource.leases == 0 && !source.hasWork) _remove(key);
      });
    };
    source.addListener(resource.listener!);
    resource.subscription = target.updates.listen(
      (document) {
        if (document == null) {
          source.acceptRemoteDeletion();
        } else {
          source.refreshDocument(document);
        }
      },
      onError: (Object error, StackTrace stackTrace) {
        source.refreshDocument(
          source.document.copyWith(
            readOnly: true,
            diagnostics: [
              TypeDiagnostic(
                code: TypeDiagnosticCode.invalidValue,
                message: "The resource could not be refreshed",
              ),
            ],
          ),
        );
        FlutterError.reportError(
          FlutterErrorDetails(exception: error, stack: stackTrace),
        );
      },
    );
    return source;
  }

  void retain(EditorResourceKey key) => _resources[key]!.leases++;

  void release(EditorResourceKey key) {
    final resource = _resources[key];
    if (resource == null) return;
    resource.leases--;
    if (resource.leases > 0 || resource.source.hasWork) return;
    _remove(key);
  }

  void _remove(EditorResourceKey key) {
    final resource = _resources.remove(key);
    if (resource == null) return;
    resource.destination = null;
    resource.source.removeListener(resource.listener!);
    unawaited(resource.subscription?.cancel());
    resource.source.dispose();
  }

  @override
  void dispose() {
    _disposed = true;
    for (final resource in _resources.values) {
      resource.destination = null;
      resource.source.removeListener(resource.listener!);
      unawaited(resource.subscription?.cancel());
      resource.source.dispose();
    }
    _resources.clear();
    super.dispose();
  }
}
