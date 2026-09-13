import "package:flutter/material.dart" hide SearchController;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:hooks_riverpod/legacy.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Controller scoped to one search surface.
///
/// The nullable default allows the provider to exist as a dependency anchor;
/// [SearchRoot] supplies the actual controller for its subtree.
final searchProvider = ChangeNotifierProvider<SearchController?>(
  (ref) => null,
  dependencies: [],
);

/// Creates and owns the controller visible to [child].
///
/// The [create] callback remains current without forcing provider recreation on
/// every rebuild. The provider owns the controller for this scope, and
/// Riverpod disposes the controller and its source when the scope is removed.
class SearchRoot extends HookConsumerWidget {
  const SearchRoot({required this.create, required this.child, super.key});

  final Widget child;
  final SearchController Function(Ref ref) create;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final latestCreate = useRef(create)..value = create;
    final stableCreate = useMemoized(
      () =>
          (providerRef) => latestCreate.value(providerRef),
    );
    return ProviderScope(
      overrides: [searchProvider.overrideWith(stableCreate)],
      child: child,
    );
  }
}
