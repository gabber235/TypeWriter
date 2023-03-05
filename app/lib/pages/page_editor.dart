import "package:auto_route/auto_route.dart";
import "package:collection/collection.dart";
import "package:flutter/material.dart" hide Page;
import "package:flutter/services.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/smart_single_activator.dart";
import "package:typewriter/widgets/components/app/cinematic_view.dart";
import "package:typewriter/widgets/components/app/entries_graph.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/app/select_entries.dart";
import "package:typewriter/widgets/components/app/staging.dart";
import "package:typewriter/widgets/components/app/static_entries_list.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/always_focused.dart";
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

  return book.pages.firstWhereOrNull((element) => element.name == id);
}

@riverpod
PageType? currentPageType(CurrentPageTypeRef ref) {
  return ref.watch(currentPageProvider.select((page) => page?.type));
}

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
        SmartSingleActivator(LogicalKeyboardKey.keyK, control: true): SearchIntent(),
      },
      child: Actions(
        actions: {
          SearchIntent: CallbackAction<SearchIntent>(
            onInvoke: (intent) {
              final tag = ref.read(currentPageTypeProvider)?.tag;
              if (tag == null) return;
              ref.read(searchProvider.notifier).startGlobalSearch(tag);
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
          FaIcon(pageType?.icon, size: 16),
          const SizedBox(width: 8),
          Text(ref.watch(currentPageLabelProvider)),
          const SizedBox(width: 5),
          const Spacer(),
          // When selecting entries, we want to disable these interactions
          const SelectingEntriesBlocker(
            child: Row(
              children: [
                GlobalWriters(),
                SizedBox(width: 20),
                StagingIndicator(key: Key("staging-indicator")),
                SizedBox(width: 20),
                _SearchBar(),
                SizedBox(width: 5),
                _AddEntryButton(),
                SizedBox(width: 10),
              ],
            ),
          ),
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
                const Icon(
                  FontAwesomeIcons.magnifyingGlass,
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
  const _AddEntryButton({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tag = ref.watch(currentPageTypeProvider.select((type) => type?.tag));
    if (tag == null) {
      return const SizedBox();
    }
    return IconButton(
      iconSize: 16,
      padding: EdgeInsets.zero,
      icon: const Icon(FontAwesomeIcons.plus),
      onPressed: () => ref.read(searchProvider.notifier).startAddSearch(tag),
    );
  }
}

class _Inspector extends HookConsumerWidget {
  const _Inspector();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelectingEntries = ref.watch(isSelectingEntriesProvider);

    // When we are selecting entries, we want a special inspector that allows
    // us to select entries.
    if (isSelectingEntries) {
      return const EntriesSelectorInspector();
    }

    final pageType = ref.watch(currentPageTypeProvider);
    if (pageType == null) {
      return const SizedBox();
    }

    switch (pageType) {
      case PageType.sequence: return const EntryInspector();
      case PageType.static: return const EntryInspector();
      case PageType.cinematic: return const EntryInspector();
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
      case PageType.sequence: return const EntriesGraph();
      case PageType.static: return const StaticEntriesList();
      case PageType.cinematic: return const CinematicView();
    }
  }
}
