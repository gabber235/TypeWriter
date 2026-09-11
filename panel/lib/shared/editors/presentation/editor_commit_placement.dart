import "package:flutter/foundation.dart";
import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Marks retained presentation branches that are currently accessible.
class PresentationActivity extends InheritedWidget {
  const PresentationActivity({
    required this.active,
    required super.child,
    super.key,
  });
  final bool active;

  static bool of(BuildContext context) =>
      context
          .dependOnInheritedWidgetOfExactType<PresentationActivity>()
          ?.active ??
      true;

  @override
  bool updateShouldNotify(PresentationActivity oldWidget) =>
      active != oldWidget.active;
}

/// Counts active placements without assigning ownership by widget build order.
/// Publishes the complete mounted set after the frame; registrations own no draft.
class EditorCommitPlacements extends ChangeNotifier {
  final Map<Object, Set<EditorSource>> _pending = {};
  Map<EditorSource, Set<Object>> _active = {};
  bool _scheduled = false;
  bool _disposed = false;

  int count(EditorSource owner) => _active[owner]?.length ?? 0;

  void register(Object token, Set<EditorSource> owners) {
    if (setEquals(_pending[token] ?? {}, owners)) return;
    if (owners.isEmpty) {
      _pending.remove(token);
    } else {
      _pending[token] = owners;
    }
    if (_scheduled || _disposed) return;
    _scheduled = true;
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_disposed) return;
      _scheduled = false;
      final next = <EditorSource, Set<Object>>{};
      for (final entry in _pending.entries) {
        for (final owner in entry.value) {
          next.putIfAbsent(owner, () => {}).add(entry.key);
        }
      }
      _active = next;
      notifyListeners();
    });
  }

  @override
  void dispose() {
    _disposed = true;
    _pending.clear();
    _active.clear();
    super.dispose();
  }
}

/// Supplies transaction owners explicitly at the editor boundary.
class EditorCommitPlacementScope extends InheritedWidget {
  const EditorCommitPlacementScope({
    required this.placements,
    required this.resolve,
    required super.child,
    this.labels = const {},
    super.key,
  });
  final Map<EditOwner, String> labels;
  final EditorCommitPlacements placements;
  final Set<EditorSource>? Function(BindingReference) resolve;

  @override
  bool updateShouldNotify(EditorCommitPlacementScope oldWidget) => true;
}

/// Renders existing commit controls at a presentation element's location.
class EditorCommitPlacement extends StatefulWidget {
  const EditorCommitPlacement({
    required this.binding,
    required this.enabled,
    super.key,
  });
  final BindingReference binding;
  final bool enabled;

  @override
  State<EditorCommitPlacement> createState() => _EditorCommitPlacementState();
}

class _EditorCommitPlacementState extends State<EditorCommitPlacement> {
  EditorCommitPlacements? _placements;
  Set<EditorSource>? _owners;
  Map<EditOwner, String> _labels = {};

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _resolve();
  }

  @override
  void didUpdateWidget(EditorCommitPlacement oldWidget) {
    super.didUpdateWidget(oldWidget);
    _resolve();
  }

  void _resolve() {
    final scope = context
        .dependOnInheritedWidgetOfExactType<EditorCommitPlacementScope>();
    if (_placements != scope?.placements) {
      _placements?.register(this, {});
      _placements = scope?.placements;
    }
    _owners = scope?.resolve(widget.binding);
    _labels = scope?.labels ?? {};
    _placements?.register(
      this,
      PresentationActivity.of(context) ? _owners ?? {} : {},
    );
  }

  @override
  void dispose() {
    _placements?.register(this, {});
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final placements = _placements;
    final owners = _owners;
    if (placements == null || owners == null) {
      return presentationDiagnostic(context, [
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidPresentation,
          message: "Commit controls require an editable transaction binding",
        ),
      ]);
    }
    return ListenableBuilder(
      listenable: placements,
      builder: (context, _) => Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          for (final owner in owners)
            if (placements.count(owner) > 1)
              presentationDiagnostic(context, [
                const TypeDiagnostic(
                  code: TypeDiagnosticCode.invalidPresentation,
                  message: "Duplicate commit controls; use the controls at the editor end",
                ),
              ])
            else if (placements.count(owner) == 1)
              EditorCommitControls(
                owner: owner,
                enabled: widget.enabled,
                label: owners.length > 1 ? _labels[owner] : null,
              ),
        ],
      ),
    );
  }
}
