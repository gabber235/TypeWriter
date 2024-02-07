// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'sound.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

SoundId _$SoundIdFromJson(Map<String, dynamic> json) {
  switch (json['type']) {
    case 'default':
      return DefaultSoundId.fromJson(json);
    case 'entry':
      return EntrySoundId.fromJson(json);

    default:
      throw CheckedFromJsonException(
          json, 'type', 'SoundId', 'Invalid union type "${json['type']}"!');
  }
}

/// @nodoc
mixin _$SoundId {
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "value") String id) $default, {
    required TResult Function(@JsonKey(name: "value") String entryId) entry,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "value") String id)? $default, {
    TResult? Function(@JsonKey(name: "value") String entryId)? entry,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "value") String id)? $default, {
    TResult Function(@JsonKey(name: "value") String entryId)? entry,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(DefaultSoundId value) $default, {
    required TResult Function(EntrySoundId value) entry,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(DefaultSoundId value)? $default, {
    TResult? Function(EntrySoundId value)? entry,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(DefaultSoundId value)? $default, {
    TResult Function(EntrySoundId value)? entry,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $SoundIdCopyWith<$Res> {
  factory $SoundIdCopyWith(SoundId value, $Res Function(SoundId) then) =
      _$SoundIdCopyWithImpl<$Res, SoundId>;
}

/// @nodoc
class _$SoundIdCopyWithImpl<$Res, $Val extends SoundId>
    implements $SoundIdCopyWith<$Res> {
  _$SoundIdCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$DefaultSoundIdImplCopyWith<$Res> {
  factory _$$DefaultSoundIdImplCopyWith(_$DefaultSoundIdImpl value,
          $Res Function(_$DefaultSoundIdImpl) then) =
      __$$DefaultSoundIdImplCopyWithImpl<$Res>;
  @useResult
  $Res call({@JsonKey(name: "value") String id});
}

/// @nodoc
class __$$DefaultSoundIdImplCopyWithImpl<$Res>
    extends _$SoundIdCopyWithImpl<$Res, _$DefaultSoundIdImpl>
    implements _$$DefaultSoundIdImplCopyWith<$Res> {
  __$$DefaultSoundIdImplCopyWithImpl(
      _$DefaultSoundIdImpl _value, $Res Function(_$DefaultSoundIdImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
  }) {
    return _then(_$DefaultSoundIdImpl(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$DefaultSoundIdImpl implements DefaultSoundId {
  const _$DefaultSoundIdImpl(
      {@JsonKey(name: "value") required this.id, final String? $type})
      : $type = $type ?? 'default';

  factory _$DefaultSoundIdImpl.fromJson(Map<String, dynamic> json) =>
      _$$DefaultSoundIdImplFromJson(json);

  @override
  @JsonKey(name: "value")
  final String id;

  @JsonKey(name: 'type')
  final String $type;

  @override
  String toString() {
    return 'SoundId(id: $id)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$DefaultSoundIdImpl &&
            (identical(other.id, id) || other.id == id));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, id);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$DefaultSoundIdImplCopyWith<_$DefaultSoundIdImpl> get copyWith =>
      __$$DefaultSoundIdImplCopyWithImpl<_$DefaultSoundIdImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "value") String id) $default, {
    required TResult Function(@JsonKey(name: "value") String entryId) entry,
  }) {
    return $default(id);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "value") String id)? $default, {
    TResult? Function(@JsonKey(name: "value") String entryId)? entry,
  }) {
    return $default?.call(id);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "value") String id)? $default, {
    TResult Function(@JsonKey(name: "value") String entryId)? entry,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(id);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(DefaultSoundId value) $default, {
    required TResult Function(EntrySoundId value) entry,
  }) {
    return $default(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(DefaultSoundId value)? $default, {
    TResult? Function(EntrySoundId value)? entry,
  }) {
    return $default?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(DefaultSoundId value)? $default, {
    TResult Function(EntrySoundId value)? entry,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$DefaultSoundIdImplToJson(
      this,
    );
  }
}

abstract class DefaultSoundId implements SoundId {
  const factory DefaultSoundId(
          {@JsonKey(name: "value") required final String id}) =
      _$DefaultSoundIdImpl;

  factory DefaultSoundId.fromJson(Map<String, dynamic> json) =
      _$DefaultSoundIdImpl.fromJson;

  @JsonKey(name: "value")
  String get id;
  @JsonKey(ignore: true)
  _$$DefaultSoundIdImplCopyWith<_$DefaultSoundIdImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$EntrySoundIdImplCopyWith<$Res> {
  factory _$$EntrySoundIdImplCopyWith(
          _$EntrySoundIdImpl value, $Res Function(_$EntrySoundIdImpl) then) =
      __$$EntrySoundIdImplCopyWithImpl<$Res>;
  @useResult
  $Res call({@JsonKey(name: "value") String entryId});
}

/// @nodoc
class __$$EntrySoundIdImplCopyWithImpl<$Res>
    extends _$SoundIdCopyWithImpl<$Res, _$EntrySoundIdImpl>
    implements _$$EntrySoundIdImplCopyWith<$Res> {
  __$$EntrySoundIdImplCopyWithImpl(
      _$EntrySoundIdImpl _value, $Res Function(_$EntrySoundIdImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? entryId = null,
  }) {
    return _then(_$EntrySoundIdImpl(
      entryId: null == entryId
          ? _value.entryId
          : entryId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$EntrySoundIdImpl implements EntrySoundId {
  const _$EntrySoundIdImpl(
      {@JsonKey(name: "value") required this.entryId, final String? $type})
      : $type = $type ?? 'entry';

  factory _$EntrySoundIdImpl.fromJson(Map<String, dynamic> json) =>
      _$$EntrySoundIdImplFromJson(json);

  @override
  @JsonKey(name: "value")
  final String entryId;

  @JsonKey(name: 'type')
  final String $type;

  @override
  String toString() {
    return 'SoundId.entry(entryId: $entryId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EntrySoundIdImpl &&
            (identical(other.entryId, entryId) || other.entryId == entryId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, entryId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$EntrySoundIdImplCopyWith<_$EntrySoundIdImpl> get copyWith =>
      __$$EntrySoundIdImplCopyWithImpl<_$EntrySoundIdImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "value") String id) $default, {
    required TResult Function(@JsonKey(name: "value") String entryId) entry,
  }) {
    return entry(entryId);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "value") String id)? $default, {
    TResult? Function(@JsonKey(name: "value") String entryId)? entry,
  }) {
    return entry?.call(entryId);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "value") String id)? $default, {
    TResult Function(@JsonKey(name: "value") String entryId)? entry,
    required TResult orElse(),
  }) {
    if (entry != null) {
      return entry(entryId);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(DefaultSoundId value) $default, {
    required TResult Function(EntrySoundId value) entry,
  }) {
    return entry(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(DefaultSoundId value)? $default, {
    TResult? Function(EntrySoundId value)? entry,
  }) {
    return entry?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(DefaultSoundId value)? $default, {
    TResult Function(EntrySoundId value)? entry,
    required TResult orElse(),
  }) {
    if (entry != null) {
      return entry(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$EntrySoundIdImplToJson(
      this,
    );
  }
}

abstract class EntrySoundId implements SoundId {
  const factory EntrySoundId(
          {@JsonKey(name: "value") required final String entryId}) =
      _$EntrySoundIdImpl;

  factory EntrySoundId.fromJson(Map<String, dynamic> json) =
      _$EntrySoundIdImpl.fromJson;

  @JsonKey(name: "value")
  String get entryId;
  @JsonKey(ignore: true)
  _$$EntrySoundIdImplCopyWith<_$EntrySoundIdImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
