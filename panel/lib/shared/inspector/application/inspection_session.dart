import "package:flutter/foundation.dart";
import "package:flutter/scheduler.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Composes selections while retaining drafts by resource identity.
final class InspectionSession extends ChangeNotifier {
  InspectionSession(this.ref)
    : owners = EditorOwnerRegistry(workspace: ref.read(localWorkProvider)) {
    ref.listen(inspectedSelectionProvider, (_, next) {
      if (next.asError?.error case SelectableNotFoundException(:final id)) {
        owners.deleted(id);
        SchedulerBinding.instance.addPostFrameCallback((_) {
          if (!_disposed) ref.read(selectionProvider.notifier).unselect(id);
        });
        SchedulerBinding.instance.ensureVisualUpdate();
      } else if (next.isLoading || next.hasError) {
        owners.unavailable(
          next.hasError
              ? "The selected resources could not be refreshed"
              : "The selected resources are refreshing",
        );
        notifyListeners();
      } else {
        _refresh();
      }
    });
    _refresh();
  }
  final Ref ref;
  bool _disposed = false;
  final EditorOwnerRegistry owners;
  final List<MultiEditOwner> _groups = [];
  PresentationModel? model;

  void _refresh() {
    final selection = ref.read(inspectedSelectionProvider).value;
    if (selection == null) return;
    for (final group in _groups) {
      group.dispose();
    }
    _groups.clear();
    final router = ref.read(appRouterProvider);
    final path = router.currentPath;
    final container = ref.container;
    owners.destinationFor = (identity) => InspectorDestination(
      container: container,
      router: router,
      path: path,
      identity: identity,
    );
    owners.begin();
    final models = selection
        .map((item) => item.buildPresentation(owners))
        .toList();
    owners.end();
    model = switch (models.length) {
      0 => null,
      1 => models.single,
      _ => _combine(models),
    };
    model = model?.copyWith(ownerLabels: owners.labels);
    notifyListeners();
  }

  PresentationModel _combine(List<PresentationModel> models) {
    final catalog = TypeCatalog(
      models.expand((model) => model.catalog.definitions).toSet().toList(),
    );
    final inputsBySelection = models
        .map(
          (model) => model.inputs.values
              .whereType<PresentationEditInput>()
              .map(
                (input) => input.path == DataPath.root
                    ? input.owner
                    : ProjectedEditOwner(input.owner, input.path),
              )
              .toSet()
              .toList(),
        )
        .toList();
    final combinations = <List<EditOwner>>[];
    if (inputsBySelection.every((inputs) => inputs.length == 1)) {
      combinations.add(
        inputsBySelection.map((inputs) => inputs.single).toSet().toList(),
      );
    } else {
      for (final first in inputsBySelection.first) {
        final matches = <EditOwner>[first];
        for (final candidates in inputsBySelection.skip(1)) {
          final match = candidates
              .where(
                (owner) => typeExpressionsEqual(owner.rootType, first.rootType),
              )
              .firstOrNull;
          if (match != null) matches.add(match);
        }
        if (matches.length == models.length) {
          combinations.add(matches.toSet().toList());
        }
      }
    }
    final inputs = <BindingId, PresentationInput>{};
    final children = <PresentationNode>[];
    for (final members in combinations) {
      final types = members
          .map(
            (owner) => owner.rootType is NamedType
                ? TypeRegistry(owner.typeCatalog)
                          .resolve(owner.rootType as NamedType)
                          .valueOrNull
                          ?.representation ??
                      owner.rootType
                : owner.rootType,
          )
          .toList();
      final common = types.commonEditableProjection().valueOrNull;
      if (common == null) continue;
      final owner = MultiEditOwner(
        owners: members,
        rootType: common,
        typeCatalog: catalog,
      );
      _groups.add(owner);
      final id = BindingId(inputs.length);
      inputs[id] = PresentationInput.edit(owner);
      children.add(
        common.generateDefaultPresentation(
          binding: BindingReference(bindingId: id),
          nodeId: "selection.${id.value}",
        ),
      );
    }
    return PresentationModel(
      catalog: catalog,
      inputs: inputs,
      root: PresentationNode(
        id: "selection",
        element: ColumnElement(children: children),
      ),
    );
  }

  Future<Map<Object, TypedMutationResult>> flush({bool failedOnly = false}) =>
      owners.flush(failedOnly: failedOnly);

  @override
  void dispose() {
    _disposed = true;
    for (final group in _groups) {
      group.dispose();
    }
    owners.dispose();
    super.dispose();
  }
}

/// Restores a resource selection and its route without retaining an inspector.
final class InspectorDestination extends EditorDestination {
  InspectorDestination({
    required this.container,
    required this.router,
    required this.path,
    required this.identity,
  }) {
    router.addListener(notifyListeners);
    _selection = container.listen(
      selectionProvider,
      (_, _) => notifyListeners(),
    );
  }

  final ProviderContainer container;
  final AppRouter router;
  final String path;
  final Object identity;
  late final ProviderSubscription<List<SelectableIdentifier>> _selection;

  @override
  bool get isCurrent =>
      router.currentPath == path &&
      (identity is! SelectableIdentifier ||
          container.read(selectionProvider).contains(identity));

  @override
  Future<void> open() async {
    if (router.currentPath != path) await router.navigatePath(path);
    if (identity case final SelectableIdentifier selected) {
      container.read(selectionProvider.notifier).selectAll([selected]);
    }
  }

  @override
  void dispose() {
    router.removeListener(notifyListeners);
    _selection.close();
    super.dispose();
  }
}
