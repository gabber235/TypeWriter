import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';
import 'package:typewriter/pages/inspection_menu.dart';

class ActionInspector extends HookConsumerWidget {
  final ActionEntry action;

  const ActionInspector({
    Key? key,
    required this.action,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        EntryInformation(
          entry: action,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(name: name)),
        ),
        const Divider(),
        TriggersField(
          title: "Triggers",
          triggers: action.triggers,
          onAdd: () => ref.read(pageProvider.notifier).insertAction(
              action.copyWith(triggers: [...action.triggers, ""])),
          onChanged: (index, trigger) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(triggers: [
                ...action.triggers.sublist(0, index),
                trigger,
                ...action.triggers.sublist(index + 1),
              ])),
          onRemove: (trigger) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(triggers: [
                ...action.triggers.where((e) => e != trigger),
              ])),
        ),
        const Divider(),
        const SectionTitle(title: "Triggered By"),
        const SizedBox(height: 8),
        SizableList(
          hintText: "Enter a trigger",
          addButtonText: "Add Trigger By",
          icon: FontAwesomeIcons.satelliteDish,
          list: action.triggeredBy,
          onAdd: () => ref.read(pageProvider.notifier).insertAction(
              action.copyWith(triggeredBy: [...action.triggeredBy, ""])),
          onChanged: (index, value) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(triggeredBy: [
                ...action.triggeredBy.sublist(0, index),
                value,
                ...action.triggeredBy.sublist(index + 1),
              ])),
          onRemove: (value) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(triggeredBy: [
                ...action.triggeredBy.where((e) => e != value),
              ])),
          onQuery: (value) => ref
              .read(pageProvider)
              .triggerEntries
              .expand((entry) => [
                    ...entry.triggers,
                    if (entry is RuleEntry) ...entry.triggeredBy,
                    if (entry is OptionDialogue)
                      ...entry.options.expand((e) => e.triggers),
                  ])
              .toSet(),
        ),
        const Divider(),
        const SectionTitle(title: "Criteria"),
        const SizedBox(height: 8),
        CriteriaField(
          criteria: action.criteria,
          operators: const ["==", ">=", "<=", ">", "<"],
          onAdd: () => ref.read(pageProvider.notifier).insertAction(action
                  .copyWith(criteria: [
                ...action.criteria,
                const Criterion(fact: "", operator: "==", value: 0)
              ])),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(criteria: [
                ...action.criteria.sublist(0, index),
                criterion,
                ...action.criteria.sublist(index + 1),
              ])),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(criteria: [
                ...action.criteria.sublist(0, index),
                ...action.criteria.sublist(index + 1),
              ])),
        ),
        const Divider(),
        const SectionTitle(title: "Modifiers"),
        const SizedBox(height: 8),
        CriteriaField(
          addButtonText: "Add Modifier",
          criteria: action.modifiers,
          operators: const ["=", "+"],
          onAdd: () => ref.read(pageProvider.notifier).insertAction(action
                  .copyWith(modifiers: [
                ...action.modifiers,
                const Criterion(fact: "", operator: "=", value: 0)
              ])),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(modifiers: [
                ...action.modifiers.sublist(0, index),
                criterion,
                ...action.modifiers.sublist(index + 1),
              ])),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertAction(action.copyWith(modifiers: [
                ...action.modifiers.sublist(0, index),
                ...action.modifiers.sublist(index + 1),
              ])),
        ),
        const Divider(),
        const SectionTitle(title: "Type"),
        const SizedBox(height: 8),
        TypeSelector(
            types: const ["simple"],
            selected: action.type,
            onChanged: (type) =>
                ref.read(pageProvider.notifier).transformType(action, type)),
        const Divider(),
        Operations(entry: action),
      ],
    );
  }
}
