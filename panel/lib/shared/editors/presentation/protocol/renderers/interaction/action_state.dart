part of "../../interaction_renderer.dart";

/// Applies the interaction policy shared by every action backed control.
///
/// A disabled presentation scope suppresses both local and Realm actions. A
/// read only scope suppresses local mutations but still permits Realm actions,
/// because those requests are owned by the Realm rather than the local draft.
/// The check is repeated at the control boundary so disabled widgets cannot
/// dispatch through a stale callback.
extension on EditorAction {
  bool enabledIn(PresentationRenderScope scope) =>
      scope.enabled && (this is RealmEditorAction || !scope.readOnly);
}
