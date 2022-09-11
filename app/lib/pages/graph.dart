import 'dart:io';
import 'dart:math';

import 'package:collection/collection.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:graphview/GraphView.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/inspection_menu.dart';
import 'package:typewriter/pages/open_page.dart';
import 'package:typewriter/widgets/dropdown.dart';

final fileNameProvider = StateProvider<String>((ref) {
  return "test.json";
});

class PageNotifier extends StateNotifier<PageModel> {
  PageNotifier() : super(const PageModel());

  PageModel get model => state;

  void setModel(PageModel model) {
    state = model;
  }

  static const _chars =
      'AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890';
  final Random _random = Random();

  String getRandomString(int length) => String.fromCharCodes(Iterable.generate(
      length, (_) => _chars.codeUnitAt(_random.nextInt(_chars.length))));

  void addFact() {
    final fact = Fact(
      name: "new_fact",
      id: getRandomString(15),
    );
    insertFact(fact);
  }

  void addDialogue() {
    final dialogue = SpokenDialogue(
      name: "new_dialogue",
      id: getRandomString(15),
      speaker: "",
      text: "",
      triggeredBy: [],
      triggers: [],
    );
    insertDialogue(dialogue);
  }

  void addEvent() {
    final event = NpcEvent(
      name: "new_event",
      id: getRandomString(15),
      identifier: "",
      triggers: [],
    );
    insertEvent(event);
  }

  void insertFact(Fact fact) {
    state = state
        .copyWith(facts: [...state.facts.where((e) => e.id != fact.id), fact]);
  }

  void insertEvent(Event event) {
    state = state.copyWith(
        events: [...state.events.where((e) => e.id != event.id), event]);
  }

  void insertDialogue(Dialogue dialogue) {
    state = state.copyWith(dialogue: [
      ...state.dialogue.where((d) => d.id != dialogue.id),
      dialogue
    ]);
  }

  void transformType(Entry entry, String type) {
    if (entry is Event) {
      Event event = entry;
      if (type == "npc_interact") {
        event = Event.npc(
          name: event.name,
          id: event.id,
          identifier: "",
          triggers: event.triggers,
        );
      }

      insertEvent(event);
    } else if (entry is Dialogue) {
      Dialogue dialogue = entry;
      if (type == "spoken") {
        dialogue = Dialogue.spoken(
          name: dialogue.name,
          id: dialogue.id,
          triggers: dialogue.triggers,
          triggeredBy: dialogue.triggeredBy,
          criteria: dialogue.criteria,
          modifiers: dialogue.modifiers,
          text: dialogue.text,
          speaker: dialogue.speaker,
        );
      } else if (type == "option") {
        dialogue = Dialogue.option(
          name: dialogue.name,
          id: dialogue.id,
          triggers: dialogue.triggers,
          triggeredBy: dialogue.triggeredBy,
          criteria: dialogue.criteria,
          modifiers: dialogue.modifiers,
          text: dialogue.text,
          speaker: dialogue.speaker,
        );
      }

      insertDialogue(dialogue);
    }
  }

  void deleteEntry(String id) {
    state = state.copyWith(
      facts: state.facts.where((e) => e.id != id).toList(),
      events: state.events.where((e) => e.id != id).toList(),
      dialogue: state.dialogue.where((d) => d.id != id).toList(),
    );
  }
}

final pageProvider = StateNotifierProvider<PageNotifier, PageModel>((ref) {
  return PageNotifier();
});

final selectedProvider = StateProvider<String>((ref) {
  return "";
});

class PageGraph extends HookConsumerWidget {
  const PageGraph({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final page = ref.watch(pageProvider);
    final selectedId = ref.watch(selectedProvider);

    final selected = page.getEntry(selectedId);

    if (page.events.isEmpty && page.rules.isEmpty) {
      return const SizedBox.expand(
        child: OpenPage(),
      );
    }

    return Scaffold(
      appBar: const PreferredSize(
        preferredSize: Size.fromHeight(60),
        child: _AppBar(),
      ),
      body: Stack(
        children: [
          const _Graph(),
          if (selected != null)
            Align(
              alignment: const Alignment(0.98, 0),
              child: InspectionMenu(
                entry: selected,
              ),
            ),
        ],
      ),
    );
  }
}

class _Graph extends HookConsumerWidget {
  const _Graph({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final page = ref.watch(pageProvider);
    final graph = page.toGraph();
    final builder = SugiyamaConfiguration()
      ..nodeSeparation = (30)
      ..levelSeparation = (30)
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
            builder: (Node node) {
              final id = node.key!.value as String?;

              final rule = page.rules.firstWhereOrNull((r) => r.id == id);

              if (rule != null) {
                return _Node(
                  id: rule.id,
                  name: rule.formattedName,
                  color: rule.backgroundColor(context),
                );
              }

              final event = page.events.firstWhereOrNull((e) => e.id == id);

              if (event != null) {
                return _Node(
                  id: event.id,
                  name: event.formattedName,
                  color: event.backgroundColor(context),
                );
              }

              return const SizedBox();
            },
          )),
    );
  }
}

class _AppBar extends HookConsumerWidget {
  const _AppBar({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return AppBar(
      title: Text("Page Editor", style: Theme.of(context).textTheme.titleLarge),
      elevation: 0,
      backgroundColor: Colors.transparent,
      leading: IconButton(
        icon: const Icon(FontAwesomeIcons.chevronLeft),
        onPressed: () async {
          final sureLeave = await showDialog<bool>(
            context: context,
            builder: (context) => AlertDialog(
              title: const Text("Are you sure you want to leave?"),
              content: const Text(
                  "Any unsaved changes will be lost if you leave this page."),
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

          if (sureLeave == true) {
            ref.read(pageProvider.notifier).setModel(const PageModel());
          }
        },
      ),
      actions: [
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 8.0),
          child: TextButton(
            onPressed: () => showDialog(
                context: context, builder: (context) => const _FactModal()),
            child: Row(
              children: const [
                Text("Modify Facts"),
                SizedBox(width: 8),
                Icon(FontAwesomeIcons.penFancy, size: 18),
              ],
            ),
          ),
        ),
        const SizedBox(width: 8),
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 8.0),
          child: TextButton(
            onPressed: () => ref.read(pageProvider.notifier).addDialogue(),
            child: Row(
              children: const [
                Text("Add Event"),
                SizedBox(width: 8),
                Icon(FontAwesomeIcons.bolt, size: 18),
              ],
            ),
          ),
        ),
        const SizedBox(width: 8),
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 8.0),
          child: TextButton(
            onPressed: () => ref.read(pageProvider.notifier).addDialogue(),
            child: Row(
              children: const [
                Text("Add Dialogue"),
                SizedBox(width: 8),
                Icon(FontAwesomeIcons.solidComments, size: 18),
              ],
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
                            Icon(FontAwesomeIcons.triangleExclamation,
                                size: 18),
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

              String? output = await FilePicker.platform.saveFile(
                dialogTitle: "Save page to a file",
                allowedExtensions: ["json"],
                type: FileType.custom,
                fileName: ref.read(fileNameProvider),
              );
              if (output == null) return;
              final text = pageModelToJson(page);
              File(output).writeAsString(text);
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
}

class _Node extends HookConsumerWidget {
  final String id;
  final String name;
  final Color color;

  const _Node({
    Key? key,
    required this.id,
    required this.name,
    required this.color,
  }) : super(key: key);

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
        color: color,
        child: Padding(
          padding: const EdgeInsets.all(4.0),
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 400),
            curve: Curves.easeOutCirc,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(4),
              border: Border.all(
                color: selected
                    ? Theme.of(context).scaffoldBackgroundColor
                    : color,
                width: 3,
              ),
            ),
            child: Padding(
              padding: const EdgeInsets.all(8),
              child: Text(
                name,
                style: GoogleFonts.jetBrainsMono(fontSize: 13),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

final _factProvider = Provider<Fact>((ref) {
  return const Fact(id: "", name: "");
});

class _FactModal extends HookConsumerWidget {
  const _FactModal({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final facts = ref.watch(pageProvider.select((value) => value.facts));
    return SimpleDialog(
      title: const Text(
        "Facts",
        style: TextStyle(fontSize: 30),
      ),
      children: [
        DataTable(
          showBottomBorder: true,
          showCheckboxColumn: true,
          dataRowHeight: 60,
          columnSpacing: 20,
          columns: const [
            DataColumn(label: Text("Name")),
            DataColumn(label: Text("Lifetime")),
            DataColumn(label: Text("Data")),
            DataColumn(label: Text("Options")),
          ],
          rows: [
            for (final fact in facts)
              DataRow(
                selected: false,
                cells: [
                  DataCell(ProviderScope(
                    overrides: [
                      _factProvider.overrideWithValue(fact),
                    ],
                    child: const _TableNameCell(),
                  )),
                  DataCell(ProviderScope(
                    overrides: [
                      _factProvider.overrideWithValue(fact),
                    ],
                    child: const _TableLifetimeCell(),
                  )),
                  DataCell(ProviderScope(
                    overrides: [
                      _factProvider.overrideWithValue(fact),
                    ],
                    child: const _TableDataCell(),
                  )),
                  DataCell(
                    IconButton(
                      icon: const Icon(FontAwesomeIcons.trash, size: 18),
                      color: Colors.redAccent,
                      onPressed: () async {
                        final delete = await showDialog<bool>(
                          context: context,
                          builder: (context) => AlertDialog(
                            title: const Text("Delete Fact"),
                            content: const Text(
                                "Are you sure you want to delete this fact?"),
                            actions: [
                              TextButton(
                                onPressed: () => Navigator.pop(context, false),
                                child: const Text("Cancel"),
                              ),
                              ElevatedButton(
                                onPressed: () => Navigator.pop(context, true),
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.redAccent,
                                ),
                                child: Row(
                                  mainAxisSize: MainAxisSize.min,
                                  children: const [
                                    Text("Delete"),
                                    SizedBox(width: 8),
                                    Icon(
                                      FontAwesomeIcons.trash,
                                      size: 18,
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                        );
                        if (delete == true) {
                          ref.read(pageProvider.notifier).deleteEntry(fact.id);
                        }
                      },
                    ),
                  ),
                ],
              ),
          ],
        ),
        const SizedBox(height: 12),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextButton(
              onPressed: () {
                ref.read(pageProvider.notifier).addFact();
              },
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: const [
                  Text("Add Fact"),
                  SizedBox(width: 4),
                  Icon(
                    FontAwesomeIcons.plus,
                    size: 18,
                  )
                ],
              ),
            ),
          ],
        ),
      ],
    );
  }
}

class _TableNameCell extends HookConsumerWidget {
  const _TableNameCell({
    Key? key,
  }) : super(key: key);

  void _updateFact(String name, WidgetRef ref) {
    ref
        .read(pageProvider.notifier)
        .insertFact(ref.read(_factProvider).copyWith(name: name));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final fact = ref.watch(_factProvider);
    final controller = useTextEditingController(text: fact.formattedName);
    final focus = useFocusNode();
    final focused = useState(false);

    useEffect(() {
      focus.addListener(() {
        if (focus.hasFocus) {
          controller.text = fact.name;
          focused.value = true;
          controller.selection =
              TextSelection.collapsed(offset: controller.text.length);
        } else {
          controller.text = fact.formattedName;
          focused.value = false;
        }
      });
      return null;
    });

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: TextField(
        controller: controller,
        focusNode: focus,
        textCapitalization: TextCapitalization.none,
        textInputAction: TextInputAction.done,
        maxLines: 1,
        onChanged: (name) => _updateFact(name, ref),
        onSubmitted: (name) => _updateFact(name, ref),
        onEditingComplete: () => controller.text,
        inputFormatters: [
          TextInputFormatter.withFunction((oldValue, newValue) =>
              newValue.copyWith(
                  text: newValue.text.toLowerCase().replaceAll(" ", "_"))),
          FilteringTextInputFormatter.singleLineFormatter,
          FilteringTextInputFormatter.allow(RegExp(r'[a-z0-9_.]')),
        ],
        decoration: InputDecoration(
          contentPadding: const EdgeInsets.symmetric(horizontal: 8),
          filled: focused.value,
        ),
      ),
    );
  }
}

class _TableLifetimeCell extends HookConsumerWidget {
  const _TableLifetimeCell({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final lifetime = ref.watch(_factProvider.select((value) => value.lifetime));
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Dropdown(
        value: lifetime,
        values: FactLifetime.values,
        filled: false,
        padding: EdgeInsets.zero,
        builder: (context, lifetime) => Padding(
          padding: const EdgeInsets.symmetric(vertical: 4.0, horizontal: 8.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(lifetime.formattedName),
              Text(lifetime.description,
                  style: Theme.of(context).textTheme.caption),
            ],
          ),
        ),
        onChanged: (lifetime) => ref
            .read(pageProvider.notifier)
            .insertFact(ref.read(_factProvider).copyWith(
                  lifetime: lifetime,
                  data: "",
                )),
      ),
    );
  }
}

class _TableDataCell extends HookConsumerWidget {
  const _TableDataCell({
    Key? key,
  }) : super(key: key);

  void _updateFact(String data, WidgetRef ref) {
    ref
        .read(pageProvider.notifier)
        .insertFact(ref.read(_factProvider).copyWith(data: data));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final lifetime = ref.watch(_factProvider.select((value) => value.lifetime));
    final data = ref.watch(_factProvider.select((value) => value.data));
    final controller = useTextEditingController(text: data);
    final focus = useFocusNode();
    final focused = useState(false);

    if (lifetime == FactLifetime.permanent ||
        lifetime == FactLifetime.server ||
        lifetime == FactLifetime.session) {
      return Container();
    }

    useEffect(() {
      focus.addListener(() {
        focused.value = focus.hasFocus;
      });
    });

    useEffect(() {
      if (controller.text != data) controller.text = data;
      return null;
    }, [data]);

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: TextField(
        controller: controller,
        focusNode: focus,
        textCapitalization: TextCapitalization.none,
        textInputAction: TextInputAction.done,
        maxLines: 1,
        onChanged: (data) => _updateFact(data, ref),
        onSubmitted: (data) => _updateFact(data, ref),
        inputFormatters: [
          FilteringTextInputFormatter.singleLineFormatter,
          if (lifetime == FactLifetime.cron)
            FilteringTextInputFormatter.allow(RegExp(r'[0-9*,\-/ ]')),
          if (lifetime == FactLifetime.timed)
            FilteringTextInputFormatter.allow(RegExp(r"[0-9a-z ]"))
        ],
        decoration: InputDecoration(
          contentPadding: const EdgeInsets.symmetric(horizontal: 8),
          filled: focused.value,
        ),
      ),
    );
  }
}
