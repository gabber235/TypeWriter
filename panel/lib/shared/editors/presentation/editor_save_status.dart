import "dart:async";

import "package:flutter/material.dart";
import "package:iconify_flutter_plus/icons/material_symbols.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Communicates save progress and offers recovery for a failed owner write.
///
/// [state] is a projection of the owner persistence lifecycle. Retry delegates
/// to the owner, while conflict actions choose the remote or local resolution
/// supplied by the owner. Successful feedback is transient, so this widget
/// clears it after [savedFeedbackDuration] and cancels its timer when replaced
/// or unmounted.
class EditorSaveStatus extends StatefulWidget {
  const EditorSaveStatus({
    required this.state,
    this.onRetry,
    this.onUseRemote,
    this.onKeepLocal,
    super.key,
  });

  final EditorSaveState state;
  final Future<void> Function()? onRetry;
  final Future<void> Function()? onUseRemote;
  final Future<void> Function()? onKeepLocal;

  @override
  State<EditorSaveStatus> createState() => _EditorSaveStatusState();
}

class _EditorSaveStatusState extends State<EditorSaveStatus> {
  Timer? _expiry;
  bool _expired = false;

  @override
  void initState() {
    super.initState();
    _scheduleExpiry();
  }

  @override
  void didUpdateWidget(EditorSaveStatus oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.state.phase != widget.state.phase ||
        oldWidget.state.submissionId != widget.state.submissionId) {
      _scheduleExpiry();
    }
  }

  void _scheduleExpiry() {
    _expiry?.cancel();
    _expired = false;
    if (widget.state.phase == EditorSavePhase.saved) {
      _expiry = Timer(savedFeedbackDuration, () {
        if (mounted) setState(() => _expired = true);
      });
    }
  }

  @override
  void dispose() {
    _expiry?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final state = widget.state;
    final label = _expired
        ? null
        : switch (state.phase) {
            EditorSavePhase.idle => null,
            EditorSavePhase.pending => "Pending",
            EditorSavePhase.saving => "Saving",
            EditorSavePhase.saved => "Saved",
            EditorSavePhase.failed => "Save failed",
            EditorSavePhase.uncertain => "Outcome unknown",
            EditorSavePhase.conflict => "Changed elsewhere",
            EditorSavePhase.repeatedContention =>
              "Changed repeatedly elsewhere",
            EditorSavePhase.deletedElsewhere => "Deleted elsewhere",
          };
    Widget? child;
    if (label == null) {
      child = null;
    } else {
      final color = switch (state.phase) {
        EditorSavePhase.uncertain ||
        EditorSavePhase.failed ||
        EditorSavePhase.conflict ||
        EditorSavePhase.repeatedContention ||
        EditorSavePhase.deletedElsewhere => Theme.of(context).colorScheme.error,
        _ => Theme.of(context).colorScheme.onSurfaceVariant,
      };
      child = Semantics(
        liveRegion: true,
        label: label,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          spacing: context.spacing.space2,
          children: [
            if (state.phase == .conflict)
              Row(
                mainAxisSize: MainAxisSize.min,
                spacing: context.spacing.space2,
                children: [
                  LoadingButton.filledIcon(
                    onPressed: widget.onUseRemote,
                    style: TextButton.styleFrom(
                      backgroundColor: context.theme.colorScheme.error,
                    ),
                    icon: const Icones(MaterialSymbols.arrow_upward),
                    label: const Text("Use theirs"),
                  ),
                  LoadingButton.filledIcon(
                    onPressed: widget.onKeepLocal,
                    icon: const Icones(MaterialSymbols.arrow_downward),
                    label: const Text("Keep mine"),
                  ),
                ],
              ),
            Row(
              mainAxisSize: MainAxisSize.min,
              spacing: context.spacing.space2,
              children: [
                Text(
                  label,
                  style: Theme.of(context).textTheme.labelSmall
                      ?.copyWith(color: color),
                ),
                if (state.canRetry)
                  LoadingIconButton(
                    icon: const Icones(MaterialSymbols.refresh),
                    onPressed: widget.onRetry,
                  ),
              ],
            ),
          ],
        ),
      );
    }

    return ElasticMessageSwitcher(child: child);
  }
}
