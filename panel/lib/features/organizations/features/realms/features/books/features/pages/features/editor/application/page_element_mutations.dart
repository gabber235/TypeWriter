part of "page_elements.dart";

mixin _PageElementMutations
    on _$PageElements, _PageElementMutationContext, _PageElementValues {
  void optimisticMoveAll(List<(String, int, int)> changed) {
    final positions = {for (final item in changed) item.$1: (item.$2, item.$3)};
    state = AsyncData([
      for (final element in state.requireValue)
        if (positions[element.id] case final position?)
          element.moveTo(position.$1, position.$2)
        else
          element,
    ]);
  }

  void optimisticResizeAll(List<(String, int, int)> changed) {
    final sizes = {for (final item in changed) item.$1: (item.$2, item.$3)};
    state = AsyncData([
      for (final element in state.requireValue)
        if (sizes[element.id] case final size?)
          element.resizeTo(size.$1, size.$2)
        else
          element,
    ]);
  }

  void optimisticCuesUpdate(List<(String, int, int)> changed) {
    final timings = {for (final item in changed) item.$1: (item.$2, item.$3)};
    state = AsyncData([
      for (final element in state.requireValue)
        if (timings[element.id] case final timing?)
          element.updateCueTo(timing.$1, timing.$2)
        else
          element,
    ]);
  }

  Future<void> moveAll(List<(String, int, int)> changed) => _commitPlacements(
    changed,
    (element, x, y) => wire.ElementPlacement.createGraph(
      x: x,
      y: y,
      width: _graph(element).width,
      height: _graph(element).height,
    ),
  );

  Future<void> resizeAll(List<(String, int, int)> changed) => _commitPlacements(
    changed,
    (element, width, height) => wire.ElementPlacement.createGraph(
      x: _graph(element).x,
      y: _graph(element).y,
      width: width,
      height: height,
    ),
  );

  Future<void> updateCues(List<(String, int, int)> changed) =>
      _commitPlacements(
        changed,
        (element, start, end) => switch (element) {
          wire.ElementPlacement_timelineSegmentWrapper() =>
            wire.ElementPlacement.createTimelineSegment(
              startFrame: start,
              endFrame: end,
            ),
          wire.ElementPlacement_timelineKeyframeWrapper() =>
            wire.ElementPlacement.createTimelineKeyframe(frame: start),
          _ => throw ApiException.badRequest("The element is not a cue"),
        },
      );

  Future<void> _commitPlacements(
    List<(String, int, int)> changed,
    wire.ElementPlacement Function(wire.ElementPlacement, int, int) placement,
  ) async {
    state.ensureReady();
    if (changed.isEmpty) return;
    final owners = EditorOwnerRegistry(
      workspace: ref.read(editorWorkspaceProvider),
      scope: (organizationId, realmId),
    );
    final commands = _commands;
    final targets = <TransactionalEditorSource, (String, EditorTarget)>{};
    final changes = <TransactionalEditorSource, Map<DataPath, DataValue>>{};
    try {
      for (final (id, first, second) in changed) {
        final target = _target(id);
        final owner = owners.editor(target) as TransactionalEditorSource;
        targets[owner] = (id, target);
        final current = encodeElementPlacement(
          owner.value(elementPlacementPath).valueOrNull!,
        );
        changes[owner] = {
          elementPlacementPath: elementPlacementValue(
            placement(current, first, second),
          ),
        };
      }
      final results = await EditorBatch.submit(
        changes: changes,
        send: (commits) async {
          Future<Map<TransactionalEditorSource, TypedMutationResult>> accept(
            wire.ApplyAuthoringBatchResponse response,
          ) async => {
            for (final entry in commits.entries)
              entry.key: await acceptElementCommit(
                response,
                entry.value,
                entry.key.document,
              ),
          };
          try {
            return await accept(
              await commands.apply([
                for (final entry in commits.entries)
                  elementCommitOperation(
                    targets[entry.key]!.$1,
                    entry.value,
                    entry.key.typeCatalog,
                  ),
              ]),
            );
          } on SubmissionException<wire.ApplyAuthoringBatchResponse> catch (
            error
          ) {
            return {
              for (final entry in commits.entries)
                entry.key: error.toMutation(
                  (response) async => (await accept(response))[entry.key]!,
                ),
            };
          }
        },
      );
      for (final result in results.values) {
        if (result is MutationSuccess || result is MutationUncertain) continue;
        throw ApiException.conflict(
          "The placement batch could not be saved. Review the retained draft.",
        );
      }
    } finally {
      owners.dispose();
    }
  }

  wire.GraphPlacement _graph(wire.ElementPlacement element) =>
      switch (element) {
        wire.ElementPlacement_graphWrapper(:final value) => value,
        _ => throw ApiException.badRequest("The element is not on a graph"),
      };

  Future<void> deleteAll(List<String> elementIds) async {
    state.ensureReady();
    if (elementIds.isEmpty) return;
    state = AsyncData(
      state.requireValue
          .where((element) => !elementIds.contains(element.id))
          .toList(),
    );
    try {
      await _submit(
        _commands.deleteElements([
          for (final id in elementIds) recordId("element:$id"),
        ]),
      );
    } on Object {
      _replaceFromSession();
      rethrow;
    }
  }

  Future<List<String>> createEntries(
    List<ElementDefinition> definitions,
    EntryPlacementKind placementKind,
  ) async {
    state.ensureReady();
    if (definitions.isEmpty) return const [];
    final codec = _codec();
    final registry = codec.registry;
    final ids = [
      for (final _ in definitions) newResourceId(AuthoringResource.element).id,
    ];
    final graphY = state.requireValue
        .whereType<PageElementEntry>()
        .map((element) => element.entry)
        .whereType<DefinitionPageEntry>()
        .map((entry) => entry.definition.placement.y + 1)
        .fold(0, (maximum, value) => value > maximum ? value : maximum);
    await _submit(
      _commands.createElements([
        for (final indexed in definitions.indexed)
          wire.PageElement(
            id: recordId("element:${ids[indexed.$1]}"),
            page: _pageId,
            elementType: indexed.$2.typeId.uuid,
            schemaRevision: indexed.$2.rootType.revision,
            name: indexed.$2.name,
            value: _initialElementValue(indexed.$2, registry, codec.codec),
            placement: switch (placementKind) {
              EntryPlacementKind.graph => wire.ElementPlacement.createGraph(
                x: 0,
                y: graphY + indexed.$1,
                width: 4,
                height: 1,
              ),
              EntryPlacementKind.timelineEntry =>
                wire.ElementPlacement.createTimelineEntry(
                  trackIndex: indexed.$1,
                ),
            },
          ),
      ]),
    );
    _replaceFromSession();
    return ids;
  }

  Future<List<String>> duplicateAll(List<String> elementIds) async {
    state.ensureReady();
    if (elementIds.isEmpty) return const [];
    final elements = {for (final item in _document.elements) item.id.id: item};
    final ids = {
      for (final id in elementIds) id: newResourceId(AuthoringResource.element),
    };
    await _submit(
      _commands.duplicateElements({
        for (final id in elementIds) elements[id]!: ids[id]!,
      }),
    );
    _replaceFromSession();
    return [for (final id in elementIds) ids[id]!.id];
  }

  Future<void> moveEntriesToPage(
    List<String> elementIds,
    String targetPageId,
  ) async {
    if (elementIds.isEmpty || targetPageId == _pageId.id) return;
    final elements = {for (final item in _document.elements) item.id.id: item};
    await _submit(
      _commands.moveElementsToPage([
        for (final id in elementIds) elements[id]!,
      ], recordId("page:$targetPageId")),
    );
    state = AsyncData(
      state.requireValue
          .where((element) => !elementIds.contains(element.id))
          .toList(),
    );
  }
}
