import "package:flutter/widgets.dart";
import "package:typewriter_panel/typewriter_panel.dart";

abstract interface class MultiInspectionDefinition {
  PresentationId get id;

  bool isCompatibleWith(MultiInspectionDefinition other);

  TypeResult<InspectionContent> build(
    List<EditableSelectable> selection,
    InspectionBuildContext context,
  );
}

abstract class InspectableSelectable<I extends SelectableIdentifier>
    extends Selectable<I> {
  const InspectableSelectable();

  PresentationModel buildPresentation(EditorOwnerScope owners);

  InspectionContent buildInspection(EditorOwnerScope owners) =>
      InspectionContent(model: buildPresentation(owners));
}

final class InspectionContent {
  const InspectionContent({required this.model, this.header});

  final PresentationModel model;
  final Widget? header;
}

abstract class EditableSelectable<I extends SelectableIdentifier>
    extends InspectableSelectable<I>
    implements EditorTarget {
  const EditableSelectable();

  MultiInspectionDefinition? get multiInspection => null;

  @override
  SelectableIdentifier get targetId => id;

  @override
  String get label => name;

  @override
  EditorCommitPolicy get commitPolicy => EditorCommitPolicy.autosaveChanges;

  @override
  List<TypeDiagnostic> validateDraft(DataValue value) =>
      snapshot.validateDraft(value);

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

  Widget? buildInspectorHeader(EditOwner owner);

  @override
  PresentationModel buildPresentation(EditorOwnerScope owners) =>
      PresentationModel.editor(
        owner: owners.editor(this),
        path: presentationPath,
        presentation: rootPresentation,
        presentations: presentations,
        collections: collections,
        diagnostics: document.diagnostics,
      );

  @override
  InspectionContent buildInspection(EditorOwnerScope owners) =>
      InspectionContent(
        header: buildInspectorHeader(owners.editor(this)),
        model: buildPresentation(owners),
      );
}

extension EditableSelectionInspection on List<EditableSelectable> {
  TypeResult<MultiInspectionDefinition?> get sharedMultiInspection {
    if (isEmpty) return const TypeResult.success(null);
    final definitions = map((target) => target.multiInspection).toList();
    final first = definitions.first;
    if (first == null || definitions.any((definition) => definition == null)) {
      return const TypeResult.success(null);
    }
    final peers = definitions.cast<MultiInspectionDefinition>();
    if (peers.any((candidate) => candidate.id != first.id)) {
      return const TypeResult.success(null);
    }
    if (peers.any(
      (candidate) =>
          !first.isCompatibleWith(candidate) ||
          !candidate.isCompatibleWith(first),
    )) {
      return TypeResult.failure([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Selected inspector definitions conflict",
        ),
      ]);
    }
    return TypeResult.success(first);
  }

  TypeResult<TypeCatalog> get mergedTypeCatalog {
    final definitions = <TypeDefinition>[];
    final byId = <ResolvedTypeRef, TypeDefinition>{};
    for (final selectable in this) {
      for (final definition in selectable.typeCatalog.definitions) {
        final existing = byId[definition.id];
        if (existing == definition) continue;
        if (existing != null) {
          return TypeResult.failure([
            TypeDiagnostic(
              code: TypeDiagnosticCode.invalidValue,
              message:
                  "Selected type ${definition.id} has conflicting definitions",
            ),
          ]);
        }
        byId[definition.id] = definition;
        definitions.add(definition);
      }
    }
    return TypeResult.success(TypeCatalog(definitions));
  }

  TypeResult<List<T>> requireAll<T extends EditableSelectable>() {
    final selected = whereType<T>().toList(growable: false);
    if (selected.length == length) return TypeResult.success(selected);
    return TypeResult.failure([
      TypeDiagnostic(
        code: TypeDiagnosticCode.invalidValue,
        message: "Selected inspector requires only $T values",
      ),
    ]);
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
