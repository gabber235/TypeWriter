import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/models/segment.dart";
import "package:typewriter/models/staging.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/cinematic_view.dart";
import "package:typewriter/widgets/components/app/header_button.dart";
import "package:typewriter/widgets/components/general/toasts.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class ContentModeHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, FieldInfo field) =>
      (field.getModifier("contentMode")?.data as String?) != null;

  @override
  HeaderActionLocation location(String path, FieldInfo field) =>
      HeaderActionLocation.actions;

  @override
  Widget build(String path, FieldInfo field) =>
      ContentModeHeaderAction(path, field);
}

class ContentModeHeaderAction extends HookConsumerWidget {
  const ContentModeHeaderAction(
    this.path,
    this.field, {
    super.key,
  });

  final String path;
  final FieldInfo field;

  Future<void> _requestContentMode(PassingRef ref, Header? header) async {
    final contentModeClassPath =
        field.getModifier("contentMode")?.data as String?;
    if (contentModeClassPath == null) return;

    /// ------- Entry ID -------
    final entryId = ref.read(inspectingEntryIdProvider);

    if (entryId == null) {
      Toasts.showError(
        ref,
        "No Entry Selected",
        description: "An entry must be selected to capture a field.",
      );
      return;
    }

    /// ------- Field Value -------
    final value = ref.read(fieldValueProvider(path, null));

    /// ------- Page name -------
    final pageId = ref.read(currentPageIdProvider);
    if (pageId == null) {
      Toasts.showError(
        ref,
        "No Page Selected",
        description: "A page must be selected to capture a field.",
      );
      return;
    }

    final data = {
      "entryId": entryId,
      "pageId": pageId,
      "fieldPath": path,
      "fieldValue": value,
    };

    /// ------- Start & End Frame -------
    final segment = ref.read(inspectingSegmentProvider);
    final range = segment?.range;
    if (range != null) {
      data["startFrame"] = range.from;
      data["endFrame"] = range.to;
    }

    // Publish the changes before requesting the content mode to ensure the
    // latest changes are captured. And all entries are published.
    if (ref.read(stagingStateProvider) == StagingState.staging) {
      await ref.read(communicatorProvider).publish();
      await Future.delayed(const Duration(milliseconds: 500));
    }

    if (header != null && header.canExpand) {
      header.expanded.value = true;
    }

    await ref
        .read(communicatorProvider)
        .requestContentMode(contentModeClassPath, data);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final header = Header.maybeOf(context);
    return HeaderButton(
      tooltip: "Request Content Mode",
      icon: TWIcons.camera,
      onTap: () => _requestContentMode(ref.passing, header),
    );
  }
}
