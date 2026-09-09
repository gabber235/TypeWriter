// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'local_work.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(localWork)
final localWorkProvider = LocalWorkProvider._();

final class LocalWorkProvider
    extends $FunctionalProvider<LocalWork, LocalWork, LocalWork>
    with $Provider<LocalWork> {
  LocalWorkProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'localWorkProvider',
        isAutoDispose: false,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$localWorkHash();

  @$internal
  @override
  $ProviderElement<LocalWork> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  LocalWork create(Ref ref) {
    return localWork(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(LocalWork value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<LocalWork>(value),
    );
  }
}

String _$localWorkHash() => r'325efa3fdc9ec9bc43aa5ce9c9da776c906d8133';
