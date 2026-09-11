// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'books.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(CanonicalBooks)
final canonicalBooksProvider = CanonicalBooksProvider._();

final class CanonicalBooksProvider
    extends $AsyncNotifierProvider<CanonicalBooks, List<Book>> {
  CanonicalBooksProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'canonicalBooksProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$canonicalBooksHash();

  @$internal
  @override
  CanonicalBooks create() => CanonicalBooks();
}

String _$canonicalBooksHash() => r'596f6546e2c31bf9c7157a127b2c87c8571da64d';

abstract class _$CanonicalBooks extends $AsyncNotifier<List<Book>> {
  FutureOr<List<Book>> build();
  @$mustCallSuper
  @override
  WhenComplete runBuild() {
    final ref = this.ref as $Ref<AsyncValue<List<Book>>, List<Book>>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<AsyncValue<List<Book>>, List<Book>>,
              AsyncValue<List<Book>>,
              Object?,
              Object?
            >;
    return element.handleCreate(ref, build);
  }
}

@ProviderFor(filteredBooks)
final filteredBooksProvider = FilteredBooksFamily._();

final class FilteredBooksProvider
    extends
        $FunctionalProvider<
          AsyncValue<List<Book>>,
          AsyncValue<List<Book>>,
          AsyncValue<List<Book>>
        >
    with $Provider<AsyncValue<List<Book>>> {
  FilteredBooksProvider._({
    required FilteredBooksFamily super.from,
    required String super.argument,
  }) : super(
         retry: null,
         name: r'filteredBooksProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$filteredBooksHash();

  @override
  String toString() {
    return r'filteredBooksProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<List<Book>>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<List<Book>> create(Ref ref) {
    final argument = this.argument as String;
    return filteredBooks(ref, argument);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<List<Book>> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<List<Book>>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is FilteredBooksProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$filteredBooksHash() => r'0e92e57f11f3e60af7c06e43862d13346de4854b';

final class FilteredBooksFamily extends $Family
    with $FunctionalFamilyOverride<AsyncValue<List<Book>>, String> {
  FilteredBooksFamily._()
    : super(
        retry: null,
        name: r'filteredBooksProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  FilteredBooksProvider call(String query) =>
      FilteredBooksProvider._(argument: query, from: this);

  @override
  String toString() => r'filteredBooksProvider';
}

@ProviderFor(bookId)
final bookIdProvider = BookIdProvider._();

final class BookIdProvider
    extends $FunctionalProvider<skir.RecordId?, skir.RecordId?, skir.RecordId?>
    with $Provider<skir.RecordId?> {
  BookIdProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'bookIdProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$bookIdHash();

  @$internal
  @override
  $ProviderElement<skir.RecordId?> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  skir.RecordId? create(Ref ref) {
    return bookId(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(skir.RecordId? value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<skir.RecordId?>(value),
    );
  }
}

String _$bookIdHash() => r'167ce138e263f44eb212912668031e5954892725';

@ProviderFor(canonicalBook)
final canonicalBookProvider = CanonicalBookFamily._();

final class CanonicalBookProvider
    extends $FunctionalProvider<AsyncValue<Book?>, Book?, FutureOr<Book?>>
    with $FutureModifier<Book?>, $FutureProvider<Book?> {
  CanonicalBookProvider._({
    required CanonicalBookFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'canonicalBookProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$canonicalBookHash();

  @override
  String toString() {
    return r'canonicalBookProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $FutureProviderElement<Book?> $createElement($ProviderPointer pointer) =>
      $FutureProviderElement(pointer);

  @override
  FutureOr<Book?> create(Ref ref) {
    final argument = this.argument as skir.RecordId;
    return canonicalBook(ref, argument);
  }

  @override
  bool operator ==(Object other) {
    return other is CanonicalBookProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$canonicalBookHash() => r'4792d2e5ec03050b83447ee8f7f590c7e94ee241';

final class CanonicalBookFamily extends $Family
    with $FunctionalFamilyOverride<FutureOr<Book?>, skir.RecordId> {
  CanonicalBookFamily._()
    : super(
        retry: null,
        name: r'canonicalBookProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  CanonicalBookProvider call(skir.RecordId bookId) =>
      CanonicalBookProvider._(argument: bookId, from: this);

  @override
  String toString() => r'canonicalBookProvider';
}

@ProviderFor(projectedBooks)
final projectedBooksProvider = ProjectedBooksProvider._();

final class ProjectedBooksProvider
    extends
        $FunctionalProvider<
          AsyncValue<List<Book>>,
          AsyncValue<List<Book>>,
          AsyncValue<List<Book>>
        >
    with $Provider<AsyncValue<List<Book>>> {
  ProjectedBooksProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'projectedBooksProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$projectedBooksHash();

  @$internal
  @override
  $ProviderElement<AsyncValue<List<Book>>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<List<Book>> create(Ref ref) {
    return projectedBooks(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<List<Book>> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<List<Book>>>(value),
    );
  }
}

String _$projectedBooksHash() => r'e54b3aacde4ddc90b0a34b08879e9bf735e80152';

@ProviderFor(projectedBook)
final projectedBookProvider = ProjectedBookFamily._();

final class ProjectedBookProvider
    extends
        $FunctionalProvider<
          AsyncValue<Book?>,
          AsyncValue<Book?>,
          AsyncValue<Book?>
        >
    with $Provider<AsyncValue<Book?>> {
  ProjectedBookProvider._({
    required ProjectedBookFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'projectedBookProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$projectedBookHash();

  @override
  String toString() {
    return r'projectedBookProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $ProviderElement<AsyncValue<Book?>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  AsyncValue<Book?> create(Ref ref) {
    final argument = this.argument as skir.RecordId;
    return projectedBook(ref, argument);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(AsyncValue<Book?> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<AsyncValue<Book?>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is ProjectedBookProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$projectedBookHash() => r'a9305728db6040d2333f7f9b751fbea6584ddf01';

final class ProjectedBookFamily extends $Family
    with $FunctionalFamilyOverride<AsyncValue<Book?>, skir.RecordId> {
  ProjectedBookFamily._()
    : super(
        retry: null,
        name: r'projectedBookProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  ProjectedBookProvider call(skir.RecordId bookId) =>
      ProjectedBookProvider._(argument: bookId, from: this);

  @override
  String toString() => r'projectedBookProvider';
}
