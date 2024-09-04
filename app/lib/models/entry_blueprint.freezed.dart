// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'entry_blueprint.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

EntryBlueprint _$EntryBlueprintFromJson(Map<String, dynamic> json) {
  return _EntryBlueprint.fromJson(json);
}

/// @nodoc
mixin _$EntryBlueprint {
  String get id => throw _privateConstructorUsedError;
  String get name => throw _privateConstructorUsedError;
  String get description => throw _privateConstructorUsedError;
  String get extension => throw _privateConstructorUsedError;
  ObjectBlueprint get dataBlueprint => throw _privateConstructorUsedError;
  List<String> get tags => throw _privateConstructorUsedError;
  @ColorConverter()
  Color get color => throw _privateConstructorUsedError;
  String get icon => throw _privateConstructorUsedError;

  /// Serializes this EntryBlueprint to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of EntryBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $EntryBlueprintCopyWith<EntryBlueprint> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $EntryBlueprintCopyWith<$Res> {
  factory $EntryBlueprintCopyWith(
          EntryBlueprint value, $Res Function(EntryBlueprint) then) =
      _$EntryBlueprintCopyWithImpl<$Res, EntryBlueprint>;
  @useResult
  $Res call(
      {String id,
      String name,
      String description,
      String extension,
      ObjectBlueprint dataBlueprint,
      List<String> tags,
      @ColorConverter() Color color,
      String icon});
}

/// @nodoc
class _$EntryBlueprintCopyWithImpl<$Res, $Val extends EntryBlueprint>
    implements $EntryBlueprintCopyWith<$Res> {
  _$EntryBlueprintCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of EntryBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? name = null,
    Object? description = null,
    Object? extension = null,
    Object? dataBlueprint = freezed,
    Object? tags = null,
    Object? color = null,
    Object? icon = null,
  }) {
    return _then(_value.copyWith(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      extension: null == extension
          ? _value.extension
          : extension // ignore: cast_nullable_to_non_nullable
              as String,
      dataBlueprint: freezed == dataBlueprint
          ? _value.dataBlueprint
          : dataBlueprint // ignore: cast_nullable_to_non_nullable
              as ObjectBlueprint,
      tags: null == tags
          ? _value.tags
          : tags // ignore: cast_nullable_to_non_nullable
              as List<String>,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$EntryBlueprintImplCopyWith<$Res>
    implements $EntryBlueprintCopyWith<$Res> {
  factory _$$EntryBlueprintImplCopyWith(_$EntryBlueprintImpl value,
          $Res Function(_$EntryBlueprintImpl) then) =
      __$$EntryBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String id,
      String name,
      String description,
      String extension,
      ObjectBlueprint dataBlueprint,
      List<String> tags,
      @ColorConverter() Color color,
      String icon});
}

/// @nodoc
class __$$EntryBlueprintImplCopyWithImpl<$Res>
    extends _$EntryBlueprintCopyWithImpl<$Res, _$EntryBlueprintImpl>
    implements _$$EntryBlueprintImplCopyWith<$Res> {
  __$$EntryBlueprintImplCopyWithImpl(
      _$EntryBlueprintImpl _value, $Res Function(_$EntryBlueprintImpl) _then)
      : super(_value, _then);

  /// Create a copy of EntryBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? name = null,
    Object? description = null,
    Object? extension = null,
    Object? dataBlueprint = freezed,
    Object? tags = null,
    Object? color = null,
    Object? icon = null,
  }) {
    return _then(_$EntryBlueprintImpl(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      extension: null == extension
          ? _value.extension
          : extension // ignore: cast_nullable_to_non_nullable
              as String,
      dataBlueprint: freezed == dataBlueprint
          ? _value.dataBlueprint
          : dataBlueprint // ignore: cast_nullable_to_non_nullable
              as ObjectBlueprint,
      tags: null == tags
          ? _value._tags
          : tags // ignore: cast_nullable_to_non_nullable
              as List<String>,
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      icon: null == icon
          ? _value.icon
          : icon // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$EntryBlueprintImpl
    with DiagnosticableTreeMixin
    implements _EntryBlueprint {
  const _$EntryBlueprintImpl(
      {required this.id,
      required this.name,
      required this.description,
      required this.extension,
      required this.dataBlueprint,
      final List<String> tags = const <String>[],
      @ColorConverter() this.color = Colors.grey,
      this.icon = TWIcons.help})
      : _tags = tags;

  factory _$EntryBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$EntryBlueprintImplFromJson(json);

  @override
  final String id;
  @override
  final String name;
  @override
  final String description;
  @override
  final String extension;
  @override
  final ObjectBlueprint dataBlueprint;
  final List<String> _tags;
  @override
  @JsonKey()
  List<String> get tags {
    if (_tags is EqualUnmodifiableListView) return _tags;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_tags);
  }

  @override
  @JsonKey()
  @ColorConverter()
  final Color color;
  @override
  @JsonKey()
  final String icon;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'EntryBlueprint(id: $id, name: $name, description: $description, extension: $extension, dataBlueprint: $dataBlueprint, tags: $tags, color: $color, icon: $icon)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'EntryBlueprint'))
      ..add(DiagnosticsProperty('id', id))
      ..add(DiagnosticsProperty('name', name))
      ..add(DiagnosticsProperty('description', description))
      ..add(DiagnosticsProperty('extension', extension))
      ..add(DiagnosticsProperty('dataBlueprint', dataBlueprint))
      ..add(DiagnosticsProperty('tags', tags))
      ..add(DiagnosticsProperty('color', color))
      ..add(DiagnosticsProperty('icon', icon));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EntryBlueprintImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.extension, extension) ||
                other.extension == extension) &&
            const DeepCollectionEquality()
                .equals(other.dataBlueprint, dataBlueprint) &&
            const DeepCollectionEquality().equals(other._tags, _tags) &&
            (identical(other.color, color) || other.color == color) &&
            (identical(other.icon, icon) || other.icon == icon));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      id,
      name,
      description,
      extension,
      const DeepCollectionEquality().hash(dataBlueprint),
      const DeepCollectionEquality().hash(_tags),
      color,
      icon);

  /// Create a copy of EntryBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$EntryBlueprintImplCopyWith<_$EntryBlueprintImpl> get copyWith =>
      __$$EntryBlueprintImplCopyWithImpl<_$EntryBlueprintImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$EntryBlueprintImplToJson(
      this,
    );
  }
}

abstract class _EntryBlueprint implements EntryBlueprint {
  const factory _EntryBlueprint(
      {required final String id,
      required final String name,
      required final String description,
      required final String extension,
      required final ObjectBlueprint dataBlueprint,
      final List<String> tags,
      @ColorConverter() final Color color,
      final String icon}) = _$EntryBlueprintImpl;

  factory _EntryBlueprint.fromJson(Map<String, dynamic> json) =
      _$EntryBlueprintImpl.fromJson;

  @override
  String get id;
  @override
  String get name;
  @override
  String get description;
  @override
  String get extension;
  @override
  ObjectBlueprint get dataBlueprint;
  @override
  List<String> get tags;
  @override
  @ColorConverter()
  Color get color;
  @override
  String get icon;

  /// Create a copy of EntryBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$EntryBlueprintImplCopyWith<_$EntryBlueprintImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

DataBlueprint _$DataBlueprintFromJson(Map<String, dynamic> json) {
  switch (json['kind']) {
    case 'default':
      return _DataBlueprintType.fromJson(json);
    case 'primitive':
      return PrimitiveBlueprint.fromJson(json);
    case 'enum':
      return EnumBlueprint.fromJson(json);
    case 'list':
      return ListBlueprint.fromJson(json);
    case 'map':
      return MapBlueprint.fromJson(json);
    case 'object':
      return ObjectBlueprint.fromJson(json);
    case 'custom':
      return CustomBlueprint.fromJson(json);

    default:
      throw CheckedFromJsonException(json, 'kind', 'DataBlueprint',
          'Invalid union type "${json['kind']}"!');
  }
}

/// @nodoc
mixin _$DataBlueprint {
  @JsonKey(name: "default")
  dynamic get internalDefaultValue => throw _privateConstructorUsedError;
  List<Modifier> get modifiers => throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;

  /// Serializes this DataBlueprint to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $DataBlueprintCopyWith<DataBlueprint> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $DataBlueprintCopyWith<$Res> {
  factory $DataBlueprintCopyWith(
          DataBlueprint value, $Res Function(DataBlueprint) then) =
      _$DataBlueprintCopyWithImpl<$Res, DataBlueprint>;
  @useResult
  $Res call(
      {@JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});
}

/// @nodoc
class _$DataBlueprintCopyWithImpl<$Res, $Val extends DataBlueprint>
    implements $DataBlueprintCopyWith<$Res> {
  _$DataBlueprintCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_value.copyWith(
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value.modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$DataBlueprintTypeImplCopyWith<$Res>
    implements $DataBlueprintCopyWith<$Res> {
  factory _$$DataBlueprintTypeImplCopyWith(_$DataBlueprintTypeImpl value,
          $Res Function(_$DataBlueprintTypeImpl) then) =
      __$$DataBlueprintTypeImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});
}

/// @nodoc
class __$$DataBlueprintTypeImplCopyWithImpl<$Res>
    extends _$DataBlueprintCopyWithImpl<$Res, _$DataBlueprintTypeImpl>
    implements _$$DataBlueprintTypeImplCopyWith<$Res> {
  __$$DataBlueprintTypeImplCopyWithImpl(_$DataBlueprintTypeImpl _value,
      $Res Function(_$DataBlueprintTypeImpl) _then)
      : super(_value, _then);

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$DataBlueprintTypeImpl(
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$DataBlueprintTypeImpl
    with DiagnosticableTreeMixin
    implements _DataBlueprintType {
  const _$DataBlueprintTypeImpl(
      {@JsonKey(name: "default") this.internalDefaultValue,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'default';

  factory _$DataBlueprintTypeImpl.fromJson(Map<String, dynamic> json) =>
      _$$DataBlueprintTypeImplFromJson(json);

  @override
  @JsonKey(name: "default")
  final dynamic internalDefaultValue;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    if (_modifiers is EqualUnmodifiableListView) return _modifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'DataBlueprint(internalDefaultValue: $internalDefaultValue, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'DataBlueprint'))
      ..add(DiagnosticsProperty('internalDefaultValue', internalDefaultValue))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$DataBlueprintTypeImpl &&
            const DeepCollectionEquality()
                .equals(other.internalDefaultValue, internalDefaultValue) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(internalDefaultValue),
      const DeepCollectionEquality().hash(_modifiers));

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$DataBlueprintTypeImplCopyWith<_$DataBlueprintTypeImpl> get copyWith =>
      __$$DataBlueprintTypeImplCopyWithImpl<_$DataBlueprintTypeImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) {
    return $default(internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) {
    return $default?.call(internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(internalDefaultValue, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) {
    return $default(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) {
    return $default?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$DataBlueprintTypeImplToJson(
      this,
    );
  }
}

abstract class _DataBlueprintType implements DataBlueprint {
  const factory _DataBlueprintType(
      {@JsonKey(name: "default") final dynamic internalDefaultValue,
      final List<Modifier> modifiers}) = _$DataBlueprintTypeImpl;

  factory _DataBlueprintType.fromJson(Map<String, dynamic> json) =
      _$DataBlueprintTypeImpl.fromJson;

  @override
  @JsonKey(name: "default")
  dynamic get internalDefaultValue;
  @override
  List<Modifier> get modifiers;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$DataBlueprintTypeImplCopyWith<_$DataBlueprintTypeImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$PrimitiveBlueprintImplCopyWith<$Res>
    implements $DataBlueprintCopyWith<$Res> {
  factory _$$PrimitiveBlueprintImplCopyWith(_$PrimitiveBlueprintImpl value,
          $Res Function(_$PrimitiveBlueprintImpl) then) =
      __$$PrimitiveBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {PrimitiveType type,
      @JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});
}

/// @nodoc
class __$$PrimitiveBlueprintImplCopyWithImpl<$Res>
    extends _$DataBlueprintCopyWithImpl<$Res, _$PrimitiveBlueprintImpl>
    implements _$$PrimitiveBlueprintImplCopyWith<$Res> {
  __$$PrimitiveBlueprintImplCopyWithImpl(_$PrimitiveBlueprintImpl _value,
      $Res Function(_$PrimitiveBlueprintImpl) _then)
      : super(_value, _then);

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$PrimitiveBlueprintImpl(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as PrimitiveType,
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$PrimitiveBlueprintImpl
    with DiagnosticableTreeMixin
    implements PrimitiveBlueprint {
  const _$PrimitiveBlueprintImpl(
      {required this.type,
      @JsonKey(name: "default") this.internalDefaultValue,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'primitive';

  factory _$PrimitiveBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$PrimitiveBlueprintImplFromJson(json);

  @override
  final PrimitiveType type;
  @override
  @JsonKey(name: "default")
  final dynamic internalDefaultValue;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    if (_modifiers is EqualUnmodifiableListView) return _modifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'DataBlueprint.primitive(type: $type, internalDefaultValue: $internalDefaultValue, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'DataBlueprint.primitive'))
      ..add(DiagnosticsProperty('type', type))
      ..add(DiagnosticsProperty('internalDefaultValue', internalDefaultValue))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$PrimitiveBlueprintImpl &&
            (identical(other.type, type) || other.type == type) &&
            const DeepCollectionEquality()
                .equals(other.internalDefaultValue, internalDefaultValue) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      type,
      const DeepCollectionEquality().hash(internalDefaultValue),
      const DeepCollectionEquality().hash(_modifiers));

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$PrimitiveBlueprintImplCopyWith<_$PrimitiveBlueprintImpl> get copyWith =>
      __$$PrimitiveBlueprintImplCopyWithImpl<_$PrimitiveBlueprintImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) {
    return primitive(type, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) {
    return primitive?.call(type, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if (primitive != null) {
      return primitive(type, internalDefaultValue, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) {
    return primitive(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) {
    return primitive?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) {
    if (primitive != null) {
      return primitive(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$PrimitiveBlueprintImplToJson(
      this,
    );
  }
}

abstract class PrimitiveBlueprint implements DataBlueprint {
  const factory PrimitiveBlueprint(
      {required final PrimitiveType type,
      @JsonKey(name: "default") final dynamic internalDefaultValue,
      final List<Modifier> modifiers}) = _$PrimitiveBlueprintImpl;

  factory PrimitiveBlueprint.fromJson(Map<String, dynamic> json) =
      _$PrimitiveBlueprintImpl.fromJson;

  PrimitiveType get type;
  @override
  @JsonKey(name: "default")
  dynamic get internalDefaultValue;
  @override
  List<Modifier> get modifiers;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$PrimitiveBlueprintImplCopyWith<_$PrimitiveBlueprintImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$EnumBlueprintImplCopyWith<$Res>
    implements $DataBlueprintCopyWith<$Res> {
  factory _$$EnumBlueprintImplCopyWith(
          _$EnumBlueprintImpl value, $Res Function(_$EnumBlueprintImpl) then) =
      __$$EnumBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {List<String> values,
      @JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});
}

/// @nodoc
class __$$EnumBlueprintImplCopyWithImpl<$Res>
    extends _$DataBlueprintCopyWithImpl<$Res, _$EnumBlueprintImpl>
    implements _$$EnumBlueprintImplCopyWith<$Res> {
  __$$EnumBlueprintImplCopyWithImpl(
      _$EnumBlueprintImpl _value, $Res Function(_$EnumBlueprintImpl) _then)
      : super(_value, _then);

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? values = null,
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$EnumBlueprintImpl(
      values: null == values
          ? _value._values
          : values // ignore: cast_nullable_to_non_nullable
              as List<String>,
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$EnumBlueprintImpl
    with DiagnosticableTreeMixin
    implements EnumBlueprint {
  const _$EnumBlueprintImpl(
      {required final List<String> values,
      @JsonKey(name: "default") this.internalDefaultValue,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _values = values,
        _modifiers = modifiers,
        $type = $type ?? 'enum';

  factory _$EnumBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$EnumBlueprintImplFromJson(json);

  final List<String> _values;
  @override
  List<String> get values {
    if (_values is EqualUnmodifiableListView) return _values;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_values);
  }

  @override
  @JsonKey(name: "default")
  final dynamic internalDefaultValue;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    if (_modifiers is EqualUnmodifiableListView) return _modifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'DataBlueprint.enumBlueprint(values: $values, internalDefaultValue: $internalDefaultValue, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'DataBlueprint.enumBlueprint'))
      ..add(DiagnosticsProperty('values', values))
      ..add(DiagnosticsProperty('internalDefaultValue', internalDefaultValue))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EnumBlueprintImpl &&
            const DeepCollectionEquality().equals(other._values, _values) &&
            const DeepCollectionEquality()
                .equals(other.internalDefaultValue, internalDefaultValue) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_values),
      const DeepCollectionEquality().hash(internalDefaultValue),
      const DeepCollectionEquality().hash(_modifiers));

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$EnumBlueprintImplCopyWith<_$EnumBlueprintImpl> get copyWith =>
      __$$EnumBlueprintImplCopyWithImpl<_$EnumBlueprintImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) {
    return enumBlueprint(values, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) {
    return enumBlueprint?.call(values, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if (enumBlueprint != null) {
      return enumBlueprint(values, internalDefaultValue, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) {
    return enumBlueprint(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) {
    return enumBlueprint?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) {
    if (enumBlueprint != null) {
      return enumBlueprint(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$EnumBlueprintImplToJson(
      this,
    );
  }
}

abstract class EnumBlueprint implements DataBlueprint {
  const factory EnumBlueprint(
      {required final List<String> values,
      @JsonKey(name: "default") final dynamic internalDefaultValue,
      final List<Modifier> modifiers}) = _$EnumBlueprintImpl;

  factory EnumBlueprint.fromJson(Map<String, dynamic> json) =
      _$EnumBlueprintImpl.fromJson;

  List<String> get values;
  @override
  @JsonKey(name: "default")
  dynamic get internalDefaultValue;
  @override
  List<Modifier> get modifiers;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$EnumBlueprintImplCopyWith<_$EnumBlueprintImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ListBlueprintImplCopyWith<$Res>
    implements $DataBlueprintCopyWith<$Res> {
  factory _$$ListBlueprintImplCopyWith(
          _$ListBlueprintImpl value, $Res Function(_$ListBlueprintImpl) then) =
      __$$ListBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {DataBlueprint type,
      @JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});

  $DataBlueprintCopyWith<$Res> get type;
}

/// @nodoc
class __$$ListBlueprintImplCopyWithImpl<$Res>
    extends _$DataBlueprintCopyWithImpl<$Res, _$ListBlueprintImpl>
    implements _$$ListBlueprintImplCopyWith<$Res> {
  __$$ListBlueprintImplCopyWithImpl(
      _$ListBlueprintImpl _value, $Res Function(_$ListBlueprintImpl) _then)
      : super(_value, _then);

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$ListBlueprintImpl(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as DataBlueprint,
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DataBlueprintCopyWith<$Res> get type {
    return $DataBlueprintCopyWith<$Res>(_value.type, (value) {
      return _then(_value.copyWith(type: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$ListBlueprintImpl
    with DiagnosticableTreeMixin
    implements ListBlueprint {
  const _$ListBlueprintImpl(
      {required this.type,
      @JsonKey(name: "default") this.internalDefaultValue,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'list';

  factory _$ListBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$ListBlueprintImplFromJson(json);

  @override
  final DataBlueprint type;
  @override
  @JsonKey(name: "default")
  final dynamic internalDefaultValue;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    if (_modifiers is EqualUnmodifiableListView) return _modifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'DataBlueprint.list(type: $type, internalDefaultValue: $internalDefaultValue, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'DataBlueprint.list'))
      ..add(DiagnosticsProperty('type', type))
      ..add(DiagnosticsProperty('internalDefaultValue', internalDefaultValue))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ListBlueprintImpl &&
            (identical(other.type, type) || other.type == type) &&
            const DeepCollectionEquality()
                .equals(other.internalDefaultValue, internalDefaultValue) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      type,
      const DeepCollectionEquality().hash(internalDefaultValue),
      const DeepCollectionEquality().hash(_modifiers));

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$ListBlueprintImplCopyWith<_$ListBlueprintImpl> get copyWith =>
      __$$ListBlueprintImplCopyWithImpl<_$ListBlueprintImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) {
    return list(type, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) {
    return list?.call(type, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if (list != null) {
      return list(type, internalDefaultValue, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) {
    return list(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) {
    return list?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) {
    if (list != null) {
      return list(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$ListBlueprintImplToJson(
      this,
    );
  }
}

abstract class ListBlueprint implements DataBlueprint {
  const factory ListBlueprint(
      {required final DataBlueprint type,
      @JsonKey(name: "default") final dynamic internalDefaultValue,
      final List<Modifier> modifiers}) = _$ListBlueprintImpl;

  factory ListBlueprint.fromJson(Map<String, dynamic> json) =
      _$ListBlueprintImpl.fromJson;

  DataBlueprint get type;
  @override
  @JsonKey(name: "default")
  dynamic get internalDefaultValue;
  @override
  List<Modifier> get modifiers;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$ListBlueprintImplCopyWith<_$ListBlueprintImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$MapBlueprintImplCopyWith<$Res>
    implements $DataBlueprintCopyWith<$Res> {
  factory _$$MapBlueprintImplCopyWith(
          _$MapBlueprintImpl value, $Res Function(_$MapBlueprintImpl) then) =
      __$$MapBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {DataBlueprint key,
      DataBlueprint value,
      @JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});

  $DataBlueprintCopyWith<$Res> get key;
  $DataBlueprintCopyWith<$Res> get value;
}

/// @nodoc
class __$$MapBlueprintImplCopyWithImpl<$Res>
    extends _$DataBlueprintCopyWithImpl<$Res, _$MapBlueprintImpl>
    implements _$$MapBlueprintImplCopyWith<$Res> {
  __$$MapBlueprintImplCopyWithImpl(
      _$MapBlueprintImpl _value, $Res Function(_$MapBlueprintImpl) _then)
      : super(_value, _then);

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$MapBlueprintImpl(
      key: null == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as DataBlueprint,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as DataBlueprint,
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DataBlueprintCopyWith<$Res> get key {
    return $DataBlueprintCopyWith<$Res>(_value.key, (value) {
      return _then(_value.copyWith(key: value));
    });
  }

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DataBlueprintCopyWith<$Res> get value {
    return $DataBlueprintCopyWith<$Res>(_value.value, (value) {
      return _then(_value.copyWith(value: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$MapBlueprintImpl with DiagnosticableTreeMixin implements MapBlueprint {
  const _$MapBlueprintImpl(
      {required this.key,
      required this.value,
      @JsonKey(name: "default") this.internalDefaultValue,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'map';

  factory _$MapBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$MapBlueprintImplFromJson(json);

  @override
  final DataBlueprint key;
  @override
  final DataBlueprint value;
  @override
  @JsonKey(name: "default")
  final dynamic internalDefaultValue;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    if (_modifiers is EqualUnmodifiableListView) return _modifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'DataBlueprint.map(key: $key, value: $value, internalDefaultValue: $internalDefaultValue, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'DataBlueprint.map'))
      ..add(DiagnosticsProperty('key', key))
      ..add(DiagnosticsProperty('value', value))
      ..add(DiagnosticsProperty('internalDefaultValue', internalDefaultValue))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MapBlueprintImpl &&
            (identical(other.key, key) || other.key == key) &&
            (identical(other.value, value) || other.value == value) &&
            const DeepCollectionEquality()
                .equals(other.internalDefaultValue, internalDefaultValue) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      key,
      value,
      const DeepCollectionEquality().hash(internalDefaultValue),
      const DeepCollectionEquality().hash(_modifiers));

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$MapBlueprintImplCopyWith<_$MapBlueprintImpl> get copyWith =>
      __$$MapBlueprintImplCopyWithImpl<_$MapBlueprintImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) {
    return map(key, value, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) {
    return map?.call(key, value, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if (map != null) {
      return map(key, value, internalDefaultValue, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) {
    return map(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) {
    return map?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) {
    if (map != null) {
      return map(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$MapBlueprintImplToJson(
      this,
    );
  }
}

abstract class MapBlueprint implements DataBlueprint {
  const factory MapBlueprint(
      {required final DataBlueprint key,
      required final DataBlueprint value,
      @JsonKey(name: "default") final dynamic internalDefaultValue,
      final List<Modifier> modifiers}) = _$MapBlueprintImpl;

  factory MapBlueprint.fromJson(Map<String, dynamic> json) =
      _$MapBlueprintImpl.fromJson;

  DataBlueprint get key;
  DataBlueprint get value;
  @override
  @JsonKey(name: "default")
  dynamic get internalDefaultValue;
  @override
  List<Modifier> get modifiers;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$MapBlueprintImplCopyWith<_$MapBlueprintImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ObjectBlueprintImplCopyWith<$Res>
    implements $DataBlueprintCopyWith<$Res> {
  factory _$$ObjectBlueprintImplCopyWith(_$ObjectBlueprintImpl value,
          $Res Function(_$ObjectBlueprintImpl) then) =
      __$$ObjectBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {Map<String, DataBlueprint> fields,
      @JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});
}

/// @nodoc
class __$$ObjectBlueprintImplCopyWithImpl<$Res>
    extends _$DataBlueprintCopyWithImpl<$Res, _$ObjectBlueprintImpl>
    implements _$$ObjectBlueprintImplCopyWith<$Res> {
  __$$ObjectBlueprintImplCopyWithImpl(
      _$ObjectBlueprintImpl _value, $Res Function(_$ObjectBlueprintImpl) _then)
      : super(_value, _then);

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fields = null,
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$ObjectBlueprintImpl(
      fields: null == fields
          ? _value._fields
          : fields // ignore: cast_nullable_to_non_nullable
              as Map<String, DataBlueprint>,
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ObjectBlueprintImpl
    with DiagnosticableTreeMixin
    implements ObjectBlueprint {
  const _$ObjectBlueprintImpl(
      {required final Map<String, DataBlueprint> fields,
      @JsonKey(name: "default") this.internalDefaultValue,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _fields = fields,
        _modifiers = modifiers,
        $type = $type ?? 'object';

  factory _$ObjectBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$ObjectBlueprintImplFromJson(json);

  final Map<String, DataBlueprint> _fields;
  @override
  Map<String, DataBlueprint> get fields {
    if (_fields is EqualUnmodifiableMapView) return _fields;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_fields);
  }

  @override
  @JsonKey(name: "default")
  final dynamic internalDefaultValue;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    if (_modifiers is EqualUnmodifiableListView) return _modifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'DataBlueprint.object(fields: $fields, internalDefaultValue: $internalDefaultValue, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'DataBlueprint.object'))
      ..add(DiagnosticsProperty('fields', fields))
      ..add(DiagnosticsProperty('internalDefaultValue', internalDefaultValue))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ObjectBlueprintImpl &&
            const DeepCollectionEquality().equals(other._fields, _fields) &&
            const DeepCollectionEquality()
                .equals(other.internalDefaultValue, internalDefaultValue) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_fields),
      const DeepCollectionEquality().hash(internalDefaultValue),
      const DeepCollectionEquality().hash(_modifiers));

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$ObjectBlueprintImplCopyWith<_$ObjectBlueprintImpl> get copyWith =>
      __$$ObjectBlueprintImplCopyWithImpl<_$ObjectBlueprintImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) {
    return object(fields, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) {
    return object?.call(fields, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if (object != null) {
      return object(fields, internalDefaultValue, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) {
    return object(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) {
    return object?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) {
    if (object != null) {
      return object(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$ObjectBlueprintImplToJson(
      this,
    );
  }
}

abstract class ObjectBlueprint implements DataBlueprint {
  const factory ObjectBlueprint(
      {required final Map<String, DataBlueprint> fields,
      @JsonKey(name: "default") final dynamic internalDefaultValue,
      final List<Modifier> modifiers}) = _$ObjectBlueprintImpl;

  factory ObjectBlueprint.fromJson(Map<String, dynamic> json) =
      _$ObjectBlueprintImpl.fromJson;

  Map<String, DataBlueprint> get fields;
  @override
  @JsonKey(name: "default")
  dynamic get internalDefaultValue;
  @override
  List<Modifier> get modifiers;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$ObjectBlueprintImplCopyWith<_$ObjectBlueprintImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$CustomBlueprintImplCopyWith<$Res>
    implements $DataBlueprintCopyWith<$Res> {
  factory _$$CustomBlueprintImplCopyWith(_$CustomBlueprintImpl value,
          $Res Function(_$CustomBlueprintImpl) then) =
      __$$CustomBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String editor,
      DataBlueprint shape,
      @JsonKey(name: "default") dynamic internalDefaultValue,
      List<Modifier> modifiers});

  $DataBlueprintCopyWith<$Res> get shape;
}

/// @nodoc
class __$$CustomBlueprintImplCopyWithImpl<$Res>
    extends _$DataBlueprintCopyWithImpl<$Res, _$CustomBlueprintImpl>
    implements _$$CustomBlueprintImplCopyWith<$Res> {
  __$$CustomBlueprintImplCopyWithImpl(
      _$CustomBlueprintImpl _value, $Res Function(_$CustomBlueprintImpl) _then)
      : super(_value, _then);

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? editor = null,
    Object? shape = null,
    Object? internalDefaultValue = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$CustomBlueprintImpl(
      editor: null == editor
          ? _value.editor
          : editor // ignore: cast_nullable_to_non_nullable
              as String,
      shape: null == shape
          ? _value.shape
          : shape // ignore: cast_nullable_to_non_nullable
              as DataBlueprint,
      internalDefaultValue: freezed == internalDefaultValue
          ? _value.internalDefaultValue
          : internalDefaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DataBlueprintCopyWith<$Res> get shape {
    return $DataBlueprintCopyWith<$Res>(_value.shape, (value) {
      return _then(_value.copyWith(shape: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$CustomBlueprintImpl
    with DiagnosticableTreeMixin
    implements CustomBlueprint {
  const _$CustomBlueprintImpl(
      {required this.editor,
      required this.shape,
      @JsonKey(name: "default") this.internalDefaultValue,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'custom';

  factory _$CustomBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$CustomBlueprintImplFromJson(json);

  @override
  final String editor;
  @override
  final DataBlueprint shape;
  @override
  @JsonKey(name: "default")
  final dynamic internalDefaultValue;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    if (_modifiers is EqualUnmodifiableListView) return _modifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'DataBlueprint.custom(editor: $editor, shape: $shape, internalDefaultValue: $internalDefaultValue, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'DataBlueprint.custom'))
      ..add(DiagnosticsProperty('editor', editor))
      ..add(DiagnosticsProperty('shape', shape))
      ..add(DiagnosticsProperty('internalDefaultValue', internalDefaultValue))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$CustomBlueprintImpl &&
            (identical(other.editor, editor) || other.editor == editor) &&
            (identical(other.shape, shape) || other.shape == shape) &&
            const DeepCollectionEquality()
                .equals(other.internalDefaultValue, internalDefaultValue) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      editor,
      shape,
      const DeepCollectionEquality().hash(internalDefaultValue),
      const DeepCollectionEquality().hash(_modifiers));

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$CustomBlueprintImplCopyWith<_$CustomBlueprintImpl> get copyWith =>
      __$$CustomBlueprintImplCopyWithImpl<_$CustomBlueprintImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        $default, {
    required TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        primitive,
    required TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        enumBlueprint,
    required TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        list,
    required TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        object,
    required TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)
        custom,
  }) {
    return custom(editor, shape, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult? Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult? Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult? Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult? Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult? Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult? Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
  }) {
    return custom?.call(editor, shape, internalDefaultValue, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(@JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        $default, {
    TResult Function(
            PrimitiveType type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        primitive,
    TResult Function(
            List<String> values,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        enumBlueprint,
    TResult Function(
            DataBlueprint type,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        list,
    TResult Function(
            DataBlueprint key,
            DataBlueprint value,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        map,
    TResult Function(
            Map<String, DataBlueprint> fields,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        object,
    TResult Function(
            String editor,
            DataBlueprint shape,
            @JsonKey(name: "default") dynamic internalDefaultValue,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if (custom != null) {
      return custom(editor, shape, internalDefaultValue, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_DataBlueprintType value) $default, {
    required TResult Function(PrimitiveBlueprint value) primitive,
    required TResult Function(EnumBlueprint value) enumBlueprint,
    required TResult Function(ListBlueprint value) list,
    required TResult Function(MapBlueprint value) map,
    required TResult Function(ObjectBlueprint value) object,
    required TResult Function(CustomBlueprint value) custom,
  }) {
    return custom(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataBlueprintType value)? $default, {
    TResult? Function(PrimitiveBlueprint value)? primitive,
    TResult? Function(EnumBlueprint value)? enumBlueprint,
    TResult? Function(ListBlueprint value)? list,
    TResult? Function(MapBlueprint value)? map,
    TResult? Function(ObjectBlueprint value)? object,
    TResult? Function(CustomBlueprint value)? custom,
  }) {
    return custom?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataBlueprintType value)? $default, {
    TResult Function(PrimitiveBlueprint value)? primitive,
    TResult Function(EnumBlueprint value)? enumBlueprint,
    TResult Function(ListBlueprint value)? list,
    TResult Function(MapBlueprint value)? map,
    TResult Function(ObjectBlueprint value)? object,
    TResult Function(CustomBlueprint value)? custom,
    required TResult orElse(),
  }) {
    if (custom != null) {
      return custom(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$CustomBlueprintImplToJson(
      this,
    );
  }
}

abstract class CustomBlueprint implements DataBlueprint {
  const factory CustomBlueprint(
      {required final String editor,
      required final DataBlueprint shape,
      @JsonKey(name: "default") final dynamic internalDefaultValue,
      final List<Modifier> modifiers}) = _$CustomBlueprintImpl;

  factory CustomBlueprint.fromJson(Map<String, dynamic> json) =
      _$CustomBlueprintImpl.fromJson;

  String get editor;
  DataBlueprint get shape;
  @override
  @JsonKey(name: "default")
  dynamic get internalDefaultValue;
  @override
  List<Modifier> get modifiers;

  /// Create a copy of DataBlueprint
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$CustomBlueprintImplCopyWith<_$CustomBlueprintImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Modifier _$ModifierFromJson(Map<String, dynamic> json) {
  return _Modifier.fromJson(json);
}

/// @nodoc
mixin _$Modifier {
  String get name => throw _privateConstructorUsedError;
  dynamic get data => throw _privateConstructorUsedError;

  /// Serializes this Modifier to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of Modifier
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $ModifierCopyWith<Modifier> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ModifierCopyWith<$Res> {
  factory $ModifierCopyWith(Modifier value, $Res Function(Modifier) then) =
      _$ModifierCopyWithImpl<$Res, Modifier>;
  @useResult
  $Res call({String name, dynamic data});
}

/// @nodoc
class _$ModifierCopyWithImpl<$Res, $Val extends Modifier>
    implements $ModifierCopyWith<$Res> {
  _$ModifierCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of Modifier
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? data = freezed,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      data: freezed == data
          ? _value.data
          : data // ignore: cast_nullable_to_non_nullable
              as dynamic,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$ModifierImplCopyWith<$Res>
    implements $ModifierCopyWith<$Res> {
  factory _$$ModifierImplCopyWith(
          _$ModifierImpl value, $Res Function(_$ModifierImpl) then) =
      __$$ModifierImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String name, dynamic data});
}

/// @nodoc
class __$$ModifierImplCopyWithImpl<$Res>
    extends _$ModifierCopyWithImpl<$Res, _$ModifierImpl>
    implements _$$ModifierImplCopyWith<$Res> {
  __$$ModifierImplCopyWithImpl(
      _$ModifierImpl _value, $Res Function(_$ModifierImpl) _then)
      : super(_value, _then);

  /// Create a copy of Modifier
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? data = freezed,
  }) {
    return _then(_$ModifierImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      data: freezed == data
          ? _value.data
          : data // ignore: cast_nullable_to_non_nullable
              as dynamic,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ModifierImpl with DiagnosticableTreeMixin implements _Modifier {
  const _$ModifierImpl({required this.name, this.data});

  factory _$ModifierImpl.fromJson(Map<String, dynamic> json) =>
      _$$ModifierImplFromJson(json);

  @override
  final String name;
  @override
  final dynamic data;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'Modifier(name: $name, data: $data)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'Modifier'))
      ..add(DiagnosticsProperty('name', name))
      ..add(DiagnosticsProperty('data', data));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ModifierImpl &&
            (identical(other.name, name) || other.name == name) &&
            const DeepCollectionEquality().equals(other.data, data));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode =>
      Object.hash(runtimeType, name, const DeepCollectionEquality().hash(data));

  /// Create a copy of Modifier
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$ModifierImplCopyWith<_$ModifierImpl> get copyWith =>
      __$$ModifierImplCopyWithImpl<_$ModifierImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$ModifierImplToJson(
      this,
    );
  }
}

abstract class _Modifier implements Modifier {
  const factory _Modifier({required final String name, final dynamic data}) =
      _$ModifierImpl;

  factory _Modifier.fromJson(Map<String, dynamic> json) =
      _$ModifierImpl.fromJson;

  @override
  String get name;
  @override
  dynamic get data;

  /// Create a copy of Modifier
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$ModifierImplCopyWith<_$ModifierImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
