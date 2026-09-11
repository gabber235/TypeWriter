part of "tags.dart";

const tagCollectionSourceId = PresentationCollectionSourceId("realm.tags");
const tagInheritsRelationId = PresentationCollectionRelationId("inherits");
const tagCollectionRowBindingId = BindingId(40);

final tagReferenceType = NamedType(
  standardTypeRefs.refTo(NamedType(tagInspectorTypeRef)),
);

final tagCollectionRowType = RecordType(
  fields: {
    "key": TypeField(name: "key", type: tagReferenceType),
    "name": TypeField(name: "name", type: StringType()),
    "color": TypeField(name: "color", type: NamedType(standardTypeRefs.color)),
    "parents": TypeField(
      name: "parents",
      type: ListType(element: tagReferenceType),
    ),
    "selectable": TypeField(name: "selectable", type: BooleanType()),
  },
);

final tagCollectionSchema = PresentationCollectionSchema(
  rowType: tagCollectionRowType,
  keyType: tagReferenceType,
  rowBindingId: tagCollectionRowBindingId,
  key: _tagRowField("key", tagReferenceType),
  relations: [
    PresentationCollectionRelation(
      id: tagInheritsRelationId,
      targets: _tagRowField("parents", ListType(element: tagReferenceType)),
    ),
  ],
);

extension TagPresentationCollection on Iterable<Tag> {
  PresentationCollectionSource presentationCollection({
    skir.RecordId? editingTagId,
    Iterable<skir.RecordId> existingParentIds = const [],
  }) {
    final existingParents = existingParentIds.toSet();
    final rows = map(
      (tag) => tag._collectionRow(
        selectable:
            tag.unavailableParentReason(
              this,
              editingTagId: editingTagId,
              existingParents: existingParents,
            ) ==
            null,
      ),
    ).toList(growable: false);
    return LocalPresentationCollectionSource(
      id: tagCollectionSourceId,
      schema: tagCollectionSchema,
      rows: rows,
      registry: TypeRegistry(TypeCatalog([tagInspectorTypeDefinition])),
      searchPredicate: (row, query) {
        if (row is! RecordValue) return false;
        final name = row.fields["name"];
        return name?.asStringOrNull?.toLowerCase().contains(
              query.normalizedQuery.toLowerCase(),
            ) ??
            false;
      },
    );
  }
}

extension on Tag {
  RecordValue _collectionRow({required bool selectable}) => RecordValue({
    "key": tagId.id.asValue,
    "name": name.asValue,
    "color": color.asValue,
    "parents": ListValue(parentIds.map((parent) => parent.id.asValue).toList()),
    "selectable": selectable.asValue,
  });
}

extension TagParentCandidate on Tag {
  String? unavailableParentReason(
    Iterable<Tag> tags, {
    required skir.RecordId? editingTagId,
    required Set<skir.RecordId> existingParents,
  }) {
    if (editingTagId == null) return null;
    if (tagId == editingTagId) return "A Tag cannot inherit itself";
    if (existingParents.contains(tagId)) return null;
    final byId = {for (final tag in tags) tag.tagId: tag};
    final descendant = _isAncestor(
      byId,
      tagId: tagId,
      ancestorId: editingTagId,
    );
    if (descendant ?? true) return "A descendant cannot become a parent";
    return null;
  }
}

TypedExpression _tagRowField(String name, TypeExpression type) =>
    TypedExpression(
      resultType: type,
      expression: BindingExpression(
        BindingReference(
          bindingId: tagCollectionRowBindingId,
          path: DataPath.root.field(name),
        ),
      ),
    );

// TODO: Replace this eager Tag collection with a Realm backed collection
// source when Realm Tag counts make local enumeration unsuitable.
