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
  final journal = MutationJournal();
  final workspace = EditorWorkspace();
  late final EditorOwnerRegistry registry;
  late final EditorSource source;
  bool uncertain = false;
  int sequence = 0;

  @override
  void initState() {
    super.initState();
    registry = EditorOwnerRegistry(workspace: workspace, scope: "example");
    source = registry.editor(
      ResourceEditorTarget(
        targetId: "host",
        label: "Paper Host configuration",
        commitPolicy: EditorCommitPolicy.applyResource,
        document: EditorDocument(
          rootType: RecordType(
            fields: const {
              "target": TypeField(name: "target", type: StringType()),
            },
          ),
          typeCatalog: const TypeCatalog([]),
          confirmedValue: RecordValue({"target": StringValue("paper@*")}),
          revision: 1,
        ),
        commit: (commit) async {
          final submission = MutationSubmission<DataValue>(
            id: ++sequence,
            label: "Apply Paper Host configuration",
            send: () async {
              await Future<void>.delayed(const Duration(seconds: 2));
              return uncertain
                  ? SubmissionResult.uncertain(
                      message: "Lost response",
                      cause: StateError("Lost response"),
                      stackTrace: StackTrace.current,
                    )
                  : SubmissionResult.confirmed(commit.rootValue);
            },
          );
          journal.track(submission);
          return switch (await submission.run()) {
            SubmissionConfirmed(:final value) => MutationSuccess(
              revision: commit.expectedRevision + 1,
              value: value,
            ),
            _ => SubmissionException(
              submission,
            ).toMutation((_) async => throw StateError("Replay unsupported")),
          };
        },
      ),
    );
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: const Text("Services"),
      actions: [MutationActivityView(journal: journal, workspace: workspace)],
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
    journal.dispose();
    super.dispose();
  }
}
