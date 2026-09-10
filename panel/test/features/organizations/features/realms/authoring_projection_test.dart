import "package:flutter/material.dart" hide Page;
import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";

void main() {
  test("Tag projection overlays edited fields onto fresh canonical data", () {
    final canonical = Tag(
      tagId: recordId("tag:test"),
      name: "Remote name",
      color: Colors.blue,
      parentIds: const [],
      placement: const Placement(x: 1, y: 2, width: 3, height: 4),
    );
    final withDraftName = DataPath.root
        .field("name")
        .replace(canonical.inspectorValue, "Draft name".asValue)
        .valueOrNull!;
    final draft = DataPath.root
        .field("color")
        .replace(withDraftName, Colors.red.asValue)
        .valueOrNull!;

    final projected = canonical.projected(
      LocalEditorValue(
        value: draft,
        editedPaths: {DataPath.root.field("name")},
      ),
    );

    expect(projected.name, "Draft name");
    expect(projected.color.toARGB32(), Colors.blue.toARGB32());
    expect(projected.placement, canonical.placement);
  });

  test("Book and Page projections decode domain values", () {
    final book = Book(
      bookId: recordId("book:test"),
      title: "Remote title",
      icon: "mdi:book",
      color: Colors.blue,
      tagIds: const [],
    );
    final bookDraft = DataPath.root
        .field("color")
        .replace(book.inspectorValue, Colors.red.asValue)
        .valueOrNull!;
    expect(
      book
          .projected(
            LocalEditorValue(
              value: bookDraft,
              editedPaths: {DataPath.root.field("color")},
            ),
          )
          .color
          .toARGB32(),
      Colors.red.toARGB32(),
    );

    final page = Page(
      pageId: recordId("page:test"),
      bookId: book.bookId,
      name: "Remote page",
      kind: const PageKindRef(id: "kind", revision: 1),
      chapter: "remote",
      priority: 1,
    );
    final pageDraft = DataPath.root
        .field("chapter")
        .replace(page.editorValue, "draft".asValue)
        .valueOrNull!;
    final projectedPage = page.projected(
      LocalEditorValue(
        value: pageDraft,
        editedPaths: {DataPath.root.field("chapter")},
      ),
    );
    expect(projectedPage.chapter, "draft");
    expect(projectedPage.name, "Remote page");
  });

  test("Page element projection merges value and placement paths", () {
    final definition = generateRandomEntryDefinition().copyWith(
      id: "entry",
      placement: const EntryPlacement(x: 1, y: 2, width: 3, height: 4),
    );
    final element = PageElement.entry(
      entry: PageEntry.definition(definition: definition),
    );
    final x = elementPlacementPath.field("x");
    final draft = x.replace(element.editorValue!, 9.asValue).valueOrNull!;

    final projected = element.projected(
      LocalEditorValue(value: draft, editedPaths: {x}),
    );
    final projectedDefinition = switch (projected) {
      PageElementEntry(entry: DefinitionPageEntry(:final definition)) =>
        definition,
      _ => throw StateError("Expected a definition entry"),
    };

    expect(projectedDefinition.placement.x, 9);
    expect(projectedDefinition.placement.y, 2);
    expect(projectedDefinition.data, definition.data);
  });
}
