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
          {"id": "1", "name": "test_a"},
          {"id": "2", "name": "test_b"},
          {"id": "3", "name": "test_c"}
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

    expect(entry.get("complex_map.key2.inner_list.1.name"), "test_b");
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

  test("When a path is fetched, all values should be returned", () {
    final entry = Entry(rawDynamicEntry);
    expect(entry.getAll("simple_list.*"), [1, 2, 3]);
    expect(entry.getAll("simple_map.*"), ["value1", "value2"]);
    expect(entry.getAll("complex_map.*.name"), ["test1", "test2"]);
    expect(entry.getAll("complex_map.*.inner_list.*.name"), ["test1", "test2", "test3", "test_a", "test_b", "test_c"]);
    expect(entry.getAll("complex_map.key2.inner_list.*.name"), ["test_a", "test_b", "test_c"]);
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

  test("When an entry is updated, expect the original entry to be unchanged", () {
    final entry = Entry(rawDynamicEntry);
    entry.copyWith("complex_map.key2.inner_list.1.name", "new_name");
    expect(entry.get("complex_map.key2.inner_list.1.name"), "test_b");
  });

  group("Copy Mapped", () {
    test("When copying while modifying simple static field expect the field to change", () {
      final entry = Entry(rawDynamicEntry);
      final newEntry = entry.copyMapped("simple_list.1", (value) => value + 1);

      expect(newEntry.get("simple_list.0"), 1, reason: "simple_list.0 should not have changed");
      expect(newEntry.get("simple_list.1"), 3, reason: "simple_list.1 should have changed");
      expect(newEntry.get("simple_list.2"), 3, reason: "simple_list.2 should not have changed");
    });

    test("When copying while modifying complex static field expect the field to change", () {
      final entry = Entry(rawDynamicEntry);
      final newEntry = entry.copyMapped("complex_list.1.name", (value) => value + "_new");

      expect(newEntry.get("complex_list.0.name"), "test1", reason: "complex_list.0.name should not have changed");
      expect(newEntry.get("complex_list.1.name"), "test2_new", reason: "complex_list.1.name should have changed");
      expect(newEntry.get("complex_list.2.name"), "test3", reason: "complex_list.2.name should not have changed");
    });

    test("When copying while modifying simple dynamic field expect the field to change", () {
      final entry = Entry(rawDynamicEntry);
      final newEntry = entry.copyMapped("simple_map.*", (value) => value + "_new");

      expect(newEntry.get("simple_map.key1"), "value1_new", reason: "simple_map.key1 should have changed");
      expect(newEntry.get("simple_map.key2"), "value2_new", reason: "simple_map.key2 should have changed");
    });

    test("When copying while modifying complex dynamic field expect the field to change", () {
      final entry = Entry(rawDynamicEntry);
      final newEntry = entry.copyMapped("complex_map.*.name", (value) => value + "_new");

      expect(newEntry.get("complex_map.key1.name"), "test1_new", reason: "complex_map.key1.name should have changed");
      expect(newEntry.get("complex_map.key2.name"), "test2_new", reason: "complex_map.key2.name should have changed");
    });

    test("When copying while modifying fields with multiple *'s and a final field expect the fields to change", () {
      final entry = Entry(rawDynamicEntry);
      final newEntry = entry.copyMapped("complex_map.*.inner_list.*.name", (value) => value + "_new");

      expect(newEntry.get("complex_map.key1.inner_list.0.name"), "test1_new");
      expect(newEntry.get("complex_map.key1.inner_list.1.name"), "test2_new");
      expect(newEntry.get("complex_map.key1.inner_list.2.name"), "test3_new");
      expect(newEntry.get("complex_map.key2.inner_list.0.name"), "test_a_new");
      expect(newEntry.get("complex_map.key2.inner_list.1.name"), "test_b_new");
      expect(newEntry.get("complex_map.key2.inner_list.2.name"), "test_c_new");
    });

    test("When copying while modifying simple dynamic field to null expect the field to be removed", () {
      final entry = Entry(rawDynamicEntry);
      final newEntry = entry.copyMapped("simple_list.*", (value) => value == 2 ? null : value);

      expect(newEntry.get("simple_list"), [1, 3], reason: "Should have removed the value of 2");
    });

    test("When copying while modifying complex dynamic field to null expect the field to be removed", () {
      final entry = Entry(rawDynamicEntry);
      final newEntry = entry.copyMapped(
        "complex_map.*.inner_list.*",
        (value) => ["test1", "test2"].contains(value["name"]) ? null : value,
      );

      expect(newEntry.get("complex_map.key1.inner_list"), [
        {"id": "3", "name": "test3"}
      ]);
      expect(newEntry.get("complex_map.key2.inner_list"), [
        {"id": "1", "name": "test_a"},
        {"id": "2", "name": "test_b"},
        {"id": "3", "name": "test_c"}
      ]);
    });
  });
}
