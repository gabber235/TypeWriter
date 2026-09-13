import "package:typewriter_panel/typewriter_panel.dart";

/// Owns composite editors created while assembling one inspection model.
///
/// A build is provisional until [InspectionSession] installs its result. If a
/// definition fails or throws, editors created during that build are disposed
/// without affecting the previously installed graph. The session disposes the
/// committed context when the next graph replaces it.
final class InspectionBuildContext {
  InspectionBuildContext(this.owners);

  final EditorOwnerRefresh owners;
  final List<MultiEditOwner> _multiEditors = [];

  /// Creates and registers a composite owner for the selected targets.
  ///
  /// The returned owner is valid for this build context only. The individual
  /// target owners remain owned by the editor owner registry.
  MultiEditOwner multiEditorFor(
    Iterable<EditableSelectable> targets, {
    required TypeExpression rootType,
    required TypeCatalog typeCatalog,
  }) => multiEditorForOwners(
    targets.map(owners.editor),
    rootType: rootType,
    typeCatalog: typeCatalog,
  );

  /// Creates and registers a composite owner from already resolved owners.
  MultiEditOwner multiEditorForOwners(
    Iterable<EditOwner> owners, {
    required TypeExpression rootType,
    required TypeCatalog typeCatalog,
  }) {
    final editor = MultiEditOwner(
      owners: owners.toList(growable: false),
      rootType: rootType,
      typeCatalog: typeCatalog,
      commitInteractions: (interactions) => interactions.commitAtomically(),
    );
    _multiEditors.add(editor);
    return editor;
  }

  /// Runs a shared inspection build with rollback on failure.
  TypeResult<InspectionContent> compose(
    MultiInspectionDefinition definition,
    List<EditableSelectable> selection,
  ) {
    final checkpoint = _multiEditors.length;
    try {
      final result = definition.build(selection, this);
      if (result is TypeFailure) _rollbackTo(checkpoint);
      return result;
    } on Object {
      _rollbackTo(checkpoint);
      rethrow;
    }
  }

  void _rollbackTo(int checkpoint) {
    final abandoned = _multiEditors.sublist(checkpoint);
    _multiEditors.removeRange(checkpoint, _multiEditors.length);
    for (final editor in abandoned) {
      editor.dispose();
    }
  }

  /// Disposes every composite owner created by this context.
  void dispose() => _rollbackTo(0);
}
