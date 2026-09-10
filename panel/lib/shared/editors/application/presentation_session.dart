import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Owns presentation subscriptions and routing, never the supplied editors.
/// Refreshing a value binding leaves editor state and interaction sessions intact.
final class PresentationSession extends ChangeNotifier {
  PresentationSession(PresentationModel model) : _model = model {
    _attach();
  }
  PresentationModel _model;
  PresentationModel get model => _model;
  final Set<EditOwner> _owners = {};
  int _generation = 0;
  int get generation => _generation;

  void refresh(PresentationModel model) {
    for (final owner in _owners) {
      owner.removeListener(_ownerChanged);
    }
    _owners.clear();
    _model = model;
    _generation++;

    _attach();
    notifyListeners();
  }

  void _ownerChanged() {
    _generation++;
    notifyListeners();
  }

  void _attach() {
    for (final input in _model.inputs.values) {
      if (input case PresentationEditInput(:final owner)) {
        if (_owners.add(owner)) owner.addListener(_ownerChanged);
      }
    }
  }

  EditOwner? owner(BindingReference reference) =>
      switch (_model.inputs[reference.bindingId]) {
        PresentationEditInput(:final owner) => owner,
        _ => null,
      };

  EditorValue? fieldValue(BindingReference reference) {
    final input = model.inputs[reference.bindingId];
    if (input is! PresentationEditInput) return null;
    return input.owner.value(input.path.followedBy(reference.path));
  }

  BindingEnvironment get bindings {
    final registry = TypeRegistry(model.catalog);
    final snapshots = <BindingId, BindingSnapshot>{};
    for (final entry in model.inputs.entries) {
      final (type, value, writable) = switch (entry.value) {
        PresentationValueInput(:final type, :final value) => (
          type,
          value,
          false,
        ),
        PresentationEditInput(:final owner, :final path) => (
          owner.rootType.resolvePath(path, registry: registry).valueOrNull ??
              owner.rootType,
          owner.value(path),
          !owner.readOnly,
        ),
      };
      final visible =
          value.valueOrNull ??
          (value is MixedEditorValue
              ? type.createInitialValue(registry: registry).valueOrNull
              : null);
      if (visible case final value?) {
        snapshots[entry.key] = BindingSnapshot(
          type: type,
          value: value,
          revision: _generation,
          writable: writable,
        );
      }
    }
    return BindingEnvironment(snapshots);
  }

  EditorMutationResult update(
    BindingReference reference,
    DataValue value, {
    EditorStructuralMutation? structuralMutation,
  }) {
    final target = owner(reference);
    if (target == null || target.readOnly) {
      return EditorMutationResult.invalid([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidPath,
          message: "Binding has no writable edit owner",
        ),
      ]);
    }
    final input = model.inputs[reference.bindingId]! as PresentationEditInput;
    return target.update(
      input.path.followedBy(reference.path),
      value,
      structuralMutation: structuralMutation?.prefixedBy(input.path),
    );
  }

  EditorInteractionSession? beginInteraction(BindingReference reference) {
    final input = model.inputs[reference.bindingId];
    return input is PresentationEditInput
        ? input.owner.beginInteraction(input.path.followedBy(reference.path))
        : null;
  }

  EditorMutationResult executeLocal(
    LocalEditorAction action,
    ExpressionContext context,
    Map<BindingId, BindingReference> aliases,
  ) {
    final destination = action.action.mutationReference.canonicalizedWith(
      aliases,
    );
    final target = owner(destination);
    if (target is MultiEditOwner) {
      final input =
          model.inputs[destination.bindingId]! as PresentationEditInput;
      final destinationPath = input.path.followedBy(destination.path);
      final prepared = <(EditOwner, DataValue, EditorStructuralMutation?)>[];
      for (final member in target.owners) {
        final snapshots = {...context.bindings.bindings};
        for (final entry in snapshots.entries.toList()) {
          final address = BindingReference(
            bindingId: entry.key,
          ).canonicalizedWith(aliases);
          if (address.bindingId != destination.bindingId) continue;
          final input =
              model.inputs[address.bindingId]! as PresentationEditInput;
          final value = member
              .value(input.path.followedBy(address.path))
              .valueOrNull;

          if (value == null) return const EditorMutationResult.conflict();
          snapshots[entry.key] = entry.value.copyWith(
            value: value,
            writable: !member.readOnly,
          );
        }
        final memberContext = context.copyWith(
          bindings: BindingEnvironment(snapshots),
        );
        final registry = TypeRegistry(member.typeCatalog);

        final result = action.execute(memberContext, registry: registry);
        if (result case LocalMutationInvalid(:final diagnostics)) {
          return EditorMutationResult.invalid(diagnostics);
        }

        final local = action.action.mutationReference;
        final value = local.path
            .read((result as LocalMutationApplied).value)
            .valueOrNull;

        if (value == null) return const EditorMutationResult.conflict();
        final prefix = BindingReference(
          bindingId: local.bindingId,
        ).canonicalizedWith(aliases).path;

        final validation = member.validate(destinationPath, value);

        if (validation is! AppliedEditorMutation) return validation;
        prepared.add((
          member,
          validation.value,
          structuralMutationFor(
            action.action,
            memberContext,
            result,
            registry,
          )?.prefixedBy(input.path.followedBy(prefix)),
        ));
      }
      for (final (member, value, mutation) in prepared) {
        final result = member.update(
          destinationPath,
          value,
          structuralMutation: mutation,
        );
        if (result is! AppliedEditorMutation) return result;
      }
      return EditorMutationResult.applied(prepared.first.$2);
    }
    final registry = TypeRegistry(model.catalog);

    final result = action.execute(context, registry: registry);
    if (result case LocalMutationInvalid(:final diagnostics)) {
      return EditorMutationResult.invalid(diagnostics);
    }

    final applied = result as LocalMutationApplied;

    final local = action.action.mutationReference;

    final canonical = local.canonicalizedWith(aliases);

    final value = local.path.read(applied.value);
    if (value case TypeFailure(:final diagnostics)) {
      return EditorMutationResult.invalid(diagnostics);
    }
    final prefix = DataPath.root.followedBy(
      BindingReference(
        bindingId: local.bindingId,
      ).canonicalizedWith(aliases).path,
    );
    final structural = structuralMutationFor(
      action.action,
      context,
      result,
      registry,
    )?.prefixedBy(prefix);
    return update(
      canonical,
      value.valueOrNull!,
      structuralMutation: structural,
    );
  }

  @override
  void dispose() {
    for (final owner in _owners) {
      owner.removeListener(_ownerChanged);
    }
    _owners.clear();
    _generation++;
    super.dispose();
  }
}

extension LocalActionDestination on LocalAction {
  BindingReference get mutationReference => switch (this) {
    SetValueAction(:final target) ||
    InsertListItemAction(:final target) ||
    RemoveListItemAction(:final target) ||
    AppendListItemAction(:final target) ||
    PutMapEntryAction(:final target) ||
    RemoveMapEntryAction(:final target) ||
    ReplaceConcreteTypeAction(:final target) => target,
    DuplicateListItemAction(:final source) ||
    ReorderListItemAction(:final source) => BindingReference(
      bindingId: source.bindingId,
      path: DataPath(
        source.path.segments.take(source.path.segments.length - 1).toList(),
      ),
    ),
  };
}
