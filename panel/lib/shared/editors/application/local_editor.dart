import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Keeps temporary edits without inventing a backend revision or save response.
/// Its creator owns disposal. Replacing the initial value requires a new owner.
final class LocalEditor extends ChangeNotifier implements EditOwner {
  LocalEditor({
    required this.rootType,
    required this.typeCatalog,
    required this._value,
  });

  @override
  TypeExpression rootType;

  @override
  TypeCatalog typeCatalog;
  DataValue _value;
  bool _disposed = false;
  final Set<_LocalInteraction> _interactions = {};

  @override
  bool get readOnly => _disposed;

  @override
  EditorValue value(DataPath path) => _value.readEditorValue(path);

  @override
  EditorMutationResult update(
    DataPath path,
    DataValue value, {
    EditorStructuralMutation? structuralMutation,
  }) {
    if (_disposed) return const EditorMutationResult.conflict();
    final result = validate(path, value);
    if (result is! AppliedEditorMutation) return result;
    final replaced = path.replace(_value, value);
    if (replaced case TypeFailure(:final diagnostics)) {
      return EditorMutationResult.invalid(diagnostics);
    }

    _value = replaced.valueOrNull!;

    notifyListeners();
    return result;
  }

  @override
  EditorMutationResult validate(DataPath path, DataValue value) => rootType
      .validateEditorMutation(path, value, registry: TypeRegistry(typeCatalog));

  void refreshSchema(TypeExpression type, TypeCatalog catalog) {
    if (rootType == type && typeCatalog == catalog) return;
    rootType = type;
    typeCatalog = catalog;
    notifyListeners();
  }

  @override
  EditorInteractionSession beginInteraction(DataPath path) {
    final interaction = _LocalInteraction(this, path, value(path).valueOrNull);
    if (_disposed) {
      interaction.active = false;
    } else {
      _interactions.add(interaction);
    }
    return interaction;
  }

  @override
  void dispose() {
    _disposed = true;
    for (final interaction in _interactions) {
      interaction.active = false;
    }
    _interactions.clear();
    super.dispose();
  }
}

final class _LocalInteraction implements EditorInteractionSession {
  _LocalInteraction(this.owner, this.path, this.origin);

  final LocalEditor owner;
  final DataValue? origin;

  @override
  final DataPath path;

  @override
  bool active = true;
  @override
  Future<void> commit() async {
    _close();
  }

  @override
  void cancel() {
    if (!active) return;
    _close();
    if (origin case final value?) owner.update(path, value);
  }

  void _close() {
    active = false;
    owner._interactions.remove(this);
  }
}
