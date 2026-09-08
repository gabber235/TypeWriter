import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

const _id = PresentationId(namespace: "test", name: "composed");
const _root = PresentationNode(
  id: "root",
  element: TextElement(
    TypedExpression(
      resultType: StringType(),
      expression: BindingExpression(BindingReference(bindingId: BindingId(7))),
    ),
  ),
);

PresentationRenderScope _scope(Map<BindingId, BindingSnapshot> bindings) =>
    PresentationRenderScope(
      expressions: ExpressionContext(bindings: BindingEnvironment(bindings)),
      registry: TypeRegistry(const TypeCatalog([])),
      budget: const ExpressionBudget(),
      expansionStore: HeaderExpansionStore(),
      setBinding: (_, _, _, _) {},
      executeAction: (_, _, _) {},
      resolvePresentation: (_, _) => null,
    );
BindingSnapshot _text(String value, {bool writable = true}) => BindingSnapshot(
  type: const StringType(),
  value: StringValue(value),
  revision: 1,
  writable: writable,
);

void main() {
  test(
    "input swaps resolve in caller scope and retain canonical destinations",
    () {
      final scope = _scope({
        const BindingId(7): _text("first"),
        const BindingId(9): _text("second"),
      });
      final definition = ResolvedPresentationDefinition(
        id: _id,
        root: _root,
        inputs: const [
          PresentationInputParameter(
            id: BindingId(7),
            name: "first",
            type: StringType(),
            access: PresentationInputAccess.edit,
          ),
          PresentationInputParameter(
            id: BindingId(9),
            name: "second",
            type: StringType(),
            access: PresentationInputAccess.edit,
          ),
        ],
      );
      final bound = scope
          .bindPresentation(definition, {
            const BindingId(7): const BindingReference(bindingId: BindingId(9)),
            const BindingId(9): const BindingReference(bindingId: BindingId(7)),
          })
          .valueOrNull!
          .$2;
      expect(
        bound
            .resolve(const BindingReference(bindingId: BindingId(7)))
            .valueOrNull!
            .value,
        const StringValue("second"),
      );
      expect(
        bound.canonical(const BindingReference(bindingId: BindingId(9))),
        const BindingReference(bindingId: BindingId(7)),
      );
      final nested = bound
          .bindPresentation(
            definition.copyWith(
              id: const PresentationId(namespace: "test", name: "nested"),
            ),
            {
              const BindingId(7): const BindingReference(
                bindingId: BindingId(9),
              ),
              const BindingId(9): const BindingReference(
                bindingId: BindingId(7),
              ),
            },
          )
          .valueOrNull!
          .$2;
      expect(
        nested.canonical(const BindingReference(bindingId: BindingId(9))),
        const BindingReference(bindingId: BindingId(9)),
      );
    },
  );

  test("a read declaration cannot escalate access through an invocation", () {
    final caller = _scope(
      {const BindingId(7): _text("read")},
    ).copyWith(inputAccess: {const BindingId(7): PresentationInputAccess.read});
    final definition = ResolvedPresentationDefinition(
      id: _id,
      root: _root,
      inputs: const [
        PresentationInputParameter(
          id: BindingId(7),
          name: "edit",
          type: StringType(),
          access: PresentationInputAccess.edit,
        ),
      ],
    );
    expect(
      caller.bindPresentation(definition, {
        const BindingId(7): const BindingReference(bindingId: BindingId(7)),
      }),
      isA<TypeFailure>(),
    );
  });

  test(
    "virtual and scoped inputs preserve transaction origin and read access",
    () {
      final scope = _scope({const BindingId(7): _text("value")}).copyWith(
        inputAccess: {const BindingId(7): PresentationInputAccess.read},
      );
      final virtual = scope.withVirtualBinding(
        VirtualBindingHost(
          id: const BindingId(30),
          snapshot: _text("representation"),
          onChanged: (_) {},
        ),
        source: const BindingReference(bindingId: BindingId(7)),
      );
      final aliased = virtual.withAlias(
        const BindingId(31),
        const BindingReference(bindingId: BindingId(30)),
        _text("representation"),
      );
      expect(
        aliased.ownerReference(
          const BindingReference(bindingId: BindingId(31)),
        ),
        const BindingReference(bindingId: BindingId(7)),
      );
      expect(
        aliased.inputAccess[const BindingId(31)],
        PresentationInputAccess.read,
      );
      final local = scope.withVirtualBinding(
        VirtualBindingHost(
          id: const BindingId(50),
          snapshot: _text("local"),
          onChanged: (_) {},
        ),
      );
      expect(
        local.ownerReference(const BindingReference(bindingId: BindingId(50))),
        isNull,
      );
    },
  );

  test("nested invocations route edits into virtual inputs", () {
    DataValue? changed;
    final host = VirtualBindingHost(
      id: const BindingId(50),
      snapshot: _text("before"),
      onChanged: (value) => changed = value,
    );
    final scope = _scope({}).withVirtualBinding(host);
    final definition = ResolvedPresentationDefinition(
      id: _id,
      root: _root,
      inputs: const [
        PresentationInputParameter(
          id: BindingId(7),
          name: "edit",
          type: StringType(),
          access: PresentationInputAccess.edit,
        ),
      ],
    );
    final bound = scope
        .bindPresentation(definition, {
          const BindingId(7): const BindingReference(bindingId: BindingId(50)),
        })
        .valueOrNull!
        .$2;
    bound.update(
      const BindingReference(bindingId: BindingId(7)),
      const StringValue("changed"),
    );
    expect(changed, const StringValue("changed"));
    bound.invoke(
      LocalEditorAction(
        SetValueAction(
          target: const BindingReference(bindingId: BindingId(7)),
          value: "action".asStringLiteral,
        ),
      ),
    );
    expect(changed, const StringValue("action"));
  });

  test("rejects missing inputs and editing through a value input", () {
    final scope = _scope({
      const BindingId(3): _text("observed", writable: false),
    });
    final definition = ResolvedPresentationDefinition(
      id: _id,
      root: _root,
      inputs: const [
        PresentationInputParameter(
          id: BindingId(7),
          name: "edit",
          type: StringType(),
          access: PresentationInputAccess.edit,
        ),
      ],
    );
    expect(scope.bindPresentation(definition, {}), isA<TypeFailure>());
    expect(
      scope.bindPresentation(definition, {
        const BindingId(7): const BindingReference(bindingId: BindingId(3)),
      }),
      isA<TypeFailure>(),
    );
  });

  test("unifies generic inputs consistently and creates a lexical scope", () {
    final scope = _scope({
      const BindingId(1): _text("one"),
      const BindingId(2): BindingSnapshot(
        type: const BooleanType(),
        value: const BooleanValue(true),
        revision: 1,
      ),
    });
    final definition = ResolvedPresentationDefinition(
      id: _id,
      root: _root,
      inputs: const [
        PresentationInputParameter(
          id: BindingId(7),
          name: "first",
          type: ParameterType("T"),
        ),
        PresentationInputParameter(
          id: BindingId(9),
          name: "second",
          type: ParameterType("T"),
        ),
      ],
    );
    expect(
      scope.bindPresentation(definition, {
        const BindingId(7): const BindingReference(bindingId: BindingId(1)),
        const BindingId(9): const BindingReference(bindingId: BindingId(2)),
      }),
      isA<TypeFailure>(),
    );
    final bound = scope
        .bindPresentation(definition, {
          const BindingId(7): const BindingReference(bindingId: BindingId(1)),
          const BindingId(9): const BindingReference(bindingId: BindingId(1)),
        })
        .valueOrNull!
        .$2;
    expect(
      bound.resolve(const BindingReference(bindingId: BindingId(1))),
      isA<TypeFailure>(),
    );
  });
}
