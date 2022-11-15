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
  @ColorConverter()
  Color get color => throw _privateConstructorUsedError;

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
      @ColorConverter() Color color});
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
    Object? color = null,
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
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
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
      @ColorConverter() Color color});
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
    Object? color = null,
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
      color: null == color
          ? _value.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
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
      @ColorConverter() this.color = Colors.grey});

  factory _$_EntryBlueprint.fromJson(Map<String, dynamic> json) =>
      _$$_EntryBlueprintFromJson(json);

  @override
  final String name;
  @override
  final String description;
  @override
  final ObjectField fields;
  @override
  @JsonKey()
  @ColorConverter()
  final Color color;

  @override
  String toString() {
    return 'EntryBlueprint(name: $name, description: $description, fields: $fields, color: $color)';
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
            (identical(other.color, color) || other.color == color));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, name, description, fields, color);

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
      @ColorConverter() final Color color}) = _$_EntryBlueprint;

  factory _EntryBlueprint.fromJson(Map<String, dynamic> json) =
      _$_EntryBlueprint.fromJson;

  @override
  String get name;
  @override
  String get description;
  @override
  ObjectField get fields;
  @override
  @ColorConverter()
  Color get color;
  @override
  @JsonKey(ignore: true)
  _$$_EntryBlueprintCopyWith<_$_EntryBlueprint> get copyWith =>
      throw _privateConstructorUsedError;
}

FieldType _$FieldTypeFromJson(Map<String, dynamic> json) {
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
          json, 'kind', 'FieldType', 'Invalid union type "${json['kind']}"!');
  }
}

/// @nodoc
mixin _$FieldType {
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function() $default, {
    required TResult Function(PrimitiveFieldType type) primitive,
    required TResult Function(List<String> values) enumField,
    required TResult Function(FieldType type) list,
    required TResult Function(FieldType key, FieldType value) map,
    required TResult Function(Map<String, FieldType> fields) object,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function()? $default, {
    TResult? Function(PrimitiveFieldType type)? primitive,
    TResult? Function(List<String> values)? enumField,
    TResult? Function(FieldType type)? list,
    TResult? Function(FieldType key, FieldType value)? map,
    TResult? Function(Map<String, FieldType> fields)? object,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function()? $default, {
    TResult Function(PrimitiveFieldType type)? primitive,
    TResult Function(List<String> values)? enumField,
    TResult Function(FieldType type)? list,
    TResult Function(FieldType key, FieldType value)? map,
    TResult Function(Map<String, FieldType> fields)? object,
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
}

/// @nodoc
abstract class $FieldTypeCopyWith<$Res> {
  factory $FieldTypeCopyWith(FieldType value, $Res Function(FieldType) then) =
      _$FieldTypeCopyWithImpl<$Res, FieldType>;
}

/// @nodoc
class _$FieldTypeCopyWithImpl<$Res, $Val extends FieldType>
    implements $FieldTypeCopyWith<$Res> {
  _$FieldTypeCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$_FieldTypeCopyWith<$Res> {
  factory _$$_FieldTypeCopyWith(
          _$_FieldType value, $Res Function(_$_FieldType) then) =
      __$$_FieldTypeCopyWithImpl<$Res>;
}

/// @nodoc
class __$$_FieldTypeCopyWithImpl<$Res>
    extends _$FieldTypeCopyWithImpl<$Res, _$_FieldType>
    implements _$$_FieldTypeCopyWith<$Res> {
  __$$_FieldTypeCopyWithImpl(
      _$_FieldType _value, $Res Function(_$_FieldType) _then)
      : super(_value, _then);
}

/// @nodoc
@JsonSerializable()
class _$_FieldType implements _FieldType {
  const _$_FieldType({final String? $type}) : $type = $type ?? 'default';

  factory _$_FieldType.fromJson(Map<String, dynamic> json) =>
      _$$_FieldTypeFromJson(json);

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldType()';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$_FieldType);
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function() $default, {
    required TResult Function(PrimitiveFieldType type) primitive,
    required TResult Function(List<String> values) enumField,
    required TResult Function(FieldType type) list,
    required TResult Function(FieldType key, FieldType value) map,
    required TResult Function(Map<String, FieldType> fields) object,
  }) {
    return $default();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function()? $default, {
    TResult? Function(PrimitiveFieldType type)? primitive,
    TResult? Function(List<String> values)? enumField,
    TResult? Function(FieldType type)? list,
    TResult? Function(FieldType key, FieldType value)? map,
    TResult? Function(Map<String, FieldType> fields)? object,
  }) {
    return $default?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function()? $default, {
    TResult Function(PrimitiveFieldType type)? primitive,
    TResult Function(List<String> values)? enumField,
    TResult Function(FieldType type)? list,
    TResult Function(FieldType key, FieldType value)? map,
    TResult Function(Map<String, FieldType> fields)? object,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default();
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

abstract class _FieldType implements FieldType {
  const factory _FieldType() = _$_FieldType;

  factory _FieldType.fromJson(Map<String, dynamic> json) =
      _$_FieldType.fromJson;
}

/// @nodoc
abstract class _$$PrimitiveFieldCopyWith<$Res> {
  factory _$$PrimitiveFieldCopyWith(
          _$PrimitiveField value, $Res Function(_$PrimitiveField) then) =
      __$$PrimitiveFieldCopyWithImpl<$Res>;
  @useResult
  $Res call({PrimitiveFieldType type});
}

/// @nodoc
class __$$PrimitiveFieldCopyWithImpl<$Res>
    extends _$FieldTypeCopyWithImpl<$Res, _$PrimitiveField>
    implements _$$PrimitiveFieldCopyWith<$Res> {
  __$$PrimitiveFieldCopyWithImpl(
      _$PrimitiveField _value, $Res Function(_$PrimitiveField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
  }) {
    return _then(_$PrimitiveField(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as PrimitiveFieldType,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$PrimitiveField implements PrimitiveField {
  const _$PrimitiveField({required this.type, final String? $type})
      : $type = $type ?? 'primitive';

  factory _$PrimitiveField.fromJson(Map<String, dynamic> json) =>
      _$$PrimitiveFieldFromJson(json);

  @override
  final PrimitiveFieldType type;

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldType.primitive(type: $type)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$PrimitiveField &&
            (identical(other.type, type) || other.type == type));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$PrimitiveFieldCopyWith<_$PrimitiveField> get copyWith =>
      __$$PrimitiveFieldCopyWithImpl<_$PrimitiveField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function() $default, {
    required TResult Function(PrimitiveFieldType type) primitive,
    required TResult Function(List<String> values) enumField,
    required TResult Function(FieldType type) list,
    required TResult Function(FieldType key, FieldType value) map,
    required TResult Function(Map<String, FieldType> fields) object,
  }) {
    return primitive(type);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function()? $default, {
    TResult? Function(PrimitiveFieldType type)? primitive,
    TResult? Function(List<String> values)? enumField,
    TResult? Function(FieldType type)? list,
    TResult? Function(FieldType key, FieldType value)? map,
    TResult? Function(Map<String, FieldType> fields)? object,
  }) {
    return primitive?.call(type);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function()? $default, {
    TResult Function(PrimitiveFieldType type)? primitive,
    TResult Function(List<String> values)? enumField,
    TResult Function(FieldType type)? list,
    TResult Function(FieldType key, FieldType value)? map,
    TResult Function(Map<String, FieldType> fields)? object,
    required TResult orElse(),
  }) {
    if (primitive != null) {
      return primitive(type);
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

abstract class PrimitiveField implements FieldType {
  const factory PrimitiveField({required final PrimitiveFieldType type}) =
      _$PrimitiveField;

  factory PrimitiveField.fromJson(Map<String, dynamic> json) =
      _$PrimitiveField.fromJson;

  PrimitiveFieldType get type;
  @JsonKey(ignore: true)
  _$$PrimitiveFieldCopyWith<_$PrimitiveField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$EnumFieldCopyWith<$Res> {
  factory _$$EnumFieldCopyWith(
          _$EnumField value, $Res Function(_$EnumField) then) =
      __$$EnumFieldCopyWithImpl<$Res>;
  @useResult
  $Res call({List<String> values});
}

/// @nodoc
class __$$EnumFieldCopyWithImpl<$Res>
    extends _$FieldTypeCopyWithImpl<$Res, _$EnumField>
    implements _$$EnumFieldCopyWith<$Res> {
  __$$EnumFieldCopyWithImpl(
      _$EnumField _value, $Res Function(_$EnumField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? values = null,
  }) {
    return _then(_$EnumField(
      values: null == values
          ? _value._values
          : values // ignore: cast_nullable_to_non_nullable
              as List<String>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$EnumField implements EnumField {
  const _$EnumField({required final List<String> values, final String? $type})
      : _values = values,
        $type = $type ?? 'enum';

  factory _$EnumField.fromJson(Map<String, dynamic> json) =>
      _$$EnumFieldFromJson(json);

  final List<String> _values;
  @override
  List<String> get values {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_values);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldType.enumField(values: $values)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EnumField &&
            const DeepCollectionEquality().equals(other._values, _values));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_values));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$EnumFieldCopyWith<_$EnumField> get copyWith =>
      __$$EnumFieldCopyWithImpl<_$EnumField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function() $default, {
    required TResult Function(PrimitiveFieldType type) primitive,
    required TResult Function(List<String> values) enumField,
    required TResult Function(FieldType type) list,
    required TResult Function(FieldType key, FieldType value) map,
    required TResult Function(Map<String, FieldType> fields) object,
  }) {
    return enumField(values);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function()? $default, {
    TResult? Function(PrimitiveFieldType type)? primitive,
    TResult? Function(List<String> values)? enumField,
    TResult? Function(FieldType type)? list,
    TResult? Function(FieldType key, FieldType value)? map,
    TResult? Function(Map<String, FieldType> fields)? object,
  }) {
    return enumField?.call(values);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function()? $default, {
    TResult Function(PrimitiveFieldType type)? primitive,
    TResult Function(List<String> values)? enumField,
    TResult Function(FieldType type)? list,
    TResult Function(FieldType key, FieldType value)? map,
    TResult Function(Map<String, FieldType> fields)? object,
    required TResult orElse(),
  }) {
    if (enumField != null) {
      return enumField(values);
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

abstract class EnumField implements FieldType {
  const factory EnumField({required final List<String> values}) = _$EnumField;

  factory EnumField.fromJson(Map<String, dynamic> json) = _$EnumField.fromJson;

  List<String> get values;
  @JsonKey(ignore: true)
  _$$EnumFieldCopyWith<_$EnumField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ListFieldCopyWith<$Res> {
  factory _$$ListFieldCopyWith(
          _$ListField value, $Res Function(_$ListField) then) =
      __$$ListFieldCopyWithImpl<$Res>;
  @useResult
  $Res call({FieldType type});

  $FieldTypeCopyWith<$Res> get type;
}

/// @nodoc
class __$$ListFieldCopyWithImpl<$Res>
    extends _$FieldTypeCopyWithImpl<$Res, _$ListField>
    implements _$$ListFieldCopyWith<$Res> {
  __$$ListFieldCopyWithImpl(
      _$ListField _value, $Res Function(_$ListField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
  }) {
    return _then(_$ListField(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as FieldType,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $FieldTypeCopyWith<$Res> get type {
    return $FieldTypeCopyWith<$Res>(_value.type, (value) {
      return _then(_value.copyWith(type: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$ListField implements ListField {
  const _$ListField({required this.type, final String? $type})
      : $type = $type ?? 'list';

  factory _$ListField.fromJson(Map<String, dynamic> json) =>
      _$$ListFieldFromJson(json);

  @override
  final FieldType type;

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldType.list(type: $type)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ListField &&
            (identical(other.type, type) || other.type == type));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ListFieldCopyWith<_$ListField> get copyWith =>
      __$$ListFieldCopyWithImpl<_$ListField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function() $default, {
    required TResult Function(PrimitiveFieldType type) primitive,
    required TResult Function(List<String> values) enumField,
    required TResult Function(FieldType type) list,
    required TResult Function(FieldType key, FieldType value) map,
    required TResult Function(Map<String, FieldType> fields) object,
  }) {
    return list(type);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function()? $default, {
    TResult? Function(PrimitiveFieldType type)? primitive,
    TResult? Function(List<String> values)? enumField,
    TResult? Function(FieldType type)? list,
    TResult? Function(FieldType key, FieldType value)? map,
    TResult? Function(Map<String, FieldType> fields)? object,
  }) {
    return list?.call(type);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function()? $default, {
    TResult Function(PrimitiveFieldType type)? primitive,
    TResult Function(List<String> values)? enumField,
    TResult Function(FieldType type)? list,
    TResult Function(FieldType key, FieldType value)? map,
    TResult Function(Map<String, FieldType> fields)? object,
    required TResult orElse(),
  }) {
    if (list != null) {
      return list(type);
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

abstract class ListField implements FieldType {
  const factory ListField({required final FieldType type}) = _$ListField;

  factory ListField.fromJson(Map<String, dynamic> json) = _$ListField.fromJson;

  FieldType get type;
  @JsonKey(ignore: true)
  _$$ListFieldCopyWith<_$ListField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$MapFieldCopyWith<$Res> {
  factory _$$MapFieldCopyWith(
          _$MapField value, $Res Function(_$MapField) then) =
      __$$MapFieldCopyWithImpl<$Res>;
  @useResult
  $Res call({FieldType key, FieldType value});

  $FieldTypeCopyWith<$Res> get key;
  $FieldTypeCopyWith<$Res> get value;
}

/// @nodoc
class __$$MapFieldCopyWithImpl<$Res>
    extends _$FieldTypeCopyWithImpl<$Res, _$MapField>
    implements _$$MapFieldCopyWith<$Res> {
  __$$MapFieldCopyWithImpl(_$MapField _value, $Res Function(_$MapField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
  }) {
    return _then(_$MapField(
      key: null == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as FieldType,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as FieldType,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $FieldTypeCopyWith<$Res> get key {
    return $FieldTypeCopyWith<$Res>(_value.key, (value) {
      return _then(_value.copyWith(key: value));
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $FieldTypeCopyWith<$Res> get value {
    return $FieldTypeCopyWith<$Res>(_value.value, (value) {
      return _then(_value.copyWith(value: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$MapField implements MapField {
  const _$MapField(
      {required this.key, required this.value, final String? $type})
      : $type = $type ?? 'map';

  factory _$MapField.fromJson(Map<String, dynamic> json) =>
      _$$MapFieldFromJson(json);

  @override
  final FieldType key;
  @override
  final FieldType value;

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldType.map(key: $key, value: $value)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MapField &&
            (identical(other.key, key) || other.key == key) &&
            (identical(other.value, value) || other.value == value));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, key, value);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$MapFieldCopyWith<_$MapField> get copyWith =>
      __$$MapFieldCopyWithImpl<_$MapField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function() $default, {
    required TResult Function(PrimitiveFieldType type) primitive,
    required TResult Function(List<String> values) enumField,
    required TResult Function(FieldType type) list,
    required TResult Function(FieldType key, FieldType value) map,
    required TResult Function(Map<String, FieldType> fields) object,
  }) {
    return map(key, value);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function()? $default, {
    TResult? Function(PrimitiveFieldType type)? primitive,
    TResult? Function(List<String> values)? enumField,
    TResult? Function(FieldType type)? list,
    TResult? Function(FieldType key, FieldType value)? map,
    TResult? Function(Map<String, FieldType> fields)? object,
  }) {
    return map?.call(key, value);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function()? $default, {
    TResult Function(PrimitiveFieldType type)? primitive,
    TResult Function(List<String> values)? enumField,
    TResult Function(FieldType type)? list,
    TResult Function(FieldType key, FieldType value)? map,
    TResult Function(Map<String, FieldType> fields)? object,
    required TResult orElse(),
  }) {
    if (map != null) {
      return map(key, value);
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

abstract class MapField implements FieldType {
  const factory MapField(
      {required final FieldType key,
      required final FieldType value}) = _$MapField;

  factory MapField.fromJson(Map<String, dynamic> json) = _$MapField.fromJson;

  FieldType get key;
  FieldType get value;
  @JsonKey(ignore: true)
  _$$MapFieldCopyWith<_$MapField> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ObjectFieldCopyWith<$Res> {
  factory _$$ObjectFieldCopyWith(
          _$ObjectField value, $Res Function(_$ObjectField) then) =
      __$$ObjectFieldCopyWithImpl<$Res>;
  @useResult
  $Res call({Map<String, FieldType> fields});
}

/// @nodoc
class __$$ObjectFieldCopyWithImpl<$Res>
    extends _$FieldTypeCopyWithImpl<$Res, _$ObjectField>
    implements _$$ObjectFieldCopyWith<$Res> {
  __$$ObjectFieldCopyWithImpl(
      _$ObjectField _value, $Res Function(_$ObjectField) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fields = null,
  }) {
    return _then(_$ObjectField(
      fields: null == fields
          ? _value._fields
          : fields // ignore: cast_nullable_to_non_nullable
              as Map<String, FieldType>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ObjectField implements ObjectField {
  const _$ObjectField(
      {required final Map<String, FieldType> fields, final String? $type})
      : _fields = fields,
        $type = $type ?? 'object';

  factory _$ObjectField.fromJson(Map<String, dynamic> json) =>
      _$$ObjectFieldFromJson(json);

  final Map<String, FieldType> _fields;
  @override
  Map<String, FieldType> get fields {
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_fields);
  }

  @JsonKey(name: 'kind')
  final String $type;

  @override
  String toString() {
    return 'FieldType.object(fields: $fields)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ObjectField &&
            const DeepCollectionEquality().equals(other._fields, _fields));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_fields));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ObjectFieldCopyWith<_$ObjectField> get copyWith =>
      __$$ObjectFieldCopyWithImpl<_$ObjectField>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function() $default, {
    required TResult Function(PrimitiveFieldType type) primitive,
    required TResult Function(List<String> values) enumField,
    required TResult Function(FieldType type) list,
    required TResult Function(FieldType key, FieldType value) map,
    required TResult Function(Map<String, FieldType> fields) object,
  }) {
    return object(fields);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function()? $default, {
    TResult? Function(PrimitiveFieldType type)? primitive,
    TResult? Function(List<String> values)? enumField,
    TResult? Function(FieldType type)? list,
    TResult? Function(FieldType key, FieldType value)? map,
    TResult? Function(Map<String, FieldType> fields)? object,
  }) {
    return object?.call(fields);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function()? $default, {
    TResult Function(PrimitiveFieldType type)? primitive,
    TResult Function(List<String> values)? enumField,
    TResult Function(FieldType type)? list,
    TResult Function(FieldType key, FieldType value)? map,
    TResult Function(Map<String, FieldType> fields)? object,
    required TResult orElse(),
  }) {
    if (object != null) {
      return object(fields);
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

abstract class ObjectField implements FieldType {
  const factory ObjectField({required final Map<String, FieldType> fields}) =
      _$ObjectField;

  factory ObjectField.fromJson(Map<String, dynamic> json) =
      _$ObjectField.fromJson;

  Map<String, FieldType> get fields;
  @JsonKey(ignore: true)
  _$$ObjectFieldCopyWith<_$ObjectField> get copyWith =>
      throw _privateConstructorUsedError;
}
