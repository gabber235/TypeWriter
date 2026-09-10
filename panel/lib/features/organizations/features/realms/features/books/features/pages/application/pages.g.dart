// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'pages.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(CanonicalBookPages)
final canonicalBookPagesProvider = CanonicalBookPagesFamily._();

final class CanonicalBookPagesProvider
    extends $AsyncNotifierProvider<CanonicalBookPages, List<Page>> {
  CanonicalBookPagesProvider._({
    required CanonicalBookPagesFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'canonicalBookPagesProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$canonicalBookPagesHash();

  @override
  String toString() {
    return r'canonicalBookPagesProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  CanonicalBookPages create() => CanonicalBookPages();

  @override
  bool operator ==(Object other) {
    return other is CanonicalBookPagesProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$canonicalBookPagesHash() =>
    r'ba469908442a6355a0fac21b3d894ee3b97e7dc7';

final class CanonicalBookPagesFamily extends $Family
    with
        $ClassFamilyOverride<
          CanonicalBookPages,
          AsyncValue<List<Page>>,
          List<Page>,
          FutureOr<List<Page>>,
          skir.RecordId
        > {
  CanonicalBookPagesFamily._()
    : super(
        retry: null,
        name: r'canonicalBookPagesProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  CanonicalBookPagesProvider call(skir.RecordId bookId) =>
      CanonicalBookPagesProvider._(argument: bookId, from: this);

  @override
  String toString() => r'canonicalBookPagesProvider';
}

abstract class _$CanonicalBookPages extends $AsyncNotifier<List<Page>> {
  late final _$args = ref.$arg as skir.RecordId;
  skir.RecordId get bookId => _$args;

  FutureOr<List<Page>> build(skir.RecordId bookId);
  @$mustCallSuper
  @override
  WhenComplete runBuild() {
    final ref = this.ref as $Ref<AsyncValue<List<Page>>, List<Page>>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<AsyncValue<List<Page>>, List<Page>>,
              AsyncValue<List<Page>>,
              Object?,
              Object?
            >;
    return element.handleCreate(ref, () => build(_$args));
  }
}

@ProviderFor(CanonicalPage)
final canonicalPageProvider = CanonicalPageFamily._();

final class CanonicalPageProvider
    extends $AsyncNotifierProvider<CanonicalPage, Page> {
  CanonicalPageProvider._({
    required CanonicalPageFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'canonicalPageProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$canonicalPageHash();

  @override
  String toString() {
    return r'canonicalPageProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  CanonicalPage create() => CanonicalPage();

  @override
  bool operator ==(Object other) {
    return other is CanonicalPageProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$canonicalPageHash() => r'f481bfbd075a8d4f3d30f07826624af98effe5a9';

final class CanonicalPageFamily extends $Family
    with
        $ClassFamilyOverride<
          CanonicalPage,
          AsyncValue<Page>,
          Page,
          FutureOr<Page>,
          skir.RecordId
        > {
  CanonicalPageFamily._()
    : super(
        retry: null,
        name: r'canonicalPageProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  CanonicalPageProvider call(skir.RecordId pageId) =>
      CanonicalPageProvider._(argument: pageId, from: this);

  @override
  String toString() => r'canonicalPageProvider';
}

abstract class _$CanonicalPage extends $AsyncNotifier<Page> {
  late final _$args = ref.$arg as skir.RecordId;
  skir.RecordId get pageId => _$args;

  FutureOr<Page> build(skir.RecordId pageId);
  @$mustCallSuper
  @override
  WhenComplete runBuild() {
    final ref = this.ref as $Ref<AsyncValue<Page>, Page>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<AsyncValue<Page>, Page>,
              AsyncValue<Page>,
              Object?,
              Object?
            >;
    return element.handleCreate(ref, () => build(_$args));
  }
}

@ProviderFor(projectedBookPages)
final projectedBookPagesProvider = ProjectedBookPagesFamily._();

final class ProjectedBookPagesProvider
    extends
        $FunctionalProvider<
          AsyncValue<List<Page>>,
          AsyncValue<List<Page>>,
          AsyncValue<List<Page>>
        >
    with $Provider<AsyncValue<List<Page>>> {
  ProjectedBookPagesProvider._({
    required ProjectedBookPagesFamily super.from,
    required (skir.RecordId, String) super.argument,
  }) : super(
         retry: null,
         name: r'projectedBookPagesProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$projectedBookPagesHash();

  @override
  String toString() {
    return r'projectedBookPagesProvider'
        ''
        '$argument';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<List<Page>>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<List<Page>> create(Ref ref) {
    final argument = this.argument as (skir.RecordId, String);
    return projectedBookPages(ref, argument.$1, argument.$2);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<List<Page>> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<List<Page>>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is ProjectedBookPagesProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$projectedBookPagesHash() =>
    r'd8d9337bba88b6e02b21cdf141e448b15d10a2f7';

final class ProjectedBookPagesFamily extends $Family
    with
        $FunctionalFamilyOverride<
          AsyncValue<List<Page>>,
          (skir.RecordId, String)
        > {
  ProjectedBookPagesFamily._()
    : super(
        retry: null,
        name: r'projectedBookPagesProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  ProjectedBookPagesProvider call(skir.RecordId bookId, String search) =>
      ProjectedBookPagesProvider._(argument: (bookId, search), from: this);

  @override
  String toString() => r'projectedBookPagesProvider';
}

@ProviderFor(projectedPage)
final projectedPageProvider = ProjectedPageFamily._();

final class ProjectedPageProvider
    extends
        $FunctionalProvider<
          AsyncValue<Page>,
          AsyncValue<Page>,
          AsyncValue<Page>
        >
    with $Provider<AsyncValue<Page>> {
  ProjectedPageProvider._({
    required ProjectedPageFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'projectedPageProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$projectedPageHash();

  @override
  String toString() {
    return r'projectedPageProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<Page>> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  AsyncValue<Page> create(Ref ref) {
    final argument = this.argument as skir.RecordId;
    return projectedPage(ref, argument);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<Page> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<Page>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is ProjectedPageProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$projectedPageHash() => r'da4bb88cb18e4588af6f6e3a70a235e70aa227f1';

final class ProjectedPageFamily extends $Family
    with $FunctionalFamilyOverride<AsyncValue<Page>, skir.RecordId> {
  ProjectedPageFamily._()
    : super(
        retry: null,
        name: r'projectedPageProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  ProjectedPageProvider call(skir.RecordId pageId) =>
      ProjectedPageProvider._(argument: pageId, from: this);

  @override
  String toString() => r'projectedPageProvider';
}

@ProviderFor(pageId)
final pageIdProvider = PageIdProvider._();

final class PageIdProvider
    extends $FunctionalProvider<skir.RecordId?, skir.RecordId?, skir.RecordId?>
    with $Provider<skir.RecordId?> {
  PageIdProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'pageIdProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$pageIdHash();

  @$internal
  @override
  $ProviderElement<skir.RecordId?> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  skir.RecordId? create(Ref ref) {
    return pageId(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(skir.RecordId? value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<skir.RecordId?>(value),
    );
  }
}

String _$pageIdHash() => r'e84de71cb1dea33c615fa06593cb8408a692b5d9';
