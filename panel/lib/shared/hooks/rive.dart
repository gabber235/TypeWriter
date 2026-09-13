import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:rive/rive.dart";

/// Creates and manages a Rive [FileLoader] for a widget lifetime.
///
/// The loader is created once for the hook's current keys and disposed when
/// those keys change or the widget is removed. Use the matching factory method
/// for the source whose identity should control that lifetime.
///
/// Example usage:
/// ```dart
/// final fileLoader = useRiveFileLoader.fromAsset(
///   "assets/animation.riv",
///   riveFactory: Factory.rive,
/// );
/// ```
const useRiveFileLoader = _RiveFileLoaderHookCreator();

class _RiveFileLoaderHookCreator {
  const _RiveFileLoaderHookCreator();

  /// Creates a loader for the Rive file at [asset].
  ///
  /// [riveFactory] selects the renderer. Include source dependencies in [keys]
  /// when they change without changing the hook call position.
  FileLoader fromAsset(
    String asset, {
    Factory? riveFactory,
    List<Object?>? keys,
  }) {
    return use(
      _RiveFileLoaderHook.fromAsset(
        asset,
        riveFactory: riveFactory ?? Factory.rive,
        keys: keys,
      ),
    );
  }

  /// Creates a loader for the Rive file at [url].
  ///
  /// [riveFactory] selects the renderer. Include source dependencies in [keys]
  /// when they change without changing the hook call position.
  FileLoader fromUrl(String url, {Factory? riveFactory, List<Object?>? keys}) {
    return use(
      _RiveFileLoaderHook.fromUrl(
        url,
        riveFactory: riveFactory ?? Factory.rive,
        keys: keys,
      ),
    );
  }

  /// Creates a loader backed by the already loaded Rive [file].
  ///
  /// [riveFactory] selects the renderer. Include source dependencies in [keys]
  /// when they change without changing the hook call position.
  FileLoader fromFile(File file, {Factory? riveFactory, List<Object?>? keys}) {
    return use(
      _RiveFileLoaderHook.fromFile(
        file,
        riveFactory: riveFactory ?? Factory.rive,
        keys: keys,
      ),
    );
  }
}

enum _FileLoaderSource { asset, url, file }

class _RiveFileLoaderHook extends Hook<FileLoader> {
  const _RiveFileLoaderHook.fromAsset(
    String asset, {
    required this.riveFactory,
    super.keys,
  }) : _source = _FileLoaderSource.asset,
       _asset = asset,
       _url = null,
       _file = null;

  const _RiveFileLoaderHook.fromUrl(
    String url, {
    required this.riveFactory,
    super.keys,
  }) : _source = _FileLoaderSource.url,
       _url = url,
       _asset = null,
       _file = null;

  const _RiveFileLoaderHook.fromFile(
    File file, {
    required this.riveFactory,
    super.keys,
  }) : _source = _FileLoaderSource.file,
       _file = file,
       _asset = null,
       _url = null;

  final _FileLoaderSource _source;
  final String? _asset;
  final String? _url;
  final File? _file;
  final Factory riveFactory;

  @override
  _RiveFileLoaderHookState createState() => _RiveFileLoaderHookState();
}

class _RiveFileLoaderHookState
    extends HookState<FileLoader, _RiveFileLoaderHook> {
  late final FileLoader _fileLoader;

  @override
  void initHook() {
    super.initHook();
    _fileLoader = switch (hook._source) {
      _FileLoaderSource.asset => FileLoader.fromAsset(
        hook._asset!,
        riveFactory: hook.riveFactory,
      ),
      _FileLoaderSource.url => FileLoader.fromUrl(
        hook._url!,
        riveFactory: hook.riveFactory,
      ),
      _FileLoaderSource.file => FileLoader.fromFile(
        hook._file!,
        riveFactory: hook.riveFactory,
      ),
    };
  }

  @override
  FileLoader build(BuildContext context) => _fileLoader;

  @override
  void dispose() {
    _fileLoader.dispose();
    super.dispose();
  }

  @override
  String get debugLabel => "useRiveFileLoader";
}
