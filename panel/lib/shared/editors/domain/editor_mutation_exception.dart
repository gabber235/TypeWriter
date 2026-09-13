import "package:typewriter_panel/typewriter_panel.dart";

final class EditorMutationException implements Exception {
  const EditorMutationException(this.result, this.message);
  final TypedMutationResult result;
  final String message;
  @override
  String toString() => message;
}

extension RequireEditorMutation on TypedMutationResult {
  void requireApplied({required String conflictMessage}) {
    final message = switch (this) {
      MutationSuccess() => null,
      MutationConflict() => conflictMessage,
      MutationUncertain(:final message) ||
      MutationPermissionDenied(:final message) => message,
      MutationInvalid(:final diagnostics) ||
      MutationUnavailable(
        :final diagnostics,
      ) => diagnostics.map((diagnostic) => diagnostic.message).join("; "),
    };
    if (message != null) throw EditorMutationException(this, message);
  }
}
