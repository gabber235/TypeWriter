import "package:auto_route/auto_route.dart";
import "package:collection/collection.dart";
import "package:flutter/cupertino.dart" hide Page;
import "package:flutter/material.dart" hide Page;
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/always_focused.dart";
import "package:typewriter/widgets/entries_graph.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/search_bar.dart";
import "package:typewriter/widgets/shortcut_label.dart";
import "package:typewriter/widgets/staging.dart";
import "package:typewriter/widgets/static_entries_list.dart";
import "package:typewriter/widgets/writers.dart";

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
        SmartSingleActivator(LogicalKeyboardKey.arrowLeft, control: true): PreviousEntriesViewIntent(),
        SmartSingleActivator(LogicalKeyboardKey.arrowRight, control: true): NextEntriesViewIntent(),
      },
      child: Actions(
        actions: {
          SearchIntent: CallbackAction<SearchIntent>(
            onInvoke: (intent) => ref.read(searchingProvider.notifier).startSearch(),
          ),
          PreviousEntriesViewIntent: CallbackAction<PreviousEntriesViewIntent>(
            onInvoke: (intent) => ref.read(entriesViewProvider.notifier).previous(),
          ),
          NextEntriesViewIntent: CallbackAction<NextEntriesViewIntent>(
            onInvoke: (intent) => ref.read(entriesViewProvider.notifier).next(),
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
            const _AppBar(key: Key("appBar")),
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
  const _AppBar({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Padding(
      padding: const EdgeInsets.only(top: 8.0),
      child: Row(
        children: [
          const SizedBox(width: 20),
          const Icon(FontAwesomeIcons.solidFile, size: 16),
          const SizedBox(width: 5),
          Text(ref.watch(currentPageLabelProvider)),
          const SizedBox(width: 5),
          const Spacer(),
          const Flexible(child: GlobalWriters()),
          const SizedBox(width: 20),
          const StagingIndicator(key: Key("staging-indicator")),
          const SizedBox(width: 20),
          const _ViewModeButtons(),
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

class _ViewModeButtons extends HookConsumerWidget {
  const _ViewModeButtons();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final mode = ref.watch(entriesViewProvider);
    return CupertinoSlidingSegmentedControl<EntriesView>(
      backgroundColor: Theme.of(context).inputDecorationTheme.fillColor ?? Colors.white,
      groupValue: mode,
      children: {
        EntriesView.graph: _buildIcon(FontAwesomeIcons.diagramProject, "Triggers"),
        EntriesView.list: _buildIcon(FontAwesomeIcons.list, "Static"),
      },
      onValueChanged: (value) {
        if (value != null) {
          ref.read(entriesViewProvider.notifier).view = value;
        }
      },
    );
  }

  Widget _buildIcon(IconData icon, String title) => Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 16),
          const SizedBox(width: 10),
          Text(title),
        ],
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

class _AddEntryButton extends HookConsumerWidget {
  const _AddEntryButton({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return IconButton(
      iconSize: 16,
      padding: EdgeInsets.zero,
      icon: const Icon(FontAwesomeIcons.plus),
      onPressed: () => ref.read(searchingProvider.notifier).startSearch("add:"),
    );
  }
}

enum EntriesView {
  graph,
  list,
  ;
}

class EntriesViewProvider extends StateNotifier<EntriesView> {
  EntriesViewProvider(this.ref) : super(EntriesView.graph);

  final Ref<EntriesView> ref;

  EntriesView get view => super.state;
  set view(EntriesView value) => super.state = value;

  void previous() {
    final index = view.index;
    if (index > 0) {
      view = EntriesView.values[index - 1];
    }
  }

  void next() {
    final index = view.index;
    if (index < EntriesView.values.length - 1) {
      view = EntriesView.values[index + 1];
    }
  }

  void navigateToViewForEntry(Entry entry) {
    // We need to check the blueprint for this entry type to get the tags.
    // If the tags contain "trigger" then we should switch to the graph view.
    // If the tags contain "static" then we should switch to the list view.

    final blueprint = ref.read(entryBlueprintProvider(entry.type));
    final tags = blueprint?.tags ?? [];

    if (tags.contains("trigger")) {
      view = EntriesView.graph;
    } else if (tags.contains("static")) {
      view = EntriesView.list;
    }
  }
}

class PreviousEntriesViewIntent extends Intent {}

class NextEntriesViewIntent extends Intent {}

final entriesViewProvider = StateNotifierProvider<EntriesViewProvider, EntriesView>((ref) => EntriesViewProvider(ref));

class _PageContent extends HookConsumerWidget {
  const _PageContent();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final mode = ref.watch(entriesViewProvider);
    final controller = usePageController(initialPage: mode.index);

    useEffect(
      () {
        if (!controller.hasClients) return;
        controller.animateToPage(mode.index, duration: const Duration(milliseconds: 250), curve: Curves.easeInOut);
        return null;
      },
      [mode],
    );

    return PageView(
      controller: controller,
      physics: const NeverScrollableScrollPhysics(),
      children: const [
        EntriesGraph(),
        StaticEntriesList(),
      ],
    );
  }
}
