import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Applies or discards the complete draft of an explicitly submitted resource.
class EditorCommitControls extends StatelessWidget {
  const EditorCommitControls({
    required this.owner,
    this.label,
    this.enabled = true,
    super.key,
  });

  final EditorSource owner;
  final String? label;
  final bool enabled;

  @override
  Widget build(BuildContext context) => ListenableBuilder(
    listenable: owner,
    builder: (context, _) {
      final issues = owner.draftDiagnostics;
      return IgnorePointer(
        ignoring: !owner.hasWork,
        child: ElasticSwitcher(
          child: !owner.hasWork
              ? null
              : SizedBox(
                  key: const ValueKey("commit"),
                  width: double.infinity,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.end,
                    spacing: 8,
                    children: [
                      if (label != null)
                        Text(
                          label!,
                          style: Theme.of(context).textTheme.labelMedium,
                        ),
                      for (final issue in issues)
                        Text(
                          issue.message,
                          style: TextStyle(
                            color: Theme.of(context).colorScheme.error,
                          ),
                        ),
                      Wrap(
                        spacing: 8,
                        children: [
                          TextButton(
                            onPressed:
                                !enabled || !owner.hasWork || owner.readOnly
                                ? null
                                : owner.discardDraft,
                            child: const Text("Cancel"),
                          ),
                          LoadingButton.filled(
                            onPressed:
                                !enabled ||
                                    !owner.hasWork ||
                                    owner.readOnly ||
                                    issues.isNotEmpty
                                ? null
                                : () async {
                                    await owner.flush();
                                  },
                            child: const Text("Apply"),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
        ),
      );
    },
  );
}
