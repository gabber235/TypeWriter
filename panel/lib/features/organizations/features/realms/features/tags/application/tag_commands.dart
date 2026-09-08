import "package:collection/collection.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

extension TagCommands on AuthoringSession {
  Future<TypedMutationResult> commitTag(
    Tag next, {
    required Tag expected,
  }) async {
    Future<TypedMutationResult> accept(
      wire.ApplyAuthoringBatchResponse response,
    ) async {
      switch (response) {
        case wire.ApplyAuthoringBatchResponse_appliedWrapper(:final value):
          return TypedMutationResult.success(
            revision: value.sequence,
            value: next.inspectorValue,
          );
        case wire.ApplyAuthoringBatchResponse_conflictWrapper():
          final canonical = snapshot.tags[expected.tagId];
          if (canonical == null)
            return unavailableMutation(
              "The tag no longer exists",
              targetDeleted: true,
            );
          final actual = Tag.fromWire(canonical, snapshot.sequence ?? 0);
          return TypedMutationResult.conflict(
            expectedRevision: expected.authoringSequence,
            actualRevision: actual.authoringSequence,
            actualValue: actual.inspectorValue,
          );
        default:
          return response.toMutationFailure(
            unavailableMessage: "The tag update could not be completed",
          );
      }
    }

    try {
      return await accept(await patchTag(next, expected: expected));
    } on SubmissionException<wire.ApplyAuthoringBatchResponse> catch (error) {
      return error.toMutation(accept);
    }
  }

  Future<wire.ApplyAuthoringBatchResponse> createTag(wire.Tag tag) =>
      apply([wire.AuthoringOperation.createCreateTag(tag: tag)]);

  Future<wire.ApplyAuthoringBatchResponse> patchTag(
    Tag tag, {
    required Tag expected,
  }) {
    final before = expected;
    final placement = before.placement;
    final nextPlacement = tag.placement;
    final operation = wire.AuthoringOperation.createPatchTag(
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
    return apply([operation]);
  }

  Future<wire.ApplyAuthoringBatchResponse> deleteTag(skir.RecordId id) =>
      apply([wire.AuthoringOperation.createDeleteTag(id: id)]);
}

wire.Int32Change? _intChange(int expected, int value) => expected == value
    ? null
    : wire.Int32Change(expected: expected, value: value);
