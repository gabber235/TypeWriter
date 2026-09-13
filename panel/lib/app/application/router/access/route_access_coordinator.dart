import "package:flutter/foundation.dart";
import "package:typewriter_panel/app/application/router/access/authentication_route_access.dart";
import "package:typewriter_panel/app/application/router/access/organization_route_access.dart";
import "package:typewriter_panel/app/application/router/access/route_access_module.dart";

/// Owns the route access modules used by the application's router.
///
/// The coordinator forwards semantic access changes as one notification stream
/// for router reevaluation. It also owns module disposal, so callers dispose
/// this coordinator instead of managing authentication and organization access
/// separately.
final class RouteAccessCoordinator extends ChangeNotifier {
  RouteAccessCoordinator({
    required this.authentication,
    required this.organizations,
  }) : _modules = [authentication, organizations] {
    for (final module in _modules) {
      module.reevaluation.addListener(_forwardNotification);
    }
  }

  final AuthenticationRouteAccess authentication;
  final OrganizationRouteAccess organizations;
  final List<RouteAccessModule> _modules;

  void _forwardNotification() => notifyListeners();

  @override
  void dispose() {
    for (final module in _modules) {
      module.reevaluation.removeListener(_forwardNotification);
      module.dispose();
    }

    super.dispose();
  }
}
