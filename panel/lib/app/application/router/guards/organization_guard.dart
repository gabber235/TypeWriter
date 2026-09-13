part of "../app_router.dart";

/// Protects routes scoped to an organization membership.
///
/// Invalid route parameters and confirmed nonmembership are cancelled and
/// redirected to the index. Temporary access unavailability is allowed through
/// because the startup and binding layers own recovery while a stable
/// membership change triggers reevaluation.
final class _OrganizationGuard extends AutoRouteGuard {
  _OrganizationGuard(this.access, this.redirectCoordinator);

  final OrganizationRouteAccess access;
  final _IndexRedirectCoordinator redirectCoordinator;

  @override
  Future<void> onNavigation(
    NavigationResolver resolver,
    StackRouter router,
  ) async {
    final wasReevaluating = resolver.isReevaluating;
    final organizationId = resolver.route.params.optString("organizationId");
    if (organizationId == null || organizationId.isEmpty) {
      resolver.resolveNext(false, reevaluateNext: false);
      redirectCoordinator.schedule(
        router,
        reason: _AccessDenial.invalidRoute,
        shouldRedirect: () => router.stack.isEmpty,
      );
      return;
    }

    while (access.decisionFor(organizationId) ==
        OrganizationRouteDecision.loading) {
      await access.waitUntilReady();
    }
    if (resolver.isResolved) return;

    final decision = access.decisionFor(organizationId);
    if (decision == OrganizationRouteDecision.member ||
        decision == OrganizationRouteDecision.unavailable) {
      resolver.next();
      return;
    }
    resolver.resolveNext(false, reevaluateNext: false);
    redirectCoordinator.schedule(
      router,
      reason: wasReevaluating
          ? _AccessDenial.membershipRemoved
          : _AccessDenial.notAMember,
      shouldRedirect: () {
        if (router.stack.isEmpty) return true;
        return access.decisionFor(organizationId) ==
                OrganizationRouteDecision.nonMember &&
            router.topRoute.inheritedPathParams.optString("organizationId") ==
                organizationId;
      },
    );
  }
}
