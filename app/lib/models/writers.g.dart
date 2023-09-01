// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'writers.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_Writer _$$_WriterFromJson(Map<String, dynamic> json) => _$_Writer(
      id: json['id'] as String,
      iconUrl: json['iconUrl'] as String?,
      pageId: json['pageId'] as String?,
      entryId: json['entryId'] as String?,
      field: json['field'] as String?,
    );

Map<String, dynamic> _$$_WriterToJson(_$_Writer instance) => <String, dynamic>{
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

typedef FieldWritersRef = AutoDisposeProviderRef<List<Writer>>;

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
    this.path, {
    this.exact = false,
  }) : super.internal(
          (ref) => fieldWriters(
            ref,
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
        );

  final String path;
  final bool exact;

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
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member
