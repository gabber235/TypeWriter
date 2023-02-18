import "package:auto_route/auto_route.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/communicator.dart";

class ConnectedGuard extends AutoRouteGuard {
  ConnectedGuard(this.ref);
  final Ref<dynamic> ref;

  @override
  void onNavigation(NavigationResolver resolver, StackRouter router) {
    final hasConnection = ref.read(socketProvider) != null;
    if (!hasConnection) {
      resolver.next(false);
      router.replaceAll([const HomeRoute()]);
      return;
    }
    resolver.next();
  }
}
