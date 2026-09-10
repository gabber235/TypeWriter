part of "tags.dart";

@freezed
abstract class Placement with _$Placement {
  const factory Placement({
    required int x,
    required int y,
    required int width,
    required int height,
  }) = _Placement;

  const Placement._();

  factory Placement.fromWire(wire.GraphPlacement placement) => Placement(
    x: placement.x,
    y: placement.y,
    width: placement.width,
    height: placement.height,
  );

  wire.GraphPlacement toWire() =>
      wire.GraphPlacement(x: x, y: y, width: width, height: height);
}

@freezed
abstract class Tag with _$Tag {
  @Assert("name != \"\"", "Name must not be empty.")
  const factory Tag({
    required skir.RecordId tagId,
    required String name,
    required Color color,
    required List<skir.RecordId> parentIds,
    required Placement placement,
  }) = _Tag;

  const Tag._();

  factory Tag.fromWire(wire.Tag tag) => Tag(
    tagId: tag.id,
    name: tag.name,
    color: tag.color.toFlutterColor(),
    parentIds: tag.parents.toList(),
    placement: Placement.fromWire(tag.placement),
  );

  wire.Tag toWire() => wire.Tag(
    id: tagId,
    name: name,
    color: color.toSkirColor(),
    parents: parentIds,
    placement: placement.toWire(),
  );
}

extension TagInspectorValue on Tag {
  RecordValue get inspectorValue => RecordValue({
    "name": name.asValue,
    "color": color.asValue,
    "parents": ListValue(
      parentIds.map((parentId) => parentId.id.asValue).toList(),
    ),
    "layout": RecordValue({
      "x": placement.x.asValue,
      "y": placement.y.asValue,
      "width": placement.width.asValue,
      "height": placement.height.asValue,
    }),
  });

  Tag? withInspectorValue(DataValue value) {
    if (value is! RecordValue) return null;
    final name = value.fields["name"];
    final color = value.fields["color"];
    final parents = value.fields["parents"];
    final layout = value.fields["layout"];
    if (name is! StringValue ||
        name.value.trim().isEmpty ||
        color is! IntegerValue ||
        parents is! ListValue ||
        layout is! RecordValue) {
      return null;
    }

    final decodedColor = color.asColorOrNull;
    final parentIds = parents.values
        .whereType<StringValue>()
        .map((parent) => recordId("tag:${parent.value}"))
        .toList();
    final x = layout.fields["x"];
    final y = layout.fields["y"];
    final width = layout.fields["width"];
    final height = layout.fields["height"];

    if (decodedColor == null ||
        parentIds.length != parents.values.length ||
        x is! IntegerValue ||
        y is! IntegerValue ||
        width is! IntegerValue ||
        height is! IntegerValue ||
        width.value < BigInt.one ||
        height.value < BigInt.one) {
      return null;
    }
    return copyWith(
      name: name.value,
      color: decodedColor,
      parentIds: parentIds,
      placement: Placement(
        x: x.value.toInt(),
        y: y.value.toInt(),
        width: width.value.toInt(),
        height: height.value.toInt(),
      ),
    );
  }

  Tag projected(LocalEditorValue? local) {
    if (local == null) return this;
    return withInspectorValue(local.projectOnto(inspectorValue)) ?? this;
  }
}

enum TagParentDropAction { link, unlink }

TagParentDropAction? tagParentDropAction(
  Iterable<Tag> tags, {
  required skir.RecordId childId,
  required skir.RecordId parentId,
}) {
  final tagsById = {for (final tag in tags) tag.tagId: tag};
  final child = tagsById[childId];
  if (child == null || !tagsById.containsKey(parentId)) return null;
  if (childId == parentId) return null;
  if (child.parentIds.contains(parentId)) return TagParentDropAction.unlink;

  final parentIsAncestor = _isAncestor(
    tagsById,
    tagId: childId,
    ancestorId: parentId,
  );
  if (parentIsAncestor ?? true) return null;

  final childIsAncestor = _isAncestor(
    tagsById,
    tagId: parentId,
    ancestorId: childId,
  );
  if (childIsAncestor ?? true) return null;

  return TagParentDropAction.link;
}

bool? _isAncestor(
  Map<skir.RecordId, Tag> tagsById, {
  required skir.RecordId tagId,
  required skir.RecordId ancestorId,
}) {
  final pendingIds = [tagId];
  final visitedIds = <skir.RecordId>{};
  while (pendingIds.isNotEmpty) {
    final currentId = pendingIds.removeLast();
    if (!visitedIds.add(currentId)) continue;

    final current = tagsById[currentId];
    if (current == null) return null;
    for (final parentId in current.parentIds) {
      if (parentId == ancestorId) return true;
      pendingIds.add(parentId);
    }
  }

  return false;
}
