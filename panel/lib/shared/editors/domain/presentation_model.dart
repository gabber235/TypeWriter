import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "presentation_model.freezed.dart";

@freezed
sealed class PresentationInput with _$PresentationInput {
  const factory PresentationInput.value({
    required TypeExpression type,
    required EditorValue value,
  }) = PresentationValueInput;
  const factory PresentationInput.edit(
    EditOwner owner, {
    @Default(DataPath.root) DataPath path,
  }) = PresentationEditInput;
}

/// Composes independently owned values. The caller retains ownership of editors.
@freezed
abstract class PresentationModel with _$PresentationModel {
  const factory PresentationModel({
    required TypeCatalog catalog,
    required Map<BindingId, PresentationInput> inputs,
    required PresentationNode root,
    @Default({}) Map<EditOwner, String> ownerLabels,
    @Default([]) List<PresentationDefinition> presentations,
    @Default([]) List<PresentationCollectionSource> collections,
    @Default([]) List<TypeDiagnostic> diagnostics,
  }) = _PresentationModel;

  const PresentationModel._();

  factory PresentationModel.value({
    required TypeExpression type,
    required DataValue value,
    required TypeCatalog catalog,
    PresentationNode? presentation,
    List<PresentationDefinition> presentations = const [],
    List<PresentationCollectionSource> collections = const [],
    List<TypeDiagnostic> diagnostics = const [],
  }) => PresentationModel(
    catalog: catalog,
    inputs: {
      const BindingId(0): PresentationInput.value(
        type: type,
        value: EditorValue.ready(value),
      ),
    },
    root: presentation ?? _singlePresentationRoot(type, catalog, presentations),
    presentations: presentations,
    collections: collections,
    diagnostics: diagnostics,
  );

  factory PresentationModel.editor({
    required EditOwner owner,
    DataPath path = DataPath.root,
    PresentationNode? presentation,
    List<PresentationDefinition> presentations = const [],
    List<PresentationCollectionSource> collections = const [],
    List<TypeDiagnostic> diagnostics = const [],
  }) {
    return PresentationModel(
      catalog: owner.typeCatalog,
      inputs: {const BindingId(0): PresentationInput.edit(owner, path: path)},
      root:
          presentation ??
          _singlePresentationRoot(
            owner.rootType
                    .resolvePath(
                      path,
                      registry: TypeRegistry(owner.typeCatalog),
                    )
                    .valueOrNull ??
                owner.rootType,
            owner.typeCatalog,
            presentations,
          ),
      presentations: presentations,
      collections: collections,
      diagnostics: diagnostics,
    );
  }

  /// Declares root lexical access independently of current write availability.
  Map<BindingId, PresentationInputAccess> get inputAccess => {
    for (final entry in inputs.entries)
      entry.key: switch (entry.value) {
        PresentationValueInput() => PresentationInputAccess.read,
        PresentationEditInput() => PresentationInputAccess.edit,
      },
  };

  /// Identifies root transaction origins. Value inputs have no edit owner.
  Map<BindingId, BindingReference?> get ownerBindings => {
    for (final entry in inputs.entries)
      entry.key: switch (entry.value) {
        PresentationValueInput() => null,
        PresentationEditInput() => BindingReference(bindingId: entry.key),
      },
  };
}

PresentationNode _singlePresentationRoot(
  TypeExpression declared,
  TypeCatalog catalog,
  List<PresentationDefinition> presentations,
) {
  final registry = TypeRegistry(catalog);
  final resolved = declared is NamedType
      ? registry.resolve(declared).valueOrNull?.representation ?? declared
      : declared;
  final selected = declared is NamedType
      ? registry.definition(declared.reference)?.defaultPresentationId
      : null;
  final definition = [...builtinPresentationDefinitions(), ...presentations]
      .where(
        (definition) =>
            definition.id == selected && definition.inputs.length == 1,
      )
      .firstOrNull;
  if (definition == null) return resolved.generateDefaultPresentation();
  return PresentationNode(
    id: "editor",
    element: PresentationInvocationElement(
      presentationId: definition.id,
      arguments: {
        definition.inputs.single.id: const BindingReference(
          bindingId: BindingId(0),
        ),
      },
    ),
  );
}
