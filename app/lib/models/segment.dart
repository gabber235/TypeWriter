import "package:collection_ext/all.dart";
import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:freezed_annotation/freezed_annotation.dart";

part "segment.freezed.dart";

@freezed
class Segment with _$Segment {
  const factory Segment({
    @Default("") String path,
    @Default(0) int index,
    @Default(Colors.white) Color color,
    @Default(FontAwesomeIcons.star) IconData icon,
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

  bool contains(int frame) => frame >= startFrame && frame < endFrame;

  bool overlaps(int startFrame, int endFrame) =>
      contains(startFrame) || contains(endFrame);
}
