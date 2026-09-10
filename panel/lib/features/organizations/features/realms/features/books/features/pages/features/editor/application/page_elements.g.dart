// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'page_elements.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_ElementLink _$ElementLinkFromJson(Map<String, dynamic> json) => _ElementLink(
  linkId: json['linkId'] as String,
  otherId: json['otherId'] as String,
  path: json['path'] as String,
);

Map<String, dynamic> _$ElementLinkToJson(_ElementLink instance) =>
    <String, dynamic>{
      'linkId': instance.linkId,
      'otherId': instance.otherId,
      'path': instance.path,
    };

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(decodedRealmDocuments)
final decodedRealmDocumentsProvider = DecodedRealmDocumentsFamily._();

final class DecodedRealmDocumentsProvider
    extends
        $FunctionalProvider<
          AsyncValue<Map<String, List<PageElement>>>,
          AsyncValue<Map<String, List<PageElement>>>,
          AsyncValue<Map<String, List<PageElement>>>
        >
    with $Provider<AsyncValue<Map<String, List<PageElement>>>> {
  DecodedRealmDocumentsProvider._({
    required DecodedRealmDocumentsFamily super.from,
    required (skir.RecordId, skir.RecordId) super.argument,
  }) : super(
         retry: null,
         name: r'decodedRealmDocumentsProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$decodedRealmDocumentsHash();

  @override
  String toString() {
    return r'decodedRealmDocumentsProvider'
        ''
        '$argument';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<Map<String, List<PageElement>>>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<Map<String, List<PageElement>>> create(Ref ref) {
    final argument = this.argument as (skir.RecordId, skir.RecordId);
    return decodedRealmDocuments(ref, argument.$1, argument.$2);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<Map<String, List<PageElement>>> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride:
          $SyncValueProvider<AsyncValue<Map<String, List<PageElement>>>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is DecodedRealmDocumentsProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$decodedRealmDocumentsHash() =>
    r'4a0ccfc47b17cd520a63a57462920a535037d2aa';

final class DecodedRealmDocumentsFamily extends $Family
    with
        $FunctionalFamilyOverride<
          AsyncValue<Map<String, List<PageElement>>>,
          (skir.RecordId, skir.RecordId)
        > {
  DecodedRealmDocumentsFamily._()
    : super(
        retry: null,
        name: r'decodedRealmDocumentsProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  DecodedRealmDocumentsProvider call(
    skir.RecordId organizationId,
    skir.RecordId realmId,
  ) => DecodedRealmDocumentsProvider._(
    argument: (organizationId, realmId),
    from: this,
  );

  @override
  String toString() => r'decodedRealmDocumentsProvider';
}

@ProviderFor(realmEntryIndex)
final realmEntryIndexProvider = RealmEntryIndexFamily._();

final class RealmEntryIndexProvider
    extends
        $FunctionalProvider<
          AsyncValue<Map<String, CachedPageEntry>>,
          AsyncValue<Map<String, CachedPageEntry>>,
          AsyncValue<Map<String, CachedPageEntry>>
        >
    with $Provider<AsyncValue<Map<String, CachedPageEntry>>> {
  RealmEntryIndexProvider._({
    required RealmEntryIndexFamily super.from,
    required (skir.RecordId, skir.RecordId) super.argument,
  }) : super(
         retry: null,
         name: r'realmEntryIndexProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$realmEntryIndexHash();

  @override
  String toString() {
    return r'realmEntryIndexProvider'
        ''
        '$argument';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<Map<String, CachedPageEntry>>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<Map<String, CachedPageEntry>> create(Ref ref) {
    final argument = this.argument as (skir.RecordId, skir.RecordId);
    return realmEntryIndex(ref, argument.$1, argument.$2);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<Map<String, CachedPageEntry>> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride:
          $SyncValueProvider<AsyncValue<Map<String, CachedPageEntry>>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is RealmEntryIndexProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$realmEntryIndexHash() => r'1ac282a6315c4bb3e9939a2e2bc6cd34da52ae72';

final class RealmEntryIndexFamily extends $Family
    with
        $FunctionalFamilyOverride<
          AsyncValue<Map<String, CachedPageEntry>>,
          (skir.RecordId, skir.RecordId)
        > {
  RealmEntryIndexFamily._()
    : super(
        retry: null,
        name: r'realmEntryIndexProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  RealmEntryIndexProvider call(
    skir.RecordId organizationId,
    skir.RecordId realmId,
  ) => RealmEntryIndexProvider._(
    argument: (organizationId, realmId),
    from: this,
  );

  @override
  String toString() => r'realmEntryIndexProvider';
}

@ProviderFor(pageDocumentHealth)
final pageDocumentHealthProvider = PageDocumentHealthFamily._();

final class PageDocumentHealthProvider
    extends
        $FunctionalProvider<
          PageDocumentHealth?,
          PageDocumentHealth?,
          PageDocumentHealth?
        >
    with $Provider<PageDocumentHealth?> {
  PageDocumentHealthProvider._({
    required PageDocumentHealthFamily super.from,
    required (skir.RecordId, skir.RecordId, skir.RecordId) super.argument,
  }) : super(
         retry: null,
         name: r'pageDocumentHealthProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$pageDocumentHealthHash();

  @override
  String toString() {
    return r'pageDocumentHealthProvider'
        ''
        '$argument';
  }

  @$internal
  @override
  $ProviderElement<PageDocumentHealth?> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  PageDocumentHealth? create(Ref ref) {
    final argument =
        this.argument as (skir.RecordId, skir.RecordId, skir.RecordId);
    return pageDocumentHealth(ref, argument.$1, argument.$2, argument.$3);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(PageDocumentHealth? value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<PageDocumentHealth?>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is PageDocumentHealthProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$pageDocumentHealthHash() =>
    r'f5dc407df884b8780122bfb9e47040ef6cb1a893';

final class PageDocumentHealthFamily extends $Family
    with
        $FunctionalFamilyOverride<
          PageDocumentHealth?,
          (skir.RecordId, skir.RecordId, skir.RecordId)
        > {
  PageDocumentHealthFamily._()
    : super(
        retry: null,
        name: r'pageDocumentHealthProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  PageDocumentHealthProvider call(
    skir.RecordId organizationId,
    skir.RecordId realmId,
    skir.RecordId pageId,
  ) => PageDocumentHealthProvider._(
    argument: (organizationId, realmId, pageId),
    from: this,
  );

  @override
  String toString() => r'pageDocumentHealthProvider';
}

@ProviderFor(PageElements)
final pageElementsProvider = PageElementsFamily._();

final class PageElementsProvider
    extends $AsyncNotifierProvider<PageElements, List<PageElement>> {
  PageElementsProvider._({
    required PageElementsFamily super.from,
    required (skir.RecordId, skir.RecordId, String) super.argument,
  }) : super(
         retry: null,
         name: r'pageElementsProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$pageElementsHash();

  @override
  String toString() {
    return r'pageElementsProvider'
        ''
        '$argument';
  }

  @$internal
  @override
  PageElements create() => PageElements();

  @override
  bool operator ==(Object other) {
    return other is PageElementsProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$pageElementsHash() => r'ae8bd5842053bdd5532ec519345beddadb5c5000';

final class PageElementsFamily extends $Family
    with
        $ClassFamilyOverride<
          PageElements,
          AsyncValue<List<PageElement>>,
          List<PageElement>,
          FutureOr<List<PageElement>>,
          (skir.RecordId, skir.RecordId, String)
        > {
  PageElementsFamily._()
    : super(
        retry: null,
        name: r'pageElementsProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  PageElementsProvider call(
    skir.RecordId organizationId,
    skir.RecordId realmId,
    String pageId,
  ) => PageElementsProvider._(
    argument: (organizationId, realmId, pageId),
    from: this,
  );

  @override
  String toString() => r'pageElementsProvider';
}

abstract class _$PageElements extends $AsyncNotifier<List<PageElement>> {
  late final _$args = ref.$arg as (skir.RecordId, skir.RecordId, String);
  skir.RecordId get organizationId => _$args.$1;
  skir.RecordId get realmId => _$args.$2;
  String get pageId => _$args.$3;

  FutureOr<List<PageElement>> build(
    skir.RecordId organizationId,
    skir.RecordId realmId,
    String pageId,
  );
  @$mustCallSuper
  @override
  WhenComplete runBuild() {
    final ref =
        this.ref as $Ref<AsyncValue<List<PageElement>>, List<PageElement>>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<AsyncValue<List<PageElement>>, List<PageElement>>,
              AsyncValue<List<PageElement>>,
              Object?,
              Object?
            >;
    return element.handleCreate(
      ref,
      () => build(_$args.$1, _$args.$2, _$args.$3),
    );
  }
}

@ProviderFor(projectedPageElements)
final projectedPageElementsProvider = ProjectedPageElementsFamily._();

final class ProjectedPageElementsProvider
    extends
        $FunctionalProvider<
          AsyncValue<List<PageElement>>,
          AsyncValue<List<PageElement>>,
          AsyncValue<List<PageElement>>
        >
    with $Provider<AsyncValue<List<PageElement>>> {
  ProjectedPageElementsProvider._({
    required ProjectedPageElementsFamily super.from,
    required (skir.RecordId, skir.RecordId, String) super.argument,
  }) : super(
         retry: null,
         name: r'projectedPageElementsProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$projectedPageElementsHash();

  @override
  String toString() {
    return r'projectedPageElementsProvider'
        ''
        '$argument';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<List<PageElement>>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<List<PageElement>> create(Ref ref) {
    final argument = this.argument as (skir.RecordId, skir.RecordId, String);
    return projectedPageElements(ref, argument.$1, argument.$2, argument.$3);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<List<PageElement>> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<List<PageElement>>>(
        value,
      ),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is ProjectedPageElementsProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$projectedPageElementsHash() =>
    r'f3d73ca63433918d39f579bbd6ae0b34389642a3';

final class ProjectedPageElementsFamily extends $Family
    with
        $FunctionalFamilyOverride<
          AsyncValue<List<PageElement>>,
          (skir.RecordId, skir.RecordId, String)
        > {
  ProjectedPageElementsFamily._()
    : super(
        retry: null,
        name: r'projectedPageElementsProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  ProjectedPageElementsProvider call(
    skir.RecordId organizationId,
    skir.RecordId realmId,
    String pageId,
  ) => ProjectedPageElementsProvider._(
    argument: (organizationId, realmId, pageId),
    from: this,
  );

  @override
  String toString() => r'projectedPageElementsProvider';
}

@ProviderFor(projectedPageElement)
final projectedPageElementProvider = ProjectedPageElementFamily._();

final class ProjectedPageElementProvider
    extends
        $FunctionalProvider<
          AsyncValue<PageElement?>,
          AsyncValue<PageElement?>,
          AsyncValue<PageElement?>
        >
    with $Provider<AsyncValue<PageElement?>> {
  ProjectedPageElementProvider._({
    required ProjectedPageElementFamily super.from,
    required (skir.RecordId, skir.RecordId, String, String) super.argument,
  }) : super(
         retry: null,
         name: r'projectedPageElementProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$projectedPageElementHash();

  @override
  String toString() {
    return r'projectedPageElementProvider'
        ''
        '$argument';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<PageElement?>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<PageElement?> create(Ref ref) {
    final argument =
        this.argument as (skir.RecordId, skir.RecordId, String, String);
    return projectedPageElement(
      ref,
      argument.$1,
      argument.$2,
      argument.$3,
      argument.$4,
    );
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<PageElement?> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<PageElement?>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is ProjectedPageElementProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$projectedPageElementHash() =>
    r'4ea99393d3fa0c49926007b0570f7a53a97e102c';

final class ProjectedPageElementFamily extends $Family
    with
        $FunctionalFamilyOverride<
          AsyncValue<PageElement?>,
          (skir.RecordId, skir.RecordId, String, String)
        > {
  ProjectedPageElementFamily._()
    : super(
        retry: null,
        name: r'projectedPageElementProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  ProjectedPageElementProvider call(
    skir.RecordId organizationId,
    skir.RecordId realmId,
    String pageId,
    String elementId,
  ) => ProjectedPageElementProvider._(
    argument: (organizationId, realmId, pageId, elementId),
    from: this,
  );

  @override
  String toString() => r'projectedPageElementProvider';
}
