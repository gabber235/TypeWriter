// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'search_bar.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$Search {
  String get query => throw _privateConstructorUsedError;
  List<SearchFetcher> get fetchers => throw _privateConstructorUsedError;
  List<SearchFilter> get filters => throw _privateConstructorUsedError;
  dynamic Function()? get onEnd => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  $SearchCopyWith<Search> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $SearchCopyWith<$Res> {
  factory $SearchCopyWith(Search value, $Res Function(Search) then) =
      _$SearchCopyWithImpl<$Res, Search>;
  @useResult
  $Res call(
      {String query,
      List<SearchFetcher> fetchers,
      List<SearchFilter> filters,
      dynamic Function()? onEnd});
}

/// @nodoc
class _$SearchCopyWithImpl<$Res, $Val extends Search>
    implements $SearchCopyWith<$Res> {
  _$SearchCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? query = null,
    Object? fetchers = null,
    Object? filters = null,
    Object? onEnd = freezed,
  }) {
    return _then(_value.copyWith(
      query: null == query
          ? _value.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
      fetchers: null == fetchers
          ? _value.fetchers
          : fetchers // ignore: cast_nullable_to_non_nullable
              as List<SearchFetcher>,
      filters: null == filters
          ? _value.filters
          : filters // ignore: cast_nullable_to_non_nullable
              as List<SearchFilter>,
      onEnd: freezed == onEnd
          ? _value.onEnd
          : onEnd // ignore: cast_nullable_to_non_nullable
              as dynamic Function()?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$SearchImplCopyWith<$Res> implements $SearchCopyWith<$Res> {
  factory _$$SearchImplCopyWith(
          _$SearchImpl value, $Res Function(_$SearchImpl) then) =
      __$$SearchImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String query,
      List<SearchFetcher> fetchers,
      List<SearchFilter> filters,
      dynamic Function()? onEnd});
}

/// @nodoc
class __$$SearchImplCopyWithImpl<$Res>
    extends _$SearchCopyWithImpl<$Res, _$SearchImpl>
    implements _$$SearchImplCopyWith<$Res> {
  __$$SearchImplCopyWithImpl(
      _$SearchImpl _value, $Res Function(_$SearchImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? query = null,
    Object? fetchers = null,
    Object? filters = null,
    Object? onEnd = freezed,
  }) {
    return _then(_$SearchImpl(
      query: null == query
          ? _value.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
      fetchers: null == fetchers
          ? _value._fetchers
          : fetchers // ignore: cast_nullable_to_non_nullable
              as List<SearchFetcher>,
      filters: null == filters
          ? _value._filters
          : filters // ignore: cast_nullable_to_non_nullable
              as List<SearchFilter>,
      onEnd: freezed == onEnd
          ? _value.onEnd
          : onEnd // ignore: cast_nullable_to_non_nullable
              as dynamic Function()?,
    ));
  }
}

/// @nodoc

class _$SearchImpl implements _Search {
  const _$SearchImpl(
      {this.query = "",
      final List<SearchFetcher> fetchers = const [],
      final List<SearchFilter> filters = const [],
      this.onEnd})
      : _fetchers = fetchers,
        _filters = filters;

  @override
  @JsonKey()
  final String query;
  final List<SearchFetcher> _fetchers;
  @override
  @JsonKey()
  List<SearchFetcher> get fetchers {
    if (_fetchers is EqualUnmodifiableListView) return _fetchers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_fetchers);
  }

  final List<SearchFilter> _filters;
  @override
  @JsonKey()
  List<SearchFilter> get filters {
    if (_filters is EqualUnmodifiableListView) return _filters;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_filters);
  }

  @override
  final dynamic Function()? onEnd;

  @override
  String toString() {
    return 'Search(query: $query, fetchers: $fetchers, filters: $filters, onEnd: $onEnd)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$SearchImpl &&
            (identical(other.query, query) || other.query == query) &&
            const DeepCollectionEquality().equals(other._fetchers, _fetchers) &&
            const DeepCollectionEquality().equals(other._filters, _filters) &&
            (identical(other.onEnd, onEnd) || other.onEnd == onEnd));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      query,
      const DeepCollectionEquality().hash(_fetchers),
      const DeepCollectionEquality().hash(_filters),
      onEnd);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$SearchImplCopyWith<_$SearchImpl> get copyWith =>
      __$$SearchImplCopyWithImpl<_$SearchImpl>(this, _$identity);
}

abstract class _Search implements Search {
  const factory _Search(
      {final String query,
      final List<SearchFetcher> fetchers,
      final List<SearchFilter> filters,
      final dynamic Function()? onEnd}) = _$SearchImpl;

  @override
  String get query;
  @override
  List<SearchFetcher> get fetchers;
  @override
  List<SearchFilter> get filters;
  @override
  dynamic Function()? get onEnd;
  @override
  @JsonKey(ignore: true)
  _$$SearchImplCopyWith<_$SearchImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
