import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod/riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

part "book_inspector_definition.dart";
part "book_model.dart";
part "book_queries.dart";
part "book_selection.dart";
part "book_editor_resource.dart";
part "books.freezed.dart";
part "books.g.dart";

@riverpod
class CanonicalBooks extends _$CanonicalBooks {
  @override
  Future<List<Book>> build() async {
    final organizationId = ref.watch(organizationIdProvider);
    final realmId = ref.watch(realmIdProvider);
    if (organizationId == null || realmId == null) {
      return [];
    }

    final provider = authoringSessionProvider(organizationId, realmId);
    ref.listen(provider, (_, value) {
      if (value.sequence != null) state = AsyncData(_projectBooks(value));
    });
    final lease = ref.watch(
      authoringLibraryScopeProvider(organizationId, realmId),
    );
    await lease.ready;
    return _projectBooks(ref.read(provider));
  }

  Future<Book> createBook({
    required String title,
    String? icon,
    Color? color,
    List<skir.RecordId> tagIds = const [],
  }) async {
    state.ensureReady();
    final book = Book(
      bookId: newResourceId(AuthoringResource.book),
      title: title,
      icon: icon ?? "mdi:book",
      color: color ?? Colors.grey,
      tagIds: tagIds,
    );
    final response = await ref.readAuthoringSession().notifier.createBook(
      book.toWire(),
    );
    response.requireApplied(conflictMessage: "The book already exists");
    return book;
  }

  Future<TypedMutationResult> updateBook(Book book, {Book? expected}) async {
    state.ensureReady();
    final session = ref.readAuthoringSession();
    final current = session.state.books[book.bookId];
    if (current == null || session.state.sequence == null) {
      throw ApiException.notFound("Book");
    }
    final before = expected ?? Book.fromWire(current);
    final commands = session.notifier;
    final owners = EditorOwnerRegistry(
      workspace: ref.read(localWorkControllerProvider),
    );
    try {
      final owner = owners.editor(
        BookSelection(
          resource: BookEditorResource(
            ref
                .read(resourceRepositoriesProvider)
                .authoring(commands.organizationId, commands.realmId),
            book.bookId,
          ),
          onOpen: null,
          id: BookIdentifier(book.bookId),
          book: before,
          revision: session.state.sequence!,
          tagCollection: (ref.read(projectedTagsProvider).value ?? const [])
              .presentationCollection(),
        ),
      );
      return await owner.applyChanges(
        editorValueChanges(before.inspectorValue, book.inspectorValue),
      );
    } finally {
      owners.dispose();
    }
  }
}

List<Book> _projectBooks(AuthoringSessionState value) {
  return value.books.values.map(Book.fromWire).toList();
}

extension AuthoringBookValue on AuthoringSessionState {
  AuthoringValue<Book>? bookEditorValue(skir.RecordId bookId) {
    final value = books[bookId];
    final revision = sequence;
    if (value == null || revision == null) return null;
    return AuthoringValue(value: Book.fromWire(value), revision: revision);
  }
}
