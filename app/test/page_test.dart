import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/passing_reference.dart";

const testEntryData = {
  "id": "test",
  "name": "Test Entry",
  "type": "test_type",
  "simpleMap": {"some1": "test1", "some2": "test2"},
  "simpleList": ["test1", "test2"],
  "complexMap": {
    "some1": {"some1": "test1", "some2": "test2"},
    "some2": {"some1": "test1", "some2": "test2"},
  },
  "complexList": [
    {"some1": "test1", "some2": "test2"},
    {"some1": "test1", "some2": "test2"},
  ],
};

const entryBlueprintFields = ObjectBlueprint(
  fields: {
    "id": DataBlueprint.primitive(type: PrimitiveType.string),
    "name": DataBlueprint.primitive(type: PrimitiveType.string),
    "type": DataBlueprint.primitive(type: PrimitiveType.string),
    "simpleMap": DataBlueprint.map(
      key: DataBlueprint.primitive(type: PrimitiveType.string),
      value: DataBlueprint.primitive(type: PrimitiveType.string),
    ),
    "simpleList": DataBlueprint.list(
      type: DataBlueprint.primitive(type: PrimitiveType.string),
    ),
    "complexMap": DataBlueprint.map(
      key: DataBlueprint.primitive(type: PrimitiveType.string),
      value: DataBlueprint.map(
        key: DataBlueprint.primitive(type: PrimitiveType.string),
        value: DataBlueprint.primitive(type: PrimitiveType.string),
      ),
    ),
    "complexList": DataBlueprint.list(
      type: DataBlueprint.map(
        key: DataBlueprint.primitive(type: PrimitiveType.string),
        value: DataBlueprint.primitive(type: PrimitiveType.string),
      ),
    ),
  },
);

void main() {
  test("When creating an entry expect the entry to be created", () async {
    final container = ProviderContainer();
    final page =
        await container.read(bookProvider.notifier).createPage("test_page");

    expect(page.entries.length, 0);

    final entry = Entry(testEntryData);

    await page.createEntry(container.passing, entry);

    final newPage = container.read(pageProvider(page.id));

    expect(newPage!.entries.length, 1);
    expect(newPage.entries[0].id, "test");
  });

  test("When creating an entry form a blueprint expect the entry to be created",
      () async {
    final container = ProviderContainer();
    final page =
        await container.read(bookProvider.notifier).createPage("test_page");

    expect(page.entries.length, 0);

    const entry = EntryBlueprint(
      id: "test",
      name: "test_type",
      description: "Some test",
      extension: "Test",
      dataBlueprint: entryBlueprintFields,
    );

    await page.createEntryFromBlueprint(container.passing, entry);

    final newPage = container.read(pageProvider(page.id));

    expect(newPage!.entries.length, 1);
    expect(newPage.entries[0].id, isNot("test"));
  });
}
