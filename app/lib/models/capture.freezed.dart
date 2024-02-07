// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'capture.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

CaptureRequest _$CaptureRequestFromJson(Map<String, dynamic> json) {
  return _CaptureRequest.fromJson(json);
}

/// @nodoc
mixin _$CaptureRequest {
  String get capturerClassPath => throw _privateConstructorUsedError;
  String get entryId => throw _privateConstructorUsedError;
  String get fieldPath => throw _privateConstructorUsedError;
  dynamic get fieldValue => throw _privateConstructorUsedError;
  String? get cinematic => throw _privateConstructorUsedError;
  @IntRangeConverter()
  IntRange? get cinematicRange => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $CaptureRequestCopyWith<CaptureRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $CaptureRequestCopyWith<$Res> {
  factory $CaptureRequestCopyWith(
          CaptureRequest value, $Res Function(CaptureRequest) then) =
      _$CaptureRequestCopyWithImpl<$Res, CaptureRequest>;
  @useResult
  $Res call(
      {String capturerClassPath,
      String entryId,
      String fieldPath,
      dynamic fieldValue,
      String? cinematic,
      @IntRangeConverter() IntRange? cinematicRange});
}

/// @nodoc
class _$CaptureRequestCopyWithImpl<$Res, $Val extends CaptureRequest>
    implements $CaptureRequestCopyWith<$Res> {
  _$CaptureRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? capturerClassPath = null,
    Object? entryId = null,
    Object? fieldPath = null,
    Object? fieldValue = freezed,
    Object? cinematic = freezed,
    Object? cinematicRange = freezed,
  }) {
    return _then(_value.copyWith(
      capturerClassPath: null == capturerClassPath
          ? _value.capturerClassPath
          : capturerClassPath // ignore: cast_nullable_to_non_nullable
              as String,
      entryId: null == entryId
          ? _value.entryId
          : entryId // ignore: cast_nullable_to_non_nullable
              as String,
      fieldPath: null == fieldPath
          ? _value.fieldPath
          : fieldPath // ignore: cast_nullable_to_non_nullable
              as String,
      fieldValue: freezed == fieldValue
          ? _value.fieldValue
          : fieldValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      cinematic: freezed == cinematic
          ? _value.cinematic
          : cinematic // ignore: cast_nullable_to_non_nullable
              as String?,
      cinematicRange: freezed == cinematicRange
          ? _value.cinematicRange
          : cinematicRange // ignore: cast_nullable_to_non_nullable
              as IntRange?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$CaptureRequestImplCopyWith<$Res>
    implements $CaptureRequestCopyWith<$Res> {
  factory _$$CaptureRequestImplCopyWith(_$CaptureRequestImpl value,
          $Res Function(_$CaptureRequestImpl) then) =
      __$$CaptureRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String capturerClassPath,
      String entryId,
      String fieldPath,
      dynamic fieldValue,
      String? cinematic,
      @IntRangeConverter() IntRange? cinematicRange});
}

/// @nodoc
class __$$CaptureRequestImplCopyWithImpl<$Res>
    extends _$CaptureRequestCopyWithImpl<$Res, _$CaptureRequestImpl>
    implements _$$CaptureRequestImplCopyWith<$Res> {
  __$$CaptureRequestImplCopyWithImpl(
      _$CaptureRequestImpl _value, $Res Function(_$CaptureRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? capturerClassPath = null,
    Object? entryId = null,
    Object? fieldPath = null,
    Object? fieldValue = freezed,
    Object? cinematic = freezed,
    Object? cinematicRange = freezed,
  }) {
    return _then(_$CaptureRequestImpl(
      capturerClassPath: null == capturerClassPath
          ? _value.capturerClassPath
          : capturerClassPath // ignore: cast_nullable_to_non_nullable
              as String,
      entryId: null == entryId
          ? _value.entryId
          : entryId // ignore: cast_nullable_to_non_nullable
              as String,
      fieldPath: null == fieldPath
          ? _value.fieldPath
          : fieldPath // ignore: cast_nullable_to_non_nullable
              as String,
      fieldValue: freezed == fieldValue
          ? _value.fieldValue
          : fieldValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      cinematic: freezed == cinematic
          ? _value.cinematic
          : cinematic // ignore: cast_nullable_to_non_nullable
              as String?,
      cinematicRange: freezed == cinematicRange
          ? _value.cinematicRange
          : cinematicRange // ignore: cast_nullable_to_non_nullable
              as IntRange?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$CaptureRequestImpl implements _CaptureRequest {
  const _$CaptureRequestImpl(
      {required this.capturerClassPath,
      required this.entryId,
      required this.fieldPath,
      required this.fieldValue,
      this.cinematic,
      @IntRangeConverter() this.cinematicRange});

  factory _$CaptureRequestImpl.fromJson(Map<String, dynamic> json) =>
      _$$CaptureRequestImplFromJson(json);

  @override
  final String capturerClassPath;
  @override
  final String entryId;
  @override
  final String fieldPath;
  @override
  final dynamic fieldValue;
  @override
  final String? cinematic;
  @override
  @IntRangeConverter()
  final IntRange? cinematicRange;

  @override
  String toString() {
    return 'CaptureRequest(capturerClassPath: $capturerClassPath, entryId: $entryId, fieldPath: $fieldPath, fieldValue: $fieldValue, cinematic: $cinematic, cinematicRange: $cinematicRange)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$CaptureRequestImpl &&
            (identical(other.capturerClassPath, capturerClassPath) ||
                other.capturerClassPath == capturerClassPath) &&
            (identical(other.entryId, entryId) || other.entryId == entryId) &&
            (identical(other.fieldPath, fieldPath) ||
                other.fieldPath == fieldPath) &&
            const DeepCollectionEquality()
                .equals(other.fieldValue, fieldValue) &&
            (identical(other.cinematic, cinematic) ||
                other.cinematic == cinematic) &&
            const DeepCollectionEquality()
                .equals(other.cinematicRange, cinematicRange));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      capturerClassPath,
      entryId,
      fieldPath,
      const DeepCollectionEquality().hash(fieldValue),
      cinematic,
      const DeepCollectionEquality().hash(cinematicRange));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$CaptureRequestImplCopyWith<_$CaptureRequestImpl> get copyWith =>
      __$$CaptureRequestImplCopyWithImpl<_$CaptureRequestImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$CaptureRequestImplToJson(
      this,
    );
  }
}

abstract class _CaptureRequest implements CaptureRequest {
  const factory _CaptureRequest(
          {required final String capturerClassPath,
          required final String entryId,
          required final String fieldPath,
          required final dynamic fieldValue,
          final String? cinematic,
          @IntRangeConverter() final IntRange? cinematicRange}) =
      _$CaptureRequestImpl;

  factory _CaptureRequest.fromJson(Map<String, dynamic> json) =
      _$CaptureRequestImpl.fromJson;

  @override
  String get capturerClassPath;
  @override
  String get entryId;
  @override
  String get fieldPath;
  @override
  dynamic get fieldValue;
  @override
  String? get cinematic;
  @override
  @IntRangeConverter()
  IntRange? get cinematicRange;
  @override
  @JsonKey(ignore: true)
  _$$CaptureRequestImplCopyWith<_$CaptureRequestImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
