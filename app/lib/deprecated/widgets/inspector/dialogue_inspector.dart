import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/deprecated/models/page.dart';
import 'package:typewriter/deprecated/pages/graph.dart';
import 'package:typewriter/deprecated/pages/inspection_menu.dart';
import 'package:typewriter/main.dart';

class DialogueInspector extends HookConsumerWidget {
  final Dialogue dialogue;

  const DialogueInspector({
    Key? key,
    required this.dialogue,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        EntryInformation(
          entry: dialogue,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(name: name)),
        ),
        const Divider(),
        TriggersField(
          title: dialogue is OptionDialogue ? "Global Triggers" : "Triggers",
          triggers: dialogue.triggers,
          onAdd: () => ref.read(pageProvider.notifier).insertEntry(
              dialogue.copyWith(triggers: [...dialogue.triggers, ""])),
          onChanged: (index, trigger) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(triggers: [
                ...dialogue.triggers.sublist(0, index),
                trigger,
                ...dialogue.triggers.sublist(index + 1),
              ])),
          onRemove: (trigger) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(triggers: [
                ...dialogue.triggers.where((e) => e != trigger),
              ])),
        ),
        const Divider(),
        const SectionTitle(title: "Triggered By"),
        const SizedBox(height: 8),
        SizableList(
          hintText: "Enter a trigger",
          addButtonText: "Add Trigger By",
          icon: FontAwesomeIcons.satelliteDish,
          list: dialogue.triggeredBy,
          onAdd: () => ref.read(pageProvider.notifier).insertEntry(
              dialogue.copyWith(triggeredBy: [...dialogue.triggeredBy, ""])),
          onChanged: (index, value) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(triggeredBy: [
                ...dialogue.triggeredBy.sublist(0, index),
                value,
                ...dialogue.triggeredBy.sublist(index + 1),
              ])),
          onRemove: (value) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(triggeredBy: [
                ...dialogue.triggeredBy.where((e) => e != value),
              ])),
          onQuery: (value) =>
              ref.read(pageProvider).triggerEntries.expand((entry) => [
                    ...entry.triggers,
                    if (entry is RuleEntry) ...entry.triggeredBy,
                    if (entry is OptionDialogue)
                      ...entry.options.expand((e) => e.triggers),
                  ]),
        ),
        const Divider(),
        const SectionTitle(title: "Criteria"),
        const SizedBox(height: 8),
        CriteriaField(
          criteria: dialogue.criteria,
          operators: const ["==", ">=", "<=", ">", "<"],
          onAdd: () => ref.read(pageProvider.notifier).insertEntry(dialogue
                  .copyWith(criteria: [
                ...dialogue.criteria,
                const Criterion(fact: "", operator: "==", value: 0)
              ])),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(criteria: [
                ...dialogue.criteria.sublist(0, index),
                criterion,
                ...dialogue.criteria.sublist(index + 1),
              ])),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(criteria: [
                ...dialogue.criteria.sublist(0, index),
                ...dialogue.criteria.sublist(index + 1),
              ])),
        ),
        const Divider(),
        const SectionTitle(title: "Modifiers"),
        const SizedBox(height: 8),
        CriteriaField(
          addButtonText: "Add Modifier",
          criteria: dialogue.modifiers,
          operators: const ["=", "+"],
          onAdd: () => ref.read(pageProvider.notifier).insertEntry(dialogue
                  .copyWith(modifiers: [
                ...dialogue.modifiers,
                const Criterion(fact: "", operator: "=", value: 0)
              ])),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(modifiers: [
                ...dialogue.modifiers.sublist(0, index),
                criterion,
                ...dialogue.modifiers.sublist(index + 1),
              ])),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertEntry(dialogue.copyWith(modifiers: [
                ...dialogue.modifiers.sublist(0, index),
                ...dialogue.modifiers.sublist(index + 1),
              ])),
        ),
        const Divider(),
        _SpeakerField(dialogue: dialogue),
        const Divider(),
        _TextField(dialogue: dialogue),

        // ----------------- Custom Fields -----------------

        if (dialogue is SpokenDialogue) ...[
          const Divider(),
          _DurationField(dialogue: dialogue as SpokenDialogue),
        ],
        if (dialogue is OptionDialogue) ...[
          const Divider(),
          _OptionsList(dialogue: dialogue as OptionDialogue),
        ],

        // -------------------------------------------------
        const Divider(),
        Operations(entry: dialogue),
      ],
    );
  }
}

class _SpeakerField extends HookConsumerWidget {
  final Dialogue dialogue;

  const _SpeakerField({Key? key, required this.dialogue}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
      const SectionTitle(title: "Speaker"),
      const SizedBox(height: 8),
      SpeakerSelector(
          currentId: dialogue.speaker,
          onSelected: (value) {
            ref.read(pageProvider.notifier).insertEntry(dialogue.copyWith(
                  speaker: value.id,
                ));
          }),
    ]);
  }
}

class _TextField extends HookConsumerWidget {
  final Dialogue dialogue;

  const _TextField({
    Key? key,
    required this.dialogue,
  }) : super(key: key);

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertEntry(dialogue.copyWith(
          text: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final textEditingController = useTextEditingController(text: dialogue.text);

    return Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
      const SectionTitle(title: "Text"),
      const SizedBox(height: 8),
      TextField(
        controller: textEditingController,
        onSubmitted: (value) {
          _onChanged(value, ref);
        },
        onEditingComplete: () {
          _onChanged(textEditingController.text, ref);
        },
        onChanged: (value) {
          _onChanged(value, ref);
        },
        textAlign: TextAlign.right,
        textCapitalization: TextCapitalization.none,
        textInputAction: TextInputAction.done,
        maxLines: null,
        decoration: const InputDecoration(
          hintText: "Enter speakable text",
          suffixIcon: Icon(FontAwesomeIcons.solidMessage, size: 18),
          contentPadding: EdgeInsets.only(left: 8, top: 12, bottom: 12),
        ),
      ),
    ]);
  }
}

class _DurationField extends HookConsumerWidget {
  final SpokenDialogue dialogue;

  const _DurationField({
    Key? key,
    required this.dialogue,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Duration"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: (dialogue.duration * 50).toString(),
          keyboardType: TextInputType.number,
          inputFormatters: [
            FilteringTextInputFormatter.allow(RegExp(r'[0-9]')),
          ],
          onChanged: (value) {
            ref.read(pageProvider.notifier).insertEntry(dialogue.copyWith(
                  duration: (int.tryParse(value) ?? 0) ~/ 50,
                ));
          },
          hintText: "Enter a duration (ms)",
          icon: FontAwesomeIcons.solidClock,
        ),
      ],
    );
  }
}

class _OptionsList extends HookConsumerWidget {
  final OptionDialogue dialogue;

  const _OptionsList({
    Key? key,
    required this.dialogue,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final expanded = useState([]);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Options"),
        const SizedBox(height: 8),
        ExpansionPanelList(
          expansionCallback: (index, isExpanded) {
            if (isExpanded) {
              expanded.value = expanded.value.where((e) => e != index).toList();
            } else {
              expanded.value = [...expanded.value, index];
            }
          },
          children: [
            for (var i = 0; i < dialogue.options.length; i++)
              ExpansionPanel(
                isExpanded: expanded.value.contains(i),
                backgroundColor: context.isDark
                    ? Colors.black.withOpacity(0.05)
                    : Colors.white.withOpacity(0.80),
                headerBuilder: (context, expanded) {
                  return Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      if (expanded) ...[
                        IconButton(
                          onPressed: () =>
                              ref.read(pageProvider.notifier).insertEntry(
                                    dialogue.copyWith(
                                      options: [
                                        ...dialogue.options.sublist(0, i),
                                        ...dialogue.options.sublist(i + 1),
                                      ],
                                    ),
                                  ),
                          icon: const Icon(FontAwesomeIcons.trash, size: 18),
                          color: Colors.red,
                        ),
                        const Spacer(),
                      ],
                      Flexible(
                        child: SectionTitle(title: dialogue.options[i].text),
                      ),
                      const SizedBox(width: 8),
                    ],
                  );
                },
                body: _OptionField(
                  option: dialogue.options[i],
                  index: i,
                  onChanged: (option) =>
                      ref.read(pageProvider.notifier).insertEntry(
                            dialogue.copyWith(
                              options: [
                                ...dialogue.options.sublist(0, i),
                                option,
                                ...dialogue.options.sublist(i + 1),
                              ],
                            ),
                          ),
                ),
              )
          ],
        ),
        TextButton(
          onPressed: () {
            ref.read(pageProvider.notifier).insertEntry(dialogue.copyWith(
                  options: [...dialogue.options, const Option(text: "")],
                ));
          },
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: const [
              Text("Add Option"),
              SizedBox(width: 4),
              Icon(
                FontAwesomeIcons.plus,
                size: 18,
              )
            ],
          ),
        ),
      ],
    );
  }
}

class _OptionField extends HookWidget {
  final Option option;
  final int index;

  final Function(Option) onChanged;

  const _OptionField({
    Key? key,
    required this.option,
    required this.index,
    required this.onChanged,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          const Divider(),
          const SectionTitle(title: "Text"),
          const SizedBox(height: 8),
          SingleLineTextField(
            text: option.text,
            onChanged: (value) {
              onChanged(option.copyWith(text: value));
            },
            hintText: "Enter text",
            icon: FontAwesomeIcons.solidHandPointer,
          ),
          const Divider(),
          TriggersField(
            triggers: option.triggers,
            onAdd: () => onChanged(option.copyWith(
              triggers: [...option.triggers, ""],
            )),
            onChanged: (index, trigger) => onChanged(option.copyWith(
              triggers: [
                ...option.triggers.sublist(0, index),
                trigger,
                ...option.triggers.sublist(index + 1),
              ],
            )),
            onRemove: (trigger) => onChanged(option.copyWith(
              triggers: option.triggers.where((t) => t != trigger).toList(),
            )),
          ),
          const Divider(),
          const SectionTitle(title: "Criteria"),
          const SizedBox(height: 8),
          CriteriaField(
            criteria: option.criteria,
            operators: const ["==", ">=", "<=", ">", "<"],
            onAdd: () => onChanged(option.copyWith(
              criteria: [
                ...option.criteria,
                const Criterion(fact: "", operator: "==", value: 0)
              ],
            )),
            onChanged: (index, criteria) => onChanged(option.copyWith(
              criteria: [
                ...option.criteria.sublist(0, index),
                criteria,
                ...option.criteria.sublist(index + 1),
              ],
            )),
            onRemove: (index) => onChanged(option.copyWith(
              criteria: [
                ...option.criteria.sublist(0, index),
                ...option.criteria.sublist(index + 1),
              ],
            )),
          ),
          const Divider(),
          const SectionTitle(title: "Modifiers"),
          const SizedBox(height: 8),
          CriteriaField(
            criteria: option.modifiers,
            operators: const ["=", "+"],
            addButtonText: "Add Modifier",
            onAdd: () => onChanged(option.copyWith(
              modifiers: [
                ...option.modifiers,
                const Criterion(fact: "", operator: "=", value: 0)
              ],
            )),
            onChanged: (index, modifier) => onChanged(option.copyWith(
              modifiers: [
                ...option.modifiers.sublist(0, index),
                modifier,
                ...option.modifiers.sublist(index + 1),
              ],
            )),
            onRemove: (index) => onChanged(option.copyWith(
              modifiers: [
                ...option.modifiers.sublist(0, index),
                ...option.modifiers.sublist(index + 1),
              ],
            )),
          ),
        ],
      ),
    );
  }
}
