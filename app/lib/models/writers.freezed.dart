// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'writers.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

Writer _$WriterFromJson(Map<String, dynamic> json) {
  return _Writer.fromJson(json);
}

/// @nodoc
mixin _$Writer {
  String get id => throw _privateConstructorUsedError;
  String? get iconUrl => throw _privateConstructorUsedError;
  String? get pageId => throw _privateConstructorUsedError;
  String? get entryId => throw _privateConstructorUsedError;
  String? get field => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $WriterCopyWith<Writer> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $WriterCopyWith<$Res> {
  factory $WriterCopyWith(Writer value, $Res Function(Writer) then) =
      _$WriterCopyWithImpl<$Res, Writer>;
  @useResult
  $Res call(
      {String id,
      String? iconUrl,
      String? pageId,
      String? entryId,
      String? field});
}

/// @nodoc
class _$WriterCopyWithImpl<$Res, $Val extends Writer>
    implements $WriterCopyWith<$Res> {
  _$WriterCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? iconUrl = freezed,
    Object? pageId = freezed,
    Object? entryId = freezed,
    Object? field = freezed,
  }) {
    return _then(_value.copyWith(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      iconUrl: freezed == iconUrl
          ? _value.iconUrl
          : iconUrl // ignore: cast_nullable_to_non_nullable
              as String?,
      pageId: freezed == pageId
          ? _value.pageId
          : pageId // ignore: cast_nullable_to_non_nullable
              as String?,
      entryId: freezed == entryId
          ? _value.entryId
          : entryId // ignore: cast_nullable_to_non_nullable
              as String?,
      field: freezed == field
          ? _value.field
          : field // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$WriterImplCopyWith<$Res> implements $WriterCopyWith<$Res> {
  factory _$$WriterImplCopyWith(
          _$WriterImpl value, $Res Function(_$WriterImpl) then) =
      __$$WriterImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String id,
      String? iconUrl,
      String? pageId,
      String? entryId,
      String? field});
}

/// @nodoc
class __$$WriterImplCopyWithImpl<$Res>
    extends _$WriterCopyWithImpl<$Res, _$WriterImpl>
    implements _$$WriterImplCopyWith<$Res> {
  __$$WriterImplCopyWithImpl(
      _$WriterImpl _value, $Res Function(_$WriterImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? iconUrl = freezed,
    Object? pageId = freezed,
    Object? entryId = freezed,
    Object? field = freezed,
  }) {
    return _then(_$WriterImpl(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      iconUrl: freezed == iconUrl
          ? _value.iconUrl
          : iconUrl // ignore: cast_nullable_to_non_nullable
              as String?,
      pageId: freezed == pageId
          ? _value.pageId
          : pageId // ignore: cast_nullable_to_non_nullable
              as String?,
      entryId: freezed == entryId
          ? _value.entryId
          : entryId // ignore: cast_nullable_to_non_nullable
              as String?,
      field: freezed == field
          ? _value.field
          : field // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$WriterImpl implements _Writer {
  const _$WriterImpl(
      {required this.id, this.iconUrl, this.pageId, this.entryId, this.field});

  factory _$WriterImpl.fromJson(Map<String, dynamic> json) =>
      _$$WriterImplFromJson(json);

  @override
  final String id;
  @override
  final String? iconUrl;
  @override
  final String? pageId;
  @override
  final String? entryId;
  @override
  final String? field;

  @override
  String toString() {
    return 'Writer(id: $id, iconUrl: $iconUrl, pageId: $pageId, entryId: $entryId, field: $field)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$WriterImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.iconUrl, iconUrl) || other.iconUrl == iconUrl) &&
            (identical(other.pageId, pageId) || other.pageId == pageId) &&
            (identical(other.entryId, entryId) || other.entryId == entryId) &&
            (identical(other.field, field) || other.field == field));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, id, iconUrl, pageId, entryId, field);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$WriterImplCopyWith<_$WriterImpl> get copyWith =>
      __$$WriterImplCopyWithImpl<_$WriterImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$WriterImplToJson(
      this,
    );
  }
}

abstract class _Writer implements Writer {
  const factory _Writer(
      {required final String id,
      final String? iconUrl,
      final String? pageId,
      final String? entryId,
      final String? field}) = _$WriterImpl;

  factory _Writer.fromJson(Map<String, dynamic> json) = _$WriterImpl.fromJson;

  @override
  String get id;
  @override
  String? get iconUrl;
  @override
  String? get pageId;
  @override
  String? get entryId;
  @override
  String? get field;
  @override
  @JsonKey(ignore: true)
  _$$WriterImplCopyWith<_$WriterImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
