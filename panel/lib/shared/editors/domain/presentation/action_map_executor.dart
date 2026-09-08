import "package:typewriter_panel/typewriter_panel.dart";

extension PutMapEntryActionExecution on PutMapEntryAction {
  LocalMutationResult executeMapMutation(
    ExpressionContext context,
    TypeRegistry? registry,
    ExpressionBudget budget,
  ) {
    final binding = context.bindings.resolve(target, registry: registry);
    if (binding case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final declared = binding.valueOrNull!;
    final resolved = declared.copyWith(
      type: declared.type.bindingRepresentation(registry),
    );
    if (!resolved.writable) {
      return invalidLocalMutation("Binding is read only");
    }
    if (resolved.type is! MapType || resolved.value is! MapValue) {
      return invalidLocalMutation("Put target must be a map");
    }
    final mapType = resolved.type as MapType;
    final evaluatedKey = key.evaluate(
      context,
      registry: registry,
      budget: budget,
    );
    final evaluatedValue = value.evaluate(
      context,
      registry: registry,
      budget: budget,
    );
    if (evaluatedKey case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    if (evaluatedValue case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final diagnostics = [
      ...evaluatedKey.valueOrNull!.validateAgainst(
        mapType.key,
        registry: registry,
      ),
      ...evaluatedValue.valueOrNull!.validateAgainst(
        mapType.value,
        registry: registry,
      ),
    ];
    if (diagnostics.isNotEmpty) return LocalMutationInvalid(diagnostics);
    final entries = List<DataMapEntry>.of((resolved.value as MapValue).entries);
    final entryIndex = entries.indexWhere(
      (entry) => entry.key == evaluatedKey.valueOrNull!,
    );
    if (entryIndex >= 0) {
      return invalidLocalMutation("Map key already exists");
    }
    entries.add(
      DataMapEntry(
        key: evaluatedKey.valueOrNull!,
        value: evaluatedValue.valueOrNull!,
      ),
    );
    return target.replaceValue(mapType, MapValue(entries), context, registry);
  }
}

extension RemoveMapEntryActionExecution on RemoveMapEntryAction {
  LocalMutationResult executeMapMutation(
    ExpressionContext context,
    TypeRegistry? registry,
    ExpressionBudget budget,
  ) {
    final binding = context.bindings.resolve(target, registry: registry);
    if (binding case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final declared = binding.valueOrNull!;
    final resolved = declared.copyWith(
      type: declared.type.bindingRepresentation(registry),
    );
    if (!resolved.writable) {
      return invalidLocalMutation("Binding is read only");
    }
    if (resolved.type is! MapType || resolved.value is! MapValue) {
      return invalidLocalMutation("Remove target must be a map");
    }
    final evaluatedKey = key.evaluate(
      context,
      registry: registry,
      budget: budget,
    );
    if (evaluatedKey case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final entries = List<DataMapEntry>.of((resolved.value as MapValue).entries);
    final index = entries.indexWhere(
      (entry) => entry.key == evaluatedKey.valueOrNull!,
    );
    if (index < 0) return invalidLocalMutation("Map key is absent");
    entries.removeAt(index);
    return target.replaceValue(
      resolved.type,
      MapValue(entries),
      context,
      registry,
    );
  }
}

extension ReplaceConcreteTypeActionExecution on ReplaceConcreteTypeAction {
  LocalMutationResult executeConcreteReplacement(
    ExpressionContext context,
    TypeRegistry? registry,
    ExpressionBudget budget,
  ) {
    if (registry == null) {
      return invalidLocalMutation(
        "Concrete type replacement requires a registry",
      );
    }
    final binding = context.bindings.resolve(target, registry: registry);
    if (binding case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final resolved = binding.valueOrNull!;
    if (!resolved.writable) {
      return invalidLocalMutation("Binding is read only");
    }
    final concrete = registry.resolve(NamedType(concreteType));
    if (concrete case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final evaluated = initialValue.evaluate(
      context,
      registry: registry,
      budget: budget,
    );
    if (evaluated case TypeFailure(:final diagnostics)) {
      return LocalMutationInvalid(diagnostics);
    }
    final diagnostics = evaluated.valueOrNull!.validateAgainst(
      concrete.valueOrNull!.representation,
      registry: registry,
    );
    if (diagnostics.isNotEmpty) return LocalMutationInvalid(diagnostics);
    return target.replaceValue(
      resolved.type,
      PolymorphicValue(
        concreteType: concreteType,
        value: evaluated.valueOrNull!,
      ),
      context,
      registry,
    );
  }
}
