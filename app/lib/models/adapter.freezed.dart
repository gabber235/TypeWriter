// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

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
abstract class _$$AdapterImplCopyWith<$Res> implements $AdapterCopyWith<$Res> {
  factory _$$AdapterImplCopyWith(
          _$AdapterImpl value, $Res Function(_$AdapterImpl) then) =
      __$$AdapterImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String name,
      String description,
      String version,
      List<EntryBlueprint> entries});
}

/// @nodoc
class __$$AdapterImplCopyWithImpl<$Res>
    extends _$AdapterCopyWithImpl<$Res, _$AdapterImpl>
    implements _$$AdapterImplCopyWith<$Res> {
  __$$AdapterImplCopyWithImpl(
      _$AdapterImpl _value, $Res Function(_$AdapterImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
    Object? version = null,
    Object? entries = null,
  }) {
    return _then(_$AdapterImpl(
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
class _$AdapterImpl with DiagnosticableTreeMixin implements _Adapter {
  const _$AdapterImpl(
      {required this.name,
      required this.description,
      required this.version,
      required final List<EntryBlueprint> entries})
      : _entries = entries;

  factory _$AdapterImpl.fromJson(Map<String, dynamic> json) =>
      _$$AdapterImplFromJson(json);

  @override
  final String name;
  @override
  final String description;
  @override
  final String version;
  final List<EntryBlueprint> _entries;
  @override
  List<EntryBlueprint> get entries {
    if (_entries is EqualUnmodifiableListView) return _entries;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_entries);
  }

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'Adapter(name: $name, description: $description, version: $version, entries: $entries)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'Adapter'))
      ..add(DiagnosticsProperty('name', name))
      ..add(DiagnosticsProperty('description', description))
      ..add(DiagnosticsProperty('version', version))
      ..add(DiagnosticsProperty('entries', entries));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdapterImpl &&
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
  _$$AdapterImplCopyWith<_$AdapterImpl> get copyWith =>
      __$$AdapterImplCopyWithImpl<_$AdapterImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdapterImplToJson(
      this,
    );
  }
}

abstract class _Adapter implements Adapter {
  const factory _Adapter(
      {required final String name,
      required final String description,
      required final String version,
      required final List<EntryBlueprint> entries}) = _$AdapterImpl;

  factory _Adapter.fromJson(Map<String, dynamic> json) = _$AdapterImpl.fromJson;

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
  _$$AdapterImplCopyWith<_$AdapterImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

EntryBlueprint _$EntryBlueprintFromJson(Map<String, dynamic> json) {
  return _EntryBlueprint.fromJson(json);
}

/// @nodoc
mixin _$EntryBlueprint {
  String get name => throw _privateConstructorUsedError;
  String get description => throw _privateConstructorUsedError;
  String get adapter => throw _privateConstructorUsedError;
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
      String adapter,
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
    Object? adapter = null,
    Object? fields = freezed,
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
      adapter: null == adapter
          ? _value.adapter
          : adapter // ignore: cast_nullable_to_non_nullable
              as String,
      fields: freezed == fields
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
abstract class _$$EntryBlueprintImplCopyWith<$Res>
    implements $EntryBlueprintCopyWith<$Res> {
  factory _$$EntryBlueprintImplCopyWith(_$EntryBlueprintImpl value,
          $Res Function(_$EntryBlueprintImpl) then) =
      __$$EntryBlueprintImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String name,
      String description,
      String adapter,
      ObjectField fields,
      List<String> tags,
      @ColorConverter() Color color,
      @IconConverter() IconData icon});
}

/// @nodoc
class __$$EntryBlueprintImplCopyWithImpl<$Res>
    extends _$EntryBlueprintCopyWithImpl<$Res, _$EntryBlueprintImpl>
    implements _$$EntryBlueprintImplCopyWith<$Res> {
  __$$EntryBlueprintImplCopyWithImpl(
      _$EntryBlueprintImpl _value, $Res Function(_$EntryBlueprintImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
    Object? adapter = null,
    Object? fields = freezed,
    Object? tags = null,
    Object? color = null,
    Object? icon = null,
  }) {
    return _then(_$EntryBlueprintImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
      adapter: null == adapter
          ? _value.adapter
          : adapter // ignore: cast_nullable_to_non_nullable
              as String,
      fields: freezed == fields
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
class _$EntryBlueprintImpl
    with DiagnosticableTreeMixin
    implements _EntryBlueprint {
  const _$EntryBlueprintImpl(
      {required this.name,
      required this.description,
      required this.adapter,
      required this.fields,
      final List<String> tags = const <String>[],
      @ColorConverter() this.color = Colors.grey,
      @IconConverter() this.icon = Icons.help})
      : _tags = tags;

  factory _$EntryBlueprintImpl.fromJson(Map<String, dynamic> json) =>
      _$$EntryBlueprintImplFromJson(json);

  @override
  final String name;
  @override
  final String description;
  @override
  final String adapter;
  @override
  final ObjectField fields;
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
  @IconConverter()
  final IconData icon;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'EntryBlueprint(name: $name, description: $description, adapter: $adapter, fields: $fields, tags: $tags, color: $color, icon: $icon)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'EntryBlueprint'))
      ..add(DiagnosticsProperty('name', name))
      ..add(DiagnosticsProperty('description', description))
      ..add(DiagnosticsProperty('adapter', adapter))
      ..add(DiagnosticsProperty('fields', fields))
      ..add(DiagnosticsProperty('tags', tags))
      ..add(DiagnosticsProperty('color', color))
      ..add(DiagnosticsProperty('icon', icon));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EntryBlueprintImpl &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.adapter, adapter) || other.adapter == adapter) &&
            const DeepCollectionEquality().equals(other.fields, fields) &&
            const DeepCollectionEquality().equals(other._tags, _tags) &&
            (identical(other.color, color) || other.color == color) &&
            (identical(other.icon, icon) || other.icon == icon));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      name,
      description,
      adapter,
      const DeepCollectionEquality().hash(fields),
      const DeepCollectionEquality().hash(_tags),
      color,
      icon);

  @JsonKey(ignore: true)
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
      {required final String name,
      required final String description,
      required final String adapter,
      required final ObjectField fields,
      final List<String> tags,
      @ColorConverter() final Color color,
      @IconConverter() final IconData icon}) = _$EntryBlueprintImpl;

  factory _EntryBlueprint.fromJson(Map<String, dynamic> json) =
      _$EntryBlueprintImpl.fromJson;

  @override
  String get name;
  @override
  String get description;
  @override
  String get adapter;
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
  _$$EntryBlueprintImplCopyWith<_$EntryBlueprintImpl> get copyWith =>
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
    case 'custom':
      return CustomField.fromJson(json);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    required TResult Function(CustomField value) custom,
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
    TResult? Function(CustomField value)? custom,
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
    TResult Function(CustomField value)? custom,
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
abstract class _$$FieldTypeImplCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$FieldTypeImplCopyWith(
          _$FieldTypeImpl value, $Res Function(_$FieldTypeImpl) then) =
      __$$FieldTypeImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({List<Modifier> modifiers});
}

/// @nodoc
class __$$FieldTypeImplCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$FieldTypeImpl>
    implements _$$FieldTypeImplCopyWith<$Res> {
  __$$FieldTypeImplCopyWithImpl(
      _$FieldTypeImpl _value, $Res Function(_$FieldTypeImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? modifiers = null,
  }) {
    return _then(_$FieldTypeImpl(
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FieldTypeImpl with DiagnosticableTreeMixin implements _FieldType {
  const _$FieldTypeImpl(
      {final List<Modifier> modifiers = const [], final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'default';

  factory _$FieldTypeImpl.fromJson(Map<String, dynamic> json) =>
      _$$FieldTypeImplFromJson(json);

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
    return 'FieldInfo(modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'FieldInfo'))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FieldTypeImpl &&
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
  _$$FieldTypeImplCopyWith<_$FieldTypeImpl> get copyWith =>
      __$$FieldTypeImplCopyWithImpl<_$FieldTypeImpl>(this, _$identity);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    required TResult Function(CustomField value) custom,
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
    TResult? Function(CustomField value)? custom,
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
    TResult Function(CustomField value)? custom,
    required TResult orElse(),
  }) {
    if ($default != null) {
      return $default(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$FieldTypeImplToJson(
      this,
    );
  }
}

abstract class _FieldType implements FieldInfo {
  const factory _FieldType({final List<Modifier> modifiers}) = _$FieldTypeImpl;

  factory _FieldType.fromJson(Map<String, dynamic> json) =
      _$FieldTypeImpl.fromJson;

  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$FieldTypeImplCopyWith<_$FieldTypeImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$PrimitiveFieldImplCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$PrimitiveFieldImplCopyWith(_$PrimitiveFieldImpl value,
          $Res Function(_$PrimitiveFieldImpl) then) =
      __$$PrimitiveFieldImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({PrimitiveFieldType type, List<Modifier> modifiers});
}

/// @nodoc
class __$$PrimitiveFieldImplCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$PrimitiveFieldImpl>
    implements _$$PrimitiveFieldImplCopyWith<$Res> {
  __$$PrimitiveFieldImplCopyWithImpl(
      _$PrimitiveFieldImpl _value, $Res Function(_$PrimitiveFieldImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? modifiers = null,
  }) {
    return _then(_$PrimitiveFieldImpl(
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
class _$PrimitiveFieldImpl
    with DiagnosticableTreeMixin
    implements PrimitiveField {
  const _$PrimitiveFieldImpl(
      {required this.type,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'primitive';

  factory _$PrimitiveFieldImpl.fromJson(Map<String, dynamic> json) =>
      _$$PrimitiveFieldImplFromJson(json);

  @override
  final PrimitiveFieldType type;
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
    return 'FieldInfo.primitive(type: $type, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'FieldInfo.primitive'))
      ..add(DiagnosticsProperty('type', type))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$PrimitiveFieldImpl &&
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
  _$$PrimitiveFieldImplCopyWith<_$PrimitiveFieldImpl> get copyWith =>
      __$$PrimitiveFieldImplCopyWithImpl<_$PrimitiveFieldImpl>(
          this, _$identity);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    required TResult Function(CustomField value) custom,
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
    TResult? Function(CustomField value)? custom,
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
    TResult Function(CustomField value)? custom,
    required TResult orElse(),
  }) {
    if (primitive != null) {
      return primitive(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$PrimitiveFieldImplToJson(
      this,
    );
  }
}

abstract class PrimitiveField implements FieldInfo {
  const factory PrimitiveField(
      {required final PrimitiveFieldType type,
      final List<Modifier> modifiers}) = _$PrimitiveFieldImpl;

  factory PrimitiveField.fromJson(Map<String, dynamic> json) =
      _$PrimitiveFieldImpl.fromJson;

  PrimitiveFieldType get type;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$PrimitiveFieldImplCopyWith<_$PrimitiveFieldImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$EnumFieldImplCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$EnumFieldImplCopyWith(
          _$EnumFieldImpl value, $Res Function(_$EnumFieldImpl) then) =
      __$$EnumFieldImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({List<String> values, List<Modifier> modifiers});
}

/// @nodoc
class __$$EnumFieldImplCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$EnumFieldImpl>
    implements _$$EnumFieldImplCopyWith<$Res> {
  __$$EnumFieldImplCopyWithImpl(
      _$EnumFieldImpl _value, $Res Function(_$EnumFieldImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? values = null,
    Object? modifiers = null,
  }) {
    return _then(_$EnumFieldImpl(
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
class _$EnumFieldImpl with DiagnosticableTreeMixin implements EnumField {
  const _$EnumFieldImpl(
      {required final List<String> values,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _values = values,
        _modifiers = modifiers,
        $type = $type ?? 'enum';

  factory _$EnumFieldImpl.fromJson(Map<String, dynamic> json) =>
      _$$EnumFieldImplFromJson(json);

  final List<String> _values;
  @override
  List<String> get values {
    if (_values is EqualUnmodifiableListView) return _values;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_values);
  }

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
    return 'FieldInfo.enumField(values: $values, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'FieldInfo.enumField'))
      ..add(DiagnosticsProperty('values', values))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EnumFieldImpl &&
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
  _$$EnumFieldImplCopyWith<_$EnumFieldImpl> get copyWith =>
      __$$EnumFieldImplCopyWithImpl<_$EnumFieldImpl>(this, _$identity);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    required TResult Function(CustomField value) custom,
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
    TResult? Function(CustomField value)? custom,
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
    TResult Function(CustomField value)? custom,
    required TResult orElse(),
  }) {
    if (enumField != null) {
      return enumField(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$EnumFieldImplToJson(
      this,
    );
  }
}

abstract class EnumField implements FieldInfo {
  const factory EnumField(
      {required final List<String> values,
      final List<Modifier> modifiers}) = _$EnumFieldImpl;

  factory EnumField.fromJson(Map<String, dynamic> json) =
      _$EnumFieldImpl.fromJson;

  List<String> get values;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$EnumFieldImplCopyWith<_$EnumFieldImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ListFieldImplCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$ListFieldImplCopyWith(
          _$ListFieldImpl value, $Res Function(_$ListFieldImpl) then) =
      __$$ListFieldImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({FieldInfo type, List<Modifier> modifiers});

  $FieldInfoCopyWith<$Res> get type;
}

/// @nodoc
class __$$ListFieldImplCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$ListFieldImpl>
    implements _$$ListFieldImplCopyWith<$Res> {
  __$$ListFieldImplCopyWithImpl(
      _$ListFieldImpl _value, $Res Function(_$ListFieldImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? modifiers = null,
  }) {
    return _then(_$ListFieldImpl(
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
class _$ListFieldImpl with DiagnosticableTreeMixin implements ListField {
  const _$ListFieldImpl(
      {required this.type,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'list';

  factory _$ListFieldImpl.fromJson(Map<String, dynamic> json) =>
      _$$ListFieldImplFromJson(json);

  @override
  final FieldInfo type;
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
    return 'FieldInfo.list(type: $type, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'FieldInfo.list'))
      ..add(DiagnosticsProperty('type', type))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ListFieldImpl &&
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
  _$$ListFieldImplCopyWith<_$ListFieldImpl> get copyWith =>
      __$$ListFieldImplCopyWithImpl<_$ListFieldImpl>(this, _$identity);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    required TResult Function(CustomField value) custom,
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
    TResult? Function(CustomField value)? custom,
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
    TResult Function(CustomField value)? custom,
    required TResult orElse(),
  }) {
    if (list != null) {
      return list(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$ListFieldImplToJson(
      this,
    );
  }
}

abstract class ListField implements FieldInfo {
  const factory ListField(
      {required final FieldInfo type,
      final List<Modifier> modifiers}) = _$ListFieldImpl;

  factory ListField.fromJson(Map<String, dynamic> json) =
      _$ListFieldImpl.fromJson;

  FieldInfo get type;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$ListFieldImplCopyWith<_$ListFieldImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$MapFieldImplCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$MapFieldImplCopyWith(
          _$MapFieldImpl value, $Res Function(_$MapFieldImpl) then) =
      __$$MapFieldImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({FieldInfo key, FieldInfo value, List<Modifier> modifiers});

  $FieldInfoCopyWith<$Res> get key;
  $FieldInfoCopyWith<$Res> get value;
}

/// @nodoc
class __$$MapFieldImplCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$MapFieldImpl>
    implements _$$MapFieldImplCopyWith<$Res> {
  __$$MapFieldImplCopyWithImpl(
      _$MapFieldImpl _value, $Res Function(_$MapFieldImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
    Object? modifiers = null,
  }) {
    return _then(_$MapFieldImpl(
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
class _$MapFieldImpl with DiagnosticableTreeMixin implements MapField {
  const _$MapFieldImpl(
      {required this.key,
      required this.value,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'map';

  factory _$MapFieldImpl.fromJson(Map<String, dynamic> json) =>
      _$$MapFieldImplFromJson(json);

  @override
  final FieldInfo key;
  @override
  final FieldInfo value;
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
    return 'FieldInfo.map(key: $key, value: $value, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'FieldInfo.map'))
      ..add(DiagnosticsProperty('key', key))
      ..add(DiagnosticsProperty('value', value))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MapFieldImpl &&
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
  _$$MapFieldImplCopyWith<_$MapFieldImpl> get copyWith =>
      __$$MapFieldImplCopyWithImpl<_$MapFieldImpl>(this, _$identity);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    required TResult Function(CustomField value) custom,
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
    TResult? Function(CustomField value)? custom,
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
    TResult Function(CustomField value)? custom,
    required TResult orElse(),
  }) {
    if (map != null) {
      return map(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$MapFieldImplToJson(
      this,
    );
  }
}

abstract class MapField implements FieldInfo {
  const factory MapField(
      {required final FieldInfo key,
      required final FieldInfo value,
      final List<Modifier> modifiers}) = _$MapFieldImpl;

  factory MapField.fromJson(Map<String, dynamic> json) =
      _$MapFieldImpl.fromJson;

  FieldInfo get key;
  FieldInfo get value;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$MapFieldImplCopyWith<_$MapFieldImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ObjectFieldImplCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$ObjectFieldImplCopyWith(
          _$ObjectFieldImpl value, $Res Function(_$ObjectFieldImpl) then) =
      __$$ObjectFieldImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({Map<String, FieldInfo> fields, List<Modifier> modifiers});
}

/// @nodoc
class __$$ObjectFieldImplCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$ObjectFieldImpl>
    implements _$$ObjectFieldImplCopyWith<$Res> {
  __$$ObjectFieldImplCopyWithImpl(
      _$ObjectFieldImpl _value, $Res Function(_$ObjectFieldImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fields = null,
    Object? modifiers = null,
  }) {
    return _then(_$ObjectFieldImpl(
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
class _$ObjectFieldImpl with DiagnosticableTreeMixin implements ObjectField {
  const _$ObjectFieldImpl(
      {required final Map<String, FieldInfo> fields,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _fields = fields,
        _modifiers = modifiers,
        $type = $type ?? 'object';

  factory _$ObjectFieldImpl.fromJson(Map<String, dynamic> json) =>
      _$$ObjectFieldImplFromJson(json);

  final Map<String, FieldInfo> _fields;
  @override
  Map<String, FieldInfo> get fields {
    if (_fields is EqualUnmodifiableMapView) return _fields;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_fields);
  }

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
    return 'FieldInfo.object(fields: $fields, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'FieldInfo.object'))
      ..add(DiagnosticsProperty('fields', fields))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ObjectFieldImpl &&
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
  _$$ObjectFieldImplCopyWith<_$ObjectFieldImpl> get copyWith =>
      __$$ObjectFieldImplCopyWithImpl<_$ObjectFieldImpl>(this, _$identity);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
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
    required TResult Function(CustomField value) custom,
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
    TResult? Function(CustomField value)? custom,
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
    TResult Function(CustomField value)? custom,
    required TResult orElse(),
  }) {
    if (object != null) {
      return object(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$ObjectFieldImplToJson(
      this,
    );
  }
}

abstract class ObjectField implements FieldInfo {
  const factory ObjectField(
      {required final Map<String, FieldInfo> fields,
      final List<Modifier> modifiers}) = _$ObjectFieldImpl;

  factory ObjectField.fromJson(Map<String, dynamic> json) =
      _$ObjectFieldImpl.fromJson;

  Map<String, FieldInfo> get fields;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$ObjectFieldImplCopyWith<_$ObjectFieldImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$CustomFieldImplCopyWith<$Res>
    implements $FieldInfoCopyWith<$Res> {
  factory _$$CustomFieldImplCopyWith(
          _$CustomFieldImpl value, $Res Function(_$CustomFieldImpl) then) =
      __$$CustomFieldImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String editor,
      @JsonKey(name: "default") dynamic defaultValue,
      FieldInfo? fieldInfo,
      List<Modifier> modifiers});

  $FieldInfoCopyWith<$Res>? get fieldInfo;
}

/// @nodoc
class __$$CustomFieldImplCopyWithImpl<$Res>
    extends _$FieldInfoCopyWithImpl<$Res, _$CustomFieldImpl>
    implements _$$CustomFieldImplCopyWith<$Res> {
  __$$CustomFieldImplCopyWithImpl(
      _$CustomFieldImpl _value, $Res Function(_$CustomFieldImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? editor = null,
    Object? defaultValue = freezed,
    Object? fieldInfo = freezed,
    Object? modifiers = null,
  }) {
    return _then(_$CustomFieldImpl(
      editor: null == editor
          ? _value.editor
          : editor // ignore: cast_nullable_to_non_nullable
              as String,
      defaultValue: freezed == defaultValue
          ? _value.defaultValue
          : defaultValue // ignore: cast_nullable_to_non_nullable
              as dynamic,
      fieldInfo: freezed == fieldInfo
          ? _value.fieldInfo
          : fieldInfo // ignore: cast_nullable_to_non_nullable
              as FieldInfo?,
      modifiers: null == modifiers
          ? _value._modifiers
          : modifiers // ignore: cast_nullable_to_non_nullable
              as List<Modifier>,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $FieldInfoCopyWith<$Res>? get fieldInfo {
    if (_value.fieldInfo == null) {
      return null;
    }

    return $FieldInfoCopyWith<$Res>(_value.fieldInfo!, (value) {
      return _then(_value.copyWith(fieldInfo: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _$CustomFieldImpl with DiagnosticableTreeMixin implements CustomField {
  const _$CustomFieldImpl(
      {required this.editor,
      @JsonKey(name: "default") this.defaultValue,
      this.fieldInfo,
      final List<Modifier> modifiers = const [],
      final String? $type})
      : _modifiers = modifiers,
        $type = $type ?? 'custom';

  factory _$CustomFieldImpl.fromJson(Map<String, dynamic> json) =>
      _$$CustomFieldImplFromJson(json);

  @override
  final String editor;
  @override
  @JsonKey(name: "default")
  final dynamic defaultValue;
  @override
  final FieldInfo? fieldInfo;
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
    return 'FieldInfo.custom(editor: $editor, defaultValue: $defaultValue, fieldInfo: $fieldInfo, modifiers: $modifiers)';
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties
      ..add(DiagnosticsProperty('type', 'FieldInfo.custom'))
      ..add(DiagnosticsProperty('editor', editor))
      ..add(DiagnosticsProperty('defaultValue', defaultValue))
      ..add(DiagnosticsProperty('fieldInfo', fieldInfo))
      ..add(DiagnosticsProperty('modifiers', modifiers));
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$CustomFieldImpl &&
            (identical(other.editor, editor) || other.editor == editor) &&
            const DeepCollectionEquality()
                .equals(other.defaultValue, defaultValue) &&
            (identical(other.fieldInfo, fieldInfo) ||
                other.fieldInfo == fieldInfo) &&
            const DeepCollectionEquality()
                .equals(other._modifiers, _modifiers));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      editor,
      const DeepCollectionEquality().hash(defaultValue),
      fieldInfo,
      const DeepCollectionEquality().hash(_modifiers));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$CustomFieldImplCopyWith<_$CustomFieldImpl> get copyWith =>
      __$$CustomFieldImplCopyWithImpl<_$CustomFieldImpl>(this, _$identity);

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
    required TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)
        custom,
  }) {
    return custom(editor, defaultValue, fieldInfo, modifiers);
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
    TResult? Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
  }) {
    return custom?.call(editor, defaultValue, fieldInfo, modifiers);
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
    TResult Function(
            String editor,
            @JsonKey(name: "default") dynamic defaultValue,
            FieldInfo? fieldInfo,
            List<Modifier> modifiers)?
        custom,
    required TResult orElse(),
  }) {
    if (custom != null) {
      return custom(editor, defaultValue, fieldInfo, modifiers);
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
    required TResult Function(CustomField value) custom,
  }) {
    return custom(this);
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
    TResult? Function(CustomField value)? custom,
  }) {
    return custom?.call(this);
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
    TResult Function(CustomField value)? custom,
    required TResult orElse(),
  }) {
    if (custom != null) {
      return custom(this);
    }
    return orElse();
  }

  @override
  Map<String, dynamic> toJson() {
    return _$$CustomFieldImplToJson(
      this,
    );
  }
}

abstract class CustomField implements FieldInfo {
  const factory CustomField(
      {required final String editor,
      @JsonKey(name: "default") final dynamic defaultValue,
      final FieldInfo? fieldInfo,
      final List<Modifier> modifiers}) = _$CustomFieldImpl;

  factory CustomField.fromJson(Map<String, dynamic> json) =
      _$CustomFieldImpl.fromJson;

  String get editor;
  @JsonKey(name: "default")
  dynamic get defaultValue;
  FieldInfo? get fieldInfo;
  @override
  List<Modifier> get modifiers;
  @override
  @JsonKey(ignore: true)
  _$$CustomFieldImplCopyWith<_$CustomFieldImpl> get copyWith =>
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
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ModifierImpl &&
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
  @override
  @JsonKey(ignore: true)
  _$$ModifierImplCopyWith<_$ModifierImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
