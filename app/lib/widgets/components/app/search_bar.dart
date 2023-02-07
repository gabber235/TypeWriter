import "package:auto_size_text/auto_size_text.dart";
import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:fuzzy/fuzzy.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:text_scroll/text_scroll.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/debouncer.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/decorated_text_field.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "search_bar.freezed.dart";
part "search_bar.g.dart";

/// When the user wants to search for nodes.
class SearchIntent extends Intent {}

@freezed
class Search with _$Search {
  const factory Search({
    @Default("") String query,
    @Default([]) List<SearchFetcher> fetchers,
    @Default([]) List<SearchFilter> filters,
    Function()? onEnd,
  }) = _Search;
}

abstract class SearchFilter {
  const SearchFilter();

  bool get canRemove;
  String get title;
  Color get color;
  IconData get icon;

  bool filter(SearchAction action) {
    return true;
  }
}

class TagFilter extends SearchFilter {
  const TagFilter(this.tag, {this.canRemove = true});

  final String tag;
  @override
  final bool canRemove;

  @override
  String get title => tag;
  @override
  Color get color => Colors.deepOrangeAccent;
  @override
  IconData get icon => FontAwesomeIcons.hashtag;

  @override
  bool filter(SearchAction action) {
    if (action is SearchEntryAction) {
      return action.blueprint.tags.contains(tag);
    }
    if (action is SearchAddEntryAction) {
      return action.blueprint.tags.contains(tag);
    }
    return false;
  }
}

abstract class SearchFetcher {
  const SearchFetcher();

  bool get disabled;
  String get title;
  IconData get icon => FontAwesomeIcons.magnifyingGlass;
  Color get color => Colors.blueAccent;

  List<SearchAction> fetch(PassingRef ref) {
    return [];
  }

  SearchFetcher copyWith({
    bool? disabled,
  });
}

class NewEntryFetcher extends SearchFetcher {
  const NewEntryFetcher({
    this.onAdd,
    this.disabled = false,
  });

  final Function(EntryBlueprint)? onAdd;

  @override
  final bool disabled;

  @override
  String get title => "New Entries";

  @override
  List<SearchAction> fetch(PassingRef ref) {
    final search = ref.read(searchProvider);
    if (search == null) return [];
    final fuzzy = ref.read(_fuzzyBlueprintsProvider);

    final results = fuzzy.search(search.query);

    return results.map((result) => SearchAddEntryAction(result.item, onAdd: onAdd)).toList();
  }

  @override
  SearchFetcher copyWith({
    bool? disabled,
  }) {
    return NewEntryFetcher(
      onAdd: onAdd,
      disabled: disabled ?? this.disabled,
    );
  }
}

class EntryFetcher extends SearchFetcher {
  const EntryFetcher({
    this.onSelect,
    this.disabled = false,
  });

  final Function(Entry)? onSelect;

  @override
  final bool disabled;

  @override
  String get title => "Entries";

  @override
  List<SearchAction> fetch(PassingRef ref) {
    final search = ref.read(searchProvider);
    if (search == null) return [];
    final fuzzy = ref.read(_fuzzyEntriesProvider);

    final results = fuzzy.search(search.query);

    return results.map((result) {
      final definition = result.item;
      return SearchEntryAction(definition, onSelect: onSelect);
    }).toList();
  }

  @override
  SearchFetcher copyWith({
    bool? disabled,
  }) {
    return EntryFetcher(
      onSelect: onSelect,
      disabled: disabled ?? this.disabled,
    );
  }
}

class SearchNotifier extends StateNotifier<Search?> {
  SearchNotifier(this.ref) : super(null);

  final Ref ref;
  final Debouncer _debouncer = Debouncer(duration: 200.ms);

  void start(Search search) {
    state = search;
  }

  SearchBuilder asBuilder() {
    return SearchBuilder(this);
  }

  void startGlobalSearch() => asBuilder()
    ..fetchNewEntry()
    ..fetchEntry()
    ..start();

  void startAddSearch({List<SearchFilter> filters = const [], Function(EntryBlueprint)? onAdd}) => asBuilder()
    ..fetchNewEntry(onAdd: onAdd)
    ..filters(filters)
    ..start();

  void startSelectSearch({List<SearchFilter> filters = const [], Function(Entry)? onSelect}) => asBuilder()
    ..fetchEntry(onSelect: onSelect)
    ..filters(filters)
    ..start();

  void endSearch() {
    state?.onEnd?.call();
    state = null;
    _debouncer.cancel();
  }

  void updateQuery(String query) {
    _debouncer.run(() {
      state = state!.copyWith(query: query);
    });
  }

  void toggleFetcher(SearchFetcher fetcher) {
    final fetchers = state?.fetchers;
    if (fetchers == null) return;
    final newFetchers = fetchers.map((f) {
      if (f.runtimeType == fetcher.runtimeType) {
        return f.copyWith(disabled: !f.disabled);
      }
      return f;
    }).toList();
    state = state!.copyWith(fetchers: newFetchers);
  }

  void removeFilter(SearchFilter filter) {
    state = state!.copyWith(filters: state!.filters.where((f) => f != filter).toList());
  }

  @override
  void dispose() {
    _debouncer.cancel();
    super.dispose();
  }
}

final searchProvider = StateNotifierProvider<SearchNotifier, Search?>(SearchNotifier.new);

class SearchBuilder {
  SearchBuilder(this.notifier);

  var _currentSearch = const Search();
  final SearchNotifier notifier;

  void query(String query) {
    _currentSearch = _currentSearch.copyWith(query: query);
  }

  void filters(List<SearchFilter> filters) {
    _currentSearch = _currentSearch.copyWith(filters: filters);
  }

  void filter(SearchFilter filter) {
    _currentSearch = _currentSearch.copyWith(filters: [..._currentSearch.filters, filter]);
  }

  void tag(String tag, {bool canRemove = true}) {
    filter(TagFilter(tag, canRemove: canRemove));
  }

  void fetch(SearchFetcher fetcher) {
    _currentSearch = _currentSearch.copyWith(fetchers: [..._currentSearch.fetchers, fetcher]);
  }

  void fetchNewEntry({Function(EntryBlueprint)? onAdd}) {
    fetch(NewEntryFetcher(onAdd: onAdd));
  }

  void fetchEntry({Function(Entry)? onSelect}) {
    fetch(EntryFetcher(onSelect: onSelect));
  }

  void start() {
    notifier.start(_currentSearch);
  }
}

@riverpod
List<SearchFetcher> searchFetchers(SearchFetchersRef ref) {
  final search = ref.watch(searchProvider);
  if (search == null) return [];
  return search.fetchers;
}

@riverpod
List<SearchFilter> searchFilters(SearchFiltersRef ref) {
  final search = ref.watch(searchProvider);
  if (search == null) return [];
  return search.filters;
}

@riverpod
List<SearchAction> searchActions(SearchActionsRef ref) {
  final search = ref.watch(searchProvider);
  if (search == null) return [];
  if (search.query.isEmpty && search.filters.isEmpty) return [];

  final fetchers = search.fetchers.where((f) => !f.disabled);
  final actions = fetchers.expand((f) => f.fetch(ref.passing));
  final filtered = actions.where((e) => search.filters.every((f) => f.filter(e)));

  return filtered.take(30).toList();
}

@riverpod
List<GlobalKey> searchGlobalKeys(SearchGlobalKeysRef ref) {
  final actionsCount = ref.watch(searchActionsProvider.select((value) => value.length));
  return List.generate(actionsCount, (_) => GlobalKey());
}

@riverpod
List<FocusNode> searchFocusNodes(SearchFocusNodesRef ref) {
  final actionsCount = ref.watch(searchActionsProvider.select((value) => value.length));
  return List.generate(actionsCount, (_) => FocusNode());
}

@riverpod
Fuzzy<EntryDefinition> _fuzzyEntries(_FuzzyEntriesRef ref) {
  final pages = ref.watch(pagesProvider);
  final definitions = pages.expand((page) {
    return page.entries.map((entry) {
      final blueprint = ref.watch(entryBlueprintProvider(entry.type));
      if (blueprint == null) return null;
      return EntryDefinition(
        pageId: page.name,
        blueprint: blueprint,
        entry: entry,
      );
    }).whereNotNull();
  }).toList();

  return Fuzzy(
    definitions,
    options: FuzzyOptions(
      threshold: 0.3,
      sortFn: (a, b) => a.matches.map((e) => e.score).sum.compareTo(b.matches.map((e) => e.score).sum),
      // tokenize: true,
      // verbose: true,
      keys: [
        // The names of entries are like "test.some_entry".
        // We want to give the last part more priority since it is more specific.
        WeightedKey(
          name: "name-suffix",
          getter: (definition) => definition.entry.name.split(".").last,
          weight: 0.4,
        ),
        WeightedKey(
          name: "name-full",
          getter: (definition) => definition.entry.name.formatted,
          weight: 0.15,
        ),
        WeightedKey(
          name: "type",
          getter: (definition) => definition.entry.type,
          weight: 0.4,
        ),
        WeightedKey(
          name: "tags",
          getter: (definition) => definition.blueprint.tags.join(" "),
          weight: 0.3,
        ),
      ],
    ),
  );
}

@riverpod
Fuzzy<EntryBlueprint> _fuzzyBlueprints(_FuzzyBlueprintsRef ref) {
  final blueprints = ref.watch(entryBlueprintsProvider);

  return Fuzzy(
    blueprints,
    options: FuzzyOptions(
      threshold: 0.1,
      sortFn: (a, b) => a.matches.map((e) => e.score).sum.compareTo(b.matches.map((e) => e.score).sum),
      keys: [
        WeightedKey(
          name: "name",
          getter: (blueprint) => "Add ${blueprint.name.formatted}",
          weight: 0.5,
        ),
        WeightedKey(
          name: "tags",
          getter: (blueprint) => blueprint.tags.join(" "),
          weight: 0.2,
        ),
        WeightedKey(
          name: "description",
          getter: (blueprint) => blueprint.description,
          weight: 0.3,
        ),
      ],
    ),
  );
}

abstract class SearchAction {
  const SearchAction();
  Color color(BuildContext context);

  Widget icon(BuildContext context);

  Widget suffixIcon(BuildContext context);

  String title(BuildContext context);

  String description(BuildContext context);

  void activate(BuildContext context, PassingRef ref);
}

/// Action for selecting an existing entry.
class SearchEntryAction extends SearchAction {
  const SearchEntryAction(this.definition, {this.onSelect});
  final EntryDefinition definition;
  final Function(Entry)? onSelect;

  EntryBlueprint get blueprint => definition.blueprint;
  Entry get entry => definition.entry;

  @override
  Color color(BuildContext context) => blueprint.color;

  @override
  Widget icon(BuildContext context) => Icon(blueprint.icon);

  @override
  Widget suffixIcon(BuildContext context) => const Icon(FontAwesomeIcons.upRightFromSquare);

  @override
  String title(BuildContext context) => entry.formattedName;

  @override
  String description(BuildContext context) => definition.pageId.formatted;

  @override
  Future<void> activate(BuildContext context, PassingRef ref) async {
    if (onSelect != null) {
      onSelect?.call(entry);
      return;
    }

    await ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref, entry.id);
  }
}

class SearchAddEntryAction extends SearchAction {
  const SearchAddEntryAction(this.blueprint, {this.onAdd});
  final EntryBlueprint blueprint;
  final Function(EntryBlueprint)? onAdd;

  @override
  Color color(BuildContext context) => blueprint.color;

  @override
  Widget icon(BuildContext context) => Icon(blueprint.icon);

  @override
  Widget suffixIcon(BuildContext context) => const Icon(FontAwesomeIcons.plus);

  @override
  String title(BuildContext context) => "Add ${blueprint.name.formatted}";

  @override
  String description(BuildContext context) => blueprint.description;

  @override
  Future<void> activate(BuildContext context, PassingRef ref) async {
    if (onAdd != null) {
      onAdd?.call(blueprint);
      return;
    }
    final page = ref.read(currentPageProvider);
    if (page == null) return;
    final entry = await page.createEntryFromBlueprint(ref, blueprint);
    await ref.read(inspectingEntryIdProvider.notifier).navigateAndSelectEntry(ref, entry.id);
  }
}

class SearchBar extends HookConsumerWidget {
  const SearchBar({
    super.key,
  });

  void _activateItem(
    List<SearchAction> actions,
    int index,
    BuildContext context,
    WidgetRef ref,
  ) {
    if (index >= actions.length) return;
    if (index < 0) return;
    ref.read(searchProvider.notifier).endSearch();
    actions[index].activate(context, ref.passing);
  }

  /// Change focus to the next/previous search result.
  void _changeFocus(
    BuildContext context,
    List<FocusNode> focusNodes,
    List<GlobalKey> keys,
    int index,
  ) {
    FocusScope.of(context).requestFocus(focusNodes[index]);
    final indexContext = keys[index].currentContext;
    if (indexContext == null) return;
    Scrollable.ensureVisible(
      indexContext,
      alignment: 0.5,
      duration: 300.ms,
    );
  }

  void _changeFocusUp(BuildContext context, List<FocusNode> focusNodes, List<GlobalKey> keys) {
    var index = focusNodes.indexWhere((n) => n.hasFocus);
    if (index == -1) index = 0;

    index = (index - 1 + focusNodes.length) % focusNodes.length;
    _changeFocus(context, focusNodes, keys, index);
  }

  void _changeFocusDown(BuildContext context, List<FocusNode> focusNodes, List<GlobalKey> keys) {
    var index = focusNodes.indexWhere((n) => n.hasFocus);

    index = (index + 1) % focusNodes.length;
    _changeFocus(context, focusNodes, keys, index);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focusNodes = ref.watch(searchFocusNodesProvider);
    final globalKeys = ref.watch(searchGlobalKeysProvider);

    return Stack(
      children: [
        const _Barrier(),
        Actions(
          actions: {
            DirectionalFocusIntent: CallbackAction<DirectionalFocusIntent>(
              onInvoke: (intent) => intent.direction == TraversalDirection.up
                  ? _changeFocusUp(
                      context,
                      focusNodes,
                      globalKeys,
                    )
                  : _changeFocusDown(
                      context,
                      focusNodes,
                      globalKeys,
                    ),
            ),
            NextFocusIntent: CallbackAction<NextFocusIntent>(
              onInvoke: (intent) => _changeFocusDown(context, focusNodes, globalKeys),
            ),
            PreviousFocusIntent: CallbackAction<PreviousFocusIntent>(
              onInvoke: (intent) => _changeFocusUp(context, focusNodes, globalKeys),
            ),
            DismissIntent: CallbackAction<DismissIntent>(
              onInvoke: (intent) => ref.read(searchProvider.notifier).endSearch(),
            ),
            ActivateIntent: CallbackAction<ActivateIntent>(
              onInvoke: (intent) => _activateItem(
                ref.watch(searchActionsProvider),
                focusNodes.indexWhere((n) => n.hasFocus),
                context,
                ref,
              ),
            ),
          },
          child: Center(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 400, maxHeight: 500),
              child: const _Modal(),
            ),
          ),
        ),
      ],
    );
  }
}

class _Barrier extends HookConsumerWidget {
  const _Barrier();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSearching = ref.watch(searchProvider.select((value) => value != null));

    return Positioned.fill(
      child: ModalBarrier(
        dismissible: true,
        color: Colors.black.withOpacity(0.5),
        onDismiss: () {
          ref.read(searchProvider.notifier).endSearch();
        },
      )
          .animate(target: isSearching ? 1 : 0)
          .visibility(end: true, maintain: false, duration: 1.ms)
          .then()
          .fadeIn(duration: 300.ms),
    );
  }
}

class _Modal extends HookConsumerWidget {
  const _Modal();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSearching = ref.watch(searchProvider.select((value) => value != null));

    return const Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        _SearchFilters(),
        SizedBox(height: 12),
        _SearchBar(),
        SizedBox(height: 12),
        _SearchResults(),
      ],
    )
        .animate(target: isSearching ? 1 : 0)
        .visibility(end: true, maintain: false, duration: 1.ms)
        .then()
        .scaleXY(duration: 200.ms, begin: 0.9, curve: Curves.easeOut)
        .fadeIn();
  }
}

class _SearchFilters extends HookConsumerWidget {
  const _SearchFilters();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final fetchers = ref.watch(searchFetchersProvider);
    final filters = ref.watch(searchFiltersProvider);

    return Row(
      children: [
        CircleAvatar(
          radius: 16,
          backgroundColor: Theme.of(context).cardColor,
          child: FaIcon(
            FontAwesomeIcons.filter,
            color: Theme.of(context).iconTheme.color,
            size: 16,
          ),
        ),
        const SizedBox(width: 8),
        Flexible(
          child: Wrap(
            spacing: 8,
            runSpacing: 8,
            direction: Axis.horizontal,
            crossAxisAlignment: WrapCrossAlignment.center,
            children: [
              for (final fetcher in fetchers)
                Material(
                  color: fetcher.disabled ? Theme.of(context).cardColor : fetcher.color,
                  borderRadius: BorderRadius.circular(30),
                  child: InkWell(
                    onTap: () => ref.read(searchProvider.notifier).toggleFetcher(fetcher),
                    borderRadius: BorderRadius.circular(30),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 5),
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          FaIcon(
                            fetcher.icon,
                            color: Colors.white,
                            size: 16,
                          ),
                          const SizedBox(width: 8),
                          Text(fetcher.title, style: const TextStyle(color: Colors.white)),
                        ],
                      ),
                    ),
                  ),
                ),
              for (final filter in filters)
                Material(
                  color: filter.color,
                  borderRadius: BorderRadius.circular(30),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 5),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        FaIcon(
                          filter.icon,
                          color: Colors.white,
                          size: 16,
                        ),
                        const SizedBox(width: 8),
                        Text(filter.title, style: const TextStyle(color: Colors.white)),
                        if (filter.canRemove) ...[
                          const SizedBox(width: 12),
                          IconButton(
                            iconSize: 12,
                            splashRadius: 12,
                            padding: EdgeInsets.zero,
                            constraints: const BoxConstraints(maxHeight: 24, maxWidth: 24),
                            onPressed: () => ref.read(searchProvider.notifier).removeFilter(filter),
                            icon: const FaIcon(FontAwesomeIcons.xmark, color: Colors.white),
                          ),
                        ],
                      ],
                    ),
                  ),
                ),
            ],
          ),
        ),
      ],
    );
  }
}

class _SearchBar extends HookConsumerWidget {
  const _SearchBar();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final search = ref.watch(searchProvider);
    final controller = useTextEditingController(text: search?.query ?? "");
    final focusNode = useFocusNode();

    useEffect(
      () {
        focusNode.requestFocus();
        return null;
      },
      [focusNode],
    );

    return Material(
      elevation: 3.0,
      color: Theme.of(context).cardColor,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(4),
      ),
      child: Row(
        children: [
          const SizedBox(width: 12),
          FaIcon(
            FontAwesomeIcons.magnifyingGlass,
            color: Theme.of(context).iconTheme.color,
          ),
          Expanded(
            child: DecoratedTextField(
              controller: controller,
              focus: focusNode,
              autofocus: true,
              decoration: const InputDecoration(
                hintText: "Enter search query...",
                border: InputBorder.none,
                filled: false,
              ),
              onChanged: (query) => ref.read(searchProvider.notifier).updateQuery(query),
              onSubmitted: (query) => ref.read(searchProvider.notifier).endSearch(),
            ),
          ),
          IconButton(
            icon: const FaIcon(FontAwesomeIcons.xmark),
            onPressed: () => ref.read(searchProvider.notifier).endSearch(),
          ),
          const SizedBox(width: 4),
        ],
      ),
    );
  }
}

class _SearchResults extends HookConsumerWidget {
  const _SearchResults();

  void _activateItem(
    List<SearchAction> actions,
    int index,
    BuildContext context,
    WidgetRef ref,
  ) {
    if (index >= actions.length) return;
    if (index < 0) return;
    actions[index].activate(context, ref.passing);
    ref.read(searchProvider.notifier).endSearch();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final actions = ref.watch(searchActionsProvider);
    final focusNodes = ref.watch(searchFocusNodesProvider);
    final globalKeys = ref.watch(searchGlobalKeysProvider);

    return Flexible(
      child: ClipPath(
        clipper: const VerticalClipper(additionalWidth: 100),
        child: SingleChildScrollView(
          clipBehavior: Clip.none,
          child: Column(
            children: [
              for (var i = 0; i < actions.length; i++)
                _ResultTile(
                  key: globalKeys[i],
                  onPressed: () => _activateItem(actions, i, context, ref),
                  focusNode: focusNodes[i],
                  color: actions[i].color(context),
                  title: actions[i].title(context),
                  description: actions[i].description(context),
                  icon: actions[i].icon(context),
                  suffixIcon: actions[i].suffixIcon(context),
                ),
            ]
                .animate(interval: 50.ms)
                .scaleXY(begin: 0.9, curve: Curves.elasticOut, duration: 1.seconds)
                .fadeIn(duration: 500.ms),
          ),
        ),
      ),
    );
  }
}

class VerticalClipper extends CustomClipper<Path> {
  const VerticalClipper({this.additionalWidth = 0});

  final double additionalWidth;

  @override
  Path getClip(Size size) {
    return Path()
      ..moveTo(-additionalWidth, 0)
      ..lineTo(size.width + additionalWidth, 0)
      ..lineTo(size.width + additionalWidth, size.height)
      ..lineTo(-additionalWidth, size.height)
      ..close();
  }

  @override
  bool shouldReclip(covariant CustomClipper<Path> oldClipper) {
    if (oldClipper is VerticalClipper) {
      return oldClipper.additionalWidth != additionalWidth;
    }
    return true;
  }
}

class _ResultTile extends HookWidget {
  const _ResultTile({
    required this.focusNode,
    required this.onPressed,
    this.color = Colors.white,
    this.icon = const Icon(Icons.search),
    this.suffixIcon = const Icon(Icons.arrow_forward_ios),
    this.title = "",
    this.description = "",
    super.key,
  });
  final FocusNode focusNode;
  final VoidCallback onPressed;

  final Color color;
  final Widget icon;
  final Widget suffixIcon;
  final String title;
  final String description;

  @override
  Widget build(BuildContext context) {
    final focused = useState(false);
    final hover = useState(false);
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 2),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(4),
        child: Material(
          color: Theme.of(context).cardColor,
          elevation: 2,
          child: AnimatedContainer(
            duration: 200.ms,
            curve: Curves.fastLinearToSlowEaseIn,
            height: 56.0,
            width: 400,
            color: focused.value ? color.withOpacity(0.6) : Colors.transparent,
            child: Focus(
              focusNode: focusNode,
              canRequestFocus: true,
              onFocusChange: (f) => focused.value = f,
              child: InkWell(
                canRequestFocus: false,
                onTap: onPressed,
                onHover: (h) => hover.value = h,
                child: Row(
                  children: [
                    Container(
                      height: double.infinity,
                      color: color.withOpacity(color.opacity.clamp(0, 0.8)),
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 16.0),
                        child: IconTheme(
                          data: const IconThemeData(color: Colors.white),
                          child: icon,
                        ),
                      ),
                    ),
                    const SizedBox(width: 20),
                    Expanded(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          AutoSizeText(
                            maxLines: 1,
                            title,
                            overflow: TextOverflow.ellipsis,
                            style: Theme.of(context).textTheme.titleMedium?.copyWith(
                                  color: focused.value ? Colors.white : null,
                                  fontWeight: focused.value ? FontWeight.bold : null,
                                ),
                          ),
                          if (focused.value || hover.value)
                            TextScroll(
                              description,
                              delayBefore: 1.seconds,
                              pauseBetween: 3.seconds,
                              intervalSpaces: 5,
                              velocity: const Velocity(pixelsPerSecond: Offset(30, 0)),
                              style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                    color: focused.value ? Colors.white : null,
                                    fontWeight: focused.value ? FontWeight.bold : null,
                                  ),
                            )
                          else
                            AutoSizeText(
                              maxLines: 1,
                              description,
                              overflow: TextOverflow.ellipsis,
                              style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                    color: focused.value ? Colors.white : null,
                                    fontWeight: focused.value ? FontWeight.bold : null,
                                  ),
                            ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 8),
                    IconTheme(
                      data: IconThemeData(
                        size: 14,
                        color: focused.value ? Colors.white : Colors.grey,
                      ),
                      child: suffixIcon,
                    ),
                    const SizedBox(width: 16),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
