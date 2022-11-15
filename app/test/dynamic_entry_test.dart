import "package:flutter_test/flutter_test.dart";
import "package:typewriter/models/page.dart";

void main() {
  final rawDynamicEntry = {
    "id": "1",
    "name": "test",
    "type": "test_type",
    "simple_list": [1, 2, 3],
    "complex_list": [
      {"id": "1", "name": "test1"},
      {"id": "2", "name": "test2"},
      {"id": "3", "name": "test3"}
    ],
    "simple_map": {"key1": "value1", "key2": "value2"},
    "complex_map": {
      "key1": {
        "id": "1",
        "name": "test1",
        "inner_list": [
          {"id": "1", "name": "test1"},
          {"id": "2", "name": "test2"},
          {"id": "3", "name": "test3"}
        ]
      },
      "key2": {
        "id": "2",
        "name": "test2",
        "inner_list": [
          {"id": "1", "name": "test1"},
          {"id": "2", "name": "test2"},
          {"id": "3", "name": "test3"}
        ]
      }
    }
  };

  test("When a entry is parsed expect its fields to be able to be fetched", () {
    final entry = Entry(rawDynamicEntry);

    expect(entry.id, "1");
    expect(entry.name, "test");
    expect(entry.type, "test_type");

    expect(entry.get("simple_list"), [1, 2, 3]);
    expect(entry.get("simple_list.1"), 2);

    expect(entry.get("complex_list"), [
      {"id": "1", "name": "test1"},
      {"id": "2", "name": "test2"},
      {"id": "3", "name": "test3"}
    ]);
    expect(entry.get("complex_list.1.name"), "test2");

    expect(entry.get("simple_map"), {"key1": "value1", "key2": "value2"});
    expect(entry.get("simple_map.key1"), "value1");

    expect(entry.get("complex_map.key2.inner_list.1.name"), "test2");
  });

  test("When a key is not found, null is returned", () {
    final entry = Entry(rawDynamicEntry);
    expect(entry.get("not_found"), null);
    expect(entry.get("simple_list.4"), null);
    expect(entry.get("complex_list.4.name"), null);
    expect(entry.get("complex_list.1.not_found"), null);
    expect(entry.get("simple_map.key3"), null);
  });

  test("When a key is not found, a default value is returned", () {
    final entry = Entry(rawDynamicEntry);
    expect(entry.get("not_found", "default"), "default");
    expect(entry.get("simple_list.4", "default"), "default");
    expect(entry.get("complex_list.4.name", "default"), "default");
    expect(entry.get("complex_list.1.not_found", "default"), "default");
    expect(entry.get("simple_map.key3", "default"), "default");
  });

  test("When a dynamic entry is updated, the new value is returned", () {
    final entry = Entry(rawDynamicEntry);
    var newEntry = entry.copyWith("simple_list.1", 4);
    expect(newEntry.get("simple_list.1"), 4);

    newEntry = entry.copyWith("complex_list.1.name", "new_name");
    expect(newEntry.get("complex_list.1.name"), "new_name");

    newEntry = entry.copyWith("simple_map.key1", "new_value");
    expect(newEntry.get("simple_map.key1"), "new_value");

    newEntry = entry.copyWith("complex_map.key2.inner_list.1.name", "new_name");
    expect(newEntry.get("complex_map.key2.inner_list.1.name"), "new_name");
  });

  test("When a entry is updated, expect the original entry to be unchanged",
      () {
    final entry = Entry(rawDynamicEntry);
    entry.copyWith("complex_map.key2.inner_list.1.name", "new_name");
    expect(entry.get("complex_map.key2.inner_list.1.name"), "test2");
  });
}
