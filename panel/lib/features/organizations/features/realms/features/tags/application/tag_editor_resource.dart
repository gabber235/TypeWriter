part of "tag_selectable.dart";

final class TagEditorSnapshot extends EditorSnapshot {
  const TagEditorSnapshot(this.tag, this.revision);
  final Tag tag;
  final int revision;
  @override
  EditorDocument get document => EditorDocument(
    rootType: NamedType(tagInspectorTypeRef),
    typeCatalog: _tagInspectorCatalog,
    confirmedValue: tag.inspectorValue,
    revision: revision,
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

  @override
  List<TypeDiagnostic> validateDraft(DataValue value) =>
      tag.withInspectorValue(value) == null
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
            return TagEditorSnapshot(Tag.fromWire(tag), snapshot.sequence);
          }
        }
      }
    }
    return null;
  }

  @override
  EditorSnapshot? projectApplied(
    wire.AuthoringChanged change,
    EditorSnapshot submitted,
  ) {
    for (final resource in change.changes) {
      switch (resource) {
        case wire.AuthoringResourceChange_upsertTagWrapper(:final value):
          if (value.id == id) {
            return TagEditorSnapshot(Tag.fromWire(value), change.sequence);
          }
        case wire.AuthoringResourceChange_removeTagWrapper(:final value):
          if (value == id) return null;
        case wire.AuthoringResourceChange_unknown() ||
            wire.AuthoringResourceChange_upsertBookWrapper() ||
            wire.AuthoringResourceChange_removeBookWrapper() ||
            wire.AuthoringResourceChange_upsertPageWrapper() ||
            wire.AuthoringResourceChange_removePageWrapper() ||
            wire.AuthoringResourceChange_upsertElementWrapper() ||
            wire.AuthoringResourceChange_removeElementWrapper():
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
    final next = current.tag.withInspectorValue(commit.rootValue);
    final expected = current.tag.withInspectorValue(commit.baseValue);
    if (next == null || expected == null) {
      throw StateError("The Tag value is invalid");
    }
    return tagPatchOperation(next, expected: expected);
  }
}
