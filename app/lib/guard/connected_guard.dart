import "package:auto_route/auto_route.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/utils/passing_reference.dart";

class ConnectedGuard extends AutoRouteGuard {
  ConnectedGuard(this.ref);
  final PassingRef ref;

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
