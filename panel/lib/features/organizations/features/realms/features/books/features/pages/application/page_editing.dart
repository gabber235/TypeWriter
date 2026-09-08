import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

extension PageEditingRef on WidgetRef {
  Future<TypedMutationResult> editPage({
    required skir.RecordId id,
    wire.StringChange? name,
    wire.StringChange? chapter,
    wire.Int32Change? priority,
  }) =>
      PageEditing(
        readAuthoringSession().notifier,
        read(editorWorkspaceProvider),
      ).edit({
        id: {
          if (name != null)
            DataPath.root.field("name"): StringValue(name.value),
          if (chapter != null)
            DataPath.root.field("chapter"): StringValue(chapter.value),
          if (priority != null)
            DataPath.root.field("priority"): IntegerValue(
              BigInt.from(priority.value),
            ),
        },
      });

  Future<TypedMutationResult> editPagesChapter(
    List<Page> pages,
    String oldChapter,
    String newChapter,
  ) =>
      PageEditing(
        readAuthoringSession().notifier,
        read(editorWorkspaceProvider),
      ).edit({
        for (final page in pages)
          page.pageId: {
            DataPath.root.field("chapter"): StringValue(
              replacePageChapter(page.chapter, oldChapter, newChapter),
            ),
          },
      });
}

/// Adapts page metadata forms to the same retained ownership as other resources.
final class PageEditing {
  PageEditing(this.session, this.workspace);
  final AuthoringSession session;
  final EditorWorkspace workspace;

  Future<TypedMutationResult> edit(
    Map<skir.RecordId, Map<DataPath, DataValue>> changes,
  ) async {
    final leases = [for (final id in changes.keys) session.acquirePage(id)];
    final owners = EditorOwnerRegistry(
      workspace: workspace,
      scope: (session.organizationId, session.realmId),
    );
    try {
      await Future.wait(leases.map((lease) => lease.ready));
      final identities = <TransactionalEditorSource, skir.RecordId>{};
      final edits = <TransactionalEditorSource, Map<DataPath, DataValue>>{};
      for (final entry in changes.entries) {
        final target = _target(entry.key);
        if (target == null)
          return unavailableMutation(
            "The page no longer exists",
            targetDeleted: true,
          );
        final owner = owners.editor(target) as TransactionalEditorSource;
        identities[owner] = entry.key;
        edits[owner] = entry.value;
      }
      final results = await EditorBatch.submit(
        changes: edits,
        send: (commits) async {
          Future<Map<TransactionalEditorSource, TypedMutationResult>> accept(
            wire.ApplyAuthoringBatchResponse response,
          ) async => {
            for (final entry in commits.entries)
              entry.key: await _accept(
                identities[entry.key]!,
                entry.value,
                response,
              ),
          };
          try {
            return await accept(
              await session.apply([
                for (final entry in commits.entries)
                  _operation(identities[entry.key]!, entry.value),
              ]),
            );
          } on SubmissionException<wire.ApplyAuthoringBatchResponse> catch (
            error
          ) {
            return {
              for (final entry in commits.entries)
                entry.key: error.toMutation(
                  (response) async => (await accept(response))[entry.key]!,
                ),
            };
          }
        },
      );
      return results.values
              .where((result) => result is! MutationSuccess)
              .firstOrNull ??
          results.values.firstOrNull ??
          invalidMutation("No page edits were supplied");
    } finally {
      owners.dispose();
      for (final lease in leases) {
        lease.release();
      }
    }
  }

  EditorDocument? _document(skir.RecordId id) {
    final page = session.snapshot.pages[id];
    if (page == null) return null;
    return EditorDocument(
      rootType: RecordType(
        fields: const {
          "name": TypeField(name: "name", type: StringType()),
          "chapter": TypeField(name: "chapter", type: StringType()),
          "priority": TypeField(
            name: "priority",
            type: IntegerType(width: IntegerWidth.signed32),
          ),
        },
      ),
      typeCatalog: const TypeCatalog([]),
      revision: session.snapshot.sequence ?? 0,
      confirmedValue: RecordValue({
        "name": StringValue(page.name),
        "chapter": StringValue(page.chapter),
        "priority": IntegerValue(BigInt.from(page.priority)),
      }),
    );
  }

  EditorTarget? _target(skir.RecordId id) {
    final document = _document(id);
    if (document == null) return null;
    return ResourceEditorTarget(
      targetId: id,
      label: "Page: ${session.snapshot.pages[id]!.name}",
      document: document,
      updates: session
          .watchSnapshots(() => session.acquirePage(id))
          .map((_) => _document(id)),
      commit: (commit) async {
        try {
          return await _accept(
            id,
            commit,
            await session.apply([_operation(id, commit)]),
          );
        } on SubmissionException<wire.ApplyAuthoringBatchResponse> catch (
          error
        ) {
          return error.toMutation((response) => _accept(id, commit, response));
        }
      },
    );
  }

  Future<TypedMutationResult> _accept(
    skir.RecordId id,
    EditorCommit commit,
    wire.ApplyAuthoringBatchResponse response,
  ) async => switch (response) {
    wire.ApplyAuthoringBatchResponse_appliedWrapper(:final value) =>
      MutationSuccess(revision: value.sequence, value: commit.rootValue),
    wire.ApplyAuthoringBatchResponse_conflictWrapper() => switch (_document(
      id,
    )) {
      final EditorDocument actual => MutationConflict(
        expectedRevision: commit.expectedRevision,
        actualRevision: actual.revision,
        actualValue: actual.confirmedValue,
      ),
      null => unavailableMutation(
        "The page no longer exists",
        targetDeleted: true,
      ),
    },
    _ => response.toMutationFailure(
      unavailableMessage: "The page could not be saved",
    ),
  };

  wire.AuthoringOperation _operation(skir.RecordId id, EditorCommit commit) {
    wire.StringChange? text(String key) {
      final path = DataPath.root.field(key);
      if (!commit.changedPaths.contains(path)) return null;
      return wire.StringChange(
        expected:
            (path.read(commit.baseValue).valueOrNull as StringValue).value,
        value: (path.read(commit.rootValue).valueOrNull as StringValue).value,
      );
    }

    final priority = DataPath.root.field("priority");
    return wire.AuthoringOperation.createPatchPage(
      id: id,
      book: null,
      name: text("name"),
      chapter: text("chapter"),
      priority: commit.changedPaths.contains(priority)
          ? wire.Int32Change(
              expected:
                  (priority.read(commit.baseValue).valueOrNull as IntegerValue)
                      .value
                      .toInt(),
              value:
                  (priority.read(commit.rootValue).valueOrNull as IntegerValue)
                      .value
                      .toInt(),
            )
          : null,
    );
  }
}
