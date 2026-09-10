// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'inspection.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(inspectedSelection)
final inspectedSelectionProvider = InspectedSelectionProvider._();

final class InspectedSelectionProvider
    extends
        $FunctionalProvider<
          AsyncValue<List<InspectableSelectable<SelectableIdentifier>>>,
          AsyncValue<List<InspectableSelectable<SelectableIdentifier>>>,
          AsyncValue<List<InspectableSelectable<SelectableIdentifier>>>
        >
    with
        $Provider<
          AsyncValue<List<InspectableSelectable<SelectableIdentifier>>>
        > {
  InspectedSelectionProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'inspectedSelectionProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$inspectedSelectionHash();

  @$internal
  @override
  $ProviderElement<
    AsyncValue<List<InspectableSelectable<SelectableIdentifier>>>
  >
  $createElement($ProviderPointer pointer) => $ProviderElement(pointer);

  @override
  AsyncValue<List<InspectableSelectable<SelectableIdentifier>>> create(
    Ref ref,
  ) {
    return inspectedSelection(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(
    AsyncValue<List<InspectableSelectable<SelectableIdentifier>>> value,
  ) {
    return $ProviderOverride(
      origin: this,
      providerOverride:
          $SyncValueProvider<
            AsyncValue<List<InspectableSelectable<SelectableIdentifier>>>
          >(value),
    );
  }
}

String _$inspectedSelectionHash() =>
    r'fdba8a3d317c067c0afab2040f701d5c23ebca8d';

@ProviderFor(hasInspectableSelection)
final hasInspectableSelectionProvider = HasInspectableSelectionProvider._();

final class HasInspectableSelectionProvider
    extends $FunctionalProvider<bool, bool, bool>
    with $Provider<bool> {
  HasInspectableSelectionProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'hasInspectableSelectionProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$hasInspectableSelectionHash();

  @$internal
  @override
  $ProviderElement<bool> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  bool create(Ref ref) {
    return hasInspectableSelection(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(bool value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<bool>(value),
    );
  }
}

String _$hasInspectableSelectionHash() =>
    r'7cbf8deaf3235756f3140f855bf5bd9bf7d40ccb';

@ProviderFor(inspectionSession)
final inspectionSessionProvider = InspectionSessionProvider._();

final class InspectionSessionProvider
    extends
        $FunctionalProvider<
          InspectionSession,
          InspectionSession,
          InspectionSession
        >
    with $Provider<InspectionSession> {
  InspectionSessionProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'inspectionSessionProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$inspectionSessionHash();

  @$internal
  @override
  $ProviderElement<InspectionSession> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  InspectionSession create(Ref ref) {
    return inspectionSession(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(InspectionSession value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<InspectionSession>(value),
    );
  }
}

String _$inspectionSessionHash() => r'a868b7baa20bfc6f0ad563c882929f25c10f0895';
