// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'select_entries.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$isSelectingEntriesHash() =>
    r'7dbeb22f747ea2196261c6c276fb6c0985d291a8';

/// See also [isSelectingEntries].
@ProviderFor(isSelectingEntries)
final isSelectingEntriesProvider = AutoDisposeProvider<bool>.internal(
  isSelectingEntries,
  name: r'isSelectingEntriesProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$isSelectingEntriesHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef IsSelectingEntriesRef = AutoDisposeProviderRef<bool>;
String _$hasEntryInSelectionHash() =>
    r'37d3f99d551b6c262d5482dab88037f89f214ce1';

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

/// See also [hasEntryInSelection].
@ProviderFor(hasEntryInSelection)
const hasEntryInSelectionProvider = HasEntryInSelectionFamily();

/// See also [hasEntryInSelection].
class HasEntryInSelectionFamily extends Family<bool> {
  /// See also [hasEntryInSelection].
  const HasEntryInSelectionFamily();

  /// See also [hasEntryInSelection].
  HasEntryInSelectionProvider call(
    String id,
  ) {
    return HasEntryInSelectionProvider(
      id,
    );
  }

  @override
  HasEntryInSelectionProvider getProviderOverride(
    covariant HasEntryInSelectionProvider provider,
  ) {
    return call(
      provider.id,
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
  String? get name => r'hasEntryInSelectionProvider';
}

/// See also [hasEntryInSelection].
class HasEntryInSelectionProvider extends AutoDisposeProvider<bool> {
  /// See also [hasEntryInSelection].
  HasEntryInSelectionProvider(
    String id,
  ) : this._internal(
          (ref) => hasEntryInSelection(
            ref as HasEntryInSelectionRef,
            id,
          ),
          from: hasEntryInSelectionProvider,
          name: r'hasEntryInSelectionProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$hasEntryInSelectionHash,
          dependencies: HasEntryInSelectionFamily._dependencies,
          allTransitiveDependencies:
              HasEntryInSelectionFamily._allTransitiveDependencies,
          id: id,
        );

  HasEntryInSelectionProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.id,
  }) : super.internal();

  final String id;

  @override
  Override overrideWith(
    bool Function(HasEntryInSelectionRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: HasEntryInSelectionProvider._internal(
        (ref) => create(ref as HasEntryInSelectionRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        id: id,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<bool> createElement() {
    return _HasEntryInSelectionProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is HasEntryInSelectionProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin HasEntryInSelectionRef on AutoDisposeProviderRef<bool> {
  /// The parameter `id` of this provider.
  String get id;
}

class _HasEntryInSelectionProviderElement
    extends AutoDisposeProviderElement<bool> with HasEntryInSelectionRef {
  _HasEntryInSelectionProviderElement(super.provider);

  @override
  String get id => (origin as HasEntryInSelectionProvider).id;
}

String _$selectingTagHash() => r'e22363af393fbc3ddb3ad07ab830c863da440dfa';

/// See also [selectingTag].
@ProviderFor(selectingTag)
final selectingTagProvider = AutoDisposeProvider<String>.internal(
  selectingTag,
  name: r'selectingTagProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$selectingTagHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef SelectingTagRef = AutoDisposeProviderRef<String>;
String _$canSelectEntryHash() => r'071b3e46373b5397bd8d56e24e309cce442f5624';

/// See also [canSelectEntry].
@ProviderFor(canSelectEntry)
const canSelectEntryProvider = CanSelectEntryFamily();

/// See also [canSelectEntry].
class CanSelectEntryFamily extends Family<bool> {
  /// See also [canSelectEntry].
  const CanSelectEntryFamily();

  /// See also [canSelectEntry].
  CanSelectEntryProvider call(
    String id,
  ) {
    return CanSelectEntryProvider(
      id,
    );
  }

  @override
  CanSelectEntryProvider getProviderOverride(
    covariant CanSelectEntryProvider provider,
  ) {
    return call(
      provider.id,
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
  String? get name => r'canSelectEntryProvider';
}

/// See also [canSelectEntry].
class CanSelectEntryProvider extends AutoDisposeProvider<bool> {
  /// See also [canSelectEntry].
  CanSelectEntryProvider(
    String id,
  ) : this._internal(
          (ref) => canSelectEntry(
            ref as CanSelectEntryRef,
            id,
          ),
          from: canSelectEntryProvider,
          name: r'canSelectEntryProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$canSelectEntryHash,
          dependencies: CanSelectEntryFamily._dependencies,
          allTransitiveDependencies:
              CanSelectEntryFamily._allTransitiveDependencies,
          id: id,
        );

  CanSelectEntryProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.id,
  }) : super.internal();

  final String id;

  @override
  Override overrideWith(
    bool Function(CanSelectEntryRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: CanSelectEntryProvider._internal(
        (ref) => create(ref as CanSelectEntryRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        id: id,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<bool> createElement() {
    return _CanSelectEntryProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is CanSelectEntryProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin CanSelectEntryRef on AutoDisposeProviderRef<bool> {
  /// The parameter `id` of this provider.
  String get id;
}

class _CanSelectEntryProviderElement extends AutoDisposeProviderElement<bool>
    with CanSelectEntryRef {
  _CanSelectEntryProviderElement(super.provider);

  @override
  String get id => (origin as CanSelectEntryProvider).id;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
