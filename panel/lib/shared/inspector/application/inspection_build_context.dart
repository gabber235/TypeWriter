import "package:typewriter_panel/typewriter_panel.dart";

/// Owns composite editors created while assembling one inspection model.
final class InspectionBuildContext {
  InspectionBuildContext(this.owners);

  final EditorOwnerRefresh owners;
  final List<MultiEditOwner> _multiEditors = [];

  MultiEditOwner multiEditorFor(
    Iterable<EditableSelectable> targets, {
    required TypeExpression rootType,
    required TypeCatalog typeCatalog,
  }) => multiEditorForOwners(
    targets.map(owners.editor),
    rootType: rootType,
    typeCatalog: typeCatalog,
  );

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

  void dispose() => _rollbackTo(0);
}
