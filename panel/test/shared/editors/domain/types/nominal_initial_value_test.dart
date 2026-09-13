import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

void main() {
  const container = ResolvedTypeRef(
    id: QualifiedTypeId(namespace: "test", name: "Container"),
    revision: 1,
  );
  const string = StringType();
  TypeRegistry registry(DataValue initial) => TypeRegistry(
    TypeCatalog([
      TypeDefinition(
        id: container,
        kind: NominalTypeKind.concrete,
        representation: RecordType(
          fields: {
            "optional": TypeField(
              name: "optional",
              type: NamedType(standardTypeRefs.optionOf(string)),
              initialValue: initial,
            ),
          },
        ),
      ),
    ]),
  );

  test("nominal field defaults resolve with the enclosing catalog", () {
    final initial = PolymorphicValue(
      concreteType: standardTypeRefs.noneOf(string),
      value: const UnitValue(),
    );
    final types = registry(initial);
    expect(types.resolveExact(container), isA<TypeSuccess<ResolvedType>>());
    expect(
      RecordValue({
        "optional": initial,
      }).validateAgainst(NamedType(container), registry: types),
      isEmpty,
    );
  });

  test("invalid nominal defaults are rejected after resolution", () {
    final types = registry(const BooleanValue(true));
    final result = types.resolveExact(container);
    expect(result, isA<TypeFailure<ResolvedType>>());
    expect(
      result.diagnostics.map((issue) => issue.path),
      contains(DataPath.root.field("optional")),
    );
  });
}
