import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

final elementValuePath = DataPath.root.field("value");
final elementPlacementPath = DataPath.root.field("placement");

RecordValue elementPlacementValue(wire.ElementPlacement placement) =>
    RecordValue({
      for (final entry in switch (placement) {
        wire.ElementPlacement_graphWrapper(:final value) => {
          "x": value.x,
          "y": value.y,
          "width": value.width,
          "height": value.height,
        },
        wire.ElementPlacement_timelineSegmentWrapper(:final value) => {
          "start": value.startFrame,
          "end": value.endFrame,
        },
        wire.ElementPlacement_timelineKeyframeWrapper(:final value) => {
          "frame": value.frame,
        },
        wire.ElementPlacement_timelineEntryWrapper(:final value) => {
          "track": value.trackIndex,
        },
        wire.ElementPlacement_unknown() => throw ArgumentError(
          "Unknown element placement",
        ),
      }.entries)
        entry.key: IntegerValue(BigInt.from(entry.value)),
    });

RecordType elementPlacementType(wire.ElementPlacement placement) => RecordType(
  fields: {
    for (final key in elementPlacementValue(placement).fields.keys)
      key: TypeField(
        name: key,
        type: const IntegerType(width: IntegerWidth.signed32),
      ),
  },
);

wire.ElementPlacement encodeElementPlacement(DataValue value) {
  if (value is! RecordValue)
    throw ArgumentError("Element placement must be a record");
  int number(String key) => (value.fields[key] as IntegerValue).value.toInt();
  if (value.fields.containsKey("x") &&
      number("width") > 0 &&
      number("height") > 0) {
    return wire.ElementPlacement.createGraph(
      x: number("x"),
      y: number("y"),
      width: number("width"),
      height: number("height"),
    );
  }
  if (value.fields.containsKey("start") &&
      number("start") >= 0 &&
      number("end") >= number("start")) {
    return wire.ElementPlacement.createTimelineSegment(
      startFrame: number("start"),
      endFrame: number("end"),
    );
  }
  if (value.fields.containsKey("frame") && number("frame") >= 0) {
    return wire.ElementPlacement.createTimelineKeyframe(frame: number("frame"));
  }
  if (value.fields.containsKey("track")) {
    return wire.ElementPlacement.createTimelineEntry(
      trackIndex: number("track"),
    );
  }
  throw ArgumentError("Invalid element placement");
}
