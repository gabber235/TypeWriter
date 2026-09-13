import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "local_work_state.freezed.dart";

@freezed
abstract class LocalWorkState with _$LocalWorkState {
  const factory LocalWorkState({
    @Default({}) Map<EditorResourceKey, LocalWorkResourceState> resources,
    @Default({}) Map<EditorResourceKey, LocalEditorValue> editorValues,
    @Default([]) List<LocalWorkSubmissionState> submissions,
  }) = _LocalWorkState;
}

@freezed
abstract class LocalWorkResourceState with _$LocalWorkResourceState {
  const factory LocalWorkResourceState({
    required EditorResourceKey key,
    required String label,
    required EditorCommitPolicy commitPolicy,
    required EditorSavePhase savePhase,
    required bool readOnly,
    required bool hasDiagnostics,
    required LocalWorkDestinationState destination,
  }) = _LocalWorkResourceState;
}

enum LocalWorkDestinationState { unavailable, current, available }

@freezed
abstract class LocalWorkSubmissionState with _$LocalWorkSubmissionState {
  const factory LocalWorkSubmissionState({
    required Object id,
    required String label,
    required bool sending,
    required bool canReplay,
    required bool integrationFailed,
    required LocalWorkSubmissionResult result,
    String? message,
  }) = _LocalWorkSubmissionState;
}

enum LocalWorkSubmissionResult {
  ready,
  confirmed,
  rejected,
  uncertain,
  notSubmitted,
}
