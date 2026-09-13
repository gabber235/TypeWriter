import "package:typewriter_panel/typewriter_panel.dart";

/// Recovers persistence intent from a successfully evaluated local action.
///
/// A value diff cannot distinguish operations such as moving a list item from
/// replacing it. Presentation execution uses this conversion to retain the
/// operation shape, while returning `null` when the evaluated result does not
/// prove that the structural operation can be represented safely.
EditorStructuralMutation? structuralMutationFor(
  LocalAction action,
  ExpressionContext context,
  LocalMutationResult result,
  TypeRegistry registry,
) {
  if (result is! LocalMutationApplied) return null;
  final root = result.value;
  return switch (action) {
    SetValueAction(:final target) => _rootTarget(
      target,
      root,
      EditorSetValue.new,
    ),
    InsertListItemAction(:final target, :final index) => _rootListTarget(
      target,
      root,
      context,
      (path, before, after) {
        final position = _evaluateIndex(index, context, registry);
        if (position == null ||
            after.values.length != before.values.length + 1) {
          return null;
        }
        return EditorInsertListItems(path, position, [after.values[position]]);
      },
    ),
    AppendListItemAction(:final target) => _rootListTarget(
      target,
      root,
      context,
      (path, before, after) => EditorInsertListItems(
        path,
        before.values.length,
        [after.values.last],
      ),
    ),
    RemoveListItemAction(:final target, :final index) => _removeListMutation(
      target,
      index,
      context,
      registry,
    ),
    DuplicateListItemAction(:final source) => _duplicateListMutation(source),
    ReorderListItemAction(:final source, :final newIndex) =>
      _reorderListMutation(source, newIndex, context, registry),
    PutMapEntryAction(:final target) => _rootMapTarget(
      target,
      root,
      context,
      (path, before, after) => EditorPutMapEntries(
        path,
        after.entries
            .where((entry) => !before.entries.contains(entry))
            .toList(),
      ),
    ),
    RemoveMapEntryAction(:final target) => _rootMapTarget(
      target,
      root,
      context,
      (path, before, after) => EditorRemoveMapEntries(
        path,
        before.entries
            .where((entry) => !after.entries.contains(entry))
            .map((entry) => entry.key)
            .toList(),
      ),
    ),
    ReplaceConcreteTypeAction(:final target, :final concreteType) =>
      _rootTarget(
        target,
        root,
        (path, value) => value is PolymorphicValue
            ? EditorReplaceConcreteType(path, concreteType, value.value)
            : null,
      ),
  };
}

EditorStructuralMutation? _rootTarget(
  BindingReference target,
  DataValue root,
  EditorStructuralMutation? Function(DataPath path, DataValue value) create,
) {
  final value = target.path.read(root).valueOrNull;
  return value == null ? null : create(target.path, value);
}

EditorStructuralMutation? _rootListTarget(
  BindingReference target,
  DataValue root,
  ExpressionContext context,
  EditorStructuralMutation? Function(
    DataPath path,
    ListValue before,
    ListValue after,
  )
  create,
) {
  final before = context.bindings.resolve(target).valueOrNull?.value;
  final after = target.path.read(root).valueOrNull;
  if (before is! ListValue || after is! ListValue) return null;
  return create(target.path, before, after);
}

EditorStructuralMutation? _rootMapTarget(
  BindingReference target,
  DataValue root,
  ExpressionContext context,
  EditorStructuralMutation Function(
    DataPath path,
    MapValue before,
    MapValue after,
  )
  create,
) {
  final before = context.bindings.resolve(target).valueOrNull?.value;
  final after = target.path.read(root).valueOrNull;
  if (before is! MapValue || after is! MapValue) return null;
  return create(target.path, before, after);
}

int? _evaluateIndex(
  TypedExpression expression,
  ExpressionContext context,
  TypeRegistry registry,
) {
  final value = expression.evaluate(context, registry: registry).valueOrNull;
  return value is IntegerValue ? value.value.toInt() : null;
}

(BindingReference, int)? _listItemLocation(BindingReference reference) {
  if (reference.path.segments.lastOrNull case IndexPathSegment(:final index)) {
    return (
      BindingReference(
        bindingId: reference.bindingId,
        path: DataPath(
          reference.path.segments.sublist(
            0,
            reference.path.segments.length - 1,
          ),
        ),
      ),
      index,
    );
  }
  return null;
}

EditorStructuralMutation? _removeListMutation(
  BindingReference target,
  TypedExpression index,
  ExpressionContext context,
  TypeRegistry registry,
) {
  final position = _evaluateIndex(index, context, registry);
  if (position == null) return null;
  return EditorRemoveListItems(target.path, position, 1);
}

EditorStructuralMutation? _duplicateListMutation(BindingReference source) {
  final location = _listItemLocation(source);
  if (location == null) {
    return null;
  }
  return EditorDuplicateListItems(
    location.$1.path,
    location.$2,
    1,
    location.$2 + 1,
  );
}

EditorStructuralMutation? _reorderListMutation(
  BindingReference source,
  TypedExpression newIndex,
  ExpressionContext context,
  TypeRegistry registry,
) {
  final location = _listItemLocation(source);
  final destination = _evaluateIndex(newIndex, context, registry);
  if (location == null || destination == null) {
    return null;
  }
  return EditorReorderListItems(location.$1.path, location.$2, 1, destination);
}
