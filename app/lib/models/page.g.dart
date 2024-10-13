// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'page.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$PageImpl _$$PageImplFromJson(Map<String, dynamic> json) => _$PageImpl(
      id: json['id'] as String,
      pageName: json['name'] as String,
      type: $enumDecode(_$PageTypeEnumMap, json['type']),
      entries: (json['entries'] as List<dynamic>?)
              ?.map((e) => Entry.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      chapter: json['chapter'] as String? ?? "",
      priority: (json['priority'] as num?)?.toInt() ?? 0,
    );

Map<String, dynamic> _$$PageImplToJson(_$PageImpl instance) =>
    <String, dynamic>{
      'id': instance.id,
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
String _$pageHash() => r'884d7b74470acd1d1cd7aefeb91f51364a14ba89';

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
    String pageId,
  ) {
    return PageProvider(
      pageId,
    );
  }

  @override
  PageProvider getProviderOverride(
    covariant PageProvider provider,
  ) {
    return call(
      provider.pageId,
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
    String pageId,
  ) : this._internal(
          (ref) => page(
            ref as PageRef,
            pageId,
          ),
          from: pageProvider,
          name: r'pageProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product') ? null : _$pageHash,
          dependencies: PageFamily._dependencies,
          allTransitiveDependencies: PageFamily._allTransitiveDependencies,
          pageId: pageId,
        );

  PageProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageId,
  }) : super.internal();

  final String pageId;

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
        pageId: pageId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<Page?> createElement() {
    return _PageProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageProvider && other.pageId == pageId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageRef on AutoDisposeProviderRef<Page?> {
  /// The parameter `pageId` of this provider.
  String get pageId;
}

class _PageProviderElement extends AutoDisposeProviderElement<Page?>
    with PageRef {
  _PageProviderElement(super.provider);

  @override
  String get pageId => (origin as PageProvider).pageId;
}

String _$pageNameHash() => r'c1089bcbebabddfc3bb11a9ba52b4019a761cd11';

/// See also [pageName].
@ProviderFor(pageName)
const pageNameProvider = PageNameFamily();

/// See also [pageName].
class PageNameFamily extends Family<String?> {
  /// See also [pageName].
  const PageNameFamily();

  /// See also [pageName].
  PageNameProvider call(
    String pageId,
  ) {
    return PageNameProvider(
      pageId,
    );
  }

  @override
  PageNameProvider getProviderOverride(
    covariant PageNameProvider provider,
  ) {
    return call(
      provider.pageId,
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
  String? get name => r'pageNameProvider';
}

/// See also [pageName].
class PageNameProvider extends AutoDisposeProvider<String?> {
  /// See also [pageName].
  PageNameProvider(
    String pageId,
  ) : this._internal(
          (ref) => pageName(
            ref as PageNameRef,
            pageId,
          ),
          from: pageNameProvider,
          name: r'pageNameProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pageNameHash,
          dependencies: PageNameFamily._dependencies,
          allTransitiveDependencies: PageNameFamily._allTransitiveDependencies,
          pageId: pageId,
        );

  PageNameProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageId,
  }) : super.internal();

  final String pageId;

  @override
  Override overrideWith(
    String? Function(PageNameRef provider) create,
  ) {
    return ProviderOverride(
      origin: this,
      override: PageNameProvider._internal(
        (ref) => create(ref as PageNameRef),
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        pageId: pageId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<String?> createElement() {
    return _PageNameProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageNameProvider && other.pageId == pageId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageNameRef on AutoDisposeProviderRef<String?> {
  /// The parameter `pageId` of this provider.
  String get pageId;
}

class _PageNameProviderElement extends AutoDisposeProviderElement<String?>
    with PageNameRef {
  _PageNameProviderElement(super.provider);

  @override
  String get pageId => (origin as PageNameProvider).pageId;
}

String _$pageExistsHash() => r'68e3ea46a1e212bb9bc01ac653a749e9668d47f3';

/// See also [pageExists].
@ProviderFor(pageExists)
const pageExistsProvider = PageExistsFamily();

/// See also [pageExists].
class PageExistsFamily extends Family<bool> {
  /// See also [pageExists].
  const PageExistsFamily();

  /// See also [pageExists].
  PageExistsProvider call(
    String pageId,
  ) {
    return PageExistsProvider(
      pageId,
    );
  }

  @override
  PageExistsProvider getProviderOverride(
    covariant PageExistsProvider provider,
  ) {
    return call(
      provider.pageId,
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
    String pageId,
  ) : this._internal(
          (ref) => pageExists(
            ref as PageExistsRef,
            pageId,
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
          pageId: pageId,
        );

  PageExistsProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageId,
  }) : super.internal();

  final String pageId;

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
        pageId: pageId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<bool> createElement() {
    return _PageExistsProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageExistsProvider && other.pageId == pageId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageExistsRef on AutoDisposeProviderRef<bool> {
  /// The parameter `pageId` of this provider.
  String get pageId;
}

class _PageExistsProviderElement extends AutoDisposeProviderElement<bool>
    with PageExistsRef {
  _PageExistsProviderElement(super.provider);

  @override
  String get pageId => (origin as PageExistsProvider).pageId;
}

String _$pageTypeHash() => r'e22516e95c00d6723256e425e31624dc3beccddb';

/// See also [pageType].
@ProviderFor(pageType)
const pageTypeProvider = PageTypeFamily();

/// See also [pageType].
class PageTypeFamily extends Family<PageType> {
  /// See also [pageType].
  const PageTypeFamily();

  /// See also [pageType].
  PageTypeProvider call(
    String pageId,
  ) {
    return PageTypeProvider(
      pageId,
    );
  }

  @override
  PageTypeProvider getProviderOverride(
    covariant PageTypeProvider provider,
  ) {
    return call(
      provider.pageId,
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
    String pageId,
  ) : this._internal(
          (ref) => pageType(
            ref as PageTypeRef,
            pageId,
          ),
          from: pageTypeProvider,
          name: r'pageTypeProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$pageTypeHash,
          dependencies: PageTypeFamily._dependencies,
          allTransitiveDependencies: PageTypeFamily._allTransitiveDependencies,
          pageId: pageId,
        );

  PageTypeProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageId,
  }) : super.internal();

  final String pageId;

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
        pageId: pageId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<PageType> createElement() {
    return _PageTypeProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageTypeProvider && other.pageId == pageId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageTypeRef on AutoDisposeProviderRef<PageType> {
  /// The parameter `pageId` of this provider.
  String get pageId;
}

class _PageTypeProviderElement extends AutoDisposeProviderElement<PageType>
    with PageTypeRef {
  _PageTypeProviderElement(super.provider);

  @override
  String get pageId => (origin as PageTypeProvider).pageId;
}

String _$pageChapterHash() => r'c8725780b13e27c6bfb7ade62625aabb4d7ca392';

/// See also [pageChapter].
@ProviderFor(pageChapter)
const pageChapterProvider = PageChapterFamily();

/// See also [pageChapter].
class PageChapterFamily extends Family<String> {
  /// See also [pageChapter].
  const PageChapterFamily();

  /// See also [pageChapter].
  PageChapterProvider call(
    String pageId,
  ) {
    return PageChapterProvider(
      pageId,
    );
  }

  @override
  PageChapterProvider getProviderOverride(
    covariant PageChapterProvider provider,
  ) {
    return call(
      provider.pageId,
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
    String pageId,
  ) : this._internal(
          (ref) => pageChapter(
            ref as PageChapterRef,
            pageId,
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
          pageId: pageId,
        );

  PageChapterProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageId,
  }) : super.internal();

  final String pageId;

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
        pageId: pageId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<String> createElement() {
    return _PageChapterProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PageChapterProvider && other.pageId == pageId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PageChapterRef on AutoDisposeProviderRef<String> {
  /// The parameter `pageId` of this provider.
  String get pageId;
}

class _PageChapterProviderElement extends AutoDisposeProviderElement<String>
    with PageChapterRef {
  _PageChapterProviderElement(super.provider);

  @override
  String get pageId => (origin as PageChapterProvider).pageId;
}

String _$pagePriorityHash() => r'340f23a4c9b80bbd159ea066d79044424a8e4692';

/// See also [pagePriority].
@ProviderFor(pagePriority)
const pagePriorityProvider = PagePriorityFamily();

/// See also [pagePriority].
class PagePriorityFamily extends Family<int> {
  /// See also [pagePriority].
  const PagePriorityFamily();

  /// See also [pagePriority].
  PagePriorityProvider call(
    String pageId,
  ) {
    return PagePriorityProvider(
      pageId,
    );
  }

  @override
  PagePriorityProvider getProviderOverride(
    covariant PagePriorityProvider provider,
  ) {
    return call(
      provider.pageId,
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
    String pageId,
  ) : this._internal(
          (ref) => pagePriority(
            ref as PagePriorityRef,
            pageId,
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
          pageId: pageId,
        );

  PagePriorityProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.pageId,
  }) : super.internal();

  final String pageId;

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
        pageId: pageId,
      ),
    );
  }

  @override
  AutoDisposeProviderElement<int> createElement() {
    return _PagePriorityProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is PagePriorityProvider && other.pageId == pageId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, pageId.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin PagePriorityRef on AutoDisposeProviderRef<int> {
  /// The parameter `pageId` of this provider.
  String get pageId;
}

class _PagePriorityProviderElement extends AutoDisposeProviderElement<int>
    with PagePriorityRef {
  _PagePriorityProviderElement(super.provider);

  @override
  String get pageId => (origin as PagePriorityProvider).pageId;
}

String _$entryPageIdHash() => r'a317d09f9de338ebefec45be649af4dc1c60a22d';

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

String _$globalEntryHash() => r'f304c143cc53b98eeccb7696c4c1152464820b93';

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
