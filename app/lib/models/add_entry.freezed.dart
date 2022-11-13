// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target

part of 'add_entry.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$AddEntry {
  String get title => throw _privateConstructorUsedError;
  String get description => throw _privateConstructorUsedError;
  Color get color => throw _privateConstructorUsedError;
  IconData get icon => throw _privateConstructorUsedError;
  void Function(WidgetRef) get onAdd => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  $AddEntryCopyWith<AddEntry> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AddEntryCopyWith<$Res> {
  factory $AddEntryCopyWith(AddEntry value, $Res Function(AddEntry) then) =
      _$AddEntryCopyWithImpl<$Res, AddEntry>;
  @useResult
  $Res call(
      {String title,
      String description,
      Color color,
      IconData icon,
      void Function(WidgetRef) onAdd});
}

/// @nodoc
class _$AddEntryCopyWithImpl<$Res, $Val extends AddEntry>
    implements $AddEntryCopyWith<$Res> {
  _$AddEntryCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? title = null,
    Object? description = null,
    Object? color = null,
    Object? icon = null,
    Object? onAdd = null,
  }) {
    return _then(_value.copyWith(
      title: null == title
          ? _value.title
          : title // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as IconData,
      onAdd: null == onAdd
          ? _value.onAdd
          : onAdd // ignore: cast_nullable_to_non_nullable
              as void Function(WidgetRef),
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_AddEntryCopyWith<$Res> implements $AddEntryCopyWith<$Res> {
  factory _$$_AddEntryCopyWith(
          _$_AddEntry value, $Res Function(_$_AddEntry) then) =
      __$$_AddEntryCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String title,
      String description,
      Color color,
      IconData icon,
      void Function(WidgetRef) onAdd});
}

/// @nodoc
class __$$_AddEntryCopyWithImpl<$Res>
    extends _$AddEntryCopyWithImpl<$Res, _$_AddEntry>
    implements _$$_AddEntryCopyWith<$Res> {
  __$$_AddEntryCopyWithImpl(
      _$_AddEntry _value, $Res Function(_$_AddEntry) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? title = null,
    Object? description = null,
    Object? color = null,
    Object? icon = null,
    Object? onAdd = null,
  }) {
    return _then(_$_AddEntry(
      title: null == title
          ? _value.title
          : title // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as IconData,
      onAdd: null == onAdd
          ? _value.onAdd
          : onAdd // ignore: cast_nullable_to_non_nullable
              as void Function(WidgetRef),
    ));
  }
}

/// @nodoc

class _$_AddEntry implements _AddEntry {
  const _$_AddEntry(
      {required this.title,
      required this.description,
      required this.color,
      required this.icon,
      required this.onAdd});

  @override
  final String title;
  @override
  final String description;
  @override
  final Color color;
  @override
  final IconData icon;
  @override
  final void Function(WidgetRef) onAdd;

  @override
  String toString() {
    return 'AddEntry(title: $title, description: $description, color: $color, icon: $icon, onAdd: $onAdd)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_AddEntry &&
            (identical(other.title, title) || other.title == title) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.color, color) || other.color == color) &&
            (identical(other.icon, icon) || other.icon == icon) &&
            (identical(other.onAdd, onAdd) || other.onAdd == onAdd));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, title, description, color, icon, onAdd);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_AddEntryCopyWith<_$_AddEntry> get copyWith =>
      __$$_AddEntryCopyWithImpl<_$_AddEntry>(this, _$identity);
}

abstract class _AddEntry implements AddEntry {
  const factory _AddEntry(
      {required final String title,
      required final String description,
      required final Color color,
      required final IconData icon,
      required final void Function(WidgetRef) onAdd}) = _$_AddEntry;

  @override
  String get title;
  @override
  String get description;
  @override
  Color get color;
  @override
  IconData get icon;
  @override
  void Function(WidgetRef) get onAdd;
  @override
  @JsonKey(ignore: true)
  _$$_AddEntryCopyWith<_$_AddEntry> get copyWith =>
      throw _privateConstructorUsedError;
}
