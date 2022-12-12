// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target

part of 'adapter.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

Adapter _$AdapterFromJson(Map<String, dynamic> json) {
  return _Adapter.fromJson(json);
}

/// @nodoc
mixin _$Adapter {
  String get name => throw _privateConstructorUsedError;
  String get description => throw _privateConstructorUsedError;
  String get version => throw _privateConstructorUsedError;
  List<EntryBlueprint> get entries => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdapterCopyWith<Adapter> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdapterCopyWith<$Res> {
  factory $AdapterCopyWith(Adapter value, $Res Function(Adapter) then) =
      _$AdapterCopyWithImpl<$Res, Adapter>;
  @useResult
  $Res call(
      {String name,
      String description,
      String version,
      List<EntryBlueprint> entries});
}

/// @nodoc
class _$AdapterCopyWithImpl<$Res, $Val extends Adapter>
    implements $AdapterCopyWith<$Res> {
  _$AdapterCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
    Object? version = null,
    Object? entries = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      version: null == version
          ? _value.version
          : version // ignore: cast_nullable_to_non_nullable
              as String,
      entries: null == entries
          ? _value.entries
          : entries // ignore: cast_nullable_to_non_nullable
              as List<EntryBlueprint>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_AdapterCopyWith<$Res> implements $AdapterCopyWith<$Res> {
  factory _$$_AdapterCopyWith(
          _$_Adapter value, $Res Function(_$_Adapter) then) =
      __$$_AdapterCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String name,
      String description,
      String version,
      List<EntryBlueprint> entries});
}

/// @nodoc
class __$$_AdapterCopyWithImpl<$Res>
    extends _$AdapterCopyWithImpl<$Res, _$_Adapter>
    implements _$$_AdapterCopyWith<$Res> {
  __$$_AdapterCopyWithImpl(_$_Adapter _value, $Res Function(_$_Adapter) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
    Object? version = null,
    Object? entries = null,
  }) {
    return _then(_$_Adapter(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      version: null == version
          ? _value.version
          : version // ignore: cast_nullable_to_non_nullable
              as String,
      entries: null == entries
          ? _value._entries
          : entries // ignore: cast_nullable_to_non_nullable
              as List<EntryBlueprint>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$_Adapter implements _Adapter {
  const _$_Adapter(
      {required this.name,
      required this.description,
      required this.version,
      required final List<EntryBlueprint> entries})
      : _entries = entries;

  factory _$_Adapter.fromJson(Map<String, dynamic> json) =>
      _$$_AdapterFromJson(json);

  @override
  final String name;
  @override
  final String description;
  @override
  final String version;
  final List<EntryBlueprint> _entries;
  @override
  List<EntryBlueprint> get entries {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_entries);
  }

  @override
  String toString() {
    return 'Adapter(name: $name, description: $description, version: $version, entries: $entries)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_Adapter &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.version, version) || other.version == version) &&
            const DeepCollectionEquality().equals(other._entries, _entries));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, name, description, version,
      const DeepCollectionEquality().hash(_entries));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_AdapterCopyWith<_$_Adapter> get copyWith =>
      __$$_AdapterCopyWithImpl<_$_Adapter>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$_AdapterToJson(
      this,
    );
  }
}

abstract class _Adapter implements Adapter {
  const factory _Adapter(
      {required final String name,
      required final String description,
      required final String version,
      required final List<EntryBlueprint> entries}) = _$_Adapter;

  factory _Adapter.fromJson(Map<String, dynamic> json) = _$_Adapter.fromJson;

  @override
  String get name;
  @override
  String get description;
  @override
  String get version;
  @override
  List<EntryBlueprint> get entries;
  @override
  @JsonKey(ignore: true)
  _$$_AdapterCopyWith<_$_Adapter> get copyWith =>
      throw _privateConstructorUsedError;
}

EntryBlueprint _$EntryBlueprintFromJson(Map<String, dynamic> json) {
  return _EntryBlueprint.fromJson(json);
}

/// @nodoc
mixin _$EntryBlueprint {
  String get name => throw _privateConstructorUsedError;
  String get description => throw _privateConstructorUsedError;
  ObjectField get fields => throw _privateConstructorUsedError;
  List<String> get tags => throw _privateConstructorUsedError;
  @ColorConverter()
  Color get color => throw _privateConstructorUsedError;
  @IconConverter()
  IconData get icon => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
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
      {String name,
      String description,
      ObjectField fields,
      List<String> tags,
      @ColorConverter() Color color,
      @IconConverter() IconData icon});
}

/// @nodoc
class _$EntryBlueprintCopyWithImpl<$Res, $Val extends EntryBlueprint>
    implements $EntryBlueprintCopyWith<$Res> {
  _$EntryBlueprintCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
    Object? fields = null,
    Object? tags = null,
    Object? color = null,
    Object? icon = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      fields: null == fields
          ? _value.fields
          : fields // ignore: cast_nullable_to_non_nullable
              as ObjectField,
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
              as IconData,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_EntryBlueprintCopyWith<$Res>
    implements $EntryBlueprintCopyWith<$Res> {
  factory _$$_EntryBlueprintCopyWith(
          _$_EntryBlueprint value, $Res Function(_$_EntryBlueprint) then) =
      __$$_EntryBlueprintCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String name,
      String description,
      ObjectField fields,
      List<String> tags,
      @ColorConverter() Color color,
      @IconConverter() IconData icon});
}

/// @nodoc
class __$$_EntryBlueprintCopyWithImpl<$Res>
    extends _$EntryBlueprintCopyWithImpl<$Res, _$_EntryBlueprint>
    implements _$$_EntryBlueprintCopyWith<$Res> {
  __$$_EntryBlueprintCopyWithImpl(
      _$_EntryBlueprint _value, $Res Function(_$_EntryBlueprint) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
    Object? fields = null,
    Object? tags = null,
    Object? color = null,
    Object? icon = null,
  }) {
    return _then(_$_EntryBlueprint(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      fields: null == fields
          ? _value.fields
          : fields // ignore: cast_nullable_to_non_nullable
              as ObjectField,
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
              as IconData,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$_EntryBlueprint implements _EntryBlueprint {
  const _$_EntryBlueprint(
      {required this.name,
      required this.description,
      required this.fields,
      final List<String> tags = const <String>[],
      @ColorConverter() this.color = Colors.grey,
      @IconConverter() this.icon = Icons.help})
      : _tags = tags;

  factory _$_EntryBlueprint.fromJson(Map<String, dynamic> json) =>
      _$$_EntryBlueprintFromJson(json);

  @override
  final String name;
  @override
  final String description;
  @override
  final ObjectField fields;
  final List<String> _tags;
  @override
  @JsonKey()
  List<String> get tags {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_tags);
  }

  @override
  @JsonKey()
  @ColorConverter()
  final Color color;
  @override
  @JsonKey()
  @IconConverter()
  final IconData icon;

  @override
  String toString() {
    return 'EntryBlueprint(name: $name, description: $description, fields: $fields, tags: $tags, color: $color, icon: $icon)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_EntryBlueprint &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.fields, fields) || other.fields == fields) &&
            const DeepCollectionEquality().equals(other._tags, _tags) &&
            (identical(other.color, color) || other.color == color) &&
            (identical(other.icon, icon) || other.icon == icon));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, name, description, fields,
      const DeepCollectionEquality().hash(_tags), color, icon);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_EntryBlueprintCopyWith<_$_EntryBlueprint> get copyWith =>
      __$$_EntryBlueprintCopyWithImpl<_$_EntryBlueprint>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$_EntryBlueprintToJson(
      this,
    );
  }
}

abstract class _EntryBlueprint implements EntryBlueprint {
  const factory _EntryBlueprint(
      {required final String name,
      required final String description,
      required final ObjectField fields,
      final List<String> tags,
      @ColorConverter() final Color color,
      @IconConverter() final IconData icon}) = _$_EntryBlueprint;

  factory _EntryBlueprint.fromJson(Map<String, dynamic> json) =
      _$_EntryBlueprint.fromJson;

  @override
  String get name;
  @override
  String get description;
  @override
  ObjectField get fields;
  @override
  List<String> get tags;
  @override
  @ColorConverter()
  Color get color;
  @override
  @IconConverter()
  IconData get icon;
  @override
  @JsonKey(ignore: true)
  _$$_EntryBlueprintCopyWith<_$_EntryBlueprint> get copyWith =>
      throw _privateConstructorUsedError;
}

FieldInfo _$FieldInfoFromJson(Map<String, dynamic> json) {
  switch (json['kind']) {
    case 'default':
      return _FieldType.fromJson(json);
    case 'primitive':
      return PrimitiveField.fromJson(json);
    case 'enum':
      return EnumField.fromJson(json);
    case 'list':
      return ListField.fromJson(json);
    case 'map':
      return MapField.fromJson(json);
    case 'object':
      return ObjectField.fromJson(json);

    default:
      throw CheckedFromJsonException(
          json, 'kind', 'FieldInfo', 'Invalid union type "${json['kind']}"!');
  }
}

/// @nodoc
mixin _$FieldInfo {
  List<Modifier> get modifiers => throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers) $default, {
    required TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)
        primitive,
    required TResult Function(List<String> values, List<Modifier> modifiers)
        enumField,
    required TResult Function(FieldInfo type, List<Modifier> modifiers) list,
    required TResult Function(
            FieldInfo key, FieldInfo value, List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, FieldInfo> fields, List<Modifier> modifiers)
        object,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Modifier> modifiers)? $default, {
    TResult? Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult? Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult? Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult? Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult? Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers)? $default, {
    TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FieldType value) $default, {
    required TResult Function(PrimitiveField value) primitive,
    required TResult Function(EnumField value) enumField,
    required TResult Function(ListField value) list,
    required TResult Function(MapField value) map,
    required TResult Function(ObjectField value) object,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FieldType value)? $default, {
    TResult? Function(PrimitiveField value)? primitive,
    TResult? Function(EnumField value)? enumField,
    TResult? Function(ListField value)? list,
    TResult? Function(MapField value)? map,
    TResult? Function(ObjectField value)? object,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FieldType value)? $default, {
    TResult Function(PrimitiveField value)? primitive,
    TResult Function(EnumField value)? enumField,
    TResult Function(ListField value)? list,
    TResult Function(MapField value)? map,
    TResult Function(ObjectField value)? object,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $FieldInfoCopyWith<FieldInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FieldInfoCopyWith<$Res> {
  factory $FieldInfoCopyWith(FieldInfo value, $Res Function(FieldInfo) then) =
      _$FieldInfoCopyWithImpl<$Res, FieldInfo>;
  @useResult
  $Res call({List<Modifier> modifiers});
}

/// @nodoc
class _$FieldInfoCopyWithImpl<$Res, $Val extends FieldInfo>
    implements $FieldInfoCopyWith<$Res> {
  _$FieldInfoCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? modifiers = null,
  }) {
    return _then(_value.copyWith(
      modifiers: null == modifiers
          ? _value.modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_FieldTypeCopyWith<$Res> implements $FieldInfoCopyWith<$Res> {
  factory _$$_FieldTypeCopyWith(
          _$_FieldType value, $Res Function(_$_FieldType) then) =
      __$$_FieldTypeCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({List<Modifier> modifiers});
}

/// @nodoc
class __$$_FieldTypeCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$_FieldType>
    implements _$$_FieldTypeCopyWith<$Res> {
  __$$_FieldTypeCopyWithImpl(
      _$_FieldType _value, $Res Function(_$_FieldType) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? modifiers = null,
  }) {
    return _then(_$_FieldType(
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$_FieldType implements _FieldType {
  const _$_FieldType(
      {final List<Modifier> modifiers = const [], final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'default';

  factory _$_FieldType.fromJson(Map<String, dynamic> json) =>
      _$$_FieldTypeFromJson(json);

  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldInfo(modifiers: $modifiers)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_FieldType &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_modifiers));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_FieldTypeCopyWith<_$_FieldType> get copyWith =>
      __$$_FieldTypeCopyWithImpl<_$_FieldType>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers) $default, {
    required TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)
        primitive,
    required TResult Function(List<String> values, List<Modifier> modifiers)
        enumField,
    required TResult Function(FieldInfo type, List<Modifier> modifiers) list,
    required TResult Function(
            FieldInfo key, FieldInfo value, List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, FieldInfo> fields, List<Modifier> modifiers)
        object,
  }) {
    return $default(modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Modifier> modifiers)? $default, {
    TResult? Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult? Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult? Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult? Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult? Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
  }) {
    return $default?.call(modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers)? $default, {
    TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FieldType value) $default, {
    required TResult Function(PrimitiveField value) primitive,
    required TResult Function(EnumField value) enumField,
    required TResult Function(ListField value) list,
    required TResult Function(MapField value) map,
    required TResult Function(ObjectField value) object,
  }) {
    return $default(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FieldType value)? $default, {
    TResult? Function(PrimitiveField value)? primitive,
    TResult? Function(EnumField value)? enumField,
    TResult? Function(ListField value)? list,
    TResult? Function(MapField value)? map,
    TResult? Function(ObjectField value)? object,
  }) {
    return $default?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FieldType value)? $default, {
    TResult Function(PrimitiveField value)? primitive,
    TResult Function(EnumField value)? enumField,
    TResult Function(ListField value)? list,
    TResult Function(MapField value)? map,
    TResult Function(ObjectField value)? object,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$_FieldTypeToJson(
      this,
    );
  }
}

abstract class _FieldType implements FieldInfo {
  const factory _FieldType({final List<Modifier> modifiers}) = _$_FieldType;

  factory _FieldType.fromJson(Map<String, dynamic> json) =
      _$_FieldType.fromJson;

  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$_FieldTypeCopyWith<_$_FieldType> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$PrimitiveFieldCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$PrimitiveFieldCopyWith(
          _$PrimitiveField value, $Res Function(_$PrimitiveField) then) =
      __$$PrimitiveFieldCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({PrimitiveFieldType type, List<Modifier> modifiers});
}

/// @nodoc
class __$$PrimitiveFieldCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$PrimitiveField>
    implements _$$PrimitiveFieldCopyWith<$Res> {
  __$$PrimitiveFieldCopyWithImpl(
      _$PrimitiveField _value, $Res Function(_$PrimitiveField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? modifiers = null,
  }) {
    return _then(_$PrimitiveField(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as PrimitiveFieldType,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$PrimitiveField implements PrimitiveField {
  const _$PrimitiveField(
      {required this.type,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'primitive';

  factory _$PrimitiveField.fromJson(Map<String, dynamic> json) =>
      _$$PrimitiveFieldFromJson(json);

  @override
  final PrimitiveFieldType type;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldInfo.primitive(type: $type, modifiers: $modifiers)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$PrimitiveField &&
            (identical(other.type, type) || other.type == type) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType, type, const DeepCollectionEquality().hash(_modifiers));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$PrimitiveFieldCopyWith<_$PrimitiveField> get copyWith =>
      __$$PrimitiveFieldCopyWithImpl<_$PrimitiveField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers) $default, {
    required TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)
        primitive,
    required TResult Function(List<String> values, List<Modifier> modifiers)
        enumField,
    required TResult Function(FieldInfo type, List<Modifier> modifiers) list,
    required TResult Function(
            FieldInfo key, FieldInfo value, List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, FieldInfo> fields, List<Modifier> modifiers)
        object,
  }) {
    return primitive(type, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Modifier> modifiers)? $default, {
    TResult? Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult? Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult? Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult? Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult? Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
  }) {
    return primitive?.call(type, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers)? $default, {
    TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
    required TResult orElse(),
  }) {
    if (primitive != null) {
      return primitive(type, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FieldType value) $default, {
    required TResult Function(PrimitiveField value) primitive,
    required TResult Function(EnumField value) enumField,
    required TResult Function(ListField value) list,
    required TResult Function(MapField value) map,
    required TResult Function(ObjectField value) object,
  }) {
    return primitive(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FieldType value)? $default, {
    TResult? Function(PrimitiveField value)? primitive,
    TResult? Function(EnumField value)? enumField,
    TResult? Function(ListField value)? list,
    TResult? Function(MapField value)? map,
    TResult? Function(ObjectField value)? object,
  }) {
    return primitive?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FieldType value)? $default, {
    TResult Function(PrimitiveField value)? primitive,
    TResult Function(EnumField value)? enumField,
    TResult Function(ListField value)? list,
    TResult Function(MapField value)? map,
    TResult Function(ObjectField value)? object,
    required TResult orElse(),
  }) {
    if (primitive != null) {
      return primitive(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$PrimitiveFieldToJson(
      this,
    );
  }
}

abstract class PrimitiveField implements FieldInfo {
  const factory PrimitiveField(
      {required final PrimitiveFieldType type,
      final List<Modifier> modifiers}) = _$PrimitiveField;

  factory PrimitiveField.fromJson(Map<String, dynamic> json) =
      _$PrimitiveField.fromJson;

  PrimitiveFieldType get type;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$PrimitiveFieldCopyWith<_$PrimitiveField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$EnumFieldCopyWith<$Res> implements $FieldInfoCopyWith<$Res> {
  factory _$$EnumFieldCopyWith(
          _$EnumField value, $Res Function(_$EnumField) then) =
      __$$EnumFieldCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({List<String> values, List<Modifier> modifiers});
}

/// @nodoc
class __$$EnumFieldCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$EnumField>
    implements _$$EnumFieldCopyWith<$Res> {
  __$$EnumFieldCopyWithImpl(
      _$EnumField _value, $Res Function(_$EnumField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? values = null,
    Object? modifiers = null,
  }) {
    return _then(_$EnumField(
      values: null == values
          ? _value._values
          : values // ignore: cast_nullable_to_non_nullable
              as List<String>,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$EnumField implements EnumField {
  const _$EnumField(
      {required final List<String> values,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _values = values,
        _modifiers = modifiers,
        $type = $type ?? 'enum';

  factory _$EnumField.fromJson(Map<String, dynamic> json) =>
      _$$EnumFieldFromJson(json);

  final List<String> _values;
  @override
  List<String> get values {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_values);
  }

  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldInfo.enumField(values: $values, modifiers: $modifiers)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EnumField &&
            const DeepCollectionEquality().equals(other._values, _values) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_values),
      const DeepCollectionEquality().hash(_modifiers));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$EnumFieldCopyWith<_$EnumField> get copyWith =>
      __$$EnumFieldCopyWithImpl<_$EnumField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers) $default, {
    required TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)
        primitive,
    required TResult Function(List<String> values, List<Modifier> modifiers)
        enumField,
    required TResult Function(FieldInfo type, List<Modifier> modifiers) list,
    required TResult Function(
            FieldInfo key, FieldInfo value, List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, FieldInfo> fields, List<Modifier> modifiers)
        object,
  }) {
    return enumField(values, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Modifier> modifiers)? $default, {
    TResult? Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult? Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult? Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult? Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult? Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
  }) {
    return enumField?.call(values, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers)? $default, {
    TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
    required TResult orElse(),
  }) {
    if (enumField != null) {
      return enumField(values, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FieldType value) $default, {
    required TResult Function(PrimitiveField value) primitive,
    required TResult Function(EnumField value) enumField,
    required TResult Function(ListField value) list,
    required TResult Function(MapField value) map,
    required TResult Function(ObjectField value) object,
  }) {
    return enumField(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FieldType value)? $default, {
    TResult? Function(PrimitiveField value)? primitive,
    TResult? Function(EnumField value)? enumField,
    TResult? Function(ListField value)? list,
    TResult? Function(MapField value)? map,
    TResult? Function(ObjectField value)? object,
  }) {
    return enumField?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FieldType value)? $default, {
    TResult Function(PrimitiveField value)? primitive,
    TResult Function(EnumField value)? enumField,
    TResult Function(ListField value)? list,
    TResult Function(MapField value)? map,
    TResult Function(ObjectField value)? object,
    required TResult orElse(),
  }) {
    if (enumField != null) {
      return enumField(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$EnumFieldToJson(
      this,
    );
  }
}

abstract class EnumField implements FieldInfo {
  const factory EnumField(
      {required final List<String> values,
      final List<Modifier> modifiers}) = _$EnumField;

  factory EnumField.fromJson(Map<String, dynamic> json) = _$EnumField.fromJson;

  List<String> get values;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$EnumFieldCopyWith<_$EnumField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ListFieldCopyWith<$Res> implements $FieldInfoCopyWith<$Res> {
  factory _$$ListFieldCopyWith(
          _$ListField value, $Res Function(_$ListField) then) =
      __$$ListFieldCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({FieldInfo type, List<Modifier> modifiers});

  $FieldInfoCopyWith<$Res> get type;
}

/// @nodoc
class __$$ListFieldCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$ListField>
    implements _$$ListFieldCopyWith<$Res> {
  __$$ListFieldCopyWithImpl(
      _$ListField _value, $Res Function(_$ListField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? modifiers = null,
  }) {
    return _then(_$ListField(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as FieldInfo,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $FieldInfoCopyWith<$Res> get type {
    return $FieldInfoCopyWith<$Res>(_value.type, (value) {
      return _then(_value.copyWith(type: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$ListField implements ListField {
  const _$ListField(
      {required this.type,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'list';

  factory _$ListField.fromJson(Map<String, dynamic> json) =>
      _$$ListFieldFromJson(json);

  @override
  final FieldInfo type;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldInfo.list(type: $type, modifiers: $modifiers)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ListField &&
            (identical(other.type, type) || other.type == type) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType, type, const DeepCollectionEquality().hash(_modifiers));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ListFieldCopyWith<_$ListField> get copyWith =>
      __$$ListFieldCopyWithImpl<_$ListField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers) $default, {
    required TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)
        primitive,
    required TResult Function(List<String> values, List<Modifier> modifiers)
        enumField,
    required TResult Function(FieldInfo type, List<Modifier> modifiers) list,
    required TResult Function(
            FieldInfo key, FieldInfo value, List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, FieldInfo> fields, List<Modifier> modifiers)
        object,
  }) {
    return list(type, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Modifier> modifiers)? $default, {
    TResult? Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult? Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult? Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult? Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult? Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
  }) {
    return list?.call(type, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers)? $default, {
    TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
    required TResult orElse(),
  }) {
    if (list != null) {
      return list(type, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FieldType value) $default, {
    required TResult Function(PrimitiveField value) primitive,
    required TResult Function(EnumField value) enumField,
    required TResult Function(ListField value) list,
    required TResult Function(MapField value) map,
    required TResult Function(ObjectField value) object,
  }) {
    return list(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FieldType value)? $default, {
    TResult? Function(PrimitiveField value)? primitive,
    TResult? Function(EnumField value)? enumField,
    TResult? Function(ListField value)? list,
    TResult? Function(MapField value)? map,
    TResult? Function(ObjectField value)? object,
  }) {
    return list?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FieldType value)? $default, {
    TResult Function(PrimitiveField value)? primitive,
    TResult Function(EnumField value)? enumField,
    TResult Function(ListField value)? list,
    TResult Function(MapField value)? map,
    TResult Function(ObjectField value)? object,
    required TResult orElse(),
  }) {
    if (list != null) {
      return list(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$ListFieldToJson(
      this,
    );
  }
}

abstract class ListField implements FieldInfo {
  const factory ListField(
      {required final FieldInfo type,
      final List<Modifier> modifiers}) = _$ListField;

  factory ListField.fromJson(Map<String, dynamic> json) = _$ListField.fromJson;

  FieldInfo get type;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$ListFieldCopyWith<_$ListField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$MapFieldCopyWith<$Res> implements $FieldInfoCopyWith<$Res> {
  factory _$$MapFieldCopyWith(
          _$MapField value, $Res Function(_$MapField) then) =
      __$$MapFieldCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({FieldInfo key, FieldInfo value, List<Modifier> modifiers});

  $FieldInfoCopyWith<$Res> get key;
  $FieldInfoCopyWith<$Res> get value;
}

/// @nodoc
class __$$MapFieldCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$MapField>
    implements _$$MapFieldCopyWith<$Res> {
  __$$MapFieldCopyWithImpl(_$MapField _value, $Res Function(_$MapField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
    Object? modifiers = null,
  }) {
    return _then(_$MapField(
      key: null == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as FieldInfo,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as FieldInfo,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $FieldInfoCopyWith<$Res> get key {
    return $FieldInfoCopyWith<$Res>(_value.key, (value) {
      return _then(_value.copyWith(key: value));
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $FieldInfoCopyWith<$Res> get value {
    return $FieldInfoCopyWith<$Res>(_value.value, (value) {
      return _then(_value.copyWith(value: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$MapField implements MapField {
  const _$MapField(
      {required this.key,
      required this.value,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'map';

  factory _$MapField.fromJson(Map<String, dynamic> json) =>
      _$$MapFieldFromJson(json);

  @override
  final FieldInfo key;
  @override
  final FieldInfo value;
  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldInfo.map(key: $key, value: $value, modifiers: $modifiers)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MapField &&
            (identical(other.key, key) || other.key == key) &&
            (identical(other.value, value) || other.value == value) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType, key, value, const DeepCollectionEquality().hash(_modifiers));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$MapFieldCopyWith<_$MapField> get copyWith =>
      __$$MapFieldCopyWithImpl<_$MapField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers) $default, {
    required TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)
        primitive,
    required TResult Function(List<String> values, List<Modifier> modifiers)
        enumField,
    required TResult Function(FieldInfo type, List<Modifier> modifiers) list,
    required TResult Function(
            FieldInfo key, FieldInfo value, List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, FieldInfo> fields, List<Modifier> modifiers)
        object,
  }) {
    return map(key, value, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Modifier> modifiers)? $default, {
    TResult? Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult? Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult? Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult? Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult? Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
  }) {
    return map?.call(key, value, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers)? $default, {
    TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
    required TResult orElse(),
  }) {
    if (map != null) {
      return map(key, value, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FieldType value) $default, {
    required TResult Function(PrimitiveField value) primitive,
    required TResult Function(EnumField value) enumField,
    required TResult Function(ListField value) list,
    required TResult Function(MapField value) map,
    required TResult Function(ObjectField value) object,
  }) {
    return map(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FieldType value)? $default, {
    TResult? Function(PrimitiveField value)? primitive,
    TResult? Function(EnumField value)? enumField,
    TResult? Function(ListField value)? list,
    TResult? Function(MapField value)? map,
    TResult? Function(ObjectField value)? object,
  }) {
    return map?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FieldType value)? $default, {
    TResult Function(PrimitiveField value)? primitive,
    TResult Function(EnumField value)? enumField,
    TResult Function(ListField value)? list,
    TResult Function(MapField value)? map,
    TResult Function(ObjectField value)? object,
    required TResult orElse(),
  }) {
    if (map != null) {
      return map(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$MapFieldToJson(
      this,
    );
  }
}

abstract class MapField implements FieldInfo {
  const factory MapField(
      {required final FieldInfo key,
      required final FieldInfo value,
      final List<Modifier> modifiers}) = _$MapField;

  factory MapField.fromJson(Map<String, dynamic> json) = _$MapField.fromJson;

  FieldInfo get key;
  FieldInfo get value;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$MapFieldCopyWith<_$MapField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ObjectFieldCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$ObjectFieldCopyWith(
          _$ObjectField value, $Res Function(_$ObjectField) then) =
      __$$ObjectFieldCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({Map<String, FieldInfo> fields, List<Modifier> modifiers});
}

/// @nodoc
class __$$ObjectFieldCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$ObjectField>
    implements _$$ObjectFieldCopyWith<$Res> {
  __$$ObjectFieldCopyWithImpl(
      _$ObjectField _value, $Res Function(_$ObjectField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fields = null,
    Object? modifiers = null,
  }) {
    return _then(_$ObjectField(
      fields: null == fields
          ? _value._fields
          : fields // ignore: cast_nullable_to_non_nullable
              as Map<String, FieldInfo>,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ObjectField implements ObjectField {
  const _$ObjectField(
      {required final Map<String, FieldInfo> fields,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _fields = fields,
        _modifiers = modifiers,
        $type = $type ?? 'object';

  factory _$ObjectField.fromJson(Map<String, dynamic> json) =>
      _$$ObjectFieldFromJson(json);

  final Map<String, FieldInfo> _fields;
  @override
  Map<String, FieldInfo> get fields {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_fields);
  }

  final List<Modifier> _modifiers;
  @override
  @JsonKey()
  List<Modifier> get modifiers {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_modifiers);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldInfo.object(fields: $fields, modifiers: $modifiers)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ObjectField &&
            const DeepCollectionEquality().equals(other._fields, _fields) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_fields),
      const DeepCollectionEquality().hash(_modifiers));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ObjectFieldCopyWith<_$ObjectField> get copyWith =>
      __$$ObjectFieldCopyWithImpl<_$ObjectField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers) $default, {
    required TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)
        primitive,
    required TResult Function(List<String> values, List<Modifier> modifiers)
        enumField,
    required TResult Function(FieldInfo type, List<Modifier> modifiers) list,
    required TResult Function(
            FieldInfo key, FieldInfo value, List<Modifier> modifiers)
        map,
    required TResult Function(
            Map<String, FieldInfo> fields, List<Modifier> modifiers)
        object,
  }) {
    return object(fields, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Modifier> modifiers)? $default, {
    TResult? Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult? Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult? Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult? Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult? Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
  }) {
    return object?.call(fields, modifiers);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Modifier> modifiers)? $default, {
    TResult Function(PrimitiveFieldType type, List<Modifier> modifiers)?
        primitive,
    TResult Function(List<String> values, List<Modifier> modifiers)? enumField,
    TResult Function(FieldInfo type, List<Modifier> modifiers)? list,
    TResult Function(FieldInfo key, FieldInfo value, List<Modifier> modifiers)?
        map,
    TResult Function(Map<String, FieldInfo> fields, List<Modifier> modifiers)?
        object,
    required TResult orElse(),
  }) {
    if (object != null) {
      return object(fields, modifiers);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FieldType value) $default, {
    required TResult Function(PrimitiveField value) primitive,
    required TResult Function(EnumField value) enumField,
    required TResult Function(ListField value) list,
    required TResult Function(MapField value) map,
    required TResult Function(ObjectField value) object,
  }) {
    return object(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FieldType value)? $default, {
    TResult? Function(PrimitiveField value)? primitive,
    TResult? Function(EnumField value)? enumField,
    TResult? Function(ListField value)? list,
    TResult? Function(MapField value)? map,
    TResult? Function(ObjectField value)? object,
  }) {
    return object?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FieldType value)? $default, {
    TResult Function(PrimitiveField value)? primitive,
    TResult Function(EnumField value)? enumField,
    TResult Function(ListField value)? list,
    TResult Function(MapField value)? map,
    TResult Function(ObjectField value)? object,
    required TResult orElse(),
  }) {
    if (object != null) {
      return object(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$ObjectFieldToJson(
      this,
    );
  }
}

abstract class ObjectField implements FieldInfo {
  const factory ObjectField(
      {required final Map<String, FieldInfo> fields,
      final List<Modifier> modifiers}) = _$ObjectField;

  factory ObjectField.fromJson(Map<String, dynamic> json) =
      _$ObjectField.fromJson;

  Map<String, FieldInfo> get fields;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$ObjectFieldCopyWith<_$ObjectField> get copyWith =>
      throw _privateConstructorUsedError;
}

Modifier _$ModifierFromJson(Map<String, dynamic> json) {
  return _Modifier.fromJson(json);
}

/// @nodoc
mixin _$Modifier {
  String get name => throw _privateConstructorUsedError;
  dynamic get data => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
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

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? data = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      data: null == data
          ? _value.data
          : data // ignore: cast_nullable_to_non_nullable
              as dynamic,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$_ModifierCopyWith<$Res> implements $ModifierCopyWith<$Res> {
  factory _$$_ModifierCopyWith(
          _$_Modifier value, $Res Function(_$_Modifier) then) =
      __$$_ModifierCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String name, dynamic data});
}

/// @nodoc
class __$$_ModifierCopyWithImpl<$Res>
    extends _$ModifierCopyWithImpl<$Res, _$_Modifier>
    implements _$$_ModifierCopyWith<$Res> {
  __$$_ModifierCopyWithImpl(
      _$_Modifier _value, $Res Function(_$_Modifier) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? data = null,
  }) {
    return _then(_$_Modifier(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      data: null == data
          ? _value.data
          : data // ignore: cast_nullable_to_non_nullable
              as dynamic,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$_Modifier implements _Modifier {
  const _$_Modifier({required this.name, this.data});

  factory _$_Modifier.fromJson(Map<String, dynamic> json) =>
      _$$_ModifierFromJson(json);

  @override
  final String name;
  @override
  final dynamic data;

  @override
  String toString() {
    return 'Modifier(name: $name, data: $data)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$_Modifier &&
            (identical(other.name, name) || other.name == name) &&
            const DeepCollectionEquality().equals(other.data, data));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, name, const DeepCollectionEquality().hash(data));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$_ModifierCopyWith<_$_Modifier> get copyWith =>
      __$$_ModifierCopyWithImpl<_$_Modifier>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$_ModifierToJson(
      this,
    );
  }
}

abstract class _Modifier implements Modifier {
  const factory _Modifier({required final String name, final dynamic data}) =
      _$_Modifier;

  factory _Modifier.fromJson(Map<String, dynamic> json) = _$_Modifier.fromJson;

  @override
  String get name;
  @override
  dynamic get data;
  @override
  @JsonKey(ignore: true)
  _$$_ModifierCopyWith<_$_Modifier> get copyWith =>
      throw _privateConstructorUsedError;
}
