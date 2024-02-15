import "package:auto_route/auto_route.dart";
import "package:collection/collection.dart";
import "package:flutter/material.dart" hide Page, SearchBar;
import "package:flutter/services.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/models/staging.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/smart_single_activator.dart";
import "package:typewriter/widgets/components/app/cinematic_view.dart";
import "package:typewriter/widgets/components/app/entries_graph.dart";
import "package:typewriter/widgets/components/app/manifest_view.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/app/staging.dart";
import "package:typewriter/widgets/components/app/static_entries_list.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/always_focused.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/components/general/shortcut_label.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "page_editor.g.dart";

@Riverpod(keepAlive: true)
String? currentPageId(CurrentPageIdRef ref) {
  final routeData = ref.watch(currentRouteDataProvider(PageEditorRoute.name));
  return routeData?.pathParams.getString("id");
}

@riverpod
String currentPageLabel(CurrentPageLabelRef ref) {
  return ref.watch(currentPageIdProvider)?.formatted ?? "";
}

@riverpod
Page? currentPage(CurrentPageRef ref) {
  final id = ref.watch(currentPageIdProvider);
  final book = ref.watch(bookProvider);

  return book.pages.firstWhereOrNull((element) => element.pageName == id);
}

@riverpod
PageType? currentPageType(CurrentPageTypeRef ref) {
  return ref.watch(currentPageProvider.select((page) => page?.type));
}

@RoutePage(name: "PageEditorRoute")
class PageEditor extends HookConsumerWidget {
  const PageEditor({
    @PathParam("id") required this.id,
    super.key,
  });

  final String id;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Shortcuts(
      key: Key(id),
      shortcuts: {
        SmartSingleActivator(LogicalKeyboardKey.keyK, control: true):
            SearchIntent(),
        SmartSingleActivator(LogicalKeyboardKey.space, control: true):
            SearchIntent(),
        const SingleActivator(LogicalKeyboardKey.keyP, control: true):
            const PreviousFocusIntent(),
        const SingleActivator(LogicalKeyboardKey.keyN, control: true):
            const NextFocusIntent(),
        SmartSingleActivator(
          LogicalKeyboardKey.keyP,
          control: true,
          shift: true,
        ): const PublishPagesIntent(),
      },
      child: Actions(
        actions: {
          SearchIntent: CallbackAction<SearchIntent>(
            onInvoke: (intent) {
              ref.read(searchProvider.notifier).startGlobalSearch();
              return null;
            },
          ),
          PublishPagesIntent: CallbackAction<PublishPagesIntent>(
            onInvoke: (intent) {
              ref.read(communicatorProvider).publish();
              return null;
            },
          ),
        },
        child: AlwaysFocused(
          child: ColoredBox(
            color: Theme.of(context).scaffoldBackgroundColor,
            child: const _Content(),
          ),
        ),
      ),
    );
  }
}

class _Content extends HookConsumerWidget {
  const _Content();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return const Stack(
      children: [
        Column(
          children: [
            _AppBar(key: Key("appBar")),
            Divider(),
            Expanded(
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Expanded(
                    child: _PageContent(),
                  ),
                  VerticalDivider(),
                  _Inspector(),
                ],
              ),
            ),
          ],
        ),
        SearchBar(),
      ],
    );
  }
}

@riverpod
List<Writer> _writers(_WritersRef ref) {
  final pageId = ref.watch(currentPageIdProvider);
  return ref
      .watch(writersProvider)
      .where((writer) => writer.pageId == pageId)
      .toList();
}

class _AppBar extends HookConsumerWidget {
  const _AppBar({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pageType = ref.watch(currentPageTypeProvider);
    return Padding(
      padding: const EdgeInsets.only(top: 8.0),
      child: Row(
        children: [
          const SizedBox(width: 20),
          Iconify(pageType?.icon, size: 16),
          const SizedBox(width: 8),
          Text(ref.watch(currentPageLabelProvider)),
          const SizedBox(width: 5),
          const Spacer(),
          Writers(writers: ref.watch(_writersProvider)),
          const SizedBox(width: 20),
          const StagingIndicator(key: Key("staging-indicator")),
          const SizedBox(width: 20),
          const _SearchBar(),
          const SizedBox(width: 5),
          const _AddEntryButton(),
          const SizedBox(width: 10),
        ],
      ),
    );
  }
}

class _SearchBar extends HookConsumerWidget {
  const _SearchBar() : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) => Material(
        color: Theme.of(context).inputDecorationTheme.fillColor,
        borderRadius: BorderRadius.circular(8),
        child: InkWell(
          borderRadius: BorderRadius.circular(8),
          onTap: () => Actions.invoke(context, SearchIntent()),
          child: Padding(
            padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 20),
            child: Row(
              children: [
                const Iconify(
                  TWIcons.magnifyingGlass,
                  size: 16,
                  color: Colors.grey,
                ),
                const SizedBox(width: 5),
                const Text("Search", style: TextStyle(color: Colors.grey)),
                const SizedBox(width: 50),
                ShortcutLabel(
                  activator: SmartSingleActivator(
                    LogicalKeyboardKey.keyK,
                    control: true,
                  ),
                ),
              ],
            ),
          ),
        ),
      );
}

class _AddEntryButton extends HookConsumerWidget {
  const _AddEntryButton() : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return IconButton(
      iconSize: 16,
      padding: EdgeInsets.zero,
      icon: const Iconify(TWIcons.plus),
      onPressed: () => ref.read(searchProvider.notifier).startAddSearch(),
    );
  }
}

class _Inspector extends HookConsumerWidget {
  const _Inspector();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pageType = ref.watch(currentPageTypeProvider);
    if (pageType == null) {
      return const SizedBox();
    }

    switch (pageType) {
      case PageType.sequence || PageType.static || PageType.manifest:
        return const GenericInspector();
      case PageType.cinematic:
        return const CinematicInspector();
    }
  }
}

class _PageContent extends HookConsumerWidget {
  const _PageContent();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pageType = ref.watch(currentPageTypeProvider);
    if (pageType == null) {
      return const SizedBox();
    }

    switch (pageType) {
      case PageType.sequence:
        return const EntriesGraph();
      case PageType.static:
        return const StaticEntriesList();
      case PageType.cinematic:
        return const CinematicView();
      case PageType.manifest:
        return const ManifestView();
    }
  }
}
