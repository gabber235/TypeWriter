part of "selection_editor_source_test.dart";

void _testEditorAvailability() {
  test(
    "loading and refresh errors block saving without deleting the draft",
    () async {
      final identifier = _identifier(
        "availability",
        const StringValue("Initial"),
        mutation: const EditorMutationResult.applied(StringValue("Draft")),
      );
      final workspace = LocalWork();
      addTearDown(workspace.dispose);
      final container = ProviderContainer.test(
        overrides: [localWorkProvider.overrideWithValue(workspace)],
      );
      container.read(selectionProvider.notifier).select(identifier);
      final session = container.read(_sourceProvider);
      final source = (_resource(session))
        ..update(DataPath.root, const StringValue("Draft"));

      identifier.loading = true;
      container
        ..invalidate(selectedProvider)
        ..read(inspectedSelectionProvider);
      expect(source.readOnly, isTrue);
      expect(await source.flush(), isA<MutationUnavailable>());
      expect(source.hasWork, isTrue);

      identifier
        ..loading = false
        ..failure = StateError("Offline");
      container
        ..invalidate(selectedProvider)
        ..read(inspectedSelectionProvider);
      expect(source.readOnly, isTrue);
      expect(
        source.saveState(DataPath.root).phase,
        isNot(EditorSavePhase.deletedElsewhere),
      );

      identifier.failure = null;
      container
        ..invalidate(selectedProvider)
        ..read(inspectedSelectionProvider);
      expect(_resource(session), same(source));
      expect(source.readOnly, isFalse);
      expect(
        source.value(DataPath.root).valueOrNull,
        const StringValue("Draft"),
      );
      expect(await source.flush(), isA<MutationSuccess>());
    },
  );
}
