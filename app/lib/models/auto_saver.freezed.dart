// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'auto_saver.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$AutoSaverState {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() saving,
    required TResult Function(DateTime time) saved,
    required TResult Function(String error) error,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? saving,
    TResult? Function(DateTime time)? saved,
    TResult? Function(String error)? error,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? saving,
    TResult Function(DateTime time)? saved,
    TResult Function(String error)? error,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(SavingState value) saving,
    required TResult Function(SavedInfo value) saved,
    required TResult Function(ErrorState value) error,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(SavingState value)? saving,
    TResult? Function(SavedInfo value)? saved,
    TResult? Function(ErrorState value)? error,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(SavingState value)? saving,
    TResult Function(SavedInfo value)? saved,
    TResult Function(ErrorState value)? error,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AutoSaverStateCopyWith<$Res> {
  factory $AutoSaverStateCopyWith(
          AutoSaverState value, $Res Function(AutoSaverState) then) =
      _$AutoSaverStateCopyWithImpl<$Res, AutoSaverState>;
}

/// @nodoc
class _$AutoSaverStateCopyWithImpl<$Res, $Val extends AutoSaverState>
    implements $AutoSaverStateCopyWith<$Res> {
  _$AutoSaverStateCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$SavingStateCopyWith<$Res> {
  factory _$$SavingStateCopyWith(
          _$SavingState value, $Res Function(_$SavingState) then) =
      __$$SavingStateCopyWithImpl<$Res>;
}

/// @nodoc
class __$$SavingStateCopyWithImpl<$Res>
    extends _$AutoSaverStateCopyWithImpl<$Res, _$SavingState>
    implements _$$SavingStateCopyWith<$Res> {
  __$$SavingStateCopyWithImpl(
      _$SavingState _value, $Res Function(_$SavingState) _then)
      : super(_value, _then);
}

/// @nodoc

class _$SavingState implements SavingState {
  const _$SavingState();

  @override
  String toString() {
    return 'AutoSaverState.saving()';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$SavingState);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() saving,
    required TResult Function(DateTime time) saved,
    required TResult Function(String error) error,
  }) {
    return saving();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? saving,
    TResult? Function(DateTime time)? saved,
    TResult? Function(String error)? error,
  }) {
    return saving?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? saving,
    TResult Function(DateTime time)? saved,
    TResult Function(String error)? error,
    required TResult orElse(),
  }) {
    if (saving != null) {
      return saving();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(SavingState value) saving,
    required TResult Function(SavedInfo value) saved,
    required TResult Function(ErrorState value) error,
  }) {
    return saving(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(SavingState value)? saving,
    TResult? Function(SavedInfo value)? saved,
    TResult? Function(ErrorState value)? error,
  }) {
    return saving?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(SavingState value)? saving,
    TResult Function(SavedInfo value)? saved,
    TResult Function(ErrorState value)? error,
    required TResult orElse(),
  }) {
    if (saving != null) {
      return saving(this);
    }
    return orElse();
  }
}

abstract class SavingState implements AutoSaverState {
  const factory SavingState() = _$SavingState;
}

/// @nodoc
abstract class _$$SavedInfoCopyWith<$Res> {
  factory _$$SavedInfoCopyWith(
          _$SavedInfo value, $Res Function(_$SavedInfo) then) =
      __$$SavedInfoCopyWithImpl<$Res>;
  @useResult
  $Res call({DateTime time});
}

/// @nodoc
class __$$SavedInfoCopyWithImpl<$Res>
    extends _$AutoSaverStateCopyWithImpl<$Res, _$SavedInfo>
    implements _$$SavedInfoCopyWith<$Res> {
  __$$SavedInfoCopyWithImpl(
      _$SavedInfo _value, $Res Function(_$SavedInfo) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? time = null,
  }) {
    return _then(_$SavedInfo(
      time: null == time
          ? _value.time
          : time // ignore: cast_nullable_to_non_nullable
              as DateTime,
    ));
  }
}

/// @nodoc

class _$SavedInfo implements SavedInfo {
  const _$SavedInfo({required this.time});

  @override
  final DateTime time;

  @override
  String toString() {
    return 'AutoSaverState.saved(time: $time)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$SavedInfo &&
            (identical(other.time, time) || other.time == time));
  }

  @override
  int get hashCode => Object.hash(runtimeType, time);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$SavedInfoCopyWith<_$SavedInfo> get copyWith =>
      __$$SavedInfoCopyWithImpl<_$SavedInfo>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() saving,
    required TResult Function(DateTime time) saved,
    required TResult Function(String error) error,
  }) {
    return saved(time);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? saving,
    TResult? Function(DateTime time)? saved,
    TResult? Function(String error)? error,
  }) {
    return saved?.call(time);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? saving,
    TResult Function(DateTime time)? saved,
    TResult Function(String error)? error,
    required TResult orElse(),
  }) {
    if (saved != null) {
      return saved(time);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(SavingState value) saving,
    required TResult Function(SavedInfo value) saved,
    required TResult Function(ErrorState value) error,
  }) {
    return saved(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(SavingState value)? saving,
    TResult? Function(SavedInfo value)? saved,
    TResult? Function(ErrorState value)? error,
  }) {
    return saved?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(SavingState value)? saving,
    TResult Function(SavedInfo value)? saved,
    TResult Function(ErrorState value)? error,
    required TResult orElse(),
  }) {
    if (saved != null) {
      return saved(this);
    }
    return orElse();
  }
}

abstract class SavedInfo implements AutoSaverState {
  const factory SavedInfo({required final DateTime time}) = _$SavedInfo;

  DateTime get time;
  @JsonKey(ignore: true)
  _$$SavedInfoCopyWith<_$SavedInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ErrorStateCopyWith<$Res> {
  factory _$$ErrorStateCopyWith(
          _$ErrorState value, $Res Function(_$ErrorState) then) =
      __$$ErrorStateCopyWithImpl<$Res>;
  @useResult
  $Res call({String error});
}

/// @nodoc
class __$$ErrorStateCopyWithImpl<$Res>
    extends _$AutoSaverStateCopyWithImpl<$Res, _$ErrorState>
    implements _$$ErrorStateCopyWith<$Res> {
  __$$ErrorStateCopyWithImpl(
      _$ErrorState _value, $Res Function(_$ErrorState) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? error = null,
  }) {
    return _then(_$ErrorState(
      null == error
          ? _value.error
          : error // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$ErrorState implements ErrorState {
  const _$ErrorState(this.error);

  @override
  final String error;

  @override
  String toString() {
    return 'AutoSaverState.error(error: $error)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ErrorState &&
            (identical(other.error, error) || other.error == error));
  }

  @override
  int get hashCode => Object.hash(runtimeType, error);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ErrorStateCopyWith<_$ErrorState> get copyWith =>
      __$$ErrorStateCopyWithImpl<_$ErrorState>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() saving,
    required TResult Function(DateTime time) saved,
    required TResult Function(String error) error,
  }) {
    return error(this.error);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? saving,
    TResult? Function(DateTime time)? saved,
    TResult? Function(String error)? error,
  }) {
    return error?.call(this.error);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? saving,
    TResult Function(DateTime time)? saved,
    TResult Function(String error)? error,
    required TResult orElse(),
  }) {
    if (error != null) {
      return error(this.error);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(SavingState value) saving,
    required TResult Function(SavedInfo value) saved,
    required TResult Function(ErrorState value) error,
  }) {
    return error(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(SavingState value)? saving,
    TResult? Function(SavedInfo value)? saved,
    TResult? Function(ErrorState value)? error,
  }) {
    return error?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(SavingState value)? saving,
    TResult Function(SavedInfo value)? saved,
    TResult Function(ErrorState value)? error,
    required TResult orElse(),
  }) {
    if (error != null) {
      return error(this);
    }
    return orElse();
  }
}

abstract class ErrorState implements AutoSaverState {
  const factory ErrorState(final String error) = _$ErrorState;

  String get error;
  @JsonKey(ignore: true)
  _$$ErrorStateCopyWith<_$ErrorState> get copyWith =>
      throw _privateConstructorUsedError;
}
