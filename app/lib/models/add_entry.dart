import 'dart:math';

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/pages/page_editor.dart';
import 'package:typewriter/utils/extensions.dart';

part 'add_entry.freezed.dart';

part 'add_entry.g.dart';

const _chars = 'AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890';
final Random _random = Random();

String getRandomString(int length) => String.fromCharCodes(Iterable.generate(
    length, (_) => _chars.codeUnitAt(_random.nextInt(_chars.length))));

@riverpod
List<AddEntry> addEntries(AddEntriesRef ref) {
  final adapterEntries = ref.watch(adapterEntriesProvider);
  return [
    AddEntry(
        title: "Fact",
        description: "Identifier for simple data",
        color: Colors.purple,
        icon: FontAwesomeIcons.fileSignature,
        onAdd: (ref) {
          final id = getRandomString(10);
          ref
              .read(pageProvider)
              .insertFact(ref, FactEntry(id: id, name: "New Fact"));
        }),
    AddEntry(
      title: "Speaker",
      description: "A npc that can speak",
      color: Colors.orange,
      icon: FontAwesomeIcons.userTag,
      onAdd: (ref) {
        final id = getRandomString(10);
        ref
            .read(pageProvider)
            .insertSpeaker(ref, SpeakerEntry(id: id, name: "New Speaker"));
      },
    ),
    for (var e in adapterEntries)
      AddEntry(
        title: e.name.formatted,
        description: e.description,
        color: Colors.grey,
        icon: FontAwesomeIcons.fileImport,
        onAdd: (ref) {
          final entry = DynamicEntry.newEntry(
              id: getRandomString(15),
              name: "New ${e.name.formatted}",
              type: e.name);
          switch (e.type) {
            case EntryType.action:
              ref.read(pageProvider).insertAction(ref, entry);
              break;
            case EntryType.dialogue:
              ref.read(pageProvider).insertDialogue(ref, entry);
              break;
            case EntryType.event:
              ref.read(pageProvider).insertEvent(ref, entry);
              break;
          }
        },
      ),
  ];
}

@freezed
class AddEntry with _$AddEntry {
  const factory AddEntry({
    required String title,
    required String description,
    required Color color,
    required IconData icon,
    required void Function(WidgetRef ref) onAdd,
  }) = _AddEntry;
}
