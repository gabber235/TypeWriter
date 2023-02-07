// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'page.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_Page _$$_PageFromJson(Map<String, dynamic> json) => _$_Page(
      name: json['name'] as String,
      entries: (json['entries'] as List<dynamic>?)
              ?.map((e) => Entry.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$_PageToJson(_$_Page instance) => <String, dynamic>{
      'name': instance.name,
      'entries': instance.entries,
    };

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

String _$pagesHash() => r'ea54e231325c749b3affb60c0d7c761cce4f78c6';

/// See also [pages].
final pagesProvider = AutoDisposeProvider<List<Page>>(
  pages,
  name: r'pagesProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$pagesHash,
);
typedef PagesRef = AutoDisposeProviderRef<List<Page>>;
String _$pageHash() => r'cac5f933a02820136d00b486570ced2b2a399fcd';

/// See also [page].
class PageProvider extends AutoDisposeProvider<Page?> {
  PageProvider(
    this.name,
  ) : super(
          (ref) => page(
            ref,
            name,
          ),
          from: pageProvider,
          name: r'pageProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product') ? null : _$pageHash,
        );

  final String name;

  @override
  bool operator ==(Object other) {
    return other is PageProvider && other.name == name;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, name.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef PageRef = AutoDisposeProviderRef<Page?>;

/// See also [page].
final pageProvider = PageFamily();

class PageFamily extends Family<Page?> {
  PageFamily();

  PageProvider call(
    String name,
  ) {
    return PageProvider(
      name,
    );
  }

  @override
  AutoDisposeProvider<Page?> getProviderOverride(
    covariant PageProvider provider,
  ) {
    return call(
      provider.name,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'pageProvider';
}

String _$entriesPageHash() => r'722059484c7e53a0839547133217a6615f37bc6d';

/// See also [entriesPage].
class EntriesPageProvider extends AutoDisposeProvider<String?> {
  EntriesPageProvider(
    this.entryId,
  ) : super(
          (ref) => entriesPage(
            ref,
            entryId,
          ),
          from: entriesPageProvider,
          name: r'entriesPageProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entriesPageHash,
        );

  final String entryId;

  @override
  bool operator ==(Object other) {
    return other is EntriesPageProvider && other.entryId == entryId;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, entryId.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef EntriesPageRef = AutoDisposeProviderRef<String?>;

/// See also [entriesPage].
final entriesPageProvider = EntriesPageFamily();

class EntriesPageFamily extends Family<String?> {
  EntriesPageFamily();

  EntriesPageProvider call(
    String entryId,
  ) {
    return EntriesPageProvider(
      entryId,
    );
  }

  @override
  AutoDisposeProvider<String?> getProviderOverride(
    covariant EntriesPageProvider provider,
  ) {
    return call(
      provider.entryId,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'entriesPageProvider';
}

String _$entryHash() => r'f1e00a6ad6ab8e50c1e2178680c8004c98dea055';

/// See also [entry].
class EntryProvider extends AutoDisposeProvider<Entry?> {
  EntryProvider(
    this.pageId,
    this.entryId,
  ) : super(
          (ref) => entry(
            ref,
            pageId,
            entryId,
          ),
          from: entryProvider,
          name: r'entryProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$entryHash,
        );

  final String pageId;
  final String entryId;

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

typedef EntryRef = AutoDisposeProviderRef<Entry?>;

/// See also [entry].
final entryProvider = EntryFamily();

class EntryFamily extends Family<Entry?> {
  EntryFamily();

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
  AutoDisposeProvider<Entry?> getProviderOverride(
    covariant EntryProvider provider,
  ) {
    return call(
      provider.pageId,
      provider.entryId,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'entryProvider';
}

String _$globalEntryHash() => r'9aa2e0f379f22d9d87dee900fe3122f81698fac3';

/// See also [globalEntry].
class GlobalEntryProvider extends AutoDisposeProvider<Entry?> {
  GlobalEntryProvider(
    this.entryId,
  ) : super(
          (ref) => globalEntry(
            ref,
            entryId,
          ),
          from: globalEntryProvider,
          name: r'globalEntryProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$globalEntryHash,
        );

  final String entryId;

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

typedef GlobalEntryRef = AutoDisposeProviderRef<Entry?>;

/// See also [globalEntry].
final globalEntryProvider = GlobalEntryFamily();

class GlobalEntryFamily extends Family<Entry?> {
  GlobalEntryFamily();

  GlobalEntryProvider call(
    String entryId,
  ) {
    return GlobalEntryProvider(
      entryId,
    );
  }

  @override
  AutoDisposeProvider<Entry?> getProviderOverride(
    covariant GlobalEntryProvider provider,
  ) {
    return call(
      provider.entryId,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'globalEntryProvider';
}

String _$globalEntryWithPageHash() =>
    r'250ed7d1b0146d138e1237d22c12a53e00a7b0b9';

/// See also [globalEntryWithPage].
class GlobalEntryWithPageProvider
    extends AutoDisposeProvider<MapEntry<String, Entry>?> {
  GlobalEntryWithPageProvider(
    this.entryId,
  ) : super(
          (ref) => globalEntryWithPage(
            ref,
            entryId,
          ),
          from: globalEntryWithPageProvider,
          name: r'globalEntryWithPageProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$globalEntryWithPageHash,
        );

  final String entryId;

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

typedef GlobalEntryWithPageRef
    = AutoDisposeProviderRef<MapEntry<String, Entry>?>;

/// See also [globalEntryWithPage].
final globalEntryWithPageProvider = GlobalEntryWithPageFamily();

class GlobalEntryWithPageFamily extends Family<MapEntry<String, Entry>?> {
  GlobalEntryWithPageFamily();

  GlobalEntryWithPageProvider call(
    String entryId,
  ) {
    return GlobalEntryWithPageProvider(
      entryId,
    );
  }

  @override
  AutoDisposeProvider<MapEntry<String, Entry>?> getProviderOverride(
    covariant GlobalEntryWithPageProvider provider,
  ) {
    return call(
      provider.entryId,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'globalEntryWithPageProvider';
}
