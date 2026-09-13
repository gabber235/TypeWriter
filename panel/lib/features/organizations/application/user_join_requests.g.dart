// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_join_requests.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(UserJoinRequests)
final userJoinRequestsProvider = UserJoinRequestsProvider._();

final class UserJoinRequestsProvider
    extends $StreamNotifierProvider<UserJoinRequests, List<UserJoinRequest>> {
  UserJoinRequestsProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'userJoinRequestsProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$userJoinRequestsHash();

  @$internal
  @override
  UserJoinRequests create() => UserJoinRequests();
}

String _$userJoinRequestsHash() => r'a9e2c1462e3a698b5fc57916f94f18ac31a9e3b2';

abstract class _$UserJoinRequests
    extends $StreamNotifier<List<UserJoinRequest>> {
  Stream<List<UserJoinRequest>> build();
  @$mustCallSuper
  @override
  WhenComplete runBuild() {
    final ref =
        this.ref
            as $Ref<AsyncValue<List<UserJoinRequest>>, List<UserJoinRequest>>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<
                AsyncValue<List<UserJoinRequest>>,
                List<UserJoinRequest>
              >,
              AsyncValue<List<UserJoinRequest>>,
              Object?,
              Object?
            >;
    return element.handleCreate(ref, build);
  }
}
