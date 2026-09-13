import "dart:async";

import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Optional realm capabilities used by typed editor surfaces.
///
/// The nullable provider keeps shared editor code independent of a realm host.
/// A host installs the runtime when actions and presentation searches are
/// available; consumers must handle the absent runtime as an unavailable
/// capability rather than constructing a partial substitute.
final editorRealmRuntimeProvider = Provider<EditorRealmRuntime?>(
  (ref) => null,
  dependencies: [],
);

/// Groups realm operations that belong to the active editor host.
///
/// The runtime is an explicit boundary for action execution, search source
/// construction, and optional panel instructions. It carries capabilities only;
/// ownership of the realm session and its lifecycle stays with the provider
/// that installs it.
final class EditorRealmRuntime {
  const EditorRealmRuntime({
    required this.executeAction,
    required this.searchSourceBuilder,
    this.executePanelInstruction,
  });

  final EditorRealmActionExecutor executeAction;
  final RealmPresentationSearchSourceBuilder searchSourceBuilder;
  final FutureOr<void> Function(PanelInstruction instruction)?
  executePanelInstruction;
}
