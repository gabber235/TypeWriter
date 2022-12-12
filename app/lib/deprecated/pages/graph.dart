import "dart:io";
import "dart:math";

import "package:collection/collection.dart";
import "package:file_picker/file_picker.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:google_fonts/google_fonts.dart";
import "package:graphview/GraphView.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/deprecated/models/page.dart";
import "package:typewriter/deprecated/pages/inspection_menu.dart";
import "package:typewriter/deprecated/pages/open_page.dart";
import "package:typewriter/deprecated/pages/static_nodes.dart";
import "package:typewriter/deprecated/widgets/search_bar.dart";

final fileNameProvider = StateProvider<String>((ref) => "test.json");

class PageNotifier extends StateNotifier<PageModel> {
  PageNotifier() : super(const PageModel());

  PageModel get model => state;
  set model(PageModel value) => state = value;

  static const _chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
  final Random _random = Random();

  String getRandomString(int length) => String.fromCharCodes(
        Iterable.generate(
          length,
          (_) => _chars.codeUnitAt(_random.nextInt(_chars.length)),
        ),
      );

  Entry? addEntry(EntryType<Entry> type) {
    var id = getRandomString(15);
    // Make sure we don't have a duplicate id
    while (state.entries.any((e) => e.id == id)) {
      id = getRandomString(15);
    }
    final entry = type.factory?.call(id);
    if (entry == null) return null;
    insertEntry(entry);
    return entry;
  }

  void insertEntry(Entry entry) {
    if (entry is Fact) {
      state = state.copyWith(
        facts: [...state.facts.where((e) => e.id != entry.id), entry],
      );
    } else if (entry is Speaker) {
      state = state.copyWith(
        speakers: [...state.speakers.where((e) => e.id != entry.id), entry],
      );
    } else if (entry is Event) {
      state = state.copyWith(
        events: [...state.events.where((e) => e.id != entry.id), entry],
      );
    } else if (entry is Dialogue) {
      state = state.copyWith(
        dialogue: [...state.dialogue.where((e) => e.id != entry.id), entry],
      );
    } else if (entry is ActionEntry) {
      state = state.copyWith(
        actions: [...state.actions.where((e) => e.id != entry.id), entry],
      );
    }
  }

  void deleteEntry(String id) {
    state = state.copyWith(
      facts: state.facts.where((e) => e.id != id).toList(),
      speakers: state.speakers.where((e) => e.id != id).toList(),
      events: state.events.where((e) => e.id != id).toList(),
      dialogue: state.dialogue.where((d) => d.id != id).toList(),
      actions: state.actions.where((a) => a.id != id).toList(),
    );
  }
}

final pageProvider = StateNotifierProvider<PageNotifier, PageModel>((ref) => PageNotifier());

final selectedProvider = StateProvider<String>((ref) => "");

class PageGraph extends HookConsumerWidget {
  const PageGraph({
    super.key,
  });

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final page = ref.watch(pageProvider);
    final selectedId = ref.watch(selectedProvider);
    final searching = ref.watch(searchingProvider);

    final focus = useFocusNode();

    final selected = page.getEntry(selectedId);

    final currentFocus = FocusScope.of(context).focusedChild;

    useEffect(
      () {
        // For shortcuts to work, a widget must be focused.
        // So if nothing is focused, request focus for a placeholder focus.
        if (currentFocus == null) {
          focus.requestFocus();
        }
        return null;
      },
      [currentFocus],
    );

    if (page.events.isEmpty && page.rules.isEmpty) {
      return const SizedBox.expand(
        child: OpenPage(),
      );
    }

    return Shortcuts(
      shortcuts: {
        if (Platform.isIOS || Platform.isMacOS)
          const SingleActivator(
            LogicalKeyboardKey.keyP,
            shift: true,
            meta: true,
          ): SearchIntent()
        else
          const SingleActivator(
            LogicalKeyboardKey.keyP,
            shift: true,
            control: true,
          ): SearchIntent(),
        if (Platform.isIOS || Platform.isMacOS)
          const SingleActivator(LogicalKeyboardKey.keyK, meta: true): SearchIntent()
        else
          const SingleActivator(LogicalKeyboardKey.keyK, control: true): SearchIntent(),
      },
      child: Actions(
        actions: {
          SearchIntent: CallbackAction<SearchIntent>(
            onInvoke: (intent) => ref.read(searchingProvider.notifier).startSearch(),
          ),
        },
        child: Focus(
          autofocus: true,
          canRequestFocus: true,
          focusNode: focus,
          child: Scaffold(
            appBar: const PreferredSize(
              preferredSize: Size.fromHeight(60),
              child: _AppBar(),
            ),
            body: Stack(
              children: [
                const _Graph(),
                AnimatedPositioned(
                  duration: const Duration(milliseconds: 500),
                  curve: Curves.easeOutCubic,
                  bottom: 0,
                  left: 0,
                  right: selected != null ? 450 : 0,
                  child: const StaticEntries(),
                ),
                if (selected != null)
                  Align(
                    alignment: const Alignment(0.98, 0),
                    child: InspectionMenu(
                      entry: selected,
                    ),
                  ),
                if (searching) const SearchBar(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _Graph extends HookConsumerWidget {
  const _Graph();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final page = ref.watch(pageProvider);
    final graph = page.toGraph();
    final builder = SugiyamaConfiguration()
      ..nodeSeparation = (40)
      ..levelSeparation = (40)
      ..orientation = SugiyamaConfiguration.ORIENTATION_LEFT_RIGHT;

    return GestureDetector(
      onTap: () => ref.read(selectedProvider.notifier).state = "",
      child: InteractiveViewer(
        constrained: false,
        boundaryMargin: EdgeInsets.symmetric(
          horizontal: MediaQuery.of(context).size.width,
          vertical: MediaQuery.of(context).size.height,
        ),
        minScale: 0.0001,
        maxScale: 2.6,
        child: GraphView(
          graph: graph,
          algorithm: SugiyamaAlgorithm(builder),
          paint: Paint()
            ..color = Colors.green
            ..strokeWidth = 1
            ..style = PaintingStyle.stroke,
          builder: (node) {
            final id = node.key!.value as String?;

            final rule = page.rules.firstWhereOrNull((r) => r.id == id);

            if (rule != null) {
              return NodeWidget(
                id: rule.id,
                name: rule.formattedName,
                backgroundColor: rule.backgroundColor(context),
                icon: rule.icon(context),
              );
            }

            final event = page.events.firstWhereOrNull((e) => e.id == id);

            if (event != null) {
              return NodeWidget(
                id: event.id,
                name: event.formattedName,
                backgroundColor: event.backgroundColor(context),
                icon: event.icon(context),
              );
            }

            return const SizedBox();
          },
        ),
      ),
    );
  }
}

class _AppBar extends HookConsumerWidget {
  const _AppBar();

  @override
  Widget build(BuildContext context, WidgetRef ref) => AppBar(
        title: Text("Page Editor", style: Theme.of(context).textTheme.titleLarge),
        elevation: 0,
        backgroundColor: Colors.transparent,
        leading: IconButton(
          icon: const Icon(
            FontAwesomeIcons.chevronLeft,
          ),
          color: Theme.of(context).textTheme.titleLarge?.color,
          onPressed: () async {
            final sureLeave = await showDialog<bool>(
              context: context,
              builder: (context) => AlertDialog(
                title: const Text("Are you sure you want to leave?"),
                content: const Text(
                  "Any unsaved changes will be lost if you leave this page.",
                ),
                actions: [
                  TextButton(
                    onPressed: () => Navigator.of(context).pop(false),
                    child: const Text("No"),
                  ),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.red,
                    ),
                    onPressed: () => Navigator.of(context).pop(true),
                    child: const Text("Yes"),
                  ),
                ],
              ),
            );

            if (sureLeave ?? false) {
              ref.read(pageProvider.notifier).model = const PageModel();
            }
          },
        ),
        actions: [
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 8.0),
            child: Tooltip(
              message: "Shortcut: Ctrl/Cmd + Shift + P",
              waitDuration: const Duration(milliseconds: 500),
              child: TextButton(
                onPressed: () {
                  final action = Actions.maybeFind<SearchIntent>(context);
                  if (action == null) return;
                  Actions.of(context).invokeAction(action, SearchIntent());
                },
                child: Row(
                  children: const [
                    Text("Search Nodes"),
                    SizedBox(width: 8),
                    Icon(FontAwesomeIcons.magnifyingGlass, size: 18),
                  ],
                ),
              ),
            ),
          ),
          const SizedBox(width: 18),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 8.0),
            child: ElevatedButton(
              onPressed: () async {
                final page = ref.read(pageProvider);
                final error = page.validate();
                if (error != null) {
                  final ignore = await showDialog(
                    context: context,
                    builder: (context) => AlertDialog(
                      title: const Text("Invalid Page"),
                      content: Text(error),
                      actions: [
                        TextButton(
                          onPressed: () => Navigator.of(context).pop(true),
                          style: TextButton.styleFrom(
                            foregroundColor: Colors.redAccent,
                          ),
                          child: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: const [
                              Text("Ignore"),
                              SizedBox(width: 8),
                              Icon(
                                FontAwesomeIcons.triangleExclamation,
                                size: 18,
                              ),
                            ],
                          ),
                        ),
                        ElevatedButton(
                          onPressed: () => Navigator.of(context).pop(false),
                          child: const Text("Cancel"),
                        ),
                      ],
                    ),
                  );
                  if (ignore != true) return;
                }

                final output = await FilePicker.platform.saveFile(
                  dialogTitle: "Save page to a file",
                  allowedExtensions: ["json"],
                  type: FileType.custom,
                  fileName: ref.read(fileNameProvider),
                );
                if (output == null) return;
                final text = pageModelToJson(page);
                await File(output).writeAsString(text);
              },
              child: Row(
                children: const [
                  Text("Save"),
                  SizedBox(width: 8),
                  Icon(FontAwesomeIcons.solidFloppyDisk, size: 18),
                ],
              ),
            ),
          ),
          const SizedBox(width: 20),
        ],
      );
}

class NodeWidget extends HookConsumerWidget {
  const NodeWidget({
    required this.id,
    required this.name,
    required this.backgroundColor,
    this.foregroundColor,
    this.icon,
    super.key,
  });
  final String id;
  final String name;
  final Color backgroundColor;
  final Color? foregroundColor;
  final Widget? icon;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selected = ref.watch(selectedProvider) == id;
    return GestureDetector(
      onTap: () {
        if (selected) {
          ref.read(selectedProvider.notifier).state = "";
        } else {
          ref.read(selectedProvider.notifier).state = id;
        }
      },
      child: Material(
        borderRadius: BorderRadius.circular(4),
        color: backgroundColor,
        child: Padding(
          padding: const EdgeInsets.all(4.0),
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 400),
            curve: Curves.easeOutCirc,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(4),
              border: Border.all(
                color: selected ? Theme.of(context).scaffoldBackgroundColor : backgroundColor,
                width: 3,
              ),
            ),
            child: Padding(
              padding: const EdgeInsets.all(8),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (icon != null) ...[
                    icon!,
                    const SizedBox(width: 12),
                  ],
                  Text(
                    name,
                    style: GoogleFonts.jetBrainsMono(
                      fontSize: 13,
                      color: foregroundColor,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
