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
mixin _$_TrackState {
  double get start => throw _privateConstructorUsedError;
  double get end => throw _privateConstructorUsedError;
  int get totalFrames => throw _privateConstructorUsedError;
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
abstract class _$$_$__TrackStateCopyWith<$Res>
    implements _$TrackStateCopyWith<$Res> {
  factory _$$_$__TrackStateCopyWith(
          _$_$__TrackState value, $Res Function(_$_$__TrackState) then) =
      __$$_$__TrackStateCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({double start, double end, int totalFrames, double width});
}

/// @nodoc
class __$$_$__TrackStateCopyWithImpl<$Res>
    extends __$TrackStateCopyWithImpl<$Res, _$_$__TrackState>
    implements _$$_$__TrackStateCopyWith<$Res> {
  __$$_$__TrackStateCopyWithImpl(
      _$_$__TrackState _value, $Res Function(_$_$__TrackState) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? start = null,
    Object? end = null,
    Object? totalFrames = null,
    Object? width = null,
  }) {
    return _then(_$_$__TrackState(
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
    ));
  }
}

/// @nodoc

class _$_$__TrackState implements _$__TrackState {
  const _$_$__TrackState(
      {this.start = 0, this.end = 1, this.totalFrames = 0, this.width = 0});

  @override
  @JsonKey()
  final double start;
  @override
  @JsonKey()
  final double end;
  @override
  @JsonKey()
  final int totalFrames;
  @override
  @JsonKey()
  final double width;

  @override
  String toString() {
    return '_TrackState(start: $start, end: $end, totalFrames: $totalFrames, width: $width)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_$__TrackState &&
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
  _$$_$__TrackStateCopyWith<_$_$__TrackState> get copyWith =>
      __$$_$__TrackStateCopyWithImpl<_$_$__TrackState>(this, _$identity);
}

abstract class _$__TrackState implements _TrackState {
  const factory _$__TrackState(
      {final double start,
      final double end,
      final int totalFrames,
      final double width}) = _$_$__TrackState;

  @override
  double get start;
  @override
  double get end;
  @override
  int get totalFrames;
  @override
  double get width;
  @override
  @JsonKey(ignore: true)
  _$$_$__TrackStateCopyWith<_$_$__TrackState> get copyWith =>
      throw _privateConstructorUsedError;
}
