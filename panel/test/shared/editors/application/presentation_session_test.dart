import "dart:async";
import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

final _type = RecordType(
  fields: const {"name": TypeField(name: "name", type: StringType())},
);
const _catalog = TypeCatalog([]);
RecordValue _name(String name) => RecordValue({"name": StringValue(name)});
final _path = DataPath.root.field("name");
final _root = PresentationNode(
  id: "test",
  element: ColumnElement(children: const []),
);

TransactionalEditorSource _resource(
  String name,
  int revision,
  EditorCommitter commit,
) => TransactionalEditorSource(
  document: EditorDocument(
    rootType: _type,
    typeCatalog: _catalog,
    confirmedValue: _name(name),
    revision: revision,
  ),
  commit: commit,
  debounce: const Duration(days: 1),
);

void main() {
  test(
    "runtime refresh and another resource save preserve the pending name",
    () async {
      final reply = Completer<TypedMutationResult>();
      final identity = _resource("Oak", 12, (_) => reply.future);
      final config = _resource(
        "Paper",
        7,
        (commit) async =>
            TypedMutationResult.success(revision: 8, value: commit.rootValue),
      );
      addTearDown(identity.dispose);
      addTearDown(config.dispose);
      PresentationModel model(String health) => PresentationModel(
        catalog: _catalog,
        root: _root,
        inputs: {
          const BindingId(11): PresentationInput.edit(identity),
          const BindingId(37): PresentationInput.edit(config),
          const BindingId(52): PresentationInput.value(
            type: const StringType(),
            value: EditorValue.ready(StringValue(health)),
          ),
        },
      );
      final session = PresentationSession(model("Active"));
      addTearDown(session.dispose);
      session.update(
        BindingReference(bindingId: const BindingId(11), path: _path),
        const StringValue("Quest"),
      );
      final saving = identity.flush();
      session.refresh(model("Failed"));
      session.update(
        BindingReference(bindingId: const BindingId(37), path: _path),
        const StringValue("Fabric"),
      );
      await config.flush();
      expect(identity.value(_path).valueOrNull, const StringValue("Quest"));
      expect(identity.document.revision, 12);
      expect(config.document.revision, 8);
      reply.complete(
        TypedMutationResult.success(revision: 13, value: _name("Quest")),
      );
      await saving;
      expect(
        session.bindings
            .resolve(const BindingReference(bindingId: BindingId(52)))
            .valueOrNull!
            .value,
        const StringValue("Failed"),
      );
      expect(identity.document.confirmedValue, _name("Quest"));
    },
  );

  test(
    "partial saves retain independent outcomes and retry failed resources only",
    () async {
      final registry = EditorOwnerRegistry();
      addTearDown(registry.dispose);
      var firstSaves = 0;
      var secondSaves = 0;
      EditorTarget target(String id, EditorCommitter commit) =>
          ResourceEditorTarget(
            targetId: id,
            label: id,
            document: EditorDocument(
              rootType: _type,
              typeCatalog: _catalog,
              confirmedValue: _name(id),
              revision: 1,
            ),
            commit: commit,
          );
      final first = registry.editor(
        target("first", (commit) async {
          firstSaves++;
          return TypedMutationResult.success(
            revision: 2,
            value: commit.rootValue,
          );
        }),
      );
      final second = registry.editor(
        target("second", (commit) async {
          secondSaves++;
          return secondSaves == 1
              ? TypedMutationResult.unavailable([
                  const TypeDiagnostic(
                    code: TypeDiagnosticCode.invalidValue,
                    message: "Temporarily unavailable",
                  ),
                ])
              : TypedMutationResult.success(
                  revision: 2,
                  value: commit.rootValue,
                );
        }),
      );
      first.update(_path, const StringValue("edited"));
      second.update(_path, const StringValue("edited"));
      final outcomes = await registry.flush();
      expect(outcomes["first"], isA<MutationSuccess>());
      expect(outcomes["second"], isA<MutationUnavailable>());
      final retry = await registry.flush(failedOnly: true);
      expect(retry.keys, ["second"]);
      expect(retry["second"], isA<MutationSuccess>());
      expect(firstSaves, 1);
      expect(secondSaves, 2);
    },
  );

  test("local owners support cancellation without a save callback", () async {
    final owner = LocalEditor(
      rootType: _type,
      typeCatalog: _catalog,
      value: _name("Oak"),
    );
    addTearDown(owner.dispose);
    final interaction = owner.beginInteraction(_path);
    owner.update(_path, const StringValue("Quest"));
    interaction.cancel();
    expect(owner.value(_path).valueOrNull, const StringValue("Oak"));
    final accepted = owner.beginInteraction(_path);
    owner.update(_path, const StringValue("Quest"));
    await accepted.commit();
    expect(owner.value(_path).valueOrNull, const StringValue("Quest"));
  });

  test("multi owner actions evaluate each target against its own value", () {
    const type = ListType(element: StringType());
    final first = LocalEditor(
      rootType: type,
      typeCatalog: _catalog,
      value: const ListValue([StringValue("Oak")]),
    );
    final second = LocalEditor(
      rootType: type,
      typeCatalog: _catalog,
      value: const ListValue([StringValue("Pine")]),
    );
    final group = MultiEditOwner(
      owners: [first, second],
      rootType: type,
      typeCatalog: _catalog,
    );
    final session = PresentationSession(
      PresentationModel(
        catalog: _catalog,
        root: _root,
        inputs: {const BindingId(9): PresentationInput.edit(group)},
      ),
    );
    addTearDown(first.dispose);
    addTearDown(second.dispose);
    addTearDown(group.dispose);
    addTearDown(session.dispose);
    final result = session.executeLocal(
      LocalEditorAction(
        AppendListItemAction(
          target: const BindingReference(bindingId: BindingId(9)),
          value: "New".asStringLiteral,
        ),
      ),
      ExpressionContext(bindings: session.bindings),
      {},
    );
    expect(result, isA<AppliedEditorMutation>());
    expect(
      first.value(DataPath.root).valueOrNull,
      const ListValue([StringValue("Oak"), StringValue("New")]),
    );
    expect(
      second.value(DataPath.root).valueOrNull,
      const ListValue([StringValue("Pine"), StringValue("New")]),
    );
  });
}
