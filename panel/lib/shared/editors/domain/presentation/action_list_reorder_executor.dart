part of "action_executor.dart";

extension on DuplicateListItemAction {
  LocalMutationResult _duplicate(
    ExpressionContext context,
    TypeRegistry? registry,
  ) {
    final location = source._listItemLocation;
    if (location == null) {
      return invalidLocalMutation(
        "Duplicate source must be a list item binding",
      );
    }
    final parent = _resolved(location.$1, context, registry);
    if (parent case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final resolved = parent.valueOrNull!;
    if (resolved.type is! ListType || resolved.value is! ListValue) {
      return invalidLocalMutation("Duplicate source parent must be a list");
    }
    final values = List<DataValue>.of((resolved.value as ListValue).values);
    if (location.$2 >= values.length) {
      return invalidLocalMutation("Duplicate source index is outside the list");
    }
    values.insert(location.$2 + 1, values[location.$2]);
    return location.$1.replaceValue(
      resolved.type,
      ListValue(values),
      context,
      registry,
    );
  }
}

extension on ReorderListItemAction {
  LocalMutationResult _reorder(
    ExpressionContext context,
    TypeRegistry? registry,
    ExpressionBudget budget,
  ) {
    final location = source._listItemLocation;
    if (location == null) {
      return invalidLocalMutation("Reorder source must be a list item binding");
    }
    final parent = _resolved(location.$1, context, registry);
    if (parent case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final destination = newIndex._integer(context, registry, budget);
    if (destination case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final resolved = parent.valueOrNull!;
    if (resolved.type is! ListType || resolved.value is! ListValue) {
      return invalidLocalMutation("Reorder source parent must be a list");
    }
    final values = List<DataValue>.of((resolved.value as ListValue).values);
    final nextIndex = destination.valueOrNull!;
    if (location.$2 >= values.length ||
        nextIndex < 0 ||
        nextIndex >= values.length) {
      return invalidLocalMutation("Reorder index is outside the list");
    }
    if (location.$2 == nextIndex) {
      final root = context.bindings.bindings[source.bindingId];
      if (root == null) return invalidLocalMutation("Binding is not available");
      return LocalMutationApplied(
        bindingId: source.bindingId,
        value: root.value,
      );
    }
    final value = values.removeAt(location.$2);
    values.insert(nextIndex, value);
    return location.$1.replaceValue(
      resolved.type,
      ListValue(values),
      context,
      registry,
    );
  }
}

extension on TypedExpression {
  TypeResult<int> _integer(
    ExpressionContext context,
    TypeRegistry? registry,
    ExpressionBudget budget,
  ) {
    final evaluated = evaluate(context, registry: registry, budget: budget);
    if (evaluated case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }
    final value = evaluated.valueOrNull;
    if (value is! IntegerValue) {
      return TypeResult.failure([
        const TypeDiagnostic(
          code: TypeDiagnosticCode.invalidValue,
          message: "List index must evaluate to an integer",
        ),
      ]);
    }
    return TypeResult.success(value.value.toInt());
  }
}

extension on BindingReference {
  (BindingReference, int)? get _listItemLocation {
    if (path.segments.lastOrNull case IndexPathSegment(:final index)) {
      return (
        BindingReference(
          bindingId: bindingId,
          path: DataPath(path.segments.sublist(0, path.segments.length - 1)),
        ),
        index,
      );
    }
    return null;
  }
}
