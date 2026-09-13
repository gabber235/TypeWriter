part of "authoring_session.dart";

Set<skir.RecordId> _operationResources(wire.AuthoringOperation operation) =>
    switch (operation) {
      wire.AuthoringOperation_createBookWrapper(:final value) => {
        value.book.id,
      },
      wire.AuthoringOperation_patchBookWrapper(:final value) => {value.id},
      wire.AuthoringOperation_deleteBookWrapper(:final value) => {value.id},
      wire.AuthoringOperation_createTagWrapper(:final value) => {value.tag.id},
      wire.AuthoringOperation_patchTagWrapper(:final value) => {value.id},
      wire.AuthoringOperation_deleteTagWrapper(:final value) => {value.id},
      wire.AuthoringOperation_createPageWrapper(:final value) => {
        value.page.id,
        value.page.book,
      },
      wire.AuthoringOperation_patchPageWrapper(:final value) => {value.id},
      wire.AuthoringOperation_deletePageWrapper(:final value) => {value.id},
      wire.AuthoringOperation_createElementWrapper(:final value) => {
        value.element.id,
        value.element.page,
      },
      wire.AuthoringOperation_patchElementWrapper(:final value) => {
        value.id,
        if (value.page != null) value.page!.value,
      },
      wire.AuthoringOperation_duplicateElementWrapper(:final value) => {
        value.sourceId,
        value.newId,
        value.page,
      },
      wire.AuthoringOperation_deleteElementWrapper(:final value) => {value.id},
      wire.AuthoringOperation_unknown() => throw ArgumentError(
        "Unknown authoring operation",
      ),
    };
