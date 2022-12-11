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
          transitionsBuilder: TransitionsBuilders.noTransition,
          opaque: true,
          barrierDismissible: false);
    },
    BookRoute.name: (routeData) {
      return CustomPage<dynamic>(
          routeData: routeData,
          child: const BookPage(),
          transitionsBuilder: TransitionsBuilders.noTransition,
          opaque: true,
          barrierDismissible: false);
    },
    PagesListRoute.name: (routeData) {
      return CustomPage<dynamic>(
          routeData: routeData,
          child: const PagesList(),
          transitionsBuilder: TransitionsBuilders.noTransition,
          opaque: true,
          barrierDismissible: false);
    },
    EmptyPageEditorRoute.name: (routeData) {
      return CustomPage<dynamic>(
          routeData: routeData,
          child: const EmptyPageEditor(),
          transitionsBuilder: TransitionsBuilders.noTransition,
          opaque: true,
          barrierDismissible: false);
    },
    PageEditorRoute.name: (routeData) {
      final pathParams = routeData.inheritedPathParams;
      final args = routeData.argsAs<PageEditorRouteArgs>(
          orElse: () => PageEditorRouteArgs(id: pathParams.getString('id')));
      return CustomPage<dynamic>(
          routeData: routeData,
          child: PageEditor(id: args.id, key: args.key),
          transitionsBuilder: TransitionsBuilders.noTransition,
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
  PageEditorRoute({required String id, Key? key})
      : super(PageEditorRoute.name,
            path: ':id',
            args: PageEditorRouteArgs(id: id, key: key),
            rawPathParams: {'id': id});

  static const String name = 'PageEditorRoute';
}

class PageEditorRouteArgs {
  const PageEditorRouteArgs({required this.id, this.key});

  final String id;

  final Key? key;

  @override
  String toString() {
    return 'PageEditorRouteArgs{id: $id, key: $key}';
  }
}
