import 'dart:io';
import 'dart:math';

import 'package:collection/collection.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:graphview/GraphView.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/inspection_menu.dart';
import 'package:typewriter/pages/open_page.dart';
import 'package:typewriter/pages/static_nodes.dart';

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

  Fact addFact() {
    final fact = Fact(
      name: "new_fact",
      id: getRandomString(15),
    );
    insertFact(fact);
    return fact;
  }

  Speaker addSpeaker() {
    final speaker = Speaker(
      name: "new_speaker",
      id: getRandomString(15),
    );
    insertSpeaker(speaker);
    return speaker;
  }

  Event addEvent() {
    final event = NpcEvent(
      name: "new_event",
      id: getRandomString(15),
      identifier: "",
      triggers: [],
    );
    insertEvent(event);
    return event;
  }

  SpokenDialogue addDialogue() {
    final dialogue = SpokenDialogue(
      name: "new_dialogue",
      id: getRandomString(15),
      speaker: "",
      text: "",
      triggeredBy: [],
      triggers: [],
    );
    insertDialogue(dialogue);
    return dialogue;
  }

  ActionEntry addAction() {
    final action = ActionEntry.simple(
      name: "new_action",
      id: getRandomString(15),
      triggeredBy: [],
      triggers: [],
    );
    insertAction(action);
    return action;
  }

  void insertFact(Fact fact) {
    state = state
        .copyWith(facts: [...state.facts.where((e) => e.id != fact.id), fact]);
  }

  void insertSpeaker(Speaker speaker) {
    state = state.copyWith(speakers: [
      ...state.speakers.where((e) => e.id != speaker.id),
      speaker
    ]);
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

  void insertAction(ActionEntry action) {
    state = state.copyWith(
        actions: [...state.actions.where((a) => a.id != action.id), action]);
  }

  void transformType(TriggerEntry entry, String type) {
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
    } else if (entry is ActionEntry) {
      ActionEntry action = entry;
      if (type == "simple") {
        action = ActionEntry.simple(
          name: action.name,
          id: action.id,
          triggers: action.triggers,
          triggeredBy: action.triggeredBy,
          criteria: action.criteria,
          modifiers: action.modifiers,
        );
      }

      insertAction(action);
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
            onPressed: () {
              final action = ref.read(pageProvider.notifier).addAction();
              ref.read(selectedProvider.notifier).state = action.id;
            },
            child: Row(
              children: const [
                Text("Add Action"),
                SizedBox(width: 8),
                Icon(FontAwesomeIcons.personRunning, size: 18),
              ],
            ),
          ),
        ),
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 8.0),
          child: TextButton(
            onPressed: () {
              final event = ref.read(pageProvider.notifier).addEvent();
              ref.read(selectedProvider.notifier).state = event.id;
            },
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
            onPressed: () {
              final dialogue = ref.read(pageProvider.notifier).addDialogue();
              ref.read(selectedProvider.notifier).state = dialogue.id;
            },
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

class NodeWidget extends HookConsumerWidget {
  final String id;
  final String name;
  final Color backgroundColor;
  final Color? foregroundColor;
  final Widget? icon;

  const NodeWidget({
    Key? key,
    required this.id,
    required this.name,
    required this.backgroundColor,
    this.foregroundColor,
    this.icon,
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
        color: backgroundColor,
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
                    : backgroundColor,
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
                        fontSize: 13, color: foregroundColor),
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

extension IconExtension on Entry {
  Widget? icon(BuildContext context) {
    if (isFact) return _factIcon(context);
    if (isSpeaker) return _speakerIcon(context);
    if (isEvent) return _eventIcon(context);
    if (isDialogue) return _dialogueIcon(context);
    if (isAction) return _actionIcon(context);
    return null;
  }

  Widget? _factIcon(BuildContext context) {
    final fact = asFact;
    if (fact == null) return null;
    switch (fact.lifetime) {
      case FactLifetime.permanent:
        return Icon(FontAwesomeIcons.solidHardDrive,
            size: 18, color: fact.textColor(context));
      case FactLifetime.cron:
        return Icon(FontAwesomeIcons.clockRotateLeft,
            size: 18, color: fact.textColor(context));
      case FactLifetime.timed:
        return Icon(FontAwesomeIcons.stopwatch,
            size: 18, color: fact.textColor(context));
      case FactLifetime.session:
        return Icon(FontAwesomeIcons.userClock,
            size: 18, color: fact.textColor(context));
    }
  }

  Widget? _speakerIcon(BuildContext context) {
    return Icon(FontAwesomeIcons.userTag, size: 18, color: textColor(context));
  }

  Widget? _eventIcon(BuildContext context) {
    final event = asEvent;
    if (event == null) return null;
    if (this is NpcEvent) {
      return Icon(FontAwesomeIcons.userTie,
          size: 18, color: event.textColor(context));
    }
    return null;
  }

  Widget? _dialogueIcon(BuildContext context) {
    final dialogue = asDialogue;
    if (dialogue == null) return null;
    if (this is SpokenDialogue) {
      return Icon(FontAwesomeIcons.solidMessage,
          size: 18, color: dialogue.textColor(context));
    }
    if (this is OptionDialogue) {
      return Icon(FontAwesomeIcons.list,
          size: 18, color: dialogue.textColor(context));
    }
    return null;
  }

  Widget? _actionIcon(BuildContext context) {
    final action = asAction;
    if (action == null) return null;
    return Icon(FontAwesomeIcons.personRunning,
        size: 18, color: action.textColor(context));
  }
}
