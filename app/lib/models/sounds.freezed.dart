// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'sounds.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

SoundData _$SoundDataFromJson(Map<String, dynamic> json) {
  return _SoundData.fromJson(json);
}

/// @nodoc
mixin _$SoundData {
  String get name => throw _privateConstructorUsedError;
  int get weight => throw _privateConstructorUsedError;
  double get volume => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $SoundDataCopyWith<SoundData> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $SoundDataCopyWith<$Res> {
  factory $SoundDataCopyWith(SoundData value, $Res Function(SoundData) then) =
      _$SoundDataCopyWithImpl<$Res, SoundData>;
  @useResult
  $Res call({String name, int weight, double volume});
}

/// @nodoc
class _$SoundDataCopyWithImpl<$Res, $Val extends SoundData>
    implements $SoundDataCopyWith<$Res> {
  _$SoundDataCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? weight = null,
    Object? volume = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      weight: null == weight
          ? _value.weight
          : weight // ignore: cast_nullable_to_non_nullable
              as int,
      volume: null == volume
          ? _value.volume
          : volume // ignore: cast_nullable_to_non_nullable
              as double,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_SoundDataCopyWith<$Res> implements $SoundDataCopyWith<$Res> {
  factory _$$_SoundDataCopyWith(
          _$_SoundData value, $Res Function(_$_SoundData) then) =
      __$$_SoundDataCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String name, int weight, double volume});
}

/// @nodoc
class __$$_SoundDataCopyWithImpl<$Res>
    extends _$SoundDataCopyWithImpl<$Res, _$_SoundData>
    implements _$$_SoundDataCopyWith<$Res> {
  __$$_SoundDataCopyWithImpl(
      _$_SoundData _value, $Res Function(_$_SoundData) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? weight = null,
    Object? volume = null,
  }) {
    return _then(_$_SoundData(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      weight: null == weight
          ? _value.weight
          : weight // ignore: cast_nullable_to_non_nullable
              as int,
      volume: null == volume
          ? _value.volume
          : volume // ignore: cast_nullable_to_non_nullable
              as double,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$_SoundData implements _SoundData {
  const _$_SoundData({this.name = "", this.weight = 1, this.volume = 1});

  factory _$_SoundData.fromJson(Map<String, dynamic> json) =>
      _$$_SoundDataFromJson(json);

  @override
  @JsonKey()
  final String name;
  @override
  @JsonKey()
  final int weight;
  @override
  @JsonKey()
  final double volume;

  @override
  String toString() {
    return 'SoundData(name: $name, weight: $weight, volume: $volume)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_SoundData &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.weight, weight) || other.weight == weight) &&
            (identical(other.volume, volume) || other.volume == volume));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, name, weight, volume);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_SoundDataCopyWith<_$_SoundData> get copyWith =>
      __$$_SoundDataCopyWithImpl<_$_SoundData>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$_SoundDataToJson(
      this,
    );
  }
}

abstract class _SoundData implements SoundData {
  const factory _SoundData(
      {final String name,
      final int weight,
      final double volume}) = _$_SoundData;

  factory _SoundData.fromJson(Map<String, dynamic> json) =
      _$_SoundData.fromJson;

  @override
  String get name;
  @override
  int get weight;
  @override
  double get volume;
  @override
  @JsonKey(ignore: true)
  _$$_SoundDataCopyWith<_$_SoundData> get copyWith =>
      throw _privateConstructorUsedError;
}
