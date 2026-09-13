import "package:collection/collection.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

/// Encodes tag CRUD as authoring session operations.
///
/// These helpers do not update local or canonical state. The session owns
/// submission, response classification, and revision publication; higher
/// level tag providers decide when a response is acceptable.
extension TagCommands on AuthoringSession {
  Future<wire.ApplyAuthoringBatchResponse> createTag(wire.Tag tag) =>
      apply([wire.AuthoringOperation.createCreateTag(tag: tag)]);

  Future<wire.ApplyAuthoringBatchResponse> patchTag(
    Tag tag, {
    required Tag expected,
  }) {
    return apply([tagPatchOperation(tag, expected: expected)]);
  }

  Future<wire.ApplyAuthoringBatchResponse> deleteTag(skir.RecordId id) =>
      apply([wire.AuthoringOperation.createDeleteTag(id: id)]);
}

wire.Int32Change? _intChange(int expected, int value) => expected == value
    ? null
    : wire.Int32Change(expected: expected, value: value);

/// Builds a field guarded patch from [expected] to [tag].
///
/// Unchanged fields are omitted. Parent equality is set based, but a changed
/// list preserves the supplied order for the wire operation. Every emitted
/// change carries its expected value so concurrent edits become conflicts.
wire.AuthoringOperation tagPatchOperation(Tag tag, {required Tag expected}) {
  final before = expected;
  final placement = before.placement;
  final nextPlacement = tag.placement;
  return wire.AuthoringOperation.createPatchTag(
    id: tag.tagId,
    name: before.name == tag.name
        ? null
        : wire.StringChange(expected: before.name, value: tag.name),
    color: before.color == tag.color
        ? null
        : wire.ColorChange(
            expected: before.color.toSkirColor(),
            value: tag.color.toSkirColor(),
          ),
    parents:
        const SetEquality<skir.RecordId>().equals(
          before.parentIds.toSet(),
          tag.parentIds.toSet(),
        )
        ? null
        : wire.RecordIdListChange(
            expected: before.parentIds,
            value: tag.parentIds,
          ),
    x: _intChange(placement.x, nextPlacement.x),
    y: _intChange(placement.y, nextPlacement.y),
    width: _intChange(placement.width, nextPlacement.width),
    height: _intChange(placement.height, nextPlacement.height),
  );
}
