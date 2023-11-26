// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouterGenerator
// **************************************************************************

// ignore_for_file: type=lint
// coverage:ignore-file

part of 'app_router.dart';

abstract class _$AppRouter extends RootStackRouter {
  // ignore: unused_element
  _$AppRouter({super.navigatorKey});

  @override
  final Map<String, PageFactory> pagesMap = {
    BookRoute.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const BookPage(),
      );
    },
    ConnectRoute.name: (routeData) {
      final queryParams = routeData.queryParams;
      final args = routeData.argsAs<ConnectRouteArgs>(
          orElse: () => ConnectRouteArgs(
                hostname: queryParams.getString(
                  'host',
                  "",
                ),
                port: queryParams.getInt(
                  'port',
                  9092,
                ),
                token: queryParams.getString(
                  'token',
                  "",
                ),
              ));
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: ConnectPage(
          hostname: args.hostname,
          port: args.port,
          token: args.token,
          key: args.key,
        ),
      );
    },
    EmptyPageEditorRoute.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const EmptyPageEditor(),
      );
    },
    ErrorConnectRoute.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const ErrorConnectPage(),
      );
    },
    HomeRoute.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const HomePage(),
      );
    },
    PageEditorRoute.name: (routeData) {
      final pathParams = routeData.inheritedPathParams;
      final args = routeData.argsAs<PageEditorRouteArgs>(
          orElse: () => PageEditorRouteArgs(id: pathParams.getString('id')));
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: PageEditor(
          id: args.id,
          key: args.key,
        ),
      );
    },
    PagesListRoute.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const PagesList(),
      );
    },
  };
}

/// generated route for
/// [BookPage]
class BookRoute extends PageRouteInfo<void> {
  const BookRoute({List<PageRouteInfo>? children})
      : super(
          BookRoute.name,
          initialChildren: children,
        );

  static const String name = 'BookRoute';

  static const PageInfo<void> page = PageInfo<void>(name);
}

/// generated route for
/// [ConnectPage]
class ConnectRoute extends PageRouteInfo<ConnectRouteArgs> {
  ConnectRoute({
    String hostname = "",
    int port = 9092,
    String token = "",
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          ConnectRoute.name,
          args: ConnectRouteArgs(
            hostname: hostname,
            port: port,
            token: token,
            key: key,
          ),
          rawQueryParams: {
            'host': hostname,
            'port': port,
            'token': token,
          },
          initialChildren: children,
        );

  static const String name = 'ConnectRoute';

  static const PageInfo<ConnectRouteArgs> page =
      PageInfo<ConnectRouteArgs>(name);
}

class ConnectRouteArgs {
  const ConnectRouteArgs({
    this.hostname = "",
    this.port = 9092,
    this.token = "",
    this.key,
  });

  final String hostname;

  final int port;

  final String token;

  final Key? key;

  @override
  String toString() {
    return 'ConnectRouteArgs{hostname: $hostname, port: $port, token: $token, key: $key}';
  }
}

/// generated route for
/// [EmptyPageEditor]
class EmptyPageEditorRoute extends PageRouteInfo<void> {
  const EmptyPageEditorRoute({List<PageRouteInfo>? children})
      : super(
          EmptyPageEditorRoute.name,
          initialChildren: children,
        );

  static const String name = 'EmptyPageEditorRoute';

  static const PageInfo<void> page = PageInfo<void>(name);
}

/// generated route for
/// [ErrorConnectPage]
class ErrorConnectRoute extends PageRouteInfo<void> {
  const ErrorConnectRoute({List<PageRouteInfo>? children})
      : super(
          ErrorConnectRoute.name,
          initialChildren: children,
        );

  static const String name = 'ErrorConnectRoute';

  static const PageInfo<void> page = PageInfo<void>(name);
}

/// generated route for
/// [HomePage]
class HomeRoute extends PageRouteInfo<void> {
  const HomeRoute({List<PageRouteInfo>? children})
      : super(
          HomeRoute.name,
          initialChildren: children,
        );

  static const String name = 'HomeRoute';

  static const PageInfo<void> page = PageInfo<void>(name);
}

/// generated route for
/// [PageEditor]
class PageEditorRoute extends PageRouteInfo<PageEditorRouteArgs> {
  PageEditorRoute({
    required String id,
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          PageEditorRoute.name,
          args: PageEditorRouteArgs(
            id: id,
            key: key,
          ),
          rawPathParams: {'id': id},
          initialChildren: children,
        );

  static const String name = 'PageEditorRoute';

  static const PageInfo<PageEditorRouteArgs> page =
      PageInfo<PageEditorRouteArgs>(name);
}

class PageEditorRouteArgs {
  const PageEditorRouteArgs({
    required this.id,
    this.key,
  });

  final String id;

  final Key? key;

  @override
  String toString() {
    return 'PageEditorRouteArgs{id: $id, key: $key}';
  }
}

/// generated route for
/// [PagesList]
class PagesListRoute extends PageRouteInfo<void> {
  const PagesListRoute({List<PageRouteInfo>? children})
      : super(
          PagesListRoute.name,
          initialChildren: children,
        );

  static const String name = 'PagesListRoute';

  static const PageInfo<void> page = PageInfo<void>(name);
}
