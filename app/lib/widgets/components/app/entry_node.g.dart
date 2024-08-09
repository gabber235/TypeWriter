// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'entry_node.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$linkablePathsHash() => r'884f9f6cc3e0c6e6c97dba3088c2d824ff2c1be4';

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

/// See also [linkablePaths].
@ProviderFor(linkablePaths)
const linkablePathsProvider = LinkablePathsFamily();

/// See also [linkablePaths].
class LinkablePathsFamily extends Family<List<String>> {
  /// See also [linkablePaths].
  const LinkablePathsFamily();

  /// See also [linkablePaths].
  LinkablePathsProvider call(
    String entryId,
  ) {
    return LinkablePathsProvider(
      entryId,
    );
  }

  @override
  LinkablePathsProvider getProviderOverride(
    covariant LinkablePathsProvider provider,
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
  String? get name => r'linkablePathsProvider';
}

/// See also [linkablePaths].
class LinkablePathsProvider extends AutoDisposeProvider<List<String>> {
  /// See also [linkablePaths].
  LinkablePathsProvider(
    String entryId,
  ) : this._internal(
          (ref) => linkablePaths(
            ref as LinkablePathsRef,
            entryId,
          ),
          from: linkablePathsProvider,
          name: r'linkablePathsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$linkablePathsHash,
          dependencies: LinkablePathsFamily._dependencies,
          allTransitiveDependencies:
              LinkablePathsFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  LinkablePathsProvider._internal(
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
    List<String> Function(LinkablePathsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: LinkablePathsProvider._internal(
        (ref) => create(ref as LinkablePathsRef),
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
  AutoDisposeProviderElement<List<String>> createElement() {
    return _LinkablePathsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is LinkablePathsProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin LinkablePathsRef on AutoDisposeProviderRef<List<String>> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _LinkablePathsProviderElement
    extends AutoDisposeProviderElement<List<String>> with LinkablePathsRef {
  _LinkablePathsProviderElement(super.provider);

  @override
  String get entryId => (origin as LinkablePathsProvider).entryId;
}

String _$linkableDuplicatePathsHash() =>
    r'b2c2a0a1159773985a764adf969dbb3706ceb56a';

/// See also [linkableDuplicatePaths].
@ProviderFor(linkableDuplicatePaths)
const linkableDuplicatePathsProvider = LinkableDuplicatePathsFamily();

/// See also [linkableDuplicatePaths].
class LinkableDuplicatePathsFamily extends Family<List<String>> {
  /// See also [linkableDuplicatePaths].
  const LinkableDuplicatePathsFamily();

  /// See also [linkableDuplicatePaths].
  LinkableDuplicatePathsProvider call(
    String entryId,
  ) {
    return LinkableDuplicatePathsProvider(
      entryId,
    );
  }

  @override
  LinkableDuplicatePathsProvider getProviderOverride(
    covariant LinkableDuplicatePathsProvider provider,
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
  String? get name => r'linkableDuplicatePathsProvider';
}

/// See also [linkableDuplicatePaths].
class LinkableDuplicatePathsProvider extends AutoDisposeProvider<List<String>> {
  /// See also [linkableDuplicatePaths].
  LinkableDuplicatePathsProvider(
    String entryId,
  ) : this._internal(
          (ref) => linkableDuplicatePaths(
            ref as LinkableDuplicatePathsRef,
            entryId,
          ),
          from: linkableDuplicatePathsProvider,
          name: r'linkableDuplicatePathsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$linkableDuplicatePathsHash,
          dependencies: LinkableDuplicatePathsFamily._dependencies,
          allTransitiveDependencies:
              LinkableDuplicatePathsFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  LinkableDuplicatePathsProvider._internal(
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
    List<String> Function(LinkableDuplicatePathsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: LinkableDuplicatePathsProvider._internal(
        (ref) => create(ref as LinkableDuplicatePathsRef),
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
  AutoDisposeProviderElement<List<String>> createElement() {
    return _LinkableDuplicatePathsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is LinkableDuplicatePathsProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin LinkableDuplicatePathsRef on AutoDisposeProviderRef<List<String>> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _LinkableDuplicatePathsProviderElement
    extends AutoDisposeProviderElement<List<String>>
    with LinkableDuplicatePathsRef {
  _LinkableDuplicatePathsProviderElement(super.provider);

  @override
  String get entryId => (origin as LinkableDuplicatePathsProvider).entryId;
}

String _$acceptingPathsHash() => r'76724dcc043808279bdf1f4aa92687fe87f54446';

/// See also [_acceptingPaths].
@ProviderFor(_acceptingPaths)
const _acceptingPathsProvider = _AcceptingPathsFamily();

/// See also [_acceptingPaths].
class _AcceptingPathsFamily extends Family<List<String>> {
  /// See also [_acceptingPaths].
  const _AcceptingPathsFamily();

  /// See also [_acceptingPaths].
  _AcceptingPathsProvider call(
    String entryId,
    String targetId,
  ) {
    return _AcceptingPathsProvider(
      entryId,
      targetId,
    );
  }

  @override
  _AcceptingPathsProvider getProviderOverride(
    covariant _AcceptingPathsProvider provider,
  ) {
    return call(
      provider.entryId,
      provider.targetId,
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
  String? get name => r'_acceptingPathsProvider';
}

/// See also [_acceptingPaths].
class _AcceptingPathsProvider extends AutoDisposeProvider<List<String>> {
  /// See also [_acceptingPaths].
  _AcceptingPathsProvider(
    String entryId,
    String targetId,
  ) : this._internal(
          (ref) => _acceptingPaths(
            ref as _AcceptingPathsRef,
            entryId,
            targetId,
          ),
          from: _acceptingPathsProvider,
          name: r'_acceptingPathsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$acceptingPathsHash,
          dependencies: _AcceptingPathsFamily._dependencies,
          allTransitiveDependencies:
              _AcceptingPathsFamily._allTransitiveDependencies,
          entryId: entryId,
          targetId: targetId,
        );

  _AcceptingPathsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.entryId,
    required this.targetId,
  }) : super.internal();

  final String entryId;
  final String targetId;

  @override
  Override overrideWith(
    List<String> Function(_AcceptingPathsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _AcceptingPathsProvider._internal(
        (ref) => create(ref as _AcceptingPathsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        entryId: entryId,
        targetId: targetId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<List<String>> createElement() {
    return _AcceptingPathsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _AcceptingPathsProvider &&
        other.entryId == entryId &&
        other.targetId == targetId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);
    hash = _SystemHash.combine(hash, targetId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _AcceptingPathsRef on AutoDisposeProviderRef<List<String>> {
  /// The parameter `entryId` of this provider.
  String get entryId;

  /// The parameter `targetId` of this provider.
  String get targetId;
}

class _AcceptingPathsProviderElement
    extends AutoDisposeProviderElement<List<String>> with _AcceptingPathsRef {
  _AcceptingPathsProviderElement(super.provider);

  @override
  String get entryId => (origin as _AcceptingPathsProvider).entryId;
  @override
  String get targetId => (origin as _AcceptingPathsProvider).targetId;
}

String _$writersHash() => r'991a539369dd5e8d952a9bd955779c6a4e65c69f';

/// See also [_writers].
@ProviderFor(_writers)
const _writersProvider = _WritersFamily();

/// See also [_writers].
class _WritersFamily extends Family<List<Writer>> {
  /// See also [_writers].
  const _WritersFamily();

  /// See also [_writers].
  _WritersProvider call(
    String id,
  ) {
    return _WritersProvider(
      id,
    );
  }

  @override
  _WritersProvider getProviderOverride(
    covariant _WritersProvider provider,
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
  String? get name => r'_writersProvider';
}

/// See also [_writers].
class _WritersProvider extends AutoDisposeProvider<List<Writer>> {
  /// See also [_writers].
  _WritersProvider(
    String id,
  ) : this._internal(
          (ref) => _writers(
            ref as _WritersRef,
            id,
          ),
          from: _writersProvider,
          name: r'_writersProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$writersHash,
          dependencies: _WritersFamily._dependencies,
          allTransitiveDependencies: _WritersFamily._allTransitiveDependencies,
          id: id,
        );

  _WritersProvider._internal(
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
    List<Writer> Function(_WritersRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: _WritersProvider._internal(
        (ref) => create(ref as _WritersRef),
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
  AutoDisposeProviderElement<List<Writer>> createElement() {
    return _WritersProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is _WritersProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin _WritersRef on AutoDisposeProviderRef<List<Writer>> {
  /// The parameter `id` of this provider.
  String get id;
}

class _WritersProviderElement extends AutoDisposeProviderElement<List<Writer>>
    with _WritersRef {
  _WritersProviderElement(super.provider);

  @override
  String get id => (origin as _WritersProvider).id;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
