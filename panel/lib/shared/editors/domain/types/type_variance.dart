import "package:typewriter_panel/typewriter_panel.dart";

/// Rejects variance declarations that cannot describe the editable shape.
///
/// A non invariant parameter may not occur in a position whose structure is
/// invariant. The registry runs this check before applying generic arguments,
/// because accepting the declaration would make later assignability unsound.
extension TypeDefinitionVarianceValidation on TypeDefinition {
  List<TypeDiagnostic> validateVarianceUse() => [
    for (final parameter in parameters)
      if (parameter.variance != TypeVariance.invariant &&
          representation._usesInvariantPosition(parameter.name))
        TypeDiagnostic(
          code: TypeDiagnosticCode.varianceViolation,
          message:
              "Editable representation uses '${parameter.name}' in an invariant position",
          type: id,
        ),
  ];
}

extension on TypeExpression {
  bool _usesInvariantPosition(String name) {
    if (this is ParameterType) return false;
    if (this case ListType(:final element)) {
      return element._containsParameter(name);
    }
    if (this case MapType(:final key, :final value)) {
      return key._containsParameter(name) || value._containsParameter(name);
    }
    if (this case RecordType(:final fields)) {
      return fields.values.any((field) => field.type._containsParameter(name));
    }
    if (this case EnumType(:final valueType)) {
      return valueType._containsParameter(name);
    }
    if (this case NamedType(:final reference)) {
      return reference.arguments.any((value) => value._containsParameter(name));
    }

    return false;
  }

  bool _containsParameter(String parameterName) {
    if (this case ParameterType(:final name)) return name == parameterName;
    if (this case ListType(:final element)) {
      return element._containsParameter(parameterName);
    }
    if (this case MapType(:final key, :final value)) {
      return key._containsParameter(parameterName) ||
          value._containsParameter(parameterName);
    }
    if (this case RecordType(:final fields)) {
      return fields.values.any(
        (field) => field.type._containsParameter(parameterName),
      );
    }
    if (this case EnumType(:final valueType)) {
      return valueType._containsParameter(parameterName);
    }
    if (this case NamedType(:final reference)) {
      return reference.arguments.any(
        (value) => value._containsParameter(parameterName),
      );
    }

    return false;
  }
}
