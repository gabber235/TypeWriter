import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// The shared prologue of every bound input control.
///
/// Resolves the control's binding, verifies its shape, computes editability
/// once, owns the field's interaction session, and labels the control, so
/// leaf renderers only build the control-specific widget.
///
/// [builder] must not call hooks: the shell returns diagnostics before it
/// runs, which would make hook order unstable.
class BoundControlShell extends HookWidget {
  const BoundControlShell({
    required this.control,
    required this.scope,
    required this.builder,
    this.shapeMismatch,
    this.labeled = true,
    this.nominal = false,
    super.key,
  });

  final BoundControl control;
  final PresentationRenderScope scope;

  /// Whether the built control is wrapped in a [LabeledControl]. Disable for
  /// controls whose chrome is provided elsewhere, such as absorbed headers.
  final bool labeled;
  final bool nominal;

  /// Returns a diagnostic message when the resolved binding does not have
  /// the shape this control requires.
  final String? Function(ResolvedBinding binding)? shapeMismatch;

  final Widget Function(BuildContext context, BoundControlField field) builder;

  @override
  Widget build(BuildContext context) {
    final interaction = useEditorFieldInteraction(scope, control.binding);
    final resolved = scope.resolve(control.binding);
    if (resolved case TypeFailure(:final diagnostics)) {
      return presentationDiagnostic(context, diagnostics);
    }
    final declared = resolved.valueOrNull!;
    final binding = declared.copyWith(
      type: nominal
          ? declared.type.bindingNominal(scope.registry)
          : declared.type.bindingRepresentation(scope.registry),
    );
    if (scope.fieldValue?.call(scope.canonical(control.binding))
            is MixedEditorValue &&
        binding.type.bindingRepresentation(scope.registry) is! RecordType) {
      return LabeledControl(
        control: control,
        scope: scope,
        child: TextButton(
          onPressed: scope.readOnly || !binding.writable
              ? null
              : () {
                  final value = binding.type
                      .createInitialValue(registry: scope.registry)
                      .valueOrNull;
                  if (value != null) scope.update(control.binding, value);
                },
          child: const Text("Mixed values. Set a common value"),
        ),
      );
    }

    if (shapeMismatch?.call(binding) case final message?) {
      return presentationDiagnostic(context, [
        TypeDiagnostic(code: TypeDiagnosticCode.invalidValue, message: message),
      ]);
    }

    final field = BoundControlField._(
      scope: scope,
      binding: binding,
      interaction: interaction,
      enabled: scope.enabled && binding.writable,
      readOnly: scope.readOnly,
    );

    final child = builder(context, field);

    if (!labeled) return child;
    return LabeledControl(control: control, scope: scope, child: child);
  }
}

final class BoundControlField {
  const BoundControlField._({
    required PresentationRenderScope scope,
    required this.binding,
    required this.interaction,
    required this.enabled,
    required this.readOnly,
  }) : _scope = scope;

  final PresentationRenderScope _scope;
  final ResolvedBinding binding;
  final EditorFieldInteraction interaction;
  final bool enabled;
  final bool readOnly;

  bool get editable => enabled && !readOnly;
  bool get locked => !editable;

  void update(DataValue value) => _scope.update(binding.reference, value);
}
