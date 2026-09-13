import "dart:async";

import "package:flutter/widgets.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Coordinates one rendered field with the editor owner's interaction gate.
///
/// The coordinator owns only whether this field has started a session. The
/// owner still owns the draft, commit policy, and persistence. Normal exits
/// commit on blur, dismiss, or unmount; only an intentional cancel, such as
/// [CancelIntent], restores the owner's pre interaction boundary. Repeated
/// begin, commit, and cancel calls are safe for widget callbacks.
final class EditorFieldInteraction {
  EditorInteractionSession? _session;
  EditorInteractionSession? Function()? _start;

  bool get active => _session != null;

  void begin() => _session ??= _start?.call();

  void commit() {
    final active = _session;
    _session = null;
    if (active != null) unawaited(active.commit());
  }

  void cancel() {
    final active = _session;
    _session = null;
    active?.cancel();
  }
}

EditorFieldInteraction useEditorFieldInteraction(
  PresentationRenderScope scope,
  BindingReference reference,
) {
  final interaction = useMemoized(EditorFieldInteraction.new)
    .._start = () => scope.beginInteraction(reference);
  useEffect(() {
    return () {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        interaction.commit();
      });
    };
  }, [reference]);
  return interaction;
}
