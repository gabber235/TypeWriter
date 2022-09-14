import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';
import 'package:typewriter/pages/inspection_menu.dart';

class EventInspector extends HookConsumerWidget {
  final Event event;

  const EventInspector({
    Key? key,
    required this.event,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        EntryInformation(
          entry: event,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertEvent(event.copyWith(name: name)),
        ),
        const Divider(),
        TriggersField(
          title: "Triggers",
          triggers: event.triggers,
          onAdd: () => ref
              .read(pageProvider.notifier)
              .insertEvent(event.copyWith(triggers: [...event.triggers, ""])),
          onChanged: (index, trigger) => ref
              .read(pageProvider.notifier)
              .insertEvent(event.copyWith(triggers: [
                ...event.triggers.take(index),
                trigger,
                ...event.triggers.skip(index + 1),
              ])),
          onRemove: (trigger) => ref.read(pageProvider.notifier).insertEvent(
                event.copyWith(triggers: [
                  ...event.triggers.where((t) => t != trigger),
                ]),
              ),
        ),
        const Divider(),
        const SectionTitle(title: "Type"),
        const SizedBox(height: 8),
        TypeSelector(
            types: const ["npc_interact", "run_command", "island_create"],
            selected: event.type,
            onChanged: (type) =>
                ref.read(pageProvider.notifier).transformType(event, type)),
        if (event is NpcEvent) ...[
          const Divider(),
          _NpcIdentifierField(event: event as NpcEvent),
        ],
        if (event is RunCommandEvent) ...[
          const Divider(),
          _CommandRegexField(event: event as RunCommandEvent),
        ],
        const Divider(),
        Operations(entry: event),
      ],
    );
  }
}

class _NpcIdentifierField extends HookConsumerWidget {
  final NpcEvent event;

  const _NpcIdentifierField({Key? key, required this.event}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Npc Identifier"),
        const SizedBox(height: 8),
        SpeakerSelector(
            currentId: event.identifier,
            onSelected: (value) {
              ref.read(pageProvider.notifier).insertEvent(event.copyWith(
                    identifier: value.id,
                  ));
            }),
      ],
    );
  }
}

class _CommandRegexField extends HookConsumerWidget {
  final RunCommandEvent event;

  const _CommandRegexField({
    Key? key,
    required this.event,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Command Regex"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: event.command,
          inputFormatters: [
            TextInputFormatter.withFunction((oldValue, newValue) {
              if (newValue.text.startsWith("/")) {
                return newValue.copyWith(
                    text: newValue.text.substring(1),
                    selection: TextSelection.collapsed(
                        offset: newValue.text.length - 1));
              }
              return newValue;
            })
          ],
          onChanged: (value) {
            ref.read(pageProvider.notifier).insertEvent(event.copyWith(
                  command: value,
                ));
          },
          hintText: "Enter a command regex",
        ),
      ],
    );
  }
}
