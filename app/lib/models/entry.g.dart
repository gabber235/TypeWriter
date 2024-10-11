// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entry.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$entryDefinitionHash() => r'48daba0fcb38d6f4c72fe2fe06680bed39074626';

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

/// See also [entryDefinition].
@ProviderFor(entryDefinition)
const entryDefinitionProvider = EntryDefinitionFamily();

/// See also [entryDefinition].
class EntryDefinitionFamily extends Family<EntryDefinition?> {
  /// See also [entryDefinition].
  const EntryDefinitionFamily();

  /// See also [entryDefinition].
  EntryDefinitionProvider call(
    String entryId,
  ) {
    return EntryDefinitionProvider(
      entryId,
    );
  }

  @override
  EntryDefinitionProvider getProviderOverride(
    covariant EntryDefinitionProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'entryDefinitionProvider';
}

/// See also [entryDefinition].
class EntryDefinitionProvider extends AutoDisposeProvider<EntryDefinition?> {
  /// See also [entryDefinition].
  EntryDefinitionProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryDefinition(
            ref as EntryDefinitionRef,
            entryId,
          ),
          from: entryDefinitionProvider,
          name: r'entryDefinitionProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryDefinitionHash,
          dependencies: EntryDefinitionFamily._dependencies,
          allTransitiveDependencies:
              EntryDefinitionFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryDefinitionProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    EntryDefinition? Function(EntryDefinitionRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryDefinitionProvider._internal(
        (ref) => create(ref as EntryDefinitionRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<EntryDefinition?> createElement() {
    return _EntryDefinitionProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryDefinitionProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryDefinitionRef on AutoDisposeProviderRef<EntryDefinition?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryDefinitionProviderElement
    extends AutoDisposeProviderElement<EntryDefinition?>
    with EntryDefinitionRef {
  _EntryDefinitionProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryDefinitionProvider).entryId;
}

String _$entryNameHash() => r'7e19e3e55767e19fe015a69c99f4af9173872ef2';

/// See also [entryName].
@ProviderFor(entryName)
const entryNameProvider = EntryNameFamily();

/// See also [entryName].
class EntryNameFamily extends Family<String?> {
  /// See also [entryName].
  const EntryNameFamily();

  /// See also [entryName].
  EntryNameProvider call(
    String entryId,
  ) {
    return EntryNameProvider(
      entryId,
    );
  }

  @override
  EntryNameProvider getProviderOverride(
    covariant EntryNameProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'entryNameProvider';
}

/// See also [entryName].
class EntryNameProvider extends AutoDisposeProvider<String?> {
  /// See also [entryName].
  EntryNameProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryName(
            ref as EntryNameRef,
            entryId,
          ),
          from: entryNameProvider,
          name: r'entryNameProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryNameHash,
          dependencies: EntryNameFamily._dependencies,
          allTransitiveDependencies: EntryNameFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryNameProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    String? Function(EntryNameRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryNameProvider._internal(
        (ref) => create(ref as EntryNameRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<String?> createElement() {
    return _EntryNameProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryNameProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryNameRef on AutoDisposeProviderRef<String?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryNameProviderElement extends AutoDisposeProviderElement<String?>
    with EntryNameRef {
  _EntryNameProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryNameProvider).entryId;
}

String _$entryBlueprintIdHash() => r'5cac165b3797bad6ce6518cd948251f1107d7a2d';

/// See also [entryBlueprintId].
@ProviderFor(entryBlueprintId)
const entryBlueprintIdProvider = EntryBlueprintIdFamily();

/// See also [entryBlueprintId].
class EntryBlueprintIdFamily extends Family<String?> {
  /// See also [entryBlueprintId].
  const EntryBlueprintIdFamily();

  /// See also [entryBlueprintId].
  EntryBlueprintIdProvider call(
    String entryId,
  ) {
    return EntryBlueprintIdProvider(
      entryId,
    );
  }

  @override
  EntryBlueprintIdProvider getProviderOverride(
    covariant EntryBlueprintIdProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'entryBlueprintIdProvider';
}

/// See also [entryBlueprintId].
class EntryBlueprintIdProvider extends AutoDisposeProvider<String?> {
  /// See also [entryBlueprintId].
  EntryBlueprintIdProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryBlueprintId(
            ref as EntryBlueprintIdRef,
            entryId,
          ),
          from: entryBlueprintIdProvider,
          name: r'entryBlueprintIdProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryBlueprintIdHash,
          dependencies: EntryBlueprintIdFamily._dependencies,
          allTransitiveDependencies:
              EntryBlueprintIdFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryBlueprintIdProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    String? Function(EntryBlueprintIdRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryBlueprintIdProvider._internal(
        (ref) => create(ref as EntryBlueprintIdRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<String?> createElement() {
    return _EntryBlueprintIdProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryBlueprintIdProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryBlueprintIdRef on AutoDisposeProviderRef<String?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryBlueprintIdProviderElement
    extends AutoDisposeProviderElement<String?> with EntryBlueprintIdRef {
  _EntryBlueprintIdProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryBlueprintIdProvider).entryId;
}

String _$isEntryDeprecatedHash() => r'f7417cbbc63faac9f66342e4170f30e7466c646a';

/// See also [isEntryDeprecated].
@ProviderFor(isEntryDeprecated)
const isEntryDeprecatedProvider = IsEntryDeprecatedFamily();

/// See also [isEntryDeprecated].
class IsEntryDeprecatedFamily extends Family<bool> {
  /// See also [isEntryDeprecated].
  const IsEntryDeprecatedFamily();

  /// See also [isEntryDeprecated].
  IsEntryDeprecatedProvider call(
    String entryId,
  ) {
    return IsEntryDeprecatedProvider(
      entryId,
    );
  }

  @override
  IsEntryDeprecatedProvider getProviderOverride(
    covariant IsEntryDeprecatedProvider provider,
  ) {
    return call(
      provider.entryId,
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
  String? get name => r'isEntryDeprecatedProvider';
}

/// See also [isEntryDeprecated].
class IsEntryDeprecatedProvider extends AutoDisposeProvider<bool> {
  /// See also [isEntryDeprecated].
  IsEntryDeprecatedProvider(
    String entryId,
  ) : this._internal(
          (ref) => isEntryDeprecated(
            ref as IsEntryDeprecatedRef,
            entryId,
          ),
          from: isEntryDeprecatedProvider,
          name: r'isEntryDeprecatedProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$isEntryDeprecatedHash,
          dependencies: IsEntryDeprecatedFamily._dependencies,
          allTransitiveDependencies:
              IsEntryDeprecatedFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  IsEntryDeprecatedProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
  }) : super.internal();

  final String entryId;

  @override
  Override overrideWith(
    bool Function(IsEntryDeprecatedRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: IsEntryDeprecatedProvider._internal(
        (ref) => create(ref as IsEntryDeprecatedRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<bool> createElement() {
    return _IsEntryDeprecatedProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is IsEntryDeprecatedProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin IsEntryDeprecatedRef on AutoDisposeProviderRef<bool> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _IsEntryDeprecatedProviderElement extends AutoDisposeProviderElement<bool>
    with IsEntryDeprecatedRef {
  _IsEntryDeprecatedProviderElement(super.provider);

  @override
  String get entryId => (origin as IsEntryDeprecatedProvider).entryId;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
