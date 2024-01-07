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

final appRouter = Provider<AppRouter>((ref) => AppRouter(ref: ref.passing));

@AutoRouterConfig(
  replaceInRouteName: "Page,Route",
)
class AppRouter extends _$AppRouter {
  AppRouter({
    required this.ref,
  });

  final PassingRef ref;

  @override
  RouteType get defaultRouteType => const RouteType.custom(
        transitionsBuilder: TransitionsBuilders.noTransition,
      );

  @override
  List<AutoRoute> get routes => [
        AutoRoute(page: HomeRoute.page, path: "/"),
        AutoRoute(page: ConnectRoute.page, path: "/connect"),
        AutoRoute(page: ErrorConnectRoute.page, path: "/error"),
        AutoRoute(
          page: BookRoute.page,
          path: "/book",
          guards: [ConnectedGuard(ref)],
          children: [
            AutoRoute(
              page: PagesListRoute.page,
              path: "pages",
              children: [
                AutoRoute(page: EmptyPageEditorRoute.page, path: ""),
                AutoRoute(page: PageEditorRoute.page, path: ":id"),
              ],
            ),
          ],
        ),
      ];
}

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
RouteData? currentRouteData(CurrentRouteDataRef ref, String path) {
  final router = ref.watch(appRouter);
  void invalidator() => ref.invalidateSelf();
  router.addListener(invalidator);
  ref.onDispose(() => router.removeListener(invalidator));
  return _fetchCurrentRouteData(path, router);
}

extension AppRouterX on AppRouter {
  /// Navigate to the given [entryId] in the current page. If the entry is not in the current page, navigate to the page
  /// containing the entry and then navigate to the entry.
  /// Returns true if the page was changed.
  Future<void> navigateToEntry(PassingRef ref, String entryId) async {
    final currentPage = ref.read(currentPageProvider);

    if (currentPage != null &&
        currentPage.entries.none((e) => e.id == entryId)) {
      final entryPage = ref.read(globalEntryWithPageProvider(entryId))?.key;
      if (entryPage != null) {
        await navigateToPage(ref, entryPage);
      }
    }
  }

  /// Navigate to the given [pageName].
  /// Returns true if the page was changed.
  Future<void> navigateToPage(PassingRef ref, String pageName) async {
    final currentPage = ref.read(currentPageProvider);

    if (currentPage?.pageName != pageName) {
      await ref.read(appRouter).push(
            PageEditorRoute(id: pageName),
            onFailure: (e) =>
                debugPrint("Failed to navigate to page $pageName: $e"),
          );
    }
  }
}
