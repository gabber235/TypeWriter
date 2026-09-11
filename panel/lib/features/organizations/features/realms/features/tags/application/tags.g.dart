// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'tags.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(CanonicalTags)
final canonicalTagsProvider = CanonicalTagsProvider._();

final class CanonicalTagsProvider
    extends $AsyncNotifierProvider<CanonicalTags, List<Tag>> {
  CanonicalTagsProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'canonicalTagsProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$canonicalTagsHash();

  @$internal
  @override
  CanonicalTags create() => CanonicalTags();
}

String _$canonicalTagsHash() => r'bfdb0f68f03a899663feb509b96241050232c6cb';

abstract class _$CanonicalTags extends $AsyncNotifier<List<Tag>> {
  FutureOr<List<Tag>> build();
  @$mustCallSuper
  @override
  WhenComplete runBuild() {
    final ref = this.ref as $Ref<AsyncValue<List<Tag>>, List<Tag>>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<AsyncValue<List<Tag>>, List<Tag>>,
              AsyncValue<List<Tag>>,
              Object?,
              Object?
            >;
    return element.handleCreate(ref, build);
  }
}

@ProviderFor(canonicalTag)
final canonicalTagProvider = CanonicalTagFamily._();

final class CanonicalTagProvider
    extends $FunctionalProvider<AsyncValue<Tag?>, Tag?, FutureOr<Tag?>>
    with $FutureModifier<Tag?>, $FutureProvider<Tag?> {
  CanonicalTagProvider._({
    required CanonicalTagFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'canonicalTagProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$canonicalTagHash();

  @override
  String toString() {
    return r'canonicalTagProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $FutureProviderElement<Tag?> $createElement($ProviderPointer pointer) =>
      $FutureProviderElement(pointer);

  @override
  FutureOr<Tag?> create(Ref ref) {
    final argument = this.argument as skir.RecordId;
    return canonicalTag(ref, argument);
  }

  @override
  bool operator ==(Object other) {
    return other is CanonicalTagProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$canonicalTagHash() => r'251f8d7c86935b1b7a105035ae211efe5aa7dea3';

final class CanonicalTagFamily extends $Family
    with $FunctionalFamilyOverride<FutureOr<Tag?>, skir.RecordId> {
  CanonicalTagFamily._()
    : super(
        retry: null,
        name: r'canonicalTagProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  CanonicalTagProvider call(skir.RecordId tagId) =>
      CanonicalTagProvider._(argument: tagId, from: this);

  @override
  String toString() => r'canonicalTagProvider';
}

@ProviderFor(projectedTags)
final projectedTagsProvider = ProjectedTagsProvider._();

final class ProjectedTagsProvider
    extends
        $FunctionalProvider<
          AsyncValue<List<Tag>>,
          AsyncValue<List<Tag>>,
          AsyncValue<List<Tag>>
        >
    with $Provider<AsyncValue<List<Tag>>> {
  ProjectedTagsProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'projectedTagsProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$projectedTagsHash();

  @$internal
  @override
  $ProviderElement<AsyncValue<List<Tag>>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<List<Tag>> create(Ref ref) {
    return projectedTags(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<List<Tag>> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<List<Tag>>>(value),
    );
  }
}

String _$projectedTagsHash() => r'e025a4a4a186a9194d8b0bf3661cbe47813a147a';

@ProviderFor(projectedTag)
final projectedTagProvider = ProjectedTagFamily._();

final class ProjectedTagProvider
    extends
        $FunctionalProvider<
          AsyncValue<Tag?>,
          AsyncValue<Tag?>,
          AsyncValue<Tag?>
        >
    with $Provider<AsyncValue<Tag?>> {
  ProjectedTagProvider._({
    required ProjectedTagFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'projectedTagProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$projectedTagHash();

  @override
  String toString() {
    return r'projectedTagProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<Tag?>> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  AsyncValue<Tag?> create(Ref ref) {
    final argument = this.argument as skir.RecordId;
    return projectedTag(ref, argument);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<Tag?> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<Tag?>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is ProjectedTagProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$projectedTagHash() => r'ca2bfc58e82b7ac33d57319167c3f17d3d6ac200';

final class ProjectedTagFamily extends $Family
    with $FunctionalFamilyOverride<AsyncValue<Tag?>, skir.RecordId> {
  ProjectedTagFamily._()
    : super(
        retry: null,
        name: r'projectedTagProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  ProjectedTagProvider call(skir.RecordId tagId) =>
      ProjectedTagProvider._(argument: tagId, from: this);

  @override
  String toString() => r'projectedTagProvider';
}
