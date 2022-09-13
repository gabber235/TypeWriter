import 'package:dotted_border/dotted_border.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';
import 'package:typewriter/widgets/dropdown.dart';

final _typeProvider = StateProvider<_StaticEntry>((ref) {
  return _StaticEntry.fact;
});

enum _StaticEntry {
  fact("Facts", "Fact"),
  speaker("Speakers", "Speaker"),
  ;

  final String title;
  final String singular;

  const _StaticEntry(this.title, this.singular);

  AlwaysAliveProviderListenable<List<Entry>> selectEntries(WidgetRef ref) {
    switch (this) {
      case _StaticEntry.fact:
        return pageProvider.select((value) => value.facts);
      case _StaticEntry.speaker:
        return pageProvider.select((value) => value.speakers);
    }
  }

  Entry createDummy() {
    switch (this) {
      case _StaticEntry.fact:
        return const Fact(id: "", name: "");
      case _StaticEntry.speaker:
        return const Speaker(id: "", name: "");
    }
  }

  void addEntry(WidgetRef ref) {
    Entry entry;
    switch (this) {
      case _StaticEntry.fact:
        entry = ref.read(pageProvider.notifier).addFact();
        break;
      case _StaticEntry.speaker:
        entry = ref.read(pageProvider.notifier).addSpeaker();
        break;
    }
    ref.read(selectedProvider.notifier).state = entry.id;
  }
}

class StaticEntries extends HookConsumerWidget {
  const StaticEntries({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final type = ref.watch(_typeProvider);
    final selected = ref.watch(selectedProvider);
    List<Entry> entries = ref.watch(type.selectEntries(ref));

    final dummy = type.createDummy();
    final bgColor = dummy.backgroundColor(context);
    final textColor = dummy.textColor(context);

    return AnimatedContainer(
      duration: const Duration(milliseconds: 500),
      curve: Curves.easeOut,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.only(
            topRight: Radius.circular(selected.isNotEmpty ? 8 : 0)),
        border: Border.all(color: bgColor, width: 3),
      ),
      child: Column(
        children: [
          const SizedBox(height: 16),
          Row(
            children: [
              const SizedBox(width: 24),
              Dropdown<_StaticEntry>(
                value: type,
                values: _StaticEntry.values,
                onChanged: (value) {
                  ref.read(_typeProvider.notifier).state = value;
                },
                filled: false,
                builder: (context, value) => Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 8),
                  child: Text(
                    value.title,
                    style: Theme.of(context).textTheme.headline6,
                  ),
                ),
                padding: const EdgeInsets.symmetric(horizontal: 0),
              ),
              const Spacer(),
            ],
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 8),
            child: SizedBox(
              height: 50,
              child: CustomScrollView(
                scrollDirection: Axis.horizontal,
                slivers: [
                  SliverList(
                    delegate: SliverChildBuilderDelegate(
                      childCount: entries.length,
                      (BuildContext context, int index) {
                        return Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 8),
                          child: NodeWidget(
                            id: entries[index].id,
                            name: entries[index].formattedName,
                            backgroundColor:
                                entries[index].backgroundColor(context),
                            icon: entries[index].icon(context),
                          ),
                        );
                      },
                    ),
                  ),
                  SliverToBoxAdapter(
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 8),
                      child: DottedBorder(
                        color: bgColor,
                        strokeWidth: 2,
                        radius: const Radius.circular(4),
                        borderType: BorderType.RRect,
                        dashPattern: const [8, 4],
                        child: TextButton(
                          style: TextButton.styleFrom(
                            foregroundColor: textColor,
                            padding: const EdgeInsets.symmetric(
                                horizontal: 8, vertical: 24),
                          ),
                          onPressed: () => type.addEntry(ref),
                          child: Row(
                            children: [
                              Text("Add ${type.singular}"),
                              const SizedBox(width: 8),
                              const Icon(FontAwesomeIcons.plus, size: 18),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
