import "package:typewriter_panel/typewriter_panel.dart";

/// Intersects a type representation while preserving assignability to its base.
///
/// Inheritance uses this boundary when combining a parent's representation
/// with a child representation. An intersection may be empty or may produce a
/// shape that is no longer assignable to the base, both of which are reported
/// as diagnostics instead of allowing an invalid resolved type.
extension SafeTypeRefinement on TypeExpression {
  /// Applies [constraint] without weakening the receiver's contract.
  ///
  /// The operation is pure. On failure, callers should keep the unresolved
  /// type out of the registry and surface the returned diagnostics.
  TypeResult<TypeExpression> safelyRefineWith(
    TypeExpression constraint,
    TypeRegistry registry,
  ) {
    final intersection = intersectTypes(this, constraint);
    if (intersection case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }
    final refined = intersection.valueOrNull!;
    if (refined.isStructurallyAssignableTo(this, registry)) {
      return TypeResult.success(refined);
    }
    return TypeResult.failure([
      const TypeDiagnostic(
        code: TypeDiagnosticCode.invalidConstraint,
        message: "Refinement is not assignable to its base type",
      ),
    ]);
  }
}
