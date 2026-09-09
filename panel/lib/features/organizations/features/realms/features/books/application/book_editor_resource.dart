part of "books.dart";

final class BookEditorSnapshot extends EditorSnapshot {
  const BookEditorSnapshot(this.book);
  final Book book;
  @override
  EditorDocument get document => EditorDocument(
    rootType: NamedType(bookInspectorTypeRef),
    typeCatalog: _bookInspectorCatalog,
    confirmedValue: book.inspectorValue,
    revision: book.authoringSequence,
    mergePolicies: {DataPath.root.field("tags"): EditorMergePolicy.set},
  );

  Book? _bookFromInspectorValue(
    DataValue value, {
    required int expectedRevision,
  }) {
    if (value is! RecordValue) return null;
    final title = value.fields["title"];
    final icon = value.fields["icon"]?.iconValueOrNull;
    final color = value.fields["color"];
    final tags = value.fields["tags"];
    if (title is! StringValue ||
        title.value.trim().isEmpty ||
        icon == null ||
        color is! IntegerValue ||
        tags is! ListValue) {
      return null;
    }
    final decodedColor = color.colorOrNull;
    final tagIds = tags.values
        .whereType<StringValue>()
        .map((tag) => recordId("tag:${tag.value}"))
        .toList();
    if (decodedColor == null || tagIds.length != tags.values.length) {
      return null;
    }
    final encodedIcon = switch (icon) {
      IconifyIconValue(:final value) => value,
      SvgIconValue(:final source) => source,
    };
    return book.copyWith(
      authoringSequence: expectedRevision,
      title: title.value,
      icon: encodedIcon,
      color: decodedColor,
      tagIds: tagIds,
    );
  }
}

final class BookEditorResource extends AuthoringEditorResource {
  const BookEditorResource(super.repository, super.id);
  @override
  wire.AuthoringSnapshotScope get scope => wire.AuthoringSnapshotScope.library_;
  @override
  EditorSnapshot? project(wire.AuthoringSnapshot snapshot) {
    for (final slice in snapshot.slices) {
      if (slice case wire.AuthoringSnapshotSlice_libraryWrapper(:final value)) {
        for (final book in value.books) {
          if (book.id == id) {
            return BookEditorSnapshot(Book.fromWire(book, snapshot.sequence));
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
    final current = snapshot as BookEditorSnapshot;
    final next = current._bookFromInspectorValue(
      commit.rootValue,
      expectedRevision: commit.expectedRevision,
    );
    final expected = current._bookFromInspectorValue(
      commit.baseValue,
      expectedRevision: commit.expectedRevision,
    );
    if (next == null || expected == null) {
      throw StateError("The Book value is invalid");
    }
    return bookPatchOperation(next, expected: expected);
  }
}
