part of "../app_router.dart";

/// Allows private routes only after authentication access is ready.
///
/// Unauthenticated users are sent to [AuthRoute]. Unavailable access fails
/// closed by cancelling navigation without redirecting, leaving the startup
/// boundary responsible for presenting the underlying error.
final class _AuthGuard extends AutoRouteGuard {
  const _AuthGuard(this.access);
  final AuthenticationRouteAccess access;

  @override
  Future<void> onNavigation(
    NavigationResolver resolver,
    StackRouter router,
  ) async {
    await access.waitUntilReady();
    if (resolver.isResolved) return;
    switch (access.decision) {
      case RouteAuthenticationAuthenticated():
        resolver.next();
      case RouteAuthenticationUnauthenticated():
        resolver.redirectUntil(const AuthRoute());
      case RouteAuthenticationLoading():
      case RouteAuthenticationUnavailable():
        resolver.next(false);
    }
  }
}

/// Keeps authenticated users out of the sign in route.
///
/// The attempted auth navigation is cancelled without reevaluation, then the
/// shared redirect coordinator returns the router to the index after the frame.
final class _UnAuthGuard extends AutoRouteGuard {
  const _UnAuthGuard(this.access, this.redirectCoordinator);
  final AuthenticationRouteAccess access;
  final _IndexRedirectCoordinator redirectCoordinator;

  @override
  Future<void> onNavigation(
    NavigationResolver resolver,
    StackRouter router,
  ) async {
    await access.waitUntilReady();
    if (resolver.isResolved) return;
    if (access.decision is! RouteAuthenticationAuthenticated) {
      resolver.next();
      return;
    }
    resolver.resolveNext(false, reevaluateNext: false);
    redirectCoordinator.schedule(
      router,
      shouldRedirect: () =>
          router.stack.isEmpty || router.topRoute.name == AuthRoute.name,
    );
  }
}
