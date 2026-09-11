import "package:flutter/widgets.dart";
import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

import "../../../support/test_utils.dart";

void main() {
  for (final (initial, option, expected) in [(0, 0, 0), (0, 1, 1), (2, 1, 2)]) {
    testWidgets("numeric selection preserves $initial or initializes $option", (
      tester,
    ) async {
      final owner = TransactionalEditorSource(
        document: EditorDocument(
          rootType: const IntegerType(width: IntegerWidth.signed32),
          typeCatalog: const TypeCatalog([]),
          confirmedValue: IntegerValue(BigInt.from(initial)),
          revision: 1,
        ),
        commitPolicy: EditorCommitPolicy.applyResource,
        commit: (change) async =>
            MutationSuccess(revision: 2, value: change.rootValue),
      );
      addTearDown(owner.dispose);
      final value = IntegerValue(BigInt.from(option)).asLiteral(owner.rootType);
      await tester.pumpTestApp(
        child: ComposedEditor(
          model: PresentationModel(
            catalog: const TypeCatalog([]),
            inputs: {const BindingId(0): PresentationInput.edit(owner)},
            root: PresentationNode(
              id: "number",
              element: SelectInputElement(
                control: const BoundControl(
                  binding: BindingReference(bindingId: BindingId(0)),
                ),
                options: [
                  SelectOption(
                    id: "option",
                    label: "Option".asStringLiteral,
                    value: value,
                  ),
                ],
              ),
            ),
          ),
        ),
      );
      expect(
        owner.value(DataPath.root).valueOrNull,
        IntegerValue(BigInt.from(expected)),
      );
    });
  }

  testWidgets("mixed selections never receive an automatic default", (
    tester,
  ) async {
    final owners = [
      for (final value in ["", "existing"])
        TransactionalEditorSource(
          document: EditorDocument(
            rootType: const StringType(),
            typeCatalog: const TypeCatalog([]),
            confirmedValue: StringValue(value),
            revision: 1,
          ),
          commitPolicy: EditorCommitPolicy.applyResource,
          commit: (change) async =>
              MutationSuccess(revision: 2, value: change.rootValue),
        ),
    ];
    for (final owner in owners) {
      addTearDown(owner.dispose);
    }
    final group = MultiEditOwner(
      owners: owners,
      rootType: const StringType(),
      typeCatalog: const TypeCatalog([]),
      commitInteractions: (interactions) => interactions.commitIndependently(),
    );
    addTearDown(group.dispose);
    await tester.pumpTestApp(
      child: ComposedEditor(
        model: PresentationModel(
          catalog: const TypeCatalog([]),
          inputs: {const BindingId(0): PresentationInput.edit(group)},
          root: PresentationNode(
            id: "mixed",
            element: SelectInputElement(
              control: const BoundControl(
                binding: BindingReference(bindingId: BindingId(0)),
              ),
              options: [
                SelectOption(
                  id: "sole",
                  label: "Only option".asStringLiteral,
                  value: "sole".asStringLiteral,
                ),
              ],
            ),
          ),
        ),
      ),
    );
    expect(owners.map((owner) => owner.value(DataPath.root).valueOrNull), [
      const StringValue(""),
      const StringValue("existing"),
    ]);
  });

  for (final readOnly in [false, true]) {
    testWidgets(
      "expression default becomes a draft only when editable ($readOnly)",
      (tester) async {
        var commits = 0;
        final owner = TransactionalEditorSource(
          document: EditorDocument(
            rootType: const StringType(),
            typeCatalog: const TypeCatalog([]),
            confirmedValue: const StringValue(""),
            revision: 1,
            readOnly: readOnly,
          ),
          commitPolicy: EditorCommitPolicy.applyResource,
          commit: (change) async {
            commits++;
            return MutationSuccess(revision: 2, value: change.rootValue);
          },
        );
        addTearDown(owner.dispose);
        final preferred = ValueNotifier("two");
        addTearDown(preferred.dispose);
        await tester.pumpTestApp(
          child: ValueListenableBuilder(
            valueListenable: preferred,
            builder: (context, value, _) => ComposedEditor(
              model: PresentationModel(
                catalog: const TypeCatalog([]),
                inputs: {
                  const BindingId(0): PresentationInput.edit(owner),
                  const BindingId(7): PresentationInput.value(
                    type: const StringType(),
                    value: EditorValue.ready(StringValue(value)),
                  ),
                },
                root: PresentationNode(
                  id: "selection",
                  element: SelectInputElement(
                    control: const BoundControl(
                      binding: BindingReference(bindingId: BindingId(0)),
                    ),
                    options: [
                      for (final option in ["one", "two"])
                        SelectOption(
                          id: option,
                          label: option.asStringLiteral,
                          value: option.asStringLiteral,
                        ),
                    ],
                    defaultValue: const TypedExpression(
                      resultType: StringType(),
                      expression: BindingExpression(
                        BindingReference(bindingId: BindingId(7)),
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
        );

        expect(
          owner.value(DataPath.root).valueOrNull,
          StringValue(readOnly ? "" : "two"),
        );
        expect(commits, 0);
        preferred.value = "one";
        await tester.pumpAndSettle();
        expect(
          owner.value(DataPath.root).valueOrNull,
          StringValue(readOnly ? "" : "two"),
        );
        expect(tester.takeException(), isNull);
      },
    );
  }
}
