// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'editor_workspace.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(editorWorkspace)
final editorWorkspaceProvider = EditorWorkspaceProvider._();

final class EditorWorkspaceProvider
    extends
        $FunctionalProvider<EditorWorkspace, EditorWorkspace, EditorWorkspace>
    with $Provider<EditorWorkspace> {
  EditorWorkspaceProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'editorWorkspaceProvider',
        isAutoDispose: false,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$editorWorkspaceHash();

  @$internal
  @override
  $ProviderElement<EditorWorkspace> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  EditorWorkspace create(Ref ref) {
    return editorWorkspace(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(EditorWorkspace value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<EditorWorkspace>(value),
    );
  }
}

String _$editorWorkspaceHash() => r'edae858b304437627be5b94b45f0f4705218553d';
