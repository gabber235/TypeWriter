import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod/riverpod.dart";
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
class CanonicalTags extends _$CanonicalTags {
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
      name: name,
      color: color ?? Colors.grey,
      parentIds: parentIds,
      placement: Placement(x: x, y: y, width: width, height: height),
    );
    final response = await ref.readAuthoringSession().notifier.createTag(
      tag.toWire(),
    );
    response.requireApplied(conflictMessage: "The tag already exists");
    return tag;
  }

  Future<TypedMutationResult> updateTag(Tag tag, {Tag? expected}) async {
    state.ensureReady();
    final session = ref.readAuthoringSession();
    final current = session.state.tags[tag.tagId];
    if (current == null || session.state.sequence == null) {
      throw ApiException.notFound("Tag");
    }
    final before = expected ?? Tag.fromWire(current);
    final commands = session.notifier;
    final owners = EditorOwnerRegistry(
      workspace: ref.read(localWorkControllerProvider),
    );
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
          revision: session.state.sequence!,
          tagCollection: state.requireValue.presentationCollection(),
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
    List<Tag> tags,
    skir.RecordId childId,
    skir.RecordId parentId,
  ) async {
    state.ensureReady();
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
    final response = await ref.readAuthoringSession().notifier.deleteTag(tagId);
    response.requireApplied(conflictMessage: "The tag changed before deletion");
  }
}

@riverpod
Future<Tag?> canonicalTag(Ref ref, skir.RecordId tagId) async {
  final tags = await ref.watch(canonicalTagsProvider.future);
  return tags.firstWhereOrNull((tag) => tag.tagId == tagId);
}

List<Tag> _projectTags(AuthoringSessionState value) {
  return value.tags.values.map(Tag.fromWire).toList();
}

extension AuthoringTagValue on AuthoringSessionState {
  AuthoringValue<Tag>? tagEditorValue(skir.RecordId tagId) {
    final value = tags[tagId];
    final revision = sequence;
    if (value == null || revision == null) return null;
    return AuthoringValue(value: Tag.fromWire(value), revision: revision);
  }
}

@riverpod
AsyncValue<List<Tag>> projectedTags(Ref ref) {
  final canonicalTags = ref.watch(canonicalTagsProvider);
  if (canonicalTags.mapUnready<List<Tag>>() case final value?) return value;

  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues),
  );
  final organizationId = ref.watch(organizationIdProvider);
  final realmId = ref.watch(realmIdProvider);
  if (organizationId == null || realmId == null) {
    return AsyncData(canonicalTags.requireValue);
  }
  return AsyncData([
    for (final tag in canonicalTags.requireValue)
      tag.projected(
        local[EditorResourceKey(
          scope: EditorResourceScope(
            organizationId: organizationId,
            realmId: realmId,
          ),
          identity: tag.tagId,
        )],
      ),
  ]);
}

@riverpod
AsyncValue<Tag?> projectedTag(Ref ref, skir.RecordId tagId) {
  final canonical = ref.watch(canonicalTagProvider(tagId));
  if (canonical.mapUnready<Tag?>() case final value?) return value;
  final organizationId = ref.watch(organizationIdProvider);
  final realmId = ref.watch(realmIdProvider);
  if (organizationId == null || realmId == null) return canonical;
  final key = EditorResourceKey(
    scope: EditorResourceScope(
      organizationId: organizationId,
      realmId: realmId,
    ),
    identity: tagId,
  );
  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues[key]),
  );
  return AsyncData(canonical.requireValue?.projected(local));
}
