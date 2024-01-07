import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/capture.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/models/segment.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/cinematic_view.dart";
import "package:typewriter/widgets/components/general/toasts.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class CaptureHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(String path, FieldInfo field) =>
      (field.getModifier("capture")?.data as String?) != null;

  @override
  HeaderActionLocation location(String path, FieldInfo field) =>
      HeaderActionLocation.actions;

  @override
  Widget build(String path, FieldInfo field) =>
      CaptureHeaderAction(path, field);
}

class CaptureHeaderAction extends HookConsumerWidget {
  const CaptureHeaderAction(
    this.path,
    this.field, {
    super.key,
  });

  final String path;
  final FieldInfo field;

  Future<void> _requestCapture(PassingRef ref) async {
    final captureClassPath = field.getModifier("capture")?.data as String?;
    if (captureClassPath == null) return;

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

    /// ------- Cinematic -------
    final page = ref.read(currentPageProvider);
    if (page == null) {
      Toasts.showError(
        ref,
        "No Page Selected",
        description: "A page must be selected to capture a field.",
      );
      return;
    }

    final cinematic = page.type == PageType.cinematic ? page.pageName : null;

    /// ------- Segment In Out -------
    final segment = ref.read(inspectingSegmentProvider);
    final range = segment?.range;

    final result = await CaptureRequest(
      capturerClassPath: captureClassPath,
      entryId: entryId,
      fieldPath: path,
      fieldValue: value,
      cinematic: cinematic,
      cinematicRange: range,
    ).requestCapture(ref);

    if (result.success) {
      Toasts.showSuccess(
        ref,
        "Capture Successful",
        description: result.message,
      );
    } else {
      Toasts.showError(ref, "Capture Failed", description: result.message);
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Tooltip(
      message: "Capture field",
      child: Material(
        borderRadius: BorderRadius.circular(4),
        color: Colors.blue,
        child: InkWell(
          borderRadius: const BorderRadius.all(Radius.circular(4)),
          onTap: () => _requestCapture(ref.passing),
          child: const Padding(
            padding: EdgeInsets.all(6.0),
            child: FaIcon(
              FontAwesomeIcons.camera,
              size: 16,
              color: Colors.white,
            ),
          ),
        ),
      ),
    );
  }
}
