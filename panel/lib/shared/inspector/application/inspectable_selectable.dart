import "package:flutter/widgets.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Describes a shared editor for a homogeneous multi selection.
///
/// A selectable supplies this definition when it can participate in a shared
/// presentation. The inspection session checks the definition identity and
/// pairwise compatibility before calling [build]. A failed build is reported
/// as diagnostics and its temporary composite editors are discarded.
abstract interface class MultiInspectionDefinition {
  PresentationId get id;

  bool isCompatibleWith(MultiInspectionDefinition other);

  TypeResult<InspectionContent> build(
    List<EditableSelectable> selection,
    InspectionBuildContext context,
  );
}

/// A selectable resource that can be represented in the inspector.
///
/// This is the read only inspection boundary. Implementations create a
/// presentation from the supplied owner scope. [buildInspection] may add a
/// header, but it does not own resource editors; the inspection session does.
abstract class InspectableSelectable<I extends SelectableIdentifier>
    extends Selectable<I> {
  const InspectableSelectable();

  /// Builds the presentation graph for this resource.
  PresentationModel buildPresentation(EditorOwnerScope owners);

  /// Builds the graph and optional resource header for this resource.
  InspectionContent buildInspection(EditorOwnerScope owners) =>
      InspectionContent(model: buildPresentation(owners));
}

/// The presentation graph and optional header produced for one inspection.
///
/// The graph is consumed by [InspectionSession]. Any composite editor created
/// while building it must be registered through [InspectionBuildContext] so
/// the session can dispose it when the graph is replaced.
final class InspectionContent {
  const InspectionContent({required this.model, this.header});

  final PresentationModel model;
  final Widget? header;
}

/// A selectable resource whose inspector presentation edits local draft state.
///
/// The resource snapshot supplies canonical metadata and validation. The
/// [EditorOwnerScope] supplies the mutable local owner used by the graph.
/// Multiple selected resources are edited through a [MultiEditOwner], which
/// projects common fields while each resource owner remains authoritative for
/// its own draft and persistence.
abstract class EditableSelectable<I extends SelectableIdentifier>
    extends InspectableSelectable<I>
    implements EditorTarget {
  const EditableSelectable();

  /// Returns the optional shared editor definition for multi selection.
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

  /// Builds the header that observes this resource's local draft.
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

/// Queries the compatibility and type information needed by shared editors.
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

/// Resolves type references used by operations on one editable selection.
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
