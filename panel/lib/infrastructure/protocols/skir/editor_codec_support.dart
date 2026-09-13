// Shared result handling for protocol boundaries.
//
// Codec failures retain every diagnostic produced while decoding nested
// values. These helpers make aggregation consistent without hiding which
// input caused a failure.
import "package:typewriter_panel/typewriter_panel.dart";

/// Transforms a successful codec result without touching its diagnostics.
extension TypeResultMapping<T> on TypeResult<T> {
  TypeResult<R> mapValue<R>(R Function(T value) transform) => switch (this) {
    TypeSuccess(:final value) => TypeResult.success(transform(value)),
    TypeFailure(:final diagnostics) => TypeResult.failure(diagnostics),
  };
}

/// Creates the standard diagnostic for malformed wire data.
TypeDiagnostic wireDiagnostic(String message) =>
    TypeDiagnostic(code: TypeDiagnosticCode.invalidValue, message: message);

/// Returns a failed result for malformed or unsupported wire data.
TypeResult<T> invalidWire<T>(String message) =>
    TypeResult.failure([wireDiagnostic(message)]);

/// Combines two nested results and preserves diagnostics from both branches.
TypeResult<R> combineResults<A, B, R>(
  TypeResult<A> first,
  TypeResult<B> second,
  R Function(A first, B second) combine,
) {
  final diagnostics = [...first.diagnostics, ...second.diagnostics];
  if (diagnostics.isNotEmpty) return TypeResult.failure(diagnostics);
  return TypeResult.success(
    combine(first.valueOrNull as A, second.valueOrNull as B),
  );
}

/// Combines three nested results and preserves diagnostics from all branches.
TypeResult<R> combineThreeResults<A, B, C, R>(
  TypeResult<A> first,
  TypeResult<B> second,
  TypeResult<C> third,
  R Function(A first, B second, C third) combine,
) {
  final diagnostics = [
    ...first.diagnostics,
    ...second.diagnostics,
    ...third.diagnostics,
  ];
  if (diagnostics.isNotEmpty) return TypeResult.failure(diagnostics);
  return TypeResult.success(
    combine(
      first.valueOrNull as A,
      second.valueOrNull as B,
      third.valueOrNull as C,
    ),
  );
}
