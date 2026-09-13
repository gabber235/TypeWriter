part of "books.dart";

const bookInspectorTypeRef = ResolvedTypeRef(
  id: QualifiedTypeId(namespace: "panel", name: "Book"),
  revision: 1,
);

const _bookInspectorPresentationId = PresentationId(
  namespace: "panel",
  name: "book.inspector",
);

final _bookInspectorType = TypeDefinition(
  id: bookInspectorTypeRef,
  kind: NominalTypeKind.concrete,
  defaultPresentationId: _bookInspectorPresentationId,
  representation: RecordType(
    fields: {
      "title": TypeField(name: "title", type: identifierStringType),
      "icon": TypeField(name: "icon", type: NamedType(standardTypeRefs.icon)),
      "color": TypeField(
        name: "color",
        type: NamedType(standardTypeRefs.color),
      ),
      "tags": TypeField(
        name: "tags",
        type: ListType(
          element: NamedType(
            standardTypeRefs.refTo(NamedType(tagInspectorTypeRef)),
          ),
          unique: true,
        ),
      ),
    },
  ),
);

final _bookInspectorCatalog = TypeCatalog([_bookInspectorType]);

final _bookInspectorPresentation = PresentationDefinition.single(
  id: _bookInspectorPresentationId,
  target: NamedType(bookInspectorTypeRef),
  root: PresentationNode(
    id: "book.inspector",
    element: ColumnElement(
      spacing: 16,
      crossAxisAlignment: PresentationCrossAxisAlignment.stretch,
      children: [
        PresentationNode(
          id: "book.title",
          element: TextInputElement(
            control: BoundControl(
              binding: _bookField("title"),
              label: "Title".asStringLiteral,
            ),
            multiline: false,
            inputFormatters: identifierInputFormats,
          ),
        ),
        PresentationNode(
          id: "book.icon",
          element: PolymorphicInputElement(
            control: BoundControl(
              binding: _bookField("icon"),
              label: "Icon".asStringLiteral,
            ),
            concreteTypes: [
              ConcreteTypePresentation(
                type: standardTypeRefs.iconifyIcon,
                label: "Iconify".asStringLiteral,
              ),
              ConcreteTypePresentation(
                type: standardTypeRefs.svgIcon,
                label: "SVG".asStringLiteral,
              ),
            ],
          ),
        ),
        PresentationNode(
          id: "book.color",
          element: ColorInputElement(
            control: BoundControl(
              binding: _bookField("color"),
              label: "Color".asStringLiteral,
            ),
          ),
        ),
        tagReferenceSearch(
          id: "book.tags",
          label: "Direct Tags",
          binding: _bookField("tags"),
        ),
        effectiveTagGraph(
          id: "book.effectiveTags",
          title: "Effective Tags",
          roots: _bookField("tags"),
        ),
      ],
    ),
  ),
);

final class BookMultiInspectionDefinition implements MultiInspectionDefinition {
  const BookMultiInspectionDefinition();

  @override
  PresentationId get id => _bookInspectorPresentationId;

  @override
  bool isCompatibleWith(MultiInspectionDefinition other) =>
      other is BookMultiInspectionDefinition;

  @override
  TypeResult<InspectionContent> build(
    List<EditableSelectable> selection,
    InspectionBuildContext context,
  ) {
    final selected = selection.requireAll<BookSelection>();
    final books = selected.valueOrNull;
    if (books == null) return TypeResult.failure(selected.diagnostics);
    final catalog = books.mergedTypeCatalog;
    final merged = catalog.valueOrNull;
    if (merged == null) return TypeResult.failure(catalog.diagnostics);
    final collection = books.sharedBookTagCollection;
    final tags = collection.valueOrNull;
    if (tags == null) return TypeResult.failure(collection.diagnostics);
    final owner = context.multiEditorFor(
      books,
      rootType: const NamedType(bookInspectorTypeRef),
      typeCatalog: merged,
    );
    return TypeResult.success(
      InspectionContent(
        model: PresentationModel.editor(
          owner: owner,
          presentations: [_bookInspectorPresentation],
          collections: [tags],
        ),
      ),
    );
  }
}

extension BookSelectionCollectionConsistency on List<BookSelection> {
  TypeResult<PresentationCollectionSource> get sharedBookTagCollection {
    final first = firstOrNull?.tagCollection;
    if (first == null) {
      return TypeResult.failure([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Selected Books have no Tag collection",
        ),
      ]);
    }
    for (final selection in skip(1)) {
      final candidate = selection.tagCollection;
      final sameLocalRows = switch ((first, candidate)) {
        (
          LocalPresentationCollectionSource(:final rows),
          LocalPresentationCollectionSource(rows: final candidateRows),
        ) =>
          const ListEquality<DataValue>().equals(
            rows.toList(),
            candidateRows.toList(),
          ),
        _ => identical(first, candidate),
      };
      if (candidate.id != first.id ||
          candidate.schema != first.schema ||
          !sameLocalRows) {
        return TypeResult.failure([
          const TypeDiagnostic(
            code: TypeDiagnosticCode.invalidValue,
            message: "Selected Books have inconsistent Tag collections",
          ),
        ]);
      }
    }
    return TypeResult.success(first);
  }
}
