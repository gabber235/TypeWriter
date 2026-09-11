import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

const _binding = BindingReference(bindingId: BindingId(0));

final _recordType = RecordType(
  fields: const {
    "title": TypeField(name: "title", type: StringType()),
    "color": TypeField(
      name: "color",
      type: IntegerType(width: IntegerWidth.signed32),
    ),
  },
);

void main() {
  test("snapshot inspection resolves a ready child path", () {
    final environment = BindingEnvironment({
      _binding.bindingId: BindingSnapshot(
        type: _recordType,
        value: RecordValue({
          "title": const StringValue("Earth"),
          "color": IntegerValue(BigInt.from(4)),
        }),
        revision: 7,
      ),
    });

    final inspected = environment.inspect(
      _binding.at(DataPath.root.field("title")),
    );

    expect(inspected, isA<TypeSuccess<InspectedBinding>>());
    expect(inspected.valueOrNull!.type, const StringType());
    expect(
      inspected.valueOrNull!.value.valueOrNull,
      const StringValue("Earth"),
    );
    expect(inspected.valueOrNull!.revision, 7);
  });

  test("mixed value input keeps child type without inventing a value", () {
    final environment = BindingEnvironment({
      _binding.bindingId: EditorValueBindingSource(
        type: _recordType,
        value: const EditorValue.mixed(),
        revision: 3,
      ),
    });

    final inspected = environment.inspect(
      _binding.at(DataPath.root.field("title")),
    );

    expect(inspected.valueOrNull!.type, const StringType());
    expect(inspected.valueOrNull!.value, isA<MixedEditorValue>());
    expect(environment.resolve(_binding), isA<TypeFailure<ResolvedBinding>>());
  });

  test("projected source preserves path state and metadata", () {
    final environment = BindingEnvironment({
      _binding.bindingId: BindingSnapshot(
        type: _recordType,
        value: RecordValue({
          "title": const StringValue("Earth"),
          "color": IntegerValue(BigInt.from(4)),
        }),
        revision: 9,
        writable: false,
      ),
    });

    final projected = environment
        .project(_binding.at(DataPath.root.field("title")))
        .valueOrNull!;
    final inspected = projected.inspect(DataPath.root);

    expect(inspected.valueOrNull!.type, const StringType());
    expect(
      inspected.valueOrNull!.value.valueOrNull,
      const StringValue("Earth"),
    );
    expect(projected.revision, 9);
    expect(projected.writable, isFalse);
  });

  test("missing and mixed bindings have distinct diagnostics", () {
    final missing = const BindingEnvironment({}).resolve(_binding);
    final mixed = BindingEnvironment({
      _binding.bindingId: EditorValueBindingSource(
        type: const StringType(),
        value: const EditorValue.mixed(),
        revision: 0,
      ),
    }).resolve(_binding);

    expect(missing.diagnostics.single.message, "Binding is not available");
    expect(mixed.diagnostics.single.message, "Binding has different values");
  });
}
