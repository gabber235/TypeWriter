import 'package:auto_size_text/auto_size_text.dart';
import 'package:collection/collection.dart';
import 'package:dropdown_search/dropdown_search.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/deprecated/models/page.dart';
import 'package:typewriter/deprecated/pages/graph.dart';
import 'package:typewriter/deprecated/widgets/dropdown.dart';
import 'package:typewriter/deprecated/widgets/inspector/action_inspector.dart';
import 'package:typewriter/deprecated/widgets/inspector/dialogue_inspector.dart';
import 'package:typewriter/deprecated/widgets/inspector/event_inspector.dart';
import 'package:typewriter/deprecated/widgets/inspector/facts_inspector.dart';
import 'package:typewriter/deprecated/widgets/inspector/speaker_inspector.dart';
import 'package:typewriter/utils/extensions.dart';

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
      return Theme(data: themData, child: FactInspector(fact: fact));
    }

    final speaker = entry.asSpeaker;
    if (speaker != null) {
      return Theme(data: themData, child: SpeakerInspector(speaker: speaker));
    }

    final event = entry.asEvent;
    if (event != null) {
      return Theme(data: themData, child: EventInspector(event: event));
    }

    final dialogue = entry.asDialogue;
    if (dialogue != null) {
      return Theme(
          data: themData, child: DialogueInspector(dialogue: dialogue));
    }

    final action = entry.asAction;
    if (action != null) {
      return Theme(data: themData, child: ActionInspector(action: action));
    }

    return Container();
  }
}

// ------- Generic entry information -------
//<editor-fold desc="Entry Information">
class EntryInformation extends StatelessWidget {
  final Entry entry;

  final Function(String) onNameChanged;

  const EntryInformation({
    Key? key,
    required this.entry,
    required this.onNameChanged,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        Title(
          title: entry.formattedName,
          color: entry.textColor(context),
        ),
        Identifier(id: entry.id),
        const Divider(),
        NameField(name: entry.name, onChanged: onNameChanged),
      ],
    );
  }
}

class SectionTitle extends StatelessWidget {
  final String title;

  const SectionTitle({
    Key? key,
    required this.title,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      title,
      style: const TextStyle(fontSize: 14),
      maxLines: 1,
      overflow: TextOverflow.ellipsis,
    );
  }
}

class Title extends StatelessWidget {
  final String title;
  final Color color;

  const Title({
    Key? key,
    required this.title,
    required this.color,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AutoSizeText(
      title,
      style: TextStyle(color: color, fontSize: 40, fontWeight: FontWeight.w900),
      maxLines: 1,
    );
  }
}

class Identifier extends StatelessWidget {
  final String id;

  const Identifier({Key? key, required this.id}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SelectableText(id, style: Theme.of(context).textTheme.caption);
  }
}

class SingleLineTextField extends HookWidget {
  final String? text;
  final Function(String)? onChanged;
  final List<TextInputFormatter>? inputFormatters;
  final TextInputType keyboardType;
  final String? hintText;
  final IconData? icon;

  const SingleLineTextField({
    Key? key,
    this.text,
    this.onChanged,
    this.inputFormatters,
    this.keyboardType = TextInputType.text,
    this.hintText,
    this.icon,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = useTextEditingController(text: text);
    return TextField(
      controller: controller,
      onEditingComplete: () => onChanged?.call(controller.text),
      onSubmitted: onChanged,
      onChanged: onChanged,
      textAlign: TextAlign.right,
      textCapitalization: TextCapitalization.none,
      textInputAction: TextInputAction.done,
      maxLines: 1,
      keyboardType: keyboardType,
      inputFormatters: [
        FilteringTextInputFormatter.singleLineFormatter,
        if (inputFormatters != null) ...inputFormatters!,
      ],
      decoration: InputDecoration(
        suffixIcon: icon != null ? Icon(icon, size: 18) : null,
        hintText: hintText,
      ),
    );
  }
}

class NameField extends HookWidget {
  final String name;
  final Function(String) onChanged;

  const NameField({Key? key, required this.name, required this.onChanged})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Name"),
        const SizedBox(height: 1),
        SingleLineTextField(
          text: name,
          onChanged: onChanged,
          inputFormatters: [
            snakeCaseFormatter(),
            FilteringTextInputFormatter.allow(RegExp(r'[a-z0-9_.]')),
          ],
          hintText: "Enter a name",
          icon: FontAwesomeIcons.signature,
        ),
      ],
    );
  }
}

class TriggersField extends HookConsumerWidget {
  final String title;
  final List<String> triggers;

  final Function() onAdd;
  final Function(int, String) onChanged;
  final Function(String) onRemove;

  const TriggersField({
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
        SectionTitle(title: title),
        const SizedBox(height: 1),
        SizableList(
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

class AutoCompleteField extends StatelessWidget {
  final String text;
  final String hintText;
  final IconData? icon;

  final Iterable<String> Function(String) onQuery;
  final Function(String) onChanged;
  final List<TextInputFormatter>? inputFormatters;

  const AutoCompleteField({
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

class SizableList extends StatelessWidget {
  final String hintText;
  final String addButtonText;
  final IconData icon;
  final List<String> list;

  final Function() onAdd;
  final Function(int, String) onChanged;
  final Function(String) onRemove;

  final Iterable<String> Function(String) onQuery;

  const SizableList({
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
              child: AutoCompleteField(
                text: list[i],
                hintText: hintText,
                icon: icon,
                onChanged: (text) => onChanged(i, text),
                onQuery: onQuery,
                inputFormatters: [
                  snakeCaseFormatter(),
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

class CriteriaField extends HookConsumerWidget {
  final String addButtonText;
  final List<Criterion> criteria;
  final List<String> operators;

  final Function() onAdd;
  final Function(int, Criterion) onChanged;
  final Function(int) onRemove;

  const CriteriaField({
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
                onChanged: (value) {
                  final intValue = int.tryParse(value) ?? 0;
                  onChanged(i, criteria[i].copyWith(value: intValue));
                },
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

class TypeSelector extends StatelessWidget {
  final List<String> types;
  final String selected;
  final Function(String) onChanged;

  const TypeSelector({
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

class Operations extends StatelessWidget {
  const Operations({
    Key? key,
    required this.entry,
  }) : super(key: key);

  final Entry entry;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SectionTitle(title: "Operations"),
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

class SpeakerSelector extends HookConsumerWidget {
  final String currentId;
  final Function(Speaker) onSelected;

  const SpeakerSelector({
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
