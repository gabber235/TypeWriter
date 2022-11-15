import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/deprecated/models/page.dart";
import "package:typewriter/deprecated/pages/graph.dart";
import "package:typewriter/deprecated/pages/inspection_menu.dart";

class ActionInspector extends HookConsumerWidget {

  const ActionInspector({
    super.key,
    required this.action,
  });
  final ActionEntry action;

  @override
  Widget build(BuildContext context, WidgetRef ref) => Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        EntryInformation(
          entry: action,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(name: name)),
        ),
        const Divider(),
        TriggersField(
          title: "Triggers",
          triggers: action.triggers,
          onAdd: () => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(triggers: [...action.triggers, ""])),
          onChanged: (index, trigger) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(triggers: [
                ...action.triggers.sublist(0, index),
                trigger,
                ...action.triggers.sublist(index + 1),
              ],),),
          onRemove: (trigger) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(triggers: [
                ...action.triggers.where((e) => e != trigger),
              ],),),
        ),
        const Divider(),
        const SectionTitle(title: "Triggered By"),
        const SizedBox(height: 8),
        SizableList(
          hintText: "Enter a trigger",
          addButtonText: "Add Trigger By",
          icon: FontAwesomeIcons.satelliteDish,
          list: action.triggeredBy,
          onAdd: () => ref.read(pageProvider.notifier).insertEntry(
              action.copyWith(triggeredBy: [...action.triggeredBy, ""]),),
          onChanged: (index, value) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(triggeredBy: [
                ...action.triggeredBy.sublist(0, index),
                value,
                ...action.triggeredBy.sublist(index + 1),
              ],),),
          onRemove: (value) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(triggeredBy: [
                ...action.triggeredBy.where((e) => e != value),
              ],),),
          onQuery: (value) => ref
              .read(pageProvider)
              .triggerEntries
              .expand((entry) => [
                    ...entry.triggers,
                    if (entry is RuleEntry) ...entry.triggeredBy,
                    if (entry is OptionDialogue)
                      ...entry.options.expand((e) => e.triggers),
                  ],)
              .toSet(),
        ),
        const Divider(),
        const SectionTitle(title: "Criteria"),
        const SizedBox(height: 8),
        CriteriaField(
          criteria: action.criteria,
          operators: const ["==", ">=", "<=", ">", "<"],
          onAdd: () => ref.read(pageProvider.notifier).insertEntry(action
                  .copyWith(criteria: [
                ...action.criteria,
                const Criterion(fact: "", operator: "==", value: 0)
              ],),),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(criteria: [
                ...action.criteria.sublist(0, index),
                criterion,
                ...action.criteria.sublist(index + 1),
              ],),),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(criteria: [
                ...action.criteria.sublist(0, index),
                ...action.criteria.sublist(index + 1),
              ],),),
        ),
        const Divider(),
        const SectionTitle(title: "Modifiers"),
        const SizedBox(height: 8),
        CriteriaField(
          addButtonText: "Add Modifier",
          criteria: action.modifiers,
          operators: const ["=", "+"],
          onAdd: () => ref.read(pageProvider.notifier).insertEntry(action
                  .copyWith(modifiers: [
                ...action.modifiers,
                const Criterion(fact: "", operator: "=", value: 0)
              ],),),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(modifiers: [
                ...action.modifiers.sublist(0, index),
                criterion,
                ...action.modifiers.sublist(index + 1),
              ],),),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertEntry(action.copyWith(modifiers: [
                ...action.modifiers.sublist(0, index),
                ...action.modifiers.sublist(index + 1),
              ],),),
        ),

        // ----------------- Custom Fields -----------------
        if (action is DelayedAction) ...[
          const Divider(),
          _DurationField(action: action as DelayedAction),
        ],
        // -------------------------------------------------

        const Divider(),
        Operations(entry: action),
      ],
    );
}

class _DurationField extends HookConsumerWidget {

  const _DurationField({
    required this.action,
  });
  final DelayedAction action;

  @override
  Widget build(BuildContext context, WidgetRef ref) => Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Duration"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: (action.duration).toString(),
          keyboardType: TextInputType.number,
          inputFormatters: [
            FilteringTextInputFormatter.allow(RegExp("[0-9]")),
          ],
          onChanged: (value) {
            ref.read(pageProvider.notifier).insertEntry(action.copyWith(
                  duration: int.tryParse(value) ?? 0,
                ),);
          },
          hintText: "Enter a duration (ms)",
          icon: FontAwesomeIcons.solidClock,
        ),
      ],
    );
}
