part of "route.stories.dart";

const _storyEntryIcon = IconValue.svg(
  "<svg viewBox=\"0 0 24 24\"><path d=\"M4 4h16v16H4z\"/></svg>",
);

List<PageElement>? pageStoryElements({
  required PageType pageType,
  required DisplayState state,
  required List<PageElement>? overwriteElements,
}) {
  if (overwriteElements != null) return overwriteElements;
  return switch (state) {
    DisplayState.loading || DisplayState.error => null,
    DisplayState.noItems => const [],
    DisplayState.fewItems => _entryStoryElements(pageType, 6),
    DisplayState.manyItems => _entryStoryElements(pageType, 12),
  };
}

List<PageElement> _entryStoryElements(PageType pageType, int count) {
  final rootType = ResolvedTypeRef(
    id: fixtureDeclaredTypeId("widgetbook:${pageType.name}Entry"),
    revision: 1,
  );
  return [
    for (var index = 0; index < count; index++)
      PageElement.entry(
        entry: PageEntry.definition(
          definition: EntryDefinition(
            id: "${pageType.name}_entry_$index",
            name: "${pageType.name.formatted} Entry ${index + 1}",
            elementDefinition: ElementDefinition(
              rootType: rootType,
              name: "${pageType.name.formatted} Entry",
              description: "Deterministic Widgetbook Entry",
              icon: _storyEntryIcon,
              color: safeColors[index % safeColors.length],
            ),
            placement: EntryPlacement(
              x: (index % 4) * 5,
              y: (index ~/ 4) * 3,
              width: 4,
              height: 2,
            ),
            data: RecordValue({
              "name": StringValue("Entry ${index + 1}"),
              "priority": IntegerValue(BigInt.from(index)),
              "weight": FloatValue(index + 0.5),
              "enabled": BooleanValue(index.isEven),
            }),
            inwardEdges: const [],
            outwardEdges: const [],
          ),
        ),
      ),
  ];
}

RealmEditorCatalogState pageStoryCatalog(
  ResolvedTypeRef rootType,
  List<PageElement> elements,
) {
  final value = _rootValues(elements)[rootType];
  final representation = value == null
      ? RecordType(fields: {})
      : _recordType(value);
  return RealmEditorCatalogState.ready(
    RealmEditorCatalogSnapshot(
      catalog: TypeCatalog([
        TypeDefinition(
          id: rootType,
          kind: NominalTypeKind.concrete,
          representation: representation,
        ),
      ]),
      generation: const CatalogGeneration("widgetbook"),
    ),
  );
}

RealmEditorCatalogState pageStoryPageCatalog(
  PageType pageType,
  List<PageElement> elements,
) {
  final editor = switch (pageType) {
    PageType.scene => const RealmTimelinePageEditor(
      trackTypes: [],
      segmentTypes: [],
      keyframeTypes: [],
    ),
    _ => const RealmGraphPageEditor(
      direction: GraphDirection.leftToRight,
      nodeTypes: [],
    ),
  };
  final definition = RealmPageDefinition(
    kind: pageType.kind,
    name: pageType.displayName.formatted,
    description: "Widgetbook page definition",
    icon: _storyEntryIcon,
    color: safeColors.first,
    editor: editor,
    originArtifactId: "widgetbook",
    sourcePart: "page-story",
  );
  return RealmEditorCatalogState.ready(
    RealmEditorCatalogSnapshot(
      catalog: TypeCatalog([
        for (final entry in _rootValues(elements).entries)
          TypeDefinition(
            id: entry.key,
            kind: NominalTypeKind.concrete,
            representation: _recordType(entry.value),
          ),
      ]),
      generation: const CatalogGeneration("widgetbook"),
      pageCatalog: RealmPageCatalog(definitions: {pageType.kind: definition}),
    ),
  );
}

Map<ResolvedTypeRef, RecordValue> _rootValues(List<PageElement> elements) {
  final values = <ResolvedTypeRef, RecordValue>{};
  for (final element in elements) {
    switch (element) {
      case PageElementEntry(entry: DefinitionPageEntry(:final definition)):
        values[definition.elementDefinition.rootType] = definition.data;
      case PageElementCue(:final cue):
        values[cue.elementDefinition.rootType] = cue.data;
      default:
        break;
    }
  }
  return values;
}

RecordType _recordType(RecordValue value) => RecordType(
  fields: {
    for (final field in value.fields.entries)
      field.key: TypeField(name: field.key, type: _valueType(field.value)),
  },
);

TypeExpression _valueType(DataValue value) {
  if (value is RecordValue) return _recordType(value);
  return switch (value) {
    UnitValue() => const UnitType(),
    BooleanValue() => const BooleanType(),
    IntegerValue() => const IntegerType(width: IntegerWidth.signed64),
    FloatValue() => const FloatType(width: FloatWidth.float64),
    DecimalValue() => const DecimalType(),
    StringValue() => const StringType(),
    BytesValue() => const BytesType(),
    TimestampValue() => const TimestampType(),
    DurationValue() => const DurationType(),
    ListValue(:final values) => ListType(
      element: values.isEmpty ? const AnyType() : _valueType(values.first),
    ),
    MapValue(:final entries) => MapType(
      key: entries.isEmpty ? const AnyType() : _valueType(entries.first.key),
      value: entries.isEmpty
          ? const AnyType()
          : _valueType(entries.first.value),
    ),
    PolymorphicValue(:final concreteType) => NamedType(concreteType),
    _ => const AnyType(),
  };
}

AuthoringSessionState pageStoryAuthoring(
  PageType pageType,
  List<PageElement> elements,
) {
  final page = wire.Page(
    id: recordId("page:example-page-id"),
    book: recordId("book:example-book-id"),
    name: "Example",
    kind: skir.PageKindRef(id: skir.PageKindId(value: "example"), revision: 1),
    chapter: "",
    priority: 0,
  );
  final catalog =
      (pageStoryPageCatalog(pageType, elements) as RealmEditorCatalogReady)
          .value
          .catalog;
  final codec = SkirEditorCodec(TypeRegistry(catalog));
  return AuthoringSessionState(
    sequence: 1,
    pages: {page.id: page},
    documents: {
      page.id: wire.PageDocument(
        page: page,
        elements: [
          for (final element in elements)
            switch (element) {
              PageElementEntry(entry: DefinitionPageEntry(:final definition)) =>
                wire.PageElement(
                  id: recordId("element:${definition.id}"),
                  page: page.id,
                  name: definition.name,
                  elementType: definition.elementDefinition.typeId.uuid,
                  schemaRevision:
                      definition.elementDefinition.rootType.revision,
                  value: codec.encodeValue(definition.data).valueOrNull!,
                  placement: wire.ElementPlacement.createGraph(
                    x: definition.placement.x,
                    y: definition.placement.y,
                    width: definition.placement.width,
                    height: definition.placement.height,
                  ),
                ),
              PageElementCue(:final cue) => wire.PageElement(
                id: recordId("element:${cue.id}"),
                page: page.id,
                name: cue.elementDefinition.name,
                elementType: cue.elementDefinition.typeId.uuid,
                schemaRevision: cue.elementDefinition.rootType.revision,
                value: codec.encodeValue(cue.data).valueOrNull!,
                placement: switch (cue) {
                  Segment(:final startFrame, :final endFrame) =>
                    wire.ElementPlacement.createTimelineSegment(
                      startFrame: startFrame,
                      endFrame: endFrame,
                    ),
                  Keyframe(:final frame) =>
                    wire.ElementPlacement.createTimelineKeyframe(frame: frame),
                  _ => throw StateError("Unknown story cue"),
                },
              ),
              _ => throw StateError("Unsupported story element"),
            },
        ],
        references: const [],
        crossPageTargets: const [],
        crossPageSources: const [],
        diagnostics: const [],
        compileStatus: wire.PageCompileStatus.createBlocked(
          lastActiveManifestId: null,
          diagnosticCount: 0,
        ),
      ),
    },
  );
}
