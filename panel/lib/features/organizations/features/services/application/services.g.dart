// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'services.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(Services)
final servicesProvider = ServicesProvider._();

final class ServicesProvider
    extends $StreamNotifierProvider<Services, List<Service>> {
  ServicesProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'servicesProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$servicesHash();

  @$internal
  @override
  Services create() => Services();
}

String _$servicesHash() => r'47bcd9872c7e5ce2115f22408b897ddc7311c174';

abstract class _$Services extends $StreamNotifier<List<Service>> {
  Stream<List<Service>> build();
  @$mustCallSuper
  @override
  void runBuild() {
    final ref = this.ref as $Ref<AsyncValue<List<Service>>, List<Service>>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<AsyncValue<List<Service>>, List<Service>>,
              AsyncValue<List<Service>>,
              Object?,
              Object?
            >;
    element.handleCreate(ref, build);
  }
}

@ProviderFor(service)
final serviceProvider = ServiceFamily._();

final class ServiceProvider
    extends
        $FunctionalProvider<AsyncValue<Service?>, Service?, FutureOr<Service?>>
    with $FutureModifier<Service?>, $FutureProvider<Service?> {
  ServiceProvider._({
    required ServiceFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'serviceProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$serviceHash();

  @override
  String toString() {
    return r'serviceProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $FutureProviderElement<Service?> $createElement($ProviderPointer pointer) =>
      $FutureProviderElement(pointer);

  @override
  FutureOr<Service?> create(Ref ref) {
    final argument = this.argument as skir.RecordId;
    return service(ref, argument);
  }

  @override
  bool operator ==(Object other) {
    return other is ServiceProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$serviceHash() => r'3c409075a9fd560b0622bb74b06d4c0b1450d092';

final class ServiceFamily extends $Family
    with $FunctionalFamilyOverride<FutureOr<Service?>, skir.RecordId> {
  ServiceFamily._()
    : super(
        retry: null,
        name: r'serviceProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  ServiceProvider call(skir.RecordId id) =>
      ServiceProvider._(argument: id, from: this);

  @override
  String toString() => r'serviceProvider';
}

@ProviderFor(OrganizationTopologyStream)
final organizationTopologyStreamProvider =
    OrganizationTopologyStreamProvider._();

final class OrganizationTopologyStreamProvider
    extends
        $StreamNotifierProvider<
          OrganizationTopologyStream,
          OrganizationTopology
        > {
  OrganizationTopologyStreamProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'organizationTopologyStreamProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$organizationTopologyStreamHash();

  @$internal
  @override
  OrganizationTopologyStream create() => OrganizationTopologyStream();
}

String _$organizationTopologyStreamHash() =>
    r'77c4991c1eabadba5982768b4604fc8a97f6adf7';

abstract class _$OrganizationTopologyStream
    extends $StreamNotifier<OrganizationTopology> {
  Stream<OrganizationTopology> build();
  @$mustCallSuper
  @override
  void runBuild() {
    final ref =
        this.ref
            as $Ref<AsyncValue<OrganizationTopology>, OrganizationTopology>;
    final element =
        ref.element
            as $ClassProviderElement<
              AnyNotifier<
                AsyncValue<OrganizationTopology>,
                OrganizationTopology
              >,
              AsyncValue<OrganizationTopology>,
              Object?,
              Object?
            >;
    element.handleCreate(ref, build);
  }
}

@ProviderFor(serviceConnectionClock)
final serviceConnectionClockProvider = ServiceConnectionClockProvider._();

final class ServiceConnectionClockProvider
    extends
        $FunctionalProvider<
          DateTime Function(),
          DateTime Function(),
          DateTime Function()
        >
    with $Provider<DateTime Function()> {
  ServiceConnectionClockProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'serviceConnectionClockProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$serviceConnectionClockHash();

  @$internal
  @override
  $ProviderElement<DateTime Function()> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  DateTime Function() create(Ref ref) {
    return serviceConnectionClock(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(DateTime Function() value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<DateTime Function()>(value),
    );
  }
}

String _$serviceConnectionClockHash() =>
    r'fd215b05162559c0812275fda4f5012bb204b95b';

/// Shares one deadline projection for a service list across its consumers.

@ProviderFor(serviceConnections)
final serviceConnectionsProvider = ServiceConnectionsFamily._();

/// Shares one deadline projection for a service list across its consumers.

final class ServiceConnectionsProvider
    extends
        $FunctionalProvider<
          Map<skir.RecordId, bool>,
          Map<skir.RecordId, bool>,
          Map<skir.RecordId, bool>
        >
    with $Provider<Map<skir.RecordId, bool>> {
  /// Shares one deadline projection for a service list across its consumers.
  ServiceConnectionsProvider._({
    required ServiceConnectionsFamily super.from,
    required List<Service> super.argument,
  }) : super(
         retry: null,
         name: r'serviceConnectionsProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$serviceConnectionsHash();

  @override
  String toString() {
    return r'serviceConnectionsProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $ProviderElement<Map<skir.RecordId, bool>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  Map<skir.RecordId, bool> create(Ref ref) {
    final argument = this.argument as List<Service>;
    return serviceConnections(ref, argument);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(Map<skir.RecordId, bool> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<Map<skir.RecordId, bool>>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is ServiceConnectionsProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$serviceConnectionsHash() =>
    r'1dd5ee28c85434ffe187290a8746954a457eaa40';

/// Shares one deadline projection for a service list across its consumers.

final class ServiceConnectionsFamily extends $Family
    with $FunctionalFamilyOverride<Map<skir.RecordId, bool>, List<Service>> {
  ServiceConnectionsFamily._()
    : super(
        retry: null,
        name: r'serviceConnectionsProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  /// Shares one deadline projection for a service list across its consumers.

  ServiceConnectionsProvider call(List<Service> services) =>
      ServiceConnectionsProvider._(argument: services, from: this);

  @override
  String toString() => r'serviceConnectionsProvider';
}

@ProviderFor(hostConnected)
final hostConnectedProvider = HostConnectedFamily._();

final class HostConnectedProvider extends $FunctionalProvider<bool, bool, bool>
    with $Provider<bool> {
  HostConnectedProvider._({
    required HostConnectedFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'hostConnectedProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$hostConnectedHash();

  @override
  String toString() {
    return r'hostConnectedProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  $ProviderElement<bool> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  bool create(Ref ref) {
    final argument = this.argument as skir.RecordId;
    return hostConnected(ref, argument);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(bool value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<bool>(value),
    );
  }

  @override
  bool operator ==(Object other) {
    return other is HostConnectedProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$hostConnectedHash() => r'087aa6f1d7bf48579e82fe937831637c759492e7';

final class HostConnectedFamily extends $Family
    with $FunctionalFamilyOverride<bool, skir.RecordId> {
  HostConnectedFamily._()
    : super(
        retry: null,
        name: r'hostConnectedProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  HostConnectedProvider call(skir.RecordId hostId) =>
      HostConnectedProvider._(argument: hostId, from: this);

  @override
  String toString() => r'hostConnectedProvider';
}
