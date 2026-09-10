import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "inspection.g.dart";

@riverpod
AsyncValue<List<InspectableSelectable>> inspectedSelection(Ref ref) {
  final selectedAsync = ref.watch(selectedProvider);
  if (selectedAsync.mapUnready<List<InspectableSelectable>>()
      case final value?) {
    return value;
  }
  final selection = selectedAsync.requireValue;
  if (selection.any((selectable) => selectable is! InspectableSelectable)) {
    return AsyncData([]);
  }
  return AsyncData(selection.cast<InspectableSelectable>());
}

@riverpod
bool hasInspectableSelection(Ref ref) {
  return ref.watch(inspectedSelectionProvider).value?.isNotEmpty ?? true;
}

@riverpod
InspectionSession inspectionSession(Ref ref) {
  ref.watch(localWorkProvider);
  final session = InspectionSession(ref);
  ref.onDispose(session.dispose);
  return session;
}
