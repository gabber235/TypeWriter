// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entry.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$entryDefinitionHash() => r'5cd9fa81725650c8aedfcf39f5cb1f767433e4f1';

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
    String pageId,
    String entryId,
  ) {
    return EntryDefinitionProvider(
      pageId,
      entryId,
    );
  }

  @override
  EntryDefinitionProvider getProviderOverride(
    covariant EntryDefinitionProvider provider,
  ) {
    return call(
      provider.pageId,
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
    String pageId,
    String entryId,
  ) : this._internal(
          (ref) => entryDefinition(
            ref as EntryDefinitionRef,
            pageId,
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
          pageId: pageId,
          entryId: entryId,
        );

  EntryDefinitionProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageId,
    required this.entryId,
  }) : super.internal();

  final String pageId;
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
        pageId: pageId,
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
    return other is EntryDefinitionProvider &&
        other.pageId == pageId &&
        other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryDefinitionRef on AutoDisposeProviderRef<EntryDefinition?> {
  /// The parameter `pageId` of this provider.
  String get pageId;

  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryDefinitionProviderElement
    extends AutoDisposeProviderElement<EntryDefinition?>
    with EntryDefinitionRef {
  _EntryDefinitionProviderElement(super.provider);

  @override
  String get pageId => (origin as EntryDefinitionProvider).pageId;
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

String _$entryTypeHash() => r'467b4a9545b746b07f721913e6c480ee100eedb1';

/// See also [entryType].
@ProviderFor(entryType)
const entryTypeProvider = EntryTypeFamily();

/// See also [entryType].
class EntryTypeFamily extends Family<String?> {
  /// See also [entryType].
  const EntryTypeFamily();

  /// See also [entryType].
  EntryTypeProvider call(
    String entryId,
  ) {
    return EntryTypeProvider(
      entryId,
    );
  }

  @override
  EntryTypeProvider getProviderOverride(
    covariant EntryTypeProvider provider,
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
  String? get name => r'entryTypeProvider';
}

/// See also [entryType].
class EntryTypeProvider extends AutoDisposeProvider<String?> {
  /// See also [entryType].
  EntryTypeProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryType(
            ref as EntryTypeRef,
            entryId,
          ),
          from: entryTypeProvider,
          name: r'entryTypeProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryTypeHash,
          dependencies: EntryTypeFamily._dependencies,
          allTransitiveDependencies: EntryTypeFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryTypeProvider._internal(
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
    String? Function(EntryTypeRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryTypeProvider._internal(
        (ref) => create(ref as EntryTypeRef),
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
    return _EntryTypeProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryTypeProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryTypeRef on AutoDisposeProviderRef<String?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryTypeProviderElement extends AutoDisposeProviderElement<String?>
    with EntryTypeRef {
  _EntryTypeProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryTypeProvider).entryId;
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
