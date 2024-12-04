// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'header.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$HeaderContext {
  DataBlueprint? get parentBlueprint => throw _privateConstructorUsedError;
  DataBlueprint? get genericBlueprint => throw _privateConstructorUsedError;

  /// Create a copy of HeaderContext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $HeaderContextCopyWith<HeaderContext> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $HeaderContextCopyWith<$Res> {
  factory $HeaderContextCopyWith(
          HeaderContext value, $Res Function(HeaderContext) then) =
      _$HeaderContextCopyWithImpl<$Res, HeaderContext>;
  @useResult
  $Res call({DataBlueprint? parentBlueprint, DataBlueprint? genericBlueprint});

  $DataBlueprintCopyWith<$Res>? get parentBlueprint;
  $DataBlueprintCopyWith<$Res>? get genericBlueprint;
}

/// @nodoc
class _$HeaderContextCopyWithImpl<$Res, $Val extends HeaderContext>
    implements $HeaderContextCopyWith<$Res> {
  _$HeaderContextCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of HeaderContext
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? parentBlueprint = freezed,
    Object? genericBlueprint = freezed,
  }) {
    return _then(_value.copyWith(
      parentBlueprint: freezed == parentBlueprint
          ? _value.parentBlueprint
          : parentBlueprint // ignore: cast_nullable_to_non_nullable
              as DataBlueprint?,
      genericBlueprint: freezed == genericBlueprint
          ? _value.genericBlueprint
          : genericBlueprint // ignore: cast_nullable_to_non_nullable
              as DataBlueprint?,
    ) as $Val);
  }

  /// Create a copy of HeaderContext
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DataBlueprintCopyWith<$Res>? get parentBlueprint {
    if (_value.parentBlueprint == null) {
      return null;
    }

    return $DataBlueprintCopyWith<$Res>(_value.parentBlueprint!, (value) {
      return _then(_value.copyWith(parentBlueprint: value) as $Val);
    });
  }

  /// Create a copy of HeaderContext
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DataBlueprintCopyWith<$Res>? get genericBlueprint {
    if (_value.genericBlueprint == null) {
      return null;
    }

    return $DataBlueprintCopyWith<$Res>(_value.genericBlueprint!, (value) {
      return _then(_value.copyWith(genericBlueprint: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$HeaderContextImplCopyWith<$Res>
    implements $HeaderContextCopyWith<$Res> {
  factory _$$HeaderContextImplCopyWith(
          _$HeaderContextImpl value, $Res Function(_$HeaderContextImpl) then) =
      __$$HeaderContextImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({DataBlueprint? parentBlueprint, DataBlueprint? genericBlueprint});

  @override
  $DataBlueprintCopyWith<$Res>? get parentBlueprint;
  @override
  $DataBlueprintCopyWith<$Res>? get genericBlueprint;
}

/// @nodoc
class __$$HeaderContextImplCopyWithImpl<$Res>
    extends _$HeaderContextCopyWithImpl<$Res, _$HeaderContextImpl>
    implements _$$HeaderContextImplCopyWith<$Res> {
  __$$HeaderContextImplCopyWithImpl(
      _$HeaderContextImpl _value, $Res Function(_$HeaderContextImpl) _then)
      : super(_value, _then);

  /// Create a copy of HeaderContext
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? parentBlueprint = freezed,
    Object? genericBlueprint = freezed,
  }) {
    return _then(_$HeaderContextImpl(
      parentBlueprint: freezed == parentBlueprint
          ? _value.parentBlueprint
          : parentBlueprint // ignore: cast_nullable_to_non_nullable
              as DataBlueprint?,
      genericBlueprint: freezed == genericBlueprint
          ? _value.genericBlueprint
          : genericBlueprint // ignore: cast_nullable_to_non_nullable
              as DataBlueprint?,
    ));
  }
}

/// @nodoc

class _$HeaderContextImpl implements _HeaderContext {
  const _$HeaderContextImpl({this.parentBlueprint, this.genericBlueprint});

  @override
  final DataBlueprint? parentBlueprint;
  @override
  final DataBlueprint? genericBlueprint;

  @override
  String toString() {
    return 'HeaderContext(parentBlueprint: $parentBlueprint, genericBlueprint: $genericBlueprint)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$HeaderContextImpl &&
            (identical(other.parentBlueprint, parentBlueprint) ||
                other.parentBlueprint == parentBlueprint) &&
            (identical(other.genericBlueprint, genericBlueprint) ||
                other.genericBlueprint == genericBlueprint));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, parentBlueprint, genericBlueprint);

  /// Create a copy of HeaderContext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$HeaderContextImplCopyWith<_$HeaderContextImpl> get copyWith =>
      __$$HeaderContextImplCopyWithImpl<_$HeaderContextImpl>(this, _$identity);
}

abstract class _HeaderContext implements HeaderContext {
  const factory _HeaderContext(
      {final DataBlueprint? parentBlueprint,
      final DataBlueprint? genericBlueprint}) = _$HeaderContextImpl;

  @override
  DataBlueprint? get parentBlueprint;
  @override
  DataBlueprint? get genericBlueprint;

  /// Create a copy of HeaderContext
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$HeaderContextImplCopyWith<_$HeaderContextImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
