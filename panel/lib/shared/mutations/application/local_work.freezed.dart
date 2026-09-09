// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'local_work.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;
/// @nodoc
mixin _$EditorResourceKey implements DiagnosticableTreeMixin {

 Object? get scope; Object get identity;
/// Create a copy of EditorResourceKey
/// with the given fields replaced by the non-null parameter values.
@JsonKey(includeFromJson: false, includeToJson: false)
@pragma('vm:prefer-inline')
$EditorResourceKeyCopyWith<EditorResourceKey> get copyWith => _$EditorResourceKeyCopyWithImpl<EditorResourceKey>(this as EditorResourceKey, _$identity);


@override
void debugFillProperties(DiagnosticPropertiesBuilder properties) {
  properties
    ..add(DiagnosticsProperty('type', 'EditorResourceKey'))
    ..add(DiagnosticsProperty('scope', scope))..add(DiagnosticsProperty('identity', identity));
}

@override
bool operator ==(Object other) {
  return identical(this, other) || (other.runtimeType == runtimeType&&other is EditorResourceKey&&const DeepCollectionEquality().equals(other.scope, scope)&&const DeepCollectionEquality().equals(other.identity, identity));
}


@override
int get hashCode => Object.hash(runtimeType,const DeepCollectionEquality().hash(scope),const DeepCollectionEquality().hash(identity));

@override
String toString({ DiagnosticLevel minLevel = DiagnosticLevel.info }) {
  return 'EditorResourceKey(scope: $scope, identity: $identity)';
}


}

/// @nodoc
abstract mixin class $EditorResourceKeyCopyWith<$Res>  {
  factory $EditorResourceKeyCopyWith(EditorResourceKey value, $Res Function(EditorResourceKey) _then) = _$EditorResourceKeyCopyWithImpl;
@useResult
$Res call({
 Object? scope, Object identity
});




}
/// @nodoc
class _$EditorResourceKeyCopyWithImpl<$Res>
    implements $EditorResourceKeyCopyWith<$Res> {
  _$EditorResourceKeyCopyWithImpl(this._self, this._then);

  final EditorResourceKey _self;
  final $Res Function(EditorResourceKey) _then;

/// Create a copy of EditorResourceKey
/// with the given fields replaced by the non-null parameter values.
@pragma('vm:prefer-inline') @override $Res call({Object? scope = freezed,Object? identity = null,}) {
  return _then(_self.copyWith(
scope: freezed == scope ? _self.scope : scope ,identity: null == identity ? _self.identity : identity ,
  ));
}

}


/// Adds pattern-matching-related methods to [EditorResourceKey].
extension EditorResourceKeyPatterns on EditorResourceKey {
/// A variant of `map` that fallback to returning `orElse`.
///
/// It is equivalent to doing:
/// ```dart
/// switch (sealedClass) {
///   case final Subclass value:
///     return ...;
///   case _:
///     return orElse();
/// }
/// ```

@optionalTypeArgs TResult maybeMap<TResult extends Object?>(TResult Function( _EditorResourceKey value)?  $default,{required TResult orElse(),}){
final _that = this;
switch (_that) {
case _EditorResourceKey() when $default != null:
return $default(_that);case _:
  return orElse();

}
}
/// A `switch`-like method, using callbacks.
///
/// Callbacks receives the raw object, upcasted.
/// It is equivalent to doing:
/// ```dart
/// switch (sealedClass) {
///   case final Subclass value:
///     return ...;
///   case final Subclass2 value:
///     return ...;
/// }
/// ```

@optionalTypeArgs TResult map<TResult extends Object?>(TResult Function( _EditorResourceKey value)  $default,){
final _that = this;
switch (_that) {
case _EditorResourceKey():
return $default(_that);case _:
  throw StateError('Unexpected subclass');

}
}
/// A variant of `map` that fallback to returning `null`.
///
/// It is equivalent to doing:
/// ```dart
/// switch (sealedClass) {
///   case final Subclass value:
///     return ...;
///   case _:
///     return null;
/// }
/// ```

@optionalTypeArgs TResult? mapOrNull<TResult extends Object?>(TResult? Function( _EditorResourceKey value)?  $default,){
final _that = this;
switch (_that) {
case _EditorResourceKey() when $default != null:
return $default(_that);case _:
  return null;

}
}
/// A variant of `when` that fallback to an `orElse` callback.
///
/// It is equivalent to doing:
/// ```dart
/// switch (sealedClass) {
///   case Subclass(:final field):
///     return ...;
///   case _:
///     return orElse();
/// }
/// ```

@optionalTypeArgs TResult maybeWhen<TResult extends Object?>(TResult Function( Object? scope,  Object identity)?  $default,{required TResult orElse(),}) {final _that = this;
switch (_that) {
case _EditorResourceKey() when $default != null:
return $default(_that.scope,_that.identity);case _:
  return orElse();

}
}
/// A `switch`-like method, using callbacks.
///
/// As opposed to `map`, this offers destructuring.
/// It is equivalent to doing:
/// ```dart
/// switch (sealedClass) {
///   case Subclass(:final field):
///     return ...;
///   case Subclass2(:final field2):
///     return ...;
/// }
/// ```

@optionalTypeArgs TResult when<TResult extends Object?>(TResult Function( Object? scope,  Object identity)  $default,) {final _that = this;
switch (_that) {
case _EditorResourceKey():
return $default(_that.scope,_that.identity);case _:
  throw StateError('Unexpected subclass');

}
}
/// A variant of `when` that fallback to returning `null`
///
/// It is equivalent to doing:
/// ```dart
/// switch (sealedClass) {
///   case Subclass(:final field):
///     return ...;
///   case _:
///     return null;
/// }
/// ```

@optionalTypeArgs TResult? whenOrNull<TResult extends Object?>(TResult? Function( Object? scope,  Object identity)?  $default,) {final _that = this;
switch (_that) {
case _EditorResourceKey() when $default != null:
return $default(_that.scope,_that.identity);case _:
  return null;

}
}

}

/// @nodoc


class _EditorResourceKey with DiagnosticableTreeMixin implements EditorResourceKey {
  const _EditorResourceKey({required this.scope, required this.identity});
  

@override final  Object? scope;
@override final  Object identity;

/// Create a copy of EditorResourceKey
/// with the given fields replaced by the non-null parameter values.
@override @JsonKey(includeFromJson: false, includeToJson: false)
@pragma('vm:prefer-inline')
_$EditorResourceKeyCopyWith<_EditorResourceKey> get copyWith => __$EditorResourceKeyCopyWithImpl<_EditorResourceKey>(this, _$identity);


@override
void debugFillProperties(DiagnosticPropertiesBuilder properties) {
  properties
    ..add(DiagnosticsProperty('type', 'EditorResourceKey'))
    ..add(DiagnosticsProperty('scope', scope))..add(DiagnosticsProperty('identity', identity));
}

@override
bool operator ==(Object other) {
  return identical(this, other) || (other.runtimeType == runtimeType&&other is _EditorResourceKey&&const DeepCollectionEquality().equals(other.scope, scope)&&const DeepCollectionEquality().equals(other.identity, identity));
}


@override
int get hashCode => Object.hash(runtimeType,const DeepCollectionEquality().hash(scope),const DeepCollectionEquality().hash(identity));

@override
String toString({ DiagnosticLevel minLevel = DiagnosticLevel.info }) {
  return 'EditorResourceKey(scope: $scope, identity: $identity)';
}


}

/// @nodoc
abstract mixin class _$EditorResourceKeyCopyWith<$Res> implements $EditorResourceKeyCopyWith<$Res> {
  factory _$EditorResourceKeyCopyWith(_EditorResourceKey value, $Res Function(_EditorResourceKey) _then) = __$EditorResourceKeyCopyWithImpl;
@override @useResult
$Res call({
 Object? scope, Object identity
});




}
/// @nodoc
class __$EditorResourceKeyCopyWithImpl<$Res>
    implements _$EditorResourceKeyCopyWith<$Res> {
  __$EditorResourceKeyCopyWithImpl(this._self, this._then);

  final _EditorResourceKey _self;
  final $Res Function(_EditorResourceKey) _then;

/// Create a copy of EditorResourceKey
/// with the given fields replaced by the non-null parameter values.
@override @pragma('vm:prefer-inline') $Res call({Object? scope = freezed,Object? identity = null,}) {
  return _then(_EditorResourceKey(
scope: freezed == scope ? _self.scope : scope ,identity: null == identity ? _self.identity : identity ,
  ));
}


}

// dart format on
