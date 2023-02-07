import "package:auto_route/auto_route.dart";
import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/guard/connected_guard.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/book_page.dart";
import "package:typewriter/pages/connect_page.dart";
import "package:typewriter/pages/error_connect_page.dart";
import "package:typewriter/pages/home_page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/pages/pages_list.dart";
import "package:typewriter/utils/passing_reference.dart";

part "app_router.g.dart";
part "app_router.gr.dart";

final appRouter = Provider<AppRouter>(
  (ref) => AppRouter(
    connectedGuard: ConnectedGuard(ref),
  ),
);

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
@Riverpod(keepAlive: true)
RouteData? currentRouteData(CurrentRouteDataRef ref, String name) {
  final router = ref.watch(appRouter);
  void invalidator() => ref.invalidateSelf();
  router.addListener(invalidator);
  ref.onDispose(() => router.removeListener(invalidator));
  return _fetchCurrentRouteData(name, router);
}

extension AppRouterX on AppRouter {
  /// Navigate to the given [entryId] in the current page. If the entry is not in the current page, navigate to the page
  /// containing the entry and then navigate to the entry.
  /// Returns true if the page was changed.
  Future<bool> navigateToEntry(PassingRef ref, String entryId) async {
    final currentPage = ref.read(currentPageProvider);

    var changedPage = false;

    if (currentPage != null && currentPage.entries.none((e) => e.id == entryId)) {
      final entryPage = ref.read(pagesProvider).firstWhereOrNull((p) => p.entries.any((e) => e.id == entryId));
      if (entryPage != null) {
        await ref.read(appRouter).push(PageEditorRoute(id: entryPage.name));
        changedPage = true;
      }
    }

    ref.read(entriesViewProvider.notifier).navigateToViewFor(entryId);
    return changedPage;
  }
}
