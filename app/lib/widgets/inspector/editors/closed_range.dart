import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/headers/info_action.dart";

class ClosedRangeFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "closedRange";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => ClosedRangeEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );

  @override
  (HeaderActions, Iterable<(String, HeaderContext, DataBlueprint)>)
      headerActions(
    Ref<Object?> ref,
    String path,
    DataBlueprint dataBlueprint,
    HeaderContext context,
  ) {
    final customBlueprint = dataBlueprint as CustomBlueprint;
    final shape = customBlueprint.shape;

    final actions = super.headerActions(ref, path, dataBlueprint, context);
    final childContext = context.copyWith(parentBlueprint: dataBlueprint);
    final startActions =
        headerActionsFor(ref, path.join("start"), shape, childContext);
    final endActions =
        headerActionsFor(ref, path.join("end"), shape, childContext);

    return (
      actions.$1.merge(startActions.$1).merge(endActions.$1),
      actions.$2.followedBy(startActions.$2).followedBy(endActions.$2),
    );
  }
}

class ClosedRangeHeaderActionFilter extends HeaderActionFilter {
  @override
  bool shouldShow(
    String path,
    HeaderContext context,
    DataBlueprint dataBlueprint,
  ) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "closedRange";

  @override
  HeaderActionLocation location(
    String path,
    HeaderContext context,
    DataBlueprint dataBlueprint,
  ) =>
      HeaderActionLocation.trailing;

  @override
  Widget build(
    String path,
    HeaderContext context,
    DataBlueprint dataBlueprint,
  ) =>
      InfoHeaderAction(
        tooltip: "From start to end inclusive",
        icon: TWIcons.inclusive,
        color: Color(0xFF0ccf92),
        url: "",
      );
}

class ClosedRangeEditor extends HookConsumerWidget {
  const ClosedRangeEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });
  final String path;

  final CustomBlueprint customBlueprint;

  DataBlueprint get startBlueprint {
    final object = customBlueprint.shape as ObjectBlueprint;
    return object.fields["start"]!;
  }

  DataBlueprint get endBlueprint {
    final object = customBlueprint.shape as ObjectBlueprint;
    return object.fields["end"]!;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return LayoutBuilder(
      builder: (context, constraints) {
        final width = (constraints.maxWidth - 24) / 2;
        return Row(
          children: [
            SizedBox(
              width: width,
              child: FieldEditor(
                path: path.join("start"),
                dataBlueprint: startBlueprint,
              ),
            ),
            const Iconify(TWIcons.range),
            SizedBox(
              width: width,
              child: FieldEditor(
                path: path.join("end"),
                dataBlueprint: endBlueprint,
              ),
            ),
          ],
        );
      },
    );
  }
}
