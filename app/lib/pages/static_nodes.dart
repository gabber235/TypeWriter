import 'package:dotted_border/dotted_border.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';

class StaticEntries extends HookConsumerWidget {
  const StaticEntries({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    List<Entry> entries =
        ref.watch(pageProvider.select((value) => value.facts));

    final bgColor = entries.isNotEmpty
        ? entries[0].backgroundColor(context)
        : Colors.black.withOpacity(0.1);

    final textColor = entries.isNotEmpty
        ? entries[0].textColor(context)
        : Colors.black.withOpacity(0.1);

    return Container(
      decoration: BoxDecoration(
        borderRadius: const BorderRadius.only(topRight: Radius.circular(8)),
        border: Border.all(color: bgColor),
      ),
      child: Column(
        children: [
          const SizedBox(height: 16),
          Row(
            children: [
              const SizedBox(width: 24),
              Text("Facts", style: Theme.of(context).textTheme.headline6),
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
                          onPressed: () {
                            final fact =
                                ref.read(pageProvider.notifier).addFact();
                            ref.read(selectedProvider.notifier).state = fact.id;
                          },
                          child: Row(
                            children: const [
                              Text("Add Fact"),
                              SizedBox(width: 8),
                              Icon(FontAwesomeIcons.plus, size: 18),
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
