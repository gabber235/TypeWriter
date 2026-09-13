import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod/riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

part "pages.freezed.dart";
part "pages.g.dart";

@freezed
abstract class Page with _$Page {
  @Assert("name != \"\"", "Name must not be empty.")
  const factory Page({
    required skir.RecordId pageId,
    required skir.RecordId bookId,
    required String name,
    required PageKindRef kind,
    required String chapter,
    required int priority,
  }) = _Page;

  const Page._();

  factory Page.fromWire(wire.Page page) => Page(
    pageId: page.id,
    bookId: page.book,
    name: page.name,
    kind: PageKindRef.fromSkir(page.kind),
    chapter: page.chapter,
    priority: page.priority,
  );

  RecordValue get editorValue => RecordValue({
    "name": name.asValue,
    "chapter": chapter.asValue,
    "priority": priority.asValue,
  });

  Page? withEditorValue(DataValue value) {
    if (value is! RecordValue) return null;
    final name = value.fields["name"];
    final chapter = value.fields["chapter"];
    final priority = value.fields["priority"];
    if (name is! StringValue ||
        name.value.trim().isEmpty ||
        chapter is! StringValue ||
        priority is! IntegerValue) {
      return null;
    }
    return copyWith(
      name: name.value,
      chapter: chapter.value,
      priority: priority.value.toInt(),
    );
  }

  Page projected(LocalEditorValue? local) {
    if (local == null) return this;
    return withEditorValue(local.projectOnto(editorValue)) ?? this;
  }
}

@riverpod
class CanonicalBookPages extends _$CanonicalBookPages {
  @override
  Future<List<Page>> build(skir.RecordId bookId) async {
    final organizationId = ref.watch(organizationIdProvider);
    final realmId = ref.watch(realmIdProvider);
    if (organizationId == null) throw ApiException.noOrganization();
    if (realmId == null) throw ApiException.badRequest("No realm selected");
    final provider = authoringSessionProvider(organizationId, realmId);
    List<Page> project(AuthoringSessionState value) {
      return value.pages.values
          .where((page) => page.book == bookId)
          .map(Page.fromWire)
          .toList();
    }

    ref.listen(provider, (_, value) {
      if (value.sequence != null) state = AsyncData(project(value));
    });

    final lease = ref.watch(
      authoringBookScopeProvider(organizationId, realmId, bookId),
    );
    await lease.ready;
    return project(ref.read(provider));
  }
}

@riverpod
class CanonicalPage extends _$CanonicalPage {
  @override
  Future<Page> build(skir.RecordId pageId) async {
    final organizationId = ref.watch(organizationIdProvider);
    final realmId = ref.watch(realmIdProvider);
    if (organizationId == null) throw ApiException.noOrganization();
    if (realmId == null) throw ApiException.badRequest("No realm selected");
    final provider = authoringSessionProvider(organizationId, realmId);
    ref.listen(provider, (_, value) {
      if (value.sequence == null) return;
      final page = value.pages[pageId];
      state = page == null
          ? AsyncError(ApiException.notFound("Page"), StackTrace.current)
          : AsyncData(Page.fromWire(page));
    });

    final lease = ref.watch(
      authoringPageScopeProvider(organizationId, realmId, pageId),
    );
    await lease.ready;
    final value = ref.read(provider);
    final page = value.pages[pageId];
    if (page == null) throw ApiException.notFound("Page");
    return Page.fromWire(page);
  }
}

@riverpod
AsyncValue<List<Page>> projectedBookPages(
  Ref ref,
  skir.RecordId bookId,
  String search,
) {
  final canonical = ref.watch(canonicalBookPagesProvider(bookId));
  if (canonical.mapUnready<List<Page>>() case final value?) return value;
  final organizationId = ref.watch(organizationIdProvider);
  final realmId = ref.watch(realmIdProvider);
  if (organizationId == null || realmId == null) {
    return AsyncData(canonical.requireValue);
  }
  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues),
  );
  final query = search.trim().toLowerCase();
  return AsyncData([
    for (final page in canonical.requireValue)
      if (page.projected(
            local[EditorResourceKey(
              scope: EditorResourceScope(
                organizationId: organizationId,
                realmId: realmId,
              ),
              identity: page.pageId,
            )],
          )
          case final projected
          when query.isEmpty ||
              projected.name.toLowerCase().contains(query) ||
              projected.chapter.toLowerCase().contains(query))
        projected,
  ]);
}

@riverpod
AsyncValue<Page> projectedPage(Ref ref, skir.RecordId pageId) {
  final canonical = ref.watch(canonicalPageProvider(pageId));
  if (canonical.mapUnready<Page>() case final value?) return value;
  final organizationId = ref.watch(organizationIdProvider);
  final realmId = ref.watch(realmIdProvider);
  if (organizationId == null) {
    return AsyncError(ApiException.noOrganization(), StackTrace.current);
  }
  if (realmId == null) {
    return AsyncError(
      ApiException.badRequest("No realm selected"),
      StackTrace.current,
    );
  }
  final key = EditorResourceKey(
    scope: EditorResourceScope(
      organizationId: organizationId,
      realmId: realmId,
    ),
    identity: pageId,
  );
  final local = ref.watch(
    localWorkProvider.select((state) => state.editorValues[key]),
  );
  return AsyncData(canonical.requireValue.projected(local));
}

@riverpod
skir.RecordId? pageId(Ref ref) {
  final id = ref.watch(routeParamProvider("pageId"));
  if (id == null) return null;
  return recordId("page:$id");
}
