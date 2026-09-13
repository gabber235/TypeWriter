part of "authoring_session.dart";

/// Produces the user facing mutation label for one authoring batch.
///
/// Uniform batches retain the specific intent, including their count. Mixed
/// batches use a generic label because no single operation describes the whole
/// transaction. The label is presentation metadata and does not affect routing
/// or protocol identity.
String _authoringLabel(Iterable<wire.AuthoringOperation> operations) {
  final labels = operations
      .map(
        (operation) => switch (operation) {
          wire.AuthoringOperation_createBookWrapper() => "Create book",
          wire.AuthoringOperation_patchBookWrapper() => "Edit book",
          wire.AuthoringOperation_deleteBookWrapper() => "Delete book",
          wire.AuthoringOperation_createTagWrapper() => "Create tag",
          wire.AuthoringOperation_patchTagWrapper() => "Edit tag",
          wire.AuthoringOperation_deleteTagWrapper() => "Delete tag",
          wire.AuthoringOperation_createPageWrapper() => "Create page",
          wire.AuthoringOperation_patchPageWrapper(:final value) =>
            value.name != null
                ? "Rename page"
                : value.chapter != null
                ? "Move page chapter"
                : "Change page priority",
          wire.AuthoringOperation_deletePageWrapper() => "Delete page",
          wire.AuthoringOperation_createElementWrapper() => "Create element",
          wire.AuthoringOperation_patchElementWrapper(:final value) =>
            value.page != null
                ? "Move element to page"
                : value.placement != null
                ? "Change element placement"
                : "Edit element",
          wire.AuthoringOperation_duplicateElementWrapper() =>
            "Duplicate element",
          wire.AuthoringOperation_deleteElementWrapper() => "Delete element",
          wire.AuthoringOperation_unknown() => "Save content",
        },
      )
      .toList();
  final distinct = labels.toSet();
  return distinct.length == 1
      ? "${distinct.single}${labels.length > 1 ? " (${labels.length})" : ""}"
      : "Save content batch (${labels.length})";
}
