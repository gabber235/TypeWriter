import "dart:math";

import "package:auto_size_text/auto_size_text.dart";
import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:fuzzy/fuzzy.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:material_floating_search_bar/material_floating_search_bar.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/hooks/search_bar_controller.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector.dart";

part "search_bar.g.dart";

/// When the user wants to search for nodes.
class SearchIntent extends Intent {}

/// A notifier to indicate of the searchbar needs to be visible.
class SearchingNotifier extends StateNotifier<bool> {
  SearchingNotifier(this.ref) : super(false);
  final StateNotifierProviderRef<SearchingNotifier, bool> ref;

  void startSearch() {
    if (state == true) return;
    state = true;
  }

  Future<void> endSearch() async {
    if (state == false) return;
    // If we end the search but there are actions visible we want to animate them out
    // Before we hide the searchbar.
    if (ref.read(_actionsProvider).isNotEmpty) {
      ref.read(_closingProvider.notifier).state = true;
      await Future.delayed(const Duration(milliseconds: 300));
    }

    ref.read(_queryProvider.notifier).state = "";
    ref.read(_closingProvider.notifier).state = false;
    state = false;
  }
}

final searchingProvider = StateNotifierProvider<SearchingNotifier, bool>(SearchingNotifier.new);

final _closingProvider = StateProvider<bool>((ref) => false);

final _queryProvider = StateProvider<String>((ref) => "");

@riverpod
Fuzzy<Entry> _fuzzyEntries(_FuzzyEntriesRef ref) {
  final pages = ref.watch(pagesProvider);

  return Fuzzy(
    pages.expand((p) => p.entries).toList(),
    options: FuzzyOptions(
      threshold: 0.4,
      sortFn: (a, b) => a.matches.map((e) => e.score).sum.compareTo(b.matches.map((e) => e.score).sum),
      keys: [
        WeightedKey(name: "id", getter: (sn) => sn.id, weight: 0.05),
        // The names of entries are like "test.some_entry".
        // We want to give the last part more priority since it is more specific.
        WeightedKey(
          name: "name-suffix",
          getter: (sn) => sn.name.split(".").last,
          weight: 0.4,
        ),
        WeightedKey(
          name: "name-full",
          getter: (sn) => sn.formattedName,
          weight: 0.15,
        ),
        WeightedKey(name: "type", getter: (sn) => sn.type, weight: 0.4)
      ],
    ),
  );
}

@riverpod
Fuzzy<EntryBlueprint> _fuzzyAddEntries(_FuzzyAddEntriesRef ref) {
  final blueprints = ref.watch(entryBlueprintsProvider);

  return Fuzzy(
    blueprints,
    options: FuzzyOptions(
      threshold: 0.1,
      sortFn: (a, b) => a.matches.map((e) => e.score).sum.compareTo(b.matches.map((e) => e.score).sum),
      keys: [
        WeightedKey(name: "name", getter: (sn) => "Add ${sn.name.formatted}", weight: 1),
      ],
    ),
  );
}

@riverpod
List<_Action> _actions(_ActionsRef ref) {
  final query = ref.watch(_queryProvider);
  final fuzzy = ref.watch(_fuzzyEntriesProvider);
  final blueprints = ref.watch(entryBlueprintsProvider);

  if (query.isEmpty) return [];
  final results = fuzzy.search(query).map((e) => e.item).toList();

  // If the query contains a ":" we want to refine our search.
  if (query.contains(":")) {
    final operatorFindings = _findActionsForOperator(query, blueprints, results);
    if (operatorFindings != null) return operatorFindings;
  }

  final addResults = ref.watch(_fuzzyAddEntriesProvider).search(query).map((e) => e.item).toList();

  return addResults.map(_AddEntryAction.new).whereType<_Action>().toList() +
      results.map(_EntryAction.new).whereType<_Action>().toList();
}

/// Finds actions for a given operator.
/// If a query contains a ":" it means that its prefix will be the operator
List<_Action>? _findActionsForOperator(String query, List<EntryBlueprint> blueprints, List<Entry> results) {
  if (query.toLowerCase().startsWith("add:")) {
    return blueprints.map(_AddEntryAction.new).toList();
  }

  final type = blueprints.firstWhereOrNull(
    (print) => query.toLowerCase().startsWith("${print.name}:"),
  );
  if (type != null) {
    return results.where((e) => e.type == type.name).map<_Action>(_EntryAction.new).toList() + [_AddEntryAction(type)];
  }

  return null;
}

abstract class _Action {
  Color color(BuildContext context);

  Widget icon(BuildContext context);

  Widget suffixIcon(BuildContext context);

  String title(BuildContext context);

  String description(BuildContext context);

  void activate(BuildContext context, WidgetRef ref);
}

/// Action for selecting an existing entry.
class _EntryAction extends _Action {
  _EntryAction(this.entry);
  final Entry entry;

  @override
  Color color(BuildContext context) => Colors.grey;

  @override
  Widget icon(BuildContext context) => const Icon(Icons.question_mark);

  @override
  Widget suffixIcon(BuildContext context) => const Icon(FontAwesomeIcons.upRightFromSquare);

  @override
  String title(BuildContext context) => entry.formattedName;

  @override
  String description(BuildContext context) => entry.id;

  @override
  Future<void> activate(BuildContext context, WidgetRef ref) async {
    if (ref.read(currentPageProvider)?.entries.any((e) => e.id == entry.id) != true) {
      final page = ref.read(pagesProvider).firstWhereOrNull((p) => p.entries.any((e) => e.id == entry.id));
      if (page != null) {
        await ref.read(appRouter).push(PageEditorRoute(id: page.name));
      }
    }

    ref.read(selectedEntryIdProvider.notifier).state = entry.id;
  }
}

class _AddEntryAction extends _Action {
  _AddEntryAction(this.blueprint);
  final EntryBlueprint blueprint;

  @override
  Color color(BuildContext context) => blueprint.color;

  @override
  Widget icon(BuildContext context) => const Icon(FontAwesomeIcons.fileImport);

  @override
  Widget suffixIcon(BuildContext context) => const Icon(FontAwesomeIcons.plus);

  @override
  String title(BuildContext context) => "Add ${blueprint.name.formatted}";

  @override
  String description(BuildContext context) => blueprint.description;

  static const _chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
  static final Random _random = Random();

  String _getRandomString(int length) => String.fromCharCodes(
        Iterable.generate(
          length,
          (_) => _chars.codeUnitAt(_random.nextInt(_chars.length)),
        ),
      );

  @override
  void activate(BuildContext context, WidgetRef ref) {
    final e = Entry.fromBlueprint(id: _getRandomString(15), blueprint: blueprint);
    ref.read(currentPageProvider)?.insertEntry(ref, e);
    ref.read(selectedEntryIdProvider.notifier).state = e.id;
  }
}

class SearchBar extends HookConsumerWidget {
  const SearchBar({
    super.key,
  });

  void _activateItem(
    List<_Action> actions,
    int index,
    BuildContext context,
    WidgetRef ref,
  ) {
    if (index >= actions.length) return;
    if (index < 0) return;
    actions[index].activate(context, ref);
    ref.read(searchingProvider.notifier).endSearch();
  }

  /// Change focus to the next/previous search result.
  void _changeFocus(
    BuildContext context,
    List<FocusNode> focusNodes,
    List<GlobalKey> keys,
    int index,
  ) {
    FocusScope.of(context).requestFocus(focusNodes[index]);
    Scrollable.ensureVisible(
      keys[index].currentContext!,
      alignment: 0.5,
      duration: const Duration(milliseconds: 300),
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
    final controller = useSearchBarController();
    final closing = ref.watch(_closingProvider);
    final actions = ref.watch(_actionsProvider);
    final focusNodes = List.generate(actions.length, (index) => FocusNode());
    final globalKeys = List.generate(actions.length, (index) => GlobalKey());

    useDelayedExecution(
      () {
        if (!closing) {
          controller.open();
        } else {
          controller.close();
        }
      },
      runEveryBuild: true,
    );

    return Stack(
      children: [
        Positioned.fill(
          child: ModalBarrier(
            dismissible: true,
            color: Colors.black.withOpacity(0.5),
            onDismiss: () {
              ref.read(searchingProvider.notifier).endSearch();
            },
          ),
        ),
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
              onInvoke: (intent) => ref.read(searchingProvider.notifier).endSearch(),
            ),
            ActivateIntent: CallbackAction<ActivateIntent>(
              onInvoke: (intent) => _activateItem(
                actions,
                focusNodes.indexWhere((n) => n.hasFocus),
                context,
                ref,
              ),
            ),
          },
          child: Center(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 400, maxHeight: 500),
              child: FloatingSearchBar(
                controller: controller,
                hint: "Search node...",
                scrollPadding: const EdgeInsets.only(top: 16, bottom: 16),
                transitionDuration: const Duration(milliseconds: 300),
                transitionCurve: Curves.easeIn,
                debounceDelay: const Duration(milliseconds: 100),
                transition: CircularFloatingSearchBarTransition(),
                clearQueryOnClose: false,
                closeOnBackdropTap: true,
                backdropColor: Colors.transparent,
                onQueryChanged: (query) => ref.read(_queryProvider.notifier).state = query,
                onFocusChanged: (hasFocus) {
                  if (!hasFocus) {
                    ref.read(searchingProvider.notifier).endSearch();
                  }
                },
                builder: (context, transition) => Material(
                  elevation: 3.0,
                  color: Theme.of(context).cardColor,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(8),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
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
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
      ],
    );
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
    return AnimatedContainer(
      duration: const Duration(milliseconds: 200),
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
          onLongPress: onPressed,
          child: Row(
            children: [
              Container(
                height: double.infinity,
                color: color.withOpacity(0.8),
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
                      style: Theme.of(context).textTheme.subtitle1?.copyWith(
                            color: focused.value ? Colors.white : null,
                            fontWeight: focused.value ? FontWeight.bold : null,
                          ),
                    ),
                    AutoSizeText(
                      maxLines: 1,
                      description,
                      overflow: TextOverflow.ellipsis,
                      style: Theme.of(context).textTheme.caption?.copyWith(
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
    );
  }
}
