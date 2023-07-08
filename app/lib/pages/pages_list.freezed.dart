// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'pages_list.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$_PageData {
  String get name => throw _privateConstructorUsedError;
  PageType get type => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  _$PageDataCopyWith<_PageData> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$PageDataCopyWith<$Res> {
  factory _$PageDataCopyWith(_PageData value, $Res Function(_PageData) then) =
      __$PageDataCopyWithImpl<$Res, _PageData>;
  @useResult
  $Res call({String name, PageType type});
}

/// @nodoc
class __$PageDataCopyWithImpl<$Res, $Val extends _PageData>
    implements _$PageDataCopyWith<$Res> {
  __$PageDataCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? type = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as PageType,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_$PageDataCopyWith<$Res> implements _$PageDataCopyWith<$Res> {
  factory _$$_$PageDataCopyWith(
          _$_$PageData value, $Res Function(_$_$PageData) then) =
      __$$_$PageDataCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String name, PageType type});
}

/// @nodoc
class __$$_$PageDataCopyWithImpl<$Res>
    extends __$PageDataCopyWithImpl<$Res, _$_$PageData>
    implements _$$_$PageDataCopyWith<$Res> {
  __$$_$PageDataCopyWithImpl(
      _$_$PageData _value, $Res Function(_$_$PageData) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? type = null,
  }) {
    return _then(_$_$PageData(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as PageType,
    ));
  }
}

/// @nodoc

class _$_$PageData implements _$PageData {
  const _$_$PageData({required this.name, required this.type});

  @override
  final String name;
  @override
  final PageType type;

  @override
  String toString() {
    return '_PageData(name: $name, type: $type)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_$PageData &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.type, type) || other.type == type));
  }

  @override
  int get hashCode => Object.hash(runtimeType, name, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_$PageDataCopyWith<_$_$PageData> get copyWith =>
      __$$_$PageDataCopyWithImpl<_$_$PageData>(this, _$identity);
}

abstract class _$PageData implements _PageData {
  const factory _$PageData(
      {required final String name,
      required final PageType type}) = _$_$PageData;

  @override
  String get name;
  @override
  PageType get type;
  @override
  @JsonKey(ignore: true)
  _$$_$PageDataCopyWith<_$_$PageData> get copyWith =>
      throw _privateConstructorUsedError;
}
