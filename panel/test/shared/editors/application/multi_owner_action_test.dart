import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

void main() {
  test(
    "nested batch actions validate every resource before changing drafts",
    () {
      const catalog = TypeCatalog([]);
      final type = RecordType(
        fields: const {
          "items": TypeField(
            name: "items",
            type: ListType(element: StringType()),
          ),
        },
      );
      final path = DataPath.root.field("items");
      var reject = true;
      TransactionalEditorSource resource(
        String initial, {
        bool guarded = false,
      }) => TransactionalEditorSource(
        document: EditorDocument(
          rootType: type,
          typeCatalog: catalog,
          confirmedValue: RecordValue({
            "items": ListValue([StringValue(initial)]),
          }),
          revision: 1,
        ),
        debounce: const Duration(days: 1),
        validate: (path, value) => guarded && reject
            ? const EditorMutationResult.conflict()
            : type.validateEditorMutation(path, value),
        commit: (commit) async =>
            TypedMutationResult.success(revision: 2, value: commit.rootValue),
      );
      final first = resource("Oak");

      final second = resource("Pine", guarded: true);
      final group = MultiEditOwner(
        owners: [first, second],
        rootType: type,
        typeCatalog: catalog,
        commitInteractions: (interactions) =>
            interactions.commitIndependently(),
      );
      final session = PresentationSession(
        PresentationModel(
          catalog: catalog,
          root: PresentationNode(
            id: "test",
            element: ColumnElement(children: const []),
          ),
          inputs: {
            const BindingId(8): PresentationInput.edit(group, path: path),
          },
        ),
      );
      addTearDown(first.dispose);
      addTearDown(second.dispose);
      addTearDown(group.dispose);

      addTearDown(session.dispose);
      EditorMutationResult append() => session.executeLocal(
        LocalEditorAction(
          AppendListItemAction(
            target: const BindingReference(bindingId: BindingId(8)),
            value: "New".asStringLiteral,
          ),
        ),
        ExpressionContext(bindings: session.bindings),
        {},
      );
      expect(append(), isA<ConflictingEditorMutation>());
      expect(
        first.value(path).valueOrNull,
        const ListValue([StringValue("Oak")]),
      );
      expect(
        second.value(path).valueOrNull,
        const ListValue([StringValue("Pine")]),
      );
      reject = false;

      expect(append(), isA<AppliedEditorMutation>());
      expect(
        first.value(path).valueOrNull,
        const ListValue([StringValue("Oak"), StringValue("New")]),
      );
      expect(
        second.value(path).valueOrNull,
        const ListValue([StringValue("Pine"), StringValue("New")]),
      );
    },
  );
}
