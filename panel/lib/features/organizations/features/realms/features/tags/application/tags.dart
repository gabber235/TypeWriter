import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

part "tags.freezed.dart";
part "tags.g.dart";
part "tag_model.dart";
part "tag_collection.dart";
part "tag_inspector_presentation.dart";
part "tag_inheritance_presentation.dart";

@riverpod
class Tags extends _$Tags {
  @override
  Future<List<Tag>> build() async {
    final organizationId = ref.watch(organizationIdProvider);
    final realmId = ref.watch(realmIdProvider);
    if (organizationId == null || realmId == null) {
      return [];
    }
    final provider = authoringSessionProvider(organizationId, realmId);
    ref.listen(provider, (_, value) {
      if (value.sequence != null) state = AsyncData(_projectTags(value));
    });
    final lease = ref.watch(
      authoringLibraryScopeProvider(organizationId, realmId),
    );

    await lease.ready;
    return _projectTags(ref.read(provider));
  }

  Future<Tag> createTag({
    required String name,
    Color? color,
    List<skir.RecordId> parentIds = const [],
    int x = 0,
    int y = 0,
    int width = 4,
    int height = 1,
  }) async {
    state.ensureReady();
    final tag = Tag(
      tagId: newResourceId(AuthoringResource.tag),
      authoringSequence: ref.readAuthoringSession().state.sequence ?? 0,
      name: name,
      color: color ?? Colors.grey,
      parentIds: parentIds,
      placement: Placement(x: x, y: y, width: width, height: height),
    );
    state = AsyncData([...state.requireValue, tag]);
    try {
      final response = await ref.readAuthoringSession().notifier.createTag(
        tag.toWire(),
      );
      response.requireApplied(conflictMessage: "The tag already exists");
      return tag;
    } on Object {
      _replaceFromSession();
      rethrow;
    }
  }

  Future<TypedMutationResult> updateTag(Tag tag, {Tag? expected}) async {
    state.ensureReady();
    final before =
        expected ??
        state.requireValue.firstWhere((value) => value.tagId == tag.tagId);
    final commands = ref.readAuthoringSession().notifier;
    final owners = EditorOwnerRegistry(workspace: ref.read(localWorkProvider));
    try {
      final owner = owners.editor(
        TagSelectable(
          resource: TagEditorResource(
            ref
                .read(resourceRepositoriesProvider)
                .authoring(commands.organizationId, commands.realmId),
            tag.tagId,
          ),
          onDelete: () => deleteTag(tag.tagId),
          id: TagIdentifier(tag.tagId),
          tag: before,
          tagCollection: tagPresentationCollection(state.requireValue),
        ),
      );
      return await owner.applyChanges(
        editorValueChanges(before.inspectorValue, tag.inspectorValue),
      );
    } finally {
      owners.dispose();
    }
  }

  Future<void> toggleTagParent(
    skir.RecordId childId,
    skir.RecordId parentId,
  ) async {
    state.ensureReady();
    final tags = state.requireValue;
    final action = tagParentDropAction(
      tags,
      childId: childId,
      parentId: parentId,
    );
    if (action == null) return;
    final child = tags.firstWhere((tag) => tag.tagId == childId);
    final parents = switch (action) {
      TagParentDropAction.link => [...child.parentIds, parentId],
      TagParentDropAction.unlink =>
        child.parentIds.where((id) => id != parentId).toList(),
    };

    await updateTag(child.copyWith(parentIds: parents), expected: child);
  }

  Future<void> deleteTag(skir.RecordId tagId) async {
    state.ensureReady();
    state = AsyncData(
      state.requireValue.where((tag) => tag.tagId != tagId).toList(),
    );
    try {
      final response = await ref.readAuthoringSession().notifier.deleteTag(
        tagId,
      );
      response.requireApplied(
        conflictMessage: "The tag changed before deletion",
      );
    } on Object {
      _replaceFromSession();
      rethrow;
    }
  }

  void _replaceFromSession() {
    state = AsyncData(_projectTags(ref.readAuthoringSession().state));
  }
}

@riverpod
Future<Tag?> tag(Ref ref, skir.RecordId tagId) async {
  final tags = await ref.watch(tagsProvider.future);
  return tags.firstWhereOrNull((tag) => tag.tagId == tagId);
}

List<Tag> _projectTags(AuthoringSessionState value) {
  final sequence = value.sequence ?? 0;
  return value.tags.values.map((tag) => Tag.fromWire(tag, sequence)).toList();
}
