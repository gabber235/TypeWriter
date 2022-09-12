import 'dart:convert';

import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:graphview/GraphView.dart';
import 'package:typewriter/main.dart';

part 'page.freezed.dart';

part 'page.g.dart';

PageModel pageModelFromJson(String str) => PageModel.fromJson(json.decode(str));

String pageModelToJson(PageModel data) => json.encode(data.toJson());

@freezed
class PageModel with _$PageModel {
  const factory PageModel({
    @Default([]) List<Fact> facts,
    @Default([]) List<Event> events,
    @Default([]) List<Dialogue> dialogue,
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
    @Default(FactLifetime.permanent) FactLifetime lifetime,
    @Default("") String data,
  }) = _Fact;

  factory Fact.fromJson(Map<String, dynamic> json) => _$FactFromJson(json);
}

enum FactLifetime {
  permanent("Saved permanently, it never gets removed"),
  cron("Saved until a specified date, like \"0 0 * * 1\""),
  timed("Saved for a specified duration, like 20 minutes"),
  server("Saved until the shutdown of a server"),
  session("Saved until a player logouts of the server"),
  ;

  final String description;

  const FactLifetime(this.description);
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
    @Default("") String identifier,
    @Default([]) List<String> triggers,
  }) = NpcEvent;

  factory Event.fromJson(Map<String, dynamic> json) => _$EventFromJson(json);
}

@Freezed(unionKey: "type", unionValueCase: FreezedUnionCase.snake)
class Dialogue with _$Dialogue implements RuleEntry {
  const factory Dialogue({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    required String speaker,
    required String text,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
  }) = _Dialogue;

  const factory Dialogue.spoken({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    required String speaker,
    required String text,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
    @Default(40) int duration,
  }) = SpokenDialogue;

  const factory Dialogue.option({
    required String name,
    required String id,
    @Default([]) @JsonKey(name: "triggered_by") List<String> triggeredBy,
    @Default([]) List<String> triggers,
    required String speaker,
    required String text,
    @Default([]) List<Criterion> criteria,
    @Default([]) List<Criterion> modifiers,
    @Default([]) List<Option> options,
  }) = OptionDialogue;

  factory Dialogue.fromJson(Map<String, dynamic> json) =>
      _$DialogueFromJson(json);
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

extension LifetimeExtension on FactLifetime {
  String get formattedName {
    return name.split(".").last.capitalize;
  }
}

extension EntryExtension on Entry {
  bool get isFact => this is Fact;

  bool get isEvent => this is Event;

  bool get isDialogue => this is Dialogue;

  Fact? get asFact => this is Fact ? this as Fact : null;

  Event? get asEvent => this is Event ? this as Event : null;

  Dialogue? get asDialogue => this is Dialogue ? this as Dialogue : null;

  String get formattedName {
    return name
        .split(".")
        .map((e) => e.capitalize)
        .join(" | ")
        .split("_")
        .map((e) => e.capitalize)
        .join(" ");
  }

  Color textColor(BuildContext context) {
    return color(Theme.of(context).brightness == Brightness.light);
  }

  Color backgroundColor(BuildContext context) {
    return color(Theme.of(context).brightness == Brightness.dark);
  }

  Color color(bool dark) {
    return asFact?.color(dark) ??
        asEvent?.color(dark) ??
        asDialogue?.color(dark) ??
        Colors.grey;
  }
}

extension FactExtension on Fact {
  Color color(bool dark) {
    if (dark) {
      return Colors.purple.shade700;
    } else {
      return Colors.purple.shade100;
    }
  }
}

extension EventExtension on Event {
  Color color(bool dark) {
    if (dark) {
      return const Color(0xffFBB612);
    } else {
      return Colors.amberAccent.shade100;
    }
  }

  String get type {
    if (this is NpcEvent) {
      return "npc_interact";
    } else {
      return "event";
    }
  }
}

extension DialogueExtension on Dialogue {
  Color color(bool dark) {
    if (this is SpokenDialogue) {
      if (dark) {
        return Colors.blue.shade600;
      } else {
        return Colors.blue.shade100;
      }
    } else if (this is OptionDialogue) {
      if (dark) {
        return Colors.green.shade500;
      } else {
        return Colors.green.shade100;
      }
    } else {
      return Colors.grey;
    }
  }

  String get type {
    if (this is SpokenDialogue) {
      return "spoken";
    } else if (this is OptionDialogue) {
      return "option";
    } else {
      return "dialogue";
    }
  }
}

extension PageModelExtension on PageModel {
  List<Entry> get entries => [...facts, ...triggerEntries];

  List<TriggerEntry> get triggerEntries => [...rules, ...events];

  List<RuleEntry> get rules => dialogue;

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

    String? checkCriterion(Criterion criterion) {
      // Check if the criterion has a existing fact
      if (facts.firstWhereOrNull((f) => f.id == criterion.fact) == null) {
        return "Criterion ${criterion.fact} does not have a valid fact";
      }
      return null;
    }

    // Check for all rules if the criteria and modifiers are valid
    for (final rule in rules) {
      for (final criterion in rule.criteria) {
        final error = checkCriterion(criterion);
        if (error != null) {
          return error;
        }
      }
      for (final modification in rule.modifiers) {
        final error = checkCriterion(modification);
        if (error != null) {
          return error;
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
