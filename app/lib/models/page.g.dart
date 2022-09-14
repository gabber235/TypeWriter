// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'page.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_PageModel _$$_PageModelFromJson(Map<String, dynamic> json) => _$_PageModel(
      facts: (json['facts'] as List<dynamic>?)
              ?.map((e) => Fact.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      speakers: (json['speakers'] as List<dynamic>?)
              ?.map((e) => Speaker.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      events: (json['events'] as List<dynamic>?)
              ?.map((e) => Event.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      dialogue: (json['dialogue'] as List<dynamic>?)
              ?.map((e) => Dialogue.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      actions: (json['actions'] as List<dynamic>?)
              ?.map((e) => ActionEntry.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$_PageModelToJson(_$_PageModel instance) =>
    <String, dynamic>{
      'facts': instance.facts,
      'speakers': instance.speakers,
      'events': instance.events,
      'dialogue': instance.dialogue,
      'actions': instance.actions,
    };

_$_Fact _$$_FactFromJson(Map<String, dynamic> json) => _$_Fact(
      id: json['id'] as String,
      name: json['name'] as String,
      comment: json['comment'] as String? ?? "",
      lifetime: $enumDecodeNullable(_$FactLifetimeEnumMap, json['lifetime']) ??
          FactLifetime.permanent,
      data: json['data'] as String? ?? "",
    );

Map<String, dynamic> _$$_FactToJson(_$_Fact instance) => <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'comment': instance.comment,
      'lifetime': _$FactLifetimeEnumMap[instance.lifetime]!,
      'data': instance.data,
    };

const _$FactLifetimeEnumMap = {
  FactLifetime.permanent: 'permanent',
  FactLifetime.cron: 'cron',
  FactLifetime.timed: 'timed',
  FactLifetime.session: 'session',
};

_$_Speaker _$$_SpeakerFromJson(Map<String, dynamic> json) => _$_Speaker(
      id: json['id'] as String,
      name: json['name'] as String,
      displayName: json['display_name'] as String? ?? "",
    );

Map<String, dynamic> _$$_SpeakerToJson(_$_Speaker instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'display_name': instance.displayName,
    };

_$_Event _$$_EventFromJson(Map<String, dynamic> json) => _$_Event(
      name: json['name'] as String,
      id: json['id'] as String,
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$_EventToJson(_$_Event instance) => <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggers': instance.triggers,
      'type': instance.$type,
    };

_$NpcEvent _$$NpcEventFromJson(Map<String, dynamic> json) => _$NpcEvent(
      name: json['name'] as String,
      id: json['id'] as String,
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      identifier: json['identifier'] as String? ?? "",
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$NpcEventToJson(_$NpcEvent instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggers': instance.triggers,
      'identifier': instance.identifier,
      'type': instance.$type,
    };

_$RunCommandEvent _$$RunCommandEventFromJson(Map<String, dynamic> json) =>
    _$RunCommandEvent(
      name: json['name'] as String,
      id: json['id'] as String,
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      command: json['command'] as String? ?? "",
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$RunCommandEventToJson(_$RunCommandEvent instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggers': instance.triggers,
      'command': instance.command,
      'type': instance.$type,
    };

_$IslandCreateEvent _$$IslandCreateEventFromJson(Map<String, dynamic> json) =>
    _$IslandCreateEvent(
      name: json['name'] as String,
      id: json['id'] as String,
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$IslandCreateEventToJson(_$IslandCreateEvent instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggers': instance.triggers,
      'type': instance.$type,
    };

_$_Dialogue _$$_DialogueFromJson(Map<String, dynamic> json) => _$_Dialogue(
      name: json['name'] as String,
      id: json['id'] as String,
      triggeredBy: (json['triggered_by'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      speaker: json['speaker'] as String,
      text: json['text'] as String,
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$_DialogueToJson(_$_Dialogue instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggered_by': instance.triggeredBy,
      'triggers': instance.triggers,
      'speaker': instance.speaker,
      'text': instance.text,
      'criteria': instance.criteria,
      'modifiers': instance.modifiers,
      'type': instance.$type,
    };

_$SpokenDialogue _$$SpokenDialogueFromJson(Map<String, dynamic> json) =>
    _$SpokenDialogue(
      name: json['name'] as String,
      id: json['id'] as String,
      triggeredBy: (json['triggered_by'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      speaker: json['speaker'] as String,
      text: json['text'] as String,
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      duration: json['duration'] as int? ?? 40,
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$SpokenDialogueToJson(_$SpokenDialogue instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggered_by': instance.triggeredBy,
      'triggers': instance.triggers,
      'speaker': instance.speaker,
      'text': instance.text,
      'criteria': instance.criteria,
      'modifiers': instance.modifiers,
      'duration': instance.duration,
      'type': instance.$type,
    };

_$OptionDialogue _$$OptionDialogueFromJson(Map<String, dynamic> json) =>
    _$OptionDialogue(
      name: json['name'] as String,
      id: json['id'] as String,
      triggeredBy: (json['triggered_by'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      speaker: json['speaker'] as String,
      text: json['text'] as String,
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      options: (json['options'] as List<dynamic>?)
              ?.map((e) => Option.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$OptionDialogueToJson(_$OptionDialogue instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggered_by': instance.triggeredBy,
      'triggers': instance.triggers,
      'speaker': instance.speaker,
      'text': instance.text,
      'criteria': instance.criteria,
      'modifiers': instance.modifiers,
      'options': instance.options,
      'type': instance.$type,
    };

_$MessageDialogue _$$MessageDialogueFromJson(Map<String, dynamic> json) =>
    _$MessageDialogue(
      name: json['name'] as String,
      id: json['id'] as String,
      triggeredBy: (json['triggered_by'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      speaker: json['speaker'] as String,
      text: json['text'] as String,
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$MessageDialogueToJson(_$MessageDialogue instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggered_by': instance.triggeredBy,
      'triggers': instance.triggers,
      'speaker': instance.speaker,
      'text': instance.text,
      'criteria': instance.criteria,
      'modifiers': instance.modifiers,
      'type': instance.$type,
    };

_$_ActionEntry _$$_ActionEntryFromJson(Map<String, dynamic> json) =>
    _$_ActionEntry(
      name: json['name'] as String,
      id: json['id'] as String,
      triggeredBy: (json['triggered_by'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$_ActionEntryToJson(_$_ActionEntry instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggered_by': instance.triggeredBy,
      'triggers': instance.triggers,
      'criteria': instance.criteria,
      'modifiers': instance.modifiers,
      'type': instance.$type,
    };

_$SimpleAction _$$SimpleActionFromJson(Map<String, dynamic> json) =>
    _$SimpleAction(
      name: json['name'] as String,
      id: json['id'] as String,
      triggeredBy: (json['triggered_by'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      $type: json['type'] as String?,
    );

Map<String, dynamic> _$$SimpleActionToJson(_$SimpleAction instance) =>
    <String, dynamic>{
      'name': instance.name,
      'id': instance.id,
      'triggered_by': instance.triggeredBy,
      'triggers': instance.triggers,
      'criteria': instance.criteria,
      'modifiers': instance.modifiers,
      'type': instance.$type,
    };

_$_Criterion _$$_CriterionFromJson(Map<String, dynamic> json) => _$_Criterion(
      fact: json['fact'] as String,
      operator: json['operator'] as String,
      value: json['value'] as int,
    );

Map<String, dynamic> _$$_CriterionToJson(_$_Criterion instance) =>
    <String, dynamic>{
      'fact': instance.fact,
      'operator': instance.operator,
      'value': instance.value,
    };

_$_Option _$$_OptionFromJson(Map<String, dynamic> json) => _$_Option(
      text: json['text'] as String,
      triggers: (json['triggers'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      modifiers: (json['modifiers'] as List<dynamic>?)
              ?.map((e) => Criterion.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$_OptionToJson(_$_Option instance) => <String, dynamic>{
      'text': instance.text,
      'triggers': instance.triggers,
      'criteria': instance.criteria,
      'modifiers': instance.modifiers,
    };
