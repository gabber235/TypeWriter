import "package:collection/collection.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

extension BookCommands on AuthoringSession {
  Future<TypedMutationResult> commitBook(
    Book next, {
    required Book expected,
  }) async {
    Future<TypedMutationResult> accept(
      wire.ApplyAuthoringBatchResponse response,
    ) async {
      switch (response) {
        case wire.ApplyAuthoringBatchResponse_appliedWrapper(:final value):
          return TypedMutationResult.success(
            revision: value.sequence,
            value: next.inspectorValue,
          );
        case wire.ApplyAuthoringBatchResponse_conflictWrapper():
          final canonical = snapshot.books[expected.bookId];
          if (canonical == null)
            return unavailableMutation(
              "The book no longer exists",
              targetDeleted: true,
            );
          final actual = Book.fromWire(canonical, snapshot.sequence ?? 0);
          return TypedMutationResult.conflict(
            expectedRevision: expected.authoringSequence,
            actualRevision: actual.authoringSequence,
            actualValue: actual.inspectorValue,
          );
        default:
          return response.toMutationFailure(
            unavailableMessage: "The book update could not be completed",
          );
      }
    }

    try {
      return await accept(await patchBook(next, expected: expected));
    } on SubmissionException<wire.ApplyAuthoringBatchResponse> catch (error) {
      return error.toMutation(accept);
    }
  }

  Future<wire.ApplyAuthoringBatchResponse> createBook(wire.Book book) =>
      apply([wire.AuthoringOperation.createCreateBook(book: book)]);

  Future<wire.ApplyAuthoringBatchResponse> patchBook(
    Book book, {
    required Book expected,
  }) {
    final before = expected;
    final operation = wire.AuthoringOperation.createPatchBook(
      id: book.bookId,
      title: before.title == book.title
          ? null
          : wire.StringChange(expected: before.title, value: book.title),
      icon: before.icon == book.icon
          ? null
          : wire.StringChange(expected: before.icon, value: book.icon),
      color: before.color == book.color
          ? null
          : wire.ColorChange(
              expected: before.color.toSkirColor(),
              value: book.color.toSkirColor(),
            ),
      tags:
          const ListEquality<skir.RecordId>().equals(before.tagIds, book.tagIds)
          ? null
          : wire.RecordIdListChange(
              expected: before.tagIds,
              value: book.tagIds,
            ),
    );
    return apply([operation]);
  }
}
