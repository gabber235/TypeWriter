// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'editors.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// ignore_for_file: avoid_private_typedef_functions, non_constant_identifier_names, subtype_of_sealed_class, invalid_use_of_internal_member, unused_element, constant_identifier_names, unnecessary_raw_strings, library_private_types_in_public_api

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

String _$fieldValueHash() => r'aabeadcbd5808e51610fd3651d159472f99afb18';

/// See also [fieldValue].
class FieldValueProvider extends AutoDisposeProvider<dynamic> {
  FieldValueProvider(
    this.path, [
    this.defaultValue,
  ]) : super(
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

typedef FieldValueRef = AutoDisposeProviderRef<dynamic>;

/// See also [fieldValue].
final fieldValueProvider = FieldValueFamily();

class FieldValueFamily extends Family<dynamic> {
  FieldValueFamily();

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
  AutoDisposeProvider<dynamic> getProviderOverride(
    covariant FieldValueProvider provider,
  ) {
    return call(
      provider.path,
      provider.defaultValue,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'fieldValueProvider';
}

String _$editorFiltersHash() => r'c4da1b8a3e45d64e389f3d4d5a383d2f9f572460';

/// See also [editorFilters].
final editorFiltersProvider = AutoDisposeProvider<List<EditorFilter>>(
  editorFilters,
  name: r'editorFiltersProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$editorFiltersHash,
);
typedef EditorFiltersRef = AutoDisposeProviderRef<List<EditorFilter>>;
String _$pathDisplayNameHash() => r'5a1d7d01eff96745145d531682d9ac4f119f1fff';

/// See also [pathDisplayName].
class PathDisplayNameProvider extends AutoDisposeProvider<String> {
  PathDisplayNameProvider(
    this.path, [
    this.defaultValue = "",
  ]) : super(
          (ref) => pathDisplayName(
            ref,
            path,
            defaultValue,
          ),
          from: pathDisplayNameProvider,
          name: r'pathDisplayNameProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pathDisplayNameHash,
        );

  final String path;
  final String defaultValue;

  @override
  bool operator ==(Object other) {
    return other is PathDisplayNameProvider &&
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

typedef PathDisplayNameRef = AutoDisposeProviderRef<String>;

/// See also [pathDisplayName].
final pathDisplayNameProvider = PathDisplayNameFamily();

class PathDisplayNameFamily extends Family<String> {
  PathDisplayNameFamily();

  PathDisplayNameProvider call(
    String path, [
    String defaultValue = "",
  ]) {
    return PathDisplayNameProvider(
      path,
      defaultValue,
    );
  }

  @override
  AutoDisposeProvider<String> getProviderOverride(
    covariant PathDisplayNameProvider provider,
  ) {
    return call(
      provider.path,
      provider.defaultValue,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'pathDisplayNameProvider';
}
