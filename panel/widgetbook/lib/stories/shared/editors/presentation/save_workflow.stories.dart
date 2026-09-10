import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";
import "package:widgetbook_annotation/widgetbook_annotation.dart" as widgetbook;

@widgetbook.UseCase(name: "Save workflow", type: MutationActivityButton)
Widget mutationActivityButtonStory(BuildContext context) =>
    const FakeApp(child: _SaveWorkflowStory());

@widgetbook.UseCase(name: "Save workflow", type: MutationActivityView)
Widget mutationActivityViewStory(BuildContext context) =>
    const FakeApp(child: _SaveWorkflowStory());

@widgetbook.UseCase(name: "Complete configuration", type: EditorCommitControls)
Widget editorCommitControlsStory(BuildContext context) =>
    const FakeApp(child: _SaveWorkflowStory());

class _SaveWorkflowStory extends StatefulWidget {
  const _SaveWorkflowStory();

  @override
  State<_SaveWorkflowStory> createState() => _SaveWorkflowStoryState();
}

class _SaveWorkflowStoryState extends State<_SaveWorkflowStory> {
  final workspace = LocalWorkSession();
  late final EditorOwnerRegistry registry;
  late final EditorSource source;
  bool uncertain = false;

  @override
  void initState() {
    super.initState();
    registry = EditorOwnerRegistry(workspace: workspace);
    final resource = _WorkflowResource(() => uncertain);
    source = registry.editor(
      ResourceEditorTarget(
        targetId: "host",
        label: "Paper Host configuration",
        resource: resource,
        snapshot: resource.snapshot,
        commitPolicy: EditorCommitPolicy.applyResource,
      ),
    );
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: const Text("Services"),
      actions: [LocalWorkSessionActivityView(controller: workspace)],
    ),
    body: Center(
      child: SizedBox(
        width: 420,
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                "Paper Host",
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 16),
              ComposedEditor(model: PresentationModel.editor(owner: source)),
              SwitchListTile(
                title: const Text("Lose the save response"),
                value: uncertain,
                onChanged: (value) => setState(() => uncertain = value),
              ),
              const Text(
                "Open Save activity to inspect the captured operation and retained draft.",
              ),
            ],
          ),
        ),
      ),
    ),
  );

  @override
  void dispose() {
    registry.dispose();
    workspace.dispose();
    super.dispose();
  }
}

final class _WorkflowResource implements EditableResource {
  _WorkflowResource(this.loseResponse);
  final bool Function() loseResponse;
  EditorSnapshot snapshot = DocumentEditorSnapshot(
    EditorDocument(
      rootType: RecordType(
        fields: const {"target": TypeField(name: "target", type: StringType())},
      ),
      typeCatalog: const TypeCatalog([]),
      confirmedValue: RecordValue({"target": StringValue("paper@*")}),
      revision: 1,
    ),
  );

  @override
  EditorResourceKey get key =>
      const EditorResourceKey(scope: "example", identity: "host");

  @override
  Set<Object> get reservations => {key};

  @override
  Future<EditorSnapshot?> refresh() async => snapshot;

  @override
  MutationIntent prepare(
    EditorSnapshot snapshot,
    EditorCommit commit,
    void Function(TypedMutationResult) accept,
  ) => IndependentMutation(
    PendingCommit(
      resources: reservations,
      prepare: () => PreparedCommit<DataValue>(
        id: Object(),
        label: "Apply Paper Host configuration",
        resources: reservations,
        send: () async {
          await Future<void>.delayed(const Duration(seconds: 2));
          if (loseResponse()) {
            return SubmissionUncertain(
              message: "Lost response",
              cause: StateError("Lost response"),
              stackTrace: StackTrace.current,
            );
          }
          return SubmissionConfirmed(commit.rootValue);
        },
        integrate: (result) async {
          if (result case SubmissionConfirmed(:final value)) {
            final revision = commit.expectedRevision + 1;
            this.snapshot = DocumentEditorSnapshot(
              snapshot.document.copyWith(
                confirmedValue: value,
                revision: revision,
              ),
            );
            accept(MutationSuccess(revision: revision, value: value));
          }
        },
      ),
    ),
  );
}
