// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'editors.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$fieldValueHash() => r'aabeadcbd5808e51610fd3651d159472f99afb18';

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

typedef FieldValueRef = AutoDisposeProviderRef<dynamic>;

/// See also [fieldValue].
@ProviderFor(fieldValue)
const fieldValueProvider = FieldValueFamily();

/// See also [fieldValue].
class FieldValueFamily extends Family<dynamic> {
  /// See also [fieldValue].
  const FieldValueFamily();

  /// See also [fieldValue].
  FieldValueProvider call(
    String path, [
    dynamic defaultValue,
  ]) {
    return FieldValueProvider(
      path,
      defaultValue,
    );
  }

  @override
  FieldValueProvider getProviderOverride(
    covariant FieldValueProvider provider,
  ) {
    return call(
      provider.path,
      provider.defaultValue,
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
  String? get name => r'fieldValueProvider';
}

/// See also [fieldValue].
class FieldValueProvider extends AutoDisposeProvider<dynamic> {
  /// See also [fieldValue].
  FieldValueProvider(
    this.path, [
    this.defaultValue,
  ]) : super.internal(
          (ref) => fieldValue(
            ref,
            path,
            defaultValue,
          ),
          from: fieldValueProvider,
          name: r'fieldValueProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$fieldValueHash,
          dependencies: FieldValueFamily._dependencies,
          allTransitiveDependencies:
              FieldValueFamily._allTransitiveDependencies,
        );

  final String path;
  final dynamic defaultValue;

  @override
  bool operator ==(Object other) {
    return other is FieldValueProvider &&
        other.path == path &&
        other.defaultValue == defaultValue;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, path.hashCode);
    hash = _SystemHash.combine(hash, defaultValue.hashCode);

    return _SystemHash.finish(hash);
  }
}

String _$editorFiltersHash() => r'82881dad4fc36c0d7950c7bee132cd91a6e0314a';

/// See also [editorFilters].
@ProviderFor(editorFilters)
final editorFiltersProvider = AutoDisposeProvider<List<EditorFilter>>.internal(
  editorFilters,
  name: r'editorFiltersProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$editorFiltersHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef EditorFiltersRef = AutoDisposeProviderRef<List<EditorFilter>>;
String _$pathDisplayNameHash() => r'c772e17ed429169ba903826d0a5605d60b129a31';
typedef PathDisplayNameRef = AutoDisposeProviderRef<String>;

/// See also [pathDisplayName].
@ProviderFor(pathDisplayName)
const pathDisplayNameProvider = PathDisplayNameFamily();

/// See also [pathDisplayName].
class PathDisplayNameFamily extends Family<String> {
  /// See also [pathDisplayName].
  const PathDisplayNameFamily();

  /// See also [pathDisplayName].
  PathDisplayNameProvider call(
    String path,
  ) {
    return PathDisplayNameProvider(
      path,
    );
  }

  @override
  PathDisplayNameProvider getProviderOverride(
    covariant PathDisplayNameProvider provider,
  ) {
    return call(
      provider.path,
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
  String? get name => r'pathDisplayNameProvider';
}

/// See also [pathDisplayName].
class PathDisplayNameProvider extends AutoDisposeProvider<String> {
  /// See also [pathDisplayName].
  PathDisplayNameProvider(
    this.path,
  ) : super.internal(
          (ref) => pathDisplayName(
            ref,
            path,
          ),
          from: pathDisplayNameProvider,
          name: r'pathDisplayNameProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pathDisplayNameHash,
          dependencies: PathDisplayNameFamily._dependencies,
          allTransitiveDependencies:
              PathDisplayNameFamily._allTransitiveDependencies,
        );

  final String path;

  @override
  bool operator ==(Object other) {
    return other is PathDisplayNameProvider && other.path == path;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, path.hashCode);

    return _SystemHash.finish(hash);
  }
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member
