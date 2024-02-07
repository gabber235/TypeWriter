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
mixin _$PageData {
  String get name => throw _privateConstructorUsedError;
  PageType get type => throw _privateConstructorUsedError;
  String get chapter => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  _$PageDataCopyWith<_PageData> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$PageDataCopyWith<$Res> {
  factory _$PageDataCopyWith(_PageData value, $Res Function(_PageData) then) =
      __$PageDataCopyWithImpl<$Res, _PageData>;
  @useResult
  $Res call({String name, PageType type, String chapter});
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
    Object? chapter = null,
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
      chapter: null == chapter
          ? _value.chapter
          : chapter // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$$__PageDataImplCopyWith<$Res>
    implements _$PageDataCopyWith<$Res> {
  factory _$$$__PageDataImplCopyWith(
          _$$__PageDataImpl value, $Res Function(_$$__PageDataImpl) then) =
      __$$$__PageDataImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String name, PageType type, String chapter});
}

/// @nodoc
class __$$$__PageDataImplCopyWithImpl<$Res>
    extends __$PageDataCopyWithImpl<$Res, _$$__PageDataImpl>
    implements _$$$__PageDataImplCopyWith<$Res> {
  __$$$__PageDataImplCopyWithImpl(
      _$$__PageDataImpl _value, $Res Function(_$$__PageDataImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? type = null,
    Object? chapter = null,
  }) {
    return _then(_$$__PageDataImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as PageType,
      chapter: null == chapter
          ? _value.chapter
          : chapter // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$$__PageDataImpl implements _$__PageData {
  const _$$__PageDataImpl(
      {required this.name, required this.type, required this.chapter});

  @override
  final String name;
  @override
  final PageType type;
  @override
  final String chapter;

  @override
  String toString() {
    return '_PageData(name: $name, type: $type, chapter: $chapter)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$$__PageDataImpl &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.type, type) || other.type == type) &&
            (identical(other.chapter, chapter) || other.chapter == chapter));
  }

  @override
  int get hashCode => Object.hash(runtimeType, name, type, chapter);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$$__PageDataImplCopyWith<_$$__PageDataImpl> get copyWith =>
      __$$$__PageDataImplCopyWithImpl<_$$__PageDataImpl>(this, _$identity);
}

abstract class _$__PageData implements _PageData {
  const factory _$__PageData(
      {required final String name,
      required final PageType type,
      required final String chapter}) = _$$__PageDataImpl;

  @override
  String get name;
  @override
  PageType get type;
  @override
  String get chapter;
  @override
  @JsonKey(ignore: true)
  _$$$__PageDataImplCopyWith<_$$__PageDataImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
