// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'segment.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$Segment {
  String get path => throw _privateConstructorUsedError;
  int get index => throw _privateConstructorUsedError;
  Color get color => throw _privateConstructorUsedError;
  IconData get icon => throw _privateConstructorUsedError;
  int get startFrame => throw _privateConstructorUsedError;
  int get endFrame => throw _privateConstructorUsedError;
  Map<String, dynamic> get data => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  $SegmentCopyWith<Segment> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $SegmentCopyWith<$Res> {
  factory $SegmentCopyWith(Segment value, $Res Function(Segment) then) =
      _$SegmentCopyWithImpl<$Res, Segment>;
  @useResult
  $Res call(
      {String path,
      int index,
      Color color,
      IconData icon,
      int startFrame,
      int endFrame,
      Map<String, dynamic> data});
}

/// @nodoc
class _$SegmentCopyWithImpl<$Res, $Val extends Segment>
    implements $SegmentCopyWith<$Res> {
  _$SegmentCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? path = null,
    Object? index = null,
    Object? color = null,
    Object? icon = null,
    Object? startFrame = null,
    Object? endFrame = null,
    Object? data = null,
  }) {
    return _then(_value.copyWith(
      path: null == path
          ? _value.path
          : path // ignore: cast_nullable_to_non_nullable
              as String,
      index: null == index
          ? _value.index
          : index // ignore: cast_nullable_to_non_nullable
              as int,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as IconData,
      startFrame: null == startFrame
          ? _value.startFrame
          : startFrame // ignore: cast_nullable_to_non_nullable
              as int,
      endFrame: null == endFrame
          ? _value.endFrame
          : endFrame // ignore: cast_nullable_to_non_nullable
              as int,
      data: null == data
          ? _value.data
          : data // ignore: cast_nullable_to_non_nullable
              as Map<String, dynamic>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_SegmentCopyWith<$Res> implements $SegmentCopyWith<$Res> {
  factory _$$_SegmentCopyWith(
          _$_Segment value, $Res Function(_$_Segment) then) =
      __$$_SegmentCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String path,
      int index,
      Color color,
      IconData icon,
      int startFrame,
      int endFrame,
      Map<String, dynamic> data});
}

/// @nodoc
class __$$_SegmentCopyWithImpl<$Res>
    extends _$SegmentCopyWithImpl<$Res, _$_Segment>
    implements _$$_SegmentCopyWith<$Res> {
  __$$_SegmentCopyWithImpl(_$_Segment _value, $Res Function(_$_Segment) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? path = null,
    Object? index = null,
    Object? color = null,
    Object? icon = null,
    Object? startFrame = null,
    Object? endFrame = null,
    Object? data = null,
  }) {
    return _then(_$_Segment(
      path: null == path
          ? _value.path
          : path // ignore: cast_nullable_to_non_nullable
              as String,
      index: null == index
          ? _value.index
          : index // ignore: cast_nullable_to_non_nullable
              as int,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as IconData,
      startFrame: null == startFrame
          ? _value.startFrame
          : startFrame // ignore: cast_nullable_to_non_nullable
              as int,
      endFrame: null == endFrame
          ? _value.endFrame
          : endFrame // ignore: cast_nullable_to_non_nullable
              as int,
      data: null == data
          ? _value._data
          : data // ignore: cast_nullable_to_non_nullable
              as Map<String, dynamic>,
    ));
  }
}

/// @nodoc

class _$_Segment implements _Segment {
  const _$_Segment(
      {this.path = "",
      this.index = 0,
      this.color = Colors.white,
      this.icon = FontAwesomeIcons.star,
      this.startFrame = 0,
      this.endFrame = 0,
      final Map<String, dynamic> data = const {}})
      : _data = data;

  @override
  @JsonKey()
  final String path;
  @override
  @JsonKey()
  final int index;
  @override
  @JsonKey()
  final Color color;
  @override
  @JsonKey()
  final IconData icon;
  @override
  @JsonKey()
  final int startFrame;
  @override
  @JsonKey()
  final int endFrame;
  final Map<String, dynamic> _data;
  @override
  @JsonKey()
  Map<String, dynamic> get data {
    if (_data is EqualUnmodifiableMapView) return _data;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_data);
  }

  @override
  String toString() {
    return 'Segment(path: $path, index: $index, color: $color, icon: $icon, startFrame: $startFrame, endFrame: $endFrame, data: $data)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_Segment &&
            (identical(other.path, path) || other.path == path) &&
            (identical(other.index, index) || other.index == index) &&
            (identical(other.color, color) || other.color == color) &&
            (identical(other.icon, icon) || other.icon == icon) &&
            (identical(other.startFrame, startFrame) ||
                other.startFrame == startFrame) &&
            (identical(other.endFrame, endFrame) ||
                other.endFrame == endFrame) &&
            const DeepCollectionEquality().equals(other._data, _data));
  }

  @override
  int get hashCode => Object.hash(runtimeType, path, index, color, icon,
      startFrame, endFrame, const DeepCollectionEquality().hash(_data));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_SegmentCopyWith<_$_Segment> get copyWith =>
      __$$_SegmentCopyWithImpl<_$_Segment>(this, _$identity);
}

abstract class _Segment implements Segment {
  const factory _Segment(
      {final String path,
      final int index,
      final Color color,
      final IconData icon,
      final int startFrame,
      final int endFrame,
      final Map<String, dynamic> data}) = _$_Segment;

  @override
  String get path;
  @override
  int get index;
  @override
  Color get color;
  @override
  IconData get icon;
  @override
  int get startFrame;
  @override
  int get endFrame;
  @override
  Map<String, dynamic> get data;
  @override
  @JsonKey(ignore: true)
  _$$_SegmentCopyWith<_$_Segment> get copyWith =>
      throw _privateConstructorUsedError;
}
