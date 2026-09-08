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
    r'403e1791575d1980022006a7024a19233db027e5';

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
    r'fc35f93d31e8a73986f4932f8650aac64a93cc09';

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

String _$inspectionSessionHash() => r'87e63ba53af6a7c0f7c7def866221560e0434209';

@ProviderFor(inspectedHeader)
final inspectedHeaderProvider = InspectedHeaderProvider._();

final class InspectedHeaderProvider
    extends $FunctionalProvider<Widget?, Widget?, Widget?>
    with $Provider<Widget?> {
  InspectedHeaderProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'inspectedHeaderProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$inspectedHeaderHash();

  @$internal
  @override
  $ProviderElement<Widget?> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  Widget? create(Ref ref) {
    return inspectedHeader(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(Widget? value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<Widget?>(value),
    );
  }
}

String _$inspectedHeaderHash() => r'6ff81ff07e2b42ada33b6d29f99c52ea618485f1';
