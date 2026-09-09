part of "tag_selectable.dart";

final class TagEditorSnapshot extends EditorSnapshot {
  const TagEditorSnapshot(this.tag);
  final Tag tag;
  @override
  EditorDocument get document => EditorDocument(
    rootType: NamedType(tagInspectorTypeRef),
    typeCatalog: _tagInspectorCatalog,
    confirmedValue: tag.inspectorValue,
    revision: tag.authoringSequence,
    mergePolicies: {DataPath.root.field("parents"): EditorMergePolicy.set},
  );

  @override
  EditorMutationResult validate(DataPath path, DataValue value) {
    final result = super.validate(path, value);
    if (result is! AppliedEditorMutation || value is! IntegerValue) {
      return result;
    }
    final widthPath = DataPath.root.field("layout").field("width");
    final heightPath = DataPath.root.field("layout").field("height");
    if ((path == widthPath || path == heightPath) && value.value < BigInt.one) {
      return EditorMutationResult.invalid([
        TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "Tag dimensions must be greater than zero",
          path: path,
        ),
      ]);
    }
    return result;
  }

  Tag? _tagFromInspectorValue(
    DataValue value, {
    required int expectedRevision,
  }) {
    if (value is! RecordValue) return null;
    final name = value.fields["name"];
    final color = value.fields["color"];
    final parents = value.fields["parents"];
    final layout = value.fields["layout"];
    if (name is! StringValue ||
        name.value.trim().isEmpty ||
        color is! IntegerValue ||
        parents is! ListValue ||
        layout is! RecordValue) {
      return null;
    }
    final decodedColor = color.colorOrNull;
    final parentIds = parents.values
        .whereType<StringValue>()
        .map((parent) => recordId("tag:${parent.value}"))
        .toList();
    final x = layout.fields["x"];
    final y = layout.fields["y"];
    final width = layout.fields["width"];
    final height = layout.fields["height"];
    if (decodedColor == null ||
        parentIds.length != parents.values.length ||
        x is! IntegerValue ||
        y is! IntegerValue ||
        width is! IntegerValue ||
        height is! IntegerValue ||
        width.value < BigInt.one ||
        height.value < BigInt.one) {
      return null;
    }
    return tag.copyWith(
      authoringSequence: expectedRevision,
      name: name.value,
      color: decodedColor,
      parentIds: parentIds,
      placement: Placement(
        x: x.value.toInt(),
        y: y.value.toInt(),
        width: width.value.toInt(),
        height: height.value.toInt(),
      ),
    );
  }

  @override
  List<TypeDiagnostic> validateDraft(DataValue value) =>
      _tagFromInspectorValue(value, expectedRevision: tag.authoringSequence) ==
          null
      ? [
          const TypeDiagnostic(
            code: TypeDiagnosticCode.invalidValue,
            message: "The Tag value is invalid",
          ),
        ]
      : const [];
}

final class TagEditorResource extends AuthoringEditorResource {
  const TagEditorResource(super.repository, super.id);
  @override
  wire.AuthoringSnapshotScope get scope => wire.AuthoringSnapshotScope.library_;
  @override
  EditorSnapshot? project(wire.AuthoringSnapshot snapshot) {
    for (final slice in snapshot.slices) {
      if (slice case wire.AuthoringSnapshotSlice_libraryWrapper(:final value)) {
        for (final tag in value.tags) {
          if (tag.id == id) {
            return TagEditorSnapshot(Tag.fromWire(tag, snapshot.sequence));
          }
        }
      }
    }
    return null;
  }

  @override
  wire.AuthoringOperation operation(
    EditorSnapshot snapshot,
    EditorCommit commit,
  ) {
    final current = snapshot as TagEditorSnapshot;
    final next = current._tagFromInspectorValue(
      commit.rootValue,
      expectedRevision: commit.expectedRevision,
    );
    final expected = current._tagFromInspectorValue(
      commit.baseValue,
      expectedRevision: commit.expectedRevision,
    );
    if (next == null || expected == null) {
      throw StateError("The Tag value is invalid");
    }
    return tagPatchOperation(next, expected: expected);
  }
}
