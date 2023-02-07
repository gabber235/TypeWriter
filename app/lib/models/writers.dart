import "dart:convert";

import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "writers.freezed.dart";
part "writers.g.dart";

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

final writersProvider = StateNotifierProvider<WritersNotifier, List<Writer>>(WritersNotifier.new);

class WritersNotifier extends StateNotifier<List<Writer>> {
  WritersNotifier(this.ref) : super([]) {
    ref
      ..listen(currentPageIdProvider, (_, next) => _handleStateChange(pageId: next))
      ..listen(inspectingEntryIdProvider, (_, next) => _handleStateChange(entryId: next))
      ..listen(currentEditingFieldProvider, (_, next) => _handleStateChange(field: next));
  }
  final Ref<dynamic> ref;

  void syncWriters(String data) {
    final self = ref.read(socketProvider)?.id;
    final json = jsonDecode(data) as List<dynamic>;
    // Filter out the current user as we don't want to show ourselves in the list
    state = json.map((e) => Writer.fromJson(e as Map<String, dynamic>)).where((writer) => writer.id != self).toList();
  }

  void _updateSelf(Map<String, dynamic> data) {
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
