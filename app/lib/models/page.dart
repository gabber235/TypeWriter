import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/book.dart';
import 'package:typewriter/utils/extensions.dart';

part 'page.freezed.dart';

part 'page.g.dart';

final pagesProvider = Provider<List<Page>>((ref) {
  return ref.watch(bookProvider).pages;
}, dependencies: [bookProvider]);

@freezed
class Page with _$Page {
  const factory Page({
    required String name,
    @Default([]) List<FactEntry> facts,
    @Default([]) List<SpeakerEntry> speakers,
    @Default([]) List<DynamicEntry> events,
    @Default([]) List<DynamicEntry> dialogue,
    @Default([]) List<DynamicEntry> actions,
  }) = _Page;

  factory Page.fromJson(Map<String, dynamic> json) => _$PageFromJson(json);
}

extension PageExtension on Page {
  List<Entry> get entries => [
        ...facts,
        ...speakers,
        ...events,
        ...dialogue,
        ...actions,
      ];

  void updatePage(WidgetRef ref, Page Function(Page) update) {
    final newPage = update(this);
    ref.read(bookProvider.notifier).insertPage(newPage);
  }

  void insertFact(WidgetRef ref, FactEntry fact) {
    updatePage(
        ref,
        (page) => page.copyWith(
            facts: [...page.facts.where((e) => e.id != fact.id), fact]));
  }

  void insertSpeaker(WidgetRef ref, SpeakerEntry speaker) {
    updatePage(
        ref,
        (page) => page.copyWith(speakers: [
              ...page.speakers.where((e) => e.id != speaker.id),
              speaker
            ]));
  }

  void insertEvent(WidgetRef ref, DynamicEntry event) {
    updatePage(
        ref,
        (page) => page.copyWith(
            events: [...page.events.where((e) => e.id != event.id), event]));
  }

  void insertDialogue(WidgetRef ref, DynamicEntry dialogue) {
    updatePage(
        ref,
        (page) => page.copyWith(dialogue: [
              ...page.dialogue.where((e) => e.id != dialogue.id),
              dialogue
            ]));
  }

  void insertAction(WidgetRef ref, DynamicEntry action) {
    updatePage(
        ref,
        (page) => page.copyWith(actions: [
              ...page.actions.where((e) => e.id != action.id),
              action
            ]));
  }
}

abstract class Entry {
  String get id;

  String get name;
}

extension EntryExtension on Entry {
  String get formattedName => name.formatted;
}

@freezed
class FactEntry with _$FactEntry, Entry {
  const factory FactEntry({
    required String id,
    required String name,
    @Default(FactLifetime.permanent) FactLifetime lifetime,
    @Default("") String comment,
    @Default("") String data,
  }) = _FactEntry;

  factory FactEntry.fromJson(Map<String, dynamic> json) =>
      _$FactEntryFromJson(json);
}

enum FactLifetime {
  permanent("Saved permanently, it never gets removed"),
  cron("Saved until a specified date, like \"0 0 * * 1\""),
  timed("Saved for a specified duration, like 20 minutes"),
  session("Saved until a player logouts of the server"),
  ;

  final String description;

  const FactLifetime(this.description);
}

@freezed
class SpeakerEntry with _$SpeakerEntry, Entry {
  const factory SpeakerEntry({
    required String id,
    required String name,
    @Default("") @JsonKey(name: "display_name") String displayName,
    @Default("") String sound,
  }) = _SpeakerEntry;

  factory SpeakerEntry.fromJson(Map<String, dynamic> json) =>
      _$SpeakerEntryFromJson(json);
}

class DynamicEntry with Entry {
  final Map<String, dynamic> data;

  DynamicEntry(this.data);

  DynamicEntry.newEntry(
      {required String id, required String name, required String type})
      : data = {
          "id": id,
          "name": name,
          "type": type,
        };

  factory DynamicEntry.fromJson(Map<String, dynamic> json) {
    return DynamicEntry(json);
  }

  Map<String, dynamic> toJson() {
    return data;
  }

  @override
  String toString() {
    return 'DynamicEntry{data: $data}';
  }

  @override
  String get id => data['id'] as String;

  @override
  String get name => data['name'] as String;
}
