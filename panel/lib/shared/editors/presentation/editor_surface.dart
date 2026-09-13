import "dart:async";

import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Convenience surface for one independently owned editor.
class EditorSurface extends StatelessWidget {
  const EditorSurface({
    required this.source,
    this.path = DataPath.root,
    this.registry,
    this.presentation,
    this.presentations = const [],
    this.collections = const [],
    this.conversions = const [],
    this.runtime,
    this.realmSearchSourceBuilder,
    this.executePanelInstruction,
    this.headerShortcuts = const {},
    this.historyNamespace = "local",
    this.readOnly = false,
    super.key,
  });

  final EditorSource source;
  final DataPath path;
  final TypeRegistry? registry;
  final PresentationNode? presentation;
  final List<PresentationDefinition> presentations;
  final List<PresentationCollectionSource> collections;
  final List<ConversionDefinition> conversions;
  final EditorRealmActionExecutor? runtime;
  final RealmPresentationSearchSourceBuilder? realmSearchSourceBuilder;
  final FutureOr<void> Function(PanelInstruction instruction)?
  executePanelInstruction;
  final Map<HeaderItemCommandId, List<ShortcutActivator>> headerShortcuts;
  final String historyNamespace;
  final bool readOnly;

  @override
  Widget build(BuildContext context) => ListenableBuilder(
    listenable: source,
    builder: (context, _) {
      if (source.document == null) return const SizedBox.shrink();
      var model = PresentationModel.editor(
        owner: source,
        presentation: presentation,
        presentations: presentations,
        collections: collections,
        diagnostics: source.document!.diagnostics,
      );
      if (path != DataPath.root) {
        final type = source.rootType
            .resolvePath(
              path,
              registry: registry ?? TypeRegistry(source.typeCatalog),
            )
            .valueOrNull;
        if (type != null) {
          model = model.copyWith(
            inputs: {
              const BindingId(0): PresentationInput.edit(source, path: path),
            },
            root: type.generateDefaultPresentation(),
          );
        }
      }
      return ComposedEditor(
        model: model,
        runtime: runtime,
        conversions: conversions,
        realmSearchSourceBuilder: realmSearchSourceBuilder,
        executePanelInstruction: executePanelInstruction,
        headerShortcuts: headerShortcuts,
        historyNamespace: historyNamespace,
        readOnly: readOnly || source.readOnly,
      );
    },
  );
}
