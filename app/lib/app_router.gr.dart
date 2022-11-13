// **************************************************************************
// AutoRouteGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouteGenerator
// **************************************************************************
//
// ignore_for_file: type=lint

part of 'app_router.dart';

class _$AppRouter extends RootStackRouter {
  _$AppRouter([GlobalKey<NavigatorState>? navigatorKey]) : super(navigatorKey);

  @override
  final Map<String, PageFactory> pagesMap = {
    HomeRoute.name: (routeData) {
      return CustomPage<dynamic>(
          routeData: routeData,
          child: const HomePage(),
          transitionsBuilder: slideLeftWithFade,
          opaque: true,
          barrierDismissible: false);
    },
    BookRoute.name: (routeData) {
      return CustomPage<dynamic>(
          routeData: routeData,
          child: const BookPage(),
          transitionsBuilder: slideLeftWithFade,
          opaque: true,
          barrierDismissible: false);
    },
    PagesListRoute.name: (routeData) {
      return CustomPage<dynamic>(
          routeData: routeData,
          child: const PagesList(),
          transitionsBuilder: slideLeftWithFade,
          opaque: true,
          barrierDismissible: false);
    },
    EmptyPageEditorRoute.name: (routeData) {
      return CustomPage<dynamic>(
          routeData: routeData,
          child: const EmptyPageEditor(),
          transitionsBuilder: slideLeftWithFade,
          opaque: true,
          barrierDismissible: false);
    },
    PageEditorRoute.name: (routeData) {
      final pathParams = routeData.inheritedPathParams;
      final args = routeData.argsAs<PageEditorRouteArgs>(
          orElse: () =>
              PageEditorRouteArgs(pageId: pathParams.getString('id')));
      return CustomPage<dynamic>(
          routeData: routeData,
          child: PageEditor(key: args.key, pageId: args.pageId),
          transitionsBuilder: slideLeftWithFade,
          opaque: true,
          barrierDismissible: false);
    }
  };

  @override
  List<RouteConfig> get routes => [
        RouteConfig(HomeRoute.name, path: '/'),
        RouteConfig(BookRoute.name, path: '/book', children: [
          RouteConfig('#redirect',
              path: '',
              parent: BookRoute.name,
              redirectTo: 'pages',
              fullMatch: true),
          RouteConfig(PagesListRoute.name,
              path: 'pages',
              parent: BookRoute.name,
              children: [
                RouteConfig(EmptyPageEditorRoute.name,
                    path: '', parent: PagesListRoute.name),
                RouteConfig(PageEditorRoute.name,
                    path: ':id', parent: PagesListRoute.name)
              ])
        ])
      ];
}

/// generated route for
/// [HomePage]
class HomeRoute extends PageRouteInfo<void> {
  const HomeRoute() : super(HomeRoute.name, path: '/');

  static const String name = 'HomeRoute';
}

/// generated route for
/// [BookPage]
class BookRoute extends PageRouteInfo<void> {
  const BookRoute({List<PageRouteInfo>? children})
      : super(BookRoute.name, path: '/book', initialChildren: children);

  static const String name = 'BookRoute';
}

/// generated route for
/// [PagesList]
class PagesListRoute extends PageRouteInfo<void> {
  const PagesListRoute({List<PageRouteInfo>? children})
      : super(PagesListRoute.name, path: 'pages', initialChildren: children);

  static const String name = 'PagesListRoute';
}

/// generated route for
/// [EmptyPageEditor]
class EmptyPageEditorRoute extends PageRouteInfo<void> {
  const EmptyPageEditorRoute() : super(EmptyPageEditorRoute.name, path: '');

  static const String name = 'EmptyPageEditorRoute';
}

/// generated route for
/// [PageEditor]
class PageEditorRoute extends PageRouteInfo<PageEditorRouteArgs> {
  PageEditorRoute({Key? key, required String pageId})
      : super(PageEditorRoute.name,
            path: ':id',
            args: PageEditorRouteArgs(key: key, pageId: pageId),
            rawPathParams: {'id': pageId});

  static const String name = 'PageEditorRoute';
}

class PageEditorRouteArgs {
  const PageEditorRouteArgs({this.key, required this.pageId});

  final Key? key;

  final String pageId;

  @override
  String toString() {
    return 'PageEditorRouteArgs{key: $key, pageId: $pageId}';
  }
}
