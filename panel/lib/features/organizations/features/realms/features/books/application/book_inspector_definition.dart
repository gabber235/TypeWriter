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
