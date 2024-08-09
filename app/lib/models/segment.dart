import "package:collection_ext/all.dart";
import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";

part "segment.g.dart";
part "segment.freezed.dart";

@riverpod
List<Writer> segmentWriters(
  SegmentWritersRef ref,
  String entryId,
  String segmentId,
) {
  return ref.watch(writersProvider).where((writer) {
    if (writer.entryId.isNullOrEmpty) return false;
    if (writer.entryId != entryId) return false;
    if (writer.field.isNullOrEmpty) return false;

    return writer.field!.startsWith(segmentId);
  }).toList();
}

@freezed
class Segment with _$Segment {
  const factory Segment({
    @Default("") String path,
    @Default(0) int index,
    @Default(Colors.white) Color color,
    @Default(TWIcons.star) String icon,
    @Default(0) int startFrame,
    @Default(0) int endFrame,
    int? minFrames,
    int? maxFrames,
    @Default({}) Map<String, dynamic> data,
  }) = _Segment;
}

extension SegmentX on Segment {
  String get truePath => path.replaceFirst("*", index.toString());

  IntRange get range => IntRange(startFrame, endFrame);

  bool get isSingleFrame => maxFrames == 1;

  bool contains(int frame) => frame >= startFrame && frame < endFrame;

  bool overlaps(int startFrame, int endFrame) =>
      contains(startFrame) || contains(endFrame);

  String get display {
    if (isSingleFrame) {
      return "$startFrame";
    }

    return "[$startFrame, $endFrame]";
  }
}
