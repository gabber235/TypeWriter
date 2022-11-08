import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';

typedef RouteControllerSupplier = RoutingController? Function();
typedef RouteStateFunction<T> = T Function(RoutingController controller);

T useRouteState<T>(
    RouteControllerSupplier supplier, RouteStateFunction<T> function,
    {T? defaultValue}) {
  return use(_RouteStateHook(supplier, function, defaultValue: defaultValue));
}

class _RouteStateHook<T> extends Hook<T> {
  final RouteControllerSupplier supplier;
  final RouteStateFunction<T> function;
  final T? defaultValue;

  const _RouteStateHook(this.supplier, this.function, {this.defaultValue});

  @override
  _RouteStateHookState<T> createState() => _RouteStateHookState<T>();
}

class _RouteStateHookState<T> extends HookState<T, _RouteStateHook<T>> {
  RoutingController? _controller;
  bool _mounted = false;

  void _initController() async {
    if (!_mounted) return;
    _controller = hook.supplier();
    if (_controller == null) {
      // Retry in 100ms
      Future.delayed(const Duration(milliseconds: 100), _initController);
    } else {
      _controller?.addListener(_onRouteChanged);
      setState(() {});
    }
  }

  void _onRouteChanged() {
    setState(() {});
  }

  @override
  void initHook() {
    _mounted = true;
    _initController();
    super.initHook();
  }

  @override
  void dispose() {
    _mounted = false;
    _controller?.removeListener(_onRouteChanged);
    super.dispose();
  }

  @override
  T build(BuildContext context) {
    if (_controller != null) return hook.function(_controller!);
    if (hook.defaultValue != null) return hook.defaultValue!;
    throw Exception(
        "No controller found in time and no default value provided");
  }
}
