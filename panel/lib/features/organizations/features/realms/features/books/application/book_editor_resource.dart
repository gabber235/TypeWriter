part of "books.dart";

final class BookEditorSnapshot extends EditorSnapshot {
  const BookEditorSnapshot(this.book, this.revision);
  final Book book;
  final int revision;
  @override
  EditorDocument get document => EditorDocument(
    rootType: NamedType(bookInspectorTypeRef),
    typeCatalog: _bookInspectorCatalog,
    confirmedValue: book.inspectorValue,
    revision: revision,
    mergePolicies: {DataPath.root.field("tags"): EditorMergePolicy.set},
  );
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
            return BookEditorSnapshot(Book.fromWire(book), snapshot.sequence);
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
    final next = current.book.withInspectorValue(commit.rootValue);
    final expected = current.book.withInspectorValue(commit.baseValue);
    if (next == null || expected == null) {
      throw StateError("The Book value is invalid");
    }
    return bookPatchOperation(next, expected: expected);
  }
}
