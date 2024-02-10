// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'toasts.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$Toast {
  String get id => throw _privateConstructorUsedError;
  String get message => throw _privateConstructorUsedError;
  String? get description => throw _privateConstructorUsedError;
  Color get color => throw _privateConstructorUsedError;
  String get icon => throw _privateConstructorUsedError;
  DateTime? get shownAt => throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)
        $default, {
    required TResult Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)
        temporary,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)?
        $default, {
    TResult? Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)?
        temporary,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)?
        $default, {
    TResult Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)?
        temporary,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_Toast value) $default, {
    required TResult Function(TemporaryToast value) temporary,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_Toast value)? $default, {
    TResult? Function(TemporaryToast value)? temporary,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_Toast value)? $default, {
    TResult Function(TemporaryToast value)? temporary,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  $ToastCopyWith<Toast> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ToastCopyWith<$Res> {
  factory $ToastCopyWith(Toast value, $Res Function(Toast) then) =
      _$ToastCopyWithImpl<$Res, Toast>;
  @useResult
  $Res call(
      {String id,
      String message,
      String? description,
      Color color,
      String icon,
      DateTime? shownAt});
}

/// @nodoc
class _$ToastCopyWithImpl<$Res, $Val extends Toast>
    implements $ToastCopyWith<$Res> {
  _$ToastCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? message = null,
    Object? description = freezed,
    Object? color = null,
    Object? icon = null,
    Object? shownAt = freezed,
  }) {
    return _then(_value.copyWith(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      message: null == message
          ? _value.message
          : message // ignore: cast_nullable_to_non_nullable
              as String,
      description: freezed == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String?,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as String,
      shownAt: freezed == shownAt
          ? _value.shownAt
          : shownAt // ignore: cast_nullable_to_non_nullable
              as DateTime?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$ToastImplCopyWith<$Res> implements $ToastCopyWith<$Res> {
  factory _$$ToastImplCopyWith(
          _$ToastImpl value, $Res Function(_$ToastImpl) then) =
      __$$ToastImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String id,
      String message,
      String? description,
      Color color,
      String icon,
      DateTime? shownAt});
}

/// @nodoc
class __$$ToastImplCopyWithImpl<$Res>
    extends _$ToastCopyWithImpl<$Res, _$ToastImpl>
    implements _$$ToastImplCopyWith<$Res> {
  __$$ToastImplCopyWithImpl(
      _$ToastImpl _value, $Res Function(_$ToastImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? message = null,
    Object? description = freezed,
    Object? color = null,
    Object? icon = null,
    Object? shownAt = freezed,
  }) {
    return _then(_$ToastImpl(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      message: null == message
          ? _value.message
          : message // ignore: cast_nullable_to_non_nullable
              as String,
      description: freezed == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String?,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as String,
      shownAt: freezed == shownAt
          ? _value.shownAt
          : shownAt // ignore: cast_nullable_to_non_nullable
              as DateTime?,
    ));
  }
}

/// @nodoc

class _$ToastImpl implements _Toast {
  const _$ToastImpl(
      {required this.id,
      required this.message,
      this.description,
      this.color = Colors.blue,
      this.icon = TWIcons.exclamation,
      this.shownAt});

  @override
  final String id;
  @override
  final String message;
  @override
  final String? description;
  @override
  @JsonKey()
  final Color color;
  @override
  @JsonKey()
  final String icon;
  @override
  final DateTime? shownAt;

  @override
  String toString() {
    return 'Toast(id: $id, message: $message, description: $description, color: $color, icon: $icon, shownAt: $shownAt)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ToastImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.message, message) || other.message == message) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.color, color) || other.color == color) &&
            (identical(other.icon, icon) || other.icon == icon) &&
            (identical(other.shownAt, shownAt) || other.shownAt == shownAt));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, id, message, description, color, icon, shownAt);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ToastImplCopyWith<_$ToastImpl> get copyWith =>
      __$$ToastImplCopyWithImpl<_$ToastImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)
        $default, {
    required TResult Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)
        temporary,
  }) {
    return $default(id, message, description, color, icon, shownAt);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)?
        $default, {
    TResult? Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)?
        temporary,
  }) {
    return $default?.call(id, message, description, color, icon, shownAt);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)?
        $default, {
    TResult Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)?
        temporary,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(id, message, description, color, icon, shownAt);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_Toast value) $default, {
    required TResult Function(TemporaryToast value) temporary,
  }) {
    return $default(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_Toast value)? $default, {
    TResult? Function(TemporaryToast value)? temporary,
  }) {
    return $default?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_Toast value)? $default, {
    TResult Function(TemporaryToast value)? temporary,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(this);
    }
    return orElse();
  }
}

abstract class _Toast implements Toast {
  const factory _Toast(
      {required final String id,
      required final String message,
      final String? description,
      final Color color,
      final String icon,
      final DateTime? shownAt}) = _$ToastImpl;

  @override
  String get id;
  @override
  String get message;
  @override
  String? get description;
  @override
  Color get color;
  @override
  String get icon;
  @override
  DateTime? get shownAt;
  @override
  @JsonKey(ignore: true)
  _$$ToastImplCopyWith<_$ToastImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$TemporaryToastImplCopyWith<$Res>
    implements $ToastCopyWith<$Res> {
  factory _$$TemporaryToastImplCopyWith(_$TemporaryToastImpl value,
          $Res Function(_$TemporaryToastImpl) then) =
      __$$TemporaryToastImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String id,
      String message,
      String? description,
      Color color,
      String icon,
      Duration duration,
      DateTime? shownAt});
}

/// @nodoc
class __$$TemporaryToastImplCopyWithImpl<$Res>
    extends _$ToastCopyWithImpl<$Res, _$TemporaryToastImpl>
    implements _$$TemporaryToastImplCopyWith<$Res> {
  __$$TemporaryToastImplCopyWithImpl(
      _$TemporaryToastImpl _value, $Res Function(_$TemporaryToastImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? message = null,
    Object? description = freezed,
    Object? color = null,
    Object? icon = null,
    Object? duration = null,
    Object? shownAt = freezed,
  }) {
    return _then(_$TemporaryToastImpl(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      message: null == message
          ? _value.message
          : message // ignore: cast_nullable_to_non_nullable
              as String,
      description: freezed == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String?,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as String,
      duration: null == duration
          ? _value.duration
          : duration // ignore: cast_nullable_to_non_nullable
              as Duration,
      shownAt: freezed == shownAt
          ? _value.shownAt
          : shownAt // ignore: cast_nullable_to_non_nullable
              as DateTime?,
    ));
  }
}

/// @nodoc

class _$TemporaryToastImpl implements TemporaryToast {
  const _$TemporaryToastImpl(
      {required this.id,
      required this.message,
      this.description,
      this.color = Colors.blue,
      this.icon = TWIcons.exclamation,
      this.duration = const Duration(seconds: 10),
      this.shownAt});

  @override
  final String id;
  @override
  final String message;
  @override
  final String? description;
  @override
  @JsonKey()
  final Color color;
  @override
  @JsonKey()
  final String icon;
  @override
  @JsonKey()
  final Duration duration;
  @override
  final DateTime? shownAt;

  @override
  String toString() {
    return 'Toast.temporary(id: $id, message: $message, description: $description, color: $color, icon: $icon, duration: $duration, shownAt: $shownAt)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$TemporaryToastImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.message, message) || other.message == message) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.color, color) || other.color == color) &&
            (identical(other.icon, icon) || other.icon == icon) &&
            (identical(other.duration, duration) ||
                other.duration == duration) &&
            (identical(other.shownAt, shownAt) || other.shownAt == shownAt));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType, id, message, description, color, icon, duration, shownAt);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$TemporaryToastImplCopyWith<_$TemporaryToastImpl> get copyWith =>
      __$$TemporaryToastImplCopyWithImpl<_$TemporaryToastImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)
        $default, {
    required TResult Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)
        temporary,
  }) {
    return temporary(id, message, description, color, icon, duration, shownAt);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)?
        $default, {
    TResult? Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)?
        temporary,
  }) {
    return temporary?.call(
        id, message, description, color, icon, duration, shownAt);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(String id, String message, String? description,
            Color color, String icon, DateTime? shownAt)?
        $default, {
    TResult Function(String id, String message, String? description,
            Color color, String icon, Duration duration, DateTime? shownAt)?
        temporary,
    required TResult orElse(),
  }) {
    if (temporary != null) {
      return temporary(
          id, message, description, color, icon, duration, shownAt);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_Toast value) $default, {
    required TResult Function(TemporaryToast value) temporary,
  }) {
    return temporary(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_Toast value)? $default, {
    TResult? Function(TemporaryToast value)? temporary,
  }) {
    return temporary?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_Toast value)? $default, {
    TResult Function(TemporaryToast value)? temporary,
    required TResult orElse(),
  }) {
    if (temporary != null) {
      return temporary(this);
    }
    return orElse();
  }
}

abstract class TemporaryToast implements Toast {
  const factory TemporaryToast(
      {required final String id,
      required final String message,
      final String? description,
      final Color color,
      final String icon,
      final Duration duration,
      final DateTime? shownAt}) = _$TemporaryToastImpl;

  @override
  String get id;
  @override
  String get message;
  @override
  String? get description;
  @override
  Color get color;
  @override
  String get icon;
  Duration get duration;
  @override
  DateTime? get shownAt;
  @override
  @JsonKey(ignore: true)
  _$$TemporaryToastImplCopyWith<_$TemporaryToastImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
