import 'package:auto_size_text/auto_size_text.dart';
import 'package:collection/collection.dart';
import 'package:dropdown_search/dropdown_search.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/main.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';
import 'package:typewriter/widgets/dropdown.dart';

class InspectionMenu extends HookConsumerWidget {
  final Entry entry;

  const InspectionMenu({
    Key? key,
    required this.entry,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(8),
        boxShadow: [
          BoxShadow(
            color: entry.textColor(context),
            spreadRadius: 0,
            blurRadius: 20,
            offset: const Offset(0, 2),
          )
        ],
      ),
      child: ConstrainedBox(
        constraints: BoxConstraints(
          maxHeight: MediaQuery.of(context).size.height * 0.9,
          maxWidth: 400,
          minWidth: 400,
        ),
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(15.0),
            child: Inspector(
              key: Key("inspection_menu_${entry.id}"),
              entry: entry,
            ),
          ),
        ),
      ),
    );
  }
}

class Inspector extends HookConsumerWidget {
  final Entry entry;

  const Inspector({
    Key? key,
    required this.entry,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final themData = Theme.of(context).copyWith(
      primaryColor: entry.textColor(context),
    );

    final fact = entry.asFact;
    if (fact != null) {
      return Theme(data: themData, child: _FactInspector(fact: fact));
    }

    final speaker = entry.asSpeaker;
    if (speaker != null) {
      return Theme(data: themData, child: _SpeakerInspector(speaker: speaker));
    }

    final event = entry.asEvent;
    if (event != null) {
      return Theme(data: themData, child: _EventInspector(event: event));
    }

    final dialogue = entry.asDialogue;
    if (dialogue != null) {
      return Theme(
          data: themData, child: _DialogueInspector(dialogue: dialogue));
    }
    return Container();
  }
}

class _FactInspector extends HookConsumerWidget {
  final Fact fact;

  const _FactInspector({
    Key? key,
    required this.fact,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        _EntryInformation(
          entry: fact,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertFact(fact.copyWith(name: name)),
        ),
        const Divider(),
        _LifetimeField(fact: fact),
        if (fact.lifetime == FactLifetime.cron) ...[
          const Divider(),
          _CronDataField(fact: fact),
        ],
        if (fact.lifetime == FactLifetime.timed) ...[
          const Divider(),
          _TimedDataField(fact: fact),
        ],
        const Divider(),
        _Operations(entry: fact),
      ],
    );
  }
}

class _SpeakerInspector extends HookConsumerWidget {
  final Speaker speaker;

  const _SpeakerInspector({
    Key? key,
    required this.speaker,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        _EntryInformation(
          entry: speaker,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertSpeaker(speaker.copyWith(name: name)),
        ),
        const Divider(),
        _DisplayNameField(speaker: speaker),
        const Divider(),
        _Operations(entry: speaker),
      ],
    );
  }
}

class _EventInspector extends HookConsumerWidget {
  final Event event;

  const _EventInspector({
    Key? key,
    required this.event,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        _EntryInformation(
          entry: event,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertEvent(event.copyWith(name: name)),
        ),
        const Divider(),
        _TriggersField(
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
        const _SectionTitle(title: "Type"),
        const SizedBox(height: 8),
        _TypeSelector(
            types: const ["npc_interact"],
            selected: event.type,
            onChanged: (type) =>
                ref.read(pageProvider.notifier).transformType(event, type)),
        if (event is NpcEvent) ...[
          const Divider(),
          _NpcIdentifierField(event: event as NpcEvent),
        ],
        const Divider(),
        _Operations(entry: event),
      ],
    );
  }
}

class _DialogueInspector extends HookConsumerWidget {
  final Dialogue dialogue;

  const _DialogueInspector({
    Key? key,
    required this.dialogue,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        _EntryInformation(
          entry: dialogue,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(name: name)),
        ),
        const Divider(),
        _TriggersField(
          title: dialogue is OptionDialogue ? "Global Triggers" : "Triggers",
          triggers: dialogue.triggers,
          onAdd: () => ref.read(pageProvider.notifier).insertDialogue(
              dialogue.copyWith(triggers: [...dialogue.triggers, ""])),
          onChanged: (index, trigger) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(triggers: [
                ...dialogue.triggers.sublist(0, index),
                trigger,
                ...dialogue.triggers.sublist(index + 1),
              ])),
          onRemove: (trigger) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(triggers: [
                ...dialogue.triggers.where((e) => e != trigger),
              ])),
        ),
        const Divider(),
        const _SectionTitle(title: "Triggered By"),
        const SizedBox(height: 8),
        _SizableList(
          hintText: "Enter a trigger",
          addButtonText: "Add Trigger By",
          icon: FontAwesomeIcons.satelliteDish,
          list: dialogue.triggeredBy,
          onAdd: () => ref.read(pageProvider.notifier).insertDialogue(
              dialogue.copyWith(triggeredBy: [...dialogue.triggeredBy, ""])),
          onChanged: (index, value) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(triggeredBy: [
                ...dialogue.triggeredBy.sublist(0, index),
                value,
                ...dialogue.triggeredBy.sublist(index + 1),
              ])),
          onRemove: (value) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(triggeredBy: [
                ...dialogue.triggeredBy.where((e) => e != value),
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
              .toSet()
            ..addAll(["system.interaction.start", "system.interaction.end"]),
        ),
        const Divider(),
        const _SectionTitle(title: "Criteria"),
        const SizedBox(height: 8),
        _CriteriaField(
          criteria: dialogue.criteria,
          operators: const ["==", ">=", "<=", ">", "<"],
          onAdd: () => ref.read(pageProvider.notifier).insertDialogue(dialogue
                  .copyWith(criteria: [
                ...dialogue.criteria,
                const Criterion(fact: "", operator: "==", value: 0)
              ])),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(criteria: [
                ...dialogue.criteria.sublist(0, index),
                criterion,
                ...dialogue.criteria.sublist(index + 1),
              ])),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(criteria: [
                ...dialogue.criteria.sublist(0, index),
                ...dialogue.criteria.sublist(index + 1),
              ])),
        ),
        const Divider(),
        const _SectionTitle(title: "Modifiers"),
        const SizedBox(height: 8),
        _CriteriaField(
          addButtonText: "Add Modifier",
          criteria: dialogue.modifiers,
          operators: const ["=", "+"],
          onAdd: () => ref.read(pageProvider.notifier).insertDialogue(dialogue
                  .copyWith(modifiers: [
                ...dialogue.modifiers,
                const Criterion(fact: "", operator: "=", value: 0)
              ])),
          onChanged: (index, criterion) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(modifiers: [
                ...dialogue.modifiers.sublist(0, index),
                criterion,
                ...dialogue.modifiers.sublist(index + 1),
              ])),
          onRemove: (index) => ref
              .read(pageProvider.notifier)
              .insertDialogue(dialogue.copyWith(modifiers: [
                ...dialogue.modifiers.sublist(0, index),
                ...dialogue.modifiers.sublist(index + 1),
              ])),
        ),
        const Divider(),
        const _SectionTitle(title: "Type"),
        const SizedBox(height: 8),
        _TypeSelector(
            types: const ["spoken", "option"],
            selected: dialogue.type,
            onChanged: (type) =>
                ref.read(pageProvider.notifier).transformType(dialogue, type)),
        const Divider(),
        _SpeakerField(dialogue: dialogue),
        const Divider(),
        _TextField(dialogue: dialogue),
        if (dialogue is SpokenDialogue) ...[
          const Divider(),
          _DurationField(dialogue: dialogue as SpokenDialogue),
        ],
        if (dialogue is OptionDialogue) ...[
          const Divider(),
          _OptionsList(dialogue: dialogue as OptionDialogue),
        ],
        const Divider(),
        _Operations(entry: dialogue),
      ],
    );
  }
}

// ------- Generic entry information -------
//<editor-fold desc="Entry Information">
class _EntryInformation extends StatelessWidget {
  final Entry entry;

  final Function(String) onNameChanged;

  const _EntryInformation({
    Key? key,
    required this.entry,
    required this.onNameChanged,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        _Title(
          title: entry.formattedName,
          color: entry.textColor(context),
        ),
        _Identifier(id: entry.id),
        const Divider(),
        _NameField(name: entry.name, onChanged: onNameChanged),
      ],
    );
  }
}

class _SectionTitle extends StatelessWidget {
  final String title;

  const _SectionTitle({
    Key? key,
    required this.title,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      title,
      style: GoogleFonts.jetBrainsMono(
        textStyle: const TextStyle(fontSize: 14),
      ),
      maxLines: 1,
      overflow: TextOverflow.ellipsis,
    );
  }
}

class _Title extends StatelessWidget {
  final String title;
  final Color color;

  const _Title({
    Key? key,
    required this.title,
    required this.color,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AutoSizeText(
      title,
      style: GoogleFonts.roboto(
          textStyle: TextStyle(
              color: color, fontSize: 40, fontWeight: FontWeight.w900)),
      maxLines: 1,
    );
  }
}

class _Identifier extends StatelessWidget {
  final String id;

  const _Identifier({Key? key, required this.id}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SelectableText(id, style: Theme.of(context).textTheme.caption);
  }
}

class _NameField extends HookWidget {
  final String name;
  final Function(String) onChanged;

  const _NameField({Key? key, required this.name, required this.onChanged})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = useTextEditingController(text: name);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const _SectionTitle(title: "Name"),
        const SizedBox(height: 1),
        TextField(
          controller: controller,
          onEditingComplete: () => onChanged(controller.text),
          onSubmitted: onChanged,
          onChanged: onChanged,
          textAlign: TextAlign.right,
          textCapitalization: TextCapitalization.none,
          textInputAction: TextInputAction.done,
          maxLines: 1,
          inputFormatters: [
            TextInputFormatter.withFunction((oldValue, newValue) =>
                newValue.copyWith(
                    text: newValue.text.toLowerCase().replaceAll(" ", "_"))),
            FilteringTextInputFormatter.singleLineFormatter,
            FilteringTextInputFormatter.allow(RegExp(r'[a-z0-9_.]')),
          ],
          decoration: const InputDecoration(
            suffixIcon: Icon(FontAwesomeIcons.signature, size: 18),
            hintText: "Enter a name",
          ),
        ),
      ],
    );
  }
}

class _TriggersField extends HookConsumerWidget {
  final String title;
  final List<String> triggers;

  final Function() onAdd;
  final Function(int, String) onChanged;
  final Function(String) onRemove;

  const _TriggersField({
    Key? key,
    this.title = "Triggers",
    required this.triggers,
    required this.onAdd,
    required this.onChanged,
    required this.onRemove,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        _SectionTitle(title: title),
        const SizedBox(height: 1),
        _SizableList(
          hintText: "Enter a trigger",
          addButtonText: "Add Triggered",
          icon: FontAwesomeIcons.towerCell,
          list: triggers,
          onAdd: onAdd,
          onChanged: onChanged,
          onRemove: onRemove,
          onQuery: (value) => ref
              .read(pageProvider)
              .triggerEntries
              .expand((entry) => [
                    ...entry.triggers,
                    if (entry is RuleEntry) ...entry.triggeredBy,
                    if (entry is OptionDialogue)
                      ...entry.options.expand((e) => e.triggers),
                  ])
              .where((trigger) => trigger.contains(value))
              .toSet(),
        ),
      ],
    );
  }
}

class _SizableList extends StatelessWidget {
  final String hintText;
  final String addButtonText;
  final IconData icon;
  final List<String> list;

  final Function() onAdd;
  final Function(int, String) onChanged;
  final Function(String) onRemove;

  final Iterable<String> Function(String) onQuery;

  const _SizableList({
    Key? key,
    required this.list,
    this.hintText = "Enter a value",
    this.addButtonText = "Add",
    this.icon = FontAwesomeIcons.pen,
    required this.onAdd,
    required this.onChanged,
    required this.onRemove,
    required this.onQuery,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        for (var i = 0; i < list.length; i++) ...[
          const SizedBox(height: 8),
          Row(children: [
            IconButton(
              onPressed: () => onRemove(list[i]),
              icon: const Icon(FontAwesomeIcons.trash, size: 18),
              color: Colors.red,
            ),
            Expanded(
              child: _AutoCompleteField(
                text: list[i],
                hintText: hintText,
                icon: icon,
                onChanged: (text) => onChanged(i, text),
                onQuery: onQuery,
                inputFormatters: [
                  TextInputFormatter.withFunction((oldValue, newValue) =>
                      newValue.copyWith(
                          text: newValue.text
                              .toLowerCase()
                              .replaceAll(" ", "_"))),
                  FilteringTextInputFormatter.singleLineFormatter,
                  FilteringTextInputFormatter.allow(RegExp(r'[a-z0-9_.]')),
                ],
              ),
            )
          ]),
        ],
        const SizedBox(height: 8),
        TextButton(
          onPressed: onAdd,
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(addButtonText),
              const SizedBox(width: 4),
              const Icon(
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

class _AutoCompleteField extends StatelessWidget {
  final String text;
  final String hintText;
  final IconData? icon;

  final Iterable<String> Function(String) onQuery;
  final Function(String) onChanged;
  final List<TextInputFormatter>? inputFormatters;

  const _AutoCompleteField({
    Key? key,
    required this.text,
    this.icon,
    this.hintText = "Enter a value",
    required this.onQuery,
    required this.onChanged,
    this.inputFormatters,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Autocomplete<String>(
      initialValue: TextEditingValue(text: text),
      fieldViewBuilder:
          (context, textEditingController, focusNode, onFieldSubmitted) {
        return TextField(
          controller: textEditingController,
          focusNode: focusNode,
          onSubmitted: (value) {
            onChanged(value);
            onFieldSubmitted();
          },
          onEditingComplete: () {
            onChanged(textEditingController.text);
            onFieldSubmitted();
          },
          onChanged: onChanged,
          textAlign: TextAlign.right,
          textCapitalization: TextCapitalization.none,
          textInputAction: TextInputAction.done,
          maxLines: 1,
          inputFormatters: inputFormatters,
          decoration: InputDecoration(
            hintText: hintText,
            suffixIcon: icon != null ? Icon(icon, size: 18) : null,
          ),
        );
      },
      onSelected: (value) {
        onChanged(value);
      },
      optionsBuilder: (textEditingValue) {
        if (textEditingValue.text.trim().isEmpty) return const Iterable.empty();
        return onQuery(textEditingValue.text)
            .where((value) => value.contains(textEditingValue.text))
            .toSet();
      },
    );
  }
}

class _CriteriaField extends HookConsumerWidget {
  final String addButtonText;
  final List<Criterion> criteria;
  final List<String> operators;

  final Function() onAdd;
  final Function(int, Criterion) onChanged;
  final Function(int) onRemove;

  const _CriteriaField({
    Key? key,
    this.addButtonText = "Add Criterion",
    required this.criteria,
    required this.operators,
    required this.onAdd,
    required this.onChanged,
    required this.onRemove,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final facts = ref.watch(pageProvider.select((value) => value.facts));
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        for (var i = 0; i < criteria.length; i++) ...[
          const SizedBox(height: 8),
          Row(children: [
            IconButton(
              onPressed: () => onRemove(i),
              icon: const Icon(FontAwesomeIcons.trash, size: 18),
              color: Colors.red,
            ),
            Flexible(
              child: DropdownSearch<Fact>(
                items: facts,
                dropdownButtonProps: const DropdownButtonProps(
                  icon: Icon(FontAwesomeIcons.caretDown, size: 18),
                ),
                selectedItem: facts
                    .firstWhereOrNull((fact) => fact.id == criteria[i].fact),
                dropdownDecoratorProps: const DropDownDecoratorProps(
                  textAlign: TextAlign.right,
                  dropdownSearchDecoration: InputDecoration(
                    contentPadding: EdgeInsets.symmetric(horizontal: 8),
                    hintText: "Select a fact",
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.only(
                          topLeft: Radius.circular(8),
                          bottomLeft: Radius.circular(8)),
                      borderSide: BorderSide.none,
                    ),
                  ),
                ),
                popupProps: PopupProps.menu(
                  showSearchBox: true,
                  title: Padding(
                    padding: const EdgeInsets.only(left: 16.0, top: 8),
                    child: Text("Select a fact",
                        style: Theme.of(context).textTheme.titleMedium),
                  ),
                  itemBuilder: (context, item, isSelected) {
                    return ListTile(
                      title: AutoSizeText(item.name, maxLines: 1),
                      subtitle: Text(item.lifetime.formattedName),
                      selected: isSelected,
                    );
                  },
                ),
                dropdownBuilder: (context, fact) {
                  return AutoSizeText(
                    fact?.name ?? "",
                    maxLines: 1,
                    textAlign: TextAlign.right,
                  );
                },
                filterFn: (fact, string) {
                  return fact.name.contains(string) ||
                      fact.formattedName.contains(string);
                },
                onChanged: (fact) {
                  if (fact == null) return;
                  onChanged(i, criteria[i].copyWith(fact: fact.id));
                },
                onSaved: (fact) {
                  if (fact == null) return;
                  onChanged(i, criteria[i].copyWith(fact: fact.id));
                },
              ),
            ),
            Dropdown(
              value: criteria[i].operator,
              values: operators,
              onChanged: (value) =>
                  onChanged(i, criteria[i].copyWith(operator: value)),
              icon: null,
              borderRadius: const BorderRadius.all(Radius.circular(0)),
            ),
            IntrinsicWidth(
              child: TextField(
                controller: TextEditingController(text: "${criteria[i].value}"),
                textAlign: TextAlign.right,
                textCapitalization: TextCapitalization.none,
                textInputAction: TextInputAction.done,
                maxLines: 1,
                keyboardType: TextInputType.number,
                inputFormatters: [
                  FilteringTextInputFormatter.allow(RegExp(r'[0-9]')),
                ],
                decoration: const InputDecoration(
                  contentPadding: EdgeInsets.symmetric(horizontal: 8),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.only(
                      topRight: Radius.circular(8),
                      bottomRight: Radius.circular(8),
                    ),
                    borderSide: BorderSide.none,
                  ),
                ),
                onChanged: (value) =>
                    onChanged(i, criteria[i].copyWith(value: int.parse(value))),
              ),
            ),
          ]),
        ],
        const SizedBox(height: 8),
        TextButton(
          onPressed: onAdd,
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(addButtonText),
              const SizedBox(width: 4),
              const Icon(
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

class _TypeSelector extends StatelessWidget {
  final List<String> types;
  final String selected;
  final Function(String) onChanged;

  const _TypeSelector({
    Key? key,
    required this.types,
    required this.selected,
    required this.onChanged,
  }) : super(key: key);

  String formattedName(String name) {
    // Transform snake_case to Normal Case
    return name.split("_").map((e) => e.capitalize).join(" ");
  }

  @override
  Widget build(BuildContext context) {
    return Dropdown(
      value: selected,
      values: types,
      builder: (context, value) => Text(formattedName(value)),
      onChanged: (value) async {
        if (value != selected) {
          final sure = await showDialog<bool>(
            context: context,
            builder: (context) => AlertDialog(
              title: const Text("Are you sure?"),
              content: const Text(
                  "Changing the type will reset some of the values of this entry."),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context, false),
                  child: const Text("Cancel"),
                ),
                ElevatedButton(
                  onPressed: () => Navigator.of(context).pop(true),
                  style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: const [
                      Text("Change"),
                      SizedBox(width: 4),
                      Icon(FontAwesomeIcons.shuffle, size: 18),
                    ],
                  ),
                ),
              ],
            ),
          );

          if (sure == true) onChanged(value);
        }
      },
    );
  }
}

class _Operations extends StatelessWidget {
  const _Operations({
    Key? key,
    required this.entry,
  }) : super(key: key);

  final Entry entry;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const _SectionTitle(title: "Operations"),
        const SizedBox(height: 8),
        _DeleteEntry(id: entry.id),
      ],
    );
  }
}

class _DeleteEntry extends HookConsumerWidget {
  final String id;

  const _DeleteEntry({
    Key? key,
    required this.id,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return ElevatedButton(
      onPressed: () async {
        final sure = await showDialog<bool>(
          context: context,
          builder: (context) => AlertDialog(
            title: const Text("Delete Entry"),
            content: const Text("Are you sure you want to delete this entry?"),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(false),
                child: const Text("Cancel"),
              ),
              ElevatedButton(
                onPressed: () => Navigator.of(context).pop(true),
                style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: const [
                    Text("Delete"),
                    SizedBox(width: 4),
                    Icon(FontAwesomeIcons.trash, size: 18),
                  ],
                ),
              ),
            ],
          ),
        );

        if (sure == true) {
          ref.read(selectedProvider.notifier).state = "";
          ref.read(pageProvider.notifier).deleteEntry(id);
        }
      },
      style: ElevatedButton.styleFrom(
        backgroundColor: Colors.red,
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: const [
          Text("Delete Entry"),
          SizedBox(width: 4),
          Icon(
            FontAwesomeIcons.trash,
            size: 16,
          )
        ],
      ),
    );
  }
}

class _SpeakerSelector extends HookConsumerWidget {
  final String currentId;
  final Function(Speaker) onSelected;

  const _SpeakerSelector({
    Key? key,
    required this.currentId,
    required this.onSelected,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final speakers = ref.watch(pageProvider.select((value) => value.speakers));
    return DropdownSearch<Speaker>(
      items: speakers,
      selectedItem: speakers.firstWhereOrNull((s) => s.id == currentId),
      dropdownButtonProps: const DropdownButtonProps(
        icon: Icon(FontAwesomeIcons.caretDown, size: 18),
      ),
      dropdownDecoratorProps: DropDownDecoratorProps(
        textAlign: TextAlign.right,
        dropdownSearchDecoration: InputDecoration(
          contentPadding: const EdgeInsets.symmetric(horizontal: 8),
          hintText: "Select a speaker",
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: BorderSide.none,
          ),
        ),
      ),
      popupProps: PopupProps.menu(
        showSearchBox: true,
        title: Padding(
          padding: const EdgeInsets.only(left: 16.0, top: 8),
          child: Text("Select a speaker",
              style: Theme.of(context).textTheme.titleMedium),
        ),
        itemBuilder: (context, item, isSelected) {
          return ListTile(
            title: AutoSizeText(item.name, maxLines: 1),
            selected: isSelected,
          );
        },
      ),
      dropdownBuilder: (context, speaker) {
        return AutoSizeText(
          speaker?.formattedName ?? "",
          maxLines: 1,
          textAlign: TextAlign.right,
        );
      },
      filterFn: (speaker, string) {
        return speaker.name.contains(string) ||
            speaker.formattedName.contains(string);
      },
      onChanged: (speaker) {
        if (speaker == null) return;
        onSelected(speaker);
      },
      onSaved: (fact) {
        if (fact == null) return;
        onSelected(fact);
      },
    );
  }
}

//</editor-fold>
// -------- Fact Specific Widgets --------
//<editor-fold desc="Fact Specific Widgets">
class _LifetimeField extends HookConsumerWidget {
  final Fact fact;

  const _LifetimeField({Key? key, required this.fact}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final lifetime = fact.lifetime;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SizedBox(height: 8),
        const _SectionTitle(title: "Lifetime"),
        const SizedBox(height: 8),
        Dropdown(
          value: lifetime,
          values: FactLifetime.values,
          padding: EdgeInsets.zero,
          alignment: AlignmentDirectional.centerEnd,
          builder: (context, lifetime) => Padding(
            padding: const EdgeInsets.symmetric(horizontal: 5),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(lifetime.formattedName),
                Flexible(
                  child: Text(lifetime.description,
                      style: Theme.of(context).textTheme.caption),
                ),
              ],
            ),
          ),
          onChanged: (lifetime) =>
              ref.read(pageProvider.notifier).insertFact(fact.copyWith(
                    lifetime: lifetime,
                    data: "",
                  )),
        ),
        const SizedBox(height: 8),
      ],
    );
  }
}

class _CronDataField extends HookConsumerWidget {
  final Fact fact;

  const _CronDataField({Key? key, required this.fact}) : super(key: key);

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertFact(fact.copyWith(
          data: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final textController = useTextEditingController(text: fact.data);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SizedBox(height: 8),
        const _SectionTitle(title: "Cron Data"),
        const SizedBox(height: 8),
        TextField(
          controller: textController,
          onSubmitted: (value) {
            _onChanged(value, ref);
          },
          onEditingComplete: () {
            _onChanged(textController.text, ref);
          },
          onChanged: (value) {
            _onChanged(value, ref);
          },
          textAlign: TextAlign.right,
          textCapitalization: TextCapitalization.none,
          textInputAction: TextInputAction.done,
          maxLines: 1,
          inputFormatters: [
            FilteringTextInputFormatter.singleLineFormatter,
            FilteringTextInputFormatter.allow(RegExp(r'[0-9*,\-/ ]')),
          ],
          decoration: const InputDecoration(
            hintText: "Enter a cron expression",
            suffixIcon: Icon(FontAwesomeIcons.clockRotateLeft, size: 18),
          ),
        ),
        const SizedBox(height: 8),
      ],
    );
  }
}

class _TimedDataField extends HookConsumerWidget {
  final Fact fact;

  const _TimedDataField({Key? key, required this.fact}) : super(key: key);

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertFact(fact.copyWith(
          data: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final textController = useTextEditingController(text: fact.data);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SizedBox(height: 8),
        const _SectionTitle(title: "Timed Data"),
        const SizedBox(height: 8),
        TextField(
          controller: textController,
          onSubmitted: (value) {
            _onChanged(value, ref);
          },
          onEditingComplete: () {
            _onChanged(textController.text, ref);
          },
          onChanged: (value) {
            _onChanged(value, ref);
          },
          textAlign: TextAlign.right,
          textCapitalization: TextCapitalization.none,
          textInputAction: TextInputAction.done,
          maxLines: 1,
          inputFormatters: [
            FilteringTextInputFormatter.singleLineFormatter,
            FilteringTextInputFormatter.allow(RegExp(r"[0-9a-z ]")),
          ],
          decoration: const InputDecoration(
            hintText: "Enter a duration",
            suffixIcon: Icon(FontAwesomeIcons.stopwatch, size: 18),
          ),
        ),
        const SizedBox(height: 8),
      ],
    );
  }
}

//</editor-fold>
// -------- Speaker Specific Widgets --------
//<editor-fold desc="Speaker Specific Widgets">
class _DisplayNameField extends HookConsumerWidget {
  final Speaker speaker;

  const _DisplayNameField({Key? key, required this.speaker}) : super(key: key);

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertSpeaker(speaker.copyWith(
          displayName: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final textController = useTextEditingController(text: speaker.displayName);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SizedBox(height: 8),
        const _SectionTitle(title: "Display Name"),
        const SizedBox(height: 8),
        TextField(
          controller: textController,
          onSubmitted: (value) {
            _onChanged(value, ref);
          },
          onEditingComplete: () {
            _onChanged(textController.text, ref);
          },
          onChanged: (value) {
            _onChanged(value, ref);
          },
          textAlign: TextAlign.right,
          textCapitalization: TextCapitalization.none,
          textInputAction: TextInputAction.done,
          maxLines: 1,
          decoration: const InputDecoration(
            hintText: "Enter a display name",
            suffixIcon: Icon(FontAwesomeIcons.tag, size: 18),
          ),
        ),
        const SizedBox(height: 8),
      ],
    );
  }
}

//</editor-fold>
// -------- Event Specific Widgets --------
//<editor-fold desc="Event Specific Widgets">
class _NpcIdentifierField extends HookConsumerWidget {
  final NpcEvent event;

  const _NpcIdentifierField({Key? key, required this.event}) : super(key: key);

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertEvent(event.copyWith(
          identifier: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
      const _SectionTitle(title: "Npc Identifier"),
      const SizedBox(height: 8),
      _SpeakerSelector(
          currentId: event.identifier,
          onSelected: (value) {
            _onChanged(value.id, ref);
          }),
    ]);
  }
}

//</editor-fold>
// -------- Dialogue Specific Widgets --------
//<editor-fold desc="Dialogue Specific Widgets">
class _SpeakerField extends HookConsumerWidget {
  final Dialogue dialogue;

  const _SpeakerField({Key? key, required this.dialogue}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
      const _SectionTitle(title: "Speaker"),
      const SizedBox(height: 8),
      _SpeakerSelector(
          currentId: dialogue.speaker,
          onSelected: (value) {
            ref.read(pageProvider.notifier).insertDialogue(dialogue.copyWith(
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
    ref.read(pageProvider.notifier).insertDialogue(dialogue.copyWith(
          text: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final textEditingController = useTextEditingController(text: dialogue.text);

    return Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
      const _SectionTitle(title: "Text"),
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
        style: GoogleFonts.jetBrainsMono(),
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
    final textEditingController = useTextEditingController(
      text: (dialogue.duration * 50).toString(),
    );

    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const _SectionTitle(title: "Duration"),
        const SizedBox(height: 8),
        TextField(
          controller: textEditingController,
          textAlign: TextAlign.right,
          textCapitalization: TextCapitalization.none,
          textInputAction: TextInputAction.done,
          style: GoogleFonts.jetBrainsMono(),
          maxLines: 1,
          keyboardType: TextInputType.number,
          onChanged: (value) {
            ref.read(pageProvider.notifier).insertDialogue(dialogue.copyWith(
                  duration: (int.tryParse(value) ?? 0) ~/ 50,
                ));
          },
          decoration: const InputDecoration(
            hintText: "Enter a duration",
            suffixIcon: Icon(FontAwesomeIcons.solidClock, size: 18),
          ),
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
        const _SectionTitle(title: "Options"),
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
                              ref.read(pageProvider.notifier).insertDialogue(
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
                        child: _SectionTitle(title: dialogue.options[i].text),
                      ),
                      const SizedBox(width: 8),
                    ],
                  );
                },
                body: _OptionField(
                  option: dialogue.options[i],
                  index: i,
                  onChanged: (option) =>
                      ref.read(pageProvider.notifier).insertDialogue(
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
            ref.read(pageProvider.notifier).insertDialogue(dialogue.copyWith(
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
    final textEditingController = useTextEditingController(text: option.text);

    return Padding(
      padding: const EdgeInsets.all(8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          const Divider(),
          const _SectionTitle(title: "Text"),
          const SizedBox(height: 8),
          TextField(
            controller: textEditingController,
            textAlign: TextAlign.right,
            textCapitalization: TextCapitalization.none,
            textInputAction: TextInputAction.done,
            maxLines: 1,
            keyboardType: TextInputType.number,
            onChanged: (value) {
              onChanged(option.copyWith(text: value));
            },
            decoration: const InputDecoration(
              hintText: "Enter text",
              suffixIcon: Icon(FontAwesomeIcons.solidHandPointer, size: 18),
            ),
          ),
          const Divider(),
          _TriggersField(
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
          const _SectionTitle(title: "Criteria"),
          const SizedBox(height: 8),
          _CriteriaField(
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
          const _SectionTitle(title: "Modifiers"),
          const SizedBox(height: 8),
          _CriteriaField(
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

//</editor-fold>
