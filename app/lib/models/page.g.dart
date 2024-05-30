// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'page.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$PageImpl _$$PageImplFromJson(Map<String, dynamic> json) => _$PageImpl(
      pageName: json['name'] as String,
      type: $enumDecode(_$PageTypeEnumMap, json['type']),
      entries: (json['entries'] as List<dynamic>?)
              ?.map((e) => Entry.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      chapter: json['chapter'] as String? ?? "",
      priority: json['priority'] as int? ?? 0,
    );

Map<String, dynamic> _$$PageImplToJson(_$PageImpl instance) =>
    <String, dynamic>{
      'name': instance.pageName,
      'type': _$PageTypeEnumMap[instance.type]!,
      'entries': instance.entries,
      'chapter': instance.chapter,
      'priority': instance.priority,
    };

const _$PageTypeEnumMap = {
  PageType.sequence: 'sequence',
  PageType.static: 'static',
  PageType.cinematic: 'cinematic',
  PageType.manifest: 'manifest',
};

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$pagesHash() => r'ea54e231325c749b3affb60c0d7c761cce4f78c6';

/// See also [pages].
@ProviderFor(pages)
final pagesProvider = AutoDisposeProvider<List<Page>>.internal(
  pages,
  name: r'pagesProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$pagesHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef PagesRef = AutoDisposeProviderRef<List<Page>>;
String _$pageHash() => r'0c5240a645e0582f10cb6e594f6fd35bb2d48c1a';

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

/// See also [page].
@ProviderFor(page)
const pageProvider = PageFamily();

/// See also [page].
class PageFamily extends Family<Page?> {
  /// See also [page].
  const PageFamily();

  /// See also [page].
  PageProvider call(
    String pageName,
  ) {
    return PageProvider(
      pageName,
    );
  }

  @override
  PageProvider getProviderOverride(
    covariant PageProvider provider,
  ) {
    return call(
      provider.pageName,
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
  String? get name => r'pageProvider';
}

/// See also [page].
class PageProvider extends AutoDisposeProvider<Page?> {
  /// See also [page].
  PageProvider(
    String pageName,
  ) : this._internal(
          (ref) => page(
            ref as PageRef,
            pageName,
          ),
          from: pageProvider,
          name: r'pageProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product') ? null : _$pageHash,
          dependencies: PageFamily._dependencies,
          allTransitiveDependencies: PageFamily._allTransitiveDependencies,
          pageName: pageName,
        );

  PageProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageName,
  }) : super.internal();

  final String pageName;

  @override
  Override overrideWith(
    Page? Function(PageRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: PageProvider._internal(
        (ref) => create(ref as PageRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        pageName: pageName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<Page?> createElement() {
    return _PageProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageProvider && other.pageName == pageName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageRef on AutoDisposeProviderRef<Page?> {
  /// The parameter `pageName` of this provider.
  String get pageName;
}

class _PageProviderElement extends AutoDisposeProviderElement<Page?>
    with PageRef {
  _PageProviderElement(super.provider);

  @override
  String get pageName => (origin as PageProvider).pageName;
}

String _$pageExistsHash() => r'1c42a5607890607b3818f12bb68d98196ccab59d';

/// See also [pageExists].
@ProviderFor(pageExists)
const pageExistsProvider = PageExistsFamily();

/// See also [pageExists].
class PageExistsFamily extends Family<bool> {
  /// See also [pageExists].
  const PageExistsFamily();

  /// See also [pageExists].
  PageExistsProvider call(
    String pageName,
  ) {
    return PageExistsProvider(
      pageName,
    );
  }

  @override
  PageExistsProvider getProviderOverride(
    covariant PageExistsProvider provider,
  ) {
    return call(
      provider.pageName,
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
  String? get name => r'pageExistsProvider';
}

/// See also [pageExists].
class PageExistsProvider extends AutoDisposeProvider<bool> {
  /// See also [pageExists].
  PageExistsProvider(
    String pageName,
  ) : this._internal(
          (ref) => pageExists(
            ref as PageExistsRef,
            pageName,
          ),
          from: pageExistsProvider,
          name: r'pageExistsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pageExistsHash,
          dependencies: PageExistsFamily._dependencies,
          allTransitiveDependencies:
              PageExistsFamily._allTransitiveDependencies,
          pageName: pageName,
        );

  PageExistsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageName,
  }) : super.internal();

  final String pageName;

  @override
  Override overrideWith(
    bool Function(PageExistsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: PageExistsProvider._internal(
        (ref) => create(ref as PageExistsRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        pageName: pageName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<bool> createElement() {
    return _PageExistsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageExistsProvider && other.pageName == pageName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageExistsRef on AutoDisposeProviderRef<bool> {
  /// The parameter `pageName` of this provider.
  String get pageName;
}

class _PageExistsProviderElement extends AutoDisposeProviderElement<bool>
    with PageExistsRef {
  _PageExistsProviderElement(super.provider);

  @override
  String get pageName => (origin as PageExistsProvider).pageName;
}

String _$pageTypeHash() => r'6912833664c34d2689d328d901719f479be512ac';

/// See also [pageType].
@ProviderFor(pageType)
const pageTypeProvider = PageTypeFamily();

/// See also [pageType].
class PageTypeFamily extends Family<PageType> {
  /// See also [pageType].
  const PageTypeFamily();

  /// See also [pageType].
  PageTypeProvider call(
    String pageName,
  ) {
    return PageTypeProvider(
      pageName,
    );
  }

  @override
  PageTypeProvider getProviderOverride(
    covariant PageTypeProvider provider,
  ) {
    return call(
      provider.pageName,
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
  String? get name => r'pageTypeProvider';
}

/// See also [pageType].
class PageTypeProvider extends AutoDisposeProvider<PageType> {
  /// See also [pageType].
  PageTypeProvider(
    String pageName,
  ) : this._internal(
          (ref) => pageType(
            ref as PageTypeRef,
            pageName,
          ),
          from: pageTypeProvider,
          name: r'pageTypeProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pageTypeHash,
          dependencies: PageTypeFamily._dependencies,
          allTransitiveDependencies: PageTypeFamily._allTransitiveDependencies,
          pageName: pageName,
        );

  PageTypeProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageName,
  }) : super.internal();

  final String pageName;

  @override
  Override overrideWith(
    PageType Function(PageTypeRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: PageTypeProvider._internal(
        (ref) => create(ref as PageTypeRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        pageName: pageName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<PageType> createElement() {
    return _PageTypeProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageTypeProvider && other.pageName == pageName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageTypeRef on AutoDisposeProviderRef<PageType> {
  /// The parameter `pageName` of this provider.
  String get pageName;
}

class _PageTypeProviderElement extends AutoDisposeProviderElement<PageType>
    with PageTypeRef {
  _PageTypeProviderElement(super.provider);

  @override
  String get pageName => (origin as PageTypeProvider).pageName;
}

String _$pageChapterHash() => r'9cdd570d02d94300415088471aa61037d66464eb';

/// See also [pageChapter].
@ProviderFor(pageChapter)
const pageChapterProvider = PageChapterFamily();

/// See also [pageChapter].
class PageChapterFamily extends Family<String> {
  /// See also [pageChapter].
  const PageChapterFamily();

  /// See also [pageChapter].
  PageChapterProvider call(
    String pageName,
  ) {
    return PageChapterProvider(
      pageName,
    );
  }

  @override
  PageChapterProvider getProviderOverride(
    covariant PageChapterProvider provider,
  ) {
    return call(
      provider.pageName,
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
  String? get name => r'pageChapterProvider';
}

/// See also [pageChapter].
class PageChapterProvider extends AutoDisposeProvider<String> {
  /// See also [pageChapter].
  PageChapterProvider(
    String pageName,
  ) : this._internal(
          (ref) => pageChapter(
            ref as PageChapterRef,
            pageName,
          ),
          from: pageChapterProvider,
          name: r'pageChapterProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pageChapterHash,
          dependencies: PageChapterFamily._dependencies,
          allTransitiveDependencies:
              PageChapterFamily._allTransitiveDependencies,
          pageName: pageName,
        );

  PageChapterProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageName,
  }) : super.internal();

  final String pageName;

  @override
  Override overrideWith(
    String Function(PageChapterRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: PageChapterProvider._internal(
        (ref) => create(ref as PageChapterRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        pageName: pageName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<String> createElement() {
    return _PageChapterProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageChapterProvider && other.pageName == pageName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageChapterRef on AutoDisposeProviderRef<String> {
  /// The parameter `pageName` of this provider.
  String get pageName;
}

class _PageChapterProviderElement extends AutoDisposeProviderElement<String>
    with PageChapterRef {
  _PageChapterProviderElement(super.provider);

  @override
  String get pageName => (origin as PageChapterProvider).pageName;
}

String _$pagePriorityHash() => r'6d67e3c592c0e5656c4c26213da90d64691825b5';

/// See also [pagePriority].
@ProviderFor(pagePriority)
const pagePriorityProvider = PagePriorityFamily();

/// See also [pagePriority].
class PagePriorityFamily extends Family<int> {
  /// See also [pagePriority].
  const PagePriorityFamily();

  /// See also [pagePriority].
  PagePriorityProvider call(
    String pageName,
  ) {
    return PagePriorityProvider(
      pageName,
    );
  }

  @override
  PagePriorityProvider getProviderOverride(
    covariant PagePriorityProvider provider,
  ) {
    return call(
      provider.pageName,
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
  String? get name => r'pagePriorityProvider';
}

/// See also [pagePriority].
class PagePriorityProvider extends AutoDisposeProvider<int> {
  /// See also [pagePriority].
  PagePriorityProvider(
    String pageName,
  ) : this._internal(
          (ref) => pagePriority(
            ref as PagePriorityRef,
            pageName,
          ),
          from: pagePriorityProvider,
          name: r'pagePriorityProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pagePriorityHash,
          dependencies: PagePriorityFamily._dependencies,
          allTransitiveDependencies:
              PagePriorityFamily._allTransitiveDependencies,
          pageName: pageName,
        );

  PagePriorityProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageName,
  }) : super.internal();

  final String pageName;

  @override
  Override overrideWith(
    int Function(PagePriorityRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: PagePriorityProvider._internal(
        (ref) => create(ref as PagePriorityRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        pageName: pageName,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<int> createElement() {
    return _PagePriorityProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PagePriorityProvider && other.pageName == pageName;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageName.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PagePriorityRef on AutoDisposeProviderRef<int> {
  /// The parameter `pageName` of this provider.
  String get pageName;
}

class _PagePriorityProviderElement extends AutoDisposeProviderElement<int>
    with PagePriorityRef {
  _PagePriorityProviderElement(super.provider);

  @override
  String get pageName => (origin as PagePriorityProvider).pageName;
}

String _$entryPageIdHash() => r'ceb41cef3f8d8b1f0a28e5de7aead8969f03d6ac';

/// See also [entryPageId].
@ProviderFor(entryPageId)
const entryPageIdProvider = EntryPageIdFamily();

/// See also [entryPageId].
class EntryPageIdFamily extends Family<String?> {
  /// See also [entryPageId].
  const EntryPageIdFamily();

  /// See also [entryPageId].
  EntryPageIdProvider call(
    String entryId,
  ) {
    return EntryPageIdProvider(
      entryId,
    );
  }

  @override
  EntryPageIdProvider getProviderOverride(
    covariant EntryPageIdProvider provider,
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
  String? get name => r'entryPageIdProvider';
}

/// See also [entryPageId].
class EntryPageIdProvider extends AutoDisposeProvider<String?> {
  /// See also [entryPageId].
  EntryPageIdProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryPageId(
            ref as EntryPageIdRef,
            entryId,
          ),
          from: entryPageIdProvider,
          name: r'entryPageIdProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryPageIdHash,
          dependencies: EntryPageIdFamily._dependencies,
          allTransitiveDependencies:
              EntryPageIdFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryPageIdProvider._internal(
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
    String? Function(EntryPageIdRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryPageIdProvider._internal(
        (ref) => create(ref as EntryPageIdRef),
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
    return _EntryPageIdProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryPageIdProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryPageIdRef on AutoDisposeProviderRef<String?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryPageIdProviderElement extends AutoDisposeProviderElement<String?>
    with EntryPageIdRef {
  _EntryPageIdProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryPageIdProvider).entryId;
}

String _$entryPageHash() => r'00d12a29c4a1bfdec76651cba6577d582e65e20a';

/// See also [entryPage].
@ProviderFor(entryPage)
const entryPageProvider = EntryPageFamily();

/// See also [entryPage].
class EntryPageFamily extends Family<Page?> {
  /// See also [entryPage].
  const EntryPageFamily();

  /// See also [entryPage].
  EntryPageProvider call(
    String entryId,
  ) {
    return EntryPageProvider(
      entryId,
    );
  }

  @override
  EntryPageProvider getProviderOverride(
    covariant EntryPageProvider provider,
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
  String? get name => r'entryPageProvider';
}

/// See also [entryPage].
class EntryPageProvider extends AutoDisposeProvider<Page?> {
  /// See also [entryPage].
  EntryPageProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryPage(
            ref as EntryPageRef,
            entryId,
          ),
          from: entryPageProvider,
          name: r'entryPageProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryPageHash,
          dependencies: EntryPageFamily._dependencies,
          allTransitiveDependencies: EntryPageFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryPageProvider._internal(
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
    Page? Function(EntryPageRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryPageProvider._internal(
        (ref) => create(ref as EntryPageRef),
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
  AutoDisposeProviderElement<Page?> createElement() {
    return _EntryPageProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryPageProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryPageRef on AutoDisposeProviderRef<Page?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryPageProviderElement extends AutoDisposeProviderElement<Page?>
    with EntryPageRef {
  _EntryPageProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryPageProvider).entryId;
}

String _$entryHash() => r'f1e00a6ad6ab8e50c1e2178680c8004c98dea055';

/// See also [entry].
@ProviderFor(entry)
const entryProvider = EntryFamily();

/// See also [entry].
class EntryFamily extends Family<Entry?> {
  /// See also [entry].
  const EntryFamily();

  /// See also [entry].
  EntryProvider call(
    String pageId,
    String entryId,
  ) {
    return EntryProvider(
      pageId,
      entryId,
    );
  }

  @override
  EntryProvider getProviderOverride(
    covariant EntryProvider provider,
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
  String? get name => r'entryProvider';
}

/// See also [entry].
class EntryProvider extends AutoDisposeProvider<Entry?> {
  /// See also [entry].
  EntryProvider(
    String pageId,
    String entryId,
  ) : this._internal(
          (ref) => entry(
            ref as EntryRef,
            pageId,
            entryId,
          ),
          from: entryProvider,
          name: r'entryProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryHash,
          dependencies: EntryFamily._dependencies,
          allTransitiveDependencies: EntryFamily._allTransitiveDependencies,
          pageId: pageId,
          entryId: entryId,
        );

  EntryProvider._internal(
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
    Entry? Function(EntryRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryProvider._internal(
        (ref) => create(ref as EntryRef),
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
  AutoDisposeProviderElement<Entry?> createElement() {
    return _EntryProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryProvider &&
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

mixin EntryRef on AutoDisposeProviderRef<Entry?> {
  /// The parameter `pageId` of this provider.
  String get pageId;

  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryProviderElement extends AutoDisposeProviderElement<Entry?>
    with EntryRef {
  _EntryProviderElement(super.provider);

  @override
  String get pageId => (origin as EntryProvider).pageId;
  @override
  String get entryId => (origin as EntryProvider).entryId;
}

String _$globalEntryHash() => r'df6937f53ba0fe88ed629630eb2b366d2ed3a540';

/// See also [globalEntry].
@ProviderFor(globalEntry)
const globalEntryProvider = GlobalEntryFamily();

/// See also [globalEntry].
class GlobalEntryFamily extends Family<Entry?> {
  /// See also [globalEntry].
  const GlobalEntryFamily();

  /// See also [globalEntry].
  GlobalEntryProvider call(
    String entryId,
  ) {
    return GlobalEntryProvider(
      entryId,
    );
  }

  @override
  GlobalEntryProvider getProviderOverride(
    covariant GlobalEntryProvider provider,
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
  String? get name => r'globalEntryProvider';
}

/// See also [globalEntry].
class GlobalEntryProvider extends AutoDisposeProvider<Entry?> {
  /// See also [globalEntry].
  GlobalEntryProvider(
    String entryId,
  ) : this._internal(
          (ref) => globalEntry(
            ref as GlobalEntryRef,
            entryId,
          ),
          from: globalEntryProvider,
          name: r'globalEntryProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$globalEntryHash,
          dependencies: GlobalEntryFamily._dependencies,
          allTransitiveDependencies:
              GlobalEntryFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  GlobalEntryProvider._internal(
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
    Entry? Function(GlobalEntryRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: GlobalEntryProvider._internal(
        (ref) => create(ref as GlobalEntryRef),
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
  AutoDisposeProviderElement<Entry?> createElement() {
    return _GlobalEntryProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is GlobalEntryProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin GlobalEntryRef on AutoDisposeProviderRef<Entry?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _GlobalEntryProviderElement extends AutoDisposeProviderElement<Entry?>
    with GlobalEntryRef {
  _GlobalEntryProviderElement(super.provider);

  @override
  String get entryId => (origin as GlobalEntryProvider).entryId;
}

String _$globalEntryWithPageHash() =>
    r'003dec3f932daec4549b62ae8bb744dc256310ec';

/// See also [globalEntryWithPage].
@ProviderFor(globalEntryWithPage)
const globalEntryWithPageProvider = GlobalEntryWithPageFamily();

/// See also [globalEntryWithPage].
class GlobalEntryWithPageFamily extends Family<MapEntry<String, Entry>?> {
  /// See also [globalEntryWithPage].
  const GlobalEntryWithPageFamily();

  /// See also [globalEntryWithPage].
  GlobalEntryWithPageProvider call(
    String entryId,
  ) {
    return GlobalEntryWithPageProvider(
      entryId,
    );
  }

  @override
  GlobalEntryWithPageProvider getProviderOverride(
    covariant GlobalEntryWithPageProvider provider,
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
  String? get name => r'globalEntryWithPageProvider';
}

/// See also [globalEntryWithPage].
class GlobalEntryWithPageProvider
    extends AutoDisposeProvider<MapEntry<String, Entry>?> {
  /// See also [globalEntryWithPage].
  GlobalEntryWithPageProvider(
    String entryId,
  ) : this._internal(
          (ref) => globalEntryWithPage(
            ref as GlobalEntryWithPageRef,
            entryId,
          ),
          from: globalEntryWithPageProvider,
          name: r'globalEntryWithPageProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$globalEntryWithPageHash,
          dependencies: GlobalEntryWithPageFamily._dependencies,
          allTransitiveDependencies:
              GlobalEntryWithPageFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  GlobalEntryWithPageProvider._internal(
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
    MapEntry<String, Entry>? Function(GlobalEntryWithPageRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: GlobalEntryWithPageProvider._internal(
        (ref) => create(ref as GlobalEntryWithPageRef),
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
  AutoDisposeProviderElement<MapEntry<String, Entry>?> createElement() {
    return _GlobalEntryWithPageProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is GlobalEntryWithPageProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin GlobalEntryWithPageRef
    on AutoDisposeProviderRef<MapEntry<String, Entry>?> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _GlobalEntryWithPageProviderElement
    extends AutoDisposeProviderElement<MapEntry<String, Entry>?>
    with GlobalEntryWithPageRef {
  _GlobalEntryWithPageProviderElement(super.provider);

  @override
  String get entryId => (origin as GlobalEntryWithPageProvider).entryId;
}

String _$entryExistsHash() => r'5280290c9246fb7003da2717fc9a1639c952a286';

/// See also [entryExists].
@ProviderFor(entryExists)
const entryExistsProvider = EntryExistsFamily();

/// See also [entryExists].
class EntryExistsFamily extends Family<bool> {
  /// See also [entryExists].
  const EntryExistsFamily();

  /// See also [entryExists].
  EntryExistsProvider call(
    String entryId,
  ) {
    return EntryExistsProvider(
      entryId,
    );
  }

  @override
  EntryExistsProvider getProviderOverride(
    covariant EntryExistsProvider provider,
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
  String? get name => r'entryExistsProvider';
}

/// See also [entryExists].
class EntryExistsProvider extends AutoDisposeProvider<bool> {
  /// See also [entryExists].
  EntryExistsProvider(
    String entryId,
  ) : this._internal(
          (ref) => entryExists(
            ref as EntryExistsRef,
            entryId,
          ),
          from: entryExistsProvider,
          name: r'entryExistsProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryExistsHash,
          dependencies: EntryExistsFamily._dependencies,
          allTransitiveDependencies:
              EntryExistsFamily._allTransitiveDependencies,
          entryId: entryId,
        );

  EntryExistsProvider._internal(
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
    bool Function(EntryExistsRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: EntryExistsProvider._internal(
        (ref) => create(ref as EntryExistsRef),
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
    return _EntryExistsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is EntryExistsProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin EntryExistsRef on AutoDisposeProviderRef<bool> {
  /// The parameter `entryId` of this provider.
  String get entryId;
}

class _EntryExistsProviderElement extends AutoDisposeProviderElement<bool>
    with EntryExistsRef {
  _EntryExistsProviderElement(super.provider);

  @override
  String get entryId => (origin as EntryExistsProvider).entryId;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
