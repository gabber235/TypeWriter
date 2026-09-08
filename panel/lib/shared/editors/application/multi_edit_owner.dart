import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Projects common editable fields while preserving every resource owner.
final class MultiEditOwner extends ChangeNotifier implements EditOwner {
  MultiEditOwner({
    required this.owners,
    required this.rootType,
    required this.typeCatalog,
  }) {
    for (final owner in owners) {
      owner.addListener(notifyListeners);
    }
  }
  final List<EditOwner> owners;
  @override
  final TypeExpression rootType;
  @override
  final TypeCatalog typeCatalog;
  @override
  bool get readOnly => owners.any((owner) => owner.readOnly);
  @override
  EditorValue value(DataPath path) {
    final values = owners.map((owner) => owner.value(path)).toList();
    if (values.any((value) => value is LoadingEditorValue))
      return const EditorValue.loading();
    final invalid = values.whereType<InvalidEditorValue>().firstOrNull;
    if (invalid != null) return invalid;
    final first = values.firstOrNull?.valueOrNull;
    if (first == null || values.any((value) => value.valueOrNull != first))
      return const EditorValue.mixed();
    return EditorValue.ready(first);
  }

  @override
  EditorMutationResult update(
    DataPath path,
    DataValue value, {
    EditorStructuralMutation? structuralMutation,
  }) {
    final validation = validate(path, value);
    if (validation is! AppliedEditorMutation) return validation;
    return owners
        .map(
          (owner) => owner.update(
            path,
            validation.value,
            structuralMutation: structuralMutation,
          ),
        )
        .aggregateEditorMutationsFor(path);
  }

  @override
  EditorMutationResult validate(DataPath path, DataValue value) {
    if (readOnly) return const EditorMutationResult.conflict();
    return owners
        .map((owner) => owner.validate(path, value))
        .aggregateEditorMutationsFor(path);
  }

  @override
  EditorInteractionSession beginInteraction(DataPath path) => _MultiInteraction(
    path,
    owners.map((owner) => owner.beginInteraction(path)).toList(),
  );
  @override
  void dispose() {
    for (final owner in owners) {
      owner.removeListener(notifyListeners);
    }
    super.dispose();
  }
}

final class _MultiInteraction implements EditorInteractionSession {
  _MultiInteraction(this.path, this.sessions);
  @override
  final DataPath path;
  final List<EditorInteractionSession> sessions;
  @override
  bool get active => sessions.any((session) => session.active);
  @override
  Future<void> commit() async {
    await Future.wait(sessions.map((session) => session.commit()));
  }

  @override
  void cancel() {
    for (final session in sessions) {
      session.cancel();
    }
  }
}

extension SelectionEditorMutationAggregation on Iterable<EditorMutationResult> {
  EditorMutationResult aggregateEditorMutationsFor(DataPath path) {
    final results = toList();
    final diagnostics = results
        .whereType<InvalidEditorMutation>()
        .expand((result) => result.diagnostics)
        .toList();
    if (diagnostics.isNotEmpty) {
      return EditorMutationResult.invalid(diagnostics);
    }
    if (results.any((result) => result is ConflictingEditorMutation)) {
      return const EditorMutationResult.conflict();
    }
    final applied = results.whereType<AppliedEditorMutation>().toList();
    if (applied.isEmpty) {
      return EditorMutationResult.invalid([
        TypeDiagnostic(
          code: TypeDiagnosticCode.invalidPath,
          message: "No inspected selection can accept the mutation",
          path: path,
        ),
      ]);
    }
    final accepted = applied.first.value;
    if (applied.skip(1).any((result) => result.value != accepted)) {
      return const EditorMutationResult.conflict();
    }
    return EditorMutationResult.applied(accepted);
  }
}
