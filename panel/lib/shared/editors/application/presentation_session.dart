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

  BindingEnvironment get bindings {
    final sources = <BindingId, BindingSource>{};
    for (final entry in model.inputs.entries) {
      switch (entry.value) {
        case PresentationValueInput(:final type, :final value):
          sources[entry.key] = EditorValueBindingSource(
            type: type,
            value: value,
            revision: _generation,
          );
        case PresentationEditInput(:final owner, :final path):
          sources[entry.key] = EditOwnerBindingSource(
            owner: owner,
            prefix: path,
            revision: _generation,
          );
      }
    }
    return BindingEnvironment(sources);
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
        final sources = {...context.bindings.bindings};
        for (final entry in sources.entries.toList()) {
          final address = BindingReference(bindingId: entry.key)
              .canonicalizedWith(aliases);
          if (address.bindingId != destination.bindingId) continue;
          final input =
              model.inputs[address.bindingId]! as PresentationEditInput;
          final value = member
              .value(input.path.followedBy(address.path))
              .valueOrNull;

          if (value == null) return const EditorMutationResult.conflict();
          final inspected = context.bindings.inspect(
            BindingReference(bindingId: entry.key),
            registry: TypeRegistry(model.catalog),
          );
          if (inspected case TypeFailure(:final diagnostics)) {
            return EditorMutationResult.invalid(diagnostics);
          }
          final binding = inspected.valueOrNull!;
          sources[entry.key] = BindingSnapshot(
            type: binding.type,
            value: value,
            revision: binding.revision,
            writable: !member.readOnly,
          );
        }
        final memberContext = context.copyWith(
          bindings: BindingEnvironment(sources),
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
        final prefix = BindingReference(bindingId: local.bindingId)
            .canonicalizedWith(aliases)
            .path;

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
      BindingReference(bindingId: local.bindingId)
          .canonicalizedWith(aliases)
          .path,
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
