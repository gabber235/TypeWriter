import 'package:auto_size_text/auto_size_text.dart';
import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:fuzzy/fuzzy.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:material_floating_search_bar/material_floating_search_bar.dart';
import 'package:typewriter/hooks/delayed_execution.dart';
import 'package:typewriter/hooks/search_bar_controller.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';

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
    if (ref.read(_entriesProvider).isNotEmpty) {
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

final _fuzzyProvider = Provider<Fuzzy<Entry>>((ref) {
  return Fuzzy(
    ref.watch(pageProvider).entries,
    options: FuzzyOptions(
      threshold: 0.7,
      sortFn: (a, b) => a.matches
          .map((e) => e.score)
          .sum
          .compareTo(b.matches.map((e) => e.score).sum),
      keys: [
        WeightedKey(name: 'id', getter: (sn) => sn.id, weight: 0.1),
        WeightedKey(
            name: 'name-suffix',
            getter: (sn) => sn.name.split(".").last,
            weight: 0.9),
        WeightedKey(
            name: 'name-full', getter: (sn) => sn.formattedName, weight: 0.2),
        WeightedKey(
            name: 'type',
            getter: (sn) =>
                EntryType.findTypes(sn).map((e) => e.name).join(" "),
            weight: 0.9)
      ],
    ),
  );
});

final _entriesProvider = Provider<List<Entry>>((ref) {
  final query = ref.watch(_queryProvider);
  final fuzzy = ref.watch(_fuzzyProvider);

  if (query.isEmpty) return [];
  final results = fuzzy.search(query).map((e) => e.item).toList();

  if (query.contains(":")) {
    final type = EntryType.values.firstWhereOrNull((type) =>
        query.toLowerCase().startsWith("${type.name.toLowerCase()}:"));
    if (type != null) {
      return results
          .where((e) => EntryType.findTypes(e).contains(type))
          .toList();
    }
  }

  return results;
});

class SearchBar extends HookConsumerWidget {
  const SearchBar({
    Key? key,
  }) : super(key: key);

  void activateItem(
      List<Entry> entries, int index, BuildContext context, WidgetRef ref) {
    if (index >= entries.length) return;
    if (index < 0) return;
    final entry = entries[index];
    ref.read(selectedProvider.notifier).state = entry.id;
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
    final entries = ref.watch(_entriesProvider);
    final focusNodes = List.generate(entries.length, (index) => FocusNode());
    final globalKeys = List.generate(entries.length, (index) => GlobalKey());

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
              onInvoke: (intent) => activateItem(entries,
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
                          for (var i = 0; i < entries.length; i++)
                            _ResultTile(
                              key: globalKeys[i],
                              onPressed: () =>
                                  activateItem(entries, i, context, ref),
                              focusNode: focusNodes[i],
                              color: entries[i].backgroundColor(context),
                              title: entries[i].formattedName,
                              icon: entries[i].icon(context) ??
                                  const Icon(Icons.help),
                              suffixIcon: const Icon(
                                  FontAwesomeIcons.upRightFromSquare),
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
