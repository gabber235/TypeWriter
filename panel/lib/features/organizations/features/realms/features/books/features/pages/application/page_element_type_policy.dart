import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "page_element_type_policy.freezed.dart";
part "page_element_type_policy.g.dart";

/// Availability of the concrete element types permitted by a page kind.
///
/// Loading and unavailable states are deliberate UI outcomes. Consumers must
/// not infer that an empty ready set means catalog failure.
@freezed
sealed class PageElementTypesState with _$PageElementTypesState {
  /// The catalog or subtype queries have not completed.
  const factory PageElementTypesState.loading() = PageElementTypesLoading;

  /// The page kind's roots and concrete descendants are available.
  const factory PageElementTypesState.ready(Set<ResolvedTypeRef> types) =
      PageElementTypesReady;

  /// The catalog cannot establish a safe element type policy.
  const factory PageElementTypesState.unavailable(
    List<TypeDiagnostic> diagnostics,
  ) = PageElementTypesUnavailable;
}

/// Resolves the concrete element types allowed by [pageKind].
///
/// Page catalog definitions provide graph node types or timeline track,
/// segment, and keyframe roots. The realm catalog lease supplies subtype
/// results, and abstract matches are removed before the ready state is emitted.
/// Catalog failure remains visible so editor consumers can disable creation and
/// show diagnostics instead of treating incomplete data as permission.
@riverpod
Stream<PageElementTypesState> pageElementTypes(Ref ref, PageKindRef pageKind) {
  final cache = ref.watch(realmEditorCatalogCacheProvider);
  if (cache == null) {
    return Stream.value(
      PageElementTypesUnavailable([
        realmEditorCatalogUnavailableDiagnostic(
          "The page element type catalogue is unavailable",
        ),
      ]),
    );
  }
  final state = ref.watch(realmEditorCatalogProvider).value;
  final definition = state?.snapshot?.pageCatalog.definitions[pageKind];
  if (definition == null) {
    return Stream.value(
      PageElementTypesUnavailable([
        realmEditorCatalogUnavailableDiagnostic(
          "The page kind is unavailable in the active catalog",
        ),
      ]),
    );
  }
  final roots = switch (definition.editor) {
    RealmGraphPageEditor(:final nodeTypes) => nodeTypes,
    RealmTimelinePageEditor(
      :final trackTypes,
      :final segmentTypes,
      :final keyframeTypes,
    ) =>
      [...trackTypes, ...segmentTypes, ...keyframeTypes],
  };

  final queries = [
    for (final root in roots.indexed)
      RealmEditorSubtypeQuery(
        id: "page:${pageKind.id}:${pageKind.revision}:${root.$1}",
        target: root.$2,
      ),
  ];
  ref.watch(
    realmEditorCatalogLeaseProvider(
      RealmEditorCatalogRequest(
        types: roots.toSet(),
        subtypeQueries: queries.toSet(),
      ),
    ),
  );
  return cache.states.map(
    (state) => state._pageElementTypes(roots, queries.map((query) => query.id)),
  );
}

extension on RealmEditorCatalogState {
  /// Maps the current catalog observation to the page type policy state.
  PageElementTypesState _pageElementTypes(
    Iterable<ResolvedTypeRef> roots,
    Iterable<String> queryIds,
  ) {
    final current = snapshot;
    final results = queryIds
        .map((id) => current?.subtypeResults[id])
        .whereType<RealmEditorSubtypeResult>()
        .toList();
    if (current != null && results.length == queryIds.length) {
      final registry = TypeRegistry(current.catalog);
      final concrete = [...roots, ...results.expand((result) => result.matches)]
          .where((reference) {
            final resolved = registry.resolveExact(reference).valueOrNull;
            return resolved?.isConcrete ?? false;
          });
      return PageElementTypesReady(concrete.toSet());
    }
    return switch (this) {
      RealmEditorCatalogUnavailable(:final diagnostics) =>
        PageElementTypesUnavailable(diagnostics),
      _ => const PageElementTypesLoading(),
    };
  }
}
