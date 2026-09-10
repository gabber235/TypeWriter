import "package:flutter/material.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

part "tag_editor_resource.dart";
part "tag_inspector_definition.dart";

class TagIdentifier extends SelectableIdentifier implements GraphDragData {
  const TagIdentifier(this.tagId);

  final skir.RecordId tagId;

  @override
  String get id => tagId.id;

  @override
  GraphIdentifier get graphId => GraphIdentifier(id);

  @override
  Object get resourceId => tagId;

  @override
  AsyncValue<Selectable> create(Ref ref) {
    final organization = ref.watch(organizationIdProvider);
    final realm = ref.watch(realmIdProvider);
    if (organization == null || realm == null) {
      return AsyncError(
        ApiException.badRequest("No realm selected"),
        StackTrace.current,
      );
    }
    final tagsCommands = ref.watch(tagsProvider.notifier);
    final tagAsync = ref.watch(tagProvider(tagId));
    if (tagAsync.mapUnready<Selectable>() case final value?) return value;

    final tag = tagAsync.requireValue;
    if (tag == null) {
      throw SelectableNotFoundException(this);
    }
    final tags = ref.watch(tagsProvider).value ?? const <Tag>[];
    return AsyncValue.data(
      TagSelectable(
        resource: TagEditorResource(
          ref
              .watch(resourceRepositoriesProvider)
              .authoring(organization, realm),
          tagId,
        ),
        onDelete: () => tagsCommands.deleteTag(tagId),
        id: this,
        tag: tag,
        tagCollection: tagPresentationCollection(
          tags,
          editingTagId: tag.tagId,
          existingParentIds: tag.parentIds,
        ),
      ),
    );
  }

  @override
  int get hashCode => tagId.hashCode;

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is TagIdentifier && other.tagId == tagId;
  }

  @override
  String toString() => "TagIdentifier(tagId: $tagId)";
}

class TagSelectable extends EditableSelectable<TagIdentifier> {
  const TagSelectable({
    required this.resource,
    required this.onDelete,
    required this.id,
    required this.tag,
    required this.tagCollection,
  });

  @override
  final TagIdentifier id;

  final Tag tag;
  final PresentationCollectionSource tagCollection;

  @override
  String get name => tag.name;

  @override
  final EditableResource resource;
  final Future<void> Function() onDelete;

  @override
  List<PresentationDefinition> get presentations => [_tagInspectorPresentation];
  @override
  List<PresentationCollectionSource> get collections => [tagCollection];

  @override
  EditorSnapshot get snapshot => TagEditorSnapshot(tag);
  @override
  List<SelectionCapability> get capabilities => [
    DeleteSelectionCapability(onDelete: onDelete),
  ];

  @override
  Widget? buildInspectorHeader(EditOwner owner) => ManagedInspectorHeader(
    id: tag.tagId.id,
    owner: owner,
    fallbackName: tag.name.formatted,
    fallbackColor: tag.color,
  );

  @override
  String toString() => "TagSelectable(id: $id, tag: $tag)";
}
