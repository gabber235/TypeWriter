import "package:typewriter_panel/typewriter_panel.dart";

enum EditorSavePhase {
  idle,
  pending,
  saving,
  saved,
  failed,
  uncertain,
  conflict,
  repeatedContention,
  deletedElsewhere,
}

final class EditorPathConflict {
  const EditorPathConflict({
    required this.base,
    required this.local,
    required this.remote,
  });

  final DataValue base;
  final DataValue local;
  final DataValue remote;
}

enum EditorContentionKind { versionMismatch }

final class EditorContentionDetails {
  EditorContentionDetails({
    required this.kind,
    required this.attempts,
    required this.retryLimit,
    required Iterable<DataPath> paths,
    this.expectedVersion,
    this.observedVersion,
  }) : paths = Set.unmodifiable(paths),
       assert(attempts > 0),
       assert(retryLimit >= 0),
       assert(attempts > retryLimit);

  final EditorContentionKind kind;
  final int attempts;
  final int retryLimit;
  final Set<DataPath> paths;
  final int? expectedVersion;
  final int? observedVersion;
}

final class EditorSaveState {
  const EditorSaveState({
    required this.phase,
    this.path,
    this.replayAvailable = false,
    this.submissionId,
    this.conflict,
    this.contention,
    this.diagnostics = const [],
  });

  const EditorSaveState.idle() : this(phase: EditorSavePhase.idle);

  final EditorSavePhase phase;
  final DataPath? path;
  final bool replayAvailable;
  final Object? submissionId;
  final EditorPathConflict? conflict;
  final EditorContentionDetails? contention;
  final List<TypeDiagnostic> diagnostics;

  bool get canRetry =>
      (phase == EditorSavePhase.uncertain && replayAvailable) ||
      phase == EditorSavePhase.failed ||
      phase == EditorSavePhase.repeatedContention;
}

abstract interface class EditorInteractionSession {
  DataPath get path;

  bool get active;

  Future<void> commit();

  void cancel();
}
