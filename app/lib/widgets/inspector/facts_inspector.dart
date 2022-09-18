import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/graph.dart';
import 'package:typewriter/pages/inspection_menu.dart';
import 'package:typewriter/widgets/dropdown.dart';

class FactInspector extends HookConsumerWidget {
  final Fact fact;

  const FactInspector({
    Key? key,
    required this.fact,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        EntryInformation(
          entry: fact,
          onNameChanged: (name) => ref
              .read(pageProvider.notifier)
              .insertEntry(fact.copyWith(name: name)),
        ),
        const Divider(),
        _CommentField(fact: fact),
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
        Operations(entry: fact),
      ],
    );
  }
}

class _CommentField extends HookConsumerWidget {
  final Fact fact;

  const _CommentField({
    Key? key,
    required this.fact,
  }) : super(key: key);

  void _onChanged(String value, WidgetRef ref) {
    ref.read(pageProvider.notifier).insertEntry(fact.copyWith(
          comment: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final textEditingController = useTextEditingController(text: fact.comment);

    return Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
      const SectionTitle(title: "Comment"),
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
          hintText: "Enter a comment",
          suffixIcon: Icon(FontAwesomeIcons.barsStaggered, size: 18),
          contentPadding: EdgeInsets.only(left: 8, top: 12, bottom: 12),
        ),
      ),
    ]);
  }
}

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
        const SectionTitle(title: "Lifetime"),
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
              ref.read(pageProvider.notifier).insertEntry(fact.copyWith(
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
    ref.read(pageProvider.notifier).insertEntry(fact.copyWith(
          data: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SizedBox(height: 8),
        const SectionTitle(title: "Cron Data"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: fact.data,
          onChanged: (value) {
            _onChanged(value, ref);
          },
          inputFormatters: [
            FilteringTextInputFormatter.allow(RegExp(r'[0-9*,\-/ ]')),
          ],
          hintText: "Enter a cron expression",
          icon: FontAwesomeIcons.clockRotateLeft,
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
    ref.read(pageProvider.notifier).insertEntry(fact.copyWith(
          data: value,
        ));
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        const SizedBox(height: 8),
        const SectionTitle(title: "Timed Data"),
        const SizedBox(height: 8),
        SingleLineTextField(
          text: fact.data,
          onChanged: (value) {
            _onChanged(value, ref);
          },
          inputFormatters: [
            FilteringTextInputFormatter.allow(RegExp(r"[0-9a-z ]")),
          ],
          hintText: "Enter a duration",
          icon: FontAwesomeIcons.stopwatch,
        ),
        const SizedBox(height: 8),
      ],
    );
  }
}
