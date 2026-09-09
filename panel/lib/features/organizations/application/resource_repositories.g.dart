// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'resource_repositories.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(resourceRepositories)
final resourceRepositoriesProvider = ResourceRepositoriesProvider._();

final class ResourceRepositoriesProvider
    extends
        $FunctionalProvider<
          ResourceRepositories,
          ResourceRepositories,
          ResourceRepositories
        >
    with $Provider<ResourceRepositories> {
  ResourceRepositoriesProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'resourceRepositoriesProvider',
        isAutoDispose: false,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$resourceRepositoriesHash();

  @$internal
  @override
  $ProviderElement<ResourceRepositories> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  ResourceRepositories create(Ref ref) {
    return resourceRepositories(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(ResourceRepositories value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<ResourceRepositories>(value),
    );
  }
}

String _$resourceRepositoriesHash() =>
    r'5001611193f978c1a25ef657a0814e723307d9d1';
