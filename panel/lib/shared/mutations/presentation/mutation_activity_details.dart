part of "mutation_activity_button.dart";

class _ActivityDetails extends StatelessWidget {
  const _ActivityDetails({required this.workspace, required this.onClose});
  final VoidCallback onClose;
  final LocalWork workspace;

  Future<void> _retry(MutationSubmission<Object?> submission) async {
    final owners = workspace.resources.values.where(
      (resource) =>
          resource.source.saveState(DataPath.root).submissionId ==
          submission.id,
    );
    if (owners.isNotEmpty) {
      await owners.first.source.flush();
    } else {
      await submission.run();
    }
  }

  Future<void> _review(BuildContext context, EditorResource resource) async {
    final key = workspace.resources.entries
        .where((entry) => identical(entry.value, resource))
        .first
        .key;
    workspace.retain(key);
    try {
      await showDialog<void>(
        context: context,
        builder: (context) => AlertDialog(
          title: Text(resource.label),
          content: SizedBox(
            width: 560,
            child: SingleChildScrollView(
              child: ComposedEditor(
                model: PresentationModel.editor(owner: resource.source),
              ),
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text("Close"),
            ),
          ],
        ),
      );
    } finally {
      workspace.release(key);
    }
  }

  @override
  Widget build(BuildContext context) => ListenableBuilder(
    listenable: workspace,
    builder: (context, _) {
      final drafts = workspace.resources.values
          .where((entry) => entry.source.hasWork)
          .toList();
      return Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Padding(
            padding: EdgeInsets.fromLTRB(
              context.spacing.space4,
              context.spacing.space3,
              context.spacing.space2,
              context.spacing.space2,
            ),
            child: Row(
              children: [
                Icon(
                  Icons.cloud_upload_outlined,
                  color: context.colors.contentSecondary,
                  size: 20,
                ),
                SizedBox(width: context.spacing.space2),
                Expanded(
                  child: Text(
                    "Save activity",
                    style: context.theme.textTheme.titleMedium,
                  ),
                ),
                IconButton(
                  tooltip: "Close",
                  onPressed: onClose,
                  icon: const Icon(Icons.close, size: 20),
                ),
              ],
            ),
          ),
          Flexible(
            child: ListView(
              shrinkWrap: true,
              padding: EdgeInsets.fromLTRB(
                context.spacing.space3,
                0,
                context.spacing.space3,
                context.spacing.space3,
              ),
              children: [
                for (final resource in drafts)
                  _ActivityCard(
                    title: resource.label,
                    message:
                        resource.source.commitPolicy ==
                            EditorCommitPolicy.applyResource
                        ? "Configuration draft"
                        : "Unsaved changes",
                    phase: MutationActivityPhase.resolve(const [], [resource]),
                    actions: [
                      if (resource.destination case final destination?) ...[
                        if (!destination.isCurrent)
                          TextButton.icon(
                            icon: const Icon(Icons.arrow_outward, size: 16),
                            onPressed: () async {
                              onClose();
                              await destination.open();
                            },
                            label: const Text("Return to draft"),
                          ),
                      ] else
                        TextButton.icon(
                          icon: const Icon(Icons.edit_outlined, size: 16),
                          onPressed: () => _review(context, resource),
                          label: const Text("Review draft"),
                        ),
                      TextButton(
                        onPressed:
                            resource.source.readOnly ||
                                {
                                  EditorSavePhase.saving,
                                  EditorSavePhase.uncertain,
                                }.contains(
                                  resource.source
                                      .saveState(DataPath.root)
                                      .phase,
                                )
                            ? null
                            : resource.source.discardDraft,
                        child: const Text("Discard"),
                      ),
                    ],
                  ),
                for (final submission in workspace.submissions)
                  _ActivityCard(
                    title: submission.label,
                    phase: MutationActivityPhase.resolve([
                      submission,
                    ], const []),
                    message: submission.integrationError != null
                        ? "Saved. Local refresh failed."
                        : submission.sending
                        ? "Saving"
                        : switch (submission.result) {
                            SubmissionConfirmed() => "Saved",
                            SubmissionRejected(:final message) => message,
                            SubmissionUncertain() =>
                              "Outcome unknown. Verify before submitting again.",
                            SubmissionNotSubmitted(:final message) => message,
                            null => "Ready",
                          },
                    actions: [
                      if (submission.integrationError != null)
                        TextButton.icon(
                          icon: const Icon(Icons.refresh, size: 16),
                          onPressed: submission.run,
                          label: const Text("Refresh saved result"),
                        )
                      else if (submission.canReplay)
                        TextButton.icon(
                          icon: const Icon(Icons.refresh, size: 16),
                          onPressed: () => _retry(submission),
                          label: const Text("Retry captured request"),
                        )
                      else if (!submission.sending &&
                          submission.result is! SubmissionUncertain)
                        TextButton(
                          onPressed: () => workspace.dismiss(submission.id),
                          child: const Text("Dismiss"),
                        ),
                    ],
                  ),
                if (drafts.isEmpty && workspace.submissions.isEmpty)
                  Padding(
                    padding: EdgeInsets.all(context.spacing.space4),
                    child: Text(
                      "No pending changes",
                      style: context.theme.textTheme.bodyMedium?.copyWith(
                        color: context.colors.contentSecondary,
                      ),
                    ),
                  ),
              ],
            ),
          ),
        ],
      );
    },
  );
}
