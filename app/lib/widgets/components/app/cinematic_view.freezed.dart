// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'cinematic_view.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$FrameLine {
  int get frame => throw _privateConstructorUsedError;
  double get offset => throw _privateConstructorUsedError;
  bool get primary => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  _$FrameLineCopyWith<_FrameLine> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$FrameLineCopyWith<$Res> {
  factory _$FrameLineCopyWith(
          _FrameLine value, $Res Function(_FrameLine) then) =
      __$FrameLineCopyWithImpl<$Res, _FrameLine>;
  @useResult
  $Res call({int frame, double offset, bool primary});
}

/// @nodoc
class __$FrameLineCopyWithImpl<$Res, $Val extends _FrameLine>
    implements _$FrameLineCopyWith<$Res> {
  __$FrameLineCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? frame = null,
    Object? offset = null,
    Object? primary = null,
  }) {
    return _then(_value.copyWith(
      frame: null == frame
          ? _value.frame
          : frame // ignore: cast_nullable_to_non_nullable
              as int,
      offset: null == offset
          ? _value.offset
          : offset // ignore: cast_nullable_to_non_nullable
              as double,
      primary: null == primary
          ? _value.primary
          : primary // ignore: cast_nullable_to_non_nullable
              as bool,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$$__FrameLineImplCopyWith<$Res>
    implements _$FrameLineCopyWith<$Res> {
  factory _$$$__FrameLineImplCopyWith(
          _$$__FrameLineImpl value, $Res Function(_$$__FrameLineImpl) then) =
      __$$$__FrameLineImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({int frame, double offset, bool primary});
}

/// @nodoc
class __$$$__FrameLineImplCopyWithImpl<$Res>
    extends __$FrameLineCopyWithImpl<$Res, _$$__FrameLineImpl>
    implements _$$$__FrameLineImplCopyWith<$Res> {
  __$$$__FrameLineImplCopyWithImpl(
      _$$__FrameLineImpl _value, $Res Function(_$$__FrameLineImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? frame = null,
    Object? offset = null,
    Object? primary = null,
  }) {
    return _then(_$$__FrameLineImpl(
      frame: null == frame
          ? _value.frame
          : frame // ignore: cast_nullable_to_non_nullable
              as int,
      offset: null == offset
          ? _value.offset
          : offset // ignore: cast_nullable_to_non_nullable
              as double,
      primary: null == primary
          ? _value.primary
          : primary // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// @nodoc

class _$$__FrameLineImpl implements _$__FrameLine {
  const _$$__FrameLineImpl(
      {required this.frame, required this.offset, required this.primary});

  @override
  final int frame;
  @override
  final double offset;
  @override
  final bool primary;

  @override
  String toString() {
    return '_FrameLine(frame: $frame, offset: $offset, primary: $primary)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$$__FrameLineImpl &&
            (identical(other.frame, frame) || other.frame == frame) &&
            (identical(other.offset, offset) || other.offset == offset) &&
            (identical(other.primary, primary) || other.primary == primary));
  }

  @override
  int get hashCode => Object.hash(runtimeType, frame, offset, primary);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$$__FrameLineImplCopyWith<_$$__FrameLineImpl> get copyWith =>
      __$$$__FrameLineImplCopyWithImpl<_$$__FrameLineImpl>(this, _$identity);
}

abstract class _$__FrameLine implements _FrameLine {
  const factory _$__FrameLine(
      {required final int frame,
      required final double offset,
      required final bool primary}) = _$$__FrameLineImpl;

  @override
  int get frame;
  @override
  double get offset;
  @override
  bool get primary;
  @override
  @JsonKey(ignore: true)
  _$$$__FrameLineImplCopyWith<_$$__FrameLineImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$MoveState {
  Segment? get previousSegment => throw _privateConstructorUsedError;
  Segment? get nextSegment => throw _privateConstructorUsedError;
  double get innerPercent => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  _$MoveStateCopyWith<_MoveState> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$MoveStateCopyWith<$Res> {
  factory _$MoveStateCopyWith(
          _MoveState value, $Res Function(_MoveState) then) =
      __$MoveStateCopyWithImpl<$Res, _MoveState>;
  @useResult
  $Res call(
      {Segment? previousSegment, Segment? nextSegment, double innerPercent});

  $SegmentCopyWith<$Res>? get previousSegment;
  $SegmentCopyWith<$Res>? get nextSegment;
}

/// @nodoc
class __$MoveStateCopyWithImpl<$Res, $Val extends _MoveState>
    implements _$MoveStateCopyWith<$Res> {
  __$MoveStateCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? previousSegment = freezed,
    Object? nextSegment = freezed,
    Object? innerPercent = null,
  }) {
    return _then(_value.copyWith(
      previousSegment: freezed == previousSegment
          ? _value.previousSegment
          : previousSegment // ignore: cast_nullable_to_non_nullable
              as Segment?,
      nextSegment: freezed == nextSegment
          ? _value.nextSegment
          : nextSegment // ignore: cast_nullable_to_non_nullable
              as Segment?,
      innerPercent: null == innerPercent
          ? _value.innerPercent
          : innerPercent // ignore: cast_nullable_to_non_nullable
              as double,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $SegmentCopyWith<$Res>? get previousSegment {
    if (_value.previousSegment == null) {
      return null;
    }

    return $SegmentCopyWith<$Res>(_value.previousSegment!, (value) {
      return _then(_value.copyWith(previousSegment: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $SegmentCopyWith<$Res>? get nextSegment {
    if (_value.nextSegment == null) {
      return null;
    }

    return $SegmentCopyWith<$Res>(_value.nextSegment!, (value) {
      return _then(_value.copyWith(nextSegment: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$$__MoveStateImplCopyWith<$Res>
    implements _$MoveStateCopyWith<$Res> {
  factory _$$$__MoveStateImplCopyWith(
          _$$__MoveStateImpl value, $Res Function(_$$__MoveStateImpl) then) =
      __$$$__MoveStateImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {Segment? previousSegment, Segment? nextSegment, double innerPercent});

  @override
  $SegmentCopyWith<$Res>? get previousSegment;
  @override
  $SegmentCopyWith<$Res>? get nextSegment;
}

/// @nodoc
class __$$$__MoveStateImplCopyWithImpl<$Res>
    extends __$MoveStateCopyWithImpl<$Res, _$$__MoveStateImpl>
    implements _$$$__MoveStateImplCopyWith<$Res> {
  __$$$__MoveStateImplCopyWithImpl(
      _$$__MoveStateImpl _value, $Res Function(_$$__MoveStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? previousSegment = freezed,
    Object? nextSegment = freezed,
    Object? innerPercent = null,
  }) {
    return _then(_$$__MoveStateImpl(
      previousSegment: freezed == previousSegment
          ? _value.previousSegment
          : previousSegment // ignore: cast_nullable_to_non_nullable
              as Segment?,
      nextSegment: freezed == nextSegment
          ? _value.nextSegment
          : nextSegment // ignore: cast_nullable_to_non_nullable
              as Segment?,
      innerPercent: null == innerPercent
          ? _value.innerPercent
          : innerPercent // ignore: cast_nullable_to_non_nullable
              as double,
    ));
  }
}

/// @nodoc

class _$$__MoveStateImpl implements _$__MoveState {
  const _$$__MoveStateImpl(
      {required this.previousSegment,
      required this.nextSegment,
      this.innerPercent = 0.0});

  @override
  final Segment? previousSegment;
  @override
  final Segment? nextSegment;
  @override
  @JsonKey()
  final double innerPercent;

  @override
  String toString() {
    return '_MoveState(previousSegment: $previousSegment, nextSegment: $nextSegment, innerPercent: $innerPercent)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$$__MoveStateImpl &&
            (identical(other.previousSegment, previousSegment) ||
                other.previousSegment == previousSegment) &&
            (identical(other.nextSegment, nextSegment) ||
                other.nextSegment == nextSegment) &&
            (identical(other.innerPercent, innerPercent) ||
                other.innerPercent == innerPercent));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, previousSegment, nextSegment, innerPercent);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$$__MoveStateImplCopyWith<_$$__MoveStateImpl> get copyWith =>
      __$$$__MoveStateImplCopyWithImpl<_$$__MoveStateImpl>(this, _$identity);
}

abstract class _$__MoveState implements _MoveState {
  const factory _$__MoveState(
      {required final Segment? previousSegment,
      required final Segment? nextSegment,
      final double innerPercent}) = _$$__MoveStateImpl;

  @override
  Segment? get previousSegment;
  @override
  Segment? get nextSegment;
  @override
  double get innerPercent;
  @override
  @JsonKey(ignore: true)
  _$$$__MoveStateImplCopyWith<_$$__MoveStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$TrackState {
// ignore: unused_element
  double get start =>
      throw _privateConstructorUsedError; // ignore: unused_element
  double get end =>
      throw _privateConstructorUsedError; // ignore: unused_element
  int get totalFrames =>
      throw _privateConstructorUsedError; // ignore: unused_element
  double get width => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  _$TrackStateCopyWith<_TrackState> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$TrackStateCopyWith<$Res> {
  factory _$TrackStateCopyWith(
          _TrackState value, $Res Function(_TrackState) then) =
      __$TrackStateCopyWithImpl<$Res, _TrackState>;
  @useResult
  $Res call({double start, double end, int totalFrames, double width});
}

/// @nodoc
class __$TrackStateCopyWithImpl<$Res, $Val extends _TrackState>
    implements _$TrackStateCopyWith<$Res> {
  __$TrackStateCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? start = null,
    Object? end = null,
    Object? totalFrames = null,
    Object? width = null,
  }) {
    return _then(_value.copyWith(
      start: null == start
          ? _value.start
          : start // ignore: cast_nullable_to_non_nullable
              as double,
      end: null == end
          ? _value.end
          : end // ignore: cast_nullable_to_non_nullable
              as double,
      totalFrames: null == totalFrames
          ? _value.totalFrames
          : totalFrames // ignore: cast_nullable_to_non_nullable
              as int,
      width: null == width
          ? _value.width
          : width // ignore: cast_nullable_to_non_nullable
              as double,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$$__TrackStateImplCopyWith<$Res>
    implements _$TrackStateCopyWith<$Res> {
  factory _$$$__TrackStateImplCopyWith(
          _$$__TrackStateImpl value, $Res Function(_$$__TrackStateImpl) then) =
      __$$$__TrackStateImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({double start, double end, int totalFrames, double width});
}

/// @nodoc
class __$$$__TrackStateImplCopyWithImpl<$Res>
    extends __$TrackStateCopyWithImpl<$Res, _$$__TrackStateImpl>
    implements _$$$__TrackStateImplCopyWith<$Res> {
  __$$$__TrackStateImplCopyWithImpl(
      _$$__TrackStateImpl _value, $Res Function(_$$__TrackStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? start = null,
    Object? end = null,
    Object? totalFrames = null,
    Object? width = null,
  }) {
    return _then(_$$__TrackStateImpl(
      null == start
          ? _value.start
          : start // ignore: cast_nullable_to_non_nullable
              as double,
      null == end
          ? _value.end
          : end // ignore: cast_nullable_to_non_nullable
              as double,
      null == totalFrames
          ? _value.totalFrames
          : totalFrames // ignore: cast_nullable_to_non_nullable
              as int,
      null == width
          ? _value.width
          : width // ignore: cast_nullable_to_non_nullable
              as double,
    ));
  }
}

/// @nodoc

class _$$__TrackStateImpl implements _$__TrackState {
  const _$$__TrackStateImpl(
      [this.start = 0, this.end = 1, this.totalFrames = 0, this.width = 0]);

// ignore: unused_element
  @override
  @JsonKey()
  final double start;
// ignore: unused_element
  @override
  @JsonKey()
  final double end;
// ignore: unused_element
  @override
  @JsonKey()
  final int totalFrames;
// ignore: unused_element
  @override
  @JsonKey()
  final double width;

  @override
  String toString() {
    return '_TrackState(start: $start, end: $end, totalFrames: $totalFrames, width: $width)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$$__TrackStateImpl &&
            (identical(other.start, start) || other.start == start) &&
            (identical(other.end, end) || other.end == end) &&
            (identical(other.totalFrames, totalFrames) ||
                other.totalFrames == totalFrames) &&
            (identical(other.width, width) || other.width == width));
  }

  @override
  int get hashCode => Object.hash(runtimeType, start, end, totalFrames, width);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$$__TrackStateImplCopyWith<_$$__TrackStateImpl> get copyWith =>
      __$$$__TrackStateImplCopyWithImpl<_$$__TrackStateImpl>(this, _$identity);
}

abstract class _$__TrackState implements _TrackState {
  const factory _$__TrackState(
      [final double start,
      final double end,
      final int totalFrames,
      final double width]) = _$$__TrackStateImpl;

  @override // ignore: unused_element
  double get start;
  @override // ignore: unused_element
  double get end;
  @override // ignore: unused_element
  int get totalFrames;
  @override // ignore: unused_element
  double get width;
  @override
  @JsonKey(ignore: true)
  _$$$__TrackStateImplCopyWith<_$$__TrackStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
