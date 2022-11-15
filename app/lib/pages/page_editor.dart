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
import "package:typewriter/widgets/always_focused.dart";
import "package:typewriter/widgets/auto_saver.dart";
import "package:typewriter/widgets/entries_graph.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/search_bar.dart";
import "package:typewriter/widgets/shortcut_label.dart";

part "page_editor.g.dart";

@riverpod
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

class PageEditor extends HookConsumerWidget {
  const PageEditor({
    @PathParam("id") required this.id,
    super.key,
  });

  final String id;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Shortcuts(
      shortcuts: {
        SmartSingleActivator(LogicalKeyboardKey.keyK, control: true): SearchIntent(),
      },
      child: Actions(
        actions: {
          SearchIntent: CallbackAction<SearchIntent>(
            onInvoke: (intent) => ref.read(searchingProvider.notifier).startSearch(),
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
    final searching = ref.watch(searchingProvider);
    return Stack(
      children: [
        Column(
          children: [
            const _AppBar(),
            const Divider(),
            Expanded(
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: const [
                  Expanded(
                    child: _PageContent(),
                  ),
                  VerticalDivider(),
                  Inspector(),
                ],
              ),
            ),
          ],
        ),
        if (searching) const SearchBar(),
      ],
    );
  }
}

class _AppBar extends HookConsumerWidget {
  const _AppBar() : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) => Padding(
        padding: const EdgeInsets.only(top: 8.0),
        child: Row(
          children: [
            const SizedBox(width: 20),
            const Icon(FontAwesomeIcons.solidFile, size: 16),
            const SizedBox(width: 5),
            Text(ref.watch(currentPageLabelProvider)),
            const Spacer(),
            const AutoSaver(),
            const SizedBox(width: 20),
            const _SearchBar(),
            const SizedBox(width: 20),
          ],
        ),
      );
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

class _PageContent extends HookConsumerWidget {
  const _PageContent();

  @override
  Widget build(BuildContext context, WidgetRef ref) => const EntriesGraph();
}
