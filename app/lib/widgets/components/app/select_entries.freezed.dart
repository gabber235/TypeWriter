// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'select_entries.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$EntriesSelection {
  String get tag => throw _privateConstructorUsedError;
  List<String> get selectedEntries => throw _privateConstructorUsedError;
  List<String> get excludedEntries => throw _privateConstructorUsedError;
  dynamic Function(Ref<dynamic>, List<String>)? get onSelectionChanged =>
      throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  $EntriesSelectionCopyWith<EntriesSelection> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $EntriesSelectionCopyWith<$Res> {
  factory $EntriesSelectionCopyWith(
          EntriesSelection value, $Res Function(EntriesSelection) then) =
      _$EntriesSelectionCopyWithImpl<$Res, EntriesSelection>;
  @useResult
  $Res call(
      {String tag,
      List<String> selectedEntries,
      List<String> excludedEntries,
      dynamic Function(Ref<dynamic>, List<String>)? onSelectionChanged});
}

/// @nodoc
class _$EntriesSelectionCopyWithImpl<$Res, $Val extends EntriesSelection>
    implements $EntriesSelectionCopyWith<$Res> {
  _$EntriesSelectionCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? tag = null,
    Object? selectedEntries = null,
    Object? excludedEntries = null,
    Object? onSelectionChanged = freezed,
  }) {
    return _then(_value.copyWith(
      tag: null == tag
          ? _value.tag
          : tag // ignore: cast_nullable_to_non_nullable
              as String,
      selectedEntries: null == selectedEntries
          ? _value.selectedEntries
          : selectedEntries // ignore: cast_nullable_to_non_nullable
              as List<String>,
      excludedEntries: null == excludedEntries
          ? _value.excludedEntries
          : excludedEntries // ignore: cast_nullable_to_non_nullable
              as List<String>,
      onSelectionChanged: freezed == onSelectionChanged
          ? _value.onSelectionChanged
          : onSelectionChanged // ignore: cast_nullable_to_non_nullable
              as dynamic Function(Ref<dynamic>, List<String>)?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$EntriesSelectionImplCopyWith<$Res>
    implements $EntriesSelectionCopyWith<$Res> {
  factory _$$EntriesSelectionImplCopyWith(_$EntriesSelectionImpl value,
          $Res Function(_$EntriesSelectionImpl) then) =
      __$$EntriesSelectionImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String tag,
      List<String> selectedEntries,
      List<String> excludedEntries,
      dynamic Function(Ref<dynamic>, List<String>)? onSelectionChanged});
}

/// @nodoc
class __$$EntriesSelectionImplCopyWithImpl<$Res>
    extends _$EntriesSelectionCopyWithImpl<$Res, _$EntriesSelectionImpl>
    implements _$$EntriesSelectionImplCopyWith<$Res> {
  __$$EntriesSelectionImplCopyWithImpl(_$EntriesSelectionImpl _value,
      $Res Function(_$EntriesSelectionImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? tag = null,
    Object? selectedEntries = null,
    Object? excludedEntries = null,
    Object? onSelectionChanged = freezed,
  }) {
    return _then(_$EntriesSelectionImpl(
      tag: null == tag
          ? _value.tag
          : tag // ignore: cast_nullable_to_non_nullable
              as String,
      selectedEntries: null == selectedEntries
          ? _value._selectedEntries
          : selectedEntries // ignore: cast_nullable_to_non_nullable
              as List<String>,
      excludedEntries: null == excludedEntries
          ? _value._excludedEntries
          : excludedEntries // ignore: cast_nullable_to_non_nullable
              as List<String>,
      onSelectionChanged: freezed == onSelectionChanged
          ? _value.onSelectionChanged
          : onSelectionChanged // ignore: cast_nullable_to_non_nullable
              as dynamic Function(Ref<dynamic>, List<String>)?,
    ));
  }
}

/// @nodoc

class _$EntriesSelectionImpl implements _EntriesSelection {
  const _$EntriesSelectionImpl(
      {required this.tag,
      required final List<String> selectedEntries,
      final List<String> excludedEntries = const [],
      this.onSelectionChanged})
      : _selectedEntries = selectedEntries,
        _excludedEntries = excludedEntries;

  @override
  final String tag;
  final List<String> _selectedEntries;
  @override
  List<String> get selectedEntries {
    if (_selectedEntries is EqualUnmodifiableListView) return _selectedEntries;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_selectedEntries);
  }

  final List<String> _excludedEntries;
  @override
  @JsonKey()
  List<String> get excludedEntries {
    if (_excludedEntries is EqualUnmodifiableListView) return _excludedEntries;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_excludedEntries);
  }

  @override
  final dynamic Function(Ref<dynamic>, List<String>)? onSelectionChanged;

  @override
  String toString() {
    return 'EntriesSelection(tag: $tag, selectedEntries: $selectedEntries, excludedEntries: $excludedEntries, onSelectionChanged: $onSelectionChanged)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EntriesSelectionImpl &&
            (identical(other.tag, tag) || other.tag == tag) &&
            const DeepCollectionEquality()
                .equals(other._selectedEntries, _selectedEntries) &&
            const DeepCollectionEquality()
                .equals(other._excludedEntries, _excludedEntries) &&
            (identical(other.onSelectionChanged, onSelectionChanged) ||
                other.onSelectionChanged == onSelectionChanged));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      tag,
      const DeepCollectionEquality().hash(_selectedEntries),
      const DeepCollectionEquality().hash(_excludedEntries),
      onSelectionChanged);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$EntriesSelectionImplCopyWith<_$EntriesSelectionImpl> get copyWith =>
      __$$EntriesSelectionImplCopyWithImpl<_$EntriesSelectionImpl>(
          this, _$identity);
}

abstract class _EntriesSelection implements EntriesSelection {
  const factory _EntriesSelection(
      {required final String tag,
      required final List<String> selectedEntries,
      final List<String> excludedEntries,
      final dynamic Function(Ref<dynamic>, List<String>)?
          onSelectionChanged}) = _$EntriesSelectionImpl;

  @override
  String get tag;
  @override
  List<String> get selectedEntries;
  @override
  List<String> get excludedEntries;
  @override
  dynamic Function(Ref<dynamic>, List<String>)? get onSelectionChanged;
  @override
  @JsonKey(ignore: true)
  _$$EntriesSelectionImplCopyWith<_$EntriesSelectionImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
