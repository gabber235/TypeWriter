import 'dart:convert';

import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:graphview/GraphView.dart';
import 'package:tuple/tuple.dart';
import 'package:typewriter/main.dart';

part 'page.freezed.dart';

part 'page.g.dart';

PageModel pageModelFromJson(String str) => PageModel.fromJson(json.decode(str));

String pageModelToJson(PageModel data) => json.encode(data.toJson());

@freezed
class PageModel with _$PageModel {
  const factory PageModel({
    @Default([]) List<Fact> facts,
    @Default([]) List<Speaker> speakers,
    @Default([]) List<Event> events,
    @Default([]) List<Dialogue> dialogue,
    @Default([]) List<ActionEntry> actions,
  }) = _PageModel;

  factory PageModel.fromJson(Map<String, dynamic> json) =>
      _$PageModelFromJson(json);
}

abstract class Entry {
  String get name;

  String get id;
}

@freezed
class Fact with _$Fact implements Entry {
  const factory Fact({
    required String id,
    required String name,
    @Default("") String comment,
    @Default(FactLifetime.permanent) FactLifetime lifetime,
    @Default("") String data,
  }) = _Fact;

  factory Fact.fromJson(Map<String, dynamic> json) => _$FactFromJson(json);
}

enum FactLifetime {
  permanent("Saved permanently, it never gets removed"),
  cron("Saved until a specified date, like \"0 0 * * 1\""),
  timed("Saved for a specified duration, like 20 minutes"),
  session("Saved until a player logouts of the server"),
  ;

  final String description;

  const FactLifetime(this.description);
}

@freezed
class Speaker with _$Speaker implements Entry {
  const factory Speaker({
    required String id,
    required String name,
    @Default("") @JsonKey(name: "display_name") String displayName,
  }) = _Speaker;

  factory Speaker.fromJson(Map<String, dynamic> json) =>
      _$SpeakerFromJson(json);
}

abstract class TriggerEntry extends Entry {
  List<String> get triggers;
}

abstract class RuleEntry extends TriggerEntry {
  List<String> get triggeredBy;

  List<Criterion> get criteria;

  List<Criterion> get modifiers;
}

@Freezed(unionKey: "type", unionValueCase: FreezedUnionCase.snake)
class Event with _$Event implements TriggerEntry {
  const factory Event({
    required String name,
    required String id,
    @Default([]) List<String> triggers,
  }) = _Event;

  @FreezedUnionValue("npc_interact")
  const factory Event.npc({
    required String name,
    required String id,
    @Default([]) List<String> triggers,
    @Default("") String identifier,
  }) = NpcEvent;

  @FreezedUnionValue("run_command")
  const factory Event.runCommand({
    required String name,
    required String id,
    @Default([]) List<String> triggers,
    @Default("") String command,
  }) = RunCommandEvent;

  // SuperiorSkyblock 2 Events
  @FreezedUnionValue("island_create")
  const factory Event.islandCreate({
    required String name,
    required String id,
    @Default([]) List<String> triggers,
  }) = IslandCreateEvent;

  factory Event.fromJson(Map<String, dynamic> json) => _$EventFromJson(json);
}

@Freezed(unionKey: "type", unionValueCase: FreezedUnionCase.snake)
class Dialogue with _$Dialogue implements RuleEntry {
  const factory Dialogue({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    @Default("") String speaker,
    @Default("") String text,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
  }) = _Dialogue;

  const factory Dialogue.spoken({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    @Default("") String speaker,
    @Default("") String text,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
    @Default(40) int duration,
  }) = SpokenDialogue;

  const factory Dialogue.option({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    @Default("") String speaker,
    @Default("") String text,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
    @Default([]) List<Option> options,
  }) = OptionDialogue;

  const factory Dialogue.message({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    @Default("") String speaker,
    @Default("") String text,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
  }) = MessageDialogue;

  factory Dialogue.fromJson(Map<String, dynamic> json) =>
      _$DialogueFromJson(json);
}

@Freezed(unionKey: "type", unionValueCase: FreezedUnionCase.snake)
class ActionEntry with _$ActionEntry implements RuleEntry {
  @FreezedUnionValue("default")
  const factory ActionEntry({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
  }) = _ActionEntry;

  @FreezedUnionValue("simple")
  const factory ActionEntry.simple({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
  }) = SimpleAction;

  factory ActionEntry.fromJson(Map<String, dynamic> json) =>
      _$ActionEntryFromJson(json);
}

@freezed
class Criterion with _$Criterion {
  const factory Criterion({
    required String fact,
    required String operator,
    required int value,
  }) = _Criterion;

  factory Criterion.fromJson(Map<String, dynamic> json) =>
      _$CriterionFromJson(json);
}

@freezed
class Option with _$Option {
  const factory Option({
    required String text,
    @Default([]) List<String> triggers,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
  }) = _Option;

  factory Option.fromJson(Map<String, dynamic> json) => _$OptionFromJson(json);
}

enum EntryType<E extends Entry> {
  fact<Fact>(
    "Fact",
    iconBuilder: factIcon,
    icon: FontAwesomeIcons.fileSignature,
    lightColor: Color(0xFFE1BEE7),
    darkColor: Color(0xFF7B1FA2),
    factory: factFactory,
  ),
  speaker<Speaker>(
    "Speaker",
    icon: FontAwesomeIcons.userTag,
    lightColor: Color(0xFFFFE0B2),
    darkColor: Color(0xFFF57C00),
    factory: speakerFactory,
  ),
  npcEvent<NpcEvent>(
    "Npc Interact Event",
    icon: FontAwesomeIcons.userTie,
    factory: npcEventFactory,
  ),
  runCommandEvent<RunCommandEvent>(
    "Run Command Event",
    icon: FontAwesomeIcons.terminal,
    factory: runCommandEventFactory,
  ),
  islandCreateEvent<IslandCreateEvent>(
    "Island Create Event",
    icon: FontAwesomeIcons.houseMedicalCircleCheck,
    factory: islandCreateEventFactory,
  ),
  event<Event>(
    "Event",
    lightColor: Color(0xFFFFE57F),
    darkColor: Color(0xffFBB612),
  ),
  spokenDialogue<SpokenDialogue>(
    "Spoken Dialogue",
    icon: FontAwesomeIcons.solidMessage,
    lightColor: Color(0xFFBBDEFB),
    darkColor: Color(0xFF1E88E5),
    factory: spokenDialogueFactory,
  ),
  optionDialogue<OptionDialogue>(
    "Option Dialogue",
    icon: FontAwesomeIcons.list,
    lightColor: Color(0xFFC8E6C9),
    darkColor: Color(0xFF4CAF50),
    factory: optionDialogueFactory,
  ),
  messageDialogue<MessageDialogue>(
    "Message Dialogue",
    icon: FontAwesomeIcons.solidEnvelope,
    lightColor: Color(0xff6279a1),
    darkColor: Color(0xff1c4da3),
    factory: messageDialogueFactory,
  ),
  simpleAction<SimpleAction>(
    "Simple Action",
    icon: FontAwesomeIcons.personRunning,
    lightColor: Color(0xFFFFCDD2),
    darkColor: Color(0xFFD32F2F),
    factory: simpleActionFactory,
  ),
  ;

  final String name;
  final Color? lightColor;
  final Color? darkColor;

  final Widget Function(E entry)? iconBuilder;
  final IconData? icon;

  final Entry Function(String id)? factory;

  const EntryType(
    this.name, {
    this.lightColor,
    this.darkColor,
    this.iconBuilder,
    this.icon,
    this.factory,
  });

  Widget? getIcon(E? entry, bool dark) {
    Widget? widget;
    if (iconBuilder != null && entry != null) {
      widget = iconBuilder!(entry);
    } else if (icon != null) {
      widget = Icon(icon);
    }
    if (widget == null) return null;

    final color =
        (dark ? darkColor : lightColor) ?? entry?.color(dark) ?? getColor(dark);

    return IconTheme(
      data: IconThemeData(
        color: color,
        size: 18,
      ),
      child: widget,
    );
  }

  Color getColor(bool dark) {
    if ((dark ? darkColor : lightColor) != null) {
      return dark ? darkColor! : lightColor!;
    }
    final types = EntryType.findTypesWhereType<E>();
    final typing = types.firstWhereOrNull(
        (type) => dark ? type.darkColor != null : type.lightColor != null);

    if (typing != null) {
      return (dark ? typing.darkColor : typing.lightColor) ?? Colors.grey;
    }
    return Colors.grey;
  }

  Color textColor(BuildContext context) {
    return getColor(Theme.of(context).brightness == Brightness.light);
  }

  Color backgroundColor(BuildContext context) {
    return getColor(Theme.of(context).brightness == Brightness.dark);
  }

  bool isType(Entry entry) {
    return entry is E;
  }

  bool isSubtype<S>() => <S>[] is List<E>;

  E? parseType(Entry entry) {
    return isType(entry) ? entry as E : null;
  }

  static Tuple2<T, EntryType<T>>? findType<T extends Entry>(Entry entry) {
    final type = EntryType.values
        .whereType<EntryType<T>>()
        .firstWhereOrNull((type) => type.isType(entry));
    if (type == null) return null;
    final t = type.parseType(entry);
    if (t == null) return null;
    return Tuple2(t, type);
  }

  static List<EntryType<T>> findTypes<T extends Entry>(Entry entry) {
    return EntryType.values
        .whereType<EntryType<T>>()
        .where((type) => type.isType(entry))
        .toList();
  }

  static List<EntryType<Entry>> findTypesWhereType<T extends Entry>() {
    return EntryType.values.where((type) => type.isSubtype<T>()).toList();
  }
}

Fact factFactory(String id) => Fact(id: id, name: 'new_fact');

Speaker speakerFactory(String id) => Speaker(id: id, name: 'new_speaker');

NpcEvent npcEventFactory(String id) => NpcEvent(id: id, name: 'new_npc_event');

RunCommandEvent runCommandEventFactory(String id) =>
    RunCommandEvent(id: id, name: 'new_run_command_event');

IslandCreateEvent islandCreateEventFactory(String id) =>
    IslandCreateEvent(id: id, name: 'new_island_create_event');

SpokenDialogue spokenDialogueFactory(String id) =>
    SpokenDialogue(id: id, name: 'new_spoken_dialogue');

OptionDialogue optionDialogueFactory(String id) =>
    OptionDialogue(id: id, name: 'new_option_dialogue');

MessageDialogue messageDialogueFactory(String id) =>
    MessageDialogue(id: id, name: 'new_message_dialogue');

SimpleAction simpleActionFactory(String id) =>
    SimpleAction(id: id, name: 'new_simple_action');

Widget factIcon(Fact fact) {
  switch (fact.lifetime) {
    case FactLifetime.permanent:
      return const Icon(FontAwesomeIcons.solidHardDrive);
    case FactLifetime.cron:
      return const Icon(FontAwesomeIcons.clockRotateLeft);
    case FactLifetime.timed:
      return const Icon(FontAwesomeIcons.stopwatch);
    case FactLifetime.session:
      return const Icon(FontAwesomeIcons.userClock);
  }
}

extension LifetimeExtension on FactLifetime {
  String get formattedName => name.split(".").last.capitalize;
}

extension EntryExtension on Entry {
  bool get isFact => this is Fact;

  bool get isSpeaker => this is Speaker;

  bool get isEvent => this is Event;

  bool get isDialogue => this is Dialogue;

  bool get isAction => this is ActionEntry;

  Fact? get asFact => this is Fact ? this as Fact : null;

  Speaker? get asSpeaker => this is Speaker ? this as Speaker : null;

  Event? get asEvent => this is Event ? this as Event : null;

  Dialogue? get asDialogue => this is Dialogue ? this as Dialogue : null;

  ActionEntry? get asAction => this is ActionEntry ? this as ActionEntry : null;

  String get formattedName {
    return name
        .split(".")
        .map((e) => e.capitalize)
        .join(" | ")
        .split("_")
        .map((e) => e.capitalize)
        .join(" ");
  }

  Widget icon(BuildContext context) {
    return EntryType.findTypes(this)
            .map((type) => type.getIcon(this, !context.isDark))
            .whereNotNull()
            .firstOrNull ??
        const SizedBox.shrink();
  }

  Color textColor(BuildContext context) {
    return color(Theme.of(context).brightness == Brightness.light);
  }

  Color backgroundColor(BuildContext context) {
    return color(Theme.of(context).brightness == Brightness.dark);
  }

  Color color(bool dark) =>
      EntryType.findType(this)?.item2.getColor(dark) ?? Colors.grey;
}

extension PageModelExtension on PageModel {
  List<Entry> get entries => [...facts, ...speakers, ...triggerEntries];

  List<TriggerEntry> get triggerEntries => [...rules, ...events];

  List<RuleEntry> get rules => [...dialogue, ...actions];

  Entry? getEntry(String id) {
    return entries.firstWhereOrNull((e) => e.id == id);
  }

  Graph toGraph() {
    final graph = Graph();

    for (final entry in entries) {
      final node = Node.Id(entry.id);
      graph.addNode(node);
    }

    // Create edges
    for (final entry in triggerEntries) {
      for (final trigger in entry.triggers) {
        rules
            .where((rule) => rule.triggeredBy.contains(trigger))
            .forEach((rule) {
          final color = entry.isEvent
              ? Colors.amberAccent.shade700
              : entry.isDialogue
                  ? Colors.blue
                  : Colors.grey;

          graph.addEdge(Node.Id(entry.id), Node.Id(rule.id),
              paint: Paint()..color = color);
        });
      }
    }

    for (final dialogue in dialogue.whereType<OptionDialogue>()) {
      for (final trigger in dialogue.options.expand((o) => o.triggers)) {
        rules.where((rule) => rule.triggeredBy.contains(trigger)).forEach((r) {
          graph.addEdge(Node.Id(dialogue.id), Node.Id(r.id),
              paint: Paint()..color = Colors.green);
        });
      }
    }

    return graph;
  }

  String? validate() {
    // Check if all entries have unique ids
    final ids = entries.map((e) => e.id).toSet();
    if (ids.length != entries.length) {
      return "All entries must have unique ids";
    }
    // Check if all entries names are not blank
    if (entries.any((e) => e.name.trim().isEmpty)) {
      return "All entries must have a name";
    }

    // Check if all rules have at least one triggered_by
    if (dialogue.any((e) => e.triggeredBy.isEmpty)) {
      return "All rules must have at least one triggered_by, ${dialogue.firstWhere((e) => e.triggeredBy.isEmpty).name} does not";
    }

    // All NpcEvents must have a valid npc identifier
    if (events.any((e) => e is NpcEvent && e.identifier.isEmpty)) {
      return "All NpcEvents must have a valid npc identifier, ${events.firstWhere((e) => e is NpcEvent && e.identifier.isEmpty).name} does not";
    }

    // All dialogue must have a valid text
    if (dialogue.any((e) => e.text.isEmpty)) {
      return "All SpokenDialogues must have a valid text, ${dialogue.firstWhere((e) => e.text.isEmpty).name} does not";
    }
    // All dialogue must have a non-empty speaker
    if (dialogue.any((e) => e.speaker.isEmpty)) {
      return "All SpokenDialogues must have a non-empty speaker, ${dialogue.firstWhere((e) => e.speaker.isEmpty).name} does not";
    }
    // All dialogue speakers must be a valid speaker
    if (dialogue.any((e) => speakers.none((s) => s.id == e.speaker))) {
      return "All SpokenDialogues must have a valid speaker, ${dialogue.firstWhere((e) => speakers.none((s) => s.id == e.speaker)).name} does not";
    }

    // All SpokenDialogues must have a valid speaker
    if (dialogue.any((e) => e is SpokenDialogue && e.speaker.isEmpty)) {
      return "All SpokenDialogues must have a valid speaker, ${dialogue.firstWhere((e) => e is SpokenDialogue && e.speaker.isEmpty).name} does not";
    }
    // All SpokenDialogues must have a valid duration
    if (dialogue.any((e) => e is SpokenDialogue && e.duration <= 0)) {
      return "All SpokenDialogues must have a duration > 0, ${dialogue.firstWhere((e) => e is SpokenDialogue && e.duration <= 0).name} does not";
    }
    // All OptionDialogues must have a valid speaker
    if (dialogue.any((e) => e is OptionDialogue && e.speaker.isEmpty)) {
      return "All OptionDialogues must have a valid speaker, ${dialogue.firstWhere((e) => e is OptionDialogue && e.speaker.isEmpty).name} does not";
    }
    // All OptionDialogues must have at least one option
    if (dialogue.any((e) => e is OptionDialogue && e.options.isEmpty)) {
      return "All OptionDialogues must have at least one option, ${dialogue.firstWhere((e) => e is OptionDialogue && e.options.isEmpty).name} does not";
    }
    // All OptionDialogues options must have a valid text
    if (dialogue.any(
        (e) => e is OptionDialogue && e.options.any((o) => o.text.isEmpty))) {
      return "All OptionDialogues options must have a valid text, ${dialogue.firstWhere((e) => e is OptionDialogue && e.options.any((o) => o.text.isEmpty)).name} does not";
    }

    // All Facts must have a valid name
    if (facts.any((e) => e.name.isEmpty)) {
      return "All Facts must have a valid name, ${facts.firstWhere((e) => e.name.isEmpty).name} does not";
    }

    String? checkCriterion(Entry entry, Criterion criterion) {
      // Check if the criterion has a existing fact
      if (facts.firstWhereOrNull((f) => f.id == criterion.fact) == null) {
        return "A Criterion/Modifier in ${entry.name} has a invalid fact: ${criterion.fact}";
      }
      return null;
    }

    // Check for all rules if the criteria and modifiers are valid
    for (final rule in rules) {
      for (final criterion in rule.criteria) {
        final error = checkCriterion(rule, criterion);
        if (error != null) {
          return error;
        }
      }
      for (final modification in rule.modifiers) {
        final error = checkCriterion(rule, modification);
        if (error != null) {
          return error;
        }
      }

      if (rule is OptionDialogue) {
        for (final option in rule.options) {
          for (final criterion in option.criteria) {
            final error = checkCriterion(rule, criterion);
            if (error != null) {
              return error;
            }
          }
          for (final modification in option.modifiers) {
            final error = checkCriterion(rule, modification);
            if (error != null) {
              return error;
            }
          }
        }
      }
    }

    // Check that all rules are triggered by an entry or option
    for (final rule in rules) {
      if (triggerEntries
              .where((e) => rule.triggeredBy.any((t) => e.triggers.contains(t)))
              .isEmpty &&
          dialogue
              .where((e) =>
                  e is OptionDialogue &&
                  e.options
                      .expand((o) => o.triggers)
                      .any((t) => rule.triggeredBy.contains(t)))
              .isEmpty) {
        return "Rule ${rule.name} is triggered by ${rule.triggeredBy} which is not triggered by any entry or option";
      }
    }

    return null;
  }
}
