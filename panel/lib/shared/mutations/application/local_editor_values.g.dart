// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'local_editor_values.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(localEditorValues)
final localEditorValuesProvider = LocalEditorValuesProvider._();

final class LocalEditorValuesProvider
    extends
        $FunctionalProvider<
          Map<EditorResourceKey, LocalEditorValue>,
          Map<EditorResourceKey, LocalEditorValue>,
          Map<EditorResourceKey, LocalEditorValue>
        >
    with $Provider<Map<EditorResourceKey, LocalEditorValue>> {
  LocalEditorValuesProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'localEditorValuesProvider',
        isAutoDispose: true,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$localEditorValuesHash();

  @$internal
  @override
  $ProviderElement<Map<EditorResourceKey, LocalEditorValue>> $createElement(
    $ProviderPointer pointer,
  ) => $ProviderElement(pointer);

  @override
  Map<EditorResourceKey, LocalEditorValue> create(Ref ref) {
    return localEditorValues(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(Map<EditorResourceKey, LocalEditorValue> value) {
    return $ProviderOverride(
      origin: this,
      providerOverride:
          $SyncValueProvider<Map<EditorResourceKey, LocalEditorValue>>(value),
    );
  }
}

String _$localEditorValuesHash() => r'8f7bb0cbbaadc75ac38d8bb715a0bee0ae863095';
