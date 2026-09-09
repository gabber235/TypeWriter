// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'services.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(OrganizationServices)
final organizationServicesProvider = OrganizationServicesFamily._();

final class OrganizationServicesProvider
    extends $StreamNotifierProvider<OrganizationServices, List<Service>> {
  OrganizationServicesProvider._({
    required OrganizationServicesFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'organizationServicesProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$organizationServicesHash();

  @override
  String toString() {
    return r'organizationServicesProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  OrganizationServices create() => OrganizationServices();

  @override
  bool operator ==(Object other) {
    return other is OrganizationServicesProvider && other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$organizationServicesHash() =>
    r'1e755f6fbc29097b084ec213c5e6a06cfce381c5';

final class OrganizationServicesFamily extends $Family
    with
        $ClassFamilyOverride<
          OrganizationServices,
          AsyncValue<List<Service>>,
          List<Service>,
          Stream<List<Service>>,
          skir.RecordId
        > {
  OrganizationServicesFamily._()
    : super(
        retry: null,
        name: r'organizationServicesProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  OrganizationServicesProvider call(skir.RecordId organizationId) =>
      OrganizationServicesProvider._(argument: organizationId, from: this);

  @override
  String toString() => r'organizationServicesProvider';
}

abstract class _$OrganizationServices extends $StreamNotifier<List<Service>> {
  late final _$args = ref.$arg as skir.RecordId;
  skir.RecordId get organizationId => _$args;

  Stream<List<Service>> build(skir.RecordId organizationId);
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
    element.handleCreate(ref, () => build(_$args));
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

String _$servicesHash() => r'a73dacdcfcc43b3b8100d858f81dbe667e3fd005';

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

@ProviderFor(organizationTopologyStream)
final organizationTopologyStreamProvider =
    OrganizationTopologyStreamProvider._();

final class OrganizationTopologyStreamProvider
    extends
        $FunctionalProvider<
          AsyncValue<OrganizationTopology>,
          OrganizationTopology,
          Stream<OrganizationTopology>
        >
    with
        $FutureModifier<OrganizationTopology>,
        $StreamProvider<OrganizationTopology> {
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
  $StreamProviderElement<OrganizationTopology> $createElement(
    $ProviderPointer pointer,
  ) => $StreamProviderElement(pointer);

  @override
  Stream<OrganizationTopology> create(Ref ref) {
    return organizationTopologyStream(ref);
  }
}

String _$organizationTopologyStreamHash() =>
    r'9bb2fbcc6854c48e401a910ea007417828c91951';

@ProviderFor(OrganizationTopologyController)
final organizationTopologyControllerProvider =
    OrganizationTopologyControllerFamily._();

final class OrganizationTopologyControllerProvider
    extends
        $StreamNotifierProvider<
          OrganizationTopologyController,
          OrganizationTopology
        > {
  OrganizationTopologyControllerProvider._({
    required OrganizationTopologyControllerFamily super.from,
    required skir.RecordId super.argument,
  }) : super(
         retry: null,
         name: r'organizationTopologyControllerProvider',
         isAutoDispose: true,
         dependencies: null,
         $allTransitiveDependencies: null,
       );

  @override
  String debugGetCreateSourceHash() => _$organizationTopologyControllerHash();

  @override
  String toString() {
    return r'organizationTopologyControllerProvider'
        ''
        '($argument)';
  }

  @$internal
  @override
  OrganizationTopologyController create() => OrganizationTopologyController();

  @override
  bool operator ==(Object other) {
    return other is OrganizationTopologyControllerProvider &&
        other.argument == argument;
  }

  @override
  int get hashCode {
    return argument.hashCode;
  }
}

String _$organizationTopologyControllerHash() =>
    r'aa04502bebbe260b8e17dd4179f734ea8567b85c';

final class OrganizationTopologyControllerFamily extends $Family
    with
        $ClassFamilyOverride<
          OrganizationTopologyController,
          AsyncValue<OrganizationTopology>,
          OrganizationTopology,
          Stream<OrganizationTopology>,
          skir.RecordId
        > {
  OrganizationTopologyControllerFamily._()
    : super(
        retry: null,
        name: r'organizationTopologyControllerProvider',
        dependencies: null,
        $allTransitiveDependencies: null,
        isAutoDispose: true,
      );

  OrganizationTopologyControllerProvider call(skir.RecordId organizationId) =>
      OrganizationTopologyControllerProvider._(
        argument: organizationId,
        from: this,
      );

  @override
  String toString() => r'organizationTopologyControllerProvider';
}

abstract class _$OrganizationTopologyController
    extends $StreamNotifier<OrganizationTopology> {
  late final _$args = ref.$arg as skir.RecordId;
  skir.RecordId get organizationId => _$args;

  Stream<OrganizationTopology> build(skir.RecordId organizationId);
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
    element.handleCreate(ref, () => build(_$args));
  }
}

/// Shares one deadline projection for a service list across its consumers.

@ProviderFor(serviceConnections)
final serviceConnectionsProvider = ServiceConnectionsProvider._();

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
  ServiceConnectionsProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'serviceConnectionsProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$serviceConnectionsHash();

  @$internal
  @override
  $ProviderElement<Map<skir.RecordId, bool>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  Map<skir.RecordId, bool> create(Ref ref) {
    return serviceConnections(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(Map<skir.RecordId, bool> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<Map<skir.RecordId, bool>>(value),
    );
  }
}

String _$serviceConnectionsHash() =>
    r'76071ad90e4e70ecbf5abf6ff2176f674530a307';

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

String _$hostConnectedHash() => r'09b1ee4918132ecf7c243b785b39033369b3ff63';

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
