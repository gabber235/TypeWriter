import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/entry.dart";
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
    "some2": {"some1": "test1", "some2": "test2"}
  },
  "complexList": [
    {"some1": "test1", "some2": "test2"},
    {"some1": "test1", "some2": "test2"}
  ],
};

const entryBlueprintFields = ObjectField(
  fields: {
    "id": FieldInfo.primitive(type: PrimitiveFieldType.string),
    "name": FieldInfo.primitive(type: PrimitiveFieldType.string),
    "type": FieldInfo.primitive(type: PrimitiveFieldType.string),
    "simpleMap": FieldInfo.map(
      key: FieldInfo.primitive(type: PrimitiveFieldType.string),
      value: FieldInfo.primitive(type: PrimitiveFieldType.string),
    ),
    "simpleList": FieldInfo.list(
      type: FieldInfo.primitive(type: PrimitiveFieldType.string),
    ),
    "complexMap": FieldInfo.map(
      key: FieldInfo.primitive(type: PrimitiveFieldType.string),
      value: FieldInfo.map(
        key: FieldInfo.primitive(type: PrimitiveFieldType.string),
        value: FieldInfo.primitive(type: PrimitiveFieldType.string),
      ),
    ),
    "complexList": FieldInfo.list(
      type: FieldInfo.map(
        key: FieldInfo.primitive(type: PrimitiveFieldType.string),
        value: FieldInfo.primitive(type: PrimitiveFieldType.string),
      ),
    ),
  },
);

void main() {
  test("When creating an entry expect the entry to be created", () async {
    final container = ProviderContainer();
    await container.read(bookProvider.notifier).createPage("test_page");

    final page = container.read(pageProvider("test_page"));

    expect(page, isNotNull, reason: "The page has not been created");
    expect(page!.entries.length, 0);

    final entry = Entry(testEntryData);

    await page.createEntry(container.passing, entry);

    final newPage = container.read(pageProvider("test_page"));

    expect(newPage!.entries.length, 1);
    expect(newPage.entries[0].id, "test");
  });

  test("When creating an entry form a blueprint expect the entry to be created", () async {
    final container = ProviderContainer();
    await container.read(bookProvider.notifier).createPage("test_page");

    final page = container.read(pageProvider("test_page"));

    expect(page, isNotNull, reason: "The page has not been created");
    expect(page!.entries.length, 0);

    const entry = EntryBlueprint(
      name: "test_type",
      description: "Some test",
      adapter: "test_adapter",
      fields: entryBlueprintFields,
    );

    await page.createEntryFromBlueprint(container.passing, entry);

    final newPage = container.read(pageProvider("test_page"));

    expect(newPage!.entries.length, 1);
    expect(newPage.entries[0].id, isNot("test"));
  });
}
