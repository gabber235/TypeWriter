import "package:flutter/widgets.dart";
import "package:typewriter_panel/typewriter_panel.dart";

abstract class InspectableSelectable<I extends SelectableIdentifier>
    extends Selectable<I> {
  const InspectableSelectable();
  PresentationModel buildPresentation(EditorOwnerRegistry owners);
  Widget? buildInspectorHeader();
}

abstract class EditableSelectable<I extends SelectableIdentifier>
    extends InspectableSelectable<I>
    implements EditorTarget {
  const EditableSelectable();
  @override
  SelectableIdentifier get targetId => id;
  @override
  String get label => name;
  @override
  EditorCommitPolicy get commitPolicy => EditorCommitPolicy.autosaveChanges;
  @override
  List<TypeDiagnostic> validateDraft(DataValue value) => snapshot.validateDraft(value);
  ResolvedTypeRef get rootType {
    final type = document.rootType;
    if (type is NamedType) return type.reference;
    throw StateError("Editable root type must be nominal");
  }

  TypeCatalog get typeCatalog => document.typeCatalog;
  TypeRegistry get typeRegistry => TypeRegistry(typeCatalog);
  @override
  EditorDocument get document => snapshot.document;
  @override
  EditorValue value(DataPath path) =>
      document.confirmedValue.readEditorValue(path);
  @override
  EditorMutationResult validate(DataPath path, DataValue value) => document
      .rootType
      .validateEditorMutation(path, value, registry: typeRegistry);
  EditorMutationResult validateUpdate(DataPath path, DataValue value) =>
      validate(path, value);
  List<PresentationDefinition> get presentations => const [];
  List<PresentationCollectionSource> get collections => const [];
  DataPath get presentationPath => DataPath.root;
  PresentationNode? get rootPresentation => null;

  @override
  PresentationModel buildPresentation(EditorOwnerRegistry owners) =>
      PresentationModel.editor(
        owner: owners.editor(this),
        path: presentationPath,
        presentation: rootPresentation,
        presentations: presentations,
        collections: collections,
        diagnostics: document.diagnostics,
      );
}

extension InspectableSelectableCatalogMerge on Iterable<EditableSelectable> {
  TypeCatalog get mergedTypeCatalog {
    final definitions = <TypeDefinition>[];
    final firstById = <ResolvedTypeRef, TypeDefinition>{};
    for (final selectable in this) {
      for (final definition in selectable.typeCatalog.definitions) {
        final existing = firstById[definition.id];
        if (identical(existing, definition)) continue;
        firstById.putIfAbsent(definition.id, () => definition);
        definitions.add(definition);
      }
    }
    return TypeCatalog(definitions);
  }
}

extension InspectableSelectableTypeQueries on EditableSelectable {
  TypeResult<List<TypeReferenceLocation>> referenceLocations({
    String? relation,
  }) {
    final resolved = typeRegistry.resolveExact(rootType);
    final type = resolved.valueOrNull;
    if (type == null) return TypeResult.failure(resolved.diagnostics);
    return TypeResult.success(
      type.representation.queryReferences(relation: relation),
    );
  }
}
