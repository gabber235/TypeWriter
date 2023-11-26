// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'writers.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$WriterImpl _$$WriterImplFromJson(Map<String, dynamic> json) => _$WriterImpl(
      id: json['id'] as String,
      iconUrl: json['iconUrl'] as String?,
      pageId: json['pageId'] as String?,
      entryId: json['entryId'] as String?,
      field: json['field'] as String?,
    );

Map<String, dynamic> _$$WriterImplToJson(_$WriterImpl instance) =>
    <String, dynamic>{
      'id': instance.id,
      'iconUrl': instance.iconUrl,
      'pageId': instance.pageId,
      'entryId': instance.entryId,
      'field': instance.field,
    };

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$fieldWritersHash() => r'1dd7121d271352fa9727b6bc811f6776c06c63d4';

/// Copied from Dart SDK
class _SystemHash {
  _SystemHash._();

  static int combine(int hash, int value) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + value);
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x0007ffff & hash) << 10));
    return hash ^ (hash >> 6);
  }

  static int finish(int hash) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x03ffffff & hash) << 3));
    // ignore: parameter_assignments
    hash = hash ^ (hash >> 11);
    return 0x1fffffff & (hash + ((0x00003fff & hash) << 15));
  }
}

/// Get all the writers that are writhing in a specific field.
/// [path] the given path of the field.
///
/// Copied from [fieldWriters].
@ProviderFor(fieldWriters)
const fieldWritersProvider = FieldWritersFamily();

/// Get all the writers that are writhing in a specific field.
/// [path] the given path of the field.
///
/// Copied from [fieldWriters].
class FieldWritersFamily extends Family<List<Writer>> {
  /// Get all the writers that are writhing in a specific field.
  /// [path] the given path of the field.
  ///
  /// Copied from [fieldWriters].
  const FieldWritersFamily();

  /// Get all the writers that are writhing in a specific field.
  /// [path] the given path of the field.
  ///
  /// Copied from [fieldWriters].
  FieldWritersProvider call(
    String path, {
    bool exact = false,
  }) {
    return FieldWritersProvider(
      path,
      exact: exact,
    );
  }

  @override
  FieldWritersProvider getProviderOverride(
    covariant FieldWritersProvider provider,
  ) {
    return call(
      provider.path,
      exact: provider.exact,
    );
  }

  static const Iterable<ProviderOrFamily>? _dependencies = null;

  @override
  Iterable<ProviderOrFamily>? get dependencies => _dependencies;

  static const Iterable<ProviderOrFamily>? _allTransitiveDependencies = null;

  @override
  Iterable<ProviderOrFamily>? get allTransitiveDependencies =>
      _allTransitiveDependencies;

  @override
  String? get name => r'fieldWritersProvider';
}

/// Get all the writers that are writhing in a specific field.
/// [path] the given path of the field.
///
/// Copied from [fieldWriters].
class FieldWritersProvider extends AutoDisposeProvider<List<Writer>> {
  /// Get all the writers that are writhing in a specific field.
  /// [path] the given path of the field.
  ///
  /// Copied from [fieldWriters].
  FieldWritersProvider(
    String path, {
    bool exact = false,
  }) : this._internal(
          (ref) => fieldWriters(
            ref as FieldWritersRef,
            path,
            exact: exact,
          ),
          from: fieldWritersProvider,
          name: r'fieldWritersProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$fieldWritersHash,
          dependencies: FieldWritersFamily._dependencies,
          allTransitiveDependencies:
              FieldWritersFamily._allTransitiveDependencies,
          path: path,
          exact: exact,
        );

  FieldWritersProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.path,
    required this.exact,
  }) : super.internal();

  final String path;
  final bool exact;

  @override
  Override overrideWith(
    List<Writer> Function(FieldWritersRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: FieldWritersProvider._internal(
        (ref) => create(ref as FieldWritersRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        path: path,
        exact: exact,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<Writer>> createElement() {
    return _FieldWritersProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is FieldWritersProvider &&
        other.path == path &&
        other.exact == exact;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, path.hashCode);
    hash = _SystemHash.combine(hash, exact.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin FieldWritersRef on AutoDisposeProviderRef<List<Writer>> {
  /// The parameter `path` of this provider.
  String get path;

  /// The parameter `exact` of this provider.
  bool get exact;
}

class _FieldWritersProviderElement
    extends AutoDisposeProviderElement<List<Writer>> with FieldWritersRef {
  _FieldWritersProviderElement(super.provider);

  @override
  String get path => (origin as FieldWritersProvider).path;
  @override
  bool get exact => (origin as FieldWritersProvider).exact;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
