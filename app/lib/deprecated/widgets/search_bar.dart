import 'package:auto_size_text/auto_size_text.dart';
import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:fuzzy/fuzzy.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:material_floating_search_bar/material_floating_search_bar.dart';
import 'package:typewriter/deprecated//models/page.dart';
import 'package:typewriter/deprecated//pages/graph.dart';
import 'package:typewriter/hooks/delayed_execution.dart';
import 'package:typewriter/hooks/search_bar_controller.dart';
import 'package:typewriter/main.dart';

/// When the user wants to search for nodes.
class SearchIntent extends Intent {}

class SearchingNotifier extends StateNotifier<bool> {
  final StateNotifierProviderRef<SearchingNotifier, bool> ref;

  SearchingNotifier(this.ref) : super(false);

  void startSearch() {
    if (state == true) return;
    state = true;
  }

  void endSearch() async {
    if (state == false) return;
    if (ref.read(_actionsProvider).isNotEmpty) {
      ref.read(_closingProvider.notifier).state = true;
      await Future.delayed(const Duration(milliseconds: 300));
    }

    ref.read(_queryProvider.notifier).state = "";
    ref.read(_closingProvider.notifier).state = false;
    state = false;
  }
}

final searchingProvider = StateNotifierProvider<SearchingNotifier, bool>(
    (ref) => SearchingNotifier(ref));

final _closingProvider = StateProvider<bool>((ref) => false);

final _queryProvider = StateProvider<String>((ref) => "");

final _entriesFuzzyProvider = Provider<Fuzzy<Entry>>((ref) {
  return Fuzzy(
    ref.watch(pageProvider).entries,
    options: FuzzyOptions(
      threshold: 0.4,
      sortFn: (a, b) => a.matches
          .map((e) => e.score)
          .sum
          .compareTo(b.matches.map((e) => e.score).sum),
      keys: [
        WeightedKey(name: 'id', getter: (sn) => sn.id, weight: 0.05),
        WeightedKey(
            name: 'name-suffix',
            getter: (sn) => sn.name.split(".").last,
            weight: 0.4),
        WeightedKey(
            name: 'name-full', getter: (sn) => sn.formattedName, weight: 0.15),
        WeightedKey(
            name: 'type',
            getter: (sn) =>
                EntryType.findTypes(sn).map((e) => e.name).join(" "),
            weight: 0.4)
      ],
    ),
  );
});

final _addEntriesFuzzyProvider = Provider<Fuzzy<EntryType>>((ref) {
  return Fuzzy(
    EntryType.values.where((type) => type.factory != null).toList(),
    options: FuzzyOptions(
      threshold: 0.1,
      sortFn: (a, b) => a.matches
          .map((e) => e.score)
          .sum
          .compareTo(b.matches.map((e) => e.score).sum),
      keys: [
        WeightedKey(name: 'name', getter: (sn) => "Add ${sn.name}", weight: 1),
      ],
    ),
  );
});

final _actionsProvider = Provider<List<Action>>((ref) {
  final query = ref.watch(_queryProvider);
  final fuzzy = ref.watch(_entriesFuzzyProvider);

  if (query.isEmpty) return [];
  final results = fuzzy.search(query).map((e) => e.item).toList();

  if (query.contains(":")) {
    if (query.toLowerCase().startsWith("add:")) {
      return EntryType.values
          .where((type) => type.factory != null)
          .map((e) => AddEntryAction(e))
          .toList();
    }

    final type = EntryType.values.firstWhereOrNull((type) =>
        query.toLowerCase().startsWith("${type.name.toLowerCase()}:"));
    if (type != null) {
      return results
              .where((e) => EntryType.findTypes(e).contains(type))
              .map((e) => EntryAction(e))
              .whereType<Action>()
              .toList() +
          [AddEntryAction(type)];
    }
  }

  final addResults = ref
      .watch(_addEntriesFuzzyProvider)
      .search(query)
      .map((e) => e.item)
      .toList();

  return addResults.map((e) => AddEntryAction(e)).whereType<Action>().toList() +
      results.map((e) => EntryAction(e)).whereType<Action>().toList();
});

abstract class Action<T> {
  Color color(BuildContext context);

  Widget icon(BuildContext context);

  Widget suffixIcon(BuildContext context);

  String title(BuildContext context);

  void activate(BuildContext context, WidgetRef ref);
}

/// Action for selecting an existing entry.
class EntryAction extends Action<Entry> {
  final Entry entry;

  EntryAction(this.entry);

  @override
  Color color(BuildContext context) => entry.backgroundColor(context);

  @override
  Widget icon(BuildContext context) => entry.icon(context);

  @override
  Widget suffixIcon(BuildContext context) =>
      const Icon(FontAwesomeIcons.upRightFromSquare);

  @override
  String title(BuildContext context) => entry.formattedName;

  @override
  void activate(BuildContext context, WidgetRef ref) {
    ref.read(selectedProvider.notifier).state = entry.id;
  }
}

class AddEntryAction extends Action<EntryType<Entry>> {
  final EntryType<Entry> type;

  AddEntryAction(this.type);

  @override
  Color color(BuildContext context) => type.backgroundColor(context);

  @override
  Widget icon(BuildContext context) =>
      type.getIcon(null, !context.isDark) ?? const SizedBox();

  @override
  Widget suffixIcon(BuildContext context) => const Icon(FontAwesomeIcons.plus);

  @override
  String title(BuildContext context) => "Add ${type.name}";

  @override
  void activate(BuildContext context, WidgetRef ref) {
    final entry = ref.read(pageProvider.notifier).addEntry(type);
    ref.read(selectedProvider.notifier).state = entry?.id ?? "";
  }
}

class SearchBar extends HookConsumerWidget {
  const SearchBar({
    Key? key,
  }) : super(key: key);

  void activateItem(
      List<Action> actions, int index, BuildContext context, WidgetRef ref) {
    if (index >= actions.length) return;
    if (index < 0) return;
    final action = actions[index];
    action.activate(context, ref);
    ref.read(searchingProvider.notifier).endSearch();
  }

  /// Change focus to the next/previous search result.
  void changeFocus(
      BuildContext context, List<FocusNode> focusNodes, List<GlobalKey> keys,
      {bool up = false}) {
    var index = focusNodes.indexWhere((n) => n.hasFocus);
    if (index == -1 && up) index = 0;

    index = up
        ? (index - 1 + focusNodes.length) % focusNodes.length
        : (index + 1) % focusNodes.length;

    FocusScope.of(context).requestFocus(focusNodes[index]);
    Scrollable.ensureVisible(keys[index].currentContext!,
        alignment: 0.5, duration: const Duration(milliseconds: 300));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useSearchBarController();
    final closing = ref.watch(_closingProvider);
    final actions = ref.watch(_actionsProvider);
    final focusNodes = List.generate(actions.length, (index) => FocusNode());
    final globalKeys = List.generate(actions.length, (index) => GlobalKey());

    useDelayedExecution(() {
      if (!closing) {
        controller.open();
      } else {
        controller.close();
      }
    }, runEveryBuild: true);

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
              onInvoke: (intent) => changeFocus(context, focusNodes, globalKeys,
                  up: intent.direction == TraversalDirection.up),
            ),
            NextFocusIntent: CallbackAction<NextFocusIntent>(
              onInvoke: (intent) =>
                  changeFocus(context, focusNodes, globalKeys),
            ),
            PreviousFocusIntent: CallbackAction<PreviousFocusIntent>(
              onInvoke: (intent) =>
                  changeFocus(context, focusNodes, globalKeys, up: true),
            ),
            DismissIntent: CallbackAction<DismissIntent>(
              onInvoke: (intent) =>
                  ref.read(searchingProvider.notifier).endSearch(),
            ),
            ActivateIntent: CallbackAction<ActivateIntent>(
              onInvoke: (intent) => activateItem(actions,
                  focusNodes.indexWhere((n) => n.hasFocus), context, ref),
            ),
          },
          child: Center(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 400, maxHeight: 500),
              child: FloatingSearchBar(
                controller: controller,
                hint: 'Search node...',
                scrollPadding: const EdgeInsets.only(top: 16, bottom: 16),
                transitionDuration: const Duration(milliseconds: 300),
                transitionCurve: Curves.easeIn,
                debounceDelay: const Duration(milliseconds: 100),
                transition: CircularFloatingSearchBarTransition(),
                clearQueryOnClose: false,
                closeOnBackdropTap: true,
                backdropColor: Colors.transparent,
                onQueryChanged: (query) =>
                    ref.read(_queryProvider.notifier).state = query,
                onFocusChanged: (hasFocus) {
                  if (!hasFocus) {
                    ref.read(searchingProvider.notifier).endSearch();
                  }
                },
                builder: (BuildContext context, Animation<double> transition) {
                  return Material(
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
                              onPressed: () =>
                                  activateItem(actions, i, context, ref),
                              focusNode: focusNodes[i],
                              color: actions[i].color(context),
                              title: actions[i].title(context),
                              icon: actions[i].icon(context),
                              suffixIcon: actions[i].suffixIcon(context),
                            ),
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
          ),
        ),
      ],
    );
  }
}

class _ResultTile extends HookWidget {
  final FocusNode focusNode;
  final VoidCallback onPressed;

  final Color color;
  final Widget icon;
  final Widget suffixIcon;
  final String title;

  const _ResultTile({
    Key? key,
    required this.focusNode,
    required this.onPressed,
    this.color = Colors.white,
    this.icon = const Icon(Icons.search),
    this.suffixIcon = const Icon(Icons.arrow_forward_ios),
    this.title = '',
  }) : super(key: key);

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
                child: AutoSizeText(
                  maxLines: 1,
                  title,
                  style: Theme.of(context).textTheme.subtitle1?.copyWith(
                        color: focused.value ? Colors.white : null,
                        fontWeight: focused.value ? FontWeight.bold : null,
                      ),
                ),
              ),
              const SizedBox(width: 8),
              IconTheme(
                data: IconThemeData(
                    size: 14,
                    color: focused.value ? Colors.white : Colors.grey),
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
