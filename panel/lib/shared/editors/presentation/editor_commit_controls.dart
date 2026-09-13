import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Exposes apply and cancel for an owner whose draft is committed explicitly.
///
/// The [EditorSource] owns the draft and performs persistence. This widget
/// only enables apply when work is present, the draft has no diagnostics, and
/// the owner is writable. Cancel discards the whole draft, not one field.
/// When the owner is saving or unavailable, its own state remains authoritative
/// and the controls follow that state on the next rebuild.
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
                    spacing: context.spacing.space2,
                    children: [
                      if (label != null)
                        Text(
                          label!,
                          style: Theme.of(context).textTheme.labelMedium,
                        ),
                      for (final issue in issues)
                        Text(
                          issue.message,
                          style: context.theme.textTheme.bodySmall?.copyWith(
                            color: Theme.of(context).colorScheme.error,
                          ),
                        ),
                      Wrap(
                        spacing: context.spacing.space2,
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
