import "dart:typed_data";

import "data_value.dart";

extension DataValueReading on DataValue {
  UnitValue? get asUnitOrNull => switch (this) {
    UnitValue() => this as UnitValue,
    _ => null,
  };

  bool? get asBooleanOrNull => switch (this) {
    BooleanValue(:final value) => value,
    _ => null,
  };

  BigInt? get asBigIntOrNull => switch (this) {
    IntegerValue(:final value) => value,
    _ => null,
  };

  double? get asDoubleOrNull => switch (this) {
    FloatValue(:final value) => value,
    _ => null,
  };

  String? get asDecimalOrNull => switch (this) {
    DecimalValue(:final value) => value,
    _ => null,
  };

  String? get asStringOrNull => switch (this) {
    StringValue(:final value) => value,
    _ => null,
  };

  Uint8List? get asBytesOrNull => switch (this) {
    BytesValue(:final value) => value,
    _ => null,
  };

  DateTime? get asDateTimeOrNull => switch (this) {
    TimestampValue(:final value) => value,
    _ => null,
  };

  Duration? get asDurationOrNull => switch (this) {
    DurationValue(:final value) => value,
    _ => null,
  };

  List<DataValue>? get asListOrNull => switch (this) {
    ListValue(:final values) => values,
    _ => null,
  };

  List<DataMapEntry>? get asMapEntriesOrNull => switch (this) {
    MapValue(:final entries) => entries,
    _ => null,
  };

  Map<String, DataValue>? get asRecordOrNull => switch (this) {
    RecordValue(:final fields) => fields,
    _ => null,
  };

  PolymorphicValue? get asPolymorphicOrNull => switch (this) {
    PolymorphicValue() => this as PolymorphicValue,
    _ => null,
  };
}

extension DataValueStringWriting on String {
  DataValue get asValue => StringValue(this);
}

extension DataValueBooleanWriting on bool {
  DataValue get asValue => BooleanValue(this);
}

extension DataValueIntegerWriting on int {
  DataValue get asValue => IntegerValue(BigInt.from(this));
}

extension DataValueBigIntWriting on BigInt {
  DataValue get asValue => IntegerValue(this);
}

extension DataValueFloatWriting on double {
  DataValue get asValue => FloatValue(this);
}

extension DataValueBytesWriting on Uint8List {
  DataValue get asValue => BytesValue(this);
}

extension DataValueTimestampWriting on DateTime {
  DataValue get asValue => TimestampValue(this);
}

extension DataValueDurationWriting on Duration {
  DataValue get asValue => DurationValue(this);
}

extension DataValueListWriting on List<DataValue> {
  DataValue get asValue => ListValue(this);
}

extension DataValueMapEntriesWriting on List<DataMapEntry> {
  DataValue get asValue => MapValue(this);
}

extension DataValueMapWriting on Map<DataValue, DataValue> {
  DataValue get asValue => MapValue([
    for (final entry in entries)
      DataMapEntry(key: entry.key, value: entry.value),
  ]);
}

extension DataValueRecordWriting on Map<String, DataValue> {
  DataValue get asValue => RecordValue(this);
}
