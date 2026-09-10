part of "entries.dart";

class EntryIdentifier extends SelectableIdentifier
    implements GraphDragData, GraphIdentifier {
  const EntryIdentifier(this.id, {this.pageId});

  final String? pageId;

  @override
  final String id;

  @override
  Object get resourceId => recordId("element:$id");

  @override
  AsyncValue<Selectable<EntryIdentifier>> create(Ref ref) {
    final organizationId = ref.watch(organizationIdProvider);
    final realmId = ref.watch(realmIdProvider);
    if (organizationId == null || realmId == null) {
      return AsyncValue.error(
        ApiException.badRequest("No realm selected"),
        StackTrace.current,
      );
    }
    final index = ref.watch(realmEntryIndexProvider(organizationId, realmId));
    if (index.mapUnready<Selectable<EntryIdentifier>>() case final state?) {
      return state;
    }
    final location = index.requireValue[id];

    if (location == null) {
      return AsyncError(SelectableNotFoundException(this), StackTrace.current);
    }
    final state = ref.watch(authoringSessionProvider(organizationId, realmId));
    final repository = ref
        .watch(resourceRepositoriesProvider)
        .authoring(organizationId, realmId);
    final asyncEntry = ref.watch(entryProvider(id));
    if (asyncEntry.mapUnready<Selectable<EntryIdentifier>>()
        case final value?) {
      return value;
    }
    final value = asyncEntry.requireValue;

    if (value == null) {
      throw SelectableNotFoundException(this);
    }
    final catalogState = ref.watch(
      realmEditorCatalogForTypeProvider(value.elementDefinition.rootType),
    );
    return catalogState.resolveElement(
      value.elementDefinition,
      (catalog, presentations) => EntrySelection(
        target: authoringElementTarget(
          repository: repository,
          state: state,
          identity: EntryIdentifier(id, pageId: location.pageId),
          pageId: location.pageId,
          label: value.name,
          document: EditorDocument(
            rootType: NamedType(value.elementDefinition.rootType),
            typeCatalog: catalog,
            confirmedValue: value.data,
            revision: value.authoringSequence,
          ),
        ),
        id: EntryIdentifier(id, pageId: location.pageId),
        definition: value,
        typeCatalog: catalog,
        presentations: presentations,
      ),
    );
  }

  @override
  GraphIdentifier get graphId => this;

  @override
  int get hashCode => id.hashCode;

  @override
  bool operator ==(Object other) =>
      identical(this, other) || (other is EntryIdentifier && other.id == id);

  @override
  String toString() => "EntryIdentifier($id)";
}

class EntrySelection extends EditableSelectable<EntryIdentifier> {
  const EntrySelection({
    required this.target,
    required this.id,
    required this.definition,
    required this.typeCatalog,
    required this.presentations,
  });

  final EditorTarget target;

  @override
  final EntryIdentifier id;
  final EntryDefinition definition;

  @override
  final TypeCatalog typeCatalog;
  @override
  final List<PresentationDefinition> presentations;

  @override
  String get name => definition.name;

  @override
  EditorDocument get document => target.document;

  @override
  DataPath get presentationPath => elementValuePath;

  @override
  ResolvedTypeRef get rootType => definition.elementDefinition.rootType;

  @override
  List<SelectionCapability> get capabilities => [];

  @override
  Widget? buildInspectorHeader() {
    return EntryHeader(
      id: id.id,
      name: name,
      color: definition.elementDefinition.color,
    );
  }

  @override
  EditableResource get resource => target.resource;
  @override
  EditorSnapshot get snapshot => target.snapshot;

  @override
  String toString() => "EntrySelection($id)";
}

/// Header for a entry displaying title and identifier.
class EntryHeader extends HookWidget {
  const EntryHeader({
    required this.id,
    required this.name,
    required this.color,
    super.key,
  });

  final String id;
  final String name;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Title(title: name, color: color),
        const SizedBox(height: 8),
        Identifier(id: id),
      ],
    );
  }
}
