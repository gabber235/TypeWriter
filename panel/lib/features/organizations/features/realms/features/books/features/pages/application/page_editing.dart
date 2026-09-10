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
  }) => _pageEditing().edit({
    id: {
      if (name != null) DataPath.root.field("name"): name.value.asValue,
      if (chapter != null)
        DataPath.root.field("chapter"): chapter.value.asValue,
      if (priority != null)
        DataPath.root.field("priority"): priority.value.asValue,
    },
  });

  Future<TypedMutationResult> editPagesChapter(
    List<Page> pages,
    String oldChapter,
    String newChapter,
  ) => _pageEditing().edit({
    for (final page in pages)
      page.pageId: {
        DataPath.root.field("chapter"): replacePageChapter(
          page.chapter,
          oldChapter,
          newChapter,
        ).asValue,
      },
  });
  PageEditing _pageEditing() {
    final session = readAuthoringSession().notifier;
    return PageEditing(
      session,
      read(localWorkProvider),
      read(resourceRepositoriesProvider)
          .authoring(session.organizationId, session.realmId),
    );
  }
}

/// Adapts page metadata forms to the same retained ownership as other resources.
final class PageEditing {
  PageEditing(this.session, this.workspace, this.repository);
  final AuthoringResourceRepository repository;
  final AuthoringSession session;
  final LocalWork workspace;

  Future<TypedMutationResult> edit(
    Map<skir.RecordId, Map<DataPath, DataValue>> changes,
  ) async {
    final leases = [for (final id in changes.keys) session.acquirePage(id)];
    final owners = EditorOwnerRegistry(workspace: workspace);
    try {
      await Future.wait(leases.map((lease) => lease.ready));
      final edits = <TransactionalEditorSource, Map<DataPath, DataValue>>{};
      for (final entry in changes.entries) {
        final target = this.target(entry.key);
        if (target == null) {
          return unavailableMutation(
            "The page no longer exists",
            targetDeleted: true,
          );
        }
        final owner = owners.editor(target) as TransactionalEditorSource;
        edits[owner] = entry.value;
      }
      final results = await EditorBatch.submit(changes: edits);
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

  EditorTarget? target(skir.RecordId id) {
    final page = session.snapshot.pages[id];
    if (page == null) return null;
    return ResourceEditorTarget(
      targetId: id,
      label: "Page: ${page.name}",
      resource: PageEditorResource(repository, id),
      snapshot: pageEditorSnapshot(page, session.snapshot.sequence ?? 0),
    );
  }
}

wire.AuthoringOperation pagePatchOperation(
  skir.RecordId id,
  EditorCommit commit,
) {
  wire.StringChange? text(String key) {
    final path = DataPath.root.field(key);
    if (!commit.changedPaths.contains(path)) return null;
    return wire.StringChange(
      expected: (path.read(commit.baseValue).valueOrNull! as StringValue).value,
      value: (path.read(commit.rootValue).valueOrNull! as StringValue).value,
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
                (priority.read(commit.baseValue).valueOrNull! as IntegerValue)
                    .value
                    .toInt(),
            value:
                (priority.read(commit.rootValue).valueOrNull! as IntegerValue)
                    .value
                    .toInt(),
          )
        : null,
  );
}

EditorSnapshot pageEditorSnapshot(wire.Page page, int sequence) =>
    DocumentEditorSnapshot(
      EditorDocument(
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
        revision: sequence,
        confirmedValue: Page.fromWire(page).editorValue,
      ),
    );

final class PageEditorResource extends AuthoringEditorResource {
  const PageEditorResource(super.repository, super.id);
  @override
  wire.AuthoringSnapshotScope get scope =>
      wire.AuthoringSnapshotScope.createPage(pageId: id);
  @override
  EditorSnapshot? project(wire.AuthoringSnapshot snapshot) {
    for (final slice in snapshot.slices) {
      if (slice case wire.AuthoringSnapshotSlice_pageWrapper(:final value)) {
        if (value.document case final document?) {
          return pageEditorSnapshot(document.page, snapshot.sequence);
        }
      }
    }
    return null;
  }

  @override
  wire.AuthoringOperation operation(
    EditorSnapshot snapshot,
    EditorCommit commit,
  ) => pagePatchOperation(id, commit);
}
