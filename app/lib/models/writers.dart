import "dart:convert";

import "package:flutter_animate/flutter_animate.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/debouncer.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "writers.freezed.dart";
part "writers.g.dart";

/// Get all the writers that are writhing in a specific field.
/// [path] the given path of the field.
@riverpod
List<Writer> fieldWriters(
  Ref ref,
  String path, {
  bool exact = false,
}) {
  final selectedEntryId = ref.watch(inspectingEntryIdProvider);

  return ref.watch(writersProvider).where((writer) {
    if (writer.entryId.isNullOrEmpty) return false;
    if (writer.entryId != selectedEntryId) return false;
    if (writer.field.isNullOrEmpty) return false;

    if (exact) {
      return writer.field == path;
    } else {
      return writer.field!.startsWith(path);
    }
  }).toList();
}

@freezed
class Writer with _$Writer {
  const factory Writer({
    required String id,
    String? iconUrl,
    String? pageId,
    String? entryId,
    String? field,
  }) = _Writer;

  factory Writer.fromJson(Map<String, dynamic> json) => _$WriterFromJson(json);
}

final writersProvider = StateNotifierProvider<WritersNotifier, List<Writer>>(
  WritersNotifier.new,
  name: "writersProvider",
);

class WritersNotifier extends StateNotifier<List<Writer>> {
  WritersNotifier(this.ref) : super([]) {
    ref
      ..listen(
        currentPageIdProvider,
        (_, next) => _handleStateChange(pageId: next),
      )
      ..listen(
        inspectingEntryIdProvider,
        (_, next) => _handleStateChange(entryId: next),
      )
      ..listen(
        currentEditingFieldProvider,
        (_, next) => _handleStateChange(field: next),
      );

    _debouncer = Debouncer<Map<String, dynamic>>(
      duration: 300.ms,
      callback: _flushUpdateSelf,
    );
  }
  final Ref<dynamic> ref;
  late Debouncer<Map<String, dynamic>> _debouncer;

  @override
  bool updateShouldNotify(List<Writer> old, List<Writer> current) {
    if (old.length != current.length) return true;
    for (var i = 0; i < old.length; i++) {
      if (old[i] != current[i]) return true;
    }
    return false;
  }

  void syncWriters(String data) {
    final self = ref.read(socketProvider)?.id;
    final json = jsonDecode(data) as List<dynamic>;
    // Filter out the current user as we don't want to show ourselves in the list
    state = json
        .map((e) => Writer.fromJson(e as Map<String, dynamic>))
        .where((writer) => writer.id != self)
        .toList();
  }

  void _updateSelf(Map<String, dynamic> data) {
    // We use a debouncer to allow the application to render the ui as writing to the sockets blocks the ui.
    // This is a workaround for the fact that it is not possible to create isolates in flutter web.
    _debouncer.run(data);
  }

  void _flushUpdateSelf(Map<String, dynamic> data) {
    ref.read(communicatorProvider).updateSelfWriter(data);
  }

  void _handleStateChange({
    String? pageId,
    String? entryId,
    String? field,
  }) {
    final localPageId = pageId ?? ref.read(currentPageIdProvider);
    final localEntryId = entryId ?? ref.read(inspectingEntryIdProvider) ?? "";
    final localField = field ?? ref.read(currentEditingFieldProvider) ?? "";

    _updateSelf({
      if (localPageId != null) "pageId": localPageId,
      if (localEntryId.isNotEmpty) "entryId": localEntryId,
      if (localField.isNotEmpty) "field": localField,
    });
  }
}
