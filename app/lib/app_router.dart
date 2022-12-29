import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import 'package:typewriter/guard/connected_guard.dart';
import "package:typewriter/main.dart";
import "package:typewriter/pages/book_page.dart";
import "package:typewriter/pages/connect_page.dart";
import "package:typewriter/pages/home_page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/pages/pages_list.dart";

part "app_router.gr.dart";

@CustomAutoRouter(
  replaceInRouteName: "Page,Route",
  routes: [
    AutoRoute(page: HomePage, initial: true),
    AutoRoute(
      path: "/connect",
      page: ConnectPage,
    ),
    AutoRoute(
      path: "/error",
      page: ErrorConnectPage,
    ),
    AutoRoute(
      path: "/book",
      page: BookPage,
      guards: [ConnectedGuard],
      children: [
        AutoRoute(
          path: "pages",
          page: PagesList,
          name: "PagesListRoute",
          initial: true,
          children: [
            AutoRoute(
              path: "",
              page: EmptyPageEditor,
              name: "EmptyPageEditorRoute",
              initial: true,
            ),
            AutoRoute(
              path: ":id",
              page: PageEditor,
              name: "PageEditorRoute",
            ),
          ],
        ),
      ],
    ),
  ],
  transitionsBuilder: TransitionsBuilders.noTransition,
)
class AppRouter extends _$AppRouter {
  AppRouter({required super.connectedGuard});
}

Widget slideLeftWithFade(
  BuildContext context,
  Animation<double> animation,
  Animation<double> secondaryAnimation,
  Widget child,
) =>
    SlideTransition(
      position: Tween<Offset>(
        begin: const Offset(.5, 0.0),
        end: Offset.zero,
      ).animate(animation),
      child: FadeTransition(opacity: animation, child: child),
    );

/// Fetch a nested route from the current route.
RouteData? _fetchCurrentRouteData(String name, RoutingController controller) {
  if (controller.current.name == name) {
    return controller.current;
  }
  final child = controller.innerRouterOf(controller.current.name);
  return child != null ? _fetchCurrentRouteData(name, child) : null;
}

/// Provides the current route data for the given [name].
final currentRouteDataProvider = Provider.family<RouteData?, String>((ref, name) {
  final router = ref.watch(appRouter);
  void invalidator() => ref.invalidateSelf();
  router.addListener(invalidator);
  ref.onDispose(() => router.removeListener(invalidator));
  return _fetchCurrentRouteData(name, router);
});
